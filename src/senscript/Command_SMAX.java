package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_SMAX extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_SMAX(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {		
		WisenSimulation.simLog.add("S" + sensor.getId() + " SMAX");
		String arg1s = sensor.getScript().getVariableValue(arg2);
		String arg2s = sensor.getScript().getVariableValue(arg3);
		if (Double.valueOf(arg1s.split("#")[1]) > Double.valueOf(arg2s.split("#")[1]))
			sensor.getScript().addVariable(arg1, arg1s);
		else
			sensor.getScript().addVariable(arg1, arg2s);
		return 0 ;		
	}

	@Override
	public String toString() {
		return "SMAX";
	}
	
}
