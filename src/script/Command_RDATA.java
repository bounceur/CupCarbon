package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_RDATA extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_RDATA(SensorNode sensor, String [] args) {
		this.sensor = sensor ;		
		this.args = args ;
	}

	@Override
	public long execute() {
		data = sensor.getScript().getVariableValue(args[1]);
		SimLog.add("S" + sensor.getId() + " Read DATA: "+data);
		String [] tab = data.split("#");
		
		for(int i=2; i<args.length; i++) {
			try {
				sensor.getScript().addVariable(args[i], tab[i-2]);
			} 
			catch(Exception e) {
				System.err.println("S"+sensor.getId()+" [ERROR RDATA]: No data to read!");
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return "RPACKET";
	}
	
}