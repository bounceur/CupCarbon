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
	
	//private UControl uProc = new UControl();
	//private RadioModule radioModule = new RadioModule(0);
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
		// return (int)(capacite/capaciteDeBase*100.) ;
		return (int) (eMax);
	}
	
	/**
	 * @return the capacity of the battery
	 */
	public int getLevel() {
		// return (int)(capacite/capaciteDeBase*100.) ;
		return level;
	}
	
	/**
	 * @return the capacity of the battery (in percent)
	 */
	public int getLevelInPercent() {
		return (int) (level * 1.0 / eMax * 100.);
		//return (int) (capacity);
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
	 * Consume 1 unit of the battery
	 */
	/*
	public void consume() {
		double vUp = 0;
		double vA = 0;
		double vUc = 0;

		if (uProc != null)
			vUp = uProc.getConsumedUnit();
		if (radioModule != null)
			vA = radioModule.getConsumedUnit();
		if (sensorUnit != null)
			vUc = sensorUnit.getConsumedUnit();

		double v = vUp + vA + vUc;
		if (level >= v)
			level -= v;
		else if (level > 0)
			level = 0;
	}*/

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
	 * Consume the battery by taking into account the consumptions of the
	 * elements that are connected to the battery: - Microcontroller - RadioModule -
	 * Capture unit
	 * 
	 * @param unit1
	 * @param unit2
	 * @param unit3
	 */
	/*
	public void consume(double unit1, double unit2, double unit3) {
		double vUp = 0;
		double vA = 0;
		double vUc = 0;

		if (uProc != null)
			vUp = uProc.getConsumedUnit(unit1);
		if (radioModule != null)
			vA = radioModule.getConsumedUnit(unit2);
		if (sensorUnit != null)
			vUc = sensorUnit.getConsumedUnit(unit3);

		double v = vUp + vA + vUc;
		if (level >= v)
			level -= v;
		else if (level > 0)
			level = 0;
	}
	 */

	/**
	 * @return if the battery is empty
	 */
	public boolean empty() {
		//return (capacity <= (30. * eMax / 100.));
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

	/**
	 * Connect to a Microcontroller
	 * 
	 * @param uProc
	 */
	/*
	public void setUControl(UControl uProc) {
		this.uProc = uProc;
	}
	 */
	
	/**
	 * Connect to a Antanna
	 * 
	 * @param radioModule
	 */	
	/*
	public void setAntenna(RadioModule radioModule) {
		this.radioModule = radioModule;
	}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Battery clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) super.clone();
		newBattery.setSensorUnit(sensorUnit.clone());
		//newBattery.setUControl((UControl) uProc.clone());
		//newBattery.setAntenna((RadioModule) radioModule.clone());
		return newBattery;
	}
}
