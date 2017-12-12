package action;

import cupcarbon.CupCarbon;
import device.DeviceList;
import device.SensorNode;
import simulation.SimulationInputs;
import visibility.VisibilityZones;

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
		if (SimulationInputs.visibility) {
			VisibilityZones vz = new VisibilityZones(sensorNode);
			vz.run();
		}
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		sensorNode.setElevation(elevation);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		if (SimulationInputs.visibility) {
			VisibilityZones vz = new VisibilityZones(sensorNode);
			vz.run();
		}
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		sensorNode.setElevation(cElevation);
	}

}
