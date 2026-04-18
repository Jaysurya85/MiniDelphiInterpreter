public class LLVMGenerator {

    private StringBuilder code = new StringBuilder();
    private int tempCount = 0;

    private String newTemp() {
        return "%t" + (tempCount++);
    }

    private void emit(String line) {
        code.append(line).append("\n");
    }

    public String getCode() {
        return code.toString();
    }

    public String generateExpr(ExprNode node) {
        if (node instanceof IntLiteralNode) {
            return Integer.toString(((IntLiteralNode) node).value);
        }

        if (node instanceof VarRefNode) {
            return "%" + ((VarRefNode) node).name;
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
}
