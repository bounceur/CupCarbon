package ext_visualisation;

import device.BaseStation;
import device.Device;
import device.Mobile;
import device.SensorNode;
import markers.Marker;

public class Visualisation2 {
	
	public static void changeColorOfArrows(int color) {
		if(visual()) {
			
		}
	}
	
	public static void arrowDrawing(SensorNode sensor1, SensorNode sensor2, int type, int color, int size) {
		if(visual()) {
			//System.out.println("Arrow between "+sensor1+" and "+sensor2+" is added.");
//			double longitude1 = sensor1.getLongitude() ;
//			double latitude1 = sensor1.getLatitude() ;
//			double elevation1 = sensor1.getElevation() ; 
//			double longitude2 = sensor2.getLongitude() ;
//			double latitude2 = sensor2.getLatitude() ;
//			double elevation2 = sensor2.getElevation() ;
			// type :
			// 0 : flèche fine (size=1?) et continue
			// 1 : flèche fine (size=1?) et pointillée
			// 2 : flèche grosse (size=2?) et continue
			// Color : 
			// 0 : gris foncé (proche du noir)
			// 1 : gris transparent (proche de rien)
			// 2 : gris clair			
			// 3 : rouge
			// 4 : bleu
			// 5 : noir
			// 6 : vert
			// 7 : orange
			// 8 : rose
		}
	}
	
	public static void comAddArrow(SensorNode sensor1, SensorNode sensor2, int type, int color, int size) {
		if(visual()) {
			//System.out.println("Arrow between "+sensor1+" and "+sensor2+" is added.");
//			double longitude1 = sensor1.getLongitude() ;
//			double latitude1 = sensor1.getLatitude() ;
//			double elevation1 = sensor1.getElevation() ; 
//			double longitude2 = sensor2.getLongitude() ;
//			double latitude2 = sensor2.getLatitude() ;
//			double elevation2 = sensor2.getElevation() ;
		}
	}
	
	public static void comDeleteArrow(SensorNode sensor1, SensorNode sensor2) {
		if(visual()) {
			//System.out.println("Delete Com Arrow between ");
			
		}
	}
	
	public static void removeDevice(Device device) {
		if(visual()) {
			
		}
	}
	
	public static void addStdSensorNode(SensorNode sensorNode) {
		if(visual()) {
			//System.out.println("Standard Sensor Node added!");
//			double longitude = sensorNode.getLongitude();
//			double latitude = sensorNode.getLatitude();
//			double elevation = sensorNode.getElevation();
//			double radioRadius = sensorNode.getRadioRadius();
//			double sensorUnitRadius = sensorNode.getSensorUnitRadius();
//			int ch = sensorNode.getCh();
//			int my = sensorNode.getMy();
//			int id = sensorNode.getId();
//			int nid = sensorNode.getNId();
//			int pl = sensorNode.getPl();
//			double energyMax = sensorNode.getBattery().getInitialLevel();
//			double batteryLevel = sensorNode.getBatteryLevel();
//			double eTx = sensorNode.getETx();
//			double eRx = sensorNode.getERx();
//			double sleepE = sensorNode.getESlp();
//			double sensingE = sensorNode.getES();
//			double listeningE = sensorNode.getEL();
//			double bufferLevel = sensorNode.getBufferIndex()*1.0/sensorNode.getBufferSize()*100.;
//			String message = sensorNode.getMessage(); 
//			boolean marked = sensorNode.isMarked();
//			int led = sensorNode.getLedColor();
//			String gpsFileName = sensorNode.getGPSFileName();
//			String scriptFileName = sensorNode.getScriptFileName();
		}
	}
	
	public static void updateStdSensorNode(SensorNode sensorNode) {
		if(visual()) {
			//System.out.println("Standard Sensor Node updated!");
//			double longitude = sensorNode.getLongitude();
//			double latitude = sensorNode.getLatitude();
//			double elevation = sensorNode.getElevation();
//			double radioRadius = sensorNode.getRadioRadius();
//			double sensorUnitRadius = sensorNode.getSensorUnitRadius();
//			int ch = sensorNode.getCh();
//			int my = sensorNode.getMy();
//			int id = sensorNode.getId();
//			int nid = sensorNode.getNId();
//			int pl = sensorNode.getPl();
//			double energyMax = sensorNode.getBattery().getInitialLevel();
//			double batteryLevel = sensorNode.getBatteryLevel();
//			double eTx = sensorNode.getETx();
//			double eRx = sensorNode.getERx();
//			double sleepE = sensorNode.getESlp();
//			double sensingE = sensorNode.getES();
//			double listeningE = sensorNode.getEL();
//			double bufferLevel = sensorNode.getBufferIndex()*1.0/sensorNode.getBufferSize()*100.;
//			String message = sensorNode.getMessage(); 
//			boolean marked = sensorNode.isMarked();
//			int led = sensorNode.getLedColor();
//			String gpsFileName = sensorNode.getGPSFileName();
//			String scriptFileName = sensorNode.getScriptFileName();
		}
	}
	
	public static void addBaseStation(BaseStation baseStation) {
		if(visual()) {
			//System.out.println("Base station added!");
//			double longitude = baseStation.getLongitude();
//			double latitude = baseStation.getLatitude();
//			double elevation = baseStation.getElevation();
//			double radioRadius = baseStation.getRadioRadius();
//			double sensorUnitRadius = baseStation.getSensorUnitRadius();
//			int ch = baseStation.getCh();
//			int my = baseStation.getMy();
//			int id = baseStation.getId();
//			int nid = baseStation.getNId();
//			int pl = baseStation.getPl();
//			double energyMax = baseStation.getBattery().getInitialLevel();
//			double batteryLevel = baseStation.getBatteryLevel();
//			double eTx = baseStation.getETx();
//			double eRx = baseStation.getERx();
//			double sleepE = baseStation.getESlp();
//			double sensingE = baseStation.getES();
//			double listeningE = baseStation.getEL();
//			double bufferLevel = baseStation.getBufferIndex()*1.0/baseStation.getBufferSize()*100.;
//			String message = baseStation.getMessage(); 
		}
	}
	
	public static void addMobile(Mobile mobile) {
		if(visual()) {
			//System.out.println("Mobile added!");
//			double longitude = mobile.getLongitude();
//			double latitude = mobile.getLatitude();
//			double elevation = mobile.getElevation();			
		}
	}
	
	public static void addMarker(Marker marker) {
		if(visual()) {
			//System.out.println("Marker added!");
//			double longitude = marker.getLongitude();
//			double latitude = marker.getLatitude();
//			double elevation = marker.getElevation();		
		}
	}
	
	public static void updatePosition(int type, int id, Device device) {
		if(visual()) {
//			double longitude = device.getLongitude();
//			double latitude = device.getLatitude();
//			double elevation = device.getElevation();	
			//System.out.println("The position of Object "+id+" is updated");
		}
	}
	
	public static void updateRadioRadius(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The radio radius of Object "+id+" is updated");
		}
	}
	
	public static void updateSensorUnitRadius(Device device, double value) {
		if(visual()) {
//			int id = device.getId();
			//System.out.println("The sensor unit radius of Object "+id+" is updated");
		}
	}
	
	public static void updateCh(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The ch of Object "+id+" is updated");
		}
	}
	
	public static void updateId(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The id of Object "+id+" is updated");
		}
	}
	
	public static void updateNid(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The nid of Object "+id+" is updated");
		}
	}
	
	public static void updateMy(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The my of Object "+id+" is updated");
		}
	}
	
	public static void updatePl(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The pl of Object "+id+" is updated");
		}
	}
	
	public static void updateEnergyMax(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Energy max of Object "+id+" is updated");
		}
	}
	
	public static void updateBatteryLevel(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The battery level of Object "+id+" is updated");
		}
	}
	
	public static void updateETx(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Tx energy of Object "+id+" is updated");
		}
	}
	
	public static void updateERx(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Tx energy of Object "+id+" is updated");
		}
	}
	
	public static void updateSleepE(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Sleeping energy of Object "+id+" is updated");
		}
	}
	
	public static void updateSensingE(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Sensing energy of Object "+id+" is updated");
		}
	}
	
	public static void updateListeningE(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Listening energy of Object "+id+" is updated");
		}
	}
	
	public static void updateBufferLevel(int type, int id, double value) {
		if(visual()) {
			//System.out.println("The Buffer level of Object "+id+" is updated");
		}
	}
	
	public static void updateMessage(int type, int id, int value) {
		if(visual()) {
			//System.out.println("The message of Object "+id+" is updated");
		}
	}
	
	public static void updateGpsFileName(int type, int id, String value) {
		if(visual()) {
			//System.out.println("The GPS File Name of Object "+id+" is updated");
		}
	}
	
	public static void updateScriptFileName(int type, int id, String value) {
		if(visual()) {
			//System.out.println("The Script File Name of Object "+id+" is updated");
		}
	}
	
	
	public static void drawText(double longitude, double latitude, double elevation) {
		// Write text in the given GPS position 
		if(visual()) {
			
		}
	}
	
	public static void moveDevice(Device device) {
		//System.out.println("Move device!");
//		double longitude = device.getLongitude();
//		double latitude = device.getLatitude();
//		double elevation = device.getElevation();
	}
	

	
	//------------------------------------------------------------------
	
	public static boolean visual() {
		return true; //CupCarbon_old.ihmType == CupCarbon_old.THREE_D_UNITY;
	}
	
}
