package senscript;

import java.util.Random;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RANDB extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_RANDB(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String arg = arg1 ;
		int a =  Integer.valueOf(sensor.getScript().getVariableValue(arg2));
		int b =  Integer.valueOf(sensor.getScript().getVariableValue(arg3))+1;
		
		Random r = new Random();
		int rand = a+r.nextInt(b-a);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " RANDB: "+a+" "+b);
		
		sensor.getScript().addVariable(arg,""+rand);
		return 0 ;
	}

	@Override
	public String toString() {
		return "RANDB";
	}
	
}
