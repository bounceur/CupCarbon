package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_TAB extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_TAB(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {		
		String name = arg1;
		String heigth_str = sensor.getScript().getVariableValue(arg2);			
		String width_str = sensor.getScript().getVariableValue(arg3);
		int height = Integer.valueOf(heigth_str);
		int width = Integer.valueOf(width_str);
		sensor.getScript().putTable(name, height,width);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " TABLE "+name+"["+height+"]["+width+"]");
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "TAB";
	}
}
