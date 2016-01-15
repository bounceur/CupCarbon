/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2016 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/

/**
 * @author Ahcene Bounceur
 * @author Olivier Marc
 */

package visualisation;

import java.util.ArrayList;
import java.util.HashMap;

import com.virtualys.cupcarbon.communication.Communication;

import cupcarbon.CupCarbon;
import device.BaseStation;
import device.Device;
import device.Mobile;
import device.SensorNode;
import markers.Marker;


public class Visualisation {
	public static HashMap<String, ArrayList<Integer>> coArrowsToDrawing = new HashMap<String, ArrayList<Integer>>();
	public static HashMap<Integer, Boolean> coSensorsMarked = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Integer> coSensorsLed = new HashMap<Integer, Integer>();	
	
	public static void arrowDrawing(SensorNode sensor1, SensorNode sensor2, int type, int color, int size) {
		if(ThreeDUnity()) {
			// type :
			// 0 : Thin Arrow (size=1), continue line
			// 1 : Thin Arrow (size=1), dushed line
			// 2 : Big Arrow (size=2), continue line
			// Color : 
			// 0 : dark gray
			// 1 : light gray (close to white)
			// 2 : light gray 	
			// 3 : red
			// 4 : blue
			// 5 : black
			// 6 : green
			// 7 : orange
			// 8 : pink
			
			boolean lbToSend = false;
			String id = sensor1.getId() + "-" + sensor2.getId();
			if (coArrowsToDrawing.containsKey(id)) {
				ArrayList<Integer> values = coArrowsToDrawing.get(id);
				if (values.get(2)!=type || values.get(3)!=color|| values.get(4)!=size) {
					lbToSend = true;
				}				
			} else {
				ArrayList<Integer> l = new ArrayList<Integer>();
				l.add(sensor1.getId());
				l.add(sensor2.getId());
				l.add(type);
				l.add(color);
				l.add(size);
				coArrowsToDrawing.put(id, l);
				lbToSend = true;				
			}
			if (lbToSend) {
				Communication.getInstance().writeMessage((new StringBuffer("A_AR=")
															.append(sensor1.getId()).append(" ")
															.append(sensor2.getId()).append(" ")
															.append(type).append(" ")
															.append(color).append(" ")
															.append(size)).toString());
			}
			
			/*
			Communication.getInstance().writeMessage((new StringBuffer("A_AR=")
			.append(sensor1.getId()).append(" ")
			.append(sensor2.getId()).append(" ")
			.append(type).append(" ")
			.append(color).append(" ")
			.append(size)).toString());
			*/
		}
	}
	
	public static void comAddArrow(SensorNode sensor1, SensorNode sensor2, int type, int color, int size) {
		if(ThreeDUnity()) {
			Communication.getInstance().writeMessage((new StringBuffer("A_CA=")
														.append(sensor1.getId()).append(" ").append(sensor2.getId()).append(" ")
														.append(type).append(" ").append(color).append(" ").append(size)).toString());
		}
	}
	
	public static void comDeleteArrow(SensorNode sensor1, SensorNode sensor2) {
		if(ThreeDUnity()) {
			Communication.getInstance().writeMessage((new StringBuffer("D_CA=")
														.append(sensor1.getId()).append(" ").append(sensor2.getId()).append(" ")).toString());
		}
	}
	
	public static void removeDevice(Device device) {
		if(ThreeDUnity()) {
			Communication.getInstance().writeMessage("D_DE=" + device.getId());
		}
	}
	
	public static void addStdSensorNode(SensorNode sensorNode) {
		if(ThreeDUnity()) {
			/*
			System.out.println("Standard Sensor Node added!");
			double longitude = sensorNode.getLongitude();
			double latitude = sensorNode.getLatitude();
			double elevation = sensorNode.getElevation();
			double radioRadius = sensorNode.getRadioRadius();
			double sensorUnitRadius = sensorNode.getSensorUnitRadius();
			int ch = sensorNode.getCh();
			int my = sensorNode.getMy();
			int id = sensorNode.getId();
			int nid = sensorNode.getNId();
			int pl = sensorNode.getPl();
			double energyMax = sensorNode.getBattery().getInitialLevel();
			double batteryLevel = sensorNode.getBatteryLevel();
			double eTx = sensorNode.getETx();
			double eRx = sensorNode.getERx();
			double sleepE = sensorNode.getESlp();
			double sensingE = sensorNode.getES();
			double listeningE = sensorNode.getEL();
			double bufferLevel = sensorNode.getBufferIndex()*1.0/sensorNode.getBufferSize()*100.;
			String message = sensorNode.getMessage(); 
			boolean marked = sensorNode.isMarked();
			int led = sensorNode.getLedColor();
			String gpsFileName = sensorNode.getGPSFileName();
			String scriptFileName = sensorNode.getScriptFileName();
			*/
		}
	}
	
	public static void updateStdSensorNode(SensorNode sensorNode) {
		if(ThreeDUnity()) {
			//
			//
			/*
			boolean lbToSendLed = false;
			if (coSensorsLed.containsKey(sensorNode.getId())) {
				if (sensorNode.getLedColor() != coSensorsLed.get(sensorNode.getId())) {
					System.out.println("Standard Sensor Node updated! " + sensorNode.getLedColor() + " " + coSensorsLed.get(sensorNode.getId()));
					lbToSendLed = true;
				}
			} else {
				coSensorsLed.put(sensorNode.getId(), sensorNode.getLedColor());
				if (sensorNode.isMarked()) {
					lbToSendLed = true;
				}
			}
			if (lbToSendLed) {
				Communication.getInstance().writeMessage((new StringBuffer("S_L=")).append(sensorNode.getId()).append(" ").append(sensorNode.getLedColor()).toString());
			}
			*/
			Communication.getInstance().writeMessage((new StringBuffer("S_L=")).append(sensorNode.getId()).append(" ").append(sensorNode.getLedColor()).toString());
			//
			//
			/*
			boolean lbToSendMark = false;
			if (coSensorsMarked.containsKey(sensorNode.getId())) {
				if (sensorNode.isMarked() != coSensorsMarked.get(sensorNode.getId())) {
					lbToSendMark = true;
				}
			} else {
				coSensorsMarked.put(sensorNode.getId(), Boolean.valueOf(sensorNode.isMarked()));
				if (sensorNode.isMarked()) {
					lbToSendMark = true;
				}
			}
			if (lbToSendMark) {
				Communication.getInstance().writeMessage((new StringBuffer("S_M=")).append(sensorNode.getId()).append(" ").append(sensorNode.isMarked()).toString());
			}
			*/
			//
			/*
			System.out.println("Standard Sensor Node updated!");
			double longitude = sensorNode.getLongitude();
			double latitude = sensorNode.getLatitude();
			double elevation = sensorNode.getElevation();
			double radioRadius = sensorNode.getRadioRadius();
			double sensorUnitRadius = sensorNode.getSensorUnitRadius();
			int ch = sensorNode.getCh();
			int my = sensorNode.getMy();
			int id = sensorNode.getId();
			int nid = sensorNode.getNId();
			int pl = sensorNode.getPl();
			double energyMax = sensorNode.getBattery().getInitialLevel();
			double batteryLevel = sensorNode.getBatteryLevel();
			double eTx = sensorNode.getETx();
			double eRx = sensorNode.getERx();
			double sleepE = sensorNode.getESlp();
			double sensingE = sensorNode.getES();
			double listeningE = sensorNode.getEL();
			double bufferLevel = sensorNode.getBufferIndex()*1.0/sensorNode.getBufferSize()*100.;
			String message = sensorNode.getMessage();
			boolean marked = sensorNode.isMarked();
			int led = sensorNode.getLedColor();
			String gpsFileName = sensorNode.getGPSFileName();
			String scriptFileName = sensorNode.getScriptFileName();
			*/
		}
	}
	
	public static void addBaseStation(BaseStation baseStation) {
		if(ThreeDUnity()) {
			/*
			System.out.println("Base station added!");
			double longitude = baseStation.getLongitude();
			double latitude = baseStation.getLatitude();
			double elevation = baseStation.getElevation();
			double radioRadius = baseStation.getRadioRadius();
			double sensorUnitRadius = baseStation.getSensorUnitRadius();
			int ch = baseStation.getCh();
			int my = baseStation.getMy();
			int id = baseStation.getId();
			int nid = baseStation.getNId();
			int pl = baseStation.getPl();
			double energyMax = baseStation.getBattery().getInitialLevel();
			double batteryLevel = baseStation.getBatteryLevel();
			double eTx = baseStation.getETx();
			double eRx = baseStation.getERx();
			double sleepE = baseStation.getESlp();
			double sensingE = baseStation.getES();
			double listeningE = baseStation.getEL();
			double bufferLevel = baseStation.getBufferIndex()*1.0/baseStation.getBufferSize()*100.;
			String message = baseStation.getMessage(); 
			*/
		}
	}
	
	public static void addMobile(Mobile mobile) {
		if(ThreeDUnity()) {
			/*
			System.out.println("Mobile added!");
			double longitude = mobile.getLongitude();
			double latitude = mobile.getLatitude();
			double elevation = mobile.getElevation();
			*/			
		}
	}
	
	public static void addMarker(Marker marker) {
		if(ThreeDUnity()) {
			/*
			System.out.println("Marker added!");
			double longitude = marker.getLongitude();
			double latitude = marker.getLatitude();
			double elevation = marker.getElevation();
			*/		
		}
	}
	
	public static void updatePosition(int type, int id, Device device) {
		if(ThreeDUnity()) {
			/*
			double longitude = device.getLongitude();
			double latitude = device.getLatitude();
			double elevation = device.getElevation();	
			System.out.println("The position of Object "+id+" is updated");
			*/
		}
	}
	
	public static void updateRadioRadius(int type, int id, double value) {
		if(ThreeDUnity()) {
	//		System.out.println("The radio radius of Object "+id+" is updated");
		}
	}
	
	public static void updateSensorUnitRadius(Device device, double value) {
		if(ThreeDUnity()) {
			//int id = device.getId();
		//	System.out.println("The sensor unit radius of Object "+id+" is updated");
		}
	}
	
	public static void updateCh(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The ch of Object "+id+" is updated");
		}
	}
	
	public static void updateId(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The id of Object "+id+" is updated");
		}
	}
	
	public static void updateNid(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The nid of Object "+id+" is updated");
		}
	}
	
	public static void updateMy(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The my of Object "+id+" is updated");
		}
	}
	
	public static void updatePl(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The pl of Object "+id+" is updated");
		}
	}
	
	public static void updateEnergyMax(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Energy max of Object "+id+" is updated");
		}
	}
	
	public static void updateBatteryLevel(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The battery level of Object "+id+" is updated");
		}
	}
	
	public static void updateETx(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Tx energy of Object "+id+" is updated");
		}
	}
	
	public static void updateERx(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Tx energy of Object "+id+" is updated");
		}
	}
	
	public static void updateSleepE(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Sleeping energy of Object "+id+" is updated");
		}
	}
	
	public static void updateSensingE(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Sensing energy of Object "+id+" is updated");
		}
	}
	
	public static void updateListeningE(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Listening energy of Object "+id+" is updated");
		}
	}
	
	public static void updateBufferLevel(int type, int id, double value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Buffer level of Object "+id+" is updated");
		}
	}
	
	public static void updateMessage(int type, int id, int value) {
		if(ThreeDUnity()) {
		//	System.out.println("The message of Object "+id+" is updated");
		}
	}
	
	public static void updateGpsFileName(int type, int id, String value) {
		if(ThreeDUnity()) {
		//	System.out.println("The GPS File Name of Object "+id+" is updated");
		}
	}
	
	public static void updateScriptFileName(int type, int id, String value) {
		if(ThreeDUnity()) {
		//	System.out.println("The Script File Name of Object "+id+" is updated");
		}
	}
	
	
	public static void drawText(double longitude, double latitude, double elevation) {
		// Write text in the given GPS position 
		if(ThreeDUnity()) {
			
		}
	}
	
	public static void moveDevice(Device device) {
		/*
		System.out.println("Move device!");
		double longitude = device.getLongitude();
		double latitude = device.getLatitude();
		double elevation = device.getElevation();
		*/
	}
	

	
	//------------------------------------------------------------------
	
	public static boolean ThreeDUnity() {
		return CupCarbon.ihmType == CupCarbon.THREE_D_UNITY;
	}
	
}
