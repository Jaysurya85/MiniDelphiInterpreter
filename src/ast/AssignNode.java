public class AssignNode {

    public final String targetName;
    public final Object expression;

    public AssignNode(String targetName, Object expression) {
        this.targetName = targetName;
        this.expression = expression;
    }
}
