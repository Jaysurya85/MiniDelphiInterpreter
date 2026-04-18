public class AstPrinter {

    public String print(ProgramNode program) {
        StringBuilder builder = new StringBuilder();
        printProgram(builder, program, 0);
        return builder.toString();
    }

    private void printProgram(StringBuilder builder, ProgramNode program, int indent) {
        line(builder, indent, "Program");
        line(builder, indent + 1, "Globals");
        if (program.globalVars.isEmpty()) {
            line(builder, indent + 2, "<empty>");
        } else {
            for (VarDeclNode varDecl : program.globalVars) {
                line(builder, indent + 2, "VarDecl(" + varDecl.name + ")");
            }
        }

        line(builder, indent + 1, "Procedures");
        if (program.procedures.isEmpty()) {
            line(builder, indent + 2, "<empty>");
        } else {
            for (ProcedureDefNode procedure : program.procedures) {
                printProcedureDef(builder, procedure, indent + 2);
            }
        }

        line(builder, indent + 1, "Functions");
        if (program.functions.isEmpty()) {
            line(builder, indent + 2, "<empty>");
        } else {
            for (FunctionDefNode function : program.functions) {
                printFunctionDef(builder, function, indent + 2);
            }
        }

        line(builder, indent + 1, "Main");
        printBlock(builder, program.mainBlock, indent + 2);
    }

    private void printBlock(StringBuilder builder, BlockNode block, int indent) {
        line(builder, indent, "Block");
        if (block.statements.isEmpty()) {
            line(builder, indent + 1, "<empty>");
            return;
        }

        for (Object statement : block.statements) {
            printNode(builder, statement, indent + 1);
        }
    }

    private void printProcedureDef(StringBuilder builder, ProcedureDefNode node, int indent) {
        line(builder, indent, "ProcedureDef(" + node.name + ")");
        line(builder, indent + 1, "Params: " + node.parameterNames);
        printBlock(builder, node.body, indent + 1);
    }

    private void printFunctionDef(StringBuilder builder, FunctionDefNode node, int indent) {
        line(builder, indent, "FunctionDef(" + node.name + ")");
        line(builder, indent + 1, "Params: " + node.parameterNames);
        printBlock(builder, node.body, indent + 1);
    }

    private void printNode(StringBuilder builder, Object node, int indent) {
        if (node instanceof AssignNode assign) {
            line(builder, indent, "Assign(" + assign.targetName + ")");
            printNode(builder, assign.expression, indent + 1);
            return;
        }
        if (node instanceof WhileNode whileNode) {
            line(builder, indent, "While");
            printNode(builder, whileNode.condition, indent + 1);
            printBlock(builder, whileNode.body, indent + 1);
            return;
        }
        if (node instanceof ForNode forNode) {
            line(builder, indent, "For(" + forNode.loopVariableName + ")");
            line(builder, indent + 1, "Start");
            printNode(builder, forNode.startExpr, indent + 2);
            line(builder, indent + 1, "End");
            printNode(builder, forNode.endExpr, indent + 2);
            printBlock(builder, forNode.body, indent + 1);
            return;
        }
        if (node instanceof BreakNode) {
            line(builder, indent, "Break");
            return;
        }
        if (node instanceof ContinueNode) {
            line(builder, indent, "Continue");
            return;
        }
        if (node instanceof ProcedureCallNode call) {
            line(builder, indent, "ProcedureCall(" + call.name + ")");
            printArguments(builder, call.arguments, indent + 1);
            return;
        }
        if (node instanceof FunctionCallNode call) {
            line(builder, indent, "FunctionCall(" + call.name + ")");
            printArguments(builder, call.arguments, indent + 1);
            return;
        }
        if (node instanceof ReturnNode returnNode) {
            line(builder, indent, "Return");
            printNode(builder, returnNode.expression, indent + 1);
            return;
        }
        if (node instanceof WriteLnNode writeLnNode) {
            line(builder, indent, "WriteLn");
            printNode(builder, writeLnNode.expression, indent + 1);
            return;
        }
        if (node instanceof BinaryExprNode expr) {
            line(builder, indent, "BinaryExpr(" + expr.operator + ")");
            printNode(builder, expr.left, indent + 1);
            printNode(builder, expr.right, indent + 1);
            return;
        }
        if (node instanceof IntLiteralNode literal) {
            line(builder, indent, "IntLiteral(" + literal.value + ")");
            return;
        }
        if (node instanceof VarRefNode varRef) {
            line(builder, indent, "VarRef(" + varRef.name + ")");
            return;
        }
        if (node instanceof BlockNode block) {
            printBlock(builder, block, indent);
            return;
        }

        throw new RuntimeException("Unknown AST node: " + node);
    }

    private void printArguments(StringBuilder builder, java.util.List<Object> arguments, int indent) {
        if (arguments.isEmpty()) {
            line(builder, indent, "Args: []");
            return;
        }

        line(builder, indent, "Args");
        for (Object argument : arguments) {
            printNode(builder, argument, indent + 1);
        }
    }

    private void line(StringBuilder builder, int indent, String text) {
        builder.append("  ".repeat(Math.max(0, indent)))
                .append(text)
                .append(System.lineSeparator());
    }
}
