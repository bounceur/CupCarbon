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

package utilities;

import java.awt.Color;

public final class UColor {
	private static int transparency = 190;
	private static int sen_transparency = 30;
	// User Colors
	public static final Color BLUE_TRANSPARENT = new Color(18, 188, 228, 50);
	public static final Color BLUED_TRANSPARENT = new Color(18, 188, 228, 150);
	public static final Color CYAN_TRANSPARENT = new Color(18, 128, 230, 100);
	public static final Color YELLOW_TRANSPARENT = new Color(255, 198, 0, 20);
	public static final Color YELLOWD_TRANSPARENT = new Color(255, 198, 0, 50);
	public static final Color PINK_TRANSPARENT = new Color(210, 255, 0, 100);
	public static final Color GREEND_TRANSPARENT = new Color(210, 255, 0, 150);
	public static final Color PURPLE_TRANSPARENT = new Color(134, 20, 143, sen_transparency);
	public static final Color PURPLED_TRANSPARENT = new Color(134, 20, 143, sen_transparency+30);
	public static final Color RED_TTRANSPARENT = new Color(255, 200, 103, 20);
	public static final Color RED_TRANSPARENT = new Color(255, 200, 103, 120);
	public static final Color REDD_TRANSPARENT = new Color(255, 200, 103, 150);
	public static final Color RED = new Color(216, 0, 0);
	public static final Color BLUE = new Color(81, 127, 174);
	public static final Color GREEN = new Color(180, 225, 22);
	public static final Color ORANGE = new Color(240, 113, 51);
	public static final Color ORANGE_TRANSPARENT = new Color(240, 113, 51, 100);
	public static final Color YELLOW_SENSOR = new Color(255, 198, 0, 180);
	public static final Color WHITE_TRANSPARENT = new Color(255, 255, 255, 230);
	public static final Color WHITE_LTRANSPARENT = new Color(255, 255, 255, 160);
	public static final Color WHITE_LLTRANSPARENT = new Color(255, 255, 255, 60);
	public static final Color BLACK_TRANSPARENT = new Color(0, 0, 0, 150);
	public static final Color BLACK_TTRANSPARENT = new Color(0, 0, 0, 100);
	public static final Color BLACK_TTTRANSPARENT = new Color(0, 0, 0, 70);
	public static final Color BLACK_TTTTRANSPARENT = new Color(0, 0, 0, 10);

	public static final Color [] colorTab = {
		new Color(221,0,0,transparency),
		new Color(37,105,219,transparency),
		new Color(17,229,87,transparency),
		new Color(255,156,0,transparency),
		new Color(238,224,0,transparency),
		new Color(26,191,185,transparency),
		new Color(101,48,174,transparency),
		new Color(50,50,50,transparency),
		new Color(199,199,199,transparency),
		new Color(226,50,104,transparency),
		new Color(141,67,26,transparency),
		new Color(200,127,203,transparency),
		new Color(255,255,255,transparency)
	};
	
	public static final Color [] colorTab2 = {
		new Color(221,0,0),
		new Color(37,105,219),
		new Color(0,143,3),
		new Color(197,120,0),
		new Color(238,224,0),
		new Color(26,191,185),
		new Color(101,48,174),
		new Color(50,50,50),
		new Color(199,199,199),
		new Color(226,50,104),
		new Color(141,67,26),
		new Color(200,127,203),
		new Color(255,255,255)
	};
	
}
