package device;

import java.util.LinkedList;
import java.util.List;

import wisen_simulation.SimLog;

public class Channel2 {

	protected List<Packet2> receivedMessages = new LinkedList<Packet2>();
	
	public Channel2() {
		receivedMessages = new LinkedList<Packet2>();
	}
	
	public void init() {
		receivedMessages = new LinkedList<Packet2>();
	}
	
	public void addPacket2(String message, SensorNode sensor) {
		SimLog.add("S" + sensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		int duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		//mrEvent = duration;
		
		//int end = 0;
		
		//if (receivedMessages.size()>0)
		//	end = receivedMessages.get(0).getEndTime();
		
		Packet2 packet2 = new Packet2(message, 0, duration);
		//System.out.println(packet2);
		receivedMessages.add(packet2);
		//Collections.sort(receivedMessages);
		System.out.println(receivedMessages);
	}
	
	public void messageReceived(SensorNode sensor) {
		if(receivedMessages.size()>0)
			if(receivedMessages.get(0).getEndTime()==0) {
				sensor.addMessageToBuffer(receivedMessages.get(0).getMessage().length()*8, receivedMessages.get(0).getMessage());
				receivedMessages.remove(0);
			}
	}
}
