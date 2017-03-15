/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
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
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package cupcarbon;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import action.CupActionStack;
import javafx.stage.Stage;
import map.WorldMap;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 3.0.0 (U-One)
 */

public class CupCarbon2 {

	public static Stage stage;
	public static CupCarbonController cupCarbonController;
	public static boolean macos = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {					
				try {					
					WorldMap map = new WorldMap();
					map.getMainMap().setLoadingImage(Toolkit.getDefaultToolkit().getImage("tiles/mer.png"));
					map.setZoomSliderVisible(false);
					map.setZoomButtonsVisible(false);
					map.setMiniMapVisible(false);
					map.getZoomSlider().setSnapToTicks(false);
					map.getZoomSlider().setPaintTicks(false);
					
					CupActionStack.init();
					
					JFrame jf = new JFrame();
					jf.add(map);
					jf.setSize(1000, 600);
					jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					jf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		

	}
	
}
