public class WhileNode {

    public final Object condition;
    public final BlockNode body;

    public WhileNode(Object condition, BlockNode body) {
        this.condition = condition;
        this.body = body;
    }
}
