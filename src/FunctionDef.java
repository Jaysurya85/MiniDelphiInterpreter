import java.util.List;

public class FunctionDef {

	public String name;
	public List<String> params;
	public DelphiParser.FunctionDefContext ctx;

	public FunctionDef(String name, List<String> params, DelphiParser.FunctionDefContext ctx) {
		this.name = name;
		this.params = params;
		this.ctx = ctx;
	}
}
