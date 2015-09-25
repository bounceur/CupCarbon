package device;

public class Packet implements Comparable<Packet> {

	protected SensorNode sensor = null;
	protected String message = "";
	protected long time = 0;
	
	public Packet(SensorNode sensor, String message, long time) {
		super();
		this.sensor = sensor ;
		this.message = message;
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public SensorNode getSensor() {
		return sensor;
	}

	@Override
	public int compareTo(Packet packet) {
		return (time>packet.getTime())?1:(time<packet.getTime())?-1:0;
	}
	
	@Override
	public String toString() {
		return message+" ["+sensor.getId()+":"+time+"]";
	}
	
}
