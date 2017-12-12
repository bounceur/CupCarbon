package senscript;

import device.SensorNode;

public class Command_ELSE extends Command {
	
	public Command_ELSE(SensorNode sensor, Command_IF command) {
		this.sensor = sensor ;
		this.currentIf = command ;
	}
	
	public Command_ELSE(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	@Override
	public double execute() {
		//WisenSimulation.simLog.add("S" + sensor.getId() + " ELSE");
		if (currentIf.getRestultOfCondition())
			sensor.getScript().setIndex(currentIf.getEndIfIndex());
		return 0 ;
	}
	
	@Override
	public String getArduinoForm() {
		return "\t} \n else {";
	}

	@Override
	public String toString() {
		return "ELSE";
	}
	
}
