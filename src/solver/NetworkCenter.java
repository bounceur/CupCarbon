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

import map.Layer;
import device.Device;
import device.DeviceList;

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
		
		for (int i = 0; i < DeviceList.getNodes().size(); i++) {
			DeviceList.getNodes().get(i).setValue(0);
			DeviceList.getNodes().get(i).setMarked(false);			
		}
		Layer.getMapViewer().repaint();

		double sum = 0 ;
		Device n1, n2;
		for (int i = 0; i < DeviceList.getNodes().size(); i++) {
			sum = 0 ;
			n1 = DeviceList.getNodes().get(i);
			for (int j = 0; j < DeviceList.getNodes().size(); j++) {
				n2 = DeviceList.getNodes().get(j);
				if((i!=j) && n1.radioDetect(n2)) {
					sum++;
				}
				DeviceList.getNodes().get(i).setValue(sum);
				degList.add(sum);
			}
		}		

		double max = 0;
		int imax = 0;
		double div = 0;
		for (int iter = 0; iter < 5; iter++) {			
			for (int i = 0; i < DeviceList.getNodes().size(); i++) {
				sum = 0 ;
				n1 = DeviceList.getNodes().get(i);
				for (int j = 0; j < DeviceList.getNodes().size(); j++) {
					n2 = DeviceList.getNodes().get(j);
					if(n1.radioDetect(n2)) {
						sum += DeviceList.getNodes().get(j).getValue();
					}					
				}
				degList.set(i, sum);
			}
			for (int i = 0; i < DeviceList.getNodes().size(); i++) {
				DeviceList.getNodes().get(i).setValue(degList.get(i));
			}
			//min = 1000000000;
			max = 0;
			for (int i = 0; i < DeviceList.getNodes().size(); i++) {
				//if(DeviceList.getNodes().get(i).getRank()<min) min=DeviceList.getNodes().get(i).getRank();
				if(DeviceList.getNodes().get(i).getValue()>max) {
					max=DeviceList.getNodes().get(i).getValue();
					imax = i;
				}
			}
			
			for (int i = 0; i < DeviceList.getNodes().size(); i++) {
				div = 1;
				//if(DeviceList.getNodes().get(i).getRank() > 100) div = 10e4; 
				DeviceList.getNodes().get(i).setValue(DeviceList.getNodes().get(i).getValue()/div);
			}
		}
		
		System.out.println("--------------");
		System.out.println(max);
		System.out.println(imax);
		DeviceList.getNodes().get(imax).setMarked(true);
		Layer.getMapViewer().repaint();		
		System.out.println("--------------");
		
		
		
	}
}
