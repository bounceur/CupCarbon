package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorUnitESensing extends CupAction {	
	
	private SensorNode sensorNode;
	private double consumption;
	private double cConsumption;
	
	public CupActionModifSensorUnitESensing(SensorNode sensorNode, double cConsumption, double consumption) {
		super();
		this.sensorNode = sensorNode;
		this.consumption = consumption;
		this.cConsumption = cConsumption;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.getSensorUnit().setESensing(consumption);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.getSensorUnit().setESensing(cConsumption);
	}

}
