import java.util.List;

public class ProgramNode {

    public final List<ProcedureDefNode> procedures;
    public final List<FunctionDefNode> functions;
    public final List<VarDeclNode> globalVars;
    public final BlockNode mainBlock;

    public ProgramNode(
            List<ProcedureDefNode> procedures,
            List<FunctionDefNode> functions,
            List<VarDeclNode> globalVars,
            BlockNode mainBlock) {
        this.procedures = procedures;
        this.functions = functions;
        this.globalVars = globalVars;
        this.mainBlock = mainBlock;
    }
}
