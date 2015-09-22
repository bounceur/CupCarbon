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
	private static int transpa = 190;
	private static int sentranspa = 30;
	// User Colors
	public static final Color BLEU_TRANSPARENT = new Color(18, 188, 228, 50);
	public static final Color BLEUF_TRANSPARENT = new Color(18, 188, 228, 150);
	public static final Color CYAN_TRANSPARENT = new Color(18, 128, 230, 100);
	public static final Color JAUNE_TRANSPARENT = new Color(255, 198, 0, 20);
	public static final Color JAUNEF_TRANSPARENT = new Color(255, 198, 0, 50);
	public static final Color ROSE_TRANSPARENT = new Color(210, 255, 0, 100);
	public static final Color VERTF_TRANSPARENT = new Color(210, 255, 0, 150);
	public static final Color MAUVE_TRANSPARENT = new Color(134, 20, 143, sentranspa);
	public static final Color MAUVEF_TRANSPARENT = new Color(134, 20, 143, sentranspa+30);
	public static final Color ROUGE_TTRANSPARENT = new Color(255, 200, 103, 20);
	public static final Color ROUGE_TRANSPARENT = new Color(255, 200, 103, 120);
	public static final Color ROUGEF_TRANSPARENT = new Color(255, 200, 103, 150);
	public static final Color RED = new Color(216, 0, 0);
	public static final Color BLUE = new Color(81, 127, 174);
	public static final Color GREEN = new Color(180, 225, 22);
	public static final Color ORANGE = new Color(240, 113, 51);
	public static final Color ORANGE_TRANSPARENT = new Color(240, 113, 51, 100);
	public static final Color JAUNE_SENSOR = new Color(255, 198, 0, 180);
	public static final Color WHITE_TRANSPARENT = new Color(255, 255, 255, 230);
	public static final Color WHITE_LTRANSPARENT = new Color(255, 255, 255, 160);
	public static final Color WHITE_LLTRANSPARENT = new Color(255, 255, 255, 60);
	public static final Color NOIR_TRANSPARENT = new Color(0, 0, 0, 150);
	public static final Color NOIR_TTRANSPARENT = new Color(0, 0, 0, 100);
	public static final Color NOIRF_TTTRANSPARENT = new Color(0, 0, 0, 70);

	public static final Color [] colorTab = {
		new Color(221,0,0,transpa),
		new Color(37,105,219,transpa),
		new Color(17,229,87,transpa),
		new Color(255,156,0,transpa),
		new Color(238,224,0,transpa),
		new Color(26,191,185,transpa),
		new Color(101,48,174,transpa),
		new Color(50,50,50,transpa),
		new Color(199,199,199,transpa),
		new Color(226,50,104,transpa),
		new Color(141,67,26,transpa),
		new Color(200,127,203,transpa),
		new Color(255,255,255,transpa)
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
