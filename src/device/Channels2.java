package device;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import map.Layer;
import physical_layer.Ber;
import utilities.MapCalc;
import wisen_simulation.SimLog;

public class Channels2 {

	public static List<PacketEvent> packetEventList = new LinkedList<PacketEvent>();
	
	//protected static LinkedList<List<PacketEvent>> packetEventList = new LinkedList<List<PacketEvent>>();
	//protected static LinkedList<Integer> iMins = new LinkedList<Integer>();	
	
	public Channels2() {
		super();
		init();
		//packetEventList = new LinkedList<PacketEvent>();
	}
	
	public static void init() {
		packetEventList = new LinkedList<PacketEvent>();			
//		packetEventList = new LinkedList<List<PacketEvent>>();
//		for(int i=0; i<20; i++) {
//			packetEventList.add(new LinkedList<PacketEvent>());
//		}
	}
	
	public static void addPacket(int type, String message, SensorNode sSensor, SensorNode rSensor) {
		SimLog.add("S" + rSensor.getId() + " is receiving the message : \"" + message + "\" in its buffer.");
		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		long duration =  ((int)(Math.round(message.length()*8.*ratio))) + (message.length()*8);
		if(sSensor.isDistanceMode() && rSensor.isDistanceMode())
			duration = sSensor.getDistanceModeDelay()*DataInfo.ChDataRate;
		long lastTime = 0;
		if (packetEventList.size()>0) 
			lastTime = packetEventList.get(packetEventList.size()-1).getTime();
		PacketEvent packet = new PacketEvent(type, sSensor, rSensor, message, lastTime+duration);
		
		packetEventList.add(packet);
		//System.out.println(sSensor.getId()+" : "+packetEventList);
	}
	
	
	
	
	public static void messageReceived() {
		
		Layer.getMapViewer().repaint();
		
		int type = packetEventList.get(0).getType();
		String message = packetEventList.get(0).getPacket();
		SensorNode rSensor = packetEventList.get(0).getRSensor();
		SensorNode sSensor = packetEventList.get(0).getSSensor();
		
		//System.out.println(rSensor.getId()+" Message received "+message);
		
		boolean berOk = true;
		
		berOk = Ber.berOk();
		
		if ((type == 0) || (type == 2)) {	
			if (berOk || (type == 2)) {
				if(!rSensor.isSleeping())
					rSensor.addMessageToBuffer(packetEventList.get(0).getPacket().length()*8, packetEventList.get(0).getPacket());
				if(rSensor.getScript().getCurrent().isWait())
					rSensor.setEvent(0);
				if(type == 0)
					addPacket(1, "0", rSensor, sSensor);
			}
			else
				addPacket(1, "1", rSensor, sSensor);
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
//		for (PacketEvent p : packetEventList) {
//			s += p+ "->";
//		}
//		s += "]";
//		System.out.println(s);
		
		
//		if(!rSensor.isSleeping())
//			rSensor.addMessageToBuffer(packetEventList.get(0).getPacket().length()*8, packetEventList.get(0).getPacket());
//		if(rSensor.getScript().getCurrent().isWait())
//			rSensor.setEvent(0);
		packetEventList.remove(0);		
	}
	
	public static void goToTheNextTime(long min) {		
		for (PacketEvent packet : packetEventList) {
			//System.out.print(packet.getTime()+" : ");
			packet.setTime(packet.getTime()-min);
			//System.out.println(packet.getTime());
		}
		//System.out.println("--------");
	}
	
	public static long getMin() {
		if(packetEventList.size()>0) {			
			return packetEventList.get(0).getTime();
		}
		else
			return Long.MAX_VALUE;
	}
	
//	public static String getMessage() {
//		if(packetEventList.size()>0)
//			return packetEventList.get(0).getPacket();
//		else
//			return "";
//	}
	
//	public static int getRSensor() {
//		if(packetEventList.size()>0)
//			return packetEventList.get(0).getSensor().getId();
//		else
//			return 0;
//	}
	
//	public static List<PacketEvent> getPackets() {
//		return packetEventList;
//	}
	
//	public static long getTime() {
//		if(packetEventList.size()>0)
//			return packetEventList.get(0).getTime();
//		else
//			return Long.MAX_VALUE;
//	}
	
//	public static int size() {
//		return packetEventList.size();
//	}
	
	public static void drawChannelLinks(Graphics g) {
		if(packetEventList.size()>0) {
			Graphics2D g2 = (Graphics2D) g;
			//g2.setStroke(new BasicStroke(1));
			//if(isSending() && device.isReceiving()) {
				
				g2.setStroke(new BasicStroke(2.5f));
				
			    if(Layer.getMapViewer().getZoom() < 2) {
			    	g2.setStroke(new BasicStroke(3));		    	
			    }
			//}
			
			int [] coord ;
			int lx1 ;
			int ly1 ;
			int lx2 ;
			int ly2 ;
			double dx = 0;
			double dy = 0;
			double alpha = 0;
			for(PacketEvent pev : packetEventList) {
				if(pev.getSSensor().isSending() && pev.getRSensor().isReceiving()) {
					if(pev.getType()==0 || pev.getType()==2) {
						g.setColor(pev.getSSensor().getRadioLinkColor());
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
						if(Layer.getMapViewer().getZoom() < 2) {
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
	
	public static String display() {
		String s = "[" ;
		for (PacketEvent p : packetEventList) {
			s += p+ " | ";
		}
		s += "]";
		return s;
		
	}
}
