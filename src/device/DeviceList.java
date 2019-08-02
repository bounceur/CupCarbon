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
 *----------------------------------------------------------------------------------------------------------------*/

package device;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import action.CupAction;
import action.CupActionAddSensor;
import action.CupActionBlock;
import action.CupActionDeleteDevice;
import action.CupActionDeleteSensor;
import action.CupActionStack;
import buildings.Building;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import geometry.SNEdge;
import javafx.application.Platform;
import map.MapLayer;
import map.NetworkParameters;
import markers.Marker;
import markers.MarkerList;
//import markers.Marker;
import natural_events.Gas;
import natural_events.Weather;
import project.Project;
import simulation.WisenSimulation;
import solver.SensorGraph;
import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class DeviceList {

	public static Weather weather = null;
	public static Vector<SensorNode> sensors = new Vector<SensorNode>();
	public static Vector<Device> devices = new Vector<Device>();
	public static Vector<SNEdge> markedEdges = new Vector<SNEdge>();
	
	public static boolean drawLinks = true;
	public static LinkedList<LinkedList<Integer>> hulls = new LinkedList<LinkedList<Integer>>();
	public static int number = 1;
	public static boolean propagationsCalculated = false;
	
	public DeviceList() {
		reset();
	}
	
	public static void reset() {
		weather = null;
		sensors = new Vector<SensorNode>();
		devices = new Vector<Device>();
		drawLinks = true;
		hulls = new LinkedList<LinkedList<Integer>>();
		markedEdges = new Vector<SNEdge>();
		DeviceList.propagationsCalculated = false;
	}
	
	/**
	 * @return a node by its id
	 */
	public static Device getNodeById(int id) {
		for(SensorNode sensor : sensors) {
			if(sensor.getId() == id) return sensor;
		}
		for(Device device : devices) {
			if(device.getId() == id) return device;
		}
		return null;
	}
	
	/**
	 * @return a node by its name
	 */
	public static Device getNodeByName(String name) {
		for(SensorNode sensor : sensors) {
			if(sensor.getName().equals(name)) return sensor;
		}
		for(Device device : devices) {
			if(device.getName().equals(name)) return device;
		}
		return null;
	}
	
	/**
	 * @return a sensor node by its id
	 */
	public static SensorNode getSensorNodeById(int id) {		
		for (SensorNode sensor : sensors) {
			if(sensor.getId() == id) return sensor;
		}
		return null;
	}
	
	/**
	 * @return a sensor node by its my
	 */
	public static SensorNode getSensorNodeByMy(int my) {		
		for(SensorNode device : sensors) {
			if(device.getCurrentRadioModule().getMy() == my) return device;
		}
		return null;
	}
	
	/**
	 * @return the sensor and mobile nodes
	 */
	public static List<Device> getSensorAndMobileNodes() {
		List<Device> nodes = new ArrayList<Device>();
		for(Device node : nodes) {
			if((node.getType() == Device.SENSOR) || node.getType() == Device.DIRECTIONAL_SENSOR || (node.getType() == Device.MOBILE))
				nodes.add((SensorNode) node);
		}
		return nodes;
	}
	
	/**
	 * @return the mobile nodes
	 */
	public static List<Device> getMobileNodes() {
		List<Device> snodes = new ArrayList<Device>();
		for(Device node : devices) {
			if(node.getType() == Device.MOBILE)
				snodes.add(node);
		}
		return snodes;
	}

	/**
	 * @return the mobile sensor nodes
	 */
	public static List<SensorNode> getMobileSensorNodes() {
		List<SensorNode> snodes = new ArrayList<SensorNode>();
		for(SensorNode sensor : sensors) {
			if(!sensor.getGPSFileName().equals(""))
				snodes.add(sensor);
		}
		return snodes;
	}

	/**
	 * @param fileName
	 */
	public static void saveDevicesAndSensors(String fileName) {
		Device device;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			device = iterator.next();				
			device.save(fileName);
		}
		
		SensorNode sensor;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			sensor.save(""+sensor.getId());
		}
	}
	
	/**
	 * @param fileName
	 */
	public static void open() {
		try {
			SensorNode sensor;
			File nodeFolder = new File(Project.getProjectNodePath());
			File [] nodeFiles = nodeFolder.listFiles();
			int deviceType = -1;
			String line;
			int idMax = 0 ;
			for(int i=0; i<nodeFiles.length; i++){
				if(!(nodeFiles[i].getName().split("_")[0].startsWith("."))) {
					BufferedReader br = new BufferedReader(new FileReader(nodeFiles[i]));
					line = br.readLine();
					line = br.readLine();
					line = br.readLine();
					deviceType = Integer.parseInt(line.split(":")[1]);					
					switch (deviceType) {
					case MapObject.SENSOR:
						sensor = loadSensor(nodeFiles[i].getAbsolutePath());
						add(sensor);
						break;
					case MapObject.GAS:						
						add(loadGas(nodeFiles[i].getAbsolutePath()));
						break;
					case MapObject.BASE_STATION:
						sensor = loadBaseStation(nodeFiles[i].getAbsolutePath());												
						add(sensor);
						break;
					case MapObject.DIRECTIONAL_SENSOR:
						sensor = loadDirectionalSensor(nodeFiles[i].getAbsolutePath());
						add(sensor);
						break;
					case MapObject.MOBILE:						
						add(loadMobile(nodeFiles[i].getAbsolutePath()));
						break;
					case MapObject.WEATHER:						
						add(loadWeather(nodeFiles[i].getAbsolutePath()));
						break;
					}
					int v = Integer.valueOf(nodeFiles[i].getName().split("_")[1]);
					if (v>idMax)
						idMax = v ;
					MapLayer.repaint();
					br.close();
				}				
			}
			if(nodeFiles.length != 0)
				DeviceList.number = idMax+1 ;								
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SensorNode loadSensor(String fileName) {
		SensorNode sensor = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_sensor_unit_radius":
					parameters[4] = str[1];
					break;
				case "device_gps_file_name":
					parameters[5] = str[1];
					break;
				case "device_script_file_name":
					parameters[6] = str[1];
					break;
				case "device_type":
					parameters[7] = str[1];
					break;
				case "device_id":
					parameters[8] = str[1];
					break;
				case "device_hide":
					parameters[9] = str[1];
					break;
				case "device_draw_battery":
					parameters[10] = str[1];
					break;				
				}
			}
			sensor = new StdSensorNode(parameters[8], parameters[0], parameters[1], parameters[2], parameters[3], "0", parameters[4], parameters[5], parameters[6]);
			sensor.setHide(Integer.parseInt(parameters[9]));
			sensor.setDrawBatteryLevel(Boolean.parseBoolean(parameters[10]));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		openRadioModule(Project.getProjectRadioPath()+File.separator+"sensor_"+sensor.getId(), sensor);
		return sensor;
	}
	
	public static SensorNode loadDirectionalSensor(String fileName) {
		SensorNode sensor = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			String [] mparameters = {"","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_sensor_unit_radius":
					parameters[4] = str[1];
					break;
				case "device_gps_file_name":
					parameters[5] = str[1];
					break;
				case "device_script_file_name":
					parameters[6] = str[1];
					break;
				case "device_type":
					parameters[7] = str[1];
					break;
				case "device_id":
					parameters[8] = str[1];
					break;
				case "device_hide":
					parameters[9] = str[1];
					break;
				case "device_draw_battery":
					parameters[10] = str[1];
					break;
				case "directional_parameters":
					String [] directional = str[1].split(" ");
					for (int j = 0; j< directional.length; j++)
						mparameters[j] = directional[j];
					break;
				}
			}
			sensor = new DirectionalSensorNode(parameters[8], parameters[0], parameters[1], parameters[2], parameters[3], "0", parameters[4], parameters[5], parameters[6], mparameters[0], mparameters[1], mparameters[2]);
			sensor.setHide(Integer.parseInt(parameters[9]));
			sensor.setDrawBatteryLevel(Boolean.parseBoolean(parameters[10]));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		openRadioModule(Project.getProjectRadioPath()+File.separator+"directionalsensor_"+sensor.getId(), sensor);
		return sensor;
	}
	
	public static SensorNode loadBaseStation(String fileName) {
		SensorNode sensor = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_sensor_unit_radius":
					parameters[4] = str[1];
					break;
				case "device_gps_file_name":
					parameters[5] = str[1];
					break;
				case "device_script_file_name":
					parameters[6] = str[1];
					break;
				case "device_type":
					parameters[7] = str[1];
					break;
				case "device_id":
					parameters[8] = str[1];
					break;
				case "device_hide":
					parameters[9] = str[1];
					break;
				}
			}
			sensor = new BaseStation(parameters[8], parameters[0], parameters[1], parameters[2], parameters[3], "0", parameters[4], parameters[5], parameters[6]);
			sensor.setHide(Integer.parseInt(parameters[9]));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		openRadioModule(Project.getProjectRadioPath()+File.separator+"basestation_"+sensor.getId(), sensor);		
		return sensor;
	}
	
	public static Device loadMobile(String fileName) {
		Device device = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_gps_file_name":
					parameters[4] = str[1];
					break;
				case "device_type":
					parameters[5] = str[1];
					break;
				case "device_id":
					parameters[6] = str[1];
					break;
				case "device_hide":
					parameters[7] = str[1];
					break;
				}							
			}
			device = new Mobile(parameters[0], parameters[1], parameters[2], parameters[3],parameters[4], Integer.parseInt(parameters[6]));
			device.setHide(Integer.parseInt(parameters[7]));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return device;
	}
	
	public static Device loadGas(String fileName) {
		Device device = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_id":
					parameters[4] = str[1];
					break;
				case "device_gps_file_name":
					parameters[5] = str[1];
					break;				
				case "device_hide":
					parameters[6] = str[1];
					break;
				case "natural_event_file_name":
					parameters[7] = str[1];
					break;
				}
			}
			device = new Gas(parameters[0], parameters[1], parameters[2], parameters[3], parameters[5], Integer.parseInt(parameters[4]));
			device.setHide(Integer.parseInt(parameters[6]));
			device.setNatEventFileName(parameters[7]);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return device;
	}
	
	public static Device loadWeather(String fileName) {
		Weather device = null;
		try {
			String[] str = null;
			String line;
			String [] parameters = {"","","","","","","","","","","","","","","","","","","",""};
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_longitude":
					parameters[0] = str[1];
					break;
				case "device_latitude":
					parameters[1] = str[1];
					break;
				case "device_elevation":
					parameters[2] = str[1];
					break;
				case "device_radius":
					parameters[3] = str[1];
					break;
				case "device_id":
					parameters[4] = str[1];
					break;
				case "device_gps_file_name":
					parameters[5] = str[1];
					break;				
				case "device_hide":
					parameters[6] = str[1];
					break;
				case "natural_event_file_name":
					parameters[7] = str[1];
					break;
				}
			}
			device = new Weather(parameters[0], parameters[1], parameters[2], parameters[3], parameters[5], Integer.parseInt(parameters[4]));			
			device.setHide(Integer.parseInt(parameters[6]));
			device.setNatEventFileName(parameters[7]);
			DeviceList.weather = device;			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return device;
	}

	//old version
	public static void open1(String fileName) {
		fileName = Project.projectPath + File.separator + "config/nodes.cfg";

		reset();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str=null;
			int idMax = 0 ;
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				switch (str.length) {
				case 0:
					break;
				case 6:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5]);
					break;
				case 7:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6]);
					break;
				case 8:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]);
					break;
				case 9:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7], str[8]);
					break;
				case 10:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7], str[8], str[8]);
					break;
				case 11:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7], str[8], str[9], str[10]);
					break;
				case 14:
					addNodeByType(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7], str[8], str[9], str[10], str[11], str[12], str[13]);
					break;
				}
				int v = Integer.valueOf(str[1]);
				if (v>idMax)
					idMax = v ;
			}
			if(str!=null)
				DeviceList.number = idMax+1 ;//Integer.valueOf(str[1])+1;
			br.close();
			
			//for(Device device : DeviceList.getNodes()) device.calculateNeighbours();
			
			MapLayer.repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the number of sensors
	 */
	public static int sensorListSize() {
		return sensors.size();
	}
	
	/**
	 * @return the number of selected sensors
	 */
	public static int getNumberOfSelectedSensors() {
		int n = 0;
		for(SensorNode sensor : sensors) {
			if(sensor.isSelected())
				n++;
		}
		return n;
	}
	
	/**
	 * @return the number of directional sensors
	 */
	public static int getNumberOfDirectionalSensors() {
		int n = 0;
		for(SensorNode sensor : sensors) {
			if(sensor.getType() == Device.DIRECTIONAL_SENSOR)
				n++;
		}
		return n;
	}
	
	/**
	 * @return the number of base stations
	 */
	public static int getNumberOfBaseStations() {
		int n = 0;
		for(SensorNode sensor : sensors) {
			if(sensor.getType() == Device.BASE_STATION)
				n++;
		}
		return n;
	}
	
	/**
	 * @return the mobile nodes
	 */
	public static int getNumberOfMobiles() {
		int n = 0;
		for(Device node : devices) {
			if(node.getType() == Device.MOBILE)
				n++;
		}
		return n;
	}
	
	/**
	 * @return the mobile Gas
	 */
	public static int getNumberOfGas() {
		int n = 0;
		for(Device node : devices) {
			if(node.getType() == Device.GAS)
				n++;
		}
		return n;
	}
	
	/**
	 * @return the number of devices
	 */
	public static int deviceListSize() {
		return devices.size();
	}
	
	/**
	 * @return the number of the nodes
	 */
	public static int getSize() {
		return sensors.size()+devices.size();
	}

	/**
	 * Create a node from a set of values (table type)
	 * 
	 * @param type
	 *            table that contains information about a node to add
	 */
	public static void addNodeByType(String... type) {
		int id = Integer.valueOf(type[1]);
		switch (Integer.valueOf(type[0])) {
		case Device.SENSOR:
			if(type.length==10)
				add(new StdSensorNode(type[1], type[2], type[4], type[3], "0.0", type[5], type[6], type[7], type[8]));
			else
				add(new StdSensorNode(type[1], type[2], type[3], type[4], type[5], type[6], type[7], type[8], type[9]));	
			break;
		case Device.GAS:
			add(new Gas(type[3], type[4], type[5], type[6], type[7], id));
			break;
		case Device.BASE_STATION:
			add(new BaseStation(type[1], type[3], type[4], type[5], type[6], type[7], type[8], type[9], type[10]));
			break;
		case Device.DIRECTIONAL_SENSOR:
			add(new DirectionalSensorNode(type[1], type[2], type[3], type[4], type[5], type[6], type[7], type[8], type[9], type[10], type[11], type[12]));
			break;
		case Device.MOBILE:
			add(new Mobile(type[3], type[4], type[5], type[6], type[7], id));
			break;
//		case Device.MARKER:
//			add(new Marker(type[3], type[4], type[5], type[6]));
//			break;
		}
	}

	/**
	 * @param Sensor node
	 */
	public static void add(SensorNode sensor) {
		sensors.add(sensor);
		if(DeviceList.propagationsCalculated)			
			DeviceList.calculatePropagations();
	}
	
	/**
	 * @param device
	 */
	public static void add(Device device) {
		devices.add(device);
	}

	/**
	 * Draw devices
	 * 
	 * @param g
	 *            Graphics
	 */
	public void draw(Graphics g) {
		SensorNode sensor = null;
		for (int i=0; i<sensors.size(); i++) {
			sensor = sensors.get(i);
			if(sensor.isInside() && sensor.isSelected()) {
				MapLayer.numberOfInsideAndSelected++;
			}
			if(sensor.isSelected() || sensor.isInside())
				MapLayer.mapViewer.setPanEnabled(false);
			sensors.get(i).drawRadioRange(g);
		}

		for (int i=0; i<sensors.size(); i++) {
			sensors.get(i).drawSensorUnit(g);
		}

		int k1=0;
		for (int i=0; i<sensors.size(); i++) {
			if(propagationsCalculated)
				sensors.get(i).drawRadioPropagations(g);
			else
				sensors.get(i).drawRadioLinks(k1, g);
			k1++;
		}

		for (int i=0; i<sensors.size(); i++) {
			sensor = sensors.get(i);		
			sensor.drawMarked(g);
			sensor.draw(g);
		}
		
		Device device = null;
		for (int i=0; i<devices.size(); i++) {
			device = devices.get(i);
			if(device.isInside() && device.isSelected()) {
				MapLayer.numberOfInsideAndSelected++;
			}
			if(device.isSelected() || device.isInside())
				MapLayer.mapViewer.setPanEnabled(false);
			device.drawMarked(g);
			device.draw(g);
		}	

		for (int i=0; i<sensors.size(); i++) {
			sensor = sensors.get(i);
			if(sensor.displayInfos()) sensor.drawInfos(g);
		}
		
		for (int i=0; i<devices.size(); i++) {
			device = devices.get(i);
			if(device.displayInfos()) device.drawInfos(g);
		}
		
		for (int i=0; i<sensors.size(); i++) {
			sensors.get(i).drawRadioLinkArrows(g);
		}
		
		MultiChannels.drawChannelLinks(g);
	}

	public Device getDevice(int idx) {
		return devices.get(idx);
	}
	
	public Device getSensor(int idx) {
		return sensors.get(idx);
	}

	public void setDrawLinks(boolean b) {
		drawLinks = b;
	}

	public boolean getDrawLinks() {
		return drawLinks;
	}

	public static void deleteAll() {
		sensors.removeAll(sensors);
		devices.removeAll(devices);
	}
	
	public static void deleteNodeByName(String name) {
		Device node;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			System.out.println(node.getName());
			if (node.getName().equals(name)) {
				iterator.remove();
				node = null;
			}
		}
		SensorNode sensor;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			if (sensor.getName().equals(name)) {
				//MapLayer.mapViewer.removeMouseListener(sensor);
				//MapLayer.mapViewer.removeMouseMotionListener(sensor);
				//MapLayer.mapViewer.removeKeyListener(sensor);
				iterator.remove();
				sensor = null;
			}
		}
	}
	
	public static void deleteAllDevices() {
		Device node;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.getHide()==0) {
				iterator.remove();
				node = null;
			}
		}
	}

	public static void deleteAllSensors() {
		SensorNode sensor;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			if (sensor.getHide()==0) {
				iterator.remove();
				sensor = null;
			}
		}
	}

	
	public static void deleteAllNaturalEvents() {
		Device node;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.getHide()==0 && node.getType()==Device.GAS) {
				//MapLayer.mapViewer.removeMouseListener(node);
				//MapLayer.mapViewer.removeMouseMotionListener(node);
				//MapLayer.mapViewer.removeKeyListener(node);
				iterator.remove();
				node = null;
			}
		}	
	}

	public static void deleteAllBuildings() {
		Building node;
		for (Iterator<Building> iterator = BuildingList.buildings.iterator(); iterator.hasNext();) {
			node = iterator.next();
			MapLayer.mapViewer.removeMouseListener(node);
			MapLayer.mapViewer.removeKeyListener(node);
			iterator.remove();
			node = null;
		}				
	}
	
	public static void deleteAllMobiles() {
		Device node;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.getType() == MapObject.MOBILE) {
				//MapLayer.mapViewer.removeMouseListener(node);
				//MapLayer.mapViewer.removeMouseMotionListener(node);
				//MapLayer.mapViewer.removeKeyListener(node);
				iterator.remove();
				node = null;
			}
		}				
	}
	
	public static void deleteSensor(int idx) {		
		//SensorNode node = sensors.get(idx);
		//MapLayer.mapViewer.removeMouseListener(node);
		//MapLayer.mapViewer.removeMouseMotionListener(node);
		//MapLayer.mapViewer.removeKeyListener(node);
		sensors.remove(idx);
		//node = null;
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
	}
	
	public static void deleteDevice(int idx) {		
		//Device node = devices.get(idx);
		//MapLayer.mapViewer.removeMouseListener(node);
		//MapLayer.mapViewer.removeMouseMotionListener(node);
		//MapLayer.mapViewer.removeKeyListener(node);
		devices.remove(idx);
		//node = null;
	}
	
	public void simulateMobiles() {
		for (Device node : devices) {
			if(node.getType()==Device.MOBILE) {
				node.setSelected(true);
				node.start();
			}
		}
	}

	public static StringBuilder displaySensorGraph() {
		return SensorGraph.toSensorGraph(sensors, sensors.size()).displayNames();
	}

	public static StringBuilder displaySensorTargetGraph() {
		return SensorGraph.toSensorTargetGraph(sensors, sensors.size()).displayNames();
	}

	public void selectInsideRectangle(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
		boolean selection = false;
		for (SensorNode node : sensors) {
			node.setSelected(false);
			if (MapLayer.insideSelection(node.getLongitude(), node.getLatitude(), cadreX1, cadreY1, cadreX2, cadreY2)) {
				node.setSelected(true);
				selection = true;
			}
		}
		for (Device node : devices) {
			node.setSelected(false);
			if (MapLayer.insideSelection(node.getLongitude(), node.getLatitude(), cadreX1, cadreY1, cadreX2, cadreY2)) {
				node.setSelected(true);
				selection = true;
			}
		}
		if(selection)
			if(CupCarbon.cupCarbonController!=null) {
				CupCarbon.cupCarbonController.updateObjectListView();
				CupCarbon.cupCarbonController.getNodeInformations();
				CupCarbon.cupCarbonController.getRadioInformations();
				CupCarbon.cupCarbonController.updateSelectionInListView();
			}
	}

	public void deleteIfSelected() {
		CupActionBlock block = new CupActionBlock();
		SensorNode sensor;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			if (sensor.isSelected()) {
				CupActionDeleteSensor action = new CupActionDeleteSensor(sensor);
				block.addAction(action);				
			}			
		}
		
		Device device;
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			device = iterator.next();
			if (device.isSelected()) {
				CupActionDeleteDevice action = new CupActionDeleteDevice(device);
				block.addAction(action);
			}
		}
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute(); 
		}
		
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
	}

	public static void setGpsFileName(String gpsFileName) {
		Device node;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setGPSFileName(gpsFileName);
			}
		}		
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setGPSFileName(gpsFileName);
			}
		}
		MapLayer.repaint();
	}

	public static void setScriptFileName(String scriptFileName) {
		Device node;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setScriptFileName(scriptFileName);
			}
		}
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setScriptFileName(scriptFileName);
			}
		}
		MapLayer.repaint();
	}

	public static void initMarkedEdges() {
		markedEdges = new Vector<SNEdge>();
	}
	
	public static void initAll() {
		hulls = new LinkedList<LinkedList<Integer>>();
		markedEdges = new Vector<SNEdge>();		
		WisenSimulation.sTime = 0;
		for (SensorNode sensor : sensors) {			
			sensor.init();
			sensor.initGeoZoneList(); 
		}
		for (Device device : devices) {			
			device.init(); 
		}		
	}
	
	public static void initAllGeoZones() {
		for (SensorNode device : sensors) {
			device.initGeoZoneList();
		}		
		MapLayer.repaint();
	}

	public static void initAlgoSelectedNodes() {
		for (SensorNode device : sensors) {
			if (device.isSelected()) {
				device.setMarked(false);
				device.setVisited(false);
				device.setLedColor(0);
			}
		}
		for (Device device : devices) {
			if (device.isSelected()) {
				device.setMarked(false);
				device.setVisited(false);
				device.setLedColor(0);
			}
		}
		MapLayer.repaint();
	}

	public void setSelectionOfAllNodes(boolean selection, int type, boolean addSelect) {
//		for (Marker marker : MarkerList.markers) {
//			if (!addSelect)
//				marker.setSelected(false);
//		}
		for (SensorNode dev : sensors) {
			if (!addSelect)
				dev.setSelected(false);
			if (dev.getType() == type || type == -1)
				dev.setSelected(selection);
		}
		
		for (Device dev : devices) {
			if (!addSelect)
				dev.setSelected(false);
			if (dev.getType() == type || type == -1)
				dev.setSelected(selection);
		}
		
		MapLayer.repaint();
	}
	
	public void setSelectionOfAllMobileNodes(boolean selection, int type, boolean addSelect) {
//		for (Building building : BuildingList.buildings) {
//			if (!addSelect)
//				building.setSelected(false);
//		}
//		for (Marker marker : MarkerList.markers) {
//			if (!addSelect)
//				marker.setSelected(false);
//		}
		for (SensorNode dev : sensors) {
			if (!addSelect)
				dev.setSelected(false);
			if (!dev.getGPSFileName().equals(""))				
				dev.setSelected(selection);
		}
		for (Device dev : devices) {
			if (!addSelect)
				dev.setSelected(false);
			if (!dev.getGPSFileName().equals(""))				
				dev.setSelected(selection);
		}
		MapLayer.repaint();
	}

	public void invertSelection() {
		for (SensorNode sensor : sensors) {
			sensor.invSelection();
		}
		for (Device device : devices) {
			device.invSelection();
		}
		MapLayer.repaint();
	}

	public Point[] getCouple(Device n1, Device n2) {
		int[] coord = MapCalc.geoToPixelMapA(n1.getLongitude(), n1.getLatitude());
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToPixelMapA(n2.getLongitude(), n2.getLatitude());
		int lx2 = coord[0];
		int ly2 = coord[1];
		Point[] p = new Point[2];
		p[0] = new Point(lx1, ly1);
		p[1] = new Point(lx2, ly2);
		return p;
	}
	
	public static void initIDs() {
		Device.initNumber() ;
		for(SensorNode d : sensors) {
			d.setId(number);
			Device.incNumber();
		}
		for(Device d : devices) {
			d.setId(number);
			Device.incNumber();
		}
		MapLayer.repaint();
	}
	//---------
	
	public void loadRoutesFromFiles() {
		for(SensorNode d : sensors) {
			d.loadRouteFromFile();
		}
		for(Device d : devices) {
			d.loadRouteFromFile();
		}
	}

	public void simulate() {
		Device node;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected())
				node.start();
		}
		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected())
				node.start();
		}
	}
	
	public void simulateAll() {	
		for (SensorNode node : sensors) {
			node.setSelected(true);
			node.start();
		}
		for (Device node : devices) {
			node.setSelected(true);
			node.start();
		}
	}
	
	public void simulateSensors() {
		for (SensorNode node : sensors) {
			node.setSelected(true);
			node.start();
		}
	}
	
	public static void stopAgentSimulation() {
		for (SensorNode node : sensors) {
			node.setSelected(false);
			node.stopAgentSimulation();
		}
		for (Device node : devices) {
			node.setSelected(false);
			node.stopAgentSimulation();
		}
	}
	
	//------
	public static void addEdge(SensorNode sn1, SensorNode sn2) {
		markedEdges.add(new SNEdge(sn1, sn2));
	}
	
	public static void edge(SensorNode sn1, SensorNode sn2) {
		addEdge(sn1, sn2);
		MapLayer.repaint();
	}
	
	public static void removeEdge(SensorNode sn1, SensorNode sn2) {
		for(SNEdge edge : markedEdges) {
			if((sn1==edge.getSN1() && sn2==edge.getSN2()) || (sn1==edge.getSN2() && sn2==edge.getSN1())) {
				markedEdges.remove(edge);
				return;
			}
		}
	}
	
	public static void markEdge(SensorNode sn1, SensorNode sn2) {
		markedEdges.add(new SNEdge(sn1, sn2));
		MapLayer.repaint();
	}
	
	public static void unmarkEdge(SensorNode sn1, SensorNode sn2) {
		for(SNEdge edge : markedEdges) {
			if((sn1==edge.getSN1() && sn2==edge.getSN2()) || (sn1==edge.getSN2() && sn2==edge.getSN1())) {
				markedEdges.remove(edge);
				MapLayer.repaint();
				return;
			}
		}
	}
	
	public static void noEdge(SensorNode sn1, SensorNode sn2) {
		removeEdge(sn1, sn2);
		MapLayer.repaint();
	}
	
	public void drawMarkedEdges(Graphics2D g) {			
		if(markedEdges.size()>0) {
			for(int i=0; i<markedEdges.size(); i++) {
				markedEdges.get(i).draw(g);		
			}
		}
	}
	
	public static int getLastHullSize() {
		return hulls.getLast().size();
	}
	
	public static void initLastHull() {
		hulls.getLast().clear();
	}
	
	public static void addHull() {
		hulls.add(new LinkedList<Integer>());
	}
	
	public static void addToLastHull(Integer d) {
		hulls.getLast().add(d);
	}
	
	public static LinkedList<Integer> getLastHull() {
		return hulls.getLast();
	}
	
	public void drawHull(LinkedList<Integer> hull, Graphics2D g) {
		g.setStroke(new BasicStroke(2.0f));
		if(hull.size()>0) {
			double x = sensors.get(hull.get(0)).getLatitude();
			double y = sensors.get(hull.get(0)).getLongitude();
			int lx1=0;
			int ly1=0;
			int lx2=0;
			int ly2=0;
			int[] coord ;
			for(int i=1; i<hull.size(); i++) {
				coord = MapCalc.geoToPixelMapA(x, y);
				lx1 = coord[0];
				ly1 = coord[1];
				coord = MapCalc.geoToPixelMapA(sensors.get(hull.get(i)).getLatitude(), sensors.get(hull.get(i)).getLongitude());
				lx2 = coord[0];
				ly2 = coord[1];
				g.setColor(Color.BLUE);
				g.drawLine(lx1, ly1, lx2, ly2);
				x = sensors.get(hull.get(i)).getLatitude();
				y = sensors.get(hull.get(i)).getLongitude();		
			}
		}
	}
	
	public void drawHulls(Graphics2D g) {
		for(LinkedList<Integer> hull : hulls) {
			drawHull(hull, g);
		}
	}
	
	public static int getNumberOfSensorsWithoutScript() {
		int n = 0;
		for(SensorNode d : sensors) {
			if(d.getScriptFileName().equals("")) {
				n++;
			}
		}
		return n;
	}
	
	public static int getNumberOfSensorsWithScript() {
		int n = 0;
		for(SensorNode d : sensors) {
			if(!d.getScriptFileName().equals("")) {
				n++;
			}
		}
		return n;
	}
	
	public static void selectWitoutScript() {
		for(SensorNode d : sensors) {
			if(d.getScriptFileName().equals("")) {
				d.setSelected(true);
			}
		}
		MapLayer.repaint();
	}
	
	public static void selectWitoutGps() {
		for(SensorNode d : sensors) {
			if(d.getGPSFileName().equals("")) {
				d.setSelected(true);
			}
		}
		MapLayer.repaint();
	}
	
	public static void selectMarkedSensors() {
		for(SensorNode d : sensors) {
			if(d.isMarked()) {
				d.setSelected(true);
			}
		}
		MapLayer.repaint();
	}
	
	public static void selectDeadSensors() {
		for(SensorNode d : sensors) {
			if(d.isDead()) {
				d.setSelected(true);
			}
		}
		MapLayer.repaint();
	}
	
	public static int getNumberOfMarkedSensors() {
		int n = 0;
		for(SensorNode d : sensors) {
			if(d.isMarked()) {
				n++;
			}
		}
		return n;
	}
	
	public static int getNumberOfUnmarkedSensors() {
		int n = 0;
		for(SensorNode d : sensors) {
			if(!d.isMarked()) {
				n++;
			}
		}
		return n;
	}
	
	public static void setId(String id) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) {
				d.setId(Integer.valueOf(id));
			}
		}
		for (Device d : devices) {
			if (d.isSelected()) {
				d.setId(Integer.valueOf(id));
			}
		}
		MapLayer.repaint();
	}
		
	public static void setLongitude(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) {
				d.setLongitude(Double.valueOf(value));
			}
		}
		MapLayer.repaint();
	}
	
	public static void setLatitude(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) {
				d.setLatitude(Double.valueOf(value));
			}
		}
		MapLayer.repaint();
	}
	
	public static void setElevation(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) {
				d.setElevation(Double.valueOf(value));
			}
		}
		MapLayer.repaint();
	}
	
	public static void setRadius(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) {
				d.setRadius(Double.valueOf(value));
			}
		}
		for (Device d : devices) {
			if (d.isSelected()) {
				d.setRadius(Double.valueOf(value));
			}
		}
		MapLayer.repaint();
	}
	
	public static void setSensorUnitRadius(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) 
				d.setSensorUnitRadius(Double.valueOf(value));
		}
		MapLayer.repaint();
	}
	
	public static void setEMax(String value) {
		for (SensorNode d : sensors) {
			if (d.isSelected()) 
				d.getBattery().setEMax(Double.valueOf(value));
		}
		MapLayer.repaint();
	}	

	public static void selectById(String id) {
		String [] ids = id.split(" ");
		int k=0;
		for (SensorNode d : sensors) {
			d.setSelected(false);
			if(k<ids.length)
				if (d.getId()==Integer.valueOf(ids[k])) {
					d.setSelected(true);
					k++;
				}
		}
		k=0;
		for (Device d : devices) {
			d.setSelected(false);
			if(k<ids.length)
				if (d.getId()==Integer.valueOf(ids[k])) {
					d.setSelected(true);
					k++;
				}
		}
		MapLayer.repaint();
	}
	
	public static void selectByMy(String my) {
		String [] mys = my.split(" ");
		for (SensorNode d : sensors) {
			d.setSelected(false);
			for(int k=0; k<mys.length; k++) {
				if (d.getCurrentRadioModule().getMy()==Integer.valueOf(mys[k])) {
					d.setSelected(true);
				}
			}
		}
		MapLayer.repaint();
	}
	
	public static void selectByLed(String led) {
		String [] leds = led.split(" ");
		for (SensorNode d : sensors) {
			d.setSelected(false);
			for(int k=0; k<leds.length; k++) {
				if(d.getLedColor()==Integer.valueOf(leds[k]))
					d.setSelected(true);
			}
		}
		MapLayer.repaint();
	}
	
	public static void selectOneFromSelected() {		
		for(SensorNode d : sensors) {
			if(d.isSelected()) {
				deselectAll();
				d.setSelected(true);
				return;
			}
		}
		for(Device d : devices) {
			if(d.isSelected()) {
				deselectAll();
				d.setSelected(true);
				return;
			}
		}
	}
	
	public static void deselectAll() {
//		for(Marker marker : MarkerList.markers) {
//			marker.setSelected(false);
//		}
//		for(Building building : BuildingList.buildings) {
//			building.setSelected(false);
//		}
		for(SensorNode d : sensors) {
			d.setSelected(false);
		}
		for(Device d : devices) {
			d.setSelected(false);
		}
	}
	
	public static void deselectAllObjects() {
		for(SensorNode d : sensors) {
				d.setSelected(false);
		}
		for(Device d : devices) {
			if(d.getType() != Device.GAS)
				d.setSelected(false);
		}
	}
	
	public static void deselectAllEvents() {
		for(Device d : devices) {
			if(d.getType() == Device.GAS)
				d.setSelected(false);
		}
	}
	
	public static void calculatePropagations() {
		propagationsCalculated = true;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					for(SensorNode device : sensors) {
						device.calculatePropagations();
						MapLayer.repaint();
					}
				}
				catch(Exception e) {}
			}
		});
	}
	
	public static void calculatePropagations_vt() {
		propagationsCalculated = true;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					for(SensorNode device : sensors) {
						device.calculatePropagations();
						MapLayer.repaint();
					}					
				}
				catch(Exception e) {}
			}
		});
	}
	
//	public static void calculatePropagations(SensorNode sensor) {
//		propagationsCalculated = true;
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					sensor.calculatePropagations();
//				}
//				catch(Exception e) {}
//			}
//		});
//	}
	
	public static void resetPropagations() {
		propagationsCalculated = false;
		for(SensorNode device : sensors) {
			device.resetPropagations();
		}
	}

	public static void setUartDataRate(String selectedItem) {
		for (SensorNode device : sensors) {
			if (device.isSelected()) {
				if (selectedItem.equals("-"))
					device.setUartDataRate(Long.MAX_VALUE);
				else
					device.setUartDataRate(Long.parseLong(selectedItem));
			}
		}
		MapLayer.repaint();
	}

	public static void setSigmaOfDriftTime(double drift) {
		for (SensorNode device : sensors) {
			if (device.isSelected()) {
				device.setSigmaOfDriftTime(drift);				
			}
		}		
	}
	
	public static void openRadioModule(String fileName, SensorNode sensor) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String name = "";
			String str = null;
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(":")[0];				
				if (str.equals("current_radio_name")){
					name = line.split(":")[1];
					line = br.readLine();
					str = line.split(":")[0];
					if (str.equals("radio_standard")){
						String std = line.split(":")[1];
						sensor.addRadioModule(name, std);
						sensor.selectCurrentRadioModule(name);
					}
				}
				if (str.equals("radio_name")){
					name = line.split(":")[1];
					line = br.readLine();
					str = line.split(":")[0];
					if (str.equals("radio_standard")){
						String std = line.split(":")[1];
						sensor.addRadioModule(name, std);
					}
				}				
				else
				switch (str) {
				case "radio_my":
					sensor.getRadioModuleByName(name).setMy(Integer.parseInt(line.split(":")[1]));
					break;
				case "radio_channel":
					sensor.getRadioModuleByName(name).setCh(Integer.parseInt(line.split(":")[1]));
					break;
				case "radio_network_id":
					sensor.getRadioModuleByName(name).setNId(Integer.parseInt(line.split(":")[1]));
					break;
				case "radio_radius":
					sensor.getRadioModuleByName(name).setRadioRangeRadius(Double.parseDouble(line.split(":")[1]));
					break;
				case "radio_etx":
					sensor.getRadioModuleByName(name).setETx(Double.parseDouble(line.split(":")[1]));
					break;
				case "radio_erx":
					sensor.getRadioModuleByName(name).setERx(Double.parseDouble(line.split(":")[1]));
					break;
				case "radio_esleep":
					sensor.getRadioModuleByName(name).setESleep(Double.parseDouble(line.split(":")[1]));
					break;
				case "radio_elisten":
					sensor.getRadioModuleByName(name).setEListen(Double.parseDouble(line.split(":")[1]));
					break;
				case "radio_data_rate":
					sensor.getRadioModuleByName(name).setRadioDataRate(Integer.parseInt(line.split(":")[1]));
					break;
				case "spreading_factor":
					sensor.getRadioModuleByName(name).setSpreadingFactor(Integer.parseInt(line.split(":")[1]));
					break;
				case "code_rate":
					sensor.getRadioModuleByName(name).setCodeRate(Integer.parseInt(line.split(":")[1]));
					break;
				case "conso_tx_model":
					sensor.getRadioModuleByName(name).setRadioConsoTxModel(line.split(":")[1]);
					break;
				case "conso_rx_model":
					sensor.getRadioModuleByName(name).setRadioConsoRxModel(line.split(":")[1]);
					break;
				}
			}
			br.close();
			MapLayer.repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getNumberOfSelectedObjects() {
		int n = 0;
		for(SensorNode d : sensors) {
			if (d.isSelected()) n++;
		}
		for(Device d : devices) {
			if (d.isSelected()) n++;
		}
		return n;
	}
	
	public static int getNumberOfInside() {
		int n = 0;
		for(SensorNode d : sensors) {
			if (d.isInside()) n++;
		}
		for(Device d : devices) {
			if (d.isInside()) n++;
		}
		return n;
	}
	
	public static void delete(SensorNode sensor) {
		sensors.remove(sensor);
		if(DeviceList.propagationsCalculated)			
			DeviceList.calculatePropagations();
	}
	
	public static void delete(Device device) {
		if(device.getClass().equals(Weather.class)) weather = null;
		devices.remove(device);
		if(DeviceList.propagationsCalculated)			
			DeviceList.calculatePropagations();
	}
	
	public static void addRandomSensors(int n, int hide) {		
		CupActionBlock block = new CupActionBlock();		
		if(MarkerList.markers.size()==2) {
			Marker marker1 = MarkerList.markers.get(0);
			Marker marker2 = MarkerList.markers.get(1);
			double m1x = marker1.getLongitude();
			double m1y = marker1.getLatitude();
			double m2x = marker2.getLongitude();
			double m2y = marker2.getLatitude();
	
			double r1 ;
			double r2 ;
			double x ;
			double y ;
			int i=0;
			while(i < n) {
				r1 = Math.random();
				r2 = Math.random();
				x = ((m2x-m1x)*r1)+m1x;
				y = ((m2y-m1y)*r2)+m1y;
				
				double magnetic_step = 0.000227984;
				double delta = 0.0;
				if (MapLayer.magnetic) {
					x = x - (x % magnetic_step) - (delta % magnetic_step);
					y = y - (y % magnetic_step) - (delta % magnetic_step);			
				}
				boolean ex = false;
				for(Building b : BuildingList.buildings) {
					if((b.inside(y, x)) && (!b.isHide())) {
						ex = true;
						break;
					}
				}
				if(!ex) {
					i++;
					StdSensorNode sensor = new StdSensorNode(x, y, 0, 0, 100, 20, -1);
					sensor.setHide(hide);
					CupAction action = new CupActionAddSensor(sensor);
					block.addAction(action);
				}
			}
			if(block.size()>0) {
				CupActionStack.add(block);
				CupActionStack.execute();
				if(DeviceList.propagationsCalculated && NetworkParameters.drawRadioLinks)
					DeviceList.calculatePropagations();
			}		
			MapLayer.repaint();
		}
		
	}
	
	public static void addRandomSensors2(int n, int hide) {		
		CupActionBlock block = new CupActionBlock();		
		if(MarkerList.markers.size()==2) {
			Marker marker1 = MarkerList.markers.get(0);
			Marker marker2 = MarkerList.markers.get(1);
			double m1x = marker1.getLongitude();
			double m1y = marker1.getLatitude();
			double m2x = marker2.getLongitude();
			double m2y = marker2.getLatitude();
	
			double r1 ;
			double r2 ;
			double x ;
			double y ;
			
			for (int i = 0; i < n; i++) {
				r1 = Math.random();
				r2 = Math.random();
				x = ((m2x-m1x)*r1)+m1x;
				y = ((m2y-m1y)*r2)+m1y;
				
				double magnetic_step = 0.000227984;
				double delta = 0.0;
				if (MapLayer.magnetic) {
					x = x - (x % magnetic_step) - (delta % magnetic_step);
					y = y - (y % magnetic_step) - (delta % magnetic_step);			
				}
				StdSensorNode sensor = new StdSensorNode(x, y, 0, 0, 100, 20, -1);
				sensor.setHide(hide);
				CupAction action = new CupActionAddSensor(sensor);
				block.addAction(action);				
			}
			if(block.size()>0) {
				CupActionStack.add(block);
				CupActionStack.execute();
				if(DeviceList.propagationsCalculated && NetworkParameters.drawRadioLinks)
					DeviceList.calculatePropagations();
			}		
			MapLayer.repaint();
		}
		
	}
		
}
