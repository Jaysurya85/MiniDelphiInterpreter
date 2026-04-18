import java.util.List;

public class FunctionDefNode {

    public final String name;
    public final List<String> parameterNames;
    public final BlockNode body;

    public FunctionDefNode(String name, List<String> parameterNames, BlockNode body) {
        this.name = name;
        this.parameterNames = parameterNames;
        this.body = body;
    }
}
