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

import java.awt.Color;
import java.awt.Graphics;

import utilities.UColor;


/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class BaseStation extends StdSensorNode {

	protected int type = Device.BASE_STATION;

	{
		radioRangeColor1 = UColor.YELLOW_TRANSPARENT;
		radioRangeColor2 = UColor.YELLOWD_TRANSPARENT;
	}
	
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
	public BaseStation(String id, String rdInfos, String x, String y, String radius, String radioRadius, String cuRadius, String gpsFileName, String scriptFileName) {
		super(id, rdInfos,  x,  y,  radius, radioRadius, cuRadius, gpsFileName, scriptFileName);
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
	public BaseStation(double x, double y, double radius, double radioRadius, double cuRadius, int id) {
		super(x, y, radius, radioRadius, cuRadius, id);
	}
	
	@Override
	public void drawTheCenter(Graphics g, int x, int y) {
		int [] triangleX = new int [3];
		int [] triangleY = new int [3];
		int sz = 5;
		triangleX[0] = x-sz ;
		triangleX[1] = x ;
		triangleX[2] = x+sz ;
		triangleY[0] = y+sz ;
		triangleY[1] = y-sz ;
		triangleY[2] = y+sz ;
		
		if (underSimulation) {
			g.setColor(UColor.GREEN);
		} else {
			g.setColor(UColor.RED);
		}			
		if(isDead()) g.setColor(Color.BLACK);
		if(!getScriptFileName().equals(""))
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
	public void consumeTx(double v) {}
	
	@Override 
	public void consumeRx(double v) {}

	@Override
	public boolean detect(Device device) {
		return false;
	}	
}