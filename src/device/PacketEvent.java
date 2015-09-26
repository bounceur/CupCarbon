package device;

public class PacketEvent implements Comparable<PacketEvent> {

	protected SensorNode sensor = null;
	protected String packet = "";
	protected long time = 0;
	
	public PacketEvent(SensorNode sensor, String packet, long time) {
		super();
		this.sensor = sensor ;
		this.packet = packet;
		this.time = time;
	}

	public String getPacket() {
		return packet;
	}

	public void setPacket(String packet) {
		this.packet = packet;
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
	public int compareTo(PacketEvent packet) {
		return (time>packet.getTime())?1:(time<packet.getTime())?-1:0;
	}
	
	@Override
	public String toString() {
		return packet+" ["+sensor.getId()+":"+time+"]";
	}
	
}
