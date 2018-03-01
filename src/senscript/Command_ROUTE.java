package senscript;

import device.SensorNode;
import markers.Routes;
import simulation.WisenSimulation;

public class Command_ROUTE extends Command {

	protected String arg1 = "";
	private boolean once = false;
	
	public Command_ROUTE(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public double execute() {
		String route2 = sensor.getScript().getVariableValue(arg1);
				
		String gpsFileName = sensor.getGPSFileName();
		String route1 = gpsFileName.substring(0, gpsFileName.indexOf('.'));
		
		int closeIdx = Routes.closestIndex(sensor.getRouteIndex(), Routes.getRouteByName(route1), Routes.getRouteByName(route2));
		
		int n = Routes.numberOfClosestNodes(sensor.getRouteIndex(), Routes.getRouteByName(route1), closeIdx, Routes.getRouteByName(route2));
		
		//System.out.println(WisenSimulation.time + " "+sensor.getRoute());
		
		if(!once) {
			//System.out.println(WisenSimulation.time + " "+n);
			executing = true;
			once = true;
			return n;
		}
		//System.out.println(WisenSimulation.time + " "+sensor.getRoute());
		//System.out.println("-------");
		executing = false;
		
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " ROUTE: "+route2+" "+gpsFileName+" "+route1+" "+closeIdx+" "+n);
		
		sensor.setRoute(route2);
		sensor.loadRouteFromFile();
				
		sensor.setRouteIndex(closeIdx);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "ROUTE";
	}
}
