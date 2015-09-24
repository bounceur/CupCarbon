package device;

public class Packet implements Comparable<Packet> {

	protected SensorNode sensor = null;
	protected String message = "";
	protected int time = 0;
	
	public Packet(SensorNode sensor, String message, int time) {
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
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
