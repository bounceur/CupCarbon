package geo_objects;

import java.awt.Graphics;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import map.MapLayer;
import markers.Marker;
import markers.MarkerList;
import overpass.OsmOverpass;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class BuildingList extends Thread {

	public static LinkedList<Building> buildingList = null;	
	
	
	public BuildingList() {
		buildingList = new LinkedList<Building>();
	}
	
	public static void add(String str) {
		Building building = new Building(str);
		MapLayer.getMapViewer().addMouseListener(building);
		MapLayer.getMapViewer().addKeyListener(building);
		buildingList.add(building);
	}
	
	public static void add(String [] str) {		
		for(int i=0; i<str.length; i++) {
			buildingList.add(new Building(str));
		}
	}
	
	public static void add(Building building) {
		buildingList.add(building);
	}
	
	public void draw(Graphics g) {
		for(Building building : buildingList) {
			building.draw(g);
		}
	}
	
	public static void init() {
		for(Building building : buildingList) {
			MapLayer.getMapViewer().removeMouseListener(building);
			MapLayer.getMapViewer().removeKeyListener(building);
			building = null;
		}
		buildingList = new LinkedList<Building>();
	}
	
	public static void loadFromOsm() {
		
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		double m1x = marker1.getLongitude();
		double m1y = marker1.getLatitude();
		double m2x = marker2.getLongitude();
		double m2y = marker2.getLatitude();
		OsmOverpass ovp = new OsmOverpass(m1x, m1y, m2x, m2y);
		ovp.start();
	}

	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			fos.print("# CupCarbon\n");
			fos.print("# Buildings\n");
			fos.print("# -----------------------\n");
			fos.print("# -----------------------\n");
			for (Building building : buildingList) {
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
				MapLayer.getMapViewer().repaint();
			}
			br.close();			
		} catch (FileNotFoundException e) {
			System.out.println("No buildings.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delete(Building building) {
		for(Building b : buildingList) {			
			if(b==building) {
				MapLayer.getMapViewer().removeMouseListener(b);
				MapLayer.getMapViewer().removeKeyListener(b);
				buildingList.remove(b);
				break;
			}
		}
	}
	
	public static boolean intersect(Polygon p) {
		for (Building building : buildingList){
			if (building.intersect(p))
				return true;
		}
		return false;
	}
	
	public static int size() {
		return buildingList.size();
	}
}
