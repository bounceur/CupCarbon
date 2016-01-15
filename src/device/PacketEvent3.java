package device;

public class PacketEvent3 implements Comparable<PacketEvent3> {

	protected int type = 0;
	protected SensorNode sSensor = null;
	protected SensorNode rSensor = null;
	protected String packet = "";
	protected double time = 0;
	
	public PacketEvent3(int type, SensorNode sSensor, SensorNode rSensor, String packet, double time) {
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

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	public SensorNode getRSensor() {
		return rSensor;
	}
	
	public SensorNode getSSensor() {
		return sSensor;
	}

	@Override
	public int compareTo(PacketEvent3 packet) {
		return (time>packet.getTime())?1:(time<packet.getTime())?-1:0;
	}
	
	@Override
	public String toString() {
		//System.out.println(packet+" ["+type+":"+sSensor.getId()+"->"+rSensor.getId()+":"+time+"]");
		return packet+" ["+type+":"+sSensor.getId()+"->"+rSensor.getId()+":"+time+"]";
	}
	
}