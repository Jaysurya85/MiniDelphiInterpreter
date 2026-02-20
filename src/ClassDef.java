import java.util.List;

public class ClassDef {
	public String name;
	public List<String> fields;
	public DelphiParser.ConstructorDeclContext constructor;
	public DelphiParser.DestructorDeclContext destructor;

	public ClassDef(String name, List<String> fields,
			DelphiParser.ConstructorDeclContext constructor, DelphiParser.DestructorDeclContext destructor) {
		this.name = name;
		this.fields = fields;
		this.constructor = constructor;
		this.destructor = destructor;
	}
}
