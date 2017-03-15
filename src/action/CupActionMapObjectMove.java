package action;

import device.MapObject;

public class CupActionMapObjectMove extends CupAction {	
	
	private MapObject mapObject;
	private double longitude;
	private double latitude;
	private double pLongitude;
	private double pLatitude;
	
	public CupActionMapObjectMove(MapObject mapObject) {
		super();
		this.mapObject = mapObject;
		this.pLongitude = mapObject.getPrevLongitude();
		this.pLatitude = mapObject.getPrevLatitude();
		this.longitude = mapObject.getLongitude();
		this.latitude = mapObject.getLatitude();
	}

	@Override
	public void execute() {
		mapObject.gpsMoveTo(longitude, latitude, 0);
	}

	@Override
	public void antiExecute() {
		mapObject.gpsMoveTo(pLongitude, pLatitude, 0);
	}

}
