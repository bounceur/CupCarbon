package action;

import cupcarbon.CupCarbon;
import device.Device;

public class CupActionModifDeviceElevation extends CupAction {	
	
	private Device device;
	private double elevation;
	private double cElevation;
	
	public CupActionModifDeviceElevation(Device device, double cElevation, double elevation) {
		super();
		this.device = device;
		this.elevation = elevation;
		this.cElevation = cElevation;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);		
		
		device.setElevation(elevation);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setElevation(cElevation);
	}

}
