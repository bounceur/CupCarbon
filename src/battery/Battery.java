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

import sensorunit.SensorUnit;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Battery implements Cloneable {

	public static int eMax = 100000000;

	private SensorUnit sensorUnit;
	private int level = eMax;

	/**
	 * Battery initialization
	 * 
	 * @param sensorUnit
	 */
	public Battery(SensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit;
	}

	/**
	 * @return the initial capacity of the battery
	 */
	public long getInitialLevel() {
		return (int) (eMax);
	}
	
	/**
	 * @return the capacity of the battery
	 */
	public int getLevel() {
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
	public void setLevel(int capacity) {
		this.level = capacity;
	}

	/**
	 * @param eMax
	 * Initialization of the battery (energy max given)
	 */
	public void init(int eMax) {
		Battery.eMax = eMax;
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

	/**
	 * @return if the battery is empty
	 */
	public boolean empty() {
		return (level <= 0);
	}

	/**
	 * Connect to a Capture Unit
	 * 
	 * @param unitCapture
	 */
	public void setSensorUnit(SensorUnit unitCapture) {
		this.sensorUnit = unitCapture;
	}

	@Override
	public Battery clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) super.clone();
		newBattery.setSensorUnit(sensorUnit.clone());
		return newBattery;
	}
}
