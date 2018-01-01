package action;

import device.Device;
import device.DeviceList;
import natural_events.Weather;

public class CupActionAddDevice extends CupAction {	
	
	private Device device;
	
	public CupActionAddDevice(Device device) {
		super();
		this.device = device;
	}

	@Override
	public void execute() {
		if(device.getClass().equals(Weather.class)) {
			if(DeviceList.weather == null) DeviceList.weather = (Weather) device;			
		}
		DeviceList.add(device);
	}

	@Override
	public void antiExecute() {
		if(device.getClass().equals(Weather.class)) {
			if(DeviceList.weather == null) DeviceList.weather = null;			
		}
		DeviceList.delete(device);
	}

}
