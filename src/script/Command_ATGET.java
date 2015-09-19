package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_ATGET extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_ATGET(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public int execute() {
		String v = "" ;
		
		if(arg1.equals("id")) {
			SimLog.add("S" + sensor.getId() + " ATGET ID.");
			//System.out.println(sensor.getId());
			v = ""+sensor.getId();
		}
		
		if(arg1.equals("ch")) {
			SimLog.add("S" + sensor.getId() + " ATGET CH.");
			v = ""+sensor.getCh();
		}
		
		if(arg1.equals("my")) {
			SimLog.add("S" + sensor.getId() + " ATGET MY.");
			v = ""+sensor.getMy();
		}
		
		sensor.getScript().addVariable(arg2, v);
		return 0;
	}

	@Override
	public String toString() {
		return "GET";
	}
}
