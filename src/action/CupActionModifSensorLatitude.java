package action;

import device.SensorNode;

public class CupActionModifSensorLatitude extends CupAction {	
	
	private SensorNode sensorNode;
	private double latitude;
	private double cLatitude;
	
	public CupActionModifSensorLatitude(SensorNode sensorNode, double cLatitude, double latitude) {
		super();
		this.sensorNode = sensorNode;
		this.latitude = latitude;
		this.cLatitude = cLatitude;
	}

	@Override
	public void execute() {
		sensorNode.setLatitude(latitude);
	}

	@Override
	public void antiExecute() {
		sensorNode.setLatitude(cLatitude);
	}

}
