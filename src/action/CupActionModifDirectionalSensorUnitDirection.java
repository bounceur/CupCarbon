package action;

import device.SensorNode;
import sensorunit.DirectionalSensorUnit;

public class CupActionModifDirectionalSensorUnitDirection extends CupAction {	
	
	private SensorNode sensorNode;
	private double direction;
	private double cDirection;
	
	public CupActionModifDirectionalSensorUnitDirection(SensorNode sensorNode, double cDirection, double direction) {
		super();
		this.sensorNode = sensorNode;
		this.direction = direction;
		this.cDirection = cDirection;
	}

	@Override
	public void execute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDirection(direction);
	}

	@Override
	public void antiExecute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDirection(cDirection);
	}
}
