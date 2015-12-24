package script;

import arduino.XBeeFrameGenerator;
import device.Channel;
import device.DataInfo;
import device.DeviceList;
import device.SensorNode;
import utilities.UColor;
import wisen_simulation.SimLog;

public class Command_SEND extends Command {
	
	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";	
	protected String arg5 = "";	
	
	private boolean ack = false;
	
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
				if (sensor.radioDetect(snode) && !snode.isDead() && !snode.isSleeping() && sensor.sameCh(snode) && sensor.sameNId(snode) && snode.getId()!=v) {
					snode.setReceiving(true);
					Channel.addPacket(2, message, sensor, snode);
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
					if (sensor.radioDetect(snode) && !snode.isDead() && !snode.isSleeping() && sensor.sameCh(snode) && sensor.sameNId(snode)) {
						snode.setReceiving(true);
						Channel.addPacket(0, message, sensor, snode);
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
						if ((sensor.radioDetect(snode)) && (!snode.isDead() && !snode.isSleeping()) && (snode.getMy()==destNodeId) && sensor.sameCh(snode) && sensor.sameNId(snode)) {
							SimLog.add("  -> S" + snode.getId() + " ");							
							snode.setReceiving(true);
							Channel.addPacket(0, message, sensor, snode);
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
							if (!snode.isDead() && !snode.isSleeping() && sensor.sameCh(snode) && sensor.sameNId(snode)) {
								snode.setReceiving(true);
								Channel.addPacket(0, message, sensor, snode);
								sensor.setDistanceMode(true);
								snode.setDistanceMode(true);
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
	public long execute() {		
		if (arg1.equals("!color")) {
			sensor.setRadioLinkColor(UColor.colorTab2[Integer.parseInt(arg2)]);
			return 0;
		}
		String message = arg1;
		message = sensor.getScript().getVariableValue(message);
		int messageLength = message.length();
		
		//System.out.println(sensor.getId()+" -> "+" SEND "+sensor.getId());
		
		if(!writing) {	
			ack = false;
			SimLog.add("S" + sensor.getId() + " is writing the message : \"" + message + "\" in its buffer.");
			writing = true ;
			executing = true;			
			//System.out.println("W1 "+executing);
			
			// Considerer la mise en buffer du message (coute UartDataRate baud)			
			double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
			return (long)(Math.round(messageLength*8.*ratio));
		}
		
		if (writing && !ack) {			
			//System.out.println("sending");		
			SimLog.add("S" + sensor.getId() + " starts sending the message : \"" + message + "\".");	
			sensor.setSending(true);
			sendOperation(message);
			sensor.setTxConsumption(1);
			sensor.consumeTx(messageLength*8);
			sensor.initTxConsumption();
			
			if (arg2.equals("*")) {
				ack = false;
				writing = false ;					
				executing=false;
				return 0;
			}
			else {
				ack = true;
				return Long.MAX_VALUE;// (250000*3);
			}
		}
		
		if(ack) {
			//System.out.println(sensor.getId()+" ACK Received : "+sensor.getAckOk());
			ack = false;
			if(sensor.getAckOk()) {
				writing = false;
				executing = false;				
			} 
			else {
				writing = true;
				executing = true;
				sensor.getScript().previous();
			}
			return 0;
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
			return "SEND [RADIO]";	
		else 
			return "SEND [UART]"; 
		
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
	
}