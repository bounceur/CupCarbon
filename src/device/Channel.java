package device;

import java.util.LinkedList;
import java.util.List;

import wisen_simulation.SimLog;

public class Channel {

	protected static List<Packet> receivedMessages = new LinkedList<Packet>();
	
	public Channel() {
		receivedMessages = new LinkedList<Packet>();
	}
	
	public static void init() {
		receivedMessages = new LinkedList<Packet>();
	}
	
	public static void addPacket(String message, SensorNode sensor) {
		SimLog.add("S" + sensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		int duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		int lastTime = 0;
		if (receivedMessages.size()>0) 
			lastTime = receivedMessages.get(receivedMessages.size()-1).getTime();
		Packet packet = new Packet(sensor, message, lastTime+duration);
		
		receivedMessages.add(packet);
	}
	
	public static void messageReceived() {
		SensorNode sensor = receivedMessages.get(0).getSensor();				
		sensor.addMessageToBuffer(receivedMessages.get(0).getMessage().length()*8, receivedMessages.get(0).getMessage());
		if(sensor.getScript().getCurrent().isWait())
			sensor.setEvent(0);
		receivedMessages.remove(0);
	}
	
	public static void goToTheNextTime(int nt) {
		for (Packet packet : receivedMessages) {
			packet.setTime(packet.getTime()-nt);
		}
	}
	
	public static int getMin() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getTime();
		else
			return Integer.MAX_VALUE;
	}
	
	public static String getMessage() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getMessage();
		else
			return "";
	}
	
	public static int getSensor() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getSensor().getId();
		else
			return 0;
	}
	
	public static List<Packet> getPackets() {
		return receivedMessages;
	}
	
	public static int getTime() {
		if(receivedMessages.size()>0)
			return receivedMessages.get(0).getTime();
		else
			return -1;
	}
	
	public static int size() {
		return receivedMessages.size();
	}
}
