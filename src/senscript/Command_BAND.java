package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_BAND extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_BAND(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String s1 = sensor.getScript().getVariableValue(arg2);
		String s2 = sensor.getScript().getVariableValue(arg3);
		String s = "";
		for(int i =0; i<s1.length(); i++) {
			s += (s1.charAt(i) & s2.charAt(i)) - '0';
		}

		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + " = (" + Integer.valueOf(s1) + ") & (" + Integer.valueOf(s2) + ") -> " + s);
		sensor.getScript().addVariable(arg1, "" + s);
		return 0 ;
	}

	@Override
	public String toString() {
		return "Binary AND";
	}
}
