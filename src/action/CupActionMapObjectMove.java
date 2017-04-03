package action;

import device.MapObject;

public class CupActionMapObjectMove extends CupAction {	
	
	private MapObject mapObject;
	private double longitude;
	private double latitude;
	private double elevation;
	private double pLongitude;
	private double pLatitude;
	private double pElevation;
	
	public CupActionMapObjectMove(MapObject mapObject) {
		super();
		this.mapObject = mapObject;
		this.pLongitude = mapObject.getPrevLongitude();
		this.pLatitude = mapObject.getPrevLatitude();
		this.pElevation = mapObject.getPrevElevation();
		this.longitude = mapObject.getLongitude();
		this.latitude = mapObject.getLatitude();
		this.elevation = mapObject.getElevation();
	}

	@Override
	public void execute() {
		mapObject.gpsMoveTo(longitude, latitude, elevation);
	}

	@Override
	public void antiExecute() {
		mapObject.gpsMoveTo(pLongitude, pLatitude, pElevation);
	}

}
