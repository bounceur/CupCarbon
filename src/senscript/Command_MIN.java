package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_MIN extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_MIN(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {		
		WisenSimulation.simLog.add("S" + sensor.getId() + " MIN");
		String arg1s = sensor.getScript().getVariableValue(arg2);
		String arg2s = sensor.getScript().getVariableValue(arg3);
		if(Double.valueOf(arg1s) > Double.valueOf(arg2s)){
			sensor.getScript().addVariable(arg1, arg2s);
		}
		else {
			sensor.getScript().addVariable(arg1, arg1s);
		}
		return 0 ;		
	}

	@Override
	public String toString() {
		return "MIN";
	}
	
}
