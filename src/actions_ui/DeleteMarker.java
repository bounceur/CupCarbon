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

import map.Layer;
import device.Marker;
import device.MarkerList;

/**
 * @author Ahcene Bounceur
 * @author Tangy Clech
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class DeleteMarker extends Actions {

	public DeleteMarker(Marker marker, String typeName) {
		super(marker, typeName);
	}

	@Override
	public void exec() {
		Historic.add(this, false);
	}

	@Override
	public void undo() {
		MarkerList.add((Marker)this.device);
		Layer.getMapViewer().addMouseListener(this.device);
		Layer.getMapViewer().addMouseMotionListener(this.device);
		Layer.getMapViewer().addKeyListener(this.device);
		Historic.remove();
		Layer.getMapViewer().repaint();
	}
	
	@Override
	public void redo(){
		MarkerList.delete(MarkerList.size()-1);
		Historic.add(this,true);
		Layer.getMapViewer().repaint();
	}
	
}
