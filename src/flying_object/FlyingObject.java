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

package flying_object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import device.DeviceWithRadio;
import device.MobileG;
import map.MapLayer;
import project.Project;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 */
public class FlyingObject extends MobileG {

	protected double xc = 0;
	protected double yc = 0;
	private double direction;
	// flying object parameters
	private double speedOnX = .000005;
	private double speedOnY = .000005;
	private double rotationAngle = .2;
	private double dispersion = 400.;
	private boolean detected = false;

	private LinkedList<Long> routeTime;
	private LinkedList<Double> routeX;
	private LinkedList<Double> routeY;
	private boolean loop = false;
	private int routeIndex = 0;

	// ------------------------------------

	public FlyingObject(double x, double y, int theta, double xc, double yc) {
		this.longitude = x;
		this.latitude = y;
		this.direction = theta;
		this.xc = xc;
		this.yc = yc;
		radius = 30;
	}

	public FlyingObject(double x, double y, boolean dispersion) {
		generate(x, y, dispersion);
	}

	public void generate(double x, double y, boolean dispersion) {
		if (dispersion) {
			xc = Math.random() / this.dispersion;
			yc = Math.random() / this.dispersion;
		}
		this.longitude = x + xc;
		this.latitude = y + yc;
		direction = (int) (Math.random() * 360);
		radius = 30;
	}

	// ------------------------------------

	/**
	 * Move the flying object to the new direction
	 * 
	 * @param newDirection
	 *            Angle in degree of the new direction
	 */
	public void move(double newDirection) {
		double left = (newDirection - direction + 360) % 360;
		double right = (direction - newDirection + 360) % 360;
		double thetaChange = 0;
		if (left < right) {
			thetaChange = Math.min(rotationAngle, left);
		} else {
			thetaChange = -Math.min(rotationAngle, right);
		}
		direction = (direction + thetaChange + 360) % 360;
		longitude += (speedOnX * Math.cos(direction * Math.PI / 180));
		latitude -= (speedOnY * Math.sin(direction * Math.PI / 180));
	}

	/**
	 * Draw the flaying object
	 * 
	 * @param g
	 *            Graphics
	 */
	public void draw(Graphics g) {
		// Graphics2D g = (Graphics2D) gg;
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int x = coord[0];
		int y = coord[1];
		// int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
		// int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
		// int v = 5*(Layer.getMapViewer().getZoom()-10);
		int v = 10 - MapLayer.getMapViewer().getZoom();
		if (v < 0)
			v = 1;
		int rayon = MapCalc.radiusInPixels(this.radius);

		if (detected) {
			g.setColor(UColor.RED);
			g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
					(rayon + 4) * 2);
		}
		g.setColor(Color.BLACK);
		// g.drawArc((int) x - rayon/2, (int) y - rayon/2, rayon, rayon,
		// (int) direction - 90 - 40, 80);
		// g.drawArc((int) x - rayon*2/3, (int) y - rayon*2/3, rayon*4/3,
		// rayon*4/3,
		// (int) direction - 90 - 40, 80);

		g.fillArc((int) x - v, (int) y - v, v * 2, v * 2,
				(int) direction - 90 - 20, 40);
		g.fillArc((int) x - rayon, (int) y - rayon, rayon * 2, rayon * 2,
				(int) direction - 90 - 1, 2);

	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getDirection() {
		return direction;
	}

	// ------------------------------------------------------------------------
	// Load Route from file to Lists
	// ------------------------------------------------------------------------
	public void loadRouteFromFile() {
		routeIndex = 0;
		routeTime = new LinkedList<Long>();
		routeX = new LinkedList<Double>();
		routeY = new LinkedList<Double>();
		FileInputStream fis;
		BufferedReader b = null;
		String s;
		String[] ts;
		try {
			if (!gpsFileName.equals("")) {
				fis = new FileInputStream(Project.getProjectGpsPath()+"/"+gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				underSimulation = true; 
				b.readLine();
				b.readLine();
				b.readLine();
				//b.readLine();
				//b.readLine();
				loop = Boolean.parseBoolean(b.readLine());
				b.readLine();
				while ((s = b.readLine()) != null) {
					ts = s.split(" ");
					routeTime.add(Long.parseLong(ts[0]));
					routeX.add(Double.parseDouble(ts[1]));
					routeY.add(Double.parseDouble(ts[2]));
				}
				b.close();
				fis.close();
				//System.out.println(routeX);
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// Go to next index (routeIndex)
	// ------------------------------------------------------------------------
	// @Override
	public void goToNext() {
		if (routeTime != null) {
			routeIndex++;
			if (routeIndex == routeTime.size()) {
				if (loop) {
					routeIndex = 0;
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Test the existence of a next ponit
	// ------------------------------------------------------------------------
	public boolean hasNext() {
		if (routeIndex < routeTime.size())
			return true;
		return false;
	}

	@Override
	public void run() {
		loadRouteFromFile();
		fixori();
		underSimulation = true;
		routeIndex = 0;
		selected = false;
		double x1, y1, x2, y2;
		boolean firstTime = true;
		double distance;
		
		do {
			x2 = longitude;
			y2 = latitude;
			x1 = routeX.get(routeIndex) + Math.random() / dispersion;
			y1 = routeY.get(routeIndex) + Math.random() / dispersion;
			if (firstTime) {
				longitude = x1;
				latitude = y1;
				firstTime = false;
			} else {
				distance = 1.35 * MapCalc.distance(x1, y1, x2, y2);
				int d = 1;
				for (int i = 0; i < distance; i++) {
					move(getAngle(x1, y1, x2, y2));
					MapLayer.getMapViewer().repaint();
					try {
						Thread.sleep(d);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			goToNext();
		} while (hasNext());
		routeIndex = 0;
		selected = false;
		toOri();
		thread = null;
		underSimulation = false;
		MapLayer.getMapViewer().repaint();
	}

	/**
	 * Calculate the angle between two points (x1,y1) and (x2,y2) in degree
	 * 
	 * @param x1
	 *            x of the first point
	 * @param y1
	 *            y of the second point
	 * @param x2
	 *            x of the first point
	 * @param y2
	 *            y of the secong point
	 * @return the angle in degree
	 */
	public double getAngle(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double alpha = Math.atan(dy / dx);
		alpha = 180 * alpha / Math.PI;
		if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
			return (180 - (int) alpha);
		else
			return (-(int) alpha);
	}

	public void setXYFromMouse(int xm, int ym) {
		Point p = new Point(xm, ym);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		longitude = gp.getLatitude();
		latitude = gp.getLongitude();
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	@Override
	public int getType() {
		return Device.FLYING_OBJECT;
	}

	@Override
	public void setRadioRadius(double radiuRadius) {
	}

	@Override
	public void setSensorUnitRadius(double captureRadius) {
	}

	@Override
	public String getIdFL() {
		return "F";
	}

	public void relativeMove(double x, double y) {
		if (thread == null) {
			this.longitude = x + xc;
			this.latitude = y + yc;
		}
	}

	public void setDetected(boolean b) {
		detected = b;
	}

	public boolean getDetected() {
		return detected;
	}

	@Override
	public double getNextTime() {
		return 0;
	}

	@Override
	public void moveToNext(boolean visual, int visualDelay) {
	}

	@Override
	public boolean canMove() {
		return false;
	}
	
	public void loadScript() {}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRadioLinks(Graphics g) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void drawRadioPropagations(Graphics g) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initBattery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Polygon getRadioPolygon() {
		return null;
	}

	@Override
	public void calculatePropagations() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void resetPropagations() {

	}

	@Override
	public boolean radioDetect(DeviceWithRadio device) {
		return false;
	}

	@Override
	public void initGeoZoneList() {
		
	}
}
