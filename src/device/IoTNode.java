/*----------------------------------------------------------------------------------------------------------------
q * CupCarbon: IoT Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2021 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * 
 * IoTNode
 * 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package device;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.python.util.PythonInterpreter;

import cupcarbon.CupCarbon;
import map.MapLayer;
import project.Project;
import simulation.Simulation;
import simulation.SimulationInputs;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class IoTNode extends IoTNodeCom {
	
	protected boolean connected = false;
	
	private LinkedList<String> topics = new LinkedList<String>();
	
	protected PythonInterpreter interpreter = new PythonInterpreter();
	
	private IoTThreadMonitor thMonitor = null;
	
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
	public IoTNode(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
		super(x, y, z, radius, radioRadius, suRadius, id);
		initNode();
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
	public IoTNode(String id, String x, String y, String z, String radius, String radioRadius, String suRadius, String gpsFileName, String scriptFileName) {
		super(id, x, y, z, radius, radioRadius, suRadius, gpsFileName, scriptFileName);
		initNode();
	}
	
	public void initImages() {
		image_marked = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/car_marked.png")).getImage();
		image_non_marked = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/car.png")).getImage();
	}
	
	public void initNode() {
		this.hide = 1;
		
		initImages();
		
		initMqttModule();
	}
	
	public void initMqttModule() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mqttModule = new IoTMqttModule(IoTNode.this);
					initRadio();
				} catch (MqttException e) {
					e.printStackTrace();
				}		
			}
		}).start();
	}
	
	public void initRadio() {
		mqttModule.setMy(getCurrentRadioModule().getMy());
	}
	
	@Override
	public int getType() {
		return Device.IOT;
	}
	
	public void setThreadMonitor(IoTThreadMonitor thMonitor) {
		this.thMonitor = thMonitor; 
	}
	
	public void incNumberOfFinishedSimulatedIoTNodes() {
		thMonitor.inc(this);
	}
		
	public void subscribe(String topic) {
		mqttModule.subscribe(topic);
		topics.add(topic);
	}
	
	public void unsubscribeFromAllTopics() {
		for(String topic : topics) {
			mqttModule.unsubscribe(topic);
		}
	}
	
	public void unsubscribe(String topic) {
		mqttModule.unsubscribe(topic);
	}
	
	public void publish(String topic, String message) {
		try {
			mqttModule.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publish(String topic, double message) {
		try {
			mqttModule.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publish(String topic, int message) {
		try {
			mqttModule.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public boolean loop() {
		return Simulation.simulating;
	}
	
	public void location(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		MapLayer.repaint();
	}
	
	public void location(String location) {
		longitude = Double.parseDouble(location.split(",")[0]);
		latitude = Double.parseDouble(location.split(",")[1]);
		MapLayer.repaint();
	}
	
	
	private PrintStream ps = null;
	
	public void cgps(String fileName) {
		try {
			if(!fileName.endsWith(".gps")) fileName += ".gps";
			ps = new PrintStream(new FileOutputStream(Project.getGpsFileFromName(fileName)));
			ps.println("Route of Node "+getId());
			ps.println("From place 1");
			ps.println("To place 2");
			ps.println(false);
			ps.println("0");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void location(String location, String fileName) {
		if(!fileName.endsWith(".gps")) fileName += ".gps";
		longitude = Double.parseDouble(location.split(",")[0]);
		latitude = Double.parseDouble(location.split(",")[1]);
		MapLayer.repaint();
		
		ps.println(1 + " " + longitude + " " + latitude + " 0 4.0");
		ps.flush();
	}
	
	public PythonInterpreter getInterpreter() {
		return interpreter;
	}
	
	public void runGPS() {
		if(!IoTNode.this.getRoute().isEmpty()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					IoTNode.this.fixori();
					while(Simulation.simulating) {
						IoTNode.this.moveToNext(true, SimulationInputs.visualDelay);
					}
					IoTNode.this.toOri();
				}
			}).start();
		}
	}
	
	/*public void initBroker() throws MqttException {
		mqttModule.initConnexion();
	}*/
	
	@Override
	public void runIoTScript() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Simulation.setSimulating(true);
					iotMessages.removeAll(iotMessages);
					interpreter.exec("from device import DeviceList");
					interpreter.exec("node=DeviceList.getIoTNodeById("+id+")");
					interpreter.execfile(Project.getProjectScriptPath() + File.separator + getScriptFileName());
					unsubscribeFromAllTopics();
					interpreter.exec("node.incNumberOfFinishedSimulatedIoTNodes()");
					interpreter.close();
					
				}
				catch(Exception e) {
					Simulation.setSimulating(false);
					unsubscribeFromAllTopics();
					System.out.println("IoT Simuation: ERROR!");
					CupCarbon.cupCarbonController.displayShortErrMessage("Error: Simulation");
					CupCarbon.cupCarbonController.qRunIoTSimulationButton.setDisable(false);
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public String startingName() {
		return "iot";
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	@Override
	public void drawOvalSelection(int x, int y, int rayon, Graphics2D g) {
		
	}
	
	@Override
	public void drawSelection(int x, int y, int rayon, Graphics2D g) {
		if (inside || selected) {
			g.setColor(Color.LIGHT_GRAY);
			if(selected) g.setColor(Color.GRAY);
			if(MapLayer.dark) g.setColor(Color.WHITE);
			int d = 3;
			int r = rayon ; 
			g.drawLine(x-r-d, y-r-d, x-r+d-1, y-r-d);
			g.drawLine(x-r-d, y-r-d, x-r-d, y-r+d-1);
			g.drawLine(x-r-d, y+r+d, x-r+d-1, y+r+d);
			g.drawLine(x-r-d, y+r+d, x-r-d, y+r-d-1);			
			g.drawLine(x+r+d, y-r-d, x+r-d-1, y-r-d);
			g.drawLine(x+r+d, y-r-d, x+r+d, y-r+d-1);			
			g.drawLine(x+r+d, y+r+d, x+r-d-1, y+r+d);
			g.drawLine(x+r+d, y+r+d, x+r+d, y+r-d-1);
		}
	}
	
	@Override
	public void drawRectSelection(int x, int y, int rayon, Graphics2D g) {
		if (selected) {
			g.setColor(Color.LIGHT_GRAY);
			if(selected) g.setColor(Color.GRAY);
			if(MapLayer.dark) g.setColor(Color.RED);
			int d = 5;
			int r = rayon ; 
			g.drawLine(x-r-d, y-r-d, x-r+d-1, y-r-d);
			g.drawLine(x-r-d, y-r-d, x-r-d, y-r+d-1);
			g.drawLine(x-r-d, y+r+d, x-r+d-1, y+r+d);
			g.drawLine(x-r-d, y+r+d, x-r-d, y+r-d-1);			
			g.drawLine(x+r+d, y-r-d, x+r-d-1, y-r-d);
			g.drawLine(x+r+d, y-r-d, x+r+d, y-r+d-1);			
			g.drawLine(x+r+d, y+r+d, x+r-d-1, y+r+d);
			g.drawLine(x+r+d, y+r+d, x+r+d, y+r-d-1);
		}
	}
	
}
