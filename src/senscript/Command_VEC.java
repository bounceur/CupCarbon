package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_VEC extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_VEC(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {		
		String name = arg1;
		String heigth_str = sensor.getScript().getVariableValue(arg2);
		int height = Integer.valueOf(heigth_str);
		sensor.getScript().putVector(name, height);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " VECTOR "+name+"["+height+"]");
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "VEC";
	}
}
