package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorId extends CupAction {	
	
	private SensorNode sensorNode;
	private int id;
	private int cId;
	
	public CupActionModifSensorId(SensorNode sensorNode, int cId, int id) {
		super();
		this.sensorNode = sensorNode;
		this.id = id;
		this.cId = cId;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setId(id);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setId(cId);
	}

}
