public class AssignNode {

    public final String targetName;
    public final ExprNode expression;

    public AssignNode(String targetName, ExprNode expression) {
        this.targetName = targetName;
        this.expression = expression;
    }
}
