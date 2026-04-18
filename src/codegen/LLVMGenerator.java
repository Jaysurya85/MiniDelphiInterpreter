import java.util.HashMap;
import java.util.Map;

public class LLVMGenerator {

    private StringBuilder code = new StringBuilder();
    private int tempCount = 0;
    private Map<String, String> locals = new HashMap<>();

    private String newTemp() {
        return "%t" + (tempCount++);
    }

    private void emit(String line) {
        code.append("  ").append(line).append("\n");
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
            String ptr = locals.get(name);

            if (ptr == null) {
                throw new RuntimeException("Unknown variable: " + name);
            }

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

        throw new RuntimeException("Unsupported expression node: " + node.getClass().getSimpleName());
    }

    public String generateProgram(ProgramNode program) {
        code.setLength(0);
        tempCount = 0;
        locals.clear();

        if (!program.procedures.isEmpty() || !program.functions.isEmpty()) {
            throw new RuntimeException("Procedures and functions are not supported in LLVM generation yet");
        }

        code.append("define i32 @main() {\n");
        code.append("entry:\n");

        for (VarDeclNode varDecl : program.globalVars) {
            generateVarDecl(varDecl);
        }

        generateBlock(program.mainBlock);

        emit("ret i32 0");
        code.append("}\n");

        return code.toString();
    }

    private void generateVarDecl(VarDeclNode node) {
        String ptr = "%" + node.name;
        locals.put(node.name, ptr);
        emit(ptr + " = alloca i32");
    }

    private void generateBlock(BlockNode block) {
        for (Object stmt : block.statements) {
            generateStmt(stmt);
        }
    }

    private void generateStmt(Object node) {
        if (node instanceof AssignNode) {
            AssignNode assign = (AssignNode) node;
            String ptr = locals.get(assign.targetName);

            if (ptr == null) {
                throw new RuntimeException("Unknown variable: " + assign.targetName);
            }

            String value = generateExpr(assign.expression);
            emit("store i32 " + value + ", i32* " + ptr);
            return;
        }

        throw new RuntimeException("Unsupported statement node: " + node.getClass().getSimpleName());
    }
}
