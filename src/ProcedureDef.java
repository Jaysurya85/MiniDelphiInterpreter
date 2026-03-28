public class ProcedureDef {

	public String name;
	public DelphiParser.ProcedureDefContext ctx;

	public ProcedureDef(String name, DelphiParser.ProcedureDefContext ctx) {
		this.name = name;
		this.ctx = ctx;
	}
}
