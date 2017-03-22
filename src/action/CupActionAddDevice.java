package action;

import device.Device;
import device.DeviceList;
import natural_events.Meteo;

public class CupActionAddDevice extends CupAction {	
	
	private Device device;
	
	public CupActionAddDevice(Device device) {
		super();
		this.device = device;
	}

	@Override
	public void execute() {
		if(device.getClass().equals(Meteo.class)) {
			if(DeviceList.meteo == null) DeviceList.meteo = (Meteo) device;			
		}
		DeviceList.add(device);
	}

	@Override
	public void antiExecute() {
		if(device.getClass().equals(Meteo.class)) {
			if(DeviceList.meteo == null) DeviceList.meteo = null;			
		}
		DeviceList.delete(device);
	}

}
