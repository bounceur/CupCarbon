package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorUART extends CupAction {	
	
	private SensorNode sensorNode;
	private long rate;
	private long cRate;
	
	public CupActionModifSensorUART(SensorNode sensorNode, long cRate, long rate) {
		super();
		this.sensorNode = sensorNode;
		this.rate = rate;
		this.cRate = cRate;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setUartDataRate(rate);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setUartDataRate(cRate);
	}

}
