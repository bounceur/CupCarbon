package action;

import device.Device;
import device.DeviceList;

public class CupActionAddDevice extends CupAction {	
	
	private Device device;
	
	public CupActionAddDevice(Device device) {
		super();
		this.device = device;
	}

	@Override
	public void execute() {
		DeviceList.add(device);
	}

	@Override
	public void antiExecute() {
		DeviceList.delete(device);
	}

}
