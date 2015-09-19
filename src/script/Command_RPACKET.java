package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_RPACKET extends Command {

	protected String packet = "";
	protected String [] args = null;
	
	public Command_RPACKET(SensorNode sensor, String [] args) {
		this.sensor = sensor ;		
		this.args = args ;
	}

	@Override
	public int execute() {
		packet = sensor.getScript().getVariableValue(args[1]);
		SimLog.add("S" + sensor.getId() + " Read Packet: "+packet);
		String [] tab = packet.split("#");
		
		for(int i=2; i<args.length; i++) {
			try {
				sensor.getScript().addVariable(args[i], tab[i-2]);
			} 
			catch(Exception e) {
				System.err.println("S"+sensor.getId()+" [ERROR RPACKET]: No packet to read!");
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return "RPACKET";
	}
	
}