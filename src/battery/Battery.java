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

package battery;

import java.awt.Color;
import java.awt.Graphics;

import device.SensorNode;
import map.MapLayer;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Battery implements Cloneable {

	public double eMax = 19160; // 9580*2 -> 2AA batteries
	private double level = eMax;
	private SensorNode sensorNode;

	public Battery(SensorNode sensorNode) {
		this.sensorNode = sensorNode;
	}
	
	/**
	 * @return the initial capacity of the battery
	 */
	public double getEMax() {
		return eMax;
	}
	
	/**
	 * @return the capacity of the battery
	 */
	public double getLevel() {
		return level;
	}
	
	/**
	 * @return the capacity of the battery (in percent)
	 */
	public int getLevelInPercent() {
		return (int) (level * 1.0 / eMax * 100.);
	}

	/**
	 * Set the value of the capacity of the battery
	 * 
	 * @param capacity 
	 * the capacity of the battry
	 */
	public void setLevel(double capacity) {
		this.level = capacity;
	}

	/**
	 * @param eMax
	 * Initialization of the battery (energy max given)
	 */
	public void setEMax(double eMax) {
		this.eMax = eMax;
		level = eMax;
	}
	
	/**
	 * Initialization of the battery (energy max)
	 */
	public void init() {
		level = eMax;
	}

	/**
	 * Consume v units of the battery
	 * 
	 * @param v
	 *            Number of the units to consume
	 */
	public void consume(double v) {
		level -= v;
		if (level < 0)
			level = 0;
	}
	
	public double getBatteryConsumption() {
		return eMax - level;
	}

	/**
	 * @return if the battery is empty
	 */
	public boolean empty() {
		return (level <= 0);
	}

	@Override
	public Battery clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) super.clone();
		return newBattery;
	}
	
	public void draw(Graphics g, int x, int y) {
		g.setColor(UColor.WHITE_LTRANSPARENT);
		g.fillRect(x-30, y-25, 6, 50);
		g.setColor(UColor.GREEN);
		if (getLevel()/getEMax()<0.5) g.setColor(UColor.ORANGE);
		if (getLevel()/getEMax()<0.2) g.setColor(UColor.RED);
		g.fillRect(x-30, y-(int)(getLevel()/getEMax()*100./2.)+25, 6, (int)(getLevel()/getEMax()*100./2.));
		g.setColor(Color.DARK_GRAY);
		if(MapLayer.dark) g.setColor(Color.WHITE);
		g.drawRect(x-30, y-25, 6, 50);
		g.drawString("Battery"+sensorNode.getId()+": " + (int)getLevel(), x-30, y+35);		
	}
}
