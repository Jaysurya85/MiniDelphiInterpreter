import java.util.HashMap;
import java.util.Map;

public class ObjectInstance {

	public ClassDef classDef;
	public Map<String, Integer> fields = new HashMap<>();

	public ObjectInstance(ClassDef classDef) {
		this.classDef = classDef;

		// Initialize all fields to 0
		for (String field : classDef.fields) {
			fields.put(field, 0);
		}
	}
}
