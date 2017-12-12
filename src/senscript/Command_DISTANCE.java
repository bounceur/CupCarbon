package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_DISTANCE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_DISTANCE(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " DISTANCE");
		String vArg2 = sensor.getScript().getVariableValue(arg2);
		int id = Integer.valueOf(vArg2);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " DISTANCE "+vArg2);
		
		
		double distance = sensor.distance2(id);
		
		sensor.getScript().addVariable(arg1, "" + distance);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "DISTANCE";
	}
	
}
