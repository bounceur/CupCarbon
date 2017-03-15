package senscript;

import java.util.Random;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_RGAUSS extends Command {

	protected String arg = "" ;
	
	public Command_RGAUSS(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {	
		Random r = new Random();
		double rand = r.nextGaussian(); 
		SimLog.add("S" + sensor.getId() + " GAUSS RAND: "+rand);
		sensor.getScript().addVariable(arg, ""+rand);
		return 0 ;
	}

	@Override
	public String toString() {
		return "Gaussian-RAND";
	}
	
}
