package senscript;

import java.util.Random;

import device.SensorNode;
import simulation.WisenSimulation;

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
		WisenSimulation.simLog.add("S" + sensor.getId() + " GAUSS RAND: "+rand);
		sensor.getScript().addVariable(arg, ""+rand);
		return 0 ;
	}

	@Override
	public String toString() {
		return "Gaussian-RAND";
	}
	
}
