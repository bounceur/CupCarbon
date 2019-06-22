package radio_module;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;

import device.DeviceList;
import device.SensorNode;
import enegy_conso_model.EnergyConsumptionModel;
import utilities.UColor;

public abstract class RadioModule {

	public static final String CLASSICAL_TX = "Classical (Tx)";
	public static final String CLASSICAL_RX = "Classical (Rx)";
	public static final String HEINZELMAN_TX = "Heinzelman (Tx)";
	public static final String HEINZELMAN_RX = "Heinzelman (Rx)";
	
	protected String name ;
	protected int radioDataRate = 250000; // zigbee: default
	
	protected double radioRangeRadius = 0 ;
	protected double radioRangeRadiusOri = 0 ;
	
	protected Color radioRangeColor1 = UColor.PURPLE_TTRANSPARENT;
	protected Color radioRangeColor2 = UColor.PURPLE_TRANSPARENT;
	
	protected double eTx = 0.0000592; //sending energy consumption
	protected double eRx = 0.0000286; // receiving energy consumption
	protected double eSleep = 0.0000001;//Sleeping energy
	protected double eListen = 0.000001;//Listening energy	
	
	protected int my = 0;
	protected double pl = 100;
	protected int ch = 0x0;
	protected int nId = 0x3334;
	
	protected String radioConsoTxModel = CLASSICAL_TX;
	protected String radioConsoRxModel = CLASSICAL_RX;
	
	protected double timeToResend = 3.01;//0.001 ;
	protected int numberOfSends = 3 ;
	protected int attempts = 0;
	
	protected double frequency = 2.4e9; // GHz (zigbee: default)
	
//------ Simple Propagation
	protected double requiredQuality = -80.0; // dB
	protected double transmitPower = 0 ; // dBm
	
	protected SensorNode sensorNode;
	
	
	
	
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
	
	
	
	
	public RadioModule(SensorNode sensorNode, String name) {
		this.sensorNode = sensorNode;
		this.name = name;
	}
	
	public abstract int getStandard();
	
	public int getRadioDataRate() {
		return radioDataRate;
	}
	
	public void setRadioDataRate(int radioDataRate) {
		this.radioDataRate = radioDataRate;
	}
	
	public abstract String getStandardName();
	
	public static int getStandardByName(String stdName) {
		if(stdName.equals("NONE")) {
			return NONE;			
		}
		if(stdName.equals("ZIGBEE")) {
			return ZIGBEE_802_15_4;
		}
		if(stdName.equals("WIFI")) {
			return WIFI_802_11;
		}
		if(stdName.equals("LORA")) {
			return LORA;
		}
		return -1;
	}
	
	public static String getNameByStandard(int std) {
		if(std == NONE) {
			return "NONE";			
		}
		if(std == ZIGBEE_802_15_4) {
			return "ZIGBEE";
		}
		if(std == WIFI_802_11) {
			return "WIFI";
		}
		if(std == LORA) {
			return "LORA";
		}
		return "";
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Consume
	 * 
	 * @param v
	 */
	public void consume(int v) {
		sensorNode.getBattery().consume(v);
	}
	
	/**
	 * consumeTx
	 * 
	 * @param numberOfBits
	 */
	public void consumeTx(int numberOfBits) {
		if(radioConsoTxModel.equals(CLASSICAL_TX)) {
			sensorNode.getBattery().consume((numberOfBits/8.0) * eTx * pl/100.);
			return;
		}	
		if(radioConsoTxModel.equals(HEINZELMAN_TX)) {
			sensorNode.getBattery().consume(0.00000005 * numberOfBits + 0.0000000001*numberOfBits * (radioRangeRadius*pl/100.));
			return;
		}
		double v = EnergyConsumptionModel.evaluate("Consumption Model (TX)", getSensorNode().getId(), getName(), radioConsoTxModel, pl/100., numberOfBits, eTx, radioRangeRadius);
		sensorNode.getBattery().consume(v);
	}
	
	/**
	 * consumeRx
	 * 
	 * @param v
	 */
	public void consumeRx(int numberOfBits) {
		if(radioConsoRxModel.equals(CLASSICAL_RX)) {
			sensorNode.getBattery().consume((numberOfBits/8.0) * eRx);
			return;
		}
		
		if(radioConsoRxModel.equals(HEINZELMAN_RX)) { 
			sensorNode.getBattery().consume(0.00000005*numberOfBits);
			return;
		}
		
		double v = EnergyConsumptionModel.evaluate("Consumption Model (RX)", getSensorNode().getId(), getName(), radioConsoRxModel, pl/100., numberOfBits, eRx, radioRangeRadius);
		sensorNode.getBattery().consume(v);
	}
	
	public void setMy(int my) {
		this.my = my;
	}
	
	public void setPl(double pl) {
		this.pl = pl;
		this.getSensorNode().calculateRadioSpace();
	}
	
	public double getPl() {
		return pl;
	}
		
	public double getETx() {
		return eTx;
	}

	public void setETx(double eTx) {
		this.eTx = eTx;
	}

	public double getERx() {
		return eRx;
	}

	public void setERx(double eRx) {
		this.eRx = eRx;
	}
	
	public double getESleep() {
		return eSleep;
	}

	public void setESleep(double eSleep) {
		this.eSleep = eSleep;
	}

	public double getEListen() {
		return eListen;
	}

	public void setEListen(double eListen) {
		this.eListen = eListen;
	}

	public int getCh() {
		return ch;
	}

	public void setCh(int ch) {
		this.ch = ch;
	}

	public int getNId() {
		return nId;
	}

	public void setNId(int nId) {
		this.nId = nId;
	}
		
	/**
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 */
	public void drawRadioRadius(int x, int y, int r, Graphics g) {
		if (r > 0 && sensorNode.getDisplaydRadius()) {
			g.setColor(UColor.WHITE_TRANSPARENT);
			int lr1 = (int) (r * Math.cos(Math.PI / 4.));
			g.drawLine(x, y, (int) (x + lr1), (int) (y - lr1));
			g.drawString("" +r+" m", x + (lr1 / 2), (int) (y - (lr1 / 4.)));
		}		
	}

	public double getRequiredQuality() {
		return requiredQuality;
	}

	public void setRequiredQuality(double requiredQuality) {
		this.requiredQuality = requiredQuality;
	}

	public int getMy() {
		return my;
	}

	public double getTimeToResend() {
		return timeToResend;
	}

	public void setTimeToResend(double timeToResend) {
		this.timeToResend = timeToResend;
	}

	public int getNumberOfSends() {
		return numberOfSends;
	}

	public void setNumberOfSends(int numberOfSends) {
		this.numberOfSends = numberOfSends;
	}
	
	public double getRadioRangeRadius() {
		return radioRangeRadius;
	}	
	
	public void setRadioRangeRadius(double radioRangeRadius) {
		this.radioRangeRadius = radioRangeRadius;
		if(DeviceList.propagationsCalculated)			
			DeviceList.calculatePropagations();
	}
	
	public double getRadioRangeRadiusOri() {
		return radioRangeRadiusOri;
	}

	public void setRadioRangeRadiusOri(double radioRangeRadiusOri) {
		this.radioRangeRadiusOri = radioRangeRadiusOri;
	}
	
	public Color getRadioRangeColor1() {	
		return radioRangeColor1;
	}
	
	public Color getRadioRangeColor2() {	
		return radioRangeColor2;
	}
	
	public void resizeRadioRangeRadius(int i) {
		radioRangeRadius += i;
	}
	
	public void resizeRadioRangeRadiusOri(int i) {
		radioRangeRadius += i;
	}
	
	public abstract void init();

	public abstract double getTransmitPower();
	
	public void setTransmitPower(double transmitPower) {
		this.transmitPower = transmitPower;
	}
	
	public double getFrequency() {
		return frequency;
	}
	
	public abstract RadioModule duplicate(SensorNode sensorNode);
	
	public SensorNode getSensorNode() {
		return sensorNode;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	public void incAttempts() {
		attempts++;
	}
	
	public static RadioModule newRadioModule(SensorNode sensor, String name, String sStandard) {
		int standard = getStandardByName(sStandard);				
		return newRadioModule(sensor, name, standard);
	}
	
	public static RadioModule newRadioModule(SensorNode sensor, String name, int standard) {		
		if(standard == ZIGBEE_802_15_4) {
			return new RadioModule_ZigBee(sensor, name);
		}
		
		if(standard == LORA) {
			return new RadioModule_Lora(sensor, name);
		}
		
		if(standard == WIFI_802_11) {
			return new RadioModule_Wifi(sensor, name);
		}
		return null;
	}	
	
	public abstract void save(PrintStream fos, RadioModule currentRadioModule);
	
	public int getSpreadingFactor() {
		return -1;
	}
	
	public int getCodeRate() {
		return -1;
	}
	
	public void setSpreadingFactor(int v) {}
	public void setCodeRate(int v) {}
	
	public void setRadioConsoTxModel(String model) {
		radioConsoTxModel = model;
	}
	
	public String getRadioConsoTxModel() {
		return radioConsoTxModel;
	}
	
	public void setRadioConsoRxModel(String model) {
		radioConsoRxModel = model;
	}
	
	public String getRadioConsoRxModel() {
		return radioConsoRxModel;
	}
}
