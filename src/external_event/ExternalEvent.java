package external_event;

public class ExternalEvent implements Comparable<ExternalEvent> {

	protected int type = 0;
	protected String script = "";
	protected double startTime = 0;
	protected double finishTime = 0;
//	
//	public ExternalEvent(int type, String script, double startTime, double finishTime) {
//		super();
//		this.type = type ;
//		this.script = script ;
//		this.startTime = startTime;
//		this.finishTime = finishTime;
//	}
//		
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}
//
//	public String getScript() {
//		return script;
//	}
//	
//	public void setScript(String script){
//		this.script = script;
//	}
//
	public double getStartTime() {
		return startTime;
	}
//
//	public void setStartTime(double startTime) {
//		this.startTime = startTime;
//	}
//	
//	public double getFinishTime() {
//		return finishTime;
//	}
//
//	public void setFinishTime(double finishTime) {
//		this.finishTime = finishTime;
//	}
//
	@Override
	public int compareTo(ExternalEvent event) {
		return (startTime>event.getStartTime())?1:(startTime<event.getStartTime())?-1:0;
	}
	
}
