import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLVMGenerator {

    private StringBuilder code = new StringBuilder();
    private int tempCount = 0;
    private int labelCount = 0;
    private Map<String, String> locals = new HashMap<>();
    private Map<String, String> globals = new HashMap<>();
    private Map<String, Integer> procedures = new HashMap<>();
    private Map<String, Integer> functions = new HashMap<>();
    private Deque<String> breakTargets = new ArrayDeque<>();
    private Deque<String> continueTargets = new ArrayDeque<>();
    private boolean currentBlockTerminated = false;
    private boolean currentFunctionHasReturn = false;
    private final boolean wasmMode;

    public LLVMGenerator() {
        this(false);
    }

    public LLVMGenerator(boolean wasmMode) {
        this.wasmMode = wasmMode;
    }

    private String newTemp() {
        return "%t" + (tempCount++);
    }

    private String newLabel(String base) {
        return base + "_" + (labelCount++);
    }

    private void emit(String line) {
        code.append("  ").append(line).append("\n");
    }

    private void emitLabel(String label) {
        code.append(label).append(":\n");
    }

    public String getCode() {
        return code.toString();
    }

    public String generateExpr(ExprNode node) {
        if (node instanceof IntLiteralNode) {
            return Integer.toString(((IntLiteralNode) node).value);
        }

        if (node instanceof VarRefNode) {
            String name = ((VarRefNode) node).name;
            String ptr = resolveVariablePointer(name);

            String temp = newTemp();
            emit(temp + " = load i32, i32* " + ptr);
            return temp;
        }

        if (node instanceof BinaryExprNode) {
            BinaryExprNode bin = (BinaryExprNode) node;

            String left = generateExpr(bin.left);
            String right = generateExpr(bin.right);

            String temp = newTemp();
            String op;

            switch (bin.operator) {
                case "+":
                    op = "add";
                    break;
                case "-":
                    op = "sub";
                    break;
                case "*":
                    op = "mul";
                    break;
                case "/":
                    op = "sdiv";
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + bin.operator);
            }

            emit(temp + " = " + op + " i32 " + left + ", " + right);
            return temp;
        }

        if (node instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) node;
            validateFunctionCall(call);
            String temp = newTemp();
            emit(temp + " = call i32 @" + call.name + "(" + buildCallArgs(call.arguments) + ")");
            return temp;
        }

        throw new RuntimeException("Unsupported expression node: " + node.getClass().getSimpleName());
    }

    public String generateProgram(ProgramNode program) {
        code.setLength(0);
        globals.clear();
        procedures.clear();
        functions.clear();
        labelCount = 0;

        if (wasmMode) {
            code.append("target triple = \"wasm32-unknown-unknown\"\n");
            code.append("declare void @print_i32(i32)\n\n");
        } else {
            code.append("@.fmt = private constant [4 x i8] c\"%d\\0A\\00\"\n");
            code.append("declare i32 @printf(i8*, ...)\n\n");
        }

        generateGlobalVars(program.globalVars);
        if (!program.globalVars.isEmpty()) {
            code.append("\n");
        }

        registerSubprograms(program);

        for (ProcedureDefNode procedure : program.procedures) {
            generateProcedure(procedure);
            code.append("\n");
        }

        for (FunctionDefNode function : program.functions) {
            generateFunction(function);
            code.append("\n");
        }

        generateMain(program);

        return code.toString();
    }

    private void generateMain(ProgramNode program) {
        resetFunctionState();
        code.append("define i32 @main() {\n");
        code.append("entry:\n");

        generateBlock(program.mainBlock);

        emit("ret i32 0");
        code.append("}\n");
    }

    private void generateGlobalVars(List<VarDeclNode> globalVars) {
        for (VarDeclNode varDecl : globalVars) {
            String ptr = "@" + varDecl.name;
            if (globals.containsKey(varDecl.name)) {
                throw new RuntimeException("Duplicate global variable: " + varDecl.name);
            }
            globals.put(varDecl.name, ptr);
            code.append(ptr).append(" = global i32 0\n");
        }
    }

    private void registerSubprograms(ProgramNode program) {
        for (ProcedureDefNode procedure : program.procedures) {
            if (procedures.containsKey(procedure.name) || functions.containsKey(procedure.name)) {
                throw new RuntimeException("Duplicate subprogram: " + procedure.name);
            }
            procedures.put(procedure.name, procedure.parameterNames.size());
        }

        for (FunctionDefNode function : program.functions) {
            if (procedures.containsKey(function.name) || functions.containsKey(function.name)) {
                throw new RuntimeException("Duplicate subprogram: " + function.name);
            }
            functions.put(function.name, function.parameterNames.size());
        }
    }

    private void generateVarDecl(VarDeclNode node) {
        String ptr = "%" + node.name;
        locals.put(node.name, ptr);
        emit(ptr + " = alloca i32");
    }

    private void generateProcedure(ProcedureDefNode node) {
        resetFunctionState();
        code.append("define void @").append(node.name)
                .append("(").append(buildParamSignature(node.parameterNames)).append(") {\n");
        code.append("entry:\n");

        setupParameters(node.parameterNames);
        generateBlock(node.body);

        if (!currentBlockTerminated) {
            emit("ret void");
        }

        code.append("}\n");
    }

    private void generateFunction(FunctionDefNode node) {
        resetFunctionState();
        code.append("define i32 @").append(node.name)
                .append("(").append(buildParamSignature(node.parameterNames)).append(") {\n");
        code.append("entry:\n");

        setupParameters(node.parameterNames);
        generateBlock(node.body);

        if (!currentFunctionHasReturn) {
            throw new RuntimeException("Function did not return a value: " + node.name);
        }

        if (!currentBlockTerminated) {
            emit("ret i32 0");
        }

        code.append("}\n");
    }

    private void generateBlock(BlockNode block) {
        for (Object stmt : block.statements) {
            if (currentBlockTerminated) {
                break;
            }
            generateStmt(stmt);
        }
    }

    private void generateStmt(Object node) {
        if (node instanceof AssignNode) {
            AssignNode assign = (AssignNode) node;
            String ptr = resolveVariablePointer(assign.targetName);

            String value = generateExpr(assign.expression);
            emit("store i32 " + value + ", i32* " + ptr);
            return;
        }

        if (node instanceof ProcedureCallNode) {
            ProcedureCallNode call = (ProcedureCallNode) node;
            validateProcedureCall(call);
            emit("call void @" + call.name + "(" + buildCallArgs(call.arguments) + ")");
            return;
        }

        if (node instanceof WhileNode) {
            WhileNode whileNode = (WhileNode) node;

            String condLabel = newLabel("while_cond");
            String bodyLabel = newLabel("while_body");
            String endLabel = newLabel("while_end");

            emit("br label %" + condLabel);
            emitLabel(condLabel);

            String condValue = generateExpr(whileNode.condition);
            String cmpTemp = newTemp();
            emit(cmpTemp + " = icmp ne i32 " + condValue + ", 0");
            emit("br i1 " + cmpTemp + ", label %" + bodyLabel + ", label %" + endLabel);

            emitLabel(bodyLabel);
            breakTargets.push(endLabel);
            continueTargets.push(condLabel);
            currentBlockTerminated = false;

            generateBlock(whileNode.body);

            continueTargets.pop();
            breakTargets.pop();

            if (!currentBlockTerminated) {
                emit("br label %" + condLabel);
            }

            emitLabel(endLabel);
            currentBlockTerminated = false;
            return;
        }

        if (node instanceof ForNode) {
            ForNode forNode = (ForNode) node;
            String ptr = resolveVariablePointer(forNode.loopVariableName);

            String startVal = generateExpr(forNode.startExpr);
            String endVal = generateExpr(forNode.endExpr);

            String condLabel = newLabel("for_cond");
            String bodyLabel = newLabel("for_body");
            String incLabel = newLabel("for_inc");
            String endLabel = newLabel("for_end");

            emit("store i32 " + startVal + ", i32* " + ptr);
            emit("br label %" + condLabel);

            emitLabel(condLabel);
            String currentVal = newTemp();
            emit(currentVal + " = load i32, i32* " + ptr);
            String cmpTemp = newTemp();
            emit(cmpTemp + " = icmp sle i32 " + currentVal + ", " + endVal);
            emit("br i1 " + cmpTemp + ", label %" + bodyLabel + ", label %" + endLabel);

            emitLabel(bodyLabel);
            breakTargets.push(endLabel);
            continueTargets.push(incLabel);
            currentBlockTerminated = false;

            generateBlock(forNode.body);

            continueTargets.pop();
            breakTargets.pop();

            if (!currentBlockTerminated) {
                emit("br label %" + incLabel);
            }

            emitLabel(incLabel);
            String loaded = newTemp();
            emit(loaded + " = load i32, i32* " + ptr);
            String next = newTemp();
            emit(next + " = add i32 " + loaded + ", 1");
            emit("store i32 " + next + ", i32* " + ptr);
            emit("br label %" + condLabel);

            emitLabel(endLabel);
            currentBlockTerminated = false;
            return;
        }

        if (node instanceof BreakNode) {
            if (breakTargets.isEmpty()) {
                throw new RuntimeException("break used outside loop");
            }
            emit("br label %" + breakTargets.peek());
            currentBlockTerminated = true;
            return;
        }

        if (node instanceof ContinueNode) {
            if (continueTargets.isEmpty()) {
                throw new RuntimeException("continue used outside loop");
            }
            emit("br label %" + continueTargets.peek());
            currentBlockTerminated = true;
            return;
        }

        if (node instanceof ReturnNode) {
            ReturnNode ret = (ReturnNode) node;
            String value = generateExpr(ret.expression);
            emit("ret i32 " + value);
            currentFunctionHasReturn = true;
            currentBlockTerminated = true;
            return;
        }

        if (node instanceof WriteLnNode) {
            WriteLnNode writeLn = (WriteLnNode) node;
            String value = generateExpr(writeLn.expression);
            if (wasmMode) {
                emit("call void @print_i32(i32 " + value + ")");
            } else {
                String temp = newTemp();
                emit(temp + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds " +
                        "([4 x i8], [4 x i8]* @.fmt, i32 0, i32 0), i32 " + value + ")");
            }
            return;
        }

        throw new RuntimeException("Unsupported statement node: " + node.getClass().getSimpleName());
    }

    private void resetFunctionState() {
        locals.clear();
        tempCount = 0;
        currentBlockTerminated = false;
        currentFunctionHasReturn = false;
    }

    private String buildParamSignature(List<String> parameterNames) {
        StringBuilder params = new StringBuilder();

        for (int i = 0; i < parameterNames.size(); i++) {
            if (i > 0) {
                params.append(", ");
            }
            params.append("i32 %").append(parameterNames.get(i)).append("_param");
        }

        return params.toString();
    }

    private void setupParameters(List<String> parameterNames) {
        for (String paramName : parameterNames) {
            String ptr = "%" + paramName;
            locals.put(paramName, ptr);
            emit(ptr + " = alloca i32");
            emit("store i32 %" + paramName + "_param, i32* " + ptr);
        }
    }

    private String buildCallArgs(List<ExprNode> arguments) {
        StringBuilder args = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                args.append(", ");
            }

            String value = generateExpr(arguments.get(i));
            args.append("i32 ").append(value);
        }

        return args.toString();
    }

    private String resolveVariablePointer(String name) {
        String ptr = locals.get(name);
        if (ptr != null) {
            return ptr;
        }

        ptr = globals.get(name);
        if (ptr != null) {
            return ptr;
        }

        throw new RuntimeException("Unknown variable: " + name);
    }

    private void validateProcedureCall(ProcedureCallNode call) {
        Integer expectedCount = procedures.get(call.name);
        if (expectedCount == null) {
            throw new RuntimeException("Unknown procedure: " + call.name);
        }

        if (call.arguments.size() != expectedCount) {
            throw new RuntimeException("Procedure " + call.name + " expects " +
                    expectedCount + " arguments but got " + call.arguments.size());
        }
    }

    private void validateFunctionCall(FunctionCallNode call) {
        Integer expectedCount = functions.get(call.name);
        if (expectedCount == null) {
            throw new RuntimeException("Unknown function: " + call.name);
        }

        if (call.arguments.size() != expectedCount) {
            throw new RuntimeException("Function " + call.name + " expects " +
                    expectedCount + " arguments but got " + call.arguments.size());
        }
    }
}
