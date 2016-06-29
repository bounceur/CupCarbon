package wisen_simulation;

public class WisenEvent {

	public static final int CLASSIC = 0 ; 
	
	protected int type = CLASSIC ;
	protected long duration ;
	protected WisenAction action;
	
	public WisenEvent(int type, long duration, WisenAction action) {
		super();
		this.type = type;
		this.duration = duration;
		this.action = action;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public WisenAction getAction() {
		return action;
	}

	public void setAction(WisenAction action) {
		this.action = action;
	}
	
	
	
}
