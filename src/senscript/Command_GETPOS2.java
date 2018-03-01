package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_GETPOS2 extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_GETPOS2(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String v1 = "" ;
		String v2 = "" ;
		WisenSimulation.simLog.add("S" + sensor.getId() + " GET POSITION2.");
		
		v1 = sensor.getLongitude()+"";
		v2 = sensor.getLatitude()+"";
		//geoToPixelMap
		//Point2D p = MapCalc.geoToPixelMap(sensor.getLatitude(), sensor.getLongitude());
		sensor.getScript().addVariable(arg1, v1);
		sensor.getScript().addVariable(arg2, v2);
		//sensor.getScript().addVariable(arg1, p.getX()+"");
		//sensor.getScript().addVariable(arg2, p.getY()+"");

		return 0 ;
	}

	@Override
	public String toString() {
		return "GETPOS2";
	}
	
}
