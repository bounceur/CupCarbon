package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_WLAST extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_WLAST(SensorNode sensor, String [] args) {
		this.sensor = sensor ;
		this.args = args ;
	}

	@Override
	public double execute() {
		String symbole = "#";
		if(args[1].charAt(0)=='!') {
			symbole = "" + args[1].charAt(1);
			data = sensor.getScript().getVariableValue(args[3]);			
		}
		else
			data = sensor.getScript().getVariableValue(args[2]);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " WITHOUT LAST: "+data);
		String ret = data.substring(0, data.lastIndexOf(symbole));
		int i = 1;
		if(args[1].charAt(0)=='!') {
			i=2;
		}
		
		sensor.getScript().addVariable(args[i], ret);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "WLAST";
	}
	
}