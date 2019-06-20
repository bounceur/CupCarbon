package senscript;

import device.SensorNode;
import markers.Routes;

public class Command_ROUTE extends Command {

	protected String arg1 = "";
	
	public Command_ROUTE(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public double execute() {
		String route2 = sensor.getScript().getVariableValue(arg1);
				
		//String gpsFileName = sensor.getGPSFileName();				
		//String route1 = gpsFileName.substring(0, gpsFileName.indexOf('.'));
		
		int [] idx = Routes.closestIndex2(sensor, Routes.getRouteByName(route2));
		
		System.out.println(sensor.getRouteIndex()+" "+idx[0]+" "+idx[1]);
				
		sensor.loadRouteFrom2Files(idx[0]+1, route2+".gps", idx[1]);
		sensor.setRouteIndex(0);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "ROUTE";
	}
}
