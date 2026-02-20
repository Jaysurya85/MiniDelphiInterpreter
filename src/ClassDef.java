import java.util.List;

public class ClassDef {
	public String name;
	public List<String> fields;
	public DelphiParser.ConstructorDeclContext constructor;

	public ClassDef(String name, List<String> fields,
			DelphiParser.ConstructorDeclContext constructor) {
		this.name = name;
		this.fields = fields;
		this.constructor = constructor;
	}
}
