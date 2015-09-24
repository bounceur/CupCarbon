package device;

public class Packet2 implements Comparable<Packet2> {

	protected String message = "";
	protected int startTime = 0;
	protected int endTime = 0;
	
	public Packet2(String message, int startTime, int endTime) {
		super();
		this.message = message;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	@Override
	public int compareTo(Packet2 packet) {
		if (endTime == packet.getEndTime())
			return (startTime>packet.getEndTime())?1:(startTime<packet.getEndTime())?-1:0;
		return (endTime>packet.getEndTime())?1:(endTime<packet.getEndTime())?-1:0;
	}
	
	@Override
	public String toString() {
		return message+" ["+startTime+", "+endTime+"]";
	}
	
}
