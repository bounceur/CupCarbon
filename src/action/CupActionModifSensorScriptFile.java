package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorScriptFile extends CupAction {	
	
	private SensorNode sensorNode;
	private String scriptFile;
	private String cScriptFile;
	
	public CupActionModifSensorScriptFile(SensorNode sensorNode, String cScriptFile, String scriptFile) {
		super();
		this.sensorNode = sensorNode;
		this.scriptFile = scriptFile;
		this.cScriptFile = cScriptFile;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setScriptFileName(scriptFile);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setScriptFileName(cScriptFile);
	}

}
