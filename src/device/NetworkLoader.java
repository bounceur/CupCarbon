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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jdesktop.swingx.JXMapViewer;

public class NetworkLoader extends Thread {
	
	private JXMapViewer mapViewer;
	
	public NetworkLoader(JXMapViewer mapViewer) {
		this.mapViewer = mapViewer ;
	}
	
	@Override
	public void run() {
		try {
			double x = 0;
			double y = 0;
			double z = 0;
			FileInputStream fis = new FileInputStream("sensors.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String s;
			String[] ps;
			String[] ics;			
			for (int i = 0; i < 1000; i++) {
				s = br.readLine();
				String[][] info = new String[7][2];
				info[0][0] = "Sensor Type : ";
				info[1][0] = "";
				info[2][0] = "";
				info[3][0] = "Battery : ";
				info[4][0] = "Name : ";
				info[5][0] = "Date : ";
				info[6][0] = "Time : ";
				info[0][1] = "";
				info[1][1] = "";
				info[2][1] = "";
				info[3][1] = "";
				info[4][1] = "";
				info[5][1] = "";
				info[6][1] = "";
				s = s.substring(1, s.length() - 1);
				ps = s.split(",");
				for (String cs : ps) {
					if (cs.endsWith("}"))
						cs = cs.substring(0, cs.length() - 1);
					if (cs.startsWith("\"values\":{\""))
						cs = cs.substring(10);
					ics = cs.split(":");
					ics[0] = ics[0].substring(1, ics[0].length() - 1);
					ics[1] = ics[1].substring(1, ics[1].length() - 1);
					if (ics[0].equals("Lattitude"))
						x = Double.parseDouble(ics[1]);
					if (ics[0].equals("Longitude"))
						y = Double.parseDouble(ics[1]);
					if (ics[0].equals("Elevation"))
						z = Double.parseDouble(ics[1]);
					if (ics[0].equals("Sensor_Type"))
						info[0][1] = ics[1];
					if (ics[0].equals("Temperature")) {
						info[1][0] = "Temperature : ";
						info[1][1] = ics[1]+" C";
					}
					
					if (ics[0].equals("Status_Desc")) {
						info[1][0] = "Status Desc : ";
						info[1][1] = ics[1];
					}

					if (ics[0].equals("Co_Index")) {
						info[2][0] = "CO Index : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Light")) {
						info[2][0] = "Light : ";
						info[2][1] = ics[1] + " lux";
					}
					if (ics[0].equals("Noise")) {
						info[2][0] = "Noise : ";
						info[2][1] = ics[1] + " dB";
					}
					if (ics[0].equals("State")) {
						info[2][0] = "State : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Battery"))
						info[3][1] = ics[1]+ " %";
					if (ics[0].equals("Node"))
						info[4][1] = ics[1];
					if (ics[0].equals("Date"))
						info[5][1] = ics[1];
					if (ics[0].equals("Time"))
						info[6][1] = ics[1];
				}
				DeviceList.add(new StdSensorNode(x, y, z, 0, 30, 10, info, -1));
				//MarkerList.add(new Marker(x,y,10));
				//Layer.getMapViewer().repaint();
				mapViewer.repaint();
				//sleep(500);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
