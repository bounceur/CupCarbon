package senscript;

import device.DeviceList;
import device.SensorNode;

public class Command_EDGE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_EDGE(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		int v = Integer.parseInt(sensor.getScript().getVariableValue(arg1));
		int id = Integer.parseInt(sensor.getScript().getVariableValue(arg2));
		SensorNode sensor2 = DeviceList.getSensorNodeById(id);
//		double x1 = sensor.getLatitude();
//		double y1 = sensor.getLongitude();
//		double x2 = sensor2.getLatitude();
//		double y2 = sensor2.getLongitude();
		if(v==1)
			DeviceList.addEdge(sensor, sensor2);
		else
			DeviceList.removeEdge(sensor, sensor2);
		return 0 ;
	}

	@Override
	public String toString() {
		return "EDGE";
	}
	
}
