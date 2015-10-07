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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import markers.Marker;
import markers.MarkerList;
import natural_events.Gas;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

import overpass.OsmOverpass;
import utilities.MapCalc;
import utilities.UColor;
import actions_ui.AddDevice;
import actions_ui.AddMarker;
import buildings.Building;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import cupcarbon.CupCarbonMap;
import device.BaseStation;
import device.Device;
import device.DeviceList;
import device.MediaSensorNode;
import device.Mobile;
import device.MobileWithRadio;
import device.NetworkLoader;
import device.StdSensorNode;
import flying_object.FlyingGroup;

public class Layer implements Painter<Object>, MouseListener,
		MouseMotionListener, KeyListener {

	public static JXMapViewer mapViewer = null;
	public static DeviceList nodeList = null;
	public static MarkerList markerList = null;
	public static BuildingList buildingList = null;
	public static boolean afficherIndicateur = false;
	public static double x = 0;
	public static double y = 0;
	public static char lastKey = 0;
	public static int lastKeyCode = 0;
	public static boolean dessinerCadre = false;
	public static int cadreX1 = 0;
	public static int cadreY1 = 0;
	public static int cadreX2 = 0;
	public static int cadreY2 = 0;
	public static int selectType = 0;
	public static boolean altDown = false;
	public static boolean shiftDown = false;
	public static boolean cmdDown = false;
	public static boolean ctrlDown = false;
	public static boolean mousePressed = false;
	public static String projectPath = "";
	public static String projectName = "";

	private boolean debutSelection = false;

	public static boolean displayRLDistance = false; // RadioLink distance
	
	public Layer() {
	}

	public Layer(JXMapViewer mapViewer) {
		Layer.mapViewer = mapViewer;
		buildingList = new BuildingList();
		markerList = new MarkerList();
		nodeList = new DeviceList();
		
		//trackingPointsList = new TrackingPointsList();
		//streetGraph = new StreetGraph();
		
		/*
		 * noeuds.add(new Sensor(47.71403518643726,-3.4284210205078125, 0,
		 * 1000)); noeuds.add(new
		 * BaseStation(47.705256511726034,-3.3805274963378906, 0, 500));
		 * noeuds.add(new Router(47.71726906227617,-3.3673095703125, 0, 1000));
		 * noeuds.add(new Sensor(47.709299506131345,-3.3626747131347656, 0,
		 * 1000)); noeuds.add(new Sensor(47.724313596879625,-3.3518600463867188,
		 * 0, 600)); noeuds.add(new Sensor(47.7263921299974,-3.3664512634277344,
		 * 0, 500)); noeuds.add(new
		 * Mobile(47.69127688626756,-3.3623313903808594, 100)); noeuds.add(new
		 * Gas(47.70075109139772,-3.420696258544922,40)); noeuds.add(new
		 * Router(47.715536653750945,-3.412628173828125, 0, 1000));
		 * noeuds.add(new Sensor(47.71345768748889,-3.395977020263672, 0,
		 * 1000)); noeuds.add(new
		 * FlyingObject(47.693703371072026,-3.376750946044922,1000));
		 */

		mapViewer.setOverlayPainter(this);
		mapViewer.addMouseListener(this);
		mapViewer.addMouseMotionListener(this);
		mapViewer.addKeyListener(this);

	}

	public boolean isDebutSelection() {
		return debutSelection;
	}

	public void setDebutSelection(boolean debutSelection) {
		this.debutSelection = debutSelection;
	}

	public static JXMapViewer getMapViewer() {
		return mapViewer;
	}

	
	
	@Override
	public void paint(Graphics2D g, Object arg1, int arg2, int arg3) {
		g.setFont(new Font("arial", 0, 12));
		Rectangle rect = mapViewer.getViewportBounds();
		g.translate(-rect.x, -rect.y);		
		
		if (afficherIndicateur) {
			g.setColor(UColor.RED);
			if (lastKey == '1') {
				g.drawString("   Sensor", (float) x, (float) y);
			}
			if (lastKey == '2') {
				g.drawString("   Gas", (float) x, (float) y);
			}
			if (lastKey == '3') {
				g.drawString("   Flying Object", (float) x, (float) y);
			}
			if (lastKey == '4') {
				g.drawString("   Media Sensor", (float) x, (float) y);
			}
			if (lastKey == '5') {
				g.drawString("   Base Station", (float) x, (float) y);
			}
			if (lastKey == '6') {
				g.drawString("   Mobile", (float) x, (float) y);
			}
			if (lastKey == '7') {
				g.drawString("   Mobile With Radio", (float) x, (float) y);
			}
			if (lastKey == '8') {
				g.drawString("   Marker", (float) x, (float) y);
			}
			if (lastKey == '9') {
				g.drawString("   Vertex", (float) x, (float) y);
			}
			g.drawOval((int) (x - 8), (int) (y - 8), 16, 16);

			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, 75, 30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, 165, 30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, -75, -30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, -15, 30);
		}

		if (!OsmOverpass.isLoading) buildingList.draw(g);
		markerList.draw(g);
		//trackingPointsList.draw(g);
		//streetGraph.dessiner(g);
		nodeList.draw(g);
		nodeList.drawEnvelopeList(g);
		

		if (dessinerCadre) {
			Point2D p1 = MapCalc.pixelPanelToPixelMap(cadreX1, cadreY1);
			Point2D p2 = MapCalc.pixelPanelToPixelMap(cadreX2, cadreY2);
			g.setColor(UColor.WHITE_LLTRANSPARENT);
			g.fillRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
			g.setColor(UColor.WHITE_LTRANSPARENT);
			g.drawRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
		}

		// OSM Test BEGIN
		// This part is used just to understand how to add shapes on the OSM map
		// The class OsmTest is required
		// OsmTest osm = new OsmTest() ;
		// osm.drawFromGPS(g);
		// OSM Test END

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
		// Device dev1 = DeviceList.getNodes().get(0);
		// TrackerWS.clean();
		// TrackerWS.create(dev1.getNodeIdName(), dev1.getX(), dev1.getY());
		// TrackingPointsList.simulate();
	}

	public void simulateSensors() {
		nodeList.simulateSensors();
	}

	public void simulateMobiles() {
		nodeList.simulateMobiles();
	}

	public void addDeviceAction(String s) {
		if(DeviceList.size()>0) {
			AddDevice action = new AddDevice(DeviceList.getNodes().get(DeviceList.size()-1), s);
			action.exec();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg) {
		if (arg.getClickCount() == 2) {						
			Point p = new Point(arg.getX(), arg.getY());
			GeoPosition gp = mapViewer.convertPointToGeoPosition(p);
			CupCarbonMap.getMap().setCenterPosition(new GeoPosition(gp.getLatitude(), gp.getLongitude()));
		} else if (afficherIndicateur) {
			Point p = new Point(arg.getX(), arg.getY());
			GeoPosition gp = mapViewer.convertPointToGeoPosition(p);
			
			if (lastKey == '1') {
				DeviceList.add(new StdSensorNode(gp.getLatitude(), gp.getLongitude(),
						0, 100, 20, -1));
				mapViewer.repaint();
				/* Tanguy */
				addDeviceAction("SensorNode");
				/* ------ */
			}
			if (lastKey == '2') {
				DeviceList
						.add(new Gas(gp.getLatitude(), gp.getLongitude(), 10, -1));
				mapViewer.repaint();
				/* Tanguy */
				addDeviceAction("Gas");
				/* ------ */
			}
			if (lastKey == '4') {
				DeviceList.add(new MediaSensorNode(gp.getLatitude(), gp.getLongitude(),
						0, 100, 60, -1, 0.1, 0, 12));
				mapViewer.repaint();
				/* Tanguy */
				addDeviceAction("MediaSensorNode");
				/* ------ */
			}
			if (lastKey == '3') {
				DeviceList.add(new FlyingGroup(gp.getLatitude(), gp
						.getLongitude(), 10, 30));
				mapViewer.repaint();
				addDeviceAction("FlyingGroup");
			}
			if (lastKey == '5') {
				DeviceList.add(new BaseStation(gp.getLatitude(), gp.getLongitude(),
						0, 100, 20, -1));
				//DeviceList.add(new BaseStation(gp.getLatitude(), gp
				//		.getLongitude(), 0, 100, -1));
				mapViewer.repaint();
				addDeviceAction("BaseStation");
			}
			if (lastKey == '6') {
				DeviceList.add(new Mobile(gp.getLatitude(), gp.getLongitude(),
						10, -1));
				mapViewer.repaint();
				addDeviceAction("Mobile");
			}
			if (lastKey == '7') {
				DeviceList.add(new MobileWithRadio(gp.getLatitude(), gp
						.getLongitude(), 10, 100, -1));
				mapViewer.repaint();
				addDeviceAction("MobileWithRadio");
			}
			if (lastKey == '8') {
				MarkerList.add(new Marker(gp.getLatitude(), gp.getLongitude(),
						10));
				mapViewer.repaint();
				addMarkerAction("Marker");
			}
		}
		
		CupCarbon.updateInfos();
	}

	// public static void addNode(Device node) {
	// DeviceList.add(node);
	// mapViewer.repaint();
	// }

	// public static void addMarker(Marker marker) {
	// MarkerList.add(marker);
	// mapViewer.repaint();
	// }

	public static void addMarker(int index, Marker marker) {
		markerList.add(index, marker);
		mapViewer.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg) {
		mousePressed = true;
		if (shiftDown) {
			mapViewer.setPanEnabled(false);
			debutSelection = true;
			cadreX1 = arg.getX();
			cadreY1 = arg.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg) {
		if (shiftDown && mousePressed) {
			mousePressed = false;
			lastKeyCode = 0;
			mapViewer.setPanEnabled(true);
			debutSelection = false;
			dessinerCadre = false;

			nodeList.selectInNodeSelection(cadreX1, cadreX2, cadreY1, cadreY2);
			markerList.selectInNodeSelection(cadreX1, cadreX2, cadreY1, cadreY2);
			//buildingList.selectNodeInSelection(cadreX1, cadreY1, cadreX2, cadreY2);
			
			mapViewer.repaint();
		}
	}

	public static boolean inMultipleSelection(double x, double y, int cadreX1,
			int cadreY1, int cadreX2, int cadreY2) {
		GeoPosition gp1 = Layer.getMapViewer().convertPointToGeoPosition(
				new Point(cadreX1, cadreY1));
		GeoPosition gp2 = Layer.getMapViewer().convertPointToGeoPosition(
				new Point(cadreX2, cadreY2));
		return x < gp1.getLatitude() && y > gp1.getLongitude()
				&& x > gp2.getLatitude() && y < gp2.getLongitude();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		
		//System.out.println(key.getKeyCode());
		
		if (key.isShiftDown()) {
			shiftDown = true;
		}
		
		if (key.isAltDown())
			altDown = true;
		lastKeyCode = key.getKeyCode();
		
		if (lastKeyCode == 27) {
			lastKey = 0;
			afficherIndicateur = false;
			//streetGraph.init();
			mapViewer.repaint();
		}
		
		if (key.isControlDown()) {
			ctrlDown = true;
		}
		
		if (key.isMetaDown()) {
			cmdDown = true;
		}
		
		if (key.isShiftDown() && key.isControlDown()) {
			shiftDown = true;
			ctrlDown = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		altDown = false;
		shiftDown = false;
		ctrlDown = false;
		cmdDown = false;
	}

	@Override
	public void keyTyped(KeyEvent key) {
			
		lastKey = key.getKeyChar();
		afficherIndicateur = false;
		if (lastKey == '1' || lastKey == '2' || lastKey == '3'
				|| lastKey == '4' || lastKey == '5' || lastKey == '6'
				|| lastKey == '7' || lastKey == '8' || lastKey == '9') {
			afficherIndicateur = true;
			mapViewer.repaint();
		}

		if (lastKey == 'v') {
			nodeList.setDrawLinks(!nodeList.getDrawLinks());
			markerList.setLinks(!markerList.getLinks());
		}

		if (lastKey == 'A') {
			markerList.setArrows(!markerList.getArrows());
		}

		if (lastKey == 'w') {
			if (selectType++ == 10)
				selectType = 0;
		}
		
		if (lastKey == 'o') {
			int n = MarkerList.size();
        	Building building = new Building(n);
        	
        	for (int i=0; i<n; i++){
        		building.set(MarkerList.get(i).getLongitude(), MarkerList.get(i).getLatitude(), i);
        	}
        	
        	Layer.getMapViewer().addMouseListener(building);
    		Layer.getMapViewer().addKeyListener(building);
        	BuildingList.add(building);
	        Layer.mapViewer.repaint();
		}
		
//		if (lastKey == 't' || lastKey == 'o') {
//			int k = 0;
//			StreetVertex sv1 = null;
//			StreetVertex sv2 = null;
//			for (StreetVertex sv : streetGraph.getGraph()) {
//				if (sv.isSelected()) {
//					if (k == 0)
//						sv1 = sv;
//					if (k == 1)
//						sv2 = sv;
//					k++;
//				}
//			}
//			if (k == 2) {
//				if (sv1.hasNeighbor(sv2)) {
//					sv1.remove(sv2);
//				} else {
//					sv1.add(sv2);
//				}
//			}
//		}

//		if (lastKey == 'y' || lastKey == 'o') {
//			int k = 0;
//			StreetVertex sv1 = null;
//			StreetVertex sv2 = null;
//			for (StreetVertex sv : streetGraph.getGraph()) {
//				if (sv.isSelected()) {
//					if (k == 0)
//						sv1 = sv;
//					if (k == 1)
//						sv2 = sv;
//					k++;
//				}
//			}
//			if (k == 2) {
//				if (sv2.hasNeighbor(sv1)) {
//					sv2.remove(sv1);
//				} else {
//					sv2.add(sv1);
//				}
//			}
//		}

		if (lastKey == '>') {
			Device.moveSpeed += 5;
			CupCarbon.updateInfos();
		}

		if (lastKey == '<') {
			Device.moveSpeed -= 5;
			if (Device.moveSpeed < 0)
				Device.moveSpeed = 0;
			CupCarbon.updateInfos();
		}

		if (lastKeyCode == 8) {
			nodeList.deleteIfSelected();
			markerList.deleteIfSelected();
			CupCarbon.updateInfos();
			mapViewer.repaint();
		}
		
		if(lastKey == 'f') {
			DeviceList.selectOneFromSelected();
		}
		
		if (key.getKeyChar() == 'x') {			
			displayRLDistance = !displayRLDistance;
			mapViewer.repaint();
		}
		
		/* Tanguy */
		/*if (lastKey == 'z' && ctrlDown) {
			System.out.println("yes");
			Historic.undo();
		}
		
		if (lastKey == 'y' && ctrlDown) {
			Historic.redo();
		}*/
		/* ------ */
	}

	@Override
	public void mouseDragged(MouseEvent arg) {
		//if (lastKeyCode == 16) {
		if (shiftDown) {
			cadreX2 = arg.getX();
			cadreY2 = arg.getY();
			dessinerCadre = true;
			mapViewer.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Point2D pd = MapCalc.pixelPanelToPixelMap(me.getX(), me.getY());
		x = pd.getX();
		y = pd.getY();
		if (afficherIndicateur) {
			mapViewer.repaint();
		}
	}

	public void addNodeInMap(char c) {
		lastKey = c;
		afficherIndicateur = true;
		mapViewer.repaint();
	}

	public static void sensorParametersInit() {
//		DeviceParametersWindow.textField_5.setText("");
//		DeviceParametersWindow.textField_6.setText("");
//		DeviceParametersWindow.textField_7.setText("");
//		DeviceParametersWindow.textField_8.setText("");
//		DeviceParametersWindow.textField_9.setText("");
	}

	public void loadCityNodes() {
		NetworkLoader nl = new NetworkLoader(mapViewer);
		nl.start();
	}

	public void loadCityNodes2() {
		try {
			double x = 0;
			double y = 0;
			// FileInputStream fis = new FileInputStream("santander.txt");
			FileInputStream fis = new FileInputStream("dubai.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String s;
			String[] ps;
			String[] ics;
			// for (int i = 0; i < 1000; i++) {
			// s = br.readLine();
			int k = 0;
			while (((s = br.readLine()) != null) && (k++ < 72)) {
				String[][] info = new String[7][2];
				info[0][0] = "Sensor Type : ";
				info[1][0] = "";
				info[2][0] = "";
				info[3][0] = "Battery : ";
				info[4][0] = "Name : ";
				info[5][0] = "Date : ";
				info[6][0] = "Time : ";
				info[0][1] = "";
				info[1][1] = "";
				info[2][1] = "";
				info[3][1] = "";
				info[4][1] = "";
				info[5][1] = "";
				info[6][1] = "";
				s = s.substring(1, s.length() - 1);
				ps = s.split(",");
				for (String cs : ps) {
					if (cs.endsWith("}"))
						cs = cs.substring(0, cs.length() - 1);
					if (cs.startsWith("\"values\":{\""))
						cs = cs.substring(10);
					ics = cs.split(":");
					ics[0] = ics[0].substring(1, ics[0].length() - 1);
					ics[1] = ics[1].substring(1, ics[1].length() - 1);
					if (ics[0].equals("Lattitude"))
						x = Double.parseDouble(ics[1]);
					if (ics[0].equals("Longitude"))
						y = Double.parseDouble(ics[1]);
					if (ics[0].equals("Sensor_Type"))
						info[0][1] = ics[1];
					if (ics[0].equals("Temperature")) {
						info[1][0] = "Temperature : ";
						info[1][1] = ics[1] + " °C";
					}

					if (ics[0].equals("Co_Index")) {
						info[2][0] = "CO Index : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Light")) {
						info[2][0] = "Light : ";
						info[2][1] = ics[1] + " lux";
					}
					if (ics[0].equals("Noise")) {
						info[2][0] = "Noise : ";
						info[2][1] = ics[1] + " dB";
					}
					if (ics[0].equals("State")) {
						info[2][0] = "State : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Battery"))
						info[3][1] = ics[1] + " %";
					if (ics[0].equals("Node"))
						info[4][1] = ics[1];
					if (ics[0].equals("Date"))
						info[5][1] = ics[1];
					if (ics[0].equals("Time"))
						info[6][1] = ics[1];
				}
				DeviceList.add(new StdSensorNode(x, y, 0, 30, 10, info, -1));
				// MarkerList.add(new Marker(x,y,10));
				mapViewer.repaint();
			}
			br.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/*public void loadNodes() {
		String fileName = "nodes.dat";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String[] stab = br.readLine().split(" ");
			int sensorNumber = Integer.parseInt(stab[0]);
			int targetNumber = Integer.parseInt(stab[1]);
			double captuRadius = Double.parseDouble(stab[2]);
			double radioRadius = Double.parseDouble(stab[3]);
			double v1;
			double v2;
			double x;
			double y;

			Point p;

			for (int i = 0; i < sensorNumber; i++) {
				stab = br.readLine().split(" ");
				v1 = Double.parseDouble(stab[0]);
				// System.out.print(v1+" ");
				v2 = Double.parseDouble(stab[1]);
				// System.out.println(v2);
				p = new Point((int) v1, (int) v2);
				x = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLatitude();
				y = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLongitude();
				DeviceList.add(new SensorNode(x, y, 0, radioRadius, captuRadius, -1));

			}
			// System.out.println("-------------");
			for (int i = 0; i < targetNumber; i++) {
				stab = br.readLine().split(" ");
				v1 = Double.parseDouble(stab[0]);
				// System.out.print(v1+" ");
				v2 = Double.parseDouble(stab[1]);
				// System.out.println(v2);
				p = new Point((int) v1, (int) v2);
				x = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLatitude();
				y = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLongitude();
				DeviceList.add(new MobileWithRadio(x, y, 10, 0, -1));
			}
			// System.out.println("-------------");
			stab = br.readLine().split(" ");
			v1 = Double.parseDouble(stab[0]);
			// System.out.print(v1+" ");
			v2 = Double.parseDouble(stab[1]);
			// System.out.println(v2);
			p = new Point((int) v1, (int) v2);
			x = Layer.getMapViewer().convertPointToGeoPosition(p).getLatitude();
			y = Layer.getMapViewer().convertPointToGeoPosition(p)
					.getLongitude();
			DeviceList.add(new BaseStation(x, y, 0, 150, -1));
			// System.out.println("-------------");

			mapViewer.repaint();

			// System.out.println(sensorNumber);
			// System.out.println(targetNumber);
			// System.out.println(radius);
			// System.out.println(radioRadius);
			br.close();
			// System.out.println("-------------");
			// Thread th = new Thread(this);
			// th.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void setSelectionOfAllNodes(boolean selection, int type,
			boolean addSelection) {
		nodeList.setSelectionOfAllNodes(selection, type, addSelection);
	}

	public void invertSelection() {
		nodeList.invertSelection();
		markerList.invertSelection();
	}

	public void setSelectionOfAllMarkers(boolean selection, int type,
			boolean addSelection) {
		markerList.setSelectionOfAllMarkers(selection, type, addSelection);
	}	
	
	public static void initClick() {
		//*****BEGIN DEMO****
		lastKey = 0;
		afficherIndicateur = false;
		mapViewer.repaint();
		//*****END DEMO****
	}
	
	public void addMarkerAction(String s) {
		if(MarkerList.getMarkers().get(MarkerList.size()-1).getIdm()==0) {
			MarkerList.getMarkers().get(MarkerList.size()-1).setIdm();
		}
		if(MarkerList.size()>0) {
			AddMarker action = new AddMarker(MarkerList.getMarkers().get(MarkerList.size()-1),s);
			action.exec();
		}
	}

}
