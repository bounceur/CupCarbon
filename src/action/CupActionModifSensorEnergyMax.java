package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorEnergyMax extends CupAction {	
	
	private SensorNode sensorNode;
	private double energy;
	private double cEnergy;
	
	public CupActionModifSensorEnergyMax(SensorNode sensorNode, double cEnergy, double energy) {
		super();
		this.sensorNode = sensorNode;
		this.energy = energy;
		this.cEnergy = cEnergy;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.getBattery().setEMax(energy);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.getBattery().setEMax(cEnergy);
	}

}
