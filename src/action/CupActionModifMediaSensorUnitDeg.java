package action;

import device.SensorNode;
import sensorunit.MediaSensorUnit;

public class CupActionModifMediaSensorUnitDeg extends CupAction {	
	
	private SensorNode sensorNode;
	private double deg;
	private double cDeg;
	
	public CupActionModifMediaSensorUnitDeg(SensorNode sensorNode, double cDeg, double deg) {
		super();
		this.sensorNode = sensorNode;
		this.deg = deg;
		this.cDeg = cDeg;
	}

	@Override
	public void execute() {
		((MediaSensorUnit) sensorNode.getSensorUnit()).setDeg(deg);
	}

	@Override
	public void antiExecute() {
		((MediaSensorUnit) sensorNode.getSensorUnit()).setDeg(cDeg);
	}

}
