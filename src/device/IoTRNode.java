/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: IoT Simulator
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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class IoTRNode extends IoTNode {
		
	
	protected Image image_connected ;
	protected Image image_non_connected ;
	
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
	public IoTRNode(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
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
	public IoTRNode(String id, String x, String y, String z, String radius, String radioRadius, String suRadius, String gpsFileName, String scriptFileName) {
		super(id, x, y, z, radius, radioRadius, suRadius, gpsFileName, scriptFileName);
	}
	
	@Override
	public int getType() {
		return Device.RIOT;
	}
	
	public void initImages() {
		image_marked = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/iot_rnode_marked.png")).getImage();
		image_non_connected = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/iot_rnode_off.png")).getImage();
		image_connected = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/iot_rnode_on.png")).getImage();
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
				g.drawImage(image_marked, x-15, y-15, null);
			}
			else {
				if(connected)
					g.drawImage(image_connected, x-15, y-15, null);
				else
					g.drawImage(image_non_connected, x-15, y-15, null);
			}
		}
	}
				
	public String startingName() {
		return "riot";
	}
	
	@Override
	public void initNode() {
		
		//System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());
		
		this.hide = 1;
		
		initImages();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mqttModule = new IoTMqttModule(IoTRNode.this);
					mqttModule.subscribe(IoTMqttModule.com_real_node_topic+"/s/"+getId());
				} catch (MqttException e) {
					e.printStackTrace();
				}		
			}
		}).start();
	}
	
	public void drawRadius(int x, int y, int r, Graphics g) {
		
	}
	
	public void drawRadioRadius(int x, int y, int r, Graphics g) {
		
	}

	public void drawTheCenter(Graphics g, int x, int y) {
	
	}
	
	@Override
	public void runIoTScript() {
		try {
			setRadioParameters();
			connected = false;
			mqttModule.publish(IoTMqttModule.com_real_node_topic+"/r/"+getId(), "HI");
			System.out.println("pub");
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publishStopSimulation() {
		try {
			mqttModule.publish(IoTMqttModule.com_real_node_topic+"/r/"+getId(), "0");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
}
