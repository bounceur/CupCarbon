package device;

import java.awt.Graphics;
import java.util.LinkedList;
import radio_module.RadioStandard;

public class MultiChannels {
	
	public static LinkedList<Channels> channelList = new LinkedList<Channels>();
	
	public MultiChannels() {		
		init();
	}
	
	public static void init() {		
		channelList = new LinkedList<Channels>();		
		// Index 0 : 802.15.4
		// Index 1 : WiFi
		// Index 2 : Lora
		channelList.add(new Channels(RadioStandard.ZIGBEE_802_15_4));
		channelList.add(new Channels(RadioStandard.WIFI_802_11));
		channelList.add(new Channels(RadioStandard.LORA));
	}
	
	public static void addChannel(int std){		
		Channels channel = new Channels(std);
		channelList.add(channel);
	}
	
	public static void addPacketEvent(int type, String message, SensorNode sSensor, SensorNode rSensor) {
		int stdS = sSensor.getStandard();
		int stdR = rSensor.getStandard();
		if(stdS==stdR) {
			channelList.get(stdS-1).addPacketEvent(type, message, sSensor, rSensor);
		}
	}
		
	public static void receivedMessages() {
		for (Channels channel : channelList) {
			channel.receivedMessages();
		}
	}
	
	public static void goToTheNextTime(double min) {
		for (Channels channel : channelList) {
			channel.goToTheNextTime(min);
		}
	}
	
	public static double getMin() {
		double min = Double.MAX_VALUE;
		for (Channels channel : channelList) {		
			if(channel != null) {
				if (channel.getMin() < min)
					min = channel.getMin();
			}
		}
		return min;
	}
	
	public static void drawChannelLinks(Graphics g) {			
		for (Channels channel : channelList) {
			channel.drawChannelLinks(g);
		}
	}
}