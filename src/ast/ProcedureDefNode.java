import java.util.List;

public class ProcedureDefNode {

    public final String name;
    public final List<String> parameterNames;
    public final BlockNode body;

    public ProcedureDefNode(String name, List<String> parameterNames, BlockNode body) {
        this.name = name;
        this.parameterNames = parameterNames;
        this.body = body;
    }
}
