public class WhileNode {

    public final ExprNode condition;
    public final BlockNode body;

    public WhileNode(ExprNode condition, BlockNode body) {
        this.condition = condition;
        this.body = body;
    }
}
