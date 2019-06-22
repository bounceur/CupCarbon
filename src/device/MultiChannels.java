package device;

import java.awt.Graphics;
import java.util.LinkedList;

import radio_module.RadioModule;

public class MultiChannels {
	
	public static LinkedList<MessageEventList> channelList = new LinkedList<MessageEventList>();
	
	public MultiChannels() {		
		init();
	}
	
	public static void init() {		
		channelList = new LinkedList<MessageEventList>();		
		// Index 0 : 802.15.4
		// Index 1 : WiFi
		// Index 2 : Lora
		channelList.add(new MessageEventList(RadioModule.ZIGBEE_802_15_4));
		channelList.add(new MessageEventList(RadioModule.WIFI_802_11));
		channelList.add(new MessageEventList(RadioModule.LORA));
	}
	
	public static void addChannel(int std){		
		MessageEventList channel = new MessageEventList(std);
		channelList.add(channel);
	}
	
	public static void addPacketEvent(int type, String message, SensorNode sSensor, SensorNode rSensor) {
		int stdS = sSensor.getStandard();
		int stdR = rSensor.getStandard();
		if(stdS==stdR) {
			channelList.get(stdS-1).addMessageEvent(type, message, sSensor, rSensor);
		}
	}
		
	public static void receivedMessages() {
		for (MessageEventList channel : channelList) {
			channel.receivedMessages();
		}
	}
	
	public static void goToTheNextTime(double min) {
		for (MessageEventList channel : channelList) {
			channel.goToTheNextTime(min);
		}
	}
	
	public static double getMin() {
		double min = Double.MAX_VALUE;
		for (MessageEventList channel : channelList) {		
			if(channel != null) {
				if (channel.getMin() < min)
					min = channel.getMin();
			}
		}
		return min;
	}
	
	public static void drawChannelLinks(Graphics g) {			
		for (MessageEventList channel : channelList) {
			channel.drawChannelLinks(g);
		}
	}
}