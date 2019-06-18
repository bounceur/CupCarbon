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
				
		String gpsFileName = sensor.getGPSFileName();		
		
		String route1 = gpsFileName.substring(0, gpsFileName.indexOf('.'));
		
		//System.out.println(Routes.getRouteByName(route1).getRoute().get(0));
		//System.out.println(Routes.getRouteByName(route2).getRoute().get(0));
		
		//int [] idx = Routes.closestIndex(sensor.getRouteIndex(), Routes.getRouteByName(route1), Routes.getRouteByName(route2));
		
		int [] idx = Routes.closestIndex2(sensor, Routes.getRouteByName(route2));
		
		System.out.println(sensor.getRouteIndex()+" "+idx[0]+" "+idx[1]);
		
		//int [] intersectionIdxs = Routes.getIntersectionIdxs(sensor.getRouteIndex(), Routes.getRouteByName(route1), Routes.getRouteByName(route2));
		
		//int n = Routes.numberOfClosestNodes(sensor.getRouteIndex(), Routes.getRouteByName(route1), closeIdx, Routes.getRouteByName(route2));
		
		//WisenSimulation.simLog.add("S" + sensor.getId() + " ROUTE: "+route2+" "+gpsFileName+" "+route1+" "+closeIdx+" "+n);
		
		//sensor.setRoute(route2);
		
		sensor.loadRouteFrom2Files(route1+".gps", idx[0]+1, route2+".gps", idx[1]);
		sensor.setRouteIndex(0);//sensor.getRouteIndex());
		
		//sensor.setRouteIndex(closeIdx);
		//System.out.println(sensor.getRouteIndex()+" "+closeIdx);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "ROUTE";
	}
}
