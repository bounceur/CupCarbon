package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_AREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_AREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		sensor.getBattery().consume(0.0001);
		String value = sensor.getSensorValues();
		WisenSimulation.simLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
		sensor.getScript().addVariable(arg, value);
		return 0;
	}
	
	@Override
	public String toString() {
		return "AREADSENSOR";
	}
}
