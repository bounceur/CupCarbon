package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_BNOT extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_BNOT(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String s1 = sensor.getScript().getVariableValue(arg2);
		String s = "";
		for(int i =0; i<s1.length(); i++) {
			s += '1'- s1.charAt(i) ;
		}

		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + " = ! (" + Integer.valueOf(s1) + ") -> " + s);
		sensor.getScript().addVariable(arg1, "" + s);
		return 0 ;
	}

	@Override
	public String toString() {
		return "Binary NOT";
	}
}
