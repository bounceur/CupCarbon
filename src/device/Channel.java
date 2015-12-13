package device;

import java.util.LinkedList;
import java.util.List;

import wisen_simulation.SimLog;

public class Channel {

	protected static List<PacketEvent> messages = new LinkedList<PacketEvent>();
	
	public Channel() {
		messages = new LinkedList<PacketEvent>();
	}
	
	public static void init() {
		messages = new LinkedList<PacketEvent>();
	}
	
	public static void addPacket(String message, SensorNode sSensor, SensorNode rSensor) {
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		long duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		if(sSensor.isDistanceMode() && rSensor.isDistanceMode())
			duration = sSensor.getDistanceModeDelay()*DataInfo.ChDataRate;
		long lastTime = 0;
		if (messages.size()>0) 
			lastTime = messages.get(messages.size()-1).getTime();
		PacketEvent packet = new PacketEvent(sSensor, rSensor, message, lastTime+duration);
		
		messages.add(packet);
	}
	
	public static void messageReceived() {
		SensorNode rSensor = messages.get(0).getRSensor();
		if(!rSensor.isSleeping())
			rSensor.addMessageToBuffer(messages.get(0).getPacket().length()*8, messages.get(0).getPacket());
		if(rSensor.getScript().getCurrent().isWait())
			rSensor.setEvent(0);
		messages.remove(0);		
	}
	
	public static void goToTheNextTime(long min) {
		for (PacketEvent packet : messages) {
			packet.setTime(packet.getTime()-min);
		}
	}
	
	public static long getMin() {
		if(messages.size()>0)
			return messages.get(0).getTime();
		else
			return Long.MAX_VALUE;
	}
	
	public static String getMessage() {
		if(messages.size()>0)
			return messages.get(0).getPacket();
		else
			return "";
	}
	
//	public static int getRSensor() {
//		if(messages.size()>0)
//			return messages.get(0).getSensor().getId();
//		else
//			return 0;
//	}
	
	public static List<PacketEvent> getPackets() {
		return messages;
	}
	
	public static long getTime() {
		if(messages.size()>0)
			return messages.get(0).getTime();
		else
			return Long.MAX_VALUE;
	}
	
	public static int size() {
		return messages.size();
	}
}
