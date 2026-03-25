import java.util.HashMap;
import java.util.Map;

public class Scope {

	private final Map<String, Object> values;
	private final Scope parent;

	public Scope(Scope parent) {
		this.parent = parent;
		this.values = new HashMap<>();
	}

	public Scope getParent() {
		return parent;
	}

	public void declare(String name, Object value) {
		values.put(name, value);
	}

	public boolean containsLocal(String name) {
		return values.containsKey(name);
	}

	public boolean contains(String name) {
		if (values.containsKey(name)) {
			return true;
		}
		if (parent != null) {
			return parent.contains(name);
		}
		return false;
	}

	public Object resolve(String name) {
		if (values.containsKey(name)) {
			return values.get(name);
		}
		if (parent != null) {
			return parent.resolve(name);
		}
		throw new RuntimeException("Unknown variable: " + name);
	}

	public void assign(String name, Object value) {
		if (values.containsKey(name)) {
			values.put(name, value);
			return;
		}
		if (parent != null) {
			parent.assign(name, value);
			return;
		}
		throw new RuntimeException("Unknown variable: " + name);
	}
}
