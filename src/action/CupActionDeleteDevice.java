package action;

import device.Device;
import device.DeviceList;

public class CupActionDeleteDevice extends CupAction {
	
	private Device device;
	
	public CupActionDeleteDevice(Device device) {
		super();
		this.device = device;
	}

	@Override
	public void execute() {
		DeviceList.delete(device);
	}

	@Override
	public void antiExecute() {
		DeviceList.add(device);
	}

}
