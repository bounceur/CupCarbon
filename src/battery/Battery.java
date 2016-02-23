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


/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Battery implements Cloneable {

	public double eMax = 9580*2;

	//private SensorUnit sensorUnit;
	private double level = eMax;

	/**
	 * Battery initialization
	 * 
	 * @param sensorUnit
	 */
//	public Battery(SensorUnit sensorUnit) {
//		this.sensorUnit = sensorUnit;
//	}

	/**
	 * @return the initial capacity of the battery
	 */
	public double getInitialLevel() {
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
	public void init(double eMax) {
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

	/**
	 * Connect to a Capture Unit
	 * 
	 * @param unitCapture
	 */
//	public void setSensorUnit(SensorUnit unitCapture) {
//		this.sensorUnit = unitCapture;
//	}

	@Override
	public Battery clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) super.clone();
		//newBattery.setSensorUnit(sensorUnit.clone());
		return newBattery;
	}
}
