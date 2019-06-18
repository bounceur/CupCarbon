package senscript;

import device.DeviceList;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_KILL extends Command {

	private String arg = "";
	
	public Command_KILL(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		double v = Double.valueOf(sensor.getScript().getVariableValue(arg));
		WisenSimulation.simLog.add("S" + sensor.getId() + " KILL -> " + v);
		if(Math.random()<v) {
			sensor.getBattery().setLevel(0);
			DeviceList.calculatePropagations();
		}
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "KILL";
	}
}
