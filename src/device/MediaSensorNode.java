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

package device;

import java.awt.Graphics;

import sensorunit.MediaSensorUnit;
import utilities.MapCalc;
import battery.Battery;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class MediaSensorNode extends SensorNode {

	protected MediaSensorUnit sensorUnit;
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public MediaSensorNode() {
		super();
		sensorUnit = new MediaSensorUnit(this.longitude, this.latitude, this);		
	}

	/**
	 * Constructor 2
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 */
	public MediaSensorNode(double x, double y, double radius, double radioRadius, int id) {
		super(x, y, radius, radioRadius, id);
		sensorUnit = new MediaSensorUnit(this.longitude, this.latitude, this);		
	}

	/**
	 * Constructor 3
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public MediaSensorNode(double x, double y, double radius, double radioRadius,
			double suRadius, int id, double deg, double dec, int n) {
		super(x, y, radius, radioRadius, id);
		sensorUnit = new MediaSensorUnit(this.longitude, this.latitude, suRadius, deg, dec, n, this);
	}
	
	/**
	 * Constructor 4
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param sb
	 *            A two dimensional table that contains a set of informations
	 *            about the sensor (temperature, co2, etc.) The first column
	 *            contains the name of the parameter The second column contains
	 *            the value of the corresponding parameter
	 */
	public MediaSensorNode(double x, double y, double radius, double radioRadius, double suRadius, String[][] sb, int id, double deg, double dec, int n) {
		this(x, y, radius, radioRadius, suRadius, id, deg, dec, n);
		this.setInfos(sb);
		initBuffer();
	}
	
	/**
	 * Constructor 6
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param gpsFileName
	 *            The path of the GPS file
	 * @param scriptFileName
	 *            The path of the script file
	 */
	public MediaSensorNode(String id, String rdInfos, String x, String y, String radius, String radioRadius,
			String suRadius, String gpsFileName, String scriptFileName, String degS, String decS, String nS) {
		this(x, y, radius, radioRadius, suRadius, Integer.valueOf(id), Double.valueOf(degS), Double.valueOf(decS), Integer.valueOf(nS));
		String [] srd = rdInfos.split("#");
		my = Integer.valueOf(srd[0]);
		ch = Integer.valueOf(srd[1]);
		nId = Integer.valueOf(srd[2]);
		gpsFileName = (gpsFileName.equals("#") ? "" : gpsFileName);
		scriptFileName = (scriptFileName.equals("#") ? "" : scriptFileName);
		setGPSFileName(gpsFileName);
		setScriptFileName(scriptFileName);
		calculateRadioSpace();
		initBuffer();
	}
	
	/**
	 * Constructor 5 the same as the Constructor 3 with "String" argument
	 * instead of "double"
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param suRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public MediaSensorNode(String x, String y, String radius, String radioRadius, String suRadius, int id, double deg, double dec, int n) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius), Double.valueOf(radioRadius), id);
		sensorUnit = new MediaSensorUnit(this.longitude, this.latitude, Double.valueOf(suRadius), deg, dec, n, this);
	}
	
//	@Override
//	public void setSensorUnitRadius(double captureRadio) {
//		sensorUnit.setRadius(captureRadio);
//	}
	
	@Override
	public void drawSensorUnit(Graphics g) {
		int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
		int x = coord[0];
		int y = coord[1];
		if(hide == 0 || hide == 1) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 0, isSensorDetecting());
		}

		if(hide == 2) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 1, isSensorDetecting());
		}
	}
	
	/**
	 * Set the capture unit
	 * 
	 * @param sensorUnit
	 */
	public void setSensorUnit(MediaSensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit;
	}
	
	@Override
	public MediaSensorNode clone() throws CloneNotSupportedException {
		MediaSensorNode newSensor = (MediaSensorNode) super.clone();
		MediaSensorUnit newCaptureUnit = (MediaSensorUnit) sensorUnit.clone();
		Battery newBattery = (Battery) battery.clone();
		newSensor.setSensorUnit(newCaptureUnit);
		newCaptureUnit.setNode(newSensor);
		newSensor.setBattery(newBattery);
		return newSensor;
	}
	
	/**
	 * @param device
	 * @return if a device is in the sensor unit area of the current device
	 */
	public boolean detect(Device device) {		
		return (sensorUnit.detect(device));
	}
	
	public boolean isSensorDetecting() {
		for(Device d : DeviceList.getNodes()) {
			if(detect(d) && this!=d) return true;
		}
		return false ;
	}
	
	public double getSensorValue() {
		for(Device d : DeviceList.getNodes()) {
			if(detect(d) && this!=d) return d.getValue();
		}
		return 0.0 ;
	}
	
	public String getSensorValues() {
		String s = "";
		boolean first = true; 
		for(Device d : DeviceList.getNodes()) {
			if(detect(d) && this!=d) {
				if (!first) {
					s+="#";
				}
				s += d.getId()+"#"+d.getValue();
				first = false;
			}
		}
		return s ;
	}

	@Override
	public double getSensorUnitDeg() {
		return sensorUnit.getDeg();
	}
	
	@Override
	public double getSensorUnitDec() {
		return sensorUnit.getDec();
	}
	
	@Override
	public int getSensorUnitN() {
		return sensorUnit.getN();
	}

	@Override
	public double getSensorUnitRadius() {
		return sensorUnit.getRadius();
	}

	public void setSensorUnitDeg(double deg) {
		sensorUnit.setDeg(deg);
	}
	
	public void setSensorUnitDec(double dec) {
		sensorUnit.setDec(dec);
	}

	public void setSensorUnitRadius(double radius) {
		sensorUnit.setRadius(radius);
	}

	@Override
	public String getIdFL() {
		return "MS";
	}
	
	@Override
	public int getType() {
		return Device.MEDIA_SENSOR;
	}
}