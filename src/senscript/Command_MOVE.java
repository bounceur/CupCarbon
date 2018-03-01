package senscript;

import device.SensorNode;
import map.MapLayer;
import simulation.WisenSimulation;

public class Command_MOVE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	protected String arg3 = "" ;
	protected String arg4 = "" ;
	
	public Command_MOVE(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
	}

	@Override
	public double execute() {
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		String vArg2 = sensor.getScript().getVariableValue(arg2);
		String vArg3 = sensor.getScript().getVariableValue(arg3);
		String vArg4 = sensor.getScript().getVariableValue(arg4);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " MOVE "+vArg1+" "+vArg2+" "+vArg3+" "+vArg4);
		
		double currentLongitude = sensor.getLongitude();
		double currentLatitude = sensor.getLatitude();
		double currentElevation = sensor.getElevation();
		
		double newLongitude = Double.valueOf(vArg1);
		double newLatitude = Double.valueOf(vArg2);
		double newElevation = Double.valueOf(vArg3);
		
		double speed = Double.valueOf(vArg4);
		double v = MapLayer.distance(currentLongitude, currentLatitude, newLongitude, newLatitude)/speed;
		int n = (int) v;
		double d = v - n;
		double stepX = (newLongitude-currentLongitude)/n;
		double stepY = (newLatitude-currentLatitude)/n;
		double stepZ = (newElevation-currentElevation)/n;
		double x = currentLongitude;
		double y = currentLatitude;
		double z = currentElevation;
		sensor.initLocEvents();
		for(int i=1; i<n; i++) {
			x += stepX;
			y += stepY;
			z += stepZ;
			sensor.addLocEvent(1, x, y, z);
		}
		if(d>0)
			sensor.addLocEvent(d, newLongitude, newLatitude, newElevation);
		return 0 ;
	}

	@Override
	public String toString() {
		return "MOVE";
	}
	
}
