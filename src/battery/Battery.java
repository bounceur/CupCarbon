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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import weather.FileManager;
import weather.WeatherScenario;
import wisen_simulation.WisenSimulation;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @author Nabil Kadjouh
 * @version 2.8.3 (U-One)
 */

public class Battery implements Cloneable {

	public double eMax = 9580*2;

	//private SensorUnit sensorUnit;
	private double level = eMax;
	
//------------------------------------------------------------------------------------------KADJOUH
	private BatteryModel bm = null;
	
	public BatteryModel getBatteryModel() {
		return bm;
	}

	public void setBatteryModel(BatteryModel bm) {
		this.bm = bm;
	}
	public static void loadBatteryModelFromFile(String batteryModelFile,BatteryModel bm)
	//String modelName, int capacity, double tension, String model)
    {
    	String ErrorMsg ="";
		FileInputStream fis;
		BufferedReader b = null;
		String s="*/*/*/*";// initialization
		String[] mdl;
		try {
			if (!batteryModelFile.equals("")) 
				{
					if ( FileManager.fileExists (batteryModelFile))						
					{
						fis = new FileInputStream(batteryModelFile);				
						b = new BufferedReader(new InputStreamReader(fis));			
						if ((s = b.readLine()) != null) {bm.setModelName(s);}
						if ((s = b.readLine()) != null) 
					    	{
							mdl = s.trim().split(" ");
							bm.setCapacity(Integer.parseInt(mdl[0]));	
							}
						if ((s = b.readLine()) != null) 
				    		{
						   mdl = s.trim().split(" ");
						   bm.setTension(Double.parseDouble(mdl[0]));	
				    		}
						if ((s = b.readLine()) != null) {bm.setDischargeCurrentModel(s);}
							
						b.close();
						fis.close();							
					}	else ErrorMsg += "\n#battery model file doesn't exist";
				} else  ErrorMsg += "\n#battery model file  is Empty (not selected)! ";
				
				} catch (Exception e) {
			
					e.printStackTrace(); 										
					} 
					
			if (ErrorMsg !="" ) JOptionPane.showMessageDialog(null, ErrorMsg,"CupCarbon", JOptionPane.INFORMATION_MESSAGE);	
}
	
//------------------------------------------------------------------------------------------KADJOUH

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
//------------------------------------------------------------------------------------------KADJOUH	
	public void consume(double v) {
		
		if (WisenSimulation.weather)
		{
			double currentTemperature = WeatherScenario.getCurrentTemperature();
			int capacity = getBatteryModel().getCapacity();
			double tension = getBatteryModel().getTension();
			String dischargeCurrentModel =  getBatteryModel().getDischargeCurrentModel();
			double tt1o = 0.0039; // time to transfert one octet in zigBee transmission
			
			double dischargeCurrent = 5;
			try {
				dischargeCurrent = BatteryModel.getCurrentFromModel(currentTemperature, tension, dischargeCurrentModel );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			capacity = (int)(eMax);
			double lc = BatteryModel.levelConsumption(dischargeCurrent, capacity, tt1o);
			lc = lc*100/lc;
					
			level = level-( v*lc);
			if (level < 0)
				level = 0;
		}
		else 
		{	
		
		
		level -= v;
		if (level < 0)
			level = 0;
		}
		
	}
//------------------------------------------------------------------------------------------KADJOUH	
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
