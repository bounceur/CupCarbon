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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import actions_ui.DeleteDevice;
import flying_object.FlyingGroup;
import map.MapLayer;
import markers.Marker;
import natural_events.Gas;
import solver.SensorGraph;
import utilities.MapCalc;
import visualisation.Visualisation;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 2.0
 */
public class DeviceList {

	public static List<Device> nodes = new ArrayList<Device>();
	public static boolean drawLinks = true;
	public static LinkedList<LinkedList<Integer>> envelopeList = new LinkedList<LinkedList<Integer>>();
	public static int number = 1;
	public static boolean propagationsCalculated = false; 
	
	/**
	 * 
	 */
	public DeviceList() {
		reset();
		// Thread th = new Thread(this);
		// th.start();
	}
	
	public static void reset() {
		for(Device node : nodes) {
			MapLayer.getMapViewer().removeMouseListener(node);
			MapLayer.getMapViewer().removeMouseMotionListener(node);
			MapLayer.getMapViewer().removeKeyListener(node);
			node = null;
		}
		nodes = new ArrayList<Device>();
		drawLinks = true;
		//displayConnectionDistance = false;
		envelopeList = new LinkedList<LinkedList<Integer>>();
	}

	/**
	 * @return the nodes
	 */
	public static List<Device> getNodes() {
		return nodes;
	}
	
	/**
	 * @return a node by its id
	 */
	public static Device getNodeById(int id) {
		for(Device device : nodes) {
			if(device.getId() == id) return device;
		}
		return null;
	}
	
	/**
	 * @return a sensor node by its id
	 */
	public static SensorNode getSensorNodeById(int id) {		
		for(SensorNode snode : DeviceList.getSensorNodes()) {
			if(snode.getId() == id) return snode;
		}
		return null;
	}
	
	/**
	 * @return a sensor node by its my
	 */
	public static SensorNode getSensorNodeByMy(int my) {		
		for(SensorNode snode : DeviceList.getSensorNodes()) {
			if(snode.getMy() == my) return snode;
		}
		return null;
	}
	
	/**
	 * @return the sensor nodes
	 */
	public static List<SensorNode> getSensorNodes() {
		List<SensorNode> snodes = new LinkedList<SensorNode>();
		for(Device device : nodes) {
			if(device.getType() == Device.SENSOR || device.getType() == Device.MEDIA_SENSOR || device.getType()==Device.BASE_STATION)
				snodes.add((SensorNode) device);
		}
		return snodes;
	}
	
	/**
	 * @return the sensor and mobile nodes
	 */
	public static List<Device> getSensorAndMobileNodes() {
		List<Device> nodes = new ArrayList<Device>();
		for(Device node : nodes) {
			if((node.getType() == Device.SENSOR) || node.getType() == Device.MEDIA_SENSOR || (node.getType() == Device.MOBILE))
				nodes.add((SensorNode) node);
		}
		return nodes;
	}
	
	/**
	 * @return the mobile nodes
	 */
	public static List<Device> getMobileNodes() {
		List<Device> snodes = new ArrayList<Device>();
		for(Device node : nodes) {
			if(node.getType() == Device.MOBILE)
				snodes.add(node);
		}
		return snodes;
	}

	/**
	 * @param fileName
	 */
	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			Device node;
			for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
				node = iterator.next();
				//System.out.println(node.getGPSFileName());
				fos.print(node.getType());
				fos.print(" " + node.getId());
				fos.print(" " + node.getMy()+"#"+node.getCh()+"#"+node.getNId());
				fos.print(" " + node.getLongitude());
				fos.print(" " + node.getLatitude());
				fos.print(" " + node.getElevation());
				fos.print(" " + node.getRadius());

				if (node.getType() == Device.SENSOR || node.getType() == Device.BASE_STATION || node.getType() == Device.MEDIA_SENSOR || node.getType() == Device.MOBILE_WR)
					fos.print(" " + node.getRadioRadius());

				if (node.getType() == Device.SENSOR || node.getType() == Device.BASE_STATION || node.getType() == Device.MEDIA_SENSOR)
					fos.print(" " + node.getSensorUnitRadius());

				if (node.getType() == Device.FLYING_OBJECT)
					fos.print(" " + ((FlyingGroup) node).getflyingObjectNumber());

				if (node.getType() == Device.SENSOR || node.getType() == Device.BASE_STATION 
						|| node.getType() == Device.FLYING_OBJECT
						|| node.getType() == Device.MOBILE
						|| node.getType() == Device.MOBILE_WR
						|| node.getType() == Device.MEDIA_SENSOR) {
					//System.out.println("----> " + node.getGPSFileName());
					fos.print(" "
							+ ((node.getGPSFileName() == "") ? "#" : node
									.getGPSFileName()));
				}

				if (node.getType() == Device.SENSOR || node.getType() == Device.BASE_STATION || node.getType() == Device.MEDIA_SENSOR) {
					fos.print(" "+ ((node.getScriptFileName() == "") ? "#" : node.getScriptFileName()));
				}
				
				if (node.getType() == Device.MEDIA_SENSOR) {
					fos.print(" " + node.getSensorUnitDeg()+ " " + node.getSensorUnitDec()+ " " + node.getSensorUnitN());
				}

				fos.println();

			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param fileName
	 */
	public static void open(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str=null;
			int idMax = 0 ;
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				
				switch (str.length) {
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
			
			MapLayer.getMapViewer().repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the number of the nodes
	 */
	public static int size() {
		return nodes.size();
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
			add(new StdSensorNode(type[1], type[2], type[3], type[4], type[5], type[6], type[7], type[8], type[9], type[10]));
			break;
		case Device.GAS:
			add(new Gas(type[3], type[4], type[5], type[6], id));
			break;
		case Device.FLYING_OBJECT:
			add(new FlyingGroup(type[3], type[4], type[5], type[6], type[7], type[8]));
			break;
		case Device.BASE_STATION:
			add(new BaseStation(type[1], type[2], type[3], type[4], type[5], type[6], type[7], type[8], type[9], type[10]));
			break;
		case Device.MEDIA_SENSOR:
			add(new MediaSensorNode(type[1], type[2], type[3], type[4], type[5], type[6], type[7], type[8], type[9], type[10], type[11], type[12], type[13]));
			break;
		case Device.MOBILE:
			add(new Mobile(type[3], type[4], type[5], type[6], type[7], id));
			break;
		case Device.MARKER:
			add(new Marker(type[3], type[4], type[5], type[6]));
			break;
		}
	}

	/**
	 * @param node
	 */
	public static void add(Device node) {
		nodes.add(node);
	}

	// public void drawDistance(int x, int y, int x2, int y2, int d, Graphics g)
	// {
	// g.setColor(UColor.WHITED_TRANSPARENT);
	// g.drawString(""+d,(x2-x)/2,(y2-y)/2);
	// }

	
	/**
	 * Draw devices
	 * 
	 * @param g
	 *            Graphics
	 */
	public void draw(Graphics g) {
		for (Device n : nodes) {			
			n.drawRadioRange(g);
		}
		
		for (Device n : nodes) {
			n.drawSensorUnit(g);
		}
		
		for (Device n : nodes) {			
			n.drawMarked(g);
			n.draw(g);
		}
		
		for (Device n : getSensorNodes()) {
			//Visualisation.updateStdSensorNode((SensorNode) n);
			if(propagationsCalculated)
				n.drawRadioPropagations(g);
			else
				n.drawRadioLinks(g);
		}

		Channels.drawChannelLinks(g);
		
		for (Device n : nodes) {
			if(n.displayInfos()) n.drawInfos(g);
		}
	}

	public Device get(int idx) {
		return nodes.get(idx);
	}

	public void setDrawLinks(boolean b) {
		drawLinks = b;
	}

	public boolean getDrawLinks() {
		return drawLinks;
	}

//	public void setDisplayDistance(boolean b) {
//		displayConnectionDistance = b;
//	}
//
//	public boolean getDisplayDistance() {
//		return displayConnectionDistance;
//	}

	public static void delete(int idx) {
		Device node = nodes.get(idx);
		MapLayer.getMapViewer().removeMouseListener(node);
		MapLayer.getMapViewer().removeMouseMotionListener(node);
		MapLayer.getMapViewer().removeKeyListener(node);
		nodes.remove(idx);
		Visualisation.removeDevice(node);
		node = null;
	}
	
	public void simulateMobiles() {
		for (Device node : nodes) {
			if(node.getType()==Device.MOBILE || node.getType()==Device.MOBILE_WR) {
				node.setSelection(true);
				node.start();
			}
		}
	}

	public static StringBuilder displaySensorGraph() {
		return SensorGraph.toSensorGraph(nodes, nodes.size()).displayNames();
	}

	public static StringBuilder displaySensorTargetGraph() {
		return SensorGraph.toSensorTargetGraph(nodes, nodes.size()).displayNames();
	}

	public void selectInNodeSelection(int cadreX1, int cadreY1, int cadreX2,
			int cadreY2) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			node.setMove(false);
			node.setSelection(false);
			if (MapLayer.inMultipleSelection(node.getLongitude(), node.getLatitude(), cadreX1,
					cadreX2, cadreY1, cadreY2)) {
				node.setSelection(true);
			}
		}
	}

	public void deleteIfSelected() {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected() && node.getHide()==0) {
				MapLayer.getMapViewer().removeMouseListener(node);
				MapLayer.getMapViewer().removeMouseMotionListener(node);
				MapLayer.getMapViewer().removeKeyListener(node);
				iterator.remove();
				/* Tanguy */
				DeleteDevice action = new DeleteDevice(node, "Device deleted");
				action.exec();
				/* ------ */
				node = null;
			}
		}
	}

	public static void setGpsFileName(String gpsFileName) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setGPSFileName(gpsFileName);
			}
		}
	}

	public static void setScriptFileName(String scriptFileName) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setScriptFileName(scriptFileName);
			}
		}
	}

	public static void updateDeviceParaFromMap(String xS, String yS, String radiusS,
			String captureRadiusS, String scriptFileName, String gpsFileName,
			String eMax, String eS, String uart_dr) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setLongitude(Double.valueOf(xS));
				node.setLatitude(Double.valueOf(yS));
				node.setRadius(Double.valueOf(radiusS));
				node.setSensorUnitRadius(Double.valueOf(captureRadiusS));
				node.setScriptFileName(scriptFileName);
				node.setGPSFileName(gpsFileName);
				node.getBattery().setLevel(Integer.valueOf(eMax));
				node.setES(Double.valueOf(eS));				
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void updateRadioParaFromMap(String radioRadiusS, String eTx, String eRx, String beta, String eSlp, String eL) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setRadioRadius(Double.valueOf(radioRadiusS));
				node.setETx(Double.valueOf(eTx));
				node.setERx(Double.valueOf(eRx));
				node.setESlp(Double.valueOf(eSlp));
				node.setES(Double.valueOf(eL));			
			}
		}
		MapLayer.getMapViewer().repaint();
	}

	public static void initAll() {
		envelopeList = new LinkedList<LinkedList<Integer>>();
		for (Device device : nodes) {
			device.init();			
		}
		MapLayer.getMapViewer().repaint();
	}

	public static void initAlgoSelectedNodes() {
		for (Device device : nodes) {
			if (device.isSelected()) {
				device.setMarked(false);
				device.setVisited(false);
				device.setLedColor(0);
			}
		}
		MapLayer.getMapViewer().repaint();
	}

	public static void setAlgoSelect(boolean b) {
		for (Device node : nodes) {
			node.setMarked(false);
		}
		MapLayer.getMapViewer().repaint();
	}

	public void setSelectionOfAllNodes(boolean selection, int type,
			boolean addSelect) {
		for (Device dev : nodes) {
			if (!addSelect)
				dev.setSelection(false);
			if (dev.getType() == type || type == -1)
				dev.setSelection(selection);
		}
		MapLayer.getMapViewer().repaint();
	}

	public void invertSelection() {
		for (Device dev : nodes) {
			dev.invSelection();
		}
		MapLayer.getMapViewer().repaint();
	}

	public Point[] getCouple(Device n1, Device n2) {
		int[] coord = MapCalc.geoToIntPixelMapXY(n1.getLongitude(), n1.getLatitude());
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToIntPixelMapXY(n2.getLongitude(), n2.getLatitude());
		int lx2 = coord[0];
		int ly2 = coord[1];
		Point[] p = new Point[2];
		p[0] = new Point(lx1, ly1);
		p[1] = new Point(lx2, ly2);
		return p;
	}
	
	
	// Note: This method is not correct for a project because it changes 
	// the id of each sensor. It is used to validate simulation in simbox_simulation
	// package
	public void initId() {
		int k = 0;
		Device.initNumber() ;
		for(Device d : nodes) {
			d.setId(k++);
			Device.incNumber();
		}
		MapLayer.getMapViewer().repaint();
	}
	//---------
	
	public void loadRoutesFromFiles() {
		for(Device d : nodes) {
			d.loadRouteFromFile();
		}
	}

	public void simulate() {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected())
				node.start();
		}
	}
	
	public void simulateAll() {	
		for (Device node : nodes) {
			node.setSelection(true);
			node.start();
		}
	}
	
	public void simulateSensors() {
		for (Device node : nodes) {
			if(node.getType()==Device.SENSOR) {
				node.setSelection(true);
				node.start();
			}
		}
	}
	
	public static void stopSimulation() {
		for (Device node : nodes) {
			node.setSelection(false);
			node.stopSimulation();
		}
	}
	
	//------
	public static int getLastEnvelopeSize() {
		return envelopeList.getLast().size();
	}
	
	public static void initLastEnvelope() {
		envelopeList.getLast().clear();
	}
	
	public static void addEnvelope() {
		envelopeList.add(new LinkedList<Integer>());
	}
	
	public static void addToLastEnvelope(Integer d) {
		envelopeList.getLast().add(d);
	}
	
	public static LinkedList<Integer> getLastEnvelope() {
		return envelopeList.getLast();
	}
	
	public void drawEnvelope(LinkedList<Integer> envelope, Graphics2D g) {
		if(envelope.size()>0) {
			double x = nodes.get(envelope.get(0)).getLongitude();
			double y = nodes.get(envelope.get(0)).getLatitude();
			int lx1=0;
			int ly1=0;
			int lx2=0;
			int ly2=0;
			int[] coord ;
			for(int i=1; i<envelope.size(); i++) {
				coord = MapCalc.geoToIntPixelMapXY(x, y);
				lx1 = coord[0];
				ly1 = coord[1];
				coord = MapCalc.geoToIntPixelMapXY(nodes.get(envelope.get(i)).getLongitude(), nodes.get(envelope.get(i)).getLatitude());
				lx2 = coord[0];
				ly2 = coord[1];
				g.setColor(Color.BLUE);
				g.drawLine(lx1, ly1, lx2, ly2);
				x = nodes.get(envelope.get(i)).getLongitude();
				y = nodes.get(envelope.get(i)).getLatitude();		
			}
			coord = MapCalc.geoToIntPixelMapXY(nodes.get(envelope.get(0)).getLongitude(), nodes.get(envelope.get(0)).getLatitude());
			lx1 = coord[0];
			ly1 = coord[1];
			g.drawLine(lx2, ly2, lx1, ly1);
		}
	}
	
	public void drawEnvelopeList(Graphics2D g) {
		for(LinkedList<Integer> envelope : envelopeList) {
			drawEnvelope(envelope, g);
		}
	}
	
	public static void selectWitoutScript() {
		for(Device d : nodes) {
			if((d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION) && (d.getScriptFileName().equals(""))) {
				d.setSelection(true);
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void selectWitoutGps() {
		for(Device d : nodes) {
			if((d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION) && (d.getGPSFileName().equals(""))) {
				d.setSelection(true);
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void selectMarkedSensors() {
		for(Device d : nodes) {
			if((d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION) && (d.isMarked())) {
				d.setSelection(true);
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setMy(String my) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setMy(Integer.valueOf(my));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setId(String id) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setId(Integer.valueOf(id));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setCh(String ch) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setCh(Integer.valueOf(ch));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setNId(String NId) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setNId(Integer.valueOf(NId));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setLongitude(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setLongitude(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setLatitude(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setLatitude(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setElevation(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setElevation(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setRadius(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setRadius(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setRadioRadius(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setRadioRadius(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setSensorUnitRadius(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setSensorUnitRadius(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setEMax(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.getBattery().init(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setTx(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setETx(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setRx(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setERx(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setSensingEnergy(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setERx(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
//	public static void setBeta(String value) {
//		for (Device d : nodes) {
//			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
//				d.setBeta(Double.valueOf(value));
//			}
//		}
//		Layer.getMapViewer().repaint();
//	}
	
	public static void setESlp(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setESlp(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void setEL(String value) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setEL(Double.valueOf(value));
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	

	public static void selectById(String id) {
		String [] ids = id.split(" ");
		int k=0;
		for (Device d : nodes) {
			d.setSelection(false);
			if(k<ids.length)
				if (d.getId()==Integer.valueOf(ids[k])) {
					d.setSelection(true);
					k++;
				}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void selectByMy(String my) {
		String [] mys = my.split(" ");
		for (Device d : nodes) {
			d.setSelection(false);
		}
		for(int k=0; k<mys.length; k++) {
			for (Device d : nodes) {
				if (d.getMy()==Integer.valueOf(mys[k])) {
					d.setSelection(true);
				}			
			}
		}
		MapLayer.getMapViewer().repaint();
	}
	
	public static void selectOneFromSelected() {
		for(Device d : nodes) {
			if(d.isSelected()) {
				deselectAll();
				d.setSelection(true);
			}
		}
	}
	
	public static void deselectAll() {
		for(Device d : nodes) {
			d.setSelection(false);
		}
	}
	
	public static void calculatePropagations() {
		propagationsCalculated = true;
		for(Device device : nodes) {
			device.calculatePropagations();
		}
	}
	
	public static void resetPropagations() {
		propagationsCalculated = false;
		for(Device device : nodes) {
			device.resetPropagations();
		}
	}

	public static void setUartDataRate(String selectedItem) {
		for (Device device : nodes) {
			if (device.isSelected()) {
				if (selectedItem.equals("-"))
					device.setUartDataRate(Integer.MAX_VALUE);
				else
					device.setUartDataRate(Integer.parseInt(selectedItem));
			}
		}
		MapLayer.getMapViewer().repaint();
	}

	public static void setRadioDataRate(String text) {
		for (Device device : nodes) {
			if (device.isSelected()) {
				device.setRadioDataRate(Integer.parseInt(text));
			}
		}
		MapLayer.getMapViewer().repaint();		
		
	}

	public static void setStd(String str) {
		for (Device d : nodes) {
			if (d.isSelected() && (d.getType()==Device.SENSOR || d.getType()==Device.BASE_STATION)) {
				d.setStd(str);
				
			}
		}
		MapLayer.getMapViewer().repaint();		
	}
	
}
