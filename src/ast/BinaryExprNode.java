public class BinaryExprNode extends ExprNode {

    public final String operator;
    public final ExprNode left;
    public final ExprNode right;

    public BinaryExprNode(String operator, ExprNode left, ExprNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}
