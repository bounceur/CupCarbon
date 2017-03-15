package action;

import device.Device;

public class CupActionModifDeviceRadius extends CupAction {	
	
	private Device device;
	private double radius;
	private double cRadius;
	
	public CupActionModifDeviceRadius(Device device, double cRadius, double radius) {
		super();
		this.device = device;
		this.radius = radius;
		this.cRadius = cRadius;
	}

	@Override
	public void execute() {
		device.setRadius(radius);
	}

	@Override
	public void antiExecute() {
		device.setRadius(cRadius);
	}

}
