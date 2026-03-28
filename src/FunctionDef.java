public class FunctionDef {

	public String name;
	public DelphiParser.FunctionDefContext ctx;

	public FunctionDef(String name, DelphiParser.FunctionDefContext ctx) {
		this.name = name;
		this.ctx = ctx;
	}
}
