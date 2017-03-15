package senscript;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_RDATA extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_RDATA(SensorNode sensor, String [] args) {
		this.sensor = sensor ;		
		this.args = args ;
		//System.out.println(Arrays.toString(args));
	}

	@Override
	public double execute() {
		data = sensor.getScript().getVariableValue(args[1]);
		SimLog.add("S" + sensor.getId() + " Read DATA: "+data);
		String [] tab = data.split("#");
		for(int i=2; i<args.length; i++) {
			try {
				sensor.getScript().addVariable(args[i], tab[i-2]);
			} 
			catch(Exception e) {
				System.err.println("S"+sensor.getId()+" [ERROR RDATA: IDX OUT OF RANGE]!");
			}
		}
		return 0 ;
	}

	@Override
	public String toString() {
		return "RPACKET";
	}
	
}