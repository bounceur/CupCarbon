package simulation;

public class WisenEvent {

	public static final int CLASSIC = 0 ; 
	
	protected int type = CLASSIC ;
	protected long end ;
	protected WisenAction action;
	
	public WisenEvent(int type, long end, WisenAction action) {
		super();
		this.type = type;
		this.end = end;
		this.action = action;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public WisenAction getAction() {
		return action;
	}

	public void setAction(WisenAction action) {
		this.action = action;
	}
	
	
	
}
