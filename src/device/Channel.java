package device;

import java.util.LinkedList;
import java.util.List;

import wisen_simulation.SimLog;

public class Channel {

	protected static List<PacketEvent> receivedMessages = new LinkedList<PacketEvent>();
	
	public Channel() {
		receivedMessages = new LinkedList<PacketEvent>();
	}
	
	public static void init() {
		receivedMessages = new LinkedList<PacketEvent>();
	}
	
	public static void addPacket(String message, SensorNode sSensor, SensorNode rSensor) {
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		long duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		if(sSensor.isDistanceMode() && rSensor.isDistanceMode())
			duration = sSensor.getDistanceModeDelay()*DataInfo.ChDataRate;
		long lastTime = 0;
		if (receivedMessages.size()>0) 
			lastTime = receivedMessages.get(receivedMessages.size()-1).getTime();
		PacketEvent packet = new PacketEvent(sSensor, rSensor, message, lastTime+duration);
		
		receivedMessages.add(packet);
	}
	
	public static void messageReceived() {
		SensorNode rSensor = receivedMessages.get(0).getRSensor();
		rSensor.addMessageToBuffer(receivedMessages.get(0).getPacket().length()*8, receivedMessages.get(0).getPacket());
		if(rSensor.getScript().getCurrent().isWait())
			rSensor.setEvent(0);
		receivedMessages.remove(0);		
	}
	
	public static void goToTheNextTime(long min) {
		for (PacketEvent packet : receivedMessages) {
			packet.setTime(packet.getTime()-min);
		}
	}
	
	public static long getMin() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getTime();
		else
			return Long.MAX_VALUE;
	}
	
	public static String getMessage() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getPacket();
		else
			return "";
	}
	
//	public static int getRSensor() {
//		if(receivedMessages.size()>0)
//			return receivedMessages.get(0).getSensor().getId();
//		else
//			return 0;
//	}
	
	public static List<PacketEvent> getPackets() {
		return receivedMessages;
	}
	
	public static long getTime() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getTime();
		else
			return Long.MAX_VALUE;
	}
	
	public static int size() {
		return receivedMessages.size();
	}
}
