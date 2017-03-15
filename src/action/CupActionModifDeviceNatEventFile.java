package action;

import cupcarbon.CupCarbon;
import device.Device;

public class CupActionModifDeviceNatEventFile extends CupAction {	
	
	private Device device;
	private String natEventFile;
	private String cNatEventFile;
	
	public CupActionModifDeviceNatEventFile(Device device, String cNatEventFile, String natEventFile) {
		super();
		this.device = device;
		this.natEventFile = natEventFile;
		this.cNatEventFile = cNatEventFile;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setNatEventFileName(natEventFile);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setNatEventFileName(cNatEventFile);
	}

}
