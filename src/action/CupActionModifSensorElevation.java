package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorElevation extends CupAction {	
	
	private SensorNode sensorNode;
	private double elevation;
	private double cElevation;
	
	public CupActionModifSensorElevation(SensorNode sensorNode, double cElevation, double elevation) {
		super();
		this.sensorNode = sensorNode;
		this.elevation = elevation;
		this.cElevation = cElevation;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setElevation(elevation);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setElevation(cElevation);
	}

}
