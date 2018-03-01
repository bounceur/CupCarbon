package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RMOVE extends Command {

	protected String arg1 = "" ;
	
	public Command_RMOVE(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " RMOVE");
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		double t = Double.valueOf(vArg1);
		if (!sensor.getGPSFileName().equals("")) {
			sensor.moveToNext(true, 0);									
		}
		return t/1000. ;
	}

	@Override
	public String toString() {
		return "RMOVE";
	}
	
}
