package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorRadius extends CupAction {	
	
	private SensorNode sensorNode;
	private double radius;
	private double cRadius;
	
	public CupActionModifSensorRadius(SensorNode sensorNode, double cRadius, double radius) {
		super();
		this.sensorNode = sensorNode;
		this.radius = radius;
		this.cRadius = cRadius;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		
		sensorNode.setSelected(true);
		sensorNode.setRadius(radius);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		
		sensorNode.setSelected(true);
		sensorNode.setRadius(cRadius);
	}

}
