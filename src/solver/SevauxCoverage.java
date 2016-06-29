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

package solver;

import java.io.BufferedReader;
import java.io.FileReader;

import device.Device;
import device.DeviceList;
import map.MapLayer;

public class SevauxCoverage implements Runnable {

	@Override
	public void run() {
		DeviceList nodeList = MapLayer.getDeviceList();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("solution.txt"));
			String str;
			String[] strT;

			while ((str = br.readLine()) != null) {
				DeviceList.setAlgoSelect(false);

				double v1 = Double.parseDouble(str.split(" ")[1]);
				// System.out.println(v1);
				str = br.readLine();
				strT = str.split(" ");
				for (String v : strT) {
					nodeList.get(Integer.parseInt(v)).setMarked(true);
					nodeList.get(Integer.parseInt(v)).consumeTx(v1 * 10000);
					// System.out.println(v);
					// setAlgoSelect
				}
				MapLayer.getMapViewer().repaint();

				// Thread.sleep((long) (v1*10000));
				Thread.sleep((long) (1000));
			}
			DeviceList.setAlgoSelect(false);
			for (int i = 0; i < DeviceList.size(); i++) {
				if (nodeList.get(i).getType() == Device.SENSOR)
					System.out.println(i + ": "
							+ nodeList.get(i).getBatteryLevel());
			}

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
