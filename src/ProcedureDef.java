import java.util.List;

public class ProcedureDef {

	public String name;
	public List<String> params;
	public DelphiParser.ProcedureDefContext ctx;

	public ProcedureDef(String name, List<String> params, DelphiParser.ProcedureDefContext ctx) {
		this.name = name;
		this.params = params;
		this.ctx = ctx;
	}
}
