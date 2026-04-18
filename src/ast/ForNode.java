public class ForNode {

    public final String loopVariableName;
    public final Object startExpr;
    public final Object endExpr;
    public final BlockNode body;

    public ForNode(String loopVariableName, Object startExpr, Object endExpr, BlockNode body) {
        this.loopVariableName = loopVariableName;
        this.startExpr = startExpr;
        this.endExpr = endExpr;
        this.body = body;
    }
}
