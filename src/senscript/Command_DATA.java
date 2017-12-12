package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_DATA extends Command {

	protected String var = "";
	protected String [] args = null;
	protected char symbole = '#';
	
	public Command_DATA(SensorNode sensor, String [] args) {
		this.sensor = sensor ;
		if(args[1].charAt(0) == '!') {
			this.var = args[2] ;
			this.symbole = args[1].charAt(1);
		}
		else {
			this.var = args[1] ;
		}
		this.args = args ;
	}

	@Override
	public double execute() {		
		String packet= "";
		int i = 2;		
		if(args[1].charAt(0) == '!') {
			i=3;
		}
		while(i<args.length-1) {
			packet += sensor.getScript().getVariableValue(args[i]) + symbole;
			i++;
		}		
		packet += sensor.getScript().getVariableValue(args[args.length-1]);
		sensor.getScript().addVariable(var, packet);
		WisenSimulation.simLog.add("S" + sensor.getId() + " DATA Creation:"+packet);
		return 0 ;
	}

	@Override
	public String toString() {
		return "DATA";
	}
	
}
