import java.util.List;

public class ProcedureCallNode {

    public final String name;
    public final List<ExprNode> arguments;

    public ProcedureCallNode(String name, List<ExprNode> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
