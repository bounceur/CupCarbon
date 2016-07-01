/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2015 CupCarbon
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

package actions_ui;

import map.MapLayer;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Tangy Clech
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class AddDevice extends Actions {

	public AddDevice(Device sensor, String typeName) {
		super(sensor, typeName);
	}

	@Override
	public void exec() {
		Historic.add(this, false);
	}

	@Override
	public void undo() {
		DeviceList.delete(DeviceList.size()-1);
		Historic.remove();
		MapLayer.getMapViewer().repaint();
	}
	
	@Override
	public void redo(){
		DeviceList.add(this.device);
		MapLayer.getMapViewer().addMouseListener(this.device);
		MapLayer.getMapViewer().addMouseMotionListener(this.device);
		MapLayer.getMapViewer().addKeyListener(this.device);
		
		MapLayer.getMapViewer().repaint();
		Historic.add(null, true);
	}
}
