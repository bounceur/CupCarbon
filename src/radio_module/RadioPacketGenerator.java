package radio_module;

public class RadioPacketGenerator {

	public static int packetLengthInBits(int type, int standard) {
		if (standard == RadioStandard.ZIGBEE_802_15_4) 
			if(type==1) //ack packet
				return 40; //5*8;
			else
				return 1016; //127*8;
		
		if (standard == RadioStandard.LORA)
			if(type==1) //no ack packet
				return 0;
			else
				return 2048; //256*8;
		
		if (standard == RadioStandard.WIFI_802_11)
			if(type==1) //ack packet
				return 112; //14*8;
			else
				return 18432; //2304*8;
		return 0;
	}
	
}
