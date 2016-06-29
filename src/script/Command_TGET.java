package script;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_TGET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";
	
	public Command_TGET(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
	}

	@Override
	public double execute() {
		String tabName = arg1;
		String x_str = sensor.getScript().getVariableValue(arg2);
		String y_str = sensor.getScript().getVariableValue(arg3);
		String var = arg4;
		
		int x  = Double.valueOf(x_str).intValue();
		int y  = Double.valueOf(y_str).intValue();
		Object[][] tab = sensor.getScript().getTable(tabName);
		String val = (String) tab[x][y];
		sensor.getScript().putVariable(var, val);	
		SimLog.add("S" + sensor.getId() + " GET TABLE VALUE "+tabName+"["+x+"]["+y+"] -> "+val);
		return 0 ;
	}

	@Override
	public String toString() {
		return "TGET";
	}
	
}
