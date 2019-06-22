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

import device.Device;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public abstract class SensorUnit {

	protected double radius = 10;
	protected double longitude;
	protected double latitude;
	protected double elevation;
	protected Device node;
	protected boolean displayRadius = false;
	protected double eSensing = 1; // sensing energy
	
	protected double coverage = 0.0;
	protected double direction = 0;
	
	protected int n = 30;
	protected double deg = 0.209333;
	
	protected int [] polyX = new int[n];
	protected int [] polyY = new int[n];

	public SensorUnit(double longitude, double latitude, double elevation, Device node) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.node = node;
	}

	public abstract void calculateSensingArea();
	
	/**
	 * Change the position of the sensor unit
	 */
	public void setPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public abstract boolean detect(Device device);
	
	/**
	 * Draw the sensor unit
	 */
	public abstract void draw(Graphics g, int mode, boolean detection, boolean buildingDetection);

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
		//MapLayer.mapViewer.addKeyListener(newCU);
		return newCU;
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
	
	public Polygon getPoly() {
		return new Polygon(polyX, polyY, n);
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getCoverage() {
		return coverage;
	}
	
	public double getDirection() {
		return direction;
	}
	
	public void setCoverage(double coverage) {
		this.coverage = coverage ;
	}
	
	public void setDirection(double direction) {
		this.direction = direction ;
	}
	
	public abstract void incRadius(int u);
	
}
