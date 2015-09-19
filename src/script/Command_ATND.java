package script;

import device.SensorNode;

public class Command_ATND extends Command {

	protected String arg1 = "";
	protected String arg2 = "3000" ;
	
	public Command_ATND(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}
	
	public Command_ATND(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg2 ;
	}

	@Override
	public int execute() {
		String args = arg1;
		int n = sensor.getSensorNodeNeighbors().size();
		sensor.getScript().addVariable(args, ""+n);
		return Integer.parseInt(sensor.getScript().getVariableValue(arg2));
	}
	
	@Override
	public String toString() {
		return "ATND";
	}
	
	@Override
	public boolean isSend() {
		return true;
	}
	
	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " ATND: "+sensor.getSensorNodeNeighbors().size());
	}
}
