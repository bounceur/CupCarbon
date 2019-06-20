package action;

import device.SensorNode;
import sensorunit.DirectionalSensorUnit;

public class CupActionModifDirectionalSensorUnitDeg extends CupAction {	
	
	private SensorNode sensorNode;
	private double deg;
	private double cDeg;
	
	public CupActionModifDirectionalSensorUnitDeg(SensorNode sensorNode, double cDeg, double deg) {
		super();
		this.sensorNode = sensorNode;
		this.deg = deg;
		this.cDeg = cDeg;
	}

	@Override
	public void execute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDeg(deg);
	}

	@Override
	public void antiExecute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDeg(cDeg);
	}

}
