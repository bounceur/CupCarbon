package markers;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import device.SensorNode;
import geometry.DPoint;
import javafx.collections.ObservableList;
import map.MapLayer;
import map.NetworkParameters;
import project.Project;
import utilities.Geometry;
import utilities.MapCalc;
import utilities.UColor;

public class Routes {

	public static ArrayList<NamedRoute> routes;

	public static void draw(Graphics2D g) {		
		g.setStroke(new BasicStroke(0.6f));
		g.setColor(UColor.RED);
		for (int i = 0; i < routes.size(); i++) {
			try {
				boolean first = true;
				double x1 = 0;
				double y1 = 0;
				double x2 = 0;
				double y2 = 0;
//				double dx = 0;
//				double dy = 0;
//				double alpha = 0;
				int lx1 = 0;
				int ly1 = 0;
				int lx2 = 0;
				int ly2 = 0;
				int[] coord;
				
				ArrayList<DPoint> route = routes.get(i).getRoute();
				String routeName = routes.get(i).getName();
				for (int j = 0; j < route.size(); j++) {
					
					if (first) {
						first = false;
						x1 = route.get(0).getX();
						y1 = route.get(0).getY();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];
						g.fillOval(lx1-2, ly1-2, 4, 4);
						g.drawString(routeName, lx1-5, ly1-5);
					} else {
						x2 = route.get(j).getX();
						y2 = route.get(j).getY();
						coord = MapCalc.geoToPixelMapA(y2, x2);
						lx2 = coord[0];
						ly2 = coord[1];
						
						g.drawLine((int) lx1, (int) ly1, (int) lx2, (int) ly2);
//						dx = lx2 - lx1;
//						dy = ly2 - ly1;
						g.fillOval(lx2-2, ly2-2, 4, 4);
//						alpha = Math.atan(dy / dx);
//						alpha = 180 * alpha / Math.PI;
//						int sz = 10;
//						if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
//							g.fillArc((int) lx2 - sz, (int) ly2 - sz, sz * 2, sz * 2, 180 - (int) alpha - sz, sz * 2);
//						else
//							g.fillArc((int) lx2 - sz, (int) ly2 - sz, sz * 2, sz * 2, -(int) alpha - sz, sz * 2);

						x1 = route.get(j).getX();
						y1 = route.get(j).getY();
						coord = MapCalc.geoToPixelMapA(y1, x1);
						lx1 = coord[0];
						ly1 = coord[1];
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static void loadRoutes() {
		String path = Project.getProjectGpsPath();
		File root = new File(path);
		if(root.listFiles()!=null) {
			reset();
			NetworkParameters.displayAllRoutes = true;
			for (File file : root.listFiles()) {
				routes.add(getRoute(file, file.getName().substring(0, file.getName().indexOf('.'))));
			}
			MapLayer.repaint();
		}
	}
	
	public static void loadListOfRoutes(ObservableList<String> list) {
		String path = Project.getProjectGpsPath();
		File root = new File(path);
		
		if(root.listFiles()!=null) {
			reset();
			NetworkParameters.displayAllRoutes = true;
			for (File file : root.listFiles()) {
				String s = file.getName();
				if(list.contains(s)) {
					routes.add(getRoute(file, s.substring(0, file.getName().indexOf('.'))));
				}
			}
			MapLayer.repaint();
		}
	}

	public static NamedRoute getRoute(File file, String name) {
		try {
			ArrayList<DPoint> route = new ArrayList<DPoint>();
			NamedRoute nr = new NamedRoute(name, route);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] str;
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				route.add(new DPoint(str[1], str[2]));
			}
			br.close();
			return nr;
		} catch (FileNotFoundException e) {
			System.err.println("[Routes] No Route!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void reset() {
		if(routes != null)
			if(routes.size()>0)
				routes.removeAll(routes);
		routes = new ArrayList<NamedRoute>();
		NetworkParameters.displayAllRoutes = false;
	}

	public static void hideAll() {
		routes = null;
		MapLayer.repaint();
	}
	
	public static NamedRoute getRouteByName(String name) {
		if(routes !=null)
			for(NamedRoute route: routes) {
				if(route.getName().equals(name)) 
					return route;
			}
		return null;
	}
	
	public static int [] closestIndex(int routeIndex, NamedRoute nr1, NamedRoute nr2) {
		int [] ridx = {0, 0};
		
		//double x = nr1.getRoute().get(routeIndex).getX();
		//double y = nr1.getRoute().get(routeIndex).getY();
		//int idx1 = 0;
		//int idx2 = 0;
		//double distance = 0;
		//double distMin = Double.MAX_VALUE;
 		
		//public static boolean intersect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		
		for(int i=routeIndex; i<nr1.size()-1; i++) {
			for(int j=0; j<nr2.size()-1; j++) {
				//distance = MapLayer.distance(x, y, nr2.getRoute().get(j).getX(), nr2.getRoute().get(j).getY());
				//if(distance < distMin) {
				//	distMin = distance;
				//	idx2 = j;
				//}
				if(Geometry.intersect(
						nr1.getRoute().get(i).getX(), 
						nr1.getRoute().get(i).getY(),
						nr1.getRoute().get(i+1).getX(), 
						nr1.getRoute().get(i+1).getY(), 
						nr2.getRoute().get(j).getX(), 
						nr2.getRoute().get(j).getY(),
						nr2.getRoute().get(j+1).getX(), 
						nr2.getRoute().get(j+1).getY())) 
				{
					ridx[0] = i;
					ridx[1] = j+1;
					return ridx;
				}
			}
		}
		//return idx+1;
		return ridx;
	}
	
	public static int [] closestIndex2(SensorNode sensor, NamedRoute nr2) {
		int [] ridx = {0, 0};
		for(int i=0; i<sensor.getRouteSize()-1; i++) {
			for(int j=0; j<nr2.size()-1; j++) {
				ridx[0] = i;
				ridx[1] = j+1;
				if(Geometry.intersect(
						sensor.getIthX(i), 
						sensor.getIthY(i),
						sensor.getIthX(i+1), 
						sensor.getIthY(i+1), 
						nr2.getRoute().get(j).getX(), 
						nr2.getRoute().get(j).getY(),
						nr2.getRoute().get(j+1).getX(), 
						nr2.getRoute().get(j+1).getY())) 
				{
					return ridx;
				}
			}
		}
		return ridx;
	}
	
	public static int [] getIntersectionIdxs(int routeIndex, NamedRoute nr1, NamedRoute nr2) {
		int [] idxs = {0, 0} ;
		
		for(int i=0; i<nr1.size()-1; i++) {
			DPoint p0 = nr1.getRoute().get(i);
 			DPoint p1 = nr1.getRoute().get(i+1);
	 		for(int j=0; j<nr2.size()-1; j++) {
	 			DPoint p2 = nr2.getRoute().get(j);
	 			DPoint p3 = nr2.getRoute().get(j+1);
	 			if(Geometry.intersect(p0, p1, p2, p3)) {
	 				//System.out.println(i+" "+j+" "+Geometry.intersect(p0, p1, p2, p3));
	 				idxs[0] = i;
	 				idxs[1] = j;
	 				break;
	 			}
			}
		}
		//System.out.println("-------------------------------");
		//System.out.println("-------------------------------");
		//System.out.println();
		return idxs;
	}
	
	public static int numberOfClosestNodes(int routeIndex1, NamedRoute nr1, int routeIndex2, NamedRoute nr2) {
		double x1 = nr1.getRoute().get(routeIndex1).getX();
		double y1 = nr1.getRoute().get(routeIndex1).getY();
		double x2 = nr2.getRoute().get(routeIndex2).getX();
		double y2 = nr2.getRoute().get(routeIndex2).getY();
		
		double distance_ref = MapLayer.distance(x1, y1, x2, y2);

		int k = 1;
		
 		for(int i=routeIndex1+1; i<nr1.size(); i++) {
 			x1 = nr1.getRoute().get(i).getX();
 			y1 = nr1.getRoute().get(i).getY();
			double distance = MapLayer.distance(x1, y1, x2, y2);
			if(distance > distance_ref) {
				return k-1;
			}
			k++;
		} 		
		return 1;
	}

}
