package device;

public class MessageEvent implements Comparable<MessageEvent> {

	protected int type = 0;
	protected SensorNode sSensor = null;
	protected SensorNode rSensor = null;
	protected String message = "";
	protected double time = 0;
	
	public MessageEvent(int type, SensorNode sSensor, SensorNode rSensor, String message, double time) {
		super();
		this.type = type ;
		this.sSensor = sSensor ;
		this.rSensor = rSensor ;
		this.message = message;
		this.time = time;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	public int compareTo(MessageEvent message) {
		return (time>message.getTime())?1:(time<message.getTime())?-1:0;
	}
	
	@Override
	public String toString() {
		return message+" ["+type+":"+sSensor.getId()+"->"+rSensor.getId()+":"+time+"]";
	}
	
}
