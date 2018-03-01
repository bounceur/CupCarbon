package action;

import device.SensorNode;

public class CupActionModifSensorUnitRadius extends CupAction {	
	
	private SensorNode sensorNode;
	private double radius;
	private double cRadius;
	
	public CupActionModifSensorUnitRadius(SensorNode sensorNode, double cRadius, double radius) {
		super();
		this.sensorNode = sensorNode;
		this.radius = radius;
		this.cRadius = cRadius;
	}

	@Override
	public void execute() {
		sensorNode.getSensorUnit().setRadius(radius);
	}

	@Override
	public void antiExecute() {
		sensorNode.getSensorUnit().setRadius(cRadius);
	}

}
