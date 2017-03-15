/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

package solver;

import java.util.LinkedList;

import device.DeviceList;
import device.DeviceWithRadio;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @author Ali Benzerbadj
 * @author Farid Lalem
 * @author Massinissa Saoudi
 * @version 1.0
 */
public class NetworkCenter extends Thread {
	
	private LinkedList<Double> degList ;
	
	@Override
	public void run() {
		
		degList = new LinkedList<Double>() ;
		
		for (int i = 0; i < DeviceList.sensors.size(); i++) {
			DeviceList.sensors.get(i).setValue(0);
			DeviceList.sensors.get(i).setMarked(false);			
		}
		MapLayer.repaint();

		double sum = 0 ;
		DeviceWithRadio n1, n2;
		for (int i = 0; i < DeviceList.sensors.size(); i++) {
			sum = 0 ;
			n1 = DeviceList.sensors.get(i);
			for (int j = 0; j < DeviceList.sensors.size(); j++) {
				n2 = DeviceList.sensors.get(j);
				if((i!=j) && n1.radioDetect(n2)) {
					sum++;
				}
				DeviceList.sensors.get(i).setValue(sum);
				degList.add(sum);
			}
		}		

		double max = 0;
		int imax = 0;
		double div = 0;
		for (int iter = 0; iter < 5; iter++) {			
			for (int i = 0; i < DeviceList.sensors.size(); i++) {
				sum = 0 ;
				n1 = DeviceList.sensors.get(i);
				for (int j = 0; j < DeviceList.sensors.size(); j++) {
					n2 = DeviceList.sensors.get(j);
					if(n1.radioDetect(n2)) {
						sum += DeviceList.sensors.get(j).getValue();
					}					
				}
				degList.set(i, sum);
			}
			for (int i = 0; i < DeviceList.sensors.size(); i++) {
				DeviceList.sensors.get(i).setValue(degList.get(i));
			}
			//min = 1000000000;
			max = 0;
			for (int i = 0; i < DeviceList.sensors.size(); i++) {
				//if(DeviceList.sensors.get(i).getRank()<min) min=DeviceList.sensors.get(i).getRank();
				if(DeviceList.sensors.get(i).getValue()>max) {
					max=DeviceList.sensors.get(i).getValue();
					imax = i;
				}
			}
			
			for (int i = 0; i < DeviceList.sensors.size(); i++) {
				div = 1;
				//if(DeviceList.sensors.get(i).getRank() > 100) div = 10e4; 
				DeviceList.sensors.get(i).setValue(DeviceList.sensors.get(i).getValue()/div);
			}
		}
		
		System.out.println("--------------");
		System.out.println(max);
		System.out.println(imax);
		DeviceList.sensors.get(imax).setMarked(true);
		MapLayer.repaint();		
		System.out.println("--------------");
		
		
		
	}
}
