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

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import cupcarbon.Version;
import map.MapLayer;
import project.Project;
import utilities.MapCalc;
import utilities.UColor;

public class MarkerList {

	private static List<Marker> markers;
	private static boolean drawLinks = true;
	private static boolean drawArrows = false;

	public MarkerList() {
		markers = new ArrayList<Marker>();
		drawLinks = true;
		drawArrows = false;
	}
	
	public static void reset() {
		for(Marker marker : markers) {
			MapLayer.getMapViewer().removeMouseListener(marker);
			MapLayer.getMapViewer().removeMouseMotionListener(marker);
			MapLayer.getMapViewer().removeKeyListener(marker);
			marker = null;
		}
		markers = new ArrayList<Marker>();
		drawLinks = true;
		drawArrows = false;
	}

	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			Marker marker;
			fos.println("# CupCarbon v."+Version.VERSION+" ("+Version.YEAR+")");
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
			//line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				addNodeByType(str[1], str[2], str[3], str[4]);
			}
			br.close();
			MapLayer.getMapViewer().repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addNodeByType(String... type) {
		add(new Marker(type[0], type[1], type[2], type[3]));
	}

	public static void add(Marker marker) {
		markers.add(marker);
	}

	public void add(int index, Marker marker) {
		markers.add(index, marker);
	}

	/**
	 * Draw the markers (in red)
	 * 
	 * @param g
	 *            Graphical object
	 */
	public void draw(Graphics g) {
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
			for (Marker marker : markers)
				marker.draw(g);
			if (drawLinks && markers.size() > 0) {
				boolean firstTime = true;
				for (Marker marker : markers) {
					if (firstTime) {
						firstTime = false;
						x1 = marker.getLongitude();
						y1 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];								
						//lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
						//ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
						g.setColor(UColor.RED);
						g.drawOval((int) lx1 - 5, (int) ly1 - 5, (int) 10,
								(int) 10);
					} else {
						x2 = marker.getLongitude();
						y2 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y2, x2);
						lx2 = coord[0];
						ly2 = coord[1];
						//lx2 = MapCalc.geoToIntPixelMapX(x2, y2);
						//ly2 = MapCalc.geoToIntPixelMapY(x2, y2);

						g.setColor(UColor.RED);

						// Draw the link between markers
						g.drawLine((int) lx1, (int) ly1, (int) lx2, (int) ly2);
						// Draw arrows
						if(drawArrows) {							
							dx = lx2 - lx1;
							dy = ly2 - ly1;
							alpha = Math.atan(dy / dx);
							alpha = 180 * alpha / Math.PI;
							/*if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
								g.fillArc((int) lx2 - 15, (int) ly2 - 15, 30, 30,
										180 - (int) alpha - 10, 20);
							else
								g.fillArc((int) lx2 - 15, (int) ly2 - 15, 30, 30,
										-(int) alpha - 10, 20);*/
							if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
								g.fillArc((int) lx2 - 12, (int) ly2 - 12, 12*2, 12*2,180 - (int) alpha - 12, 12*2);
							else
								g.fillArc((int) lx2 - 12, (int) ly2 - 12, 12*2, 12*2, -(int) alpha - 12, 12*2);		
						}
						x1 = marker.getLongitude();
						y1 = marker.getLatitude();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];	
						//lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
						//ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
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

	public void setLinks(boolean b) {
		drawLinks = b;
	}

	public boolean getLinks() {
		return drawLinks;
	}
	
	public void setArrows(boolean b) {
		drawArrows = b;
	}

	public boolean getArrows() {
		return drawArrows;
	}

	public static int size() {
		return markers.size();
	}

	public static void delete(int idx) {
		Marker marker = markers.get(idx);
		MapLayer.getMapViewer().removeMouseListener(marker);
		MapLayer.getMapViewer().removeMouseMotionListener(marker);
		MapLayer.getMapViewer().removeKeyListener(marker);
		markers.remove(idx);
		marker = null;
	}

	public void simulate() {
		// for (int i = 0; i < markers.size(); i++) {
		// markers.get(i).start();
		// }
	}

	/*
	 public static void saveGpsCoords(String fileName, String title,
			String from, String to) {
		try {
			PrintStream ps;
			ps = new PrintStream(new FileOutputStream(
					Project.getGpsFileFromName(fileName)));
			ps.println(title);
			ps.println(from);
			ps.println(to);
			Marker marker;

			int initialDate = -3600000;
			Date date = new Date(initialDate);
			String s = "";
			SimpleDateFormat formateur = new SimpleDateFormat("HH:mm:ss");

			for (Iterator<Marker> iterator = markers.iterator(); iterator
					.hasNext();) {
				marker = iterator.next();
				s = formateur.format(date);
				date.setTime(initialDate += 1000);
				ps.println(s + " " + marker.getX() + " " + marker.getY() + " "
						+ marker.getRadius());
			}
			ps.close();
			JOptionPane.showMessageDialog(null, "File saved!", "Save",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	 */
	public static void saveGpsCoords(String fileName, String title, String from, String to, boolean loop, int delay, int nLoop) {
		try {
			PrintStream ps;
			ps = new PrintStream(new FileOutputStream(
					Project.getGpsFileFromName(fileName)));
			ps.println(title);
			ps.println(from);
			ps.println(to);
			ps.println(loop);
			ps.println(nLoop);
			Marker marker;

			//int initialDate = 0;
			//Date date = new Date(initialDate);
			//String s = "";
			int s = 0;
			//SimpleDateFormat formateur = new SimpleDateFormat("HH:mm:ss");

			for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
				marker = iterator.next();
				//s = formateur.format(date);
				//date.setTime(initialDate += 1000);
				if(iterator.hasNext() || !loop)
					s++;
				else {
					s+=delay;
				}
				ps.println(s + " " + marker.getLongitude() + " " + marker.getLatitude() + " " + marker.getElevation() + " "
						+ marker.getRadius());				
			}
			ps.close();
			JOptionPane.showMessageDialog(null, "File saved!", "Save",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void selectInNodeSelection(int cadreX1, int cadreY1, int cadreX2,
			int cadreY2) {
		Marker marker;
		for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			marker.setMove(false);
			marker.setSelection(false);
			if (MapLayer.inMultipleSelection(marker.getLongitude(), marker.getLatitude(),
					cadreX1, cadreX2, cadreY1, cadreY2)) {
				marker.setSelection(true);
			}
		}
	}

	public void deleteIfSelected() {
		Marker marker;
		for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			if (marker.isSelected()) {
				MapLayer.getMapViewer().removeMouseListener(marker);
				MapLayer.getMapViewer().removeMouseMotionListener(marker);
				MapLayer.getMapViewer().removeKeyListener(marker);
				iterator.remove();
				marker = null;
			}
		}
	}

	public static void deleteAll() {
		Marker marker;
		for (Iterator<Marker> iterator = markers.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			MapLayer.getMapViewer().removeMouseListener(marker);
			MapLayer.getMapViewer().removeMouseMotionListener(marker);
			MapLayer.getMapViewer().removeKeyListener(marker);
			iterator.remove();
			marker = null;
		}
	}

	public void setSelectionOfAllMarkers(boolean selection, int type,
			boolean addSelect) {
		for (Marker mar : markers) {
			if (!addSelect)
				mar.setSelection(false);
			if (mar.getType() == type || type == -1)
				mar.setSelection(selection);
		}
		MapLayer.getMapViewer().repaint();
	}

	public void invertSelection() {
		for (Marker mar : markers) {
			mar.invSelection();
		}
		MapLayer.getMapViewer().repaint();
	}

	public static void generateGpxFile() {
		String host = "http://router.project-osrm.org/viaroute?hl=fr&";

		for (Marker m : markers) {

			host += "loc=" + m.getLatitude() + "," + m.getLongitude() + "&";
		}

		host += "output=json";
		System.out.println(host);
		try {
//			File f = new File("gpx");
//			try {
//				f.mkdir();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			URL url = new URL(host);
			URLConnection uc = url.openConnection();			
			
			uc.setRequestProperty("User-Agent", "CupCarbon");
			
			InputStream in = uc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			//FileOutputStream file = new FileOutputStream("gpx/tmp.gpx");
			//int l = 0;
			String line = "";
			//while ((l = in.read()) != -1) {
			while((line=br.readLine()) != null) {
				System.out.print(line);
				//file.write(l);
				//file.flush();
			}
			in.close();
			//file.close();
			gpxToMarkers();
		} catch (MalformedURLException e) {
			System.err.println(host + " : URL problem.");
		} catch (IOException e) {
			System.err.println("------ Connexion problem ! ------");
		}
	}

	public static void gpxToMarkers() {
		deleteAll();
		try {
			BufferedReader br = new BufferedReader(
					//new FileReader("gpx2/route.gpx"));
					new FileReader("gpx/tmp.gpx"));
			//for (int i = 0; i < markers.size(); i++) {

			//}
			String[] s1 = br.readLine().split("lat=\"");
			String[] s2;
			for (int i = 1; i < s1.length; i++) {
				s2 = s1[i].split("\"");
				markers.add(new Marker(Double.valueOf(s2[2]), Double.valueOf(s2[0]), 0, 25));
				MapLayer.getMapViewer().repaint();
			}
			br.close();
			File f = new File("gpx/tmp.gpx");
			f.delete();
			f = new File("gpx");
			f.delete();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * @return the markers
	 */
	public static List<Marker> getMarkers() {
		return markers;
	}
	
	public static void insertMarkers() {
		int n = markers.size()-1;
		for (int ix = 0; ix < n; ix++) {
			//if(markers.get(ix*2).isSelected())
			MapLayer.addMarker(ix*2+1,Marker.getCentre(markers.get(ix*2),MarkerList.get(ix*2+1),true));
		}
	}
	
}
