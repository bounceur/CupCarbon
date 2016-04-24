package geo_objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import map.MapLayer;
import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class Building implements MouseListener, KeyListener {
	
	protected int type = Device.BUILDING;
	
	private double[] coordX ;
	private double[] coordY ;
	private int[] iCoordX ;
	private int[] iCoordY ;
	private int nPoints = 0 ;
	private boolean selected = false ;
	private boolean hide = false ;
	private int mapZoom = 0;
	
	
	public Building(int nPoints) {
		mapZoom = MapLayer.mapViewer.getZoom();
		this.nPoints = nPoints;
		coordX = new double [nPoints] ;
		coordY = new double [nPoints] ;
		iCoordX = new int [nPoints] ;
		iCoordY = new int [nPoints] ;
	}
	
	public Building(String [] str) {	
		nPoints = str.length;
		coordX = new double [nPoints] ;
		coordY = new double [nPoints] ;
		iCoordX = new int [nPoints] ;
		iCoordY = new int [nPoints] ;
		for(int i=0; i<nPoints; i++) {
			coordX[i]=Double.valueOf(str[i*2]);
			coordY[i]=Double.valueOf(str[i*2+1]);
			computeIntCoord(i);
		}
	}
	
	public Building(String str) {
		String [] vStr = str.split(" ");
		nPoints = vStr.length/2;
		coordX = new double [nPoints] ;
		coordY = new double [nPoints] ;
		iCoordX = new int [nPoints] ;
		iCoordY = new int [nPoints] ;
		for(int i=0; i<nPoints; i++) {
			coordX[i]=Double.valueOf(vStr[i*2]);
			coordY[i]=Double.valueOf(vStr[i*2+1]);
			computeIntCoord(i);
		}
	}
	
	public void set(double x, double y, int i) {		
		coordX[i]=x;
		coordY[i]=y;
		computeIntCoord(i);
//		System.out.println(iCoordX[i]);
//		System.out.println(iCoordY[i]);
	}
	
	public void setInt(int x, int y, int i) {
//		System.out.println(x);
//		System.out.println(y);
		iCoordX[i]=x;
		iCoordY[i]=y;
		
		coordX[i] = MapCalc.pixelMapToGeo(x, y).getLongitude();
		coordY[i] = MapCalc.pixelMapToGeo(x, y).getLatitude();
		
	}
	
	public void set(String x, String y, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
		computeIntCoord(i);
	}
	
	public double getXCoords(int i) {
		return coordX[i];
	}
	
	public double [] getXCoords() {
		return coordX;
	}
	
	public double [] getYCoords() {
		return coordY;
	}
	
	public double getYCoords(int i) {
		return coordY[i];
	}
	
	public int getNPoints() {
		return nPoints;
	}
	
	public void computeIntCoord(int i) {
		int [] coord = MapCalc.geoToPixelMapA(Double.valueOf(coordY[i]), Double.valueOf(coordX[i]));
		iCoordX[i]=coord[0];
		iCoordY[i]=coord[1];
	}
	
	public void computeIntCoords() {
		int [] coord = null ;	
		for(int i=0; i<nPoints; i++) {
			coord = MapCalc.geoToPixelMapA(Double.valueOf(coordY[i]), Double.valueOf(coordX[i]));
			iCoordX[i]=coord[0];
			iCoordY[i]=coord[1];
		}
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.6f));
		//g2.setStroke(new BasicStroke());
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		if(!hide) {
			int newZoom = MapLayer.mapViewer.getZoom();
			if(newZoom != mapZoom) {
				mapZoom = newZoom;
				computeIntCoords() ;
			}
			g2.setColor(new Color(194, 182, 164, 50));
			if (selected)
				g2.setColor(new Color(194, 182, 164, 120));				
			g2.fillPolygon(iCoordX, iCoordY, nPoints);
			//g.setColor(UColor.BLACK_TTRANSPARENT);
			g2.setColor(Color.GRAY);
			if (selected)
				g2.setColor(Color.DARK_GRAY);
			g2.drawPolygon(iCoordX, iCoordY, nPoints);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		if(inside(arg.getX(),arg.getY())) {
			selected = !selected;
			MapLayer.getMapViewer().repaint();
		}
		//if(shape.contains(x, y))
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
		Polygon poly = new Polygon(iCoordX,iCoordY,nPoints);
		return (poly.contains(p1));
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if((key.getKeyCode()==8 || key.getKeyCode()==127) && selected && !hide) {
			selected = false;
			MapLayer.getMapViewer().removeMouseListener(this);
			MapLayer.getMapViewer().removeMouseListener(this);	
			BuildingList.delete(this);
			MapLayer.getMapViewer().repaint();
		}
		
		if(key.getKeyCode() == 27) {
			selected = false;
		}
		
		if (key.getKeyCode() == 65 && key.isControlDown()) {
			selected = true;
		}
		
		if (key.getKeyChar() == 'i') {
			selected = !selected;
		}
		
		if (key.getKeyChar() == 'H') {
			if(!selected)
				hide = !hide;
			MapLayer.getMapViewer().repaint();
		}
		
		if (key.getKeyChar() == 'w') {
			if (type == MapLayer.selectType)
				selected = true;
			else 
				selected = false;
		}		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setSelection(boolean b) {
		selected = b ;
	}

	public boolean isSelected() {
		return selected ;
	}
	
	public boolean intersect(Polygon p) {
		for (int i=0; i<nPoints; i++){
			if(p.contains(iCoordX[i], iCoordY[i]))
				return true;
		}
		return false;
	}

	public int size() {
		return nPoints;
	}
	
	public Vector3d [] toIntVector3d() {
		Vector3d [] vector = new Vector3d [nPoints] ;
		for(int i=0; i<nPoints; i++) {
			vector[i] = new Vector3d(iCoordY[i], iCoordX[i], 10.0);
			System.out.println(vector[i]);
		}
		return vector;
	}
	
	public Vector3d [] toVector3d() {
		Vector3d [] vector = new Vector3d [nPoints] ;
		for(int i=0; i<nPoints; i++) {
			vector[i] = new Vector3d(coordY[i], coordX[i], 10.0);
		}
		return vector;
	}	
	
	public Vector3d [] toVector3d(double xref, double yref, double zm) {
		Vector3d [] vector = new Vector3d [nPoints] ;
		for(int i=0; i<nPoints; i++) {
			//System.out.println((coordY[i]-xref)*zm);
			//System.out.println((coordX[i]-yref)*zm);
			vector[i] = new Vector3d((coordY[i]-xref)*zm, (coordX[i]-yref)*zm, 10.0);
		}
		return vector;
	}
	
	public Polygon getPoly()  {
		Polygon poly = new Polygon(iCoordX,iCoordY,nPoints);
		return poly;
	}
	
	public void display() {
		for(int i=0; i<nPoints; i++) {
			System.out.println("("+iCoordX[i]+", "+iCoordY[i]+")");
		}		
	}
	
	public boolean intersect(Building building) {
		Polygon poly = new Polygon(iCoordX,iCoordY,nPoints);
		for(int i=0; i<building.getNPoints(); i++) {
			int[] coord = MapCalc.geoToPixelMapA(building.getYCoords(i), building.getXCoords(i));
			Point p1 = new Point(coord[0], coord[1]);
			if(poly.contains(p1)) return true;
		}
		return false;
	}
	
	public boolean isHide() {
		return hide;
	}
}
