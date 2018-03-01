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

package device;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

public class Mobile extends MobileG {
	
	private String idFL = "M" ; // ID First Letter

	public Mobile(double x, double y, double z, double radius) {
		super(x, y, z, radius, "", DeviceList.number++);
	}
	
	public Mobile(double x, double y, double z, double radius, int id) {
		super(x, y, z, radius, "", id);
	}
	
	public Mobile(double x, double y, double z, double radius, String gpsFileName, int id) {
		super(x, y, z, radius, gpsFileName, id);	
	}
	
	public Mobile(String x, String y, String z, String radius, String gpsFileName, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), gpsFileName, id);	
	}
		
	@Override
	public void draw(Graphics g2) {	
		if(visible) {
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.5f));
			initDraw(g) ;
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];		
			int radius = MapCalc.radiusInPixels(this.radius) ;
			int ra = 15;
			if (inside || selected) {
				g.setColor(Color.GRAY);
				if(MapLayer.dark) g.setColor(Color.ORANGE); 
				g.drawLine(x-ra-3, y-ra-3, x-ra+2, y-ra-3);
				g.drawLine(x-ra-3, y-ra-3, x-ra-3, y-ra+2);
				g.drawLine(x-ra-3, y+ra+3, x-ra+2, y+ra+3);
				g.drawLine(x-ra-3, y+ra+3, x-ra-3, y+ra-2);			
				g.drawLine(x+ra+3, y-ra-3, x+ra-2, y-ra-3);
				g.drawLine(x+ra+3, y-ra-3, x+ra+3, y-ra+2);			
				g.drawLine(x+ra+3, y+ra+3, x+ra-2, y+ra+3);
				g.drawLine(x+ra+3, y+ra+3, x+ra+3, y+ra-2);
			}				
	
			switch(hide) {
			case 0 :				
				g.setColor(Color.LIGHT_GRAY);
				g.fillOval(x - radius, y - radius, radius * 2, radius * 2);				
			case 1 : 				
				g.setColor(Color.GRAY);
				if(MapLayer.dark)
					g.setColor(Color.LIGHT_GRAY);
				g.setStroke(new BasicStroke(2f));
				g.rotate(angle, x, y);
				g.drawLine(x-radius, y-radius, x+radius, y+radius);
				g.drawLine(x-radius, y+radius, x+radius, y-radius);
				g.fillOval(x-radius-6, y-radius-6, 6, 6);
				g.fillOval(x-radius-6, y+radius, 6, 6);
				g.fillOval(x+radius, y-radius-6, 6, 6);
				g.fillOval(x+radius, y+radius, 6, 6);
				g.setStroke(new BasicStroke(0.5f));
				g.rotate(-angle, x, y);
				g.setColor(UColor.WHITE_LTRANSPARENT);			
				g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
			}
			
			if (selected) {
				g.setColor(Color.GRAY);
				if(MapLayer.dark) g.setColor(Color.LIGHT_GRAY);
				g.drawOval(x - radius-8, y - radius-8, (radius+8) * 2, (radius+8) * 2);
			}	

			if(displayRadius) {
				drawRadius(x, y, radius, g);
			}
			
			int r = 5;
			g.setColor(Color.GRAY);
			g.fillOval(x - r, y - r, r*2, r*2);
			
			if(!getGPSFileName().equals("")) {
				if(underSimulation) {
					g.setColor(UColor.GREEN);
					g.fillOval(x-3, y-3, 6, 6);
				}
				else {					
					g.setColor(Color.ORANGE);
					g.fillOval(x-3, y-3, 6, 6);
				}
			}
			
			drawId(x,y,g);
		}
	}
	
	@Override
	public int getType() {
		return Device.MOBILE;
	}

	@Override
	public String getIdFL() {
		return idFL ;
	}
	
	@Override
	public String getName() {
		return getIdFL()+id;
	}
	
	public void loadScript() {}
	
	public boolean isDead() {
		return false ;
	}

	@Override
	public void execute() {
	}

	@Override
	public void drawRadioLinks(int k, Graphics g) {
	}
	
	@Override
	public void drawRadioPropagations(Graphics g) {
	}

	@Override
	public void initBattery() {
		
	}
	
	public Polygon getRadioPolygon() {
		return null;
	}

	@Override
	public void calculatePropagations() {}
	
	@Override
	public void resetPropagations() {}
	
	@Override
	public boolean radioDetect(DeviceWithRadio device) {
		return false;
	}

	@Override
	public void initGeoZoneList() {
		
	}
	
	@Override
	public void initBuffer() {
		
	}
	
	@Override
	public Mobile duplicate() {
		selected = false;
		Mobile mobile = new Mobile(longitude, latitude, elevation, radius);
		mobile.setHide(hide);
		mobile.setDrawBatteryLevel(drawBatteryLevel);
		mobile.setScriptFileName(scriptFileName);	
		mobile.setSelected(true);
		return mobile;
	}

	@Override
	public Mobile duplicateWithShift(double sLongitude, double sLatitude, double sElevation) {
		Mobile mobile = duplicate();
		mobile.shift(sLongitude, sLatitude, sElevation);
		return mobile;
	}
	
	@Override	
	public double getSensorUnitRadius() {
		return 0;
	}
	
	@Override
	public double getESensing() {
		return 0;
	}
	
	@Override
	public void save(String fileName) {
		try {
			PrintStream fos = null;
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + "mobile_"+ getId()));
			fos.println("List of parameters");
			fos.println("------------------------------------------");
			fos.println("device_type:" + getType());
			fos.println("device_id:" + getId());
			fos.println("device_longitude:" + getLongitude());
			fos.println("device_latitude:" + getLatitude());
			fos.println("device_elevation:" + getElevation());
			fos.println("device_radius:" + getRadius());
			fos.println("device_hide:" + getHide());
			if(!getGPSFileName().equals(""))
				fos.println("device_gps_file_name:" + getGPSFileName());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double getNextValueTime() {return Double.MAX_VALUE;}
	public void generateNextValue() {}
}