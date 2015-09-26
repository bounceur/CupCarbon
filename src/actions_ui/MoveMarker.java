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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import map.Layer;
import markers.Marker;
import markers.MarkerList;
import device.Device;

/**
 * @author Ahcene Bounceur
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class MoveMarker extends Actions {
	
	protected List<Device> temp = new ArrayList<Device>();
	
	public MoveMarker(Device sensor, String s) {
		super(sensor, s);
	}
	
	
	@Override
	public void exec() {
		Historic.add(this, false);
	}

	protected boolean checkSwap(){
		int x=0;
		boolean nbSwap = false;
		while(x+1<MarkerList.size())
		{
			if(MarkerList.getMarkers().get(x).getIdm() > MarkerList.getMarkers().get(x+1).getIdm())
			{
				Collections.swap(MarkerList.getMarkers(), x, x+1);
				nbSwap = true;
			}
			x++;
		}
		return nbSwap;
	}
	
	protected void undoMove(int i) {	
		MarkerList.delete(MarkerList.size()-i);
		Historic.remove();
		MarkerList.add((Marker)this.device);	
		while(checkSwap()){
			checkSwap();
		}
		Layer.getMapViewer().addMouseListener(this.device);
		Layer.getMapViewer().addMouseMotionListener(this.device);
		Layer.getMapViewer().addKeyListener(this.device);
		Layer.getMapViewer().repaint();
		device.setMove(false);		
	}

	
	protected void redoMove(int i) {
		MarkerList.delete(MarkerList.size()-i);
		MarkerList.add((Marker)this.device);
		while(checkSwap()){
			checkSwap();
		}
		Layer.getMapViewer().addMouseListener(this.device);
		Layer.getMapViewer().addMouseMotionListener(this.device);
		Layer.getMapViewer().addKeyListener(this.device);
		Layer.getMapViewer().repaint();
		Historic.add(null, true);
		device.setMove(false);		
	}
	
	@Override
	public void undo() {
		int i =1;
		boolean found = false;
		
		if(MarkerList.size()==1) {
			undoMove(1);
		}
		else {
			while(i<=MarkerList.size() && !found) {
				if(this.device.getIdm()==MarkerList.getMarkers().get(MarkerList.size()-i).getIdm()) {
					undoMove(i);
					found = true;
				}
			i=i+1;
			}
		}
		
	}
	
	@Override
	public void redo() {
		int i = 1;
		boolean found = false;		
		if(MarkerList.size()==1) {
			redoMove(1);
		}
		else {
			while(i<=MarkerList.size() && !found) {
				if(this.device.getIdm()==MarkerList.getMarkers().get(MarkerList.size()-i).getIdm()) {
					redoMove(i);
					found = true;
				}
			i=i+1;
			}
		}
		
	}
}