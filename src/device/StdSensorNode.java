package device;

import java.awt.Graphics;

import battery.Battery;
import sensorunit.SensorUnit;
import utilities.MapCalc;

public class StdSensorNode extends SensorNode {
	
	protected SensorUnit sensorUnit;
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public StdSensorNode() {
		super();
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this.elevation, this);		
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
	public StdSensorNode(double x, double y, double z, double radius, double radioRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this.elevation, this);		
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
	public StdSensorNode(double x, double y, double z, double radius, double radioRadius,
			double suRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this.elevation, suRadius, this);
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
	public StdSensorNode(double x, double y, double z, double radius, double radioRadius, double suRadius, String[][] sb, int id) {
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
	public StdSensorNode(String id, String rdInfos, String x, String y, String z, String radius, String radioRadius,
			String suRadius, String gpsFileName, String scriptFileName) {
		this(x, y, z, radius, radioRadius, suRadius, Integer.valueOf(id));
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
	public StdSensorNode(String x, String y, String z, String radius, String radioRadius, String suRadius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), Double.valueOf(radioRadius), id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this.elevation, Double.valueOf(suRadius), this);
	}
	
	public StdSensorNode(SensorNode sensorNode) {
		super(sensorNode);
	}

	@Override
	public void setSensorUnitRadius(double captureRadio) {
		sensorUnit.setRadius(captureRadio);
	}
	
	@Override
	public void drawSensorUnit(Graphics g) {
		int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int x = coord[0];
		int y = coord[1];
		if(hide == 0 || hide == 4) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 0, isSensorDetecting());
		}

		if(hide == 3) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 1, isSensorDetecting());
		}
	}

	@Override
	public double getSensorUnitRadius() {
		return sensorUnit.getRadius();
	}
	
	/**
	 * Set the capture unit
	 * 
	 * @param sensorUnit
	 */
	public void setSensorUnit(SensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit;
	}
	
	@Override
	public Device clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) battery.clone();
		StdSensorNode newSensor = (StdSensorNode) super.clone();
		SensorUnit newSensorUnit = new SensorUnit(sensorUnit.getLongitude(), sensorUnit.getLatitude(), sensorUnit.getElevation(), sensorUnit.getRadius(), newSensor);
		newSensor.setSensorUnit(newSensorUnit);
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

}
