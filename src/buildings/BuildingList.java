package buildings;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;

import action.CupActionBlock;
import action.CupActionDeleteBuilding;
import action.CupActionStack;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import map.MapLayer;
import markers.Marker;
import markers.MarkerList;
import overpass.OsmOverpass;
import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class BuildingList {

	public static LinkedList<Building> buildings = null;
	public static boolean isLoading = false;
	public static boolean locked = false ;
	
	public BuildingList() {
		buildings = new LinkedList<Building>();
	}
	
	public static void add(String str) {
		Building building = new Building(str);
		MapLayer.mapViewer.addMouseListener(building);
		MapLayer.mapViewer.addKeyListener(building);
		buildings.add(building);
	}
	
	public static void add(String [] str) {		
		for(int i=0; i<str.length; i++) {
			buildings.add(new Building(str));
		}
	}
	
	public static void add(Building building) {
		buildings.add(building);
	}
	
	public void draw(Graphics g) {
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).draw(g);
		}
	}
	
	public static void init() {
		if(buildings != null) {
			for(Building building : buildings) {
				MapLayer.mapViewer.removeMouseListener(building);
				MapLayer.mapViewer.removeKeyListener(building);
				building = null;
			}
			buildings = new LinkedList<Building>();
		}
	}
	
	public static void loadFromOsm() {
		if(MarkerList.markers.size()==2) {
			Marker marker1 = MarkerList.markers.get(0);
			Marker marker2 = MarkerList.markers.get(1);
			double m1x = marker1.getLongitude();
			double m1y = marker1.getLatitude();
			double m2x = marker2.getLongitude();
			double m2y = marker2.getLatitude();
			OsmOverpass ovp = new OsmOverpass(m1x, m1y, m2x, m2y);
			ovp.load();
		}
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Markers");
					alert.setHeaderText(null);
					alert.setContentText("The number of markers must be 2!");
					alert.showAndWait();
				}
			});
		}
	}

	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			fos.print("# CupCarbon\n");
			fos.print("# Buildings\n");
			fos.print("# -----------------------\n");
			fos.print("# -----------------------\n");
			for (Building building : buildings) {
				for(int i=0; i<building.getNPoints(); i++) {
					fos.print(building.getXCoords(i)+" ");
					fos.print(building.getYCoords(i)+" ");					
				}
				fos.println();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void open(String fileName) {
				init();
				try {
					BufferedReader br = new BufferedReader(new FileReader(fileName));
					String line;
					line = br.readLine();
					line = br.readLine();
					line = br.readLine();
					line = br.readLine();
					while ((line = br.readLine()) != null) {
						BuildingList.add(line);						
					}
					br.close();
					MapLayer.repaint();
				} catch (FileNotFoundException e) {
					System.out.println("[BuildingList] No buildings.");
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
	public void deleteIfSelected() {
		if(!locked) {
			CupActionBlock block = new CupActionBlock();
			Building building;
			for (Iterator<Building> iterator = buildings.iterator(); iterator.hasNext();) {
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
	}
	
	public static void delete(Building building) {
		if(!locked) {
			buildings.remove(building);
		}
	}
	
	public static boolean intersect(Polygon p) {
		for (int i=0; i<buildings.size(); i++) {	
			if (buildings.get(i).intersect(p))
				return true;
		}
		return false;
	}
	
	public static int size() {
		return buildings.size();
	}
	
	public void selectInsideRectangle(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {		
		Point2D p1 = MapCalc.pixelPanelToPixelMap(cadreX1, cadreY1);
		Point2D p2 = MapCalc.pixelPanelToPixelMap(cadreX2, cadreY2);
		Building b = new Building(4);
		b.setInt((int)p1.getX(), (int)p1.getY(), 0);
		b.setInt((int)p2.getX(), (int)p1.getY(), 1);
		b.setInt((int)p2.getX(), (int)p2.getY(), 2);
		b.setInt((int)p1.getX(), (int)p2.getY(), 3);
		Rectangle rec = new Rectangle((int)p1.getX(), (int)p1.getY(), (int)(p2.getX()-p1.getX()), (int)(p2.getY()-p1.getY()));
		
		for (Building building : buildings) {			
			building.setSelection(false);
			if(!building.isHide() && building.getPoly().intersects(rec))
				building.setSelection(true);
		}
	}
	
	public void setSelectionOfAllBuildings(boolean selection, boolean addSelect) {		
		for (Building building : buildings) {
			if (!addSelect)
				building.setSelection(false);
			building.setSelection(selection);
		}
		MapLayer.repaint();
	}
	
	public static void deselectAll() {
		for(Building building : buildings) {
			building.setSelection(false);
		}
	}
	
	public static void showHideBuildings() {
		for(Building building : buildings) {
			building.setHide(!building.isHide());
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for(Building building : buildings) {
			s += building.toString() + " ";
		}
		return s;
	}
	
}
