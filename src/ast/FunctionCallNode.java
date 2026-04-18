import java.util.List;

public class FunctionCallNode extends ExprNode {

    public final String name;
    public final List<ExprNode> arguments;

    public FunctionCallNode(String name, List<ExprNode> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
