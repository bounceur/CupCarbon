package radio_module;

public class RadioStandard {
	
	public static final int NONE = 0;
	public static final int ZIGBEE_802_15_4 = 1;
	public static final int WIFI_802_11 = 2;
	public static final int LORA = 3;
		
	public static int getSubChannel(int std) {
		if (std == NONE)
			return 128;
		if (std == ZIGBEE_802_15_4)
			return 128;
		if (std == WIFI_802_11)
			return 512;
		if (std == LORA)
			return 1;
		return -1;
	}
	
	public static int getDataRate(int std) {
		if (std == NONE)
			return 250000;
		if (std == ZIGBEE_802_15_4)
			return 250000;
		if (std == WIFI_802_11)
			return 72000000;
		if (std == LORA)
			return 30000;
		return 128;
	}
	
	public static String getDataRate(String str) {		
		if (str.equals("NONE"))
			return "250000";
		if (str.equals("802.15.4"))
			return "250000";
		if (str.equals("WIFI 802.11"))
			return "72000000";
		if (str.equals("LoRa"))
			return "30000";
		return "128";
	}
	
}
