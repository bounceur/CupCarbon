package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorDrift extends CupAction {	
	
	private SensorNode sensorNode;
	private double drift;
	private double cDrift;
	
	public CupActionModifSensorDrift(SensorNode sensorNode, double cDrift, double drift) {
		super();
		this.sensorNode = sensorNode;
		this.drift = drift;
		this.cDrift = cDrift;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setSigmaOfDriftTime(drift);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setSigmaOfDriftTime(cDrift);
	}

}
