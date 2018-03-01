package senscript;

import device.SensorNode;

public class Command_GOTO extends Command {
	
	protected String arg ;
		
	public Command_GOTO(SensorNode sensor, String arg) {
		this.sensor = sensor ; 					
		this.arg = arg ;
	}
	
	@Override
	public double execute() {
		//WisenSimulation.simLog.add("S" + sensor.getId() + " GOTO.");
		if(arg.equals("++")) {
			sensor.getScript().next();
			return 0 ;
		}
		if(arg.equals("--")) {
			sensor.getScript().previous();
			return 0 ;
		}
		String idxStr = sensor.getScript().getVariableValue(arg);
		int idx;
		if(Character.isDigit(idxStr.charAt(0))) {
			idx = Integer.valueOf(idxStr);
			sensor.getScript().setIndex(idx-2);
			return 0 ;
		}
		idx = sensor.getScript().getLineOfLabel(arg);
		sensor.getScript().setIndex(idx-2);
		return 0 ;
	}
			
	@Override
	public String toString() {
		return "GOTO";
	}
}
