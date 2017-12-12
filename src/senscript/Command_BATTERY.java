package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_BATTERY extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_BATTERY(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}
	
	public Command_BATTERY(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		if(arg2.equals("")) {
			String v = "" ;
			WisenSimulation.simLog.add("S" + sensor.getId() + " BATTERY.");
			v = sensor.getBatteryLevel()+"";
			sensor.getScript().addVariable(arg1, v);			
		}
		else {
			if(arg1.toLowerCase().equals("set")) {
				WisenSimulation.simLog.add("S" + sensor.getId() + " BATTERY SET"+ arg2+".");
				String v = sensor.getScript().getVariableValue(arg2);
				sensor.setBatteryLevel(Double.valueOf(v));
			}
		}
		return 0 ;
	}

	@Override
	public String toString() {
		return "BATTERY";
	}

}
