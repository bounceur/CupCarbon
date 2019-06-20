package action;

import device.SensorNode;
import sensorunit.DirectionalSensorUnit;

public class CupActionModifDirectionalSensorUnitDec extends CupAction {	
	
	private SensorNode sensorNode;
	private double dec;
	private double cDec;
	
	public CupActionModifDirectionalSensorUnitDec(SensorNode sensorNode, double cDec, double dec) {
		super();
		this.sensorNode = sensorNode;
		this.dec = dec;
		this.cDec = cDec;
	}

	@Override
	public void execute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDec(dec);
	}

	@Override
	public void antiExecute() {
		((DirectionalSensorUnit) sensorNode.getSensorUnit()).setDec(cDec);
	}
}
