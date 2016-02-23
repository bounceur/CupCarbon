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

import java.awt.Graphics;


public abstract class MobileG extends DeviceWithoutRadio {

	protected int gpsId = 0 ;
	protected static int gpsNbr = 0 ; 
	
	public MobileG() {
	}
	
	public MobileG(double x, double y, double z, double rayon, String gpsFileName, int id) {
		super(x, y, z, rayon, id);
		mobile = true ;		
		this.gpsFileName = gpsFileName ;
	}
	
	public void setGPSFileName(String fileName) {
		gpsFileName = fileName ;
	}
	
	@Override
	public String getGPSFileName() {
		return gpsFileName ;
	}
	
	public abstract void draw(Graphics g) ;

//	@Override
//	public double getSensorUnitRadius() {
//		return 0 ;
//	}
	
	@Override
	public double getRadioRadius() {
		return 0 ;
	}
	
	@Override
	public void setRadioRadius(double radiuRadius) {
		
	}

	@Override
	public void setSensorUnitRadius(double captureRadius) {
		
	}

	@Override
	public void initBuffer() {
		
	}
}
