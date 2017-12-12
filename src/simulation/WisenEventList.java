package simulation;

import java.util.LinkedList;
import java.util.List;

public class WisenEventList {

	protected List<WisenEvent> eventList = new LinkedList<WisenEvent>();
	
	public WisenEventList() {
		eventList = new LinkedList<WisenEvent>();
	}
	
	public void init() {
		eventList = new LinkedList<WisenEvent>();
	}
	
	public void addEvent(int type, long end, WisenAction action) {
		
		WisenEvent event = new WisenEvent(type, end, action);
		
		eventList.add(event);
	}
	
	
	public void goToTheNextEvent(long min) {
		for (WisenEvent event : eventList) {
			event.setEnd(event.getEnd()-min);
		}
	}
	
	public long getMin() {
		if(eventList.size()>0)
			return eventList.get(0).getEnd();
		else
			return Long.MAX_VALUE;
	}
	
	public int size() {
		return eventList.size();
	}
}
