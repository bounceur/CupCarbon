package script;

import device.Channels;
import device.DeviceList;
import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import utilities.UColor;
import wisen_simulation.SimLog;
import wisen_simulation.SimulationInputs;

public class Command_SEND extends Command {
	
	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";	
	protected String arg5 = "";	
	
	//private boolean ack = false;
	
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
		String packet = "";
		if(sensor.getStandard() == Standard.NONE)
			packet = message ;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			packet = XBeeFrameGenerator.data16InBin(message, "00", "00", Standard.getSubChannel(sensor.getStandard())*8);
		if(sensor.getStandard() == Standard.WIFI_802_11) {
			packet = XBeeFrameGenerator.data16InBin(message, "00", "00", Standard.getSubChannel(sensor.getStandard())*8);
		}
		
		sensor.consumeTx(packet.length());
		
		if (arg2.equals("*")) {
			SimLog.add("S" + sensor.getId() + " has finished sending in a broadcast the message : \"" + message + "\" to the nodes: ");
			double v = 0;
			if (!arg3.equals("")) {
				v = Double.valueOf(sensor.getScript().getVariableValue(arg3));
			}
			for (SensorNode rnode : sensor.getSensorNodeNeighbors()) {
				if (sensor.propagationDetect(rnode) && sensor.canCommunicateWith(rnode) && rnode.getId()!=v) {
					Channels.addPacketEvent(v==0?2:0, packet, message, sensor, rnode);
				}
			}
		}
		else {
			String dest = arg2;
			dest = sensor.getScript().getVariableValue(dest);
			double destNodeId;	
			if(!dest.equals("0")) {
				destNodeId = Double.valueOf(dest);
				SensorNode rnode = DeviceList.getSensorNodeById((int) destNodeId);
				if (rnode != null) {
					SimLog.add("S" + sensor.getId() + " has finished sending the message : \"" + message + "\" to the node: ");
					if (sensor.propagationDetect(rnode) && sensor.canCommunicateWith(rnode)) {
						Channels.addPacketEvent(0, packet, message, sensor, rnode);
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
					for(SensorNode rnode : DeviceList.getSensorNodes()) {
						if ((sensor.propagationDetect(rnode)) && (rnode.getMy()==destNodeId) && sensor.canCommunicateWith(rnode)) {
							SimLog.add("  -> S" + rnode.getId() + " ");							
							//rnode.setReceiving(true);
							Channels.addPacketEvent(0, packet, message, sensor, rnode);
						}						
					}
				}
				else {
					dest = arg4;
					dest = sensor.getScript().getVariableValue(dest);
					if(!dest.equals("0")) {
						destNodeId = Integer.valueOf(dest);
						SensorNode rnode = DeviceList.getSensorNodeById((int)destNodeId);
						if (rnode != null) {
							SimLog.add("S" + sensor.getId() + " has finished sending the message : \"" + message + "\" to the node: ");
							if (sensor.canCommunicateWith(rnode)) {
								//rnode.setReceiving(true);
								Channels.addPacketEvent(0, packet, message, sensor, rnode);
								//sensor.setDistanceMode(true);
								//rnode.setDistanceMode(true);
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
	
	private int tentative = 0;
	
	@Override
	public double execute() {	
		//System.out.println("------------------------------");
		//System.out.println("--> execute");
		if (arg1.equals("!color")) {
			sensor.setRadioLinkColor(UColor.colorTab2[Integer.parseInt(arg2)]);
			//Visualisation.changeColorOfArrows(Integer.parseInt(arg2));
			return 0;
		}
		String message = arg1;
		message = sensor.getScript().getVariableValue(message);
		int messageLength = message.length();
		
		//System.out.println(sensor.getId()+" -> "+" SEND "+sensor.getId());
		
		if(!writing) {
			//System.out.println("--> writing");
			sensor.setAckOk(false);
			tentative = 0;
			//ack = false;
			SimLog.add("S" + sensor.getId() + " is writing the message : \"" + message + "\" in its buffer.");
			writing = true ;
			executing = true;
			//System.out.println("W1 "+executing);
			
			// Considering the message sent to the buffer (it requires UartDataRate bauds)
			//double ratio = (sensor.getRadioDataRate()*1.0)/(sensor.getUartDataRate());
			//return (long)(Math.round(messageLength*8.*ratio));
			double ratio = 1.0/(sensor.getUartDataRate());
			
			return (ratio * (messageLength*8)) ;
			//return 0.0000000000001;
		}
		
		if (writing) { // && !ack) {	
			//System.out.println("--> written");
			if(sensor.getAckOk()) {
				System.out.println("ACK RECEIVED");
				sensor.setAckOk(false);
				tentative = 0;
				writing = false;
				executing = false;
				return 0 ;
			}
						
			if(tentative < sensor.getNumberOfSends()) {
				//System.out.println("SEND "+ tentative);
				SimLog.add("S" + sensor.getId() + " starts sending the message : \"" + message + "\".");
				sendOperation(message);
			}
			else {
				System.out.println("MESSAGE LOST");
			}
			
			if ((arg2.equals("*") && arg3.equals("")) || !SimulationInputs.ack) {
				//ack = false;
				writing = false ;
				executing=false;
				return 0 ;
			}
			//else {
			if (tentative < sensor.getNumberOfSends()) {
				//if(tentative > 0 && tentative < sensor.getNumberOfSends()-1) sensor.getScript().previous();
				if(tentative > 0) {
					//System.out.println("::: "+sensor.getId()+" -> "+tentative);
					sensor.getScript().previous();
				}
				tentative++;
				System.out.println("SEND "+ tentative);
				writing = true;
				executing = true;						
				return sensor.getTimeToResend();//Double.MAX_VALUE;// (250000*3);
			}
			//System.out.println("||| "+sensor.getId()+" -> "+tentative+ " RATE ");
			tentative = 0;
			writing = false;
			executing = false;
			return 0 ;
				
				
//				if (tentative < 3) {
//					tentative++;
//					//if (tentative == 1) remaining = 0.3;
//					System.out.println("Tentaive :"+tentative);					
//					return 0.3;//Double.MAX_VALUE;// (250000*3);
//				}
//				else {
//					//ack = true;
//					ack = false;
//					writing = false ;
//					executing=false;
//					System.out.println("FIN TEN");
//					return 0;//Double.MAX_VALUE;// (250000*3);
//				}				
			}
		//}
		//System.out.println("--> ?");
//		if(ack) {
//			//System.out.println(sensor.getId()+" ACK Received : "+sensor.getAckOk());
//			ack = false;
//			if(sensor.getAckOk()) {
//				writing = false;
//				executing = false;				
//			} 
//			else {
//				writing = true;
//				executing = true;
//				sensor.getScript().previous();
//			}
//			return 0 ;
//		}
				
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
		String s = XBeeToArduinoFrameGenerator.data16(s1, s2, s3);		
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