import java.util.HashMap;
import java.util.Map;

public class ObjectInstance {

	public ClassDef classDef;
	public Map<String, Integer> fields = new HashMap<>();

	public ObjectInstance(ClassDef classDef) {
		this.classDef = classDef;

		initializeFields(classDef);
	}

	private void initializeFields(ClassDef def) {

		if (def.parent != null) {
			initializeFields(def.parent);
		}

		for (String field : def.fields) {
			fields.put(field, 0);
		}
	}

}
