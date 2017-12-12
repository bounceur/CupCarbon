package senscript;

import java.util.Random;

import device.SensorNode;
import senscript_functions.ScriptFunctions;
import simulation.WisenSimulation;

public class Command_FUNCTION extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	private Random random = new Random();
	
	public Command_FUNCTION(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " CALL FUNCTION "+arg1+"("+arg2+", "+arg3+")");
		String arg0 = arg1;
		String function = arg2;			
		String [] args = arg3.split(",");
		sensor.getScript().variablesToValues(args);
		String [] ret = ScriptFunctions.function(function, args);
		String value = ret[0];
		sensor.getScript().addVariable(arg0, value);
		if(ret[1].equals("")) return 0.0;
		
		double t = (random.nextGaussian() * Double.parseDouble(ret[2])) + Double.parseDouble(ret[1]); 
		
		return t ;
	}

	@Override
	public String toString() {
		return "FUNCTION";
	}
	
}
