/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: IoT Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2021 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * 
 * IoTNodeRadio
 * 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package device;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.MqttException;

import map.MapLayer;
import project.Project;
import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public abstract class IoTNodeCom extends StdSensorNode {
	
	protected IoTMqttModule mqttModule; 
	
	protected LinkedList<String> iotMessages = new LinkedList<String>();
	
	protected String value = "";
	
	protected Image image_marked ;
	protected Image image_non_marked ;
	
	/**
	 * Constructor 3
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public IoTNodeCom(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
		super(x, y, z, radius, radioRadius, suRadius, id);
	}
	
	/**
	 * Constructor 6
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param gpsFileName
	 *            The path of the GPS file
	 * @param scriptFileName
	 *            The path of the script file
	 */
	public IoTNodeCom(String id, String x, String y, String z, String radius, String radioRadius, String suRadius, String gpsFileName, String scriptFileName) {
		super(id, x, y, z, radius, radioRadius, suRadius, gpsFileName, scriptFileName);
	}
	
	/*public void initConnexion() {
		snew Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mqttModule.initConnexion();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}*/
	
	public abstract void initImages();
	
	@Override
	public String getIdFL() {
		return "IOT";
	}
	
	@Override
	public int getTextXPosition() {
		return 20;
	}
	
	@Override
	public String getName() {
		return getIdFL() + id;
	}

	@Override
	public void save(String ref) {
		String fileName = Project.getProjectNodePath();
		try {
			PrintStream fos = null;	
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + startingName()+"_" + ref));
			fos.println("List of parameters");
			fos.println("------------------------------------------");
			fos.println("device_type:" + getType());
			fos.println("device_id:" + getId());
			fos.println("device_longitude:" + getLongitude());
			fos.println("device_latitude:" + getLatitude());
			fos.println("device_elevation:" + getElevation());
			fos.println("device_radius:" + getRadius());
			fos.println("device_hide:" + getHide());
			fos.println("device_draw_battery:" + getDrawBatteryLevel());
			fos.println("device_sensor_unit_radius:" + getSensorUnitRadius());			
			if (!getGPSFileName().equals(""))
				fos.println("device_gps_file_name:" + getGPSFileName());
			if (!getScriptFileName().equals(""))
				fos.println("device_script_file_name:" + getScriptFileName());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		saveRadioModule(Project.getProjectRadioPath() + File.separator + startingName()+"_"+ref);
	}
	
	@Override
	public void draw(Graphics g2) {
		super.draw(g2);
		int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int x = coord[0];
		int y = coord[1];
		
		Graphics2D g = (Graphics2D) g2;
		g.drawString(value, x+20, y+15);
	}
	
	@Override
	public void drawMarked(Graphics g2) {
		if (!isDead()) {
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.4f));
			if (ledColor>=1) {
				g.drawImage(image_marked, x-16, y-16, null);
			}
			else
				g.drawImage(image_non_marked, x, y+10, null);
		}
	}
	
	@Override
	public SensorNode createNewWithTheSameType() {
		return new IoTNode(longitude, latitude, elevation, radius, 0.0, sensorUnit.getRadius(), DeviceList.number++);
	}
	
	public void addMessage(String message) {
		iotMessages.push(message);
	}
	
	public int bufferSize() {
		return iotMessages.size();
	}
	
	public String read() {
		if(iotMessages.size()>0) 
			return iotMessages.pop();
		return "";
	}
		
	public void send(String message, int ... args ) {
		try {
			mqttModule.send(id+"#"+message, args);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void send(int message, int ... args) {
		send(message+"", args); 
	}
	
	public void send(double message, int ... args) {
		send(message+"", args); 
	}
	
	public void send(float message, int ... args) {
		send(message+"", args); 
	}
	
	public void print(String v) {
		value = v;
		MapLayer.repaint();
	}
	
	public void print(Double v) {
		value = v+"";
		MapLayer.repaint();
	}
	
	public int id() {
		return id;
	}

	public void addToSenders(IoTNode node) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				senders.add(node);
				MapLayer.repaint();
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				senders.remove(node);
				MapLayer.repaint();
			}
		}).start();
	}
	
	//===============================================================
	//	Radio Parameters
	//===============================================================
	
	public void setRadioParameters() {
		mqttModule.setId();
		mqttModule.setMy();
	}
		
	/*
	@Override
	public int atget(String v) {
		return 0;
	}

	@Override
	public int[] atnd(int nd) {
		return null;
	}

	@Override
	public void atch(int ch) {
		//mqttModule.setCh(ch);		
	}

	@Override
	public void atid(int id) {
		mqttModule.setId(id);
		
	}

	@Override
	public void atmy(int my) {
		mqttModule.setMy(my);
	}

	@Override
	public void atpl(int pl) {
		//mqttModule.setPl(pl);
	}*/
	
}
