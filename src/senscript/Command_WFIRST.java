package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_WFIRST extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_WFIRST(SensorNode sensor, String [] args) {
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
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " WITHOUT FIRST: "+data);
		String ret = data.substring(data.indexOf(symbole)+1);
		int i = 1;
		if(args[1].charAt(0)=='!') {
			i=2;
		}
		
		sensor.getScript().addVariable(args[i], ret);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "WFIRST";
	}
	
//	public static void main(String [] args) {
//		String s = "a-b-c-d-e";
//		System.out.println(s.substring(s.indexOf("-")+1));
//		System.out.println(s.substring(0,s.lastIndexOf("-")));
//	}
	
}