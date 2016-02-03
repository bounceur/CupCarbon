package geo_objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class GeoZone {
	
	protected int type = Device.GEOZONE;
	
	private double [] coordX ;
	private double [] coordY ;
	//private double [] coordZ ;
	private int [] iCoordX ;
	private int [] iCoordY ;
	//private int [] iCoordZ ;
	private int size = 0 ;
	private boolean selected = false ;
	private int color = 0; 
	private int mapZoom = 0;
	
	public GeoZone(int size, int color) {
		mapZoom = MapLayer.mapViewer.getZoom();
		this.size = size;
		this.color = color;
		coordX = new double [size] ;
		coordY = new double [size] ;
		//coordZ = new double [size] ;
		iCoordX = new int [size] ;
		iCoordY = new int [size] ;
		//iCoordZ = new int [size] ;	
	}
	
	public GeoZone(String [] str) {
		mapZoom = MapLayer.mapViewer.getZoom();
		size = str.length;
		coordX = new double [size] ;
		coordY = new double [size] ;
		//coordZ = new double [size] ;
		iCoordX = new int [size] ;
		iCoordY = new int [size] ;
		//iCoordZ = new int [size] ;
		for(int i=0; i<size; i++) {
			coordX[i]=Double.valueOf(str[i*2]);
			coordY[i]=Double.valueOf(str[i*2+1]);
			//coordZ[i]=Double.valueOf(str[i*2+2]);
			computeIntCoord(i);
		}
	}
	
	public GeoZone(String str) {
		mapZoom = MapLayer.mapViewer.getZoom();
		String [] vStr = str.split(" ");
		size = vStr.length/3;
		coordX = new double [size] ;
		coordY = new double [size] ;
		//coordZ = new double [size] ;
		iCoordX = new int [size] ;
		iCoordY = new int [size] ;
		//iCoordZ = new int [size] ;
		for(int i=0; i<size; i++) {
			coordX[i]=Double.valueOf(vStr[i*2]);
			coordY[i]=Double.valueOf(vStr[i*2+1]);
			//coordZ[i]=Double.valueOf(vStr[i*2+2]);
			computeIntCoord(i);
		}
	}

	public void set(double x, double y, double z, int i) {
		coordX[i] = x;
		coordY[i] = y;
		//coordZ[i] = z;
		computeIntCoord(i);
	}
	
	public void set(String x, String y, String z, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
		//coordZ[i] = Double.valueOf(z);
		computeIntCoord(i);
	}
	
	public double getXCoords(int i) {
		return coordX[i];
	}
	
	public double getYCoords(int i) {
		return coordX[i];
	}
	
//	public double getZCoords(int i) {
//		return coordZ[i];
//	}
	
	public double [] getXCoords() {
		return coordX;
	}
	
	public double [] getYCoords() {
		return coordY;
	}
	
//	public double [] getZCoords() {
//		return coordZ;
//	}
	
	public int size() {
		return size;
	}
	
	public void computeIntCoord(int i) {
		int [] coord = MapCalc.geoToPixelMapA(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
		iCoordX[i]=coord[0];
		iCoordY[i]=coord[1];
	}
	
	public void computeIntCoords() {
		int [] coord = null ;	
		for(int i=0; i<size; i++) {
			coord = MapCalc.geoToPixelMapA(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
			iCoordX[i]=coord[0];
			iCoordY[i]=coord[1];
		}
	}
	
	public void draw(Graphics g) {
		int newZoom = MapLayer.mapViewer.getZoom();
		if(newZoom != mapZoom) {
			mapZoom = newZoom;
			computeIntCoords() ;
		}	
		if (color==0)
			g.setColor(UColor.RED_TTRANSPARENT);//.ORANGE_TRANSPARENT2);
		if (color==1)
			g.setColor(UColor.GREEND_TRANSPARENT);
		if (color==2)
			g.setColor(UColor.PURPLE_TRANSPARENT);

//		Graphics2D g2 = (Graphics2D) g;
//		Point2D center = new Point2D.Float(iCoordX[0], iCoordY[0]);
//		float radius = 400;
//		float[] dist = {0.0f, 0.5f};
//		Color[] colors = {UColor.RED_TRANSPARENT, UColor.TRANSPARENT};
//		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
//		g2.setPaint(p);
		
		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		//Paint p = new GradientPaint(0, 0, UColor.RED_TRANSPARENT, 100, 100, UColor.ORANGE_TRANSPARENT2, true);

		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.fillPolygon(iCoordX, iCoordY, size);
		//g.drawPolygon(iCoordX, iCoordY, size);
	}
	
	public boolean inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
		Polygon poly = new Polygon(iCoordX,iCoordY,size);
		return (poly.contains(p1));
	}

	public void setSelection(boolean b) {
		selected = b ;
	}

	public boolean isSelected() {
		return selected ;
	}
	
	public boolean intersect(Polygon p) {
		for (int i=0; i<size; i++){
			if(p.contains(iCoordX[i], iCoordY[i]))
				return true;
		}
		return false;
	}
	
//	public Vector3d [] toVector3d() {
//		Vector3d [] vector = new Vector3d [size] ;
//		for(int i=0; i<size; i++) {
//			vector[i] = new Vector3d(coordY[i], coordX[i], 10.0);
//		}
//		return vector;
//	}
//	
//	public Vector3d [] toVector3d(int m) {
//		Vector3d [] vector = new Vector3d [size] ;
//		for(int i=0; i<size; i++) {
//			vector[i] = new Vector3d(coordY[i]/m, coordX[i]/m, 10.0);
//		}
//		return vector;
//	}

	public void reduce(double xref, double yref, double zm) {
		for(int i=0; i<size; i++) {
			coordX[i] = coordX[i]/zm+xref ;
			coordY[i] = coordY[i]/zm+yref ;
			computeIntCoord(i);
		}
	}
	
	public boolean contains(Point2D p) {
		Polygon poly = new Polygon(iCoordX, iCoordY, size);
		return (poly.contains(p));
	}
	
	public boolean contains(double px, double py) {
		Polygon poly = new Polygon(iCoordX, iCoordY, size);
		return poly.contains(px, py);
	}
	
	public void display() {
		for(int i=0; i<size; i++) {
			System.out.print("("+iCoordX[i]+", "+iCoordY[i]+") ");
		}
	}
	
}
