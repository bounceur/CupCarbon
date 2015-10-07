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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import map.Layer;
import device.Device;
import device.DeviceList;
import device.StdSensorNode;


/**
 * @author Ahcene Bounceur
 * @author Ali Benzerbadj
 * @version 1.0
 */
public class GpsrB extends Thread {

	@Override
	public void run() {
		try {
			
			FileWriter fos = new FileWriter("dotgraph.dot");
			fos.write("digraph Border {\n");
			FileReader fr = new FileReader("gpsrb.txt");
			BufferedReader br = new BufferedReader(fr);
			String s ;	
			double x ;
			double y ;
			boolean b=true;
			String v="";
			int nextn=0;
			while((s = br.readLine()) != null) {
			//int k = 0;
			//while(((s = br.readLine()) != null) && (k++<2000)) {
				String [] ss = s.split(" ");
				if(ss.length>30) {	
					for(int i=0; i<ss.length; i++) {
						if(ss[i].equals("initial")) {
							x = (Double.parseDouble(ss[i+3].split(":")[0])/9000.)-4.44945216178894;
							y = (Double.parseDouble(ss[i+3].split(":")[1])/9000.)+48.389923740704795;
							//System.out.println("("+x+", "+y+")");
							DeviceList.add(new StdSensorNode(y,x,20,50,-1));
							Layer.getMapViewer().repaint();
						}
					}
				}				
				if(ss.length>20) {
					for(int i=0; i<ss.length; i++) {
						if(ss[i].equals("hop")) {
							if(b) {
								b=false;
								StringTokenizer st = new StringTokenizer(ss[i-16],".[]");
								st.nextToken();
								st.nextToken();
								v = st.nextToken();
								fos.write(v);
								//System.out.println(ss[i-16]);
							}
							nextn = Integer.parseInt(ss[i+1].split(",")[0]);
							//System.out.println(nextn);
							Device d = Layer.getDeviceList().get(nextn);
							d.setMarked(true);
							Layer.getMapViewer().repaint();
							Thread.sleep(500);								
							fos.write(" -> "+nextn+";\n"+nextn);
						}
					}
				}
			}
			fos.write(" -> "+v+";\n}");
			fos.close();
			fr.close();
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
