package script;

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
	public long execute() {
		return 0;
	}
	
	@Override
	public String toString() {		
		return "ENDIF";
	}
	
}
