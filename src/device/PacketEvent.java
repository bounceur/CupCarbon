package device;

public class PacketEvent implements Comparable<PacketEvent> {

	protected int type = 0;
	protected SensorNode sSensor = null;
	protected SensorNode rSensor = null;
	protected String packet = "";
	protected long time = 0;
	
	public PacketEvent(int type, SensorNode sSensor, SensorNode rSensor, String packet, long time) {
		super();
		this.type = type ;
		this.sSensor = sSensor ;
		this.rSensor = rSensor ;
		this.packet = packet;
		this.time = time;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	
	public SensorNode getRSensor() {
		return rSensor;
	}
	
	public SensorNode getSSensor() {
		return sSensor;
	}

	@Override
	public int compareTo(PacketEvent packet) {
		return (time>packet.getTime())?1:(time<packet.getTime())?-1:0;
	}
	
	@Override
	public String toString() {
		return packet+" ["+type+":"+sSensor.getId()+"->"+rSensor.getId()+":"+time+"]";
	}
	
}
