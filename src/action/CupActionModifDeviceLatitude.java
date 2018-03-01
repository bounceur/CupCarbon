package action;

import device.Device;

public class CupActionModifDeviceLatitude extends CupAction {	
	
	private Device device;
	private double latitude;
	private double cLatitude;
	
	public CupActionModifDeviceLatitude(Device device, double cLatitude, double latitude) {
		super();
		this.device = device;
		this.latitude = latitude;
		this.cLatitude = cLatitude;
	}

	@Override
	public void execute() {
		device.setLatitude(latitude);
	}

	@Override
	public void antiExecute() {
		device.setLatitude(cLatitude);
	}

}
