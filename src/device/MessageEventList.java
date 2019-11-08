package device;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import map.MapLayer;
import map.NetworkParameters;
import radio_module.ErrorBits;
import radio_module.RadioModule;
import radio_module.RadioPacketGenerator;
import simulation.SimulationInputs;
import simulation.WisenSimulation;
import utilities.MapCalc;

public class MessageEventList {
	
	public static double numberOfSentMessages = 0; // in number
	public static double numberOfReceivedMessages = 0; // in number
	public static double numberOfAckMessages = 0; // in number
	public static double numberOfLostMessages = 0; // in number
	public static double numberOfSentMessages_b = 0; // in bytes
	public static double numberOfReceivedMessages_b = 0; // in bytes
	public static double numberOfAckMessages_b = 0; // in bytes
	public static double numberOfLostMessages_b = 0; // in bytes
	
	protected List<List<MessageEvent>> messageEventLists = Collections.synchronizedList(new LinkedList<List<MessageEvent>>());
	
	public MessageEventList(int std) {
		super();
		init(std);
	}
	
	public void init(int std) {	
		messageEventLists = new LinkedList<List<MessageEvent>>();
		int channelNumber = 0;
		if(std == RadioModule.ZIGBEE_802_15_4) channelNumber = 16;
		if(std == RadioModule.LORA) channelNumber = 1;
		if(std == RadioModule.WIFI_802_11) channelNumber = 14;
		for(int i=0; i < channelNumber; i++) {
			messageEventLists.add(new LinkedList<MessageEvent>());
		}
	}
	
	public void addMessageEvent(int type, String message, SensorNode tSensor, SensorNode rSensor) {
		// type=0 : Direct sending
		// type=1 : ACK sending
		// type=2 : Broadcast sending 		
		
		if((!rSensor.isReceiving() || !SimulationInputs.ack)  || type==1) {
			if(type == 1) {
				numberOfAckMessages += tSensor.getPl()/100.;
				numberOfAckMessages_b += message.length() * (tSensor.getPl()/100.);
			}
			WisenSimulation.simLog.add("S" + rSensor.getId() +" (radio: " + rSensor.getCurrentRadioModule().getName() + ") is receiving the message : \"" + message + "\" in its buffer.");
			double ratio1 = 1.0/tSensor.getCurrentRadioModule().getRadioDataRate();
			double ratio2 = 1.0/rSensor.getUartDataRate();
			
			double durationOfSending = ratio1*(RadioPacketGenerator.packetLengthInBits(message, type, tSensor.getStandard())) ;
			double durationOfUARTReceiving = 0;
			if(type==1)
				durationOfUARTReceiving = 0 ;
			else
				durationOfUARTReceiving = ratio2*(message.length()*8) ;
			double duration = durationOfSending + durationOfUARTReceiving ;
			
			double lastTime = 0;
			tSensor.setSending(true);
			rSensor.setReceiving(true);
			
			MessageEvent messageEvent = new MessageEvent(type, tSensor, rSensor, message, lastTime+duration);		
			messageEventLists.get(tSensor.getCurrentRadioModule().getCh()).add(messageEvent);
			//Collections.sort(messageEventList.get(sSensor.getCurrentRadioModule().getCh()));
			//System.out.println(WisenSimulation.time+ " " + messageEventList);
		}
	}

	public void receivedMessages() {
		for (List<MessageEvent> messageEventList : messageEventLists) {			
			while(messageEventList.size()>0 && messageEventList.get(0).getTime()==0) {
				int type = messageEventList.get(0).getType();
				String message = messageEventList.get(0).getMessage();
				SensorNode tSensor = messageEventList.get(0).getSSensor();	
				SensorNode rSensor = messageEventList.get(0).getRSensor();

				rSensor.setDrssi(tSensor.distance(rSensor));
				
				numberOfReceivedMessages += tSensor.getPl()/100.;
				numberOfReceivedMessages_b += message.length() * (tSensor.getPl()/100.);
				
				rSensor.consumeRx(RadioPacketGenerator.packetLengthInBits(message, type, tSensor.getStandard()));
				
				boolean errorBitsOk = ErrorBits.errorBitsOk(message, tSensor, rSensor);
				
				tSensor.setSending(false);
				rSensor.setReceiving(false);				
				
				// type=0 : Direct sending
				// type=1 : ACK sending
				// type=2 : Broadcast sending 
				if ((type == 0) || (type == 2)) {	
					if (errorBitsOk || (type == 2)) {
						
						rSensor.addMessageToBuffer(messageEventList.get(0).getMessage());
						
						if(rSensor.getScript().getCurrent().isWait()) {
							rSensor.setEvent(0);
						}
						
						if((type == 0) && (SimulationInputs.ack)) {
							addMessageEvent(1, "", rSensor, tSensor);
						}
					}
				}
				
				if (type == 1) {
					tSensor.setAckWaiting(false);
					rSensor.setAckReceived(true);
					rSensor.setAckWaiting(false);
					rSensor.setEvent(0);
				}				
				messageEventList.remove(0);
			}
		}
	}
	
	public void goToTheNextTime(double min) {
		for (List<MessageEvent> massageEventList : messageEventLists) {
			for (MessageEvent messageEvent : massageEventList) {
				messageEvent.setTime(messageEvent.getTime()-min);
			}
		}
	}
	
	public double getMin() {
		double min = Double.MAX_VALUE;
		for (List<MessageEvent> messageEventList : messageEventLists) {		
			if(messageEventList.size()>0) {
				if (messageEventList.get(0).getTime() < min)
					min = messageEventList.get(0).getTime();
			}
		}
		return min;
	}
	
	public void drawChannelLinks(Graphics g) {	
		for(List<MessageEvent> messageEventList : messageEventLists) {
			if(messageEventList.size()>0) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(2f));
				if(MapLayer.mapViewer.getZoom() > 3) {
					g2.setStroke(new BasicStroke(1f));
				}
			    if(MapLayer.mapViewer.getZoom() < 2) {
			    	g2.setStroke(new BasicStroke(3));		    	
			    }
				
				int [] coord ;
				int lx1 ;
				int ly1 ;
				int lx2 ;
				int ly2 ;
				double dx = 0;
				double dy = 0;
				double alpha = 0;
				for(int idx=0; idx<messageEventList.size(); idx++) {
				//for(messageEvent messageEvent : messageEventList) {
					try {
						MessageEvent messageEvent = messageEventList.get(idx);
						if(messageEvent.getSSensor().isSending() && messageEvent.getRSensor().isReceiving()) {
							if(messageEvent.getType()==0 || messageEvent.getType()==2 || ((messageEvent.getType()==1) && SimulationInputs.showAckLinks)) {
								
								g.setColor(messageEvent.getSSensor().getRadioLinkColor());
								if((messageEvent.getType()==1) && SimulationInputs.showAckLinks) {
									g.setColor(Color.BLACK);
									if(MapLayer.dark)
										g.setColor(Color.ORANGE);
								}
								
								coord = MapCalc.geoToPixelMapA(messageEvent.getSSensor().getLatitude(), messageEvent.getSSensor().getLongitude());
								lx1 = coord[0];
								ly1 = coord[1];		
								coord = MapCalc.geoToPixelMapA(messageEvent.getRSensor().getLatitude(), messageEvent.getRSensor().getLongitude());
								lx2 = coord[0];
								ly2 = coord[1];
								dx = lx2 - lx1;
								dy = ly2 - ly1;
								
								g.drawLine(lx1, ly1, lx2, ly2);
								
								alpha = Math.atan(dy / dx);
								alpha = 180 * alpha / Math.PI;
								int as = 14;
								if(MapLayer.mapViewer.getZoom() < 2) {
									as = 21;
								}
								if(MapLayer.mapViewer.getZoom() > 3) {
									as = 10;
								}
								if (dx >= 0)	
									g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2,180 - (int) alpha - as, as*2);
								else
									g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2, -(int) alpha - as, as*2);
								if(NetworkParameters.displayRadioMessages) {
									if(messageEvent.getType()!=1) {
										MapLayer.drawMessage(lx1, lx2, ly1, ly2, messageEvent.getMessage(), g2);
										MapLayer.drawMessageAttempts(lx1, lx2, ly1, ly2, ""+(messageEvent.getSSensor().getAttempts()+1), g2);
									}
								}
							}
						}
					}
					catch (Exception e) {}
				}
			}
		}
	}
	
	public void display() {
		String s="";
		for (List<MessageEvent> messageEventList : messageEventLists) {			
			s += "[" ;
			for (MessageEvent messageEvent : messageEventList) {
				s += messageEvent.toString() + " | ";
			}
			s += "]\n";
		}
		System.out.println(s);
	}
	
}