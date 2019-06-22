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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import battery.Battery;
import buildings.BuildingList;
import project.Project;
import sensorunit.DirectionalSensorUnit;
import sensorunit.SensorUnit;
import utilities.MapCalc;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class DirectionalSensorNode extends SensorNode {
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public DirectionalSensorNode() {
		super();
		sensorUnit = new DirectionalSensorUnit(this.longitude, this.latitude, this.elevation, this);		
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
	public DirectionalSensorNode(double x, double y, double z, double radius, double radioRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new DirectionalSensorUnit(this.longitude, this.latitude, this.elevation, this);		
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
	public DirectionalSensorNode(double x, double y, double z, double radius, double radioRadius,
			double suRadius, int id, double deg, double dec, int n) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new DirectionalSensorUnit(this.longitude, this.latitude, this.elevation, suRadius, deg, dec, n, this);
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
	public DirectionalSensorNode(double x, double y, double z, double radius, double radioRadius, double suRadius, String[][] sb, int id, double deg, double dec, int n) {
		this(x, y, z, radius, radioRadius, suRadius, id, deg, dec, n);
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
	public DirectionalSensorNode(String id, String x, String y, String z, String radius, String radioRadius,
			String suRadius, String gpsFileName, String scriptFileName, String degS, String decS, String nS) {
		this(x, y, z, radius, radioRadius, suRadius, Integer.valueOf(id), Double.valueOf(degS), Double.valueOf(decS), Integer.valueOf(nS));
//		String [] srd = rdInfos.split("#");
//		this.getCurrentRadioModule().setMy(Integer.valueOf(srd[0]));
//		this.getCurrentRadioModule().setCh(Integer.valueOf(srd[1]));
//		this.getCurrentRadioModule().setNId(Integer.valueOf(srd[2]));
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
	public DirectionalSensorNode(String x, String y, String z, String radius, String radioRadius, String suRadius, int id, double deg, double dec, int n) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), Double.valueOf(radioRadius), id);
		sensorUnit = new DirectionalSensorUnit(this.longitude, this.latitude, this.elevation, Double.valueOf(suRadius), deg, dec, n, this);
	}
	
//	@Override
//	public void setSensorUnitRadius(double captureRadio) {
//		sensorUnit.setRadius(captureRadio);
//	}
	
	@Override
	public void drawSensorUnit(Graphics g) {
		int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int x = coord[0];
		int y = coord[1];
		//if(hide == 0 || hide == 1) {
		if(hide == 0 || hide == 4) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 0, isSensorDetecting(), detectBuildings());
		}

		if(hide == 3) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 1, isSensorDetecting(), detectBuildings());
		}
	}
	
	@Override
	public SensorNode clone() throws CloneNotSupportedException {
		SensorNode newSensor = (SensorNode) super.clone();
		SensorUnit newCaptureUnit = (SensorUnit) sensorUnit.clone();
		Battery newBattery = (Battery) battery.clone();
		((DirectionalSensorNode)newSensor).setSensorUnit(newCaptureUnit);
		newCaptureUnit.setNode(newSensor);
		newSensor.setBattery(newBattery);
		return newSensor;
	}
	
//	public double getSensorValue() {
//		for(Device d : DeviceList.sensors) {
//			if(detect(d) && this!=d) return d.getValue();
//		}
//		return 0.0 ;
//	}
//	
//	public String getSensorValues() {
//		String s = "";
//		boolean first = true; 
//		for(Device d : DeviceList.sensors) {
//			if(detect(d) && this!=d) {
//				if (!first) {
//					s+="#";
//				}
//				s += d.getId()+"#"+d.getValue();
//				first = false;
//			}
//		}
//		return s ;
//	}

	public double getSensorUnitCoverage() {
		return ((DirectionalSensorUnit) sensorUnit).getCoverage();
	}
	
	public double getSensorUnitDirection() {
		return ((DirectionalSensorUnit) sensorUnit).getDirection();
	}
	
	public int getSensorUnitN() {
		return ((DirectionalSensorUnit) sensorUnit).getN();
	}

	public void setSensorUnitAngle(double angle) {
		((DirectionalSensorUnit) sensorUnit).setCoverage(angle);
	}
	
	public void setSensorUnitDirection(double direction) {
		((DirectionalSensorUnit) sensorUnit).setDirection(direction);
	}

	public void setSensorUnitRadius(double radius) {
		((DirectionalSensorUnit) sensorUnit).setRadius(radius);
	}

	@Override
	public String getIdFL() {
		return "MS";
	}
	
	@Override
	public int getType() {
		return Device.DIRECTIONAL_SENSOR;
	}

	@Override
	public void initBattery() {
		getBattery().init();
	}
	
	@Override
	public String getParamsStr() {
		return getSensorUnitCoverage()+ " " + getSensorUnitDirection()+ " " + getSensorUnitN();
	}
	
	public boolean detectBuildings() {		
		return BuildingList.intersect(sensorUnit.getPoly());
	}
	
	@Override
	public double getSensorUnitRadius() {
		return sensorUnit.getRadius();
	}
	
	@Override	
	public SensorNode createNewWithTheSameType() {
		DirectionalSensorNode n_msn = new DirectionalSensorNode(longitude, latitude, elevation, radius, 0.0, DeviceList.number++);
		DirectionalSensorUnit c_msu = (DirectionalSensorUnit) this.getSensorUnit(); 
		DirectionalSensorUnit n_msu = (DirectionalSensorUnit) n_msn.getSensorUnit();
		
		n_msu.setDirection(c_msu.getDirection());
		n_msu.setCoverage(c_msu.getCoverage());
		n_msu.setRadius(c_msu.getRadius());
		return n_msn;
	}
	
	@Override
	public void save(String ref) {
		String fileName = Project.getProjectNodePath();
		try {
			PrintStream fos = null;	
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + "directionalsensor_" + ref));
			fos.println("List of parameters");
			fos.println("------------------------------------------");
			fos.println("device_type:" + getType());
			fos.println("device_id:" + getId());
			fos.println("device_longitude:" + getLongitude());
			fos.println("device_latitude:" + getLatitude());
			fos.println("device_elevation:" + getElevation());
			fos.println("device_radius:" + getRadius());
			fos.println("device_hide:" + getHide());
			fos.println("device_draw_battery:" + getDrawBatteryLevel());
			fos.println("device_sensor_unit_radius:" + getSensorUnitRadius());			
			if (!getGPSFileName().equals(""))
				fos.println("device_gps_file_name:" + getGPSFileName());
			if (!getScriptFileName().equals(""))
				fos.println("device_script_file_name:" + getScriptFileName());
			if (getType() == Device.DIRECTIONAL_SENSOR)
				fos.println("directional_parameters:" + ((DirectionalSensorNode) this).getParamsStr());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		saveRadioModule(Project.getProjectRadioPath() + File.separator + "directionalsensor_"+ref);
	}

	public double getNextValueTime() {return Double.MAX_VALUE;}
	public void generateNextValue() {}

}