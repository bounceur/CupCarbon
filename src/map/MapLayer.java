/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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

package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

import action.CupAction;
import action.CupActionAddBuilding;
import action.CupActionAddDevice;
import action.CupActionAddMarker;
import action.CupActionAddSensor;
import action.CupActionBlock;
import action.CupActionDeleteBuilding;
import action.CupActionDeleteDevice;
import action.CupActionDeleteMarker;
import action.CupActionDeleteSensor;
import action.CupActionMapObjectMove;
import action.CupActionModifDeviceRadius;
import action.CupActionModifDirectionalSensorUnitDirection;
import action.CupActionModifDirectionalSensorUnitCoverage;
import action.CupActionModifRadioRadius;
import action.CupActionModifSensorRadius;
import action.CupActionModifSensorUnitRadius;
import action.CupActionStack;
import buildings.Building;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import device.BaseStation;
import device.MessageEventList;
import device.Device;
import device.DeviceList;
import device.MapObject;
import device.DirectionalSensorNode;
import device.Mobile;
import device.NetworkLoader;
import device.SensorNode;
import device.StdSensorNode;
import geo_objects.GeoZoneList;
import markers.Marker;
import markers.MarkerList;
import markers.Routes;
import natural_events.Gas;
import natural_events.Weather;
import simulation.SimulationInputs;
import simulation.WisenSimulation;
import utilities.MapCalc;
import utilities.UColor;

public class MapLayer implements Painter<Object>, MouseListener, MouseMotionListener, KeyListener {

	public static JXMapViewer mapViewer = null;
	public static DeviceList nodeList = null;
	public static MarkerList markerList = null;
	public static BuildingList buildingList = null;
	public static GeoZoneList geoZoneList = null;
	public static int mX = 0;
	public static int mY = 0;
	public static char lastKey = 0;
	public static int lastKeyCode = 0;
	public static boolean drawSelectionRectangle = false;
	public static int cadreX1 = 0;
	public static int cadreY1 = 0;
	public static int cadreX2 = 0;
	public static int cadreY2 = 0;
	public static int selectType = MapObject.SENSOR;
	public static boolean altDown = false;
	public static boolean shiftDown = false;
	public static boolean cmdDown = false;
	public static boolean ctrlDown = false;
	public static boolean mousePressed = false;
	public static String projectPath = "";
	public static String projectName = "";
	public static boolean dark = false;
	public static boolean blockBuildings = false;
	
	private boolean startSelection = false;
	
	public static boolean magnetic = false;
	public static int magnetic_step = 16;
	
	public static boolean multipleSelection = false;
	public static boolean button3Clicked = false;
	
	public static boolean showInfos = true;
	public static boolean showBakhground = true;
	public static int bg_transparency = 255;
	
	private int cx ;
	private int cy ;
	
	public MapLayer() {}
	
	public MapLayer(JXMapViewer mapViewer) {		
		MapLayer.mapViewer = mapViewer;
		initLists();
		mapViewer.setOverlayPainter(this);
		mapViewer.addMouseListener(this);
		mapViewer.addMouseMotionListener(this);
		mapViewer.addKeyListener(this);
	}
	
	public static void initLists() {
		buildingList = new BuildingList();
		geoZoneList = new GeoZoneList();
		markerList = new MarkerList();
		nodeList = new DeviceList();
	}

	public boolean isstartSelection() {
		return startSelection;
	}

	public void setstartSelection(boolean startSelection) {
		this.startSelection = startSelection;
	}
	
	public static int numberOfInsideAndSelected = 0;
	
	@Override
	public void paint(Graphics2D g, Object arg1, int arg2, int arg3) {
		//g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		if(mapViewer.getZoom()==0) g.setFont(new Font("arial", Font.PLAIN, 14));
		if(mapViewer.getZoom()==1) g.setFont(new Font("arial", Font.BOLD, 10));
		if(mapViewer.getZoom()==2) g.setFont(new Font("arial", Font.BOLD, 8));
		if(mapViewer.getZoom()==3) g.setFont(new Font("arial", Font.BOLD, 8));
		if(mapViewer.getZoom()==4) g.setFont(new Font("arial", Font.BOLD, 7));
		if(mapViewer.getZoom()>4) g.setFont(new Font("arial", Font.BOLD, 5));
		
		Rectangle rect = mapViewer.getViewportBounds();
		g.translate(-rect.x, -rect.y);				
		
		if(showBakhground) {
			g.setColor(new Color(255,255,255,bg_transparency));
			g.fillRect((int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)-20, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)-20, mapViewer.getWidth()+40,mapViewer.getHeight()+40);
			//g.setColor(new Color(200,200,200,bg_transparency));
			//g.drawRect((int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+2, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+2, mapViewer.getWidth()-4,mapViewer.getHeight()-4);
		}
		
		mapViewer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		boolean addThing = false;
		if (lastKey == '1') {					
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '2') {
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '3') {
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '4') {
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '5') {
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '6') {
			mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
			addThing = true;
		}
		if (lastKey == '7') {
			if(DeviceList.weather==null) {
				mapViewer.setCursor(new Cursor(Cursor.HAND_CURSOR));
				addThing = true;
			}
			else
				lastKey = 0;
		}
		if (lastKey == '8') {
			mapViewer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			addThing = true;
		}
		
		if(magnetic && addThing) {			
			g.drawLine(mX-6, mY, mX+6, mY);
			g.drawLine(mX, mY-6, mX, mY+6);
		}

		if(!shiftDown && !button3Clicked)
			mapViewer.setPanEnabled(true);
		
		//if (!OsmOverpass.isLoading) {
			if(buildingList != null)
				buildingList.draw(g);
		//}
		
		numberOfInsideAndSelected = 0;
		
		if(Routes.routes != null)
			if(Routes.routes.size()>0 && NetworkParameters.displayAllRoutes)
				Routes.draw(g);
		
		
		markerList.draw(g);
		
		nodeList.drawMarkedEdges(g);
		
		nodeList.draw(g);
		
		nodeList.drawHulls(g);
		

		if (drawSelectionRectangle) {
			g.setStroke(new BasicStroke(0.5f));
			Point2D p1 = MapCalc.pixelPanelToPixelMap(cadreX1, cadreY1);
			Point2D p2 = MapCalc.pixelPanelToPixelMap(cadreX2, cadreY2);
			g.setColor(UColor.BLACK_TTTTRANSPARENT);
			if(dark) g.setColor(UColor.WHITE_LLTRANSPARENT);
			g.fillRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
			g.setColor(Color.LIGHT_GRAY);
			if(dark) g.setColor(UColor.WHITE_LTRANSPARENT);
			g.drawRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
		}
		
		g.setFont(new Font("courier", Font.BOLD, 10));
		g.setColor(Color.DARK_GRAY);
		if(dark) g.setColor(new Color(198,232,106));

		if(WisenSimulation.isSimulating) {
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(1.0f));
			g.drawRect((int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+5, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+5, mapViewer.getWidth()-10, mapViewer.getHeight()-10);
		}
		if(showInfos) {
			g.drawString(String.format("Time: %4.4f s", WisenSimulation.sTime) , (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+16);
			g.drawString("Number of SENT messages:"+MessageEventList.numberOfSentMessages + " ["+ MessageEventList.numberOfSentMessages_b +"]", (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+26);
			g.drawString("Number of RECEIVED messages:"+MessageEventList.numberOfReceivedMessages + " ["+ MessageEventList.numberOfReceivedMessages_b +"]", (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+36);
			g.drawString("Number of ACK messages:"+MessageEventList.numberOfAckMessages + " ["+ MessageEventList.numberOfAckMessages_b +"]", (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+46);
			g.drawString("Number of LOST messages:"+MessageEventList.numberOfLostMessages + " ["+ MessageEventList.numberOfLostMessages_b +"]", (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+56);
			if(DeviceList.weather != null)
				g.drawString("Temperature: "+ String.format("%2.2f", DeviceList.weather.getValue()) , (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+66);
			g.drawString(BuildingList.locked?"[L]":"", (int)mapViewer.getCenter().getX()-(mapViewer.getWidth()/2)+8, (int)mapViewer.getCenter().getY()-(mapViewer.getHeight()/2)+76);
		}
		
		//int [] coord = MapCalc.geoToPixelMapA(48.391412753283895, -4.4883012771606445);
		//g.drawOval(coord[0], coord[1], 20, 20);
		
		g.dispose();
	}
	
	public static DeviceList getDeviceList() {
		return nodeList;
	}

	public void simulate() {
		nodeList.simulate();
	}

	public void simulateAll() {
		nodeList.simulateAll();
	}

	public void simulateSensors() {
		nodeList.simulateSensors();
	}

	public void simulateMobiles() {
		nodeList.simulateMobiles();
	}

	public static void addAction(CupAction action) {
		CupActionBlock block = new CupActionBlock();
		block.addAction(action);
		CupActionStack.add(block);
	}
	
	private int expanded = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {		
		if(e.getButton()==MouseEvent.BUTTON3 && lastKey != 0) {
			DeviceList.deselectAll();
			lastKey = 0;
		}
		Point p = new Point(cx, cy);
		GeoPosition gp = mapViewer.convertPointToGeoPosition(p);
		if (lastKey == '1') {
			CupAction action = new CupActionAddSensor(new StdSensorNode(gp.getLongitude(), gp.getLatitude(), 0, 0, 100, 20, -1));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}
		if (lastKey == '2') {
			CupAction action = new CupActionAddDevice(new Gas(gp.getLongitude(), gp.getLatitude(), 0, 10, -1));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}
		if (lastKey == '4') {
			CupAction action = new CupActionAddSensor(new DirectionalSensorNode(gp.getLongitude(), gp.getLatitude(), 0, 0, 100, 60, -1, 0.1, 5, 12));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}			
		if (lastKey == '5') {
			CupAction action = new CupActionAddSensor(new BaseStation(gp.getLongitude(), gp.getLatitude(), 0, 0, 100, 20, -1));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}
		if (lastKey == '6') {
			CupAction action = new CupActionAddDevice(new Mobile(gp.getLongitude(), gp.getLatitude(), 0, 8, -1));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}
		if (lastKey == '7') {
			if(DeviceList.weather==null) {
				CupAction action = new CupActionAddDevice(new Weather(gp.getLongitude(), gp.getLatitude(), 0, 8, -1));
				addAction(action);
				CupActionStack.execute();
				lastKey = 0;
				repaint();
			}
		}
		if (lastKey == '8') {
			CupAction action = new CupActionAddMarker(new Marker(gp.getLongitude(), gp.getLatitude(), 0, 4));
			addAction(action);
			CupActionStack.execute();
			repaint();
		}
		
		if(e.getClickCount()==2) {
			MarkerList.insertMarkers();
			for(SensorNode sensor : DeviceList.sensors) {
				if(sensor.isSelected()) {
					if(expanded == 2) {
						expanded = 0;
					}
					if(expanded == 1) {
						expanded++;
						CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
						break;
					}
					if(expanded == 0) {
						expanded++;
						CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
						break;
					}
				}
			}
			for(Device device : DeviceList.devices) {
				if(device.isSelected()) {
					if(expanded == 2) {
						expanded = 0;
					}
					if(expanded == 1) {
						expanded++;
						CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
						break;
					}
					if(expanded == 0) {
						expanded++;
						CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
						break;
					}
				}
			}
		}
		
		if(CupCarbon.cupCarbonController!=null) {
			CupCarbon.cupCarbonController.updateObjectListView();
			CupCarbon.cupCarbonController.getNodeInformations();
			CupCarbon.cupCarbonController.getRadioInformations();
			CupCarbon.cupCarbonController.updateSelectionInListView();
		}
	}

	public static void addMarker(int index, Marker marker) {
		MarkerList.add(index, marker);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		if (shiftDown || e.getButton()==MouseEvent.BUTTON3) {
			button3Clicked = true;
			mapViewer.setPanEnabled(false);
			startSelection = true;
			_cadreX1 = e.getX();
			_cadreY1 = e.getY();
		}
		else {
			int nbSelected = 0;
			for(SensorNode sensor : DeviceList.sensors) {
				if(!e.isControlDown() && numberOfInsideAndSelected==0) {
					sensor.setSelected(false);
				}				
				if(e.getButton()==MouseEvent.BUTTON1 && sensor.isInside()) {
					if(e.isControlDown())
						sensor.setSelected(!sensor.isSelected());
					else
						if(numberOfInsideAndSelected==0 && nbSelected==0) {
							sensor.setSelected(true);
							nbSelected++;
						}
				}
			}
			
			for(Device device : DeviceList.devices) {				
				if(!e.isControlDown() && numberOfInsideAndSelected==0) {
					device.setSelected(false);
				}	
				if(e.getButton()==MouseEvent.BUTTON1 && device.isInside()) {
					if(e.isControlDown())
						device.setSelected(!device.isSelected());
					else 
						if(numberOfInsideAndSelected==0 && nbSelected==0) {
							device.setSelected(true);
							nbSelected++;
						}
				}
			}
			
			for(Marker marker : MarkerList.markers) {
				if(!e.isControlDown() && numberOfInsideAndSelected==0) {
					marker.setSelected(false);
				}	
				if(e.getButton()==MouseEvent.BUTTON1 && marker.isInside()) {
					if(e.isControlDown())
						marker.setSelected(!marker.isSelected());
					else 
						if(numberOfInsideAndSelected==0 && nbSelected==0) {
							marker.setSelected(true);
							nbSelected++;
						}
				}
			}
			repaint();
		}

	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		cx = e.getX();
		cy = e.getY();
		
		if (magnetic) {
			cx = cx - (cx % magnetic_step) - (mapViewer.getViewportBounds().x % magnetic_step);
			cy = cy - (cy % magnetic_step) - (mapViewer.getViewportBounds().y % magnetic_step);
		}
		
		if(magnetic) {
			Point2D pd = MapCalc.pixelPanelToPixelMap(cx, cy);
			mX = (int) pd.getX();
			mY = (int) pd.getY();
			repaint();
		}
		
		movingSensors(e.getX(), e.getY()) ;		
	}
	
	public void movingSensors(int x, int y) {
		boolean selection = false;
		for(SensorNode sensor : DeviceList.sensors) {			
			sensor.calculateDxDy(cx, cy);	
			boolean tmp_inside = sensor.isInside();	
			sensor.inside(x, y);	
			if (sensor.isInside() != tmp_inside) {
				selection = true;
				repaint();
			}
		}
		
		for(Device device : DeviceList.devices) {			
			device.calculateDxDy(cx, cy);	
			boolean tmp_inside = device.isInside();	
			device.inside(x, y);	
			if (device.isInside() != tmp_inside) {
				selection = true;
				repaint();
			}
		}
		
		for(Marker marker : MarkerList.markers) {			
			marker.calculateDxDy(cx, cy);	
			boolean tmp_inside = marker.isInside();	
			marker.inside(x, y);	
			if (marker.isInside() != tmp_inside) {
				selection = true;
				repaint();
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
	
	private boolean dragging = false;
	public int _cadreX1 = 0;
	public int _cadreY1 = 0;
	public int _cadreX2 = 0;
	public int _cadreY2 = 0;
	
	@Override
	public void mouseDragged(MouseEvent e) {		
		if (shiftDown || e.getButton()==MouseEvent.BUTTON3) {
			_cadreX2 = e.getX();
			_cadreY2 = e.getY();
			
			int mx1 = Math.min(_cadreX1, _cadreX2);
			int my1 = Math.min(_cadreY1, _cadreY2);
			int mx2 = Math.max(_cadreX1, _cadreX2);
			int my2 = Math.max(_cadreY1, _cadreY2);
			
			cadreX1 = mx1;
			cadreY1 = my1;
			cadreX2 = mx2;
			cadreY2 = my2;
			
			drawSelectionRectangle = true;
			repaint();
		}
		else {
			if(e.getButton() == MouseEvent.BUTTON1) {
				for(SensorNode sensor : DeviceList.sensors) {
					if (sensor.isSelected()) {
						if(!dragging) {
							DeviceList.initMarkedEdges();
							sensor.calculateDxDy(e.getX(), e.getY());
							sensor.setPrevLongitude(sensor.getLongitude());
							sensor.setPrevLatitude(sensor.getLatitude());
						}
						cx = e.getX();
						cy = e.getY();
						sensor.moveTo(cx, cy, 0);
					}					
				}
				
				for(Device device : DeviceList.devices) {
					if (device.isSelected()) {
						if(!dragging) {							
							device.calculateDxDy(e.getX(), e.getY());
							device.setPrevLongitude(device.getLongitude());
							device.setPrevLatitude(device.getLatitude());
						}
						cx = e.getX();
						cy = e.getY();
						device.moveTo(cx, cy, 0);
					}					
				}
				
				for(Marker marker : MarkerList.markers) {
					if (marker.isSelected()) {
						if(!dragging) {
							marker.calculateDxDy(e.getX(), e.getY());
							marker.setPrevLongitude(marker.getLongitude());
							marker.setPrevLatitude(marker.getLatitude());
						}
						cx = e.getX();
						cy = e.getY();
						marker.moveTo(cx, cy, 0);
					}					
				}
				
				dragging = true;
				if(DeviceList.propagationsCalculated)
					DeviceList.calculatePropagations();
			}
		}
	}
	
	public void addActionAfterMoving() {	
		if(dragging) {
			if(CupCarbon.cupCarbonController!=null) {
				CupCarbon.cupCarbonController.updateObjectListView();
				CupCarbon.cupCarbonController.getNodeInformations();
				CupCarbon.cupCarbonController.getRadioInformations();
				CupCarbon.cupCarbonController.updateSelectionInListView();
			}
			
			dragging = false;
			CupActionBlock block = new CupActionBlock();
			for(SensorNode sensor : DeviceList.sensors) {
				if (sensor.isSelected()) {
					CupActionMapObjectMove action = new CupActionMapObjectMove(sensor);
					block.addAction(action);				
				}
			}
			
			for(Device device : DeviceList.devices) {
				if (device.isSelected()) {
					CupActionMapObjectMove action = new CupActionMapObjectMove(device);
					block.addAction(action);				
				}
			}
			
			for(Marker marker : MarkerList.markers) {				
				if (marker.isSelected()) {
					CupActionMapObjectMove action = new CupActionMapObjectMove(marker);
					block.addAction(action);				
				}
			}
			
			if(block.size()>0) {
				CupActionStack.add(block);
				CupActionStack.execute();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {		
		addActionAfterMoving();
		if (shiftDown || (mousePressed && e.getButton()==MouseEvent.BUTTON3)) {
			mapViewer.setPanEnabled(true);
			button3Clicked = false;
			mousePressed = false;
			lastKeyCode = 0;
			startSelection = false;
			
			MapLayer.multipleSelection = true;
			
			if (drawSelectionRectangle) {
				nodeList.selectInsideRectangle(cadreX1, cadreY1, cadreX2, cadreY2);
				markerList.selectInsideRectangle(cadreX1, cadreY1, cadreX2, cadreY2);
				buildingList.selectInsideRectangle(cadreX1, cadreY1, cadreX2, cadreY2);
			}
			drawSelectionRectangle = false;
			repaint();
		}
		mousePressed = false;
	}

	public static boolean insideSelection(double x, double y, int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
		GeoPosition gp1 = mapViewer.convertPointToGeoPosition(
				new Point(cadreX1, cadreY1));
		GeoPosition gp2 = mapViewer.convertPointToGeoPosition(
				new Point(cadreX2, cadreY2));
		return y < gp1.getLatitude() && x > gp1.getLongitude()
				&& y > gp2.getLatitude() && x < gp2.getLongitude();
	}
	
	public void deleteIfSelected() {
		CupActionBlock block = new CupActionBlock();
		SensorNode sensor;
		for (Iterator<SensorNode> iterator = DeviceList.sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			if (sensor.isSelected()) {
				CupActionDeleteSensor action = new CupActionDeleteSensor(sensor);
				block.addAction(action);				
			}			
		}
		
		Device device;
		for (Iterator<Device> iterator = DeviceList.devices.iterator(); iterator.hasNext();) {
			device = iterator.next();
			if (device.isSelected()) {
				CupActionDeleteDevice action = new CupActionDeleteDevice(device);
				block.addAction(action);
			}
		}
		
		Marker marker;
		int index = 0;
		for (Iterator<Marker> iterator = MarkerList.markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			if (marker.isSelected()) {
				CupActionDeleteMarker action = new CupActionDeleteMarker(marker, index);
				block.addAction(action);
			}
			index++;
		}
		
		Building building;
		for (Iterator<Building> iterator = BuildingList.buildings.iterator(); iterator.hasNext();) {
			building = iterator.next();
			if (building.isSelected() && !building.isHide()) {
				CupActionDeleteBuilding action = new CupActionDeleteBuilding(building);
				block.addAction(action);
			}
		}
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute(); 
		}

		
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		ctrlDown = key.isControlDown();
		cmdDown = key.isMetaDown();
		shiftDown = key.isShiftDown();
		altDown = key.isAltDown();
		lastKeyCode = key.getKeyCode();
		
		if(key.getKeyCode()==68 && altDown)
			showInfos = !showInfos;
		
		if(key.getKeyCode()==70 && altDown)
			showBakhground = !showBakhground;
		
		if(key.getKeyChar()=='a' && !ctrlDown && !cmdDown && !shiftDown && !altDown ) {
			NetworkParameters.drawSensorArrows = !NetworkParameters.drawSensorArrows; 
		}
		
		if(key.getKeyChar()=='A' && !ctrlDown && !cmdDown && !altDown ) {
			NetworkParameters.drawMarkerArrows = !NetworkParameters.drawMarkerArrows; 
		}
		
		if(key.getKeyChar() == KeyEvent.VK_SPACE) {
			CupCarbon.cupCarbonController.splitPaneShowHide();
		}

		if ((key.getKeyCode() == 8) || (key.getKeyCode() == 127)) {		// 8: DEL and 127: Sup
			deleteIfSelected();
			if(CupCarbon.cupCarbonController!=null) {
				CupCarbon.cupCarbonController.updateObjectListView();
				CupCarbon.cupCarbonController.updateSelectionInListView();
			}
		}

		if(key.getKeyChar() == 130) {
			NetworkParameters.drawSensorArrows = !NetworkParameters.drawSensorArrows;
		}
		
		if (key.getKeyChar() == 'd') {
			NetworkParameters.displayDetails = !NetworkParameters.displayDetails;
		}
		
		if (key.getKeyChar() == 'D') {
			NetworkParameters.displayPrintMessage = !NetworkParameters.displayPrintMessage;
		}
		
		if (key.getKeyChar() == 'R') {
			NetworkParameters.displayAllRoutes = !NetworkParameters.displayAllRoutes;
		}
		
		if (key.getKeyChar() == 'm') {
			NetworkParameters.displayRadioMessages = !NetworkParameters.displayRadioMessages ;
		}
		
		if (key.getKeyCode() == 27) { // escape
			addActionAfterMoving();
			for(SensorNode sensor : DeviceList.sensors) {
				sensor.initSelection();				
			}

			for(Device device : DeviceList.devices) {
				device.initSelection();
			}
			
			for(Marker marker : MarkerList.markers) {
				marker.initSelection();
			}
			
			if(CupCarbon.cupCarbonController!=null) {
				CupCarbon.cupCarbonController.updateObjectListView();
				CupCarbon.cupCarbonController.updateSelectionInListView();
			}
			lastKey = 0;
		}		
		
		repaint();
	}
	
	
	
	
	public static void escape() {
		mapViewer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		lastKey = 0;
		repaint();		
	}
	
	@Override
	public void keyReleased(KeyEvent key) {
		if(shiftDown && mousePressed) {
			drawSelectionRectangle = false;
			nodeList.selectInsideRectangle(cadreX1, cadreX2, cadreY1, cadreY2);
			markerList.selectInsideRectangle(cadreX1, cadreX2, cadreY1, cadreY2);
			buildingList.selectInsideRectangle(cadreX1, cadreY1, cadreX2, cadreY2);
			repaint();
		}
		
		altDown = false;
		shiftDown = false;
		ctrlDown = false;
		cmdDown = false;
		
	}

	public static void duplicate() {
		CupActionBlock block = new CupActionBlock();
		for(SensorNode sensor : DeviceList.sensors) {
			if (sensor.isSelected()) {
				CupAction action;
				if(MapLayer.magnetic)
					action = new CupActionAddSensor(sensor.duplicate());
				else
					action = new CupActionAddSensor(sensor.duplicateWithShift(0.0002,0.0002,0));
				block.addAction(action);
			}
		}
		for(Device device : DeviceList.devices) {
			if (device.isSelected()) {	
				CupAction action;
				if(MapLayer.magnetic)
					action = new CupActionAddDevice(device.duplicate());
				else
					action = new CupActionAddDevice(device.duplicateWithShift(0.0002,0.0002,0));
				block.addAction(action);
			}
		}
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute();
		}
	}
	
	public void keyTypedForDevices(KeyEvent key) {
		CupActionBlock block = new CupActionBlock();
		for(Device device : DeviceList.devices) {
			if (device.isSelected()) {	
//				if (key.getKeyChar() == 'k') {
//					device.switchVisible();
//				}
	
				if (key.getKeyChar() == 'h') {
					device.incHide();
				}
				
				if (key.getKeyChar() == 'j') {
					device.setHide(0);
				}
				
				if (key.getKeyChar() == ';') {
					CupAction action = new CupActionModifDeviceRadius(device, device.getRadius(), device.getRadius()+5);
					block.addAction(action);
				}
		
				if (key.getKeyChar() == ',') {
					if(device.getRadius()>0) {
						CupAction action = new CupActionModifDeviceRadius(device, device.getRadius(), device.getRadius()-5);
						block.addAction(action);
					}
				}
							
				if (key.getKeyChar() == 'c') {
					if(!mousePressed) {
						CupAction action;
						//if(MapLayer.magnetic)
							action = new CupActionAddDevice(device.duplicate());
						//else
						//	action = new CupActionAddDevice(device.duplicateWithShift(0.0002,0.0002,0));
						block.addAction(action);
					}
				}
				
				if (key.getKeyChar() == 'S') {
					device.agentSimulation();
				}
				
			}
			
			if (key.getKeyChar() == 'q') {
				device.stopAgentSimulation();
			}
			
			if (key.getKeyChar() == 'i') {
				device.invSelection();
			}
	
			if (key.getKeyChar() == '1' || key.getKeyChar() == '2' || key.getKeyChar() == '3' || key.getKeyChar() == '4' || key.getKeyChar() == '5'
					|| key.getKeyChar() == '6' || key.getKeyChar() == '7' || key.getKeyChar() == '8') {
				device.setSelected(false);
			}
		}
		
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute();
		}
	}
	
	public void keyTypedForSensors(KeyEvent key) {
		CupActionBlock block = new CupActionBlock();
		for(SensorNode sensor : DeviceList.sensors) {
			if (sensor.isSelected()) {	
				if ((key.getKeyChar() == '(') || (key.getKeyChar() == '[')) {
					int d = 5;
					if(key.getKeyChar() == '[') d = 1;
					CupAction action = new CupActionModifSensorUnitRadius(sensor, sensor.getSensorUnitRadius(), sensor.getSensorUnitRadius()-d);
					block.addAction(action);
					if(CupCarbon.cupCarbonController!=null) {
						CupCarbon.cupCarbonController.getSensorInformations();
					}
				}
				if ((key.getKeyChar() == ')') || (key.getKeyChar() == ']')) {
					int d = 5;
					if(key.getKeyChar() == ']') d = 1;
					CupAction action = new CupActionModifSensorUnitRadius(sensor, sensor.getSensorUnitRadius(), sensor.getSensorUnitRadius()+d);
					block.addAction(action);
					if(CupCarbon.cupCarbonController!=null) {
						CupCarbon.cupCarbonController.getSensorInformations();
					}
				}
				
				if ((key.getKeyChar() == 960) || (key.getKeyChar() == 'p')) {
					if(sensor.getType()==Device.DIRECTIONAL_SENSOR) {
						double d = 0.1;
						if(key.getKeyChar() == 960) d=0.01;
						double v =  ((DirectionalSensorNode)sensor).getSensorUnitDirection()+d;
						if(v>6.28) v=0.0;
						CupAction action = new CupActionModifDirectionalSensorUnitDirection(sensor, ((DirectionalSensorNode)sensor).getSensorUnitDirection(), v);
						block.addAction(action);
						if(CupCarbon.cupCarbonController!=null) {
							CupCarbon.cupCarbonController.getSensorInformations();
						}
					}
				}
				
				if ((key.getKeyChar() == 339) || (key.getKeyChar() == 'o')) {
					if(sensor.getType()==Device.DIRECTIONAL_SENSOR) {
						double d = 0.1;
						if(key.getKeyChar() == 339) d=0.01;
						double v =  ((DirectionalSensorNode)sensor).getSensorUnitDirection()-d;
						if(v<0.0) v=6.28;
						CupAction action = new CupActionModifDirectionalSensorUnitDirection(sensor, ((DirectionalSensorNode)sensor).getSensorUnitDirection(), v);
						block.addAction(action);
						if(CupCarbon.cupCarbonController!=null) {
							CupCarbon.cupCarbonController.getSensorInformations();
						}
					}
				}
				
				if ((key.getKeyChar() == 8719) || (key.getKeyChar() == 'P')) {
					if(sensor.getType()==Device.DIRECTIONAL_SENSOR) {
						double d = 0.01;
						if(key.getKeyChar() == 8719) d=0.001;
						double v =  ((DirectionalSensorNode)sensor).getSensorUnitCoverage()+d;
						if(v>0.628) v = 0.628;
						CupAction action = new CupActionModifDirectionalSensorUnitCoverage(sensor, ((DirectionalSensorNode)sensor).getSensorUnitCoverage(), v);
						block.addAction(action);
						if(CupCarbon.cupCarbonController!=null) {
							CupCarbon.cupCarbonController.getSensorInformations();
						}
					}
				}
				
				if ((key.getKeyChar() == 338) || (key.getKeyChar() == 'O')) {
					if(sensor.getType()==Device.DIRECTIONAL_SENSOR) {
						double d = 0.01;
						if(key.getKeyChar() == 338) d=0.001;
						double v =  ((DirectionalSensorNode)sensor).getSensorUnitCoverage()-d;
						if(v<0) v = 0.0;
						CupAction action = new CupActionModifDirectionalSensorUnitCoverage(sensor, ((DirectionalSensorNode)sensor).getSensorUnitCoverage(), v);
						block.addAction(action);
						if(CupCarbon.cupCarbonController!=null) {
							CupCarbon.cupCarbonController.getSensorInformations();
						}
					}
				}
				
//				if (key.getKeyChar() == 'k') {
//					sensor.switchVisible();
//				}
	
				if (key.getKeyChar() == 'h') {
					sensor.incHide();
					
				}
				
				if (key.getKeyChar() == 'j') {
					sensor.setHide(0);
				}
				
				if (key.getKeyChar() == ';') {
					CupAction action = new CupActionModifSensorRadius(sensor, sensor.getRadius(), sensor.getRadius()+5);
					block.addAction(action);
				}
		
				if (key.getKeyChar() == ',') {
					if(sensor.getRadius()>0) {
						CupAction action = new CupActionModifSensorRadius(sensor, sensor.getRadius(), sensor.getRadius()-5);
						block.addAction(action);
					}
				}
							
				if (key.getKeyChar() == 'c') {
					if(!mousePressed) {
						CupAction action;
						//if(MapLayer.magnetic)
							action = new CupActionAddSensor(sensor.duplicate());
						//else
						//	action = new CupActionAddSensor(sensor.duplicateWithShift(0.0002,0.0002,0));
						block.addAction(action);
					}
				}

				if (key.getKeyChar() == 'b') {
					sensor.invertDrawBatteryLevel();
				}
				
				if (key.getKeyChar() == 'B') {
					blockBuildings = !blockBuildings ;
				}
				
				if (key.getKeyChar() == 'k') {
					if (sensor.getBatteryLevel()>0) {
						sensor.setBatteryLevel(0);					
					}
					else
						sensor.initBattery();					
				}				
				
				if(key.getKeyChar()=='+') {
					CupAction action = new CupActionModifRadioRadius(sensor.getCurrentRadioModule(), sensor.getCurrentRadioModule().getRadioRangeRadius(), sensor.getCurrentRadioModule().getRadioRangeRadius()+1);
					block.addAction(action);
					if(CupCarbon.cupCarbonController!=null) {
						CupCarbon.cupCarbonController.getRadioInformations();
					}
				}
				if(key.getKeyChar()=='-') {
					if(sensor.getCurrentRadioModule().getRadioRangeRadius()>0) {
						CupAction action = new CupActionModifRadioRadius(sensor.getCurrentRadioModule(), sensor.getCurrentRadioModule().getRadioRangeRadius(), sensor.getCurrentRadioModule().getRadioRangeRadius()-1);
						block.addAction(action);
						if(CupCarbon.cupCarbonController!=null) {
							CupCarbon.cupCarbonController.getRadioInformations();
						}
					}
				}
				
				if (key.getKeyChar() == 'S') {
					sensor.agentSimulation();
				}				
	
				if (key.getKeyChar() == 'e') {
					sensor.setDisplaydRadius(!sensor.getDisplaydRadius());
				}
	
				if (key.getKeyChar() == 'r') {
					sensor.setDisplaydInfos(!sensor.getDisplaydInfos());
				}				
						
				if (key.getKeyChar() == '1' || key.getKeyChar() == '2' || key.getKeyChar() == '3' || key.getKeyChar() == '4' || key.getKeyChar() == '5'
						|| key.getKeyChar() == '6' || key.getKeyChar() == '7' || key.getKeyChar() == '8') {
					sensor.setSelected(false);
				}
			}
			if (key.getKeyChar() == 'i') {
				sensor.invSelection();
			}
			
			if (key.getKeyChar() == 'q') {
				sensor.stopAgentSimulation();
			}
		}
		
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute();
		}
	}
	
	public void keyTypedForMarkers(KeyEvent key) {
		for(Marker marker : MarkerList.markers) {
			if (key.getKeyChar() == 'i') {
				marker.invSelection();
			}
			
			if (marker.isSelected()) {	
				if (key.getKeyChar() == '1' || key.getKeyChar() == '2' || key.getKeyChar() == '3' || key.getKeyChar() == '4' || key.getKeyChar() == '5'
						|| key.getKeyChar() == '6' || key.getKeyChar() == '7' || key.getKeyChar() == '8') {
					marker.setSelected(false);
				}
			}
		}			
	}
	
	@Override
	public void keyTyped(KeyEvent key) {
		lastKey = key.getKeyChar();
		
		if (lastKey == 'n') {
			NetworkParameters.drawScriptFileName = !NetworkParameters.drawScriptFileName;					
		}
		
		if(lastKey == 'l') {
			NetworkParameters.drawRadioLinks = !NetworkParameters.drawRadioLinks;
		}
		
		if(lastKey == 'L') {
			BuildingList.locked = !BuildingList.locked ;
		}
		
		if (lastKey == 'v' || lastKey=='V') {
			nextLinkColor(lastKey);
		}

		if (lastKey == 198) {
			NetworkParameters.drawMarkerArrows = !NetworkParameters.drawMarkerArrows;
		}
		
		if (lastKey == 'w') {			
			selectNodesMarkers();
		}
		
		if (lastKey == '>') {
			Device.moveSpeed += 5;
		}

		if (lastKey == '<') {
			Device.moveSpeed -= 5;
			if (Device.moveSpeed < 0)
				Device.moveSpeed = 0;
		}
		
		if(lastKey == 'f') {
			DeviceList.selectOneFromSelected();
		}
		
		if (key.getKeyChar() == 'x') {			
			NetworkParameters.displayRLDistance = !NetworkParameters.displayRLDistance;
		}
		
		if (key.getKeyChar() == 'X') {			
			NetworkParameters.displayMarkerDistance = !NetworkParameters.displayMarkerDistance;
		}
		
		if(key.getKeyChar()=='t') {
			MarkerList.transformMarkersToSensors();
		}
				
		if(key.getKeyChar()=='u') {
			MarkerList.insertMarkers();
		}
		
		if(key.getKeyChar()=='U') {
			MarkerList.selectNextMarkers();
		}
		
		if (key.getKeyChar() == ':') {
			int n = MarkerList.size();
        	Building building = new Building(n);
        	
        	for (int i=0; i<n; i++){
        		building.set(MarkerList.get(i).getLongitude(), MarkerList.get(i).getLatitude(), i);
        	}
        	
        	mapViewer.addMouseListener(building);
    		mapViewer.addKeyListener(building);
    		
    		CupAction action = new CupActionAddBuilding(building);
			CupActionBlock block = new CupActionBlock();
			block.addAction(action);
			CupActionStack.add(block);			
			CupActionStack.execute();			
			MarkerList.selectAll();
		}
		
		keyTypedForSensors(key);
		keyTypedForDevices(key);
		keyTypedForMarkers(key);
		
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		repaint();
		
	}
	
	public static void selectNodesMarkers() {
		if(selectType==MapObject.NONE) {
			MapLayer.numberOfInsideAndSelected = 0;
			selectType = MapObject.SENSOR;
			DeviceList.deselectAll();
			MarkerList.deselectAll();
		}
		else {
			if(selectType==MapObject.MARKER) {
				selectType = MapObject.NONE;
				DeviceList.deselectAll();
				MarkerList.deselectAll();
				WorldMap.setSelectionOfAllMarkers(true, false);
				MapLayer.numberOfInsideAndSelected = MarkerList.markers.size();
			}
			if(selectType==MapObject.SENSOR) {
				selectType = MapObject.MARKER;
				WorldMap.setSelectionOfAllNodes(true, Device.SENSOR, false);
				MapLayer.numberOfInsideAndSelected = DeviceList.sensors.size();
			}
		}
	}
	
	public static void nextLinkColor(char c) {
		if(c=='v') NetworkParameters.radioLinksColor++;
		if(c=='V') NetworkParameters.radioLinksColor--;
		if(NetworkParameters.radioLinksColor>5)
			NetworkParameters.radioLinksColor = 0;
		if(NetworkParameters.radioLinksColor<0)
			NetworkParameters.radioLinksColor=5;
	}	

	

	public void addNodeInMap(char c) {
		lastKey = c;
		repaint();
	}

	public void loadCityNodes() {
		NetworkLoader nl = new NetworkLoader(mapViewer);
		nl.start();
	}

	public void setSelectionOfAllNodes(boolean selection, int type, boolean addSelection) {
		nodeList.setSelectionOfAllNodes(selection, type, addSelection);
	}
	
	public void setSelectionOfAllMobileNodes(boolean selection, int type, boolean addSelection) {
		nodeList.setSelectionOfAllMobileNodes(selection, type, addSelection);
	}

	public void invertSelection() {
		nodeList.invertSelection();
	}

	public void setSelectionOfAllMarkers(boolean selection, boolean addSelection) {
		markerList.setSelectionOfAllMarkers(selection, addSelection);		
	}
	
	public void setSelectionOfAllBuildings(boolean selection, boolean addSelection) {
		buildingList.setSelectionOfAllBuildings(selection, selection);		
	}
	
	public static void repaint() {
		mapViewer.repaint();
	}
	
	public static void drawDistance(double longitude1, double latitude1, double elevation1, double longitude2, double latitude2, double elevation2, Graphics g) {
		int[] coord = MapCalc.geoToPixelMapA(latitude1, longitude1);
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToPixelMapA(latitude2, longitude2);
		int lx2 = coord[0];
		int ly2 = coord[1];
		g.setColor(new Color(103,103,103));
		if(MapLayer.dark)
			g.setColor(Color.LIGHT_GRAY);
		int d = (int) MapLayer.distance(longitude1, latitude1, longitude2, latitude2);
		g.drawString("" + d, ((lx1 + lx2) / 2), ((ly1 + ly2) / 2 + 10));
	}
	
	public static void drawMessage(int lx1, int lx2, int ly1, int ly2, String message, Graphics g) {		
		g.setColor(Color.BLACK);
		if(dark) g.setColor(new Color(243,210,29));
		g.drawString(message, ((lx1 + lx2) / 2), ((ly1 + ly2) / 2 - 10));
	}
	
	public static void drawMessageAttempts(int lx1, int lx2, int ly1, int ly2, String message, Graphics g) {
		if(SimulationInputs.ack) {
			g.setColor(Color.BLACK);
			if(dark) g.setColor(Color.YELLOW);
			g.drawString("["+message+"]", ((lx1 + lx2) / 2) -15, ((ly1 + ly2) / 2 - 10));
		}
	}
	
	/**
	 * @param device
	 * @return the distance in meters between the current device and the one
	 *         given as a parameter
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return MapCalc.distance(x1, y1, x2, y2);
	}

}
