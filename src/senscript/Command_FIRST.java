package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_FIRST extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_FIRST(SensorNode sensor, String [] args) {
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
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " FIRST: "+data);
		String first = data.split(symbole)[0];
		int i = 1;
		if(args[1].charAt(0)=='!') {
			i=2;
		}
		
		sensor.getScript().addVariable(args[i], first);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "FIRST";
	}
	
}