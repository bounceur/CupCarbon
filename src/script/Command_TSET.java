package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_TSET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";
	
	public Command_TSET(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
	}

	@Override
	public long execute() {
		String tabName = arg1;
		String x_str = sensor.getScript().getVariableValue(arg2);
		String y_str = sensor.getScript().getVariableValue(arg3);
		String var = sensor.getScript().getVariableValue(arg4);			
		int x  = Double.valueOf(x_str).intValue();
		int y  = Double.valueOf(y_str).intValue();
		Object [][] tab = sensor.getScript().getTable(tabName);
		tab[x][y] = var;
		SimLog.add("S" + sensor.getId() + " SET TABLE VALUE "+tabName+"["+x+"]["+y+"] = "+var);
		return 0;
	}

	@Override
	public String toString() {
		return "TSET";
	}
	
}
