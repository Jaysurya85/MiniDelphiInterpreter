import java.util.*;

public class ClassDef {
	public String name;
	public List<String> fields;
	public DelphiParser.ConstructorDeclContext constructor;
	public DelphiParser.DestructorDeclContext destructor;
	public String parentName;
	public ClassDef parent;
	public Map<String, DelphiParser.ProcedureDeclContext> methods;
	public String interfaceName;
	public InterfaceDef implementedInterface;

	public ClassDef(String name,
			List<String> fields,
			DelphiParser.ConstructorDeclContext constructor,
			DelphiParser.DestructorDeclContext destructor,
			String parentName,
			String interfaceName) {

		this.name = name;
		this.fields = fields;
		this.constructor = constructor;
		this.destructor = destructor;
		this.parentName = parentName;
		this.interfaceName = interfaceName;
		this.methods = new HashMap<>();
	}
}
