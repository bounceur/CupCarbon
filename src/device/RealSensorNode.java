package device;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import battery.Battery;
import buildings.BuildingList;
import project.Project;
import sensorunit.StdSensorUnit;
import utilities.MapCalc;

public class RealSensorNode extends SensorNode {
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public RealSensorNode() {
		super();
		sensorUnit = new StdSensorUnit(this.longitude, this.latitude, this.elevation, this);
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
	public RealSensorNode(double x, double y, double z, double radius, double radioRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new StdSensorUnit(this.longitude, this.latitude, this.elevation, this);		
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
	public RealSensorNode(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new StdSensorUnit(this.longitude, this.latitude, this.elevation, suRadius, this);
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
	public RealSensorNode(double x, double y, double z, double radius, double radioRadius, double suRadius, String[][] sb, int id) {
		this(x, y, z, radius, radioRadius, suRadius, id);
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
	public RealSensorNode(String id, String x, String y, String z, String radius, String radioRadius, String suRadius, String gpsFileName, String scriptFileName) {
		this(x, y, z, radius, radioRadius, suRadius, Integer.valueOf(id));
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
	public RealSensorNode(String x, String y, String z, String radius, String radioRadius, String suRadius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), Double.valueOf(radioRadius), id);
		sensorUnit = new StdSensorUnit(this.longitude, this.latitude, this.elevation, Double.valueOf(suRadius), this);

	}
	
	@Override
	public void drawSensorUnit(Graphics g) {
		int [] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int x = coord[0];
		int y = coord[1];
		if(hide == 0 || hide == 4) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 0, isSensorDetecting(), detectBuildings());
		}

		if(hide == 3) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 1, isSensorDetecting(), detectBuildings());
		}
	}

	public double getSensorUnitRadius() {
		return sensorUnit.getRadius();
	}	
	
	@Override
	public Device clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) battery.clone();
		RealSensorNode newSensor = (RealSensorNode) super.clone();
		StdSensorUnit newSensorUnit = new StdSensorUnit(sensorUnit.getLongitude(), sensorUnit.getLatitude(), sensorUnit.getElevation(), sensorUnit.getRadius(), newSensor);
		newSensor.setSensorUnit(newSensorUnit);
		newSensor.setBattery(newBattery);
		return newSensor;
	}

//	public double getSensorValue() {
//		for(SensorNode d : DeviceList.sensors) {
//			if(detect(d) && this!=d) return d.getValue();
//		}
//		return 0.0 ;
//	}
//	
//	public String getSensorValues() {
//		String s = "";
//		boolean first = true; 
//		for(SensorNode d : DeviceList.sensors) {
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

	@Override
	public String getIdFL() {
		return "S";
	}
	
	@Override
	public int getType() {
		return Device.SENSOR;
	}

	@Override
	public void initBattery() {
		getBattery().init();
	}

	@Override
	public String getParamsStr() {
		return "";
	}

	public boolean detectBuildings() {		
		return BuildingList.intersect(sensorUnit.getPoly());
	}
	
	@Override
	public SensorNode createNewWithTheSameType() {
		return new RealSensorNode(longitude, latitude, elevation, radius, 0.0, sensorUnit.getRadius(), DeviceList.number++);
	}
	
	@Override
	public void save(String ref) {
		String fileName = Project.getProjectNodePath();
		try {
			PrintStream fos = null;	
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + "sensor_" + ref));
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
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		saveRadioModule(Project.getProjectRadioPath() + File.separator + "sensor_"+ref);
	}

	public double getNextValueTime() {return Double.MAX_VALUE;}
	public void generateNextValue() {}
}
