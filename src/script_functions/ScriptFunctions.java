package script_functions;

public class ScriptFunctions {

	public static String [] function(String function, String [] args) {
		
		// Sensor Min -> Return the min with the name of the sensor
		if(function.equals("smin")) {
			return new String [] {Functions.smin(args), "0.00026276", "0.00000162419"};
		}
		
		if(function.equals("min")) {
			return new String [] {Functions.min(args), "0.0", "0.0"};
		}
		
		if(function.equals("myf")) {
			return new String [] {Functions.myf(args), "0.0", "0.0"};
		}
		
		if(function.equals("angle")) {
			return new String [] {Functions.angle(args), "0.0", "0.0"};
		}
		
		if(function.equals("check")) {
			return new String [] {Functions.check(args), "0.0", "0.0"};
		}
		
		if(function.equals("fmu")) {
			return new String [] {Functions.fmu(args), "0.0", "0.0"};
		}
		
		if(function.equals("fsigma")) {
			return new String [] {Functions.fsigma(args), "0.0", "0.0"};
		}
		
		if(function.equals("factor")) {
			return new String [] {Functions.factor(args), "0.0", "0.0"};
		}
				
		return new String [] {"[SCRIPT] FUNCTION ERROR: Unknown function!","0.0", "0.0"};
	}
	
}
