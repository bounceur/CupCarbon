package senscript;

import device.SensorNode;

public class Command_ENDIF extends Command {
	
	public Command_ENDIF(SensorNode sensor, Command_IF command) {
		this.sensor = sensor ;
		this.currentIf = command ;
	}
	
	public Command_ENDIF(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {
		//WisenSimulation.simLog.add("S" + sensor.getId() + " END IF.");
		return 0 ;
	}
	
	@Override
	public String getArduinoForm() {
		return "}";
	}
	
	@Override
	public String toString() {		
		return "ENDIF";
	}
	
}
