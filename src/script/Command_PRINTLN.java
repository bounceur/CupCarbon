package script;

import java.util.Arrays;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_PRINTLN extends Command {

	protected String [] arg ;
	
	public Command_PRINTLN(SensorNode sensor, String [] arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		SimLog.add("S" + sensor.getId() + " PRINTLN "+Arrays.toString(arg));
		String message = "";
		System.out.print("S" + sensor.getId() + " >> ");
		for (int i=1; i<arg.length; i++) {
			message = sensor.getScript().getVariableValue(arg[i]);
			System.out.print(message+" ");
		}
		System.out.println();
		return 0;
	}
	
	@Override
	public String toString() {
		return "PRINTLN";
	}
}
