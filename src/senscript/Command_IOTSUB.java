package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_IOTSUB extends Command {

	private String broker = "";
	private String port = "";
	private String topic = "";
	
	public Command_IOTSUB(SensorNode sensor, String broker, String port, String topic) {
		this.sensor = sensor ;
		this.broker = broker ;
		this.port = port ;
		this.topic = topic ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " IOTSUB ("+broker+" "+port+" "+topic+")");		
		//String part = "";
		
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "PRINT";
	}
	
	@Override
	public String getArduinoForm() {
		return "";
	}

}
