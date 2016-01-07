/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

package sensorunit;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import flying_object.FlyingGroup;
import flying_object.FlyingObject;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class SensorUnit implements KeyListener, Cloneable {

	protected double radius = 10;
	protected double longitude;
	protected double latitude;
	protected double elevation;
	protected Device node;
	protected boolean displayRadius = false;
	
	
	protected int n = 30;
	protected double deg = 0.209333;
	
	protected int [] polyX = new int[n];
	protected int [] polyY = new int[n];

	/**
	 * Constructor 1 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param node which is associated to this sensor unit
	 */
	public SensorUnit(double longitude, double latitude, double elevation, Device node) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.node = node;
		calculateSensingArea();
		MapLayer.getMapViewer().addKeyListener(this);
	}

	/**
	 * Constructor 3 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param cuRadius the value of the radius 
	 * @param node which is associated to this sensor unit
	 */
	public SensorUnit(double longitude, double latitude, double elevation, double radius, Device node) {
		this(longitude, latitude, elevation, node);
		this.radius = radius;
		calculateSensingArea();
	}	

	public void calculateSensingArea() {
		int rayon = MapCalc.radiusInPixels(radius) ; 
		
		double r2=0;
		double r3=0;
		
		double i=0.0;
		for(int k=0; k<n; k++) {
			r2 = rayon*Math.cos(i);
			r3 = rayon*Math.sin(i);
			polyX[k]=(int)(longitude+r2);
			polyY[k]=(int)(latitude+r3);
			i+=deg;
		}
	}
	
	/**
	 * @return radius of the sensor unit
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Change the position of the sensor unit
	 */
	public void setPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	
	public boolean detect(Device device) {
		if(device.getRadius()>0) {
			Polygon poly = new Polygon(polyX, polyY, n);
			if(device.getType()==Device.FLYING_OBJECT) {
				for(FlyingObject d : ((FlyingGroup)device).getFlyingObjects()) {
					GeoPosition gp = new GeoPosition(d.getLongitude(),d.getLatitude());
					Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
					if(poly.contains(p1))
						return true;
				}
				return false;
			}
			else {
				GeoPosition gp = new GeoPosition(device.getLongitude(),device.getLatitude());
				Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());		
				return (poly.contains(p1));
			}
		}
		else
			return false;
	}
	
	/**
	 * Draw the sensor unit
	 */
	public void draw(Graphics g, int mode, boolean detection) {
		 calculateSensingArea();
		if (!detection)
			g.setColor(UColor.WHITE_LLTRANSPARENT);
		if (detection)
			g.setColor(UColor.YELLOW_SENSOR);
	
		if (mode == 0)
			g.fillPolygon(polyX, polyY, n);
		g.setColor(UColor.BLACK_TTTRANSPARENT);
		g.drawPolygon(polyX, polyY, n);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (node.isSelected()) {
			if (key.getKeyChar() == ')') {
				radius += 5;
				MapLayer.getMapViewer().repaint();
			}
			if (key.getKeyChar() == '(') {
				radius -= 5;
				MapLayer.getMapViewer().repaint();
			}			
		}
		if (key.getKeyChar() == 'e') {
			displayRadius = true;
		}

		if (key.getKeyChar() == 'r') {
			displayRadius = false;
		}
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	public void setNode(Device node) {
		this.node = node;
	}

	/**
	 * Clone the sensor unit
	 */
	@Override
	public SensorUnit clone() throws CloneNotSupportedException {
		SensorUnit newCU = (SensorUnit) super.clone();
		MapLayer.getMapViewer().addKeyListener(newCU);
		return newCU;
	}
	
}
