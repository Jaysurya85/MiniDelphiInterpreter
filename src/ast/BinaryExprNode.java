public class BinaryExprNode {

    public final String operator;
    public final Object left;
    public final Object right;

    public BinaryExprNode(String operator, Object left, Object right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}
