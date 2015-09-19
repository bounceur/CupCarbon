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
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class MoveDevice extends Actions {
	protected int i = 1;
	protected boolean found = false;
	
	public MoveDevice(Device sensor, String s) {
		super(sensor, s);
	}
	
	
	@Override
	public void exec() {
		Historic.add(this, false);
	}

	protected void undoMove(int i)
	{
		DeviceList.delete(DeviceList.size()-i);
		Historic.remove();
		DeviceList.add(this.device);				
		Layer.getMapViewer().addMouseListener(this.device);
		Layer.getMapViewer().addMouseMotionListener(this.device);
		Layer.getMapViewer().addKeyListener(this.device);
		Layer.getMapViewer().repaint();
		device.setMove(false);
	}
	
	protected void redoMove(int i)
	{
		DeviceList.delete(DeviceList.size()-i);
		Historic.add(null, true);
		DeviceList.add(this.device);
		Layer.getMapViewer().addMouseListener(this.device);
		Layer.getMapViewer().addMouseMotionListener(this.device);
		Layer.getMapViewer().addKeyListener(this.device);
		Layer.getMapViewer().repaint();
		device.setMove(false);
		
	}
	
	@Override
	public void undo() {
		i =1;
		found = false;
		
		if(DeviceList.size()==1)
		{
		undoMove(1);
		}
		else
		{
			while(i<=DeviceList.size() && !found)
			{
				if(this.device.getId()==DeviceList.getNodes().get(DeviceList.size()-i).getId())
				{
					undoMove(i);
					found = true;
				}
			i=i+1;
			}
		}
		
	}
	
	@Override
	public void redo(){
		
		
		i = 1;
		found = false;
		
		if(DeviceList.size()==1)
		{
		redoMove(1);
		}
		else
		{
			while(i<=DeviceList.size() && !found)
			{
				if(this.device.getId()==DeviceList.getNodes().get(DeviceList.size()-i).getId())
				{
					redoMove(i);
					found = true;
				}
			i=i+1;
			}
		}
		
	}
}