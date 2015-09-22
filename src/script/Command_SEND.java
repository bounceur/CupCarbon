package script;

import utilities.UColor;
import wisen_simulation.SimLog;
import arduino.XBeeFrameGenerator;
import device.DataInfo;
import device.DeviceList;
import device.SensorNode;

public class Command_SEND extends Command {

	
	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";	
	protected String arg5 = "";	
	
	
	public Command_SEND(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		writing = false ;
	}
	
	public Command_SEND(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		writing = false ;
	}
	
	public Command_SEND(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
		writing = false ;
	}

	public void sendOperation(String message) {
		if (arg2.equals("*")) {
			SimLog.add("S" + sensor.getId() + " has finished sending in a broadcast the message : \"" + message + "\" to the nodes: ");
			double v = 0;
			if (!arg3.equals("")) {
				v = Double.valueOf(sensor.getScript().getVariableValue(arg3));
			}
			for (SensorNode snode : sensor.getSensorNodeNeighbors()) {
				if (sensor.radioDetect(snode) && !snode.isDead() && sensor.sameCh(snode) && sensor.sameNId(snode) && snode.getId()!=v) {
					snode.setReceiving(true);	
					snode.setMessage(message);
				}
			}
		}
		else {
			String dest = arg2;
			dest = sensor.getScript().getVariableValue(dest);
			double destNodeId;	
			if(!dest.equals("0")) {
				destNodeId = Double.valueOf(dest);
				SensorNode snode = DeviceList.getSensorNodeById((int)destNodeId);
				if (snode != null) {
					SimLog.add("S" + sensor.getId() + " has finished sending the message : \"" + message + "\" to the node: ");
					if (sensor.radioDetect(snode) && !snode.isDead() && sensor.sameCh(snode) && sensor.sameNId(snode)) {
						snode.setReceiving(true);	
						snode.setMessage(message);
					}
				}
				else 
					SimLog.add("S" + sensor.getId() + " can not send the message : \"" + message + "\" to the non-existent node: "+destNodeId);
			}
			else {
				dest = arg3;
				dest = sensor.getScript().getVariableValue(dest);
				destNodeId = Double.valueOf(dest);
				
				if(!dest.equals("0")) {
					SimLog.add("S" + sensor.getId() + " has finished sending the message : \"" + message + "\" to the nodes with MY="+destNodeId+": ");
					for(SensorNode snode : DeviceList.getSensorNodes()) {
						if ((sensor.radioDetect(snode)) && (!snode.isDead()) && (snode.getMy()==destNodeId) && sensor.sameCh(snode) && sensor.sameNId(snode)) {
							SimLog.add("  -> S" + snode.getId() + " ");							
							snode.setReceiving(true);	
							snode.setMessage(message);
						}						
					}
				}
				else {
					dest = arg4;
					dest = sensor.getScript().getVariableValue(dest);
					if(!dest.equals("0")) {
						destNodeId = Integer.valueOf(dest);
						SensorNode snode = DeviceList.getSensorNodeById((int)destNodeId);
						if (snode != null) {
							SimLog.add("S" + sensor.getId() + " has finished sending the message : \"" + message + "\" to the node: ");
							if (!snode.isDead() && sensor.sameCh(snode) && sensor.sameNId(snode)) {
								snode.setReceiving(true);	
								snode.setMessage(message);
							}
						}
						else 
							SimLog.add("S" + sensor.getId() + " can not send the message : \"" + message + "\" to the non-existent node: "+destNodeId);
					}
				}
			}
		}
	}
	
	public void nodeOperation(int step) {
		
	}
	
	@Override
	public int execute() {		
		if (arg1.equals("!color")) {
			sensor.setRadioLinkColor(UColor.colorTab2[Integer.parseInt(arg2)]);
			return 0;
		}
		String message = arg1;
		message = sensor.getScript().getVariableValue(message);
		int messageLength = message.length();
				
		if (writing) {
			SimLog.add("S" + sensor.getId() + " starts sending the message : \"" + message + "\".");
			writing = false ;			
			sensor.setSending(true);
			sendOperation(message);
			sensor.setTxConsumption(1);
			sensor.consumeTx(messageLength*8);
			sensor.initTxConsumption(); 
			executing=false;
			return 0;
		}
		
		if(!writing) {
			SimLog.add("S" + sensor.getId() + " is writing the message : \"" + message + "\" in its buffer.");
			writing = true ;
			executing = true;
			// Considerer la mise en buffer du message (coute UartDataRate baud)			
			double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
			return (int)(Math.round(messageLength*8.*ratio));
		}
		return 0;
	}
	
	@Override
	public boolean isSend() {
		return true;
	}
	
	@Override
	public String getArduinoForm() {
		String s1 = sensor.getScript().getVariableValue(arg1);
		String s2 = sensor.getScript().getVariableValue(arg2);
		String s3 = sensor.getScript().getVariableValue(arg3);
		String s = XBeeFrameGenerator.data16(s1, s2, s3);		
		return s;
	}
	
	@Override
	public String toString() {
		if(writing)
			return "SEND [W2]";	
		else 
			return "SEND [W1]"; 
		
	}
	
	@Override
	public boolean isExecuting() {
		return executing;
	}
	
	@Override
	public String getMessage() {
		String message = arg1;
		message = sensor.getScript().getVariableValue(message);
		return message;
	}
	
	@Override
	public String finishMessage() {
		if(writing)
			return ("S" + sensor.getId() + " has finished writing in its buffer.");
		return "-finish sending-";
	}
	
//	public static void main(String [] args) {
//		List<Packet> l = new LinkedList<Packet>();
//		Packet p1 = new Packet ("A",1,10);
//		Packet p2 = new Packet ("C",6,4);
//		Packet p3 = new Packet ("B",2,8);
//		Packet p4 = new Packet ("D",5,4);
//		Packet p5 = new Packet ("E",7,4);
//			
//		l.add(p1);
//		l.add(p2);
//		l.add(p3);
//		l.add(p4);
//		l.add(p5);
//		
//		Collections.sort(l);
//		
//		for (Packet p : l)
//			System.out.println(p);
//		
//		l.remove(0);
//		l.remove(0);
//		
//		System.out.println();
//		for (Packet p : l)
//			System.out.println(p);
//	}
	
}