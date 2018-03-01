package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_LAST extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_LAST(SensorNode sensor, String [] args) {
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
		WisenSimulation.simLog.add("S" + sensor.getId() + " LAST: "+data);
		String [] t = data.split(symbole);
		int n = t.length;
		int i = 1;
		if(args[1].charAt(0)=='!') {
			i = 2;
		}
		
		sensor.getScript().addVariable(args[i], t[n-1]);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "LAST";
	}
	
}