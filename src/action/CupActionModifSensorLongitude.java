package action;

import device.SensorNode;

public class CupActionModifSensorLongitude extends CupAction {	
	
	private SensorNode sensorNode;
	private double longitude;
	private double cLongitude;
	
	public CupActionModifSensorLongitude(SensorNode sensorNode, double cLongitude, double longitude) {
		super();
		this.sensorNode = sensorNode;
		this.longitude = longitude;
		this.cLongitude = cLongitude;
	}

	@Override
	public void execute() {
		sensorNode.setLongitude(longitude);
	}

	@Override
	public void antiExecute() {
		sensorNode.setLongitude(cLongitude);
	}

}
