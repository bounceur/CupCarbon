package senscript_functions;

public class ScriptFunctions {

	public static String [] function(String function, String [] args) {
		
		if(function.equals("mysum")) {
			return new String [] {Functions.mysum(args), "0.0", "0.0"};
		}
		
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
		
		if(function.equals("encrypt")) {
			try {
				return new String [] {Functions.encrypt(args), "0.027743", "0.000000000000000019092"};
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(function.equals("decrypt")) {
			try {
				return new String [] {Functions.decrypt(args), "0.026918", "0.000000000000000051721"};
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(function.equals("hash")) {
			try {
				return new String [] {Functions.hash(args), "0.000078", "0.0000000000000000000040678"};
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(function.equals("mod")) {
			try {
				return new String [] {Functions.mod(args), "0.00026", "0.0000000000000000051721"};
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		return new String [] {"[SCRIPT] FUNCTION ERROR: Unknown function!","0.0", "0.0"};
	}
	
}
