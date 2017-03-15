package senscript;

import wisen_simulation.SimLog;

import java.util.Arrays;

import device.SensorNode;

public class Command_PRINTFILE extends Command {

protected String [] arg ;
	
	public Command_PRINTFILE(SensorNode sensor, String [] arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " PRINTF "+Arrays.toString(arg));
		String message = "";
		String part = "";
		for (int i=1; i<arg.length; i++) {
			part = sensor.getScript().getVariableValue(arg[i]); 
			message += part + " ";
		}
		sensor.getScript().printFile(message);
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "PRINTF";
	}
}
