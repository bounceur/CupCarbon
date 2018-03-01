package action;

import device.SensorNode;
import sensorunit.MediaSensorUnit;

public class CupActionModifMediaSensorUnitDec extends CupAction {	
	
	private SensorNode sensorNode;
	private double dec;
	private double cDec;
	
	public CupActionModifMediaSensorUnitDec(SensorNode sensorNode, double cDec, double dec) {
		super();
		this.sensorNode = sensorNode;
		this.dec = dec;
		this.cDec = cDec;
	}

	@Override
	public void execute() {
		((MediaSensorUnit) sensorNode.getSensorUnit()).setDec(dec);
	}

	@Override
	public void antiExecute() {
		((MediaSensorUnit) sensorNode.getSensorUnit()).setDec(cDec);
	}
}
