package arduino;

import java.util.HashMap;

public class BeginInstructions {

	public static HashMap<String, String> instructions = new HashMap<String, String>();
	
	public static void add(String s) {
		instructions.put(s, s);
	}
	
	public static String get() {
		String s = "";
		for(String alv : instructions.values()) {
			s += "\t"+alv + "\n";
		}
		return s;
	}
	
}
