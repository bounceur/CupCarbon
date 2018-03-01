package action;

import cupcarbon.CupCarbon;
import device.Device;

public class CupActionModifDeviceScriptFile extends CupAction {	
	
	private Device device;
	private String scriptFile;
	private String cScriptFile;
	
	public CupActionModifDeviceScriptFile(Device device, String cScriptFile, String scriptFile) {
		super();
		this.device = device;
		this.scriptFile = scriptFile;
		this.cScriptFile = cScriptFile;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setScriptFileName(scriptFile);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		device.setSelected(true);
		
		device.setScriptFileName(cScriptFile);
	}

}
