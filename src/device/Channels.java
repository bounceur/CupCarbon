package device;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import map.MapLayer;
import physical_layer.Ber;
import three_d_visual.ThreeDUnityIHM;
import utilities.MapCalc;
import wisen_simulation.SimLog;
import wisen_simulation.SimulationInputs;

public class Channels {
	
	protected static LinkedList<List<PacketEvent>> channelEventList = new LinkedList<List<PacketEvent>>();
	protected static LinkedList<Integer> iMins = new LinkedList<Integer>();	
	
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
	
	public static void addPacket(int type, String message, SensorNode sSensor, SensorNode rSensor) {
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		long duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		//if(sSensor.isDistanceMode() && rSensor.isDistanceMode())
		//	duration = sSensor.getDistanceModeDelay()*DataInfo.ChDataRate;
		
		
		long lastTime = 0;
		if (channelEventList.get(sSensor.getCh()).size()>0 && type != 2) { 
				lastTime = channelEventList.get(sSensor.getCh()).get(channelEventList.get(sSensor.getCh()).size()-1).getTime();
		}
		else {
			sSensor.setSending(true);
			rSensor.setReceiving(true);
		}
		
		PacketEvent packet = new PacketEvent(type, sSensor, rSensor, message, lastTime+duration);		
		channelEventList.get(sSensor.getCh()).add(packet);
	}
	
	public static void receivedMessages() {
		for (List<PacketEvent> packetEventList : channelEventList) {
			boolean stop = false; // For broadcast sending
			while(!stop) {
				if(packetEventList.size()>0) {
					if(packetEventList.get(0).getTime()==0) {
						//Layer.getMapViewer().repaint();
						int type = packetEventList.get(0).getType();
						String message = packetEventList.get(0).getPacket();
						SensorNode rSensor = packetEventList.get(0).getRSensor();
						SensorNode sSensor = packetEventList.get(0).getSSensor();					
						
						boolean berOk = true;
						
						berOk = Ber.berOk(message);
						
						sSensor.setSending(false);
						rSensor.setReceiving(false);
						
						if ((type == 0) || (type == 2)) {	
							if (berOk || (type == 2)) {
								if(!rSensor.isSleeping())
									rSensor.addMessageToBuffer(packetEventList.get(0).getPacket().length()*8, packetEventList.get(0).getPacket());
								if(rSensor.getScript().getCurrent().isWait())
									rSensor.setEvent(0);
								if((type == 0) && (SimulationInputs.ack)) {								
									addPacket(1, "0", rSensor, sSensor);
								}
							}
							else
								addPacket(1, "1", rSensor, sSensor);
						}
						if (type == 1) {
							if (message.equals("0")) {
								rSensor.setAckOk(true);
								rSensor.setEvent(0);
							}
							else {
								rSensor.setAckOk(false);
								rSensor.setEvent(0);
							}
						}
						packetEventList.remove(0);
						if(packetEventList.size()>0) {
							packetEventList.get(0).getSSensor().setSending(true);
							packetEventList.get(0).getRSensor().setReceiving(true);
						}
						//Layer.getMapViewer().repaint();
					}
					else stop = true;
				}
				else stop = true;
			}
		}
	}
	
	public static void goToTheNextTime(long min) {
		for (List<PacketEvent> packetEventList : channelEventList) {
			for (PacketEvent packet : packetEventList) {
				packet.setTime(packet.getTime()-min);
			}
		}
	}
	
	public static long getMin() {
		long min = Long.MAX_VALUE;
		for (List<PacketEvent> packetEventList : channelEventList) {		
			if(packetEventList.size()>0) {
				if (packetEventList.get(0).getTime() < min)
					min = packetEventList.get(0).getTime();
			}
		}
		return min;
	}
	
	public static void drawChannelLinks(Graphics g) {
		for (List<PacketEvent> packetEventList : channelEventList) {
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
				for(PacketEvent pev : packetEventList) {
					if(pev.getSSensor().isSending() && pev.getRSensor().isReceiving()) {
						if(pev.getType()==0 || pev.getType()==2 || ((pev.getType()==1) && SimulationInputs.showAckLinks)) {
							g.setColor(pev.getSSensor().getRadioLinkColor());
							arrColor = 3; 
							if((pev.getType()==1) && SimulationInputs.showAckLinks) {
								g.setColor(pev.getSSensor().getACKLinkColor());
								arrColor = 5;
							}
							
							ThreeDUnityIHM.arrowDrawing((SensorNode)pev.getSSensor(), (SensorNode)pev.getRSensor(), 2, arrColor, 2);
							
							coord = MapCalc.geoToIntPixelMapXY(pev.getSSensor().getLongitude(), pev.getSSensor().getLatitude());
							lx1 = coord[0];
							ly1 = coord[1];		
							coord = MapCalc.geoToIntPixelMapXY(pev.getRSensor().getLongitude(), pev.getRSensor().getLatitude());
							lx2 = coord[0];
							ly2 = coord[1];
							dx = lx2 - lx1;
							dy = ly2 - ly1;
							
							g.drawLine(lx1, ly1, lx2, ly2);
							
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
			}
		}
	}
	
	public static void display() {
		String s="";
		for (List<PacketEvent> packetEventList : channelEventList) {			
			s += "[" ;
			for (PacketEvent p : packetEventList) {
				s += p.toString() + " | ";
			}
			s += "]\n";
		}
		System.out.println(s);
	}
}