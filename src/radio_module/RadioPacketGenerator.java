package radio_module;

public class RadioPacketGenerator {

	public static int packetLengthInBits(String message, int type, int standard) {
		if (standard == RadioModule.ZIGBEE_802_15_4) 
			if(type==1) //ack packet
				return 40; //5*8;
			else
				return 512+message.length()*64; // ((48+message.length()*8+16) / 4) * 32;
		
		if (standard == RadioModule.LORA)
			if(type==1) //no ack packet
				return 0;
			else
				return 2048; //256*8;
		
		if (standard == RadioModule.WIFI_802_11)
			if(type==1) //ack packet
				return 112; //14*8;
			else
				return 18432; //2304*8;
		return 0;
	}
	
}
