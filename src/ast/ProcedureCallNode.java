import java.util.List;

public class ProcedureCallNode {

    public final String name;
    public final List<Object> arguments;

    public ProcedureCallNode(String name, List<Object> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
