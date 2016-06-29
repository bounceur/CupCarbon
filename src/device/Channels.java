package device;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import map.MapLayer;
import radio_module.Ber;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import utilities.MapCalc;
import utilities.UColor;
import visualisation.Visualisation;
import wisen_simulation.SimLog;
import wisen_simulation.SimulationInputs;

public class Channels {
	
	public static LinkedList<List<PacketEvent>> channelEventList = new LinkedList<List<PacketEvent>>();
	public static LinkedList<Integer> iMins = new LinkedList<Integer>();	
	
	public Channels() {
		super();
		init();
	}
	
	public static void init() {	
		channelEventList = new LinkedList<List<PacketEvent>>();
		for(int i=0; i<20; i++) {
			channelEventList.add(new LinkedList<PacketEvent>());
		}
	}
	
	public static void addPacketEvent(int type, String packet, String message, SensorNode sSensor, SensorNode rSensor) {
		// type=0 : Direct sendind
		// type=1 : ACK sendind
		// type=2 : Broadcast sendind 
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio1 = 1.0/sSensor.getRadioDataRate();
		double ratio2 = 1.0/rSensor.getUartDataRate();
		
		double durationOfSending = ratio1*(packet.length()) ;
		double durationOfUARTReceiving = ratio2*(message.length()*8) ;
		double duration = durationOfSending + durationOfUARTReceiving ;
		
		double lastTime = 0;
		sSensor.setSending(true);
		rSensor.setReceiving(true);
		
		PacketEvent packetEvent = new PacketEvent(type, sSensor, rSensor, packet, message, lastTime+duration);		
		channelEventList.get(sSensor.getCh()).add(packetEvent);
		Collections.sort(channelEventList.get(sSensor.getCh()));		
	}
		
	public static void receivedMessages() {
		for (List<PacketEvent> packetEventList : channelEventList) {
			while(packetEventList.size()>0 && packetEventList.get(0).getTime()==0) {
				int type = packetEventList.get(0).getType();
				String packet = packetEventList.get(0).getPacket();
				SensorNode rSensor = packetEventList.get(0).getRSensor();
				SensorNode sSensor = packetEventList.get(0).getSSensor();					
				
				rSensor.consumeRx(packet.length());
				
				boolean berOk = true;
				
				berOk = Ber.berOk(packet, rSensor);
				
				sSensor.setSending(false);
				rSensor.setReceiving(false);
				MapLayer.mapViewer.repaint();
				
				// type=0 : Direct sending
				// type=1 : ACK sending
				// type=2 : Broadcast sending 
				if ((type == 0) || (type == 2)) {	
					if (berOk || (type == 2)) {
						rSensor.addMessageToBuffer(packetEventList.get(0).getMessage());
						
						if(rSensor.getScript().getCurrent().isWait()) {
							rSensor.setEvent(0);
						}
						
						if((type == 0) && (SimulationInputs.ack)) {									
							addPacketEvent(1, XBeeFrameGenerator.ackInBin("0", "00", "00", Standard.getSubChannel(rSensor.getStandard())*8), "0", rSensor, sSensor);																		
						}
					}
				}
				
				if (type == 1) {
						rSensor.setAckOk(true);
						rSensor.setEvent(0);
				}
				
				Visualisation.comDeleteArrow(packetEventList.get(0).getSSensor(), packetEventList.get(0).getRSensor());
				packetEventList.remove(0);						
				if(packetEventList.size()>0) {
					packetEventList.get(0).getSSensor().setSending(true);
					packetEventList.get(0).getRSensor().setReceiving(true);
				}
			}
		}
	}
	
	public static void goToTheNextTime(double min) {
		for (List<PacketEvent> packetEvent : channelEventList) {
			for (PacketEvent packet : packetEvent) {
				packet.setTime(packet.getTime()-min);
			}
		}
	}
	
	public static double getMin() {
		double min = Double.MAX_VALUE;
		for (List<PacketEvent> PacketEventList : channelEventList) {		
			if(PacketEventList.size()>0) {
				if (PacketEventList.get(0).getTime() < min)
					min = PacketEventList.get(0).getTime();
			}
		}
		return min;
	}
	
	public static void drawChannelLinks(Graphics g) {			
		//Iterator<List<PacketEvent>> it1 = channelEventList.iterator();
		//for (List<PacketEvent> packetEventList : channelEventList) {
		//List<PacketEvent> packetEventList;
		//PacketEvent pev;
		//while(it1.hasNext()) {	
		//Collections.synchronizedList(
		
		for(List<PacketEvent> packetEventList : channelEventList) {
			//packetEventList = it1.next();
			if(packetEventList.size()>0) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(2.5f));
			    if(MapLayer.getMapViewer().getZoom() < 2) {
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
				int arrColor = 0;
				//Iterator<PacketEvent> it2 = packetEventList.iterator();				
				//for(PacketEvent pev : packetEventList) {
				//for(PacketEvent pev : packetEventList) {
				for(int idx=0; idx<packetEventList.size(); idx++) {
					try {
						PacketEvent pev = packetEventList.get(idx);
						//pev = it2.next();
						if(pev.getSSensor().isSending() && pev.getRSensor().isReceiving()) {
							if(pev.getType()==0 || pev.getType()==2 || ((pev.getType()==1) && SimulationInputs.showAckLinks)) {
								g.setColor(pev.getSSensor().getRadioLinkColor());
								if (pev.getSSensor().getRadioLinkColor() == UColor.RED) 
									arrColor = 0;
								else 
									arrColor = 1; 
								if((pev.getType()==1) && SimulationInputs.showAckLinks) {
									g.setColor(pev.getSSensor().getACKLinkColor());
									arrColor = 2;
								}
								
								Visualisation.comAddArrow((SensorNode)pev.getSSensor(), (SensorNode)pev.getRSensor(), 2, arrColor, 2);
								
								coord = MapCalc.geoToPixelMapA(pev.getSSensor().getLatitude(), pev.getSSensor().getLongitude());
								lx1 = coord[0];
								ly1 = coord[1];		
								coord = MapCalc.geoToPixelMapA(pev.getRSensor().getLatitude(), pev.getRSensor().getLongitude());
								lx2 = coord[0];
								ly2 = coord[1];
								dx = lx2 - lx1;
								dy = ly2 - ly1;
								
								g.drawLine(lx1, ly1, lx2, ly2);
								
								
								
								//g.setColor(Color.BLACK);
								//g.drawString(pev.getRSensor().getMessage(), (lx1+lx2)/2, (ly1+ly2)/2);

								alpha = Math.atan(dy / dx);
								alpha = 180 * alpha / Math.PI;
								int as = 16;
								if(MapLayer.getMapViewer().getZoom() < 2) {
									as = 21;		    	
								}
								if (dx >= 0)	
									g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2,180 - (int) alpha - as, as*2);
								else
									g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2, -(int) alpha - as, as*2);
							}
						}
					}
					catch (Exception e) {}
				}
			}
		}
	}
	
	public static void display() {
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
	
	
//	public static void main(String [] args) {
//		List<Integer> liste = new LinkedList<Integer>();
//		liste.add(4);
//		liste.add(3);
//		liste.add(34);
//		liste.add(7);
//		Collections.sort(liste);
//		//liste.sort(null);
//		System.out.println(liste);
//	}
}