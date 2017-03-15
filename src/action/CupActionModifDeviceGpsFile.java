package action;

import cupcarbon.CupCarbon;
import device.Device;

public class CupActionModifDeviceGpsFile extends CupAction {	
	
	private Device device;
	private String gpsFile;
	private String cGpsFile;
	
	public CupActionModifDeviceGpsFile(Device device, String cGpsFile, String gpsFile) {
		super();
		this.device = device;
		this.gpsFile = gpsFile;
		this.cGpsFile = cGpsFile;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setGPSFileName(gpsFile);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setGPSFileName(cGpsFile);
	}

}
