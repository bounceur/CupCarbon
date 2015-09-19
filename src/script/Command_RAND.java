package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_RAND extends Command {

	protected String arg = "" ;
	
	public Command_RAND(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public int execute() {	
		double rand = Math.random();
		SimLog.add("S" + sensor.getId() + " RAND: "+rand);
		sensor.getScript().addVariable(arg, ""+rand);
		return 0;
	}

	@Override
	public String toString() {
		return "RAND";
	}
	
}
