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

package map;

import java.awt.Color;
import java.awt.Graphics;

import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * 
 * The aim of this class is to explain how to add shapes in the OSM map in the following cases:
 * 1. Using GPS coordinates 
 * 2. Using the window pixels
 * 3. Using the mpa pixels
 * 
 * Go to the class
 */
public class OsmTest {

	private double longitude = 48.39188295873048 ;
	private double latitude = -4.44371223449707;
	
	public void drawFromGPS(Graphics g) {
		g.setColor(Color.red);
		int[] coord = MapCalc.geoToPixelMapA(longitude, latitude);
		int x = coord[0];
		int y = coord[1];
		g.fillOval(x, y, 50, 50);
	}

}
