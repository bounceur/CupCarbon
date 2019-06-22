package radio_module;

import java.io.PrintStream;

import device.SensorNode;
import utilities.UColor;

public class RadioModule_ZigBee extends RadioModule {

	public RadioModule_ZigBee(SensorNode sensorNode, String name) {
		super(sensorNode, name);
		init();
	}

	public void init() {
		frequency = 2.4e9; 
		radioRangeColor1 = UColor.PURPLE_TTRANSPARENT;
		radioRangeColor2 = UColor.PURPLE_TRANSPARENT;
//		if(MapLayer.dark) {
//			radioRangeColor1 = UColor.PURPLE_TRANSPARENT;
//			radioRangeColor2 = UColor.PURPLE_DARK_TRANSPARENT;
//		}
		setRadioRangeRadius(100);
		setRadioRangeRadiusOri(100);
	}	
	
	public int getStandard() {
		return RadioModule.ZIGBEE_802_15_4;
	}	
	
	@Override
	public String getStandardName() {
		return "ZIGBEE";
	}
	
	public double getTransmitPower() {
		double tpW = (Math.pow(10, transmitPower/10.))/1000.;
		double nTpW = tpW * 1e6;
		nTpW = nTpW * pl /100.;
		double nTpDbm = 10*Math.log10(nTpW/1000.);
		return nTpDbm;
	}

	@Override
	public RadioModule duplicate(SensorNode sensorNode) {
		RadioModule_ZigBee nRadioModule = new RadioModule_ZigBee(sensorNode, name);
		nRadioModule.setMy(getMy());
		nRadioModule.setCh(getCh());
		nRadioModule.setNId(getNId());
		nRadioModule.setRadioRangeRadius(getRadioRangeRadius());
		nRadioModule.setETx(getETx());
		nRadioModule.setERx(getERx());
		nRadioModule.setESleep(getESleep());
		nRadioModule.setEListen(getEListen());
		nRadioModule.setRadioDataRate(getRadioDataRate());
		nRadioModule.setRadioConsoTxModel(getRadioConsoTxModel());
		nRadioModule.setRadioConsoRxModel(getRadioConsoRxModel());
		return nRadioModule;
	}
	
	public static String getPayLoad(String data) {
		return "";
	}
	
	@Override
	public void save(PrintStream fos, RadioModule currentRadioModule) {
		if (currentRadioModule.getName().equals(getName()))
			fos.println("current_radio_name:"+ getName());
		else
			fos.println("radio_name:"+ getName());
		fos.println("radio_standard:" + getStandardName());
		fos.println("radio_my:" + getMy());
		fos.println("radio_channel:" + getCh());
		fos.println("radio_network_id:" + getNId());
		fos.println("radio_radius:" + getRadioRangeRadius());
		fos.println("radio_etx:" + getETx());
		fos.println("radio_erx:" + getERx());
		fos.println("radio_esleep:" + getESleep());
		fos.println("radio_elisten:" + getEListen());
		fos.println("radio_data_rate:" + getRadioDataRate());
		fos.println("conso_tx_model:" + getRadioConsoTxModel());
		fos.println("conso_rx_model:" + getRadioConsoRxModel());
	}
}
