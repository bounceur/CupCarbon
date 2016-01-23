package geo_objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class GeoZone implements MouseListener, KeyListener {
	
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
	
	public GeoZone(int size, int color) {
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
		}
	}
	
	public GeoZone(String str) {
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
		}
	}

	public void set(double x, double y, double z, int i) {
		coordX[i] = x;
		coordY[i] = y;
		//coordZ[i] = z;
	}
	
	public void set(String x, String y, String z, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
		//coordZ[i] = Double.valueOf(z);
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
	
	public void draw(Graphics g) {
		int [] coord = null ;	
		for(int i=0; i<size; i++) {
			coord = MapCalc.geoToIntPixelMapXY(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
			iCoordX[i]=coord[0];
			iCoordY[i]=coord[1];
		}
		if(selected)
			g.setColor(UColor.colorTab[color]);
		else
			g.setColor(UColor.PURPLE_TRANSPARENT);
		g.fillPolygon(iCoordX, iCoordY, size);
		g.setColor(UColor.BLACK_TTTRANSPARENT);
		g.drawPolygon(iCoordX, iCoordY, size);
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
		Polygon poly = new Polygon(iCoordX,iCoordY,size);
		return (poly.contains(p1));
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode()==8 && selected) {
			selected = false;
			MapLayer.getMapViewer().removeMouseListener(this);
			MapLayer.getMapViewer().removeMouseListener(this);	
			//GeoZoneList.delete(this);
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
		}
	}
	
	//public 
	
//	public boolean intersect(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
//		Polygon poly = new Polygon(iCoordX,iCoordY,n);
//		return poly.intersects((double) cadreX1, (double) cadreY1, (double) cadreX2, (double) cadreY2);
//	}
}
