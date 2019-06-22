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
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class StdSensorUnit extends SensorUnit {
	
	protected double deg = 0.209333;

	/**
	 * Constructor 1 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param node which is associated to this sensor unit
	 */
	public StdSensorUnit(double longitude, double latitude, double elevation, Device node) {
		super(longitude, latitude, elevation, node);		
		radius = 10;
		n = 30;
		 deg = 0.209333;
		calculateSensingArea();
		//MapLayer.mapViewer.addKeyListener(this);
	}

	/**
	 * Constructor 3 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param cuRadius the value of the radius 
	 * @param node which is associated to this sensor unit
	 */
	public StdSensorUnit(double longitude, double latitude, double elevation, double radius, Device node) {
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
	 * Set the radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	/**
	 * Get the radius
	 */
	public double getRadius() {
		return radius;
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
			GeoPosition gp = new GeoPosition(device.getLatitude(), device.getLongitude());
			Point2D p1 = MapLayer.mapViewer.getTileFactory().geoToPixel(gp, MapLayer.mapViewer.getZoom());		
			return (poly.contains(p1));
		}
		else
			return false;
	}
	
	/**
	 * Draw the sensor unit
	 */
	public void draw(Graphics g, int mode, boolean detection, boolean buildingDetection) {
		 calculateSensingArea();
		 if (!detection)
			g.setColor(UColor.WHITE_LLLTRANSPARENT);
		 else
			 g.setColor(UColor.YELLOW_SENSOR_TR);
	
		if (mode == 0)
			g.fillPolygon(polyX, polyY, n);
		g.setColor(UColor.BLACK_TTTRANSPARENT);
		g.drawPolygon(polyX, polyY, n);
	}
	
	public void incRadius(int u) {
		radius += u;
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
	public StdSensorUnit clone() throws CloneNotSupportedException {
		StdSensorUnit newSU = (StdSensorUnit) super.clone();
		//MapLayer.mapViewer.addKeyListener(newCU);
		return newSU;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	
	public double getESensing() {
		return eSensing;
	}

	public void setESensing(double eSensing) {
		this.eSensing = eSensing;
	}

	@Override
	public double getCoverage() {
		return 0;
	}

	@Override
	public double getDirection() {
		return 0;
	}
	
}
