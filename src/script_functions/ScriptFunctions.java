package script_functions;

public class ScriptFunctions {

	public static String function(String function, String [] args) {
		
		// Sensor Min -> Return the min with the name of the sensor
		if(function.equals("smin")) {
			return Functions.smin(args);
		}
		
		if(function.equals("min")) {
			return Functions.min(args);
		}
		
		if(function.equals("myf")) {
			return Functions.myf(args);
		}
		
		if(function.equals("angle")) {
			return Functions.angle(args);
		}
		
		if(function.equals("check")) {
			return Functions.check(args);
		}
				
		return "[SCRIPT] FUNCTION ERROR: Unknown function!";
	}
	
}
