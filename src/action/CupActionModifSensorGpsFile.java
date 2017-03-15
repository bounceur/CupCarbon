package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorGpsFile extends CupAction {	
	
	private SensorNode sensorNode;
	private String gpsFile;
	private String cGpsFile;
	
	public CupActionModifSensorGpsFile(SensorNode sensorNode, String cGpsFile, String gpsFile) {
		super();
		this.sensorNode = sensorNode;
		this.gpsFile = gpsFile;
		this.cGpsFile = cGpsFile;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setGPSFileName(gpsFile);
	}

	@Override
	public void antiExecute() {		
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setGPSFileName(cGpsFile);
	}

}
