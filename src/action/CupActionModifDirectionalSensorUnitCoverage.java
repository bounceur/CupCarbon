package action;

import device.SensorNode;
import sensorunit.DirectionalSensorUnit;

public class CupActionModifDirectionalSensorUnitCoverage extends CupAction {	
	
	private SensorNode sensorNode;
	private double coverage;
	private double cCoverage;
	
	public CupActionModifDirectionalSensorUnitCoverage(SensorNode sensorNode, double cCoverage, double coverage) {
		super();
		this.sensorNode = sensorNode;
		this.coverage = coverage;
		this.cCoverage = cCoverage;
	}

	@Override
	public void execute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setCoverage(coverage);
	}

	@Override
	public void antiExecute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setCoverage(cCoverage);
	}

}
