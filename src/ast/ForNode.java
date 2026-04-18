public class ForNode {

    public final String loopVariableName;
    public final ExprNode startExpr;
    public final ExprNode endExpr;
    public final BlockNode body;

    public ForNode(String loopVariableName, ExprNode startExpr, ExprNode endExpr, BlockNode body) {
        this.loopVariableName = loopVariableName;
        this.startExpr = startExpr;
        this.endExpr = endExpr;
        this.body = body;
    }
}
