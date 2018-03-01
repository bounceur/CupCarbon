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
import radio_module.RadioPacketGenerator;
import radio_module.RadioStandard;
import simulation.SimulationInputs;
import simulation.WisenSimulation;
import utilities.MapCalc;

public class Channels {
	
	public static double numberOfSentMessages = 0; // in number
	public static double numberOfReceivedMessages = 0; // in number
	public static double numberOfAckMessages = 0; // in number
	public static double numberOfLostMessages = 0; // in number
	public static double numberOfSentMessages_b = 0; // in bytes
	public static double numberOfReceivedMessages_b = 0; // in bytes
	public static double numberOfAckMessages_b = 0; // in bytes
	public static double numberOfLostMessages_b = 0; // in bytes
	
	protected List<List<PacketEvent>> channelEventList = Collections.synchronizedList(new LinkedList<List<PacketEvent>>());
	
	public Channels(int std) {
		super();
		init(std);
	}
	
	public void init(int std) {	
		channelEventList = new LinkedList<List<PacketEvent>>();
		int channelNumber = 0;
		if(std == RadioStandard.ZIGBEE_802_15_4) channelNumber = 16;
		if(std == RadioStandard.LORA) channelNumber = 1;
		if(std == RadioStandard.WIFI_802_11) channelNumber = 14;
		for(int i=0; i < channelNumber; i++) {
			channelEventList.add(new LinkedList<PacketEvent>());
		}
	}
	
	public void addPacketEvent(int type, String message, SensorNode tSensor, SensorNode rSensor) {
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
			
			double durationOfSending = ratio1*(RadioPacketGenerator.packetLengthInBits(type, tSensor.getStandard())) ;
			double durationOfUARTReceiving = 0;
			if(type==1)
				durationOfUARTReceiving = 0 ;
			else
				durationOfUARTReceiving = ratio2*(message.length()*8) ;
			double duration = durationOfSending + durationOfUARTReceiving ;
			
			double lastTime = 0;
			tSensor.setSending(true);
			rSensor.setReceiving(true);
			
			PacketEvent packetEvent = new PacketEvent(type, tSensor, rSensor, message, lastTime+duration);		
			channelEventList.get(tSensor.getCurrentRadioModule().getCh()).add(packetEvent);
			//Collections.sort(channelEventList.get(sSensor.getCurrentRadioModule().getCh()));
			//System.out.println(WisenSimulation.time+ " " + channelEventList);
		}
	}

	public void receivedMessages() {
		for (List<PacketEvent> packetEventList : channelEventList) {			
			while(packetEventList.size()>0 && packetEventList.get(0).getTime()==0) {
				int type = packetEventList.get(0).getType();
				String message = packetEventList.get(0).getMessage();
				SensorNode tSensor = packetEventList.get(0).getSSensor();	
				SensorNode rSensor = packetEventList.get(0).getRSensor();

				rSensor.setDrssi(tSensor.distance(rSensor));
				
				numberOfReceivedMessages += tSensor.getPl()/100.;
				numberOfReceivedMessages_b += message.length() * (tSensor.getPl()/100.);
				
				rSensor.consumeRx(RadioPacketGenerator.packetLengthInBits(type, tSensor.getStandard()));
				
				boolean errorBitsOk = ErrorBits.errorBitsOk(message, tSensor, rSensor);
				
				tSensor.setSending(false);
				rSensor.setReceiving(false);				
				
				// type=0 : Direct sending
				// type=1 : ACK sending
				// type=2 : Broadcast sending 
				if ((type == 0) || (type == 2)) {	
					if (errorBitsOk || (type == 2)) {
						
						rSensor.addMessageToBuffer(packetEventList.get(0).getMessage());
						
						if(rSensor.getScript().getCurrent().isWait()) {
							rSensor.setEvent(0);
						}
						
						if((type == 0) && (SimulationInputs.ack)) {
							addPacketEvent(1, "", rSensor, tSensor);
						}
					}
				}
				
				if (type == 1) {
					tSensor.setAckWaiting(false);
					rSensor.setAckReceived(true);
					rSensor.setAckWaiting(false);
					rSensor.setEvent(0);
				}				
				packetEventList.remove(0);
			}
		}
	}
	
	public void goToTheNextTime(double min) {
		for (List<PacketEvent> packetEvent : channelEventList) {
			for (PacketEvent packet : packetEvent) {
				packet.setTime(packet.getTime()-min);
			}
		}
	}
	
	public double getMin() {
		double min = Double.MAX_VALUE;
		for (List<PacketEvent> PacketEventList : channelEventList) {		
			if(PacketEventList.size()>0) {
				if (PacketEventList.get(0).getTime() < min)
					min = PacketEventList.get(0).getTime();
			}
		}
		return min;
	}
	
	public void drawChannelLinks(Graphics g) {			
		for(List<PacketEvent> packetEventList : channelEventList) {
			if(packetEventList.size()>0) {
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
				for(int idx=0; idx<packetEventList.size(); idx++) {
				//for(PacketEvent packetEvent : packetEventList) {
					try {
						PacketEvent packetEvent = packetEventList.get(idx);
						if(packetEvent.getSSensor().isSending() && packetEvent.getRSensor().isReceiving()) {
							if(packetEvent.getType()==0 || packetEvent.getType()==2 || ((packetEvent.getType()==1) && SimulationInputs.showAckLinks)) {
								
								g.setColor(packetEvent.getSSensor().getRadioLinkColor());
								if((packetEvent.getType()==1) && SimulationInputs.showAckLinks) {
									g.setColor(Color.BLACK);
									if(MapLayer.dark)
										g.setColor(Color.ORANGE);
								}
								
								coord = MapCalc.geoToPixelMapA(packetEvent.getSSensor().getLatitude(), packetEvent.getSSensor().getLongitude());
								lx1 = coord[0];
								ly1 = coord[1];		
								coord = MapCalc.geoToPixelMapA(packetEvent.getRSensor().getLatitude(), packetEvent.getRSensor().getLongitude());
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
									if(packetEvent.getType()!=1) {
										MapLayer.drawMessage(lx1, lx2, ly1, ly2, packetEvent.getMessage(), g2);
										MapLayer.drawMessageAttempts(lx1, lx2, ly1, ly2, ""+(packetEvent.getSSensor().getAttempts()+1), g2);
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
		for (List<PacketEvent> PacketEventList : channelEventList) {			
			s += "[" ;
			for (PacketEvent p : PacketEventList) {
				s += p.toString() + " | ";
			}
			s += "]\n";
		}
		System.out.println(s);
	}
	
}