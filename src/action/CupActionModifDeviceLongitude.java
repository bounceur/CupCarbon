package action;

import device.Device;

public class CupActionModifDeviceLongitude extends CupAction {	
	
	private Device device;
	private double longitude;
	private double cLongitude;
	
	public CupActionModifDeviceLongitude(Device device, double cLongitude, double longitude) {
		super();
		this.device = device;
		this.longitude = longitude;
		this.cLongitude = cLongitude;
	}

	@Override
	public void execute() {
		device.setLongitude(longitude);
	}

	@Override
	public void antiExecute() {
		device.setLongitude(cLongitude);
	}

}
