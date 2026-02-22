import java.util.*;

public class InterfaceDef {

	public String name;
	public Set<String> methodNames = new HashSet<>();

	public InterfaceDef(String name) {
		this.name = name;
	}
}
