package device;

import java.util.LinkedList;
import java.util.List;

import physical_layer.Ber;
import wisen_simulation.SimLog;

public class Channel {

	protected static List<PacketEvent> messages = new LinkedList<PacketEvent>(); 
	
	public Channel() {
		messages = new LinkedList<PacketEvent>();
	}
	
	public static void init() {
		messages = new LinkedList<PacketEvent>();
	}
	
	public static void addPacket(int type, String message, SensorNode sSensor, SensorNode rSensor) {
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		long duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		if(sSensor.isDistanceMode() && rSensor.isDistanceMode())
			duration = sSensor.getDistanceModeDelay()*DataInfo.ChDataRate;
		long lastTime = 0;
		if (messages.size()>0) 
			lastTime = messages.get(messages.size()-1).getTime();
		PacketEvent packet = new PacketEvent(type, sSensor, rSensor, message, lastTime+duration);
		
		messages.add(packet);
	}
	
	public static void messageReceived() {
		
		int type = messages.get(0).getType();
		String message = messages.get(0).getPacket();
		SensorNode rSensor = messages.get(0).getRSensor();
		SensorNode sSensor = messages.get(0).getSSensor();
		
		//System.out.println(rSensor.getId()+" Message received "+message);
		
		boolean berOk = true;
		
		berOk = Ber.berOk();
		
		if ((type == 0) || (type == 2)) {	
			if (berOk || (type == 2)) {
				if(!rSensor.isSleeping())
					rSensor.addMessageToBuffer(messages.get(0).getPacket().length()*8, messages.get(0).getPacket());
				if(rSensor.getScript().getCurrent().isWait())
					rSensor.setEvent(0);
				if(type == 0)
					Channel.addPacket(1, "0", rSensor, sSensor);
			}
			else
				Channel.addPacket(1, "1", rSensor, sSensor);
			//System.out.println(rSensor.getId()+" Send ACK to "+sSensor.getId());
		}
		if (type == 1) {
			if (message.equals("0")) {
				rSensor.setAckOk(true);
				//System.out.println(rSensor.getId()+" ACK Received Correctly");
				rSensor.setEvent(0);
			}
			else {
				rSensor.setAckOk(false);
				//System.out.println(rSensor.getId()+" ACK Received with errors");
				rSensor.setEvent(0);
			}
		}
		

//		String s = "[" ;
//		for (PacketEvent p : messages) {
//			s += p+ "->";
//		}
//		s += "]";
//		System.out.println(s);
		
		
//		if(!rSensor.isSleeping())
//			rSensor.addMessageToBuffer(messages.get(0).getPacket().length()*8, messages.get(0).getPacket());
//		if(rSensor.getScript().getCurrent().isWait())
//			rSensor.setEvent(0);
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
	
	public static String display() {
		String s = "[" ;
		for (PacketEvent p : messages) {
			s += p+ " | ";
		}
		s += "]";
		return s;
		
	}
}
