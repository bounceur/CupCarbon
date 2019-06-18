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

package markers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import action.CupAction;
import action.CupActionAddSensor;
import action.CupActionBlock;
import action.CupActionDeleteMarker;
import action.CupActionInsertMarker;
import action.CupActionRouteFromMarkers;
import action.CupActionStack;
import cupcarbon.CupCarbon;
import cupcarbon.CupCarbonVersion;
import device.DeviceList;
import device.StdSensorNode;
import map.NetworkParameters;
import map.MapLayer;
import project.Project;
import solver.SolverProxyParams;
import utilities.MapCalc;
import utilities.UColor;

public class MarkerList {

	public static LinkedList<Marker> markers;
	public static int totalDistance;
	public static int totalDuration;

	public MarkerList() {
		markers = new LinkedList<Marker>();
	}
	
	public static void reset() {
		if(markers != null) {
			markers = new LinkedList<Marker>();
		}
	}

	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			Marker marker;
			fos.println("# CupCarbon v."+CupCarbonVersion.VERSION+" ("+CupCarbonVersion.YEAR+")");
			fos.println("# Markers");
			fos.println("# -----------------------");
			fos.println("# -----------------------");
			fos.println("# -----------------------");
			for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
				marker = iterator.next();
				fos.print("00:00:00");
				fos.print(" " + marker.getLongitude());
				fos.print(" " + marker.getLatitude());
				fos.print(" " + marker.getElevation());
				fos.print(" " + marker.getRadius());
				fos.println();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void open(String fileName) {
		//Routes.reset();
		reset();		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str;
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				addNodeByType(str[1], str[2], str[3], str[4]);
			}
			br.close();
			MapLayer.repaint();
		} 
		catch (FileNotFoundException e) {
			System.out.println("[MarkerList] No Markers!");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addNodeByType(String... type) {
		add(new Marker(type[0], type[1], type[2], type[3]));
	}

	public static void add(Marker marker) {
		markers.add(marker);
	}

	public static void add(int index, Marker marker) {
		markers.add(index, marker);
	}

	/**
	 * Draw the markers (in red)
	 * 
	 * @param g
	 *            Graphical object
	 */
	public void draw(Graphics g2) {
		totalDistance = 0;
		totalDuration = 0;
		Graphics2D g = (Graphics2D) g2;
		try {
			double x1 = 0;
			double y1 = 0;
			double x2 = 0;
			double y2 = 0;
			double dx = 0;
			double dy = 0;
			double alpha = 0;
			int lx1 = 0;
			int ly1 = 0;
			int lx2 = 0;
			int ly2 = 0;
			int[] coord ;

			if (markers.size() > 0) {
				boolean first = true;
				for (Marker marker : markers) {
					if(marker.isInside() && marker.isSelected())
						MapLayer.numberOfInsideAndSelected++;
					if(marker.isSelected() || marker.isInside())
						MapLayer.mapViewer.setPanEnabled(false);
					
					// Draw the marker
					marker.draw(g);
					
					if (first) {
						first = false;
						x1 = marker.getLongitude();
						y1 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];			
						g.setColor(UColor.BLUE3);
						g.fillOval((int) lx1 - 5, (int) ly1 - 5, (int) 10, (int) 10);
						g.setColor(UColor.BLUE1);
						g.drawOval((int) lx1 - 5, (int) ly1 - 5, (int) 10, (int) 10);
					} else {
						x2 = marker.getLongitude();
						y2 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y2, x2);
						lx2 = coord[0];
						ly2 = coord[1];

						g.setStroke(new BasicStroke(3f));
						
						g.setColor(UColor.BLUE2);
						if(NetworkParameters.drawMarkerArrows)
							g.setColor(UColor.BLUE4);
						if(MapLayer.dark && NetworkParameters.drawMarkerArrows) {
							g.setColor(UColor.BLUE3);
						}
						// Draw the link between markers
						g.drawLine((int) lx1, (int) ly1, (int) lx2, (int) ly2);
												
						// Draw arrows
						if(NetworkParameters.drawMarkerArrows) {
							g.setStroke(new BasicStroke(0.8f));
							g.setColor(UColor.BLUE1);
							g.drawLine((int) lx1, (int) ly1, (int) lx2, (int) ly2);
							dx = lx2 - lx1;
							dy = ly2 - ly1;
							alpha = Math.atan(dy / dx);
							alpha = 180 * alpha / Math.PI;
							if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
								g.fillArc((int) lx2 - 12, (int) ly2 - 12, 12*2, 12*2,180 - (int) alpha - 12, 12*2);
							else
								g.fillArc((int) lx2 - 12, (int) ly2 - 12, 12*2, 12*2, -(int) alpha - 12, 12*2);		
						}
						if (NetworkParameters.displayMarkerDistance) {
							MapLayer.drawDistance(x1, y1, 0, x2, y2, 0, g);
						}
						totalDistance+=(int)  (int) MapLayer.distance(x1, y1, x2, y2);
						totalDuration++;						
						x1 = marker.getLongitude();
						y1 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];	
						
						
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static Marker get(int idx) {
		return markers.get(idx);
	}

	public static int getIndex(Marker marker) {
		for (int i = 0; i < markers.size(); i++) {
			if (markers.get(i) == marker)
				return i;
		}
		return -1;
	}
	
	public static int size() {
		return markers.size();
	}

	public static void delete(int idx) {
		markers.remove(idx);
	}

	public static void saveGpsCoords(String fileName, String title, String from, String to, boolean loop, int delay, int nLoop) {
		try {
			PrintStream ps;
			ps = new PrintStream(new FileOutputStream(Project.getGpsFileFromName(fileName)));
			ps.println(title);
			ps.println(from);
			ps.println(to);
			ps.println(loop);
			ps.println(nLoop);
			Marker marker;

			boolean first = true;
			double lo = 0;
			double la = 0;
			double el = 0;
			double ra = 0;
			for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
				marker = iterator.next();
				if(first) {
					first = false;
					lo = marker.getLongitude() ;
					la = marker.getLatitude() ;
					el = marker.getElevation() ;
					ra = marker.getRadius();
				}
				ps.println(1 + " " + marker.getLongitude() + " " + marker.getLatitude() + " " + marker.getElevation() + " " + marker.getRadius());				
			}
			if(loop) {
				ps.println(delay + " " + lo + " " + la + " " + el + " " + ra);
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void selectInsideRectangle(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
		boolean selection = false;
		Marker marker;
		for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			marker.setSelected(false);
			if (MapLayer.insideSelection(marker.getLongitude(), marker.getLatitude(), cadreX1, cadreY1, cadreX2, cadreY2)) {
				marker.setSelected(true);
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
		Marker marker;
		int index = 0;
		for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			if (marker.isSelected()) {
				CupActionDeleteMarker action = new CupActionDeleteMarker(marker, index);
				block.addAction(action);
			}
			index++;
		}
		if(block.size()>0) {
			CupActionStack.add(block);
			CupActionStack.execute();
		}
	}

	public static void deleteAll() {
		markers.removeAll(markers);
	}

	public void setSelectionOfAllMarkers(boolean selection, boolean addSelect) {
		for (Marker marker : markers) {
			if (!addSelect)
				marker.setSelected(false);
			marker.setSelected(selection);
		}
		MapLayer.repaint();
	}

	public void invertSelection() {
		for (Marker mar : markers) {
			mar.invSelection();
		}
		MapLayer.repaint();
	}

	public static void generateOSMRouteFile() {
		String host = "https://graphhopper.com/api/1/route?";
		int n1 = 0;
		int n2 = markers.size();
		
		if(n2>2) 
			n1 = n2-2;
		for (int i=n1; i<n2; i++) {
			host += "point=" + markers.get(i).getLatitude() + "," + markers.get(i).getLongitude() + "&";
		}		
		host += "&vehicle=car&locale=de&key=83a9ff19-9dc6-44ff-9f04-70a0500ad0ef&type=gpx";

		try {
			File f = new File("gpx");
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
			URL url = new URL(host);
			URLConnection uc = null;
			if(SolverProxyParams.host.equals("") && SolverProxyParams.port.equals("")) {
				uc = url.openConnection();
			}
			else {
				SocketAddress sa = new InetSocketAddress(SolverProxyParams.host, Integer.parseInt(SolverProxyParams.port));
				Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);			
				uc = url.openConnection(proxy);
			}
			
			uc.setRequestProperty("User-Agent", "CupCarbon");

			InputStream in = uc.getInputStream();
			FileOutputStream file = new FileOutputStream("gpx/tmp.gpx");
			int l = 0;
			while ((l = in.read()) != -1) {
				file.write(l);
			}
			in.close();
			file.close();
			gpxToMarkers();
		} catch (MalformedURLException e) {
			System.err.println("[CUPCARBON: MarkerList/generateOSMRouteFile] "+host + " : URL problem.");
		} catch (IOException e) {
			System.err.println("[CUPCARBON: MarkerList/generateOSMRouteFile] "+"------ Connexion problem ! ------");
		}
	}

	public static void gpxToMarkers() {
		CupActionBlock block = new CupActionBlock();
		
		LinkedList<Marker> nMarkers = new LinkedList<Marker>(markers);
		nMarkers.removeLast();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("gpx/tmp.gpx"));
			while(!br.readLine().startsWith("<trkpt")) {}
				
			String[] st ;
			String s ;
			while((s=br.readLine()).startsWith("<trkpt")) {
				st = s.split("\"");
				nMarkers.add(new Marker(Double.valueOf(st[3]), Double.valueOf(st[1]), 0, 5));
			}
			br.close();
			File f = new File("gpx/tmp.gpx");
			f.delete();
			f = new File("gpx");
			f.delete();
			
			CupAction action = new CupActionRouteFromMarkers(markers, nMarkers);
			block.addAction(action);
			CupActionStack.add(block);
			CupActionStack.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertMarkers() {
		ArrayList<Integer> iList = new ArrayList<Integer>();
		for (int i = 0; i < markers.size()-1; i++) {
			if(markers.get(i).isSelected()) {
				iList.add(i);
			}
		}
		if(iList.size()>0) {
			CupAction action = new CupActionInsertMarker(iList);		
			CupActionBlock block = new CupActionBlock();
			block.addAction(action);
			CupActionStack.add(block);
			CupActionStack.execute();
		}
	}
	
	public static void selectNextMarkers() {
		for(int i=0; i<markers.size(); i++) {
			if(markers.get(i).isSelected() && !markers.get(i+1).isSelected()) {
				markers.get(i+1).setSelected(true);
				break;
			}
		}
	}
	
	public static void insertMarkers2() {
		int n = markers.size()-1;
		for (int i = 0; i < n; i++) {
			if(markers.get(i*2+1).isSelected())
				MapLayer.addMarker(i*2+1,Marker.getCentre(markers.get(i*2),MarkerList.get(i*2+1),true));
		}
	}
		
	public static void transformMarkersToSensors() {
		if(markers.size()>0) {
			CupActionBlock block = new CupActionBlock();		
			for (int i = 0; i < markers.size(); i++) {
				Marker marker = markers.get(i);
				if(marker.isSelected()) {
					CupAction action = new CupActionAddSensor(new StdSensorNode(marker.getLongitude(), marker.getLatitude(), marker.getElevation(), 0, 100, 20, -1));
					block.addAction(action);
				}
			}
			if(block.size()>0) {
				CupActionStack.add(block);
				CupActionStack.execute();
			}
		}
	}
	
	public static void deselectAll() {
		for(Marker marker : markers) {
			marker.setSelected(false);
		}
	}
	
	public static void selectAll() {
		for(Marker marker : markers) {
			marker.setSelected(true);
		}
	}
	
	public static void delete(Marker marker) {
		Marker tMarker;
		for (Iterator<Marker> iterator = MarkerList.markers.iterator(); iterator.hasNext();) {
			tMarker = iterator.next();
			if (tMarker == marker) {
				iterator.remove();				
			}
		}
		if(DeviceList.propagationsCalculated)			
			DeviceList.calculatePropagations();
	}
	
}
