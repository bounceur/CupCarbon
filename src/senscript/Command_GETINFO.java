package senscript;

import device.Device;
import device.DeviceList;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_GETINFO extends Command {

	protected String arg = "" ;
	
	public Command_GETINFO(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {

		WisenSimulation.simLog.add("S" + sensor.getId() + " GET INFO.");
		
		for (Device device : DeviceList.devices) {
			if(device.getType() == Device.MOBILE) {	
				if (sensor.detect(device)) {
					sensor.getScript().addVariable(arg, device.getId()+"#"+device.getLatitude()+"#"+device.getLongitude());
				}
				else
					sensor.getScript().addVariable(arg, "0");
			}
		}
		return 0 ;
	}

	@Override
	public String toString() {
		return "GETINFO";
	}

}
