package script;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_SXOR extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_SXOR(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue(arg2);
		String v2 = sensor.getScript().getVariableValue(arg3);
		String z = "";	
		for(int i=0; i<v1.length(); i++) {			
			z += Integer.toHexString(((byte)v1.charAt(i) ^ (byte)v2.charAt(i)));
		}
		SimLog.add("S" + sensor.getId() + " " + arg1 + " = (" + v1 + ") ^ (" + v2 + ") -> " + z);
		sensor.getScript().addVariable(arg1, "" + z.toUpperCase());
		return 0 ;
	}

	@Override
	public String toString() {
		return "SXOR";
	}
	
	public static void main(String [] args) {
		String a = "BA";
		String b = "0C";		
		String s = "";
		for(int i=0; i<a.length(); i++) {			
			s += String.format("%X ", (byte)a.charAt(i) ^ (byte)b.charAt(i)) ;
		}
		System.out.println(s);
	}
}
