import java.util.List;

public class FunctionCallNode {

    public final String name;
    public final List<Object> arguments;

    public FunctionCallNode(String name, List<Object> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
