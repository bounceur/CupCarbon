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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.JOptionPane;

import project.Project;
import device.Device;
import device.DeviceList;
import device.StdSensorNode;
import device.SensorNode;

public class OmnetPp {

	public static void omnetFileGeneration() {
		try {
			List<Device> nodes = DeviceList.getNodes();
			SensorNode sensoRef = new StdSensorNode(84, -212, 0, 10, 10, -1);
			SensorNode sensoRef2 = new StdSensorNode(49, -5, 0, 10, 10, -1);
			SensorNode santander = new StdSensorNode(43.499880816365234, -3.8828086853027344, 0, 10, 10, -1);
			PrintStream psgps = new PrintStream(new FileOutputStream(
					Project.projectPath + "/omnet/" + "omnetGPS" + ".txt"));
			PrintStream ps = new PrintStream(new FileOutputStream(
					Project.projectPath + "/omnet/" + "omnet_p84,_m212"
							+ ".txt"));
			PrintStream ps2 = new PrintStream(new FileOutputStream(
					Project.projectPath + "/omnet/" + "omnet_p49,_m5" + ".txt"));
			PrintStream ps3 = new PrintStream(new FileOutputStream(
					Project.projectPath + "/omnet/" + "omnet_p43,_m3" + ".txt"));
			Device node;
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				psgps.println("SN.node[" + i + "].xCoor = " + node.getLongitude());
				psgps.println("SN.node[" + i + "].yCoor = " + node.getLatitude());
				psgps.println("SN.node[" + i + "].zCoor = 0");
				psgps.println();
			}
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				ps.println("SN.node[" + i + "].xCoor = "
						+ sensoRef.distanceX(node));
				ps.println("SN.node[" + i + "].yCoor = "
						+ sensoRef.distanceY(node));
				ps.println("SN.node[" + i + "].zCoor = 0");
				ps.println();
			}
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				ps2.println("SN.node[" + i + "].xCoor = "
						+ sensoRef2.distanceX(node));
				ps2.println("SN.node[" + i + "].yCoor = "
						+ sensoRef2.distanceY(node));
				ps2.println("SN.node[" + i + "].zCoor = 0");
				ps2.println();
			}
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				ps3.println("SN.node[" + i + "].xCoor = "
						+ santander.distanceX(node));
				ps3.println("SN.node[" + i + "].yCoor = "
						+ santander.distanceY(node));
				ps3.println("SN.node[" + i + "].zCoor = 0");
				ps3.println();
			}
			psgps.close();
			ps.close();
			ps2.close();
			ps3.close();
			JOptionPane.showMessageDialog(null, "File saved !");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Create Project  !",
					"OMNeT++ Operation", JOptionPane.ERROR_MESSAGE);
		}
	}

}
