package device;

public class LocationEvent {

	private double time;
	private double longitude;
	private double latitude;
	private double elevation;
	
	public LocationEvent(double time, double longitude, double latitude, double elevation) {
		super();
		this.time = time;
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getElevation() {
		return elevation;
	}
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	
	@Override
	public String toString() {
		return time + ": " + longitude + " " + latitude + " " + elevation;
	}
	
}
