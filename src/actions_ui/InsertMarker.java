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
import markers.Marker;
import markers.MarkerList;

/**
 * @author Ahcene Bounceur
 * @author Tangy Clech
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class InsertMarker extends Actions {

	
	public InsertMarker(Marker marker, String typeName) {
		super(marker, typeName);
	}

	@Override
	public void exec() {
		Historic.add(this, false);
	}

	@Override
	public void undo() {
		System.out.println("Undo");
		for(int i=0; i<MarkerList.size(); i++){
			if(MarkerList.get(i).getId() == this.device.getId()){
				MarkerList.delete(i);
				MapLayer.getMapViewer().repaint();
				Historic.remove();
				break;
			}
		}
	}
	
	@Override
	public void redo(){
		System.out.println("Redo");
		Marker marker = new Marker(this.device.getLongitude()+"", this.device.getLatitude()+"", this.device.getElevation()+"", this.device.getRadius()+"");
		MarkerList.add(marker);
		MapLayer.getMapViewer().repaint();
		Historic.add(this, true);
		
	}
}
