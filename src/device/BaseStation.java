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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import project.Project;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class BaseStation extends StdSensorNode {

	protected int type = Device.BASE_STATION;

	//{
		//this.getCurrentRadioModule().setRadioColor();
		//radioRangeColor1 = UColor.YELLOW_TRANSPARENT;
		//radioRangeColor2 = UColor.YELLOWD_TRANSPARENT;
	//}
	
	/**
	 * Constructor 6
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param gpsFileName
	 *            The path of the GPS file
	 * @param scriptFileName
	 *            The path of the script file
	 */
	public BaseStation(String id, String x, String y, String z, String radius, String radioRadius, String cuRadius, String gpsFileName, String scriptFileName) {
		super(id,  x,  y,  z, radius, radioRadius, cuRadius, gpsFileName, scriptFileName);
	}
	
	/**
	 * Constructor 3
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public BaseStation(double x, double y, double z, double radius, double radioRadius, double cuRadius, int id) {
		super(x, y, z, radius, radioRadius, cuRadius, id);
	}
	
	@Override
	public void drawTheCenter(Graphics g, int x, int y) {
		int [] triangleX = new int [3];
		int [] triangleY = new int [3];
		int sz = 8;
		triangleX[0] = x-sz ;
		triangleX[1] = x ;
		triangleX[2] = x+sz ;
		triangleY[0] = y+sz ;
		triangleY[1] = y-sz ;
		triangleY[2] = y+sz ;
		
		if (underSimulation) {
			g.setColor(new Color(38, 194, 27));
		} else {
			g.setColor(UColor.ORANGE);
			if(getScript() != null) {				
				if(getScript().isWaiting()) g.setColor(Color.RED);
			}
		}	
		
		if(isDead()) g.setColor(Color.BLACK);
		if(getScriptFileName().equals(""))
			g.setColor(Color.LIGHT_GRAY);
		g.fillPolygon(triangleX, triangleY, 3);
		
		g.setColor(UColor.BLACK_TTRANSPARENT);
		g.drawPolygon(triangleX, triangleY, 3);
	}
	
	@Override
	public String getIdFL() {
		return "SINK_";
	}
	
	@Override
	public int getType() {
		return type;
	}

	@Override 
	public void consumeTx(int v) {}
	
	@Override 
	public void consumeRx(int v) {}

	@Override
	public boolean detect(Device device) {
		return false;
	}
	
	@Override
	public SensorNode createNewWithTheSameType() {
		return new BaseStation(longitude, latitude, elevation, radius, 0.0, sensorUnit.getRadius(), DeviceList.number++);
	}
	
	@Override
	public void save(String ref) {
		String fileName = Project.getProjectNodePath();
		try {
			PrintStream fos = null;	
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + "basestation_" + ref));
			fos.println("List of parameters");
			fos.println("------------------------------------------");
			fos.println("device_type:" + getType());
			fos.println("device_id:" + getId());
			fos.println("device_longitude:" + getLongitude());
			fos.println("device_latitude:" + getLatitude());
			fos.println("device_elevation:" + getElevation());
			fos.println("device_radius:" + getRadius());
			fos.println("device_hide:" + getHide());
			fos.println("device_draw_battery:" + getDrawBatteryLevel());
			fos.println("device_sensor_unit_radius:" + getSensorUnitRadius());			
			if (!getGPSFileName().equals(""))
				fos.println("device_gps_file_name:" + getGPSFileName());
			if (!getScriptFileName().equals(""))
				fos.println("device_script_file_name:" + getScriptFileName());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		saveRadioModule(Project.getProjectRadioPath() + File.separator + "basestation_"+ref);
	}
	
	@Override
	public void drawMarked(Graphics g2) {
		if (!isDead()) {
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.4f));
			
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int [] triangleX = new int [3];
			int [] triangleY = new int [3];
			int sz = 20;
			int x = coord[0];
			int y = coord[1];
			double f1 = 1.7;
			double f2 = 0.7;
			double f3 = 0.8;
			triangleX[0] = x-(int)(f2*sz) ;
			triangleX[1] = x ;
			triangleX[2] = x+(int)(f2*sz) ;
			triangleY[0] = y+(int)(sz/f1) ;
			triangleY[1] = y-(int)(f3*sz) ;
			triangleY[2] = y+(int)(sz/f1) ;

			if (ledColor==1) {	
				g.setColor(UColor.GREEND_TRANSPARENT);
				g.fillPolygon(triangleX, triangleY, 3);
				g.setColor(Color.GRAY);
				g.drawPolygon(triangleX, triangleY, 3);
			}

			if(ledColor>1) {				
				g.setColor(UColor.colorTab[ledColor-1]);
				g.fillPolygon(triangleX, triangleY, 3);
				g.setColor(Color.GRAY);
				g.drawPolygon(triangleX, triangleY, 3);
			}
		}
	}

}