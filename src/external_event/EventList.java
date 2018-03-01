package external_event;

import java.util.LinkedList;

public class EventList {
	
	public static LinkedList<ExternalEvent> externalEventList = new LinkedList<ExternalEvent>();
	
//	public EventList() {
//		super();
//		init();
//	}
//	
//	public static void init() {	
//		externalEventList = new LinkedList<ExternalEvent>();		
//	}
//	
//	public static void addExternalEvent(int type, String script, double sTime, double fTime) {
//		ExternalEvent externalEvent = new ExternalEvent(type, script, sTime, fTime);
//		externalEventList.add(externalEvent);
//	}
//	
//	public static boolean deleteExternalEvent(ExternalEvent externalEvent){
//		if (externalEvent.getStartTime() == externalEvent.getFinishTime()){
//			externalEventList.remove(externalEvent);
//			if (externalEvent.getType() != -1){
//				System.out.println(externalEvent.getScript() + " IS EXECUTED!");
//			}
//			return true;
//		}
//		return false;
//	}
//		
//	public static boolean readyToExecute(ExternalEvent externalEvent){
//		if (externalEvent.getStartTime() == 0){
//			return true;
//		}
//		return false;
//	}
//	
//	public static void calculateNextTime() {
//		for (ExternalEvent externalEvent : externalEventList) {
//			if (externalEvent.getStartTime() - 1 > 0)
//				externalEvent.setStartTime(externalEvent.getStartTime()-1);
//			else
//				externalEvent.setStartTime(0);
//			if (externalEvent.getFinishTime() - 1 > 0)
//					externalEvent.setFinishTime(externalEvent.getFinishTime()-1);
//			else
//				externalEvent.setFinishTime(0);
//		}
//	}
//	
//	public static int getMin() {
//		int index = 0;
//		double minStart = Double.MAX_VALUE;
//		double minFinish = Double.MAX_VALUE;
//		for (ExternalEvent externalEvent: externalEventList){			
//			if(externalEvent != null) {
//				if (externalEvent.getStartTime() < minStart || (externalEvent.getStartTime() == minStart && externalEvent.getFinishTime() < minFinish)){
//					minStart = externalEvent.getStartTime();
//					minFinish = externalEvent.getFinishTime();
//					index = externalEventList.indexOf(externalEvent);
//				}
//			}
//		}
//		return index;
//	}
}