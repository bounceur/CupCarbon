/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Pattern;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import actions_ui.MoveDevice;
import battery.Battery;
import battery.BatteryModel;
import cupcarbon.DeviceParametersWindow;
import cupcarbon.RadioParametersWindow;
import geo_objects.GeoZoneList;
import map.MapLayer;
import markers.Marker;
import markers.MarkerList;
import radio_module.Standard;
import script.Script;
import utilities.MapCalc;
import utilities.UColor;
import utilities._Constantes;
import visualisation.Visualisation;
import weather.WeatherScenario;
import wisen_simulation.WisenSimulation;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @author Nabil Kadjouh
 */
public abstract class Device
		implements Runnable, MouseListener, MouseMotionListener, KeyListener, _Constantes, Cloneable {

	public static final int TARGET = 0;
	public static final int SENSOR = 1;
	public static final int GAS = 2;
	public static final int FLYING_OBJECT = 3;
	public static final int BASE_STATION = 4;
	public static final int MEDIA_SENSOR = 5;
	public static final int MOBILE = 6;
	public static final int MOBILE_WR = 7;
	public static final int MARKER = 8;
	public static final int VERTEX = 9;
	public static final int BUILDING = 10;
	public static final int GEOZONE = 11;

	public static final boolean DEAD = false;
	public static final boolean ALIVE = true;
	public static final boolean SLEEP = false;

	protected double sigmaOfDriftTime = 0.00003;
	protected double driftTime = 1.0;

	public static int moveSpeed = 100;

	protected boolean altDown = false;
	protected boolean shiftDown = false;
	protected boolean ctrlDown = false;
	protected int lastKeyCode = 0;

	protected int id = 0;
	protected int idm = 0;

	protected int my = 0;
	protected int pl = 100;
	protected int ch = 0x0;
	protected int nId = 0x3334;
	protected String userId = "";
	protected double timeToResend = 1.0;
	protected int numberOfSends = 3;

	protected long uartDataRate = 9600;
	protected int radioDataRate = 250000;
	protected int standard = Standard.ZIGBEE_802_15_4;

	protected double longitude, latitude, elevation;
	protected double longitude_ori;
	protected double latitude_ori;
	protected double elevation_ori;
	protected double dlongitude, dlatitude, delevation;
	protected double radius = 0;
	protected double radiusOri = 0;
	protected boolean selected = false;
	protected char key = 0;
	protected boolean move = false;
	protected boolean inside = false;
	protected boolean underSimulation = false;
	protected int hide = 0;
	protected boolean increaseNode = false;
	protected boolean reduceNode = false;
	protected boolean withRadio = false;
	protected boolean withSensor = false;
	protected boolean mobile = false;
	protected boolean displayRadius = false;
	protected boolean visited = false;
	protected boolean visible = true;
	protected boolean drawBatteryLevel = false;
	protected boolean drawTxRx = false;

	protected String[][] infos = { { "SENSOR: ", "" }, { "ID: ", "" }, { "MY: ", "" }, { "Network ID: ", "" },
			{ "Channel: ", "" }, { "Script: ", "" }, { "GPS: ", "" }, { "Battery: ", "" }, { "Model: ", "" }  };
	protected boolean displayInfos = false;

	// ------------------------------------------------------------------------------------------KADJOUH
	protected String batteryModelFileName = "";
	// ------------------------------------------------------------------------------------------KADJOUH

	protected String scriptFileName = "";
	protected String gpsFileName = "";
	protected Script script = null;
	protected String targetName = "";
	protected int ledColor = 0;

	protected double eTx = 0.0000592; // sending energy
	protected double eRx = 0.0000286; // receiving energy
	protected double eSlp = 0.0000001;// Sleeping energy
	protected double eL = 0.000001;// Listening energy
	protected double eS = 1; // sensing energy

	protected Color radioLinkColor = UColor.RED;
	protected Color ackLinkColor = Color.BLACK;

	protected boolean receiving = false;
	protected boolean sending = false;
	protected boolean writing = false;
	// protected boolean distanceMode = false ;
	protected long distanceModeDelay = 2000;

	protected GeoZoneList geoZoneList = null;

	protected boolean state = ALIVE;

	// ----------------------------------
	// For Algorithms
	// ----------------------------------
	protected boolean marked = false;
	protected double value = 0;
	protected LinkedList<Double> valueList;
	// ----------------------------------

	protected double event = Double.MAX_VALUE; // Event relied to actions =
												// sending/receiving
	protected double event2 = Double.MAX_VALUE; // Event relied to mobility
	// protected int nextEvent = Integer.MAX_VALUE; // Calculate the next Event
	// protected int mrEvent = Integer.MAX_VALUE; // Event relied to the message
	// reception

	protected Thread thread;

	protected String message = "";

	Random random = new Random();

	// ------

	/**
	 * Empty constructor
	 */
	public Device() {
	}

	/**
	 * Constructor with the 3 main parameters of a device
	 * 
	 * @param x
	 *            Longitude
	 * @param y
	 *            Latitude
	 * @param z
	 *            Elevation
	 * @param radius
	 *            Radius
	 */
	public Device(double x, double y, double z, double radius, int id) {
		if (id == -1)
			this.id = DeviceList.number++;
		else
			this.id = id;
		userId = "_" + id;
		this.longitude = x;
		this.latitude = y;
		this.elevation = z;
		this.radius = radius;
		radiusOri = radius;
		MapLayer.getMapViewer().addMouseListener(this);
		MapLayer.getMapViewer().addMouseMotionListener(this);
		MapLayer.getMapViewer().addKeyListener(this);
	}

	/**
	 * ID First Letter
	 * 
	 * @return the first letter of the identifier
	 */
	public abstract String getIdFL();

	/**
	 * @return the name of the identifier
	 */
	public abstract String getNodeIdName();

	/**
	 * @return the type of the device
	 */
	public abstract int getType();

	/**
	 * Draw the device
	 * 
	 * @param g
	 *            Graphics
	 */
	public abstract void draw(Graphics g);

	/**
	 * Set the radio radius of the device
	 * 
	 * @param radiuRadius
	 */
	public abstract void setRadioRadius(double radiuRadius);

	/**
	 * Set the sensor unit radius of the device
	 * 
	 * @param sensorRadius
	 */
	public abstract void setSensorUnitRadius(double sensorRadius);

	/**
	 * Set the GPS file path of the device
	 * 
	 * @param gpsFileName
	 */
	public void setGPSFileName(String gpsFileName) {
		if (gpsFileName.endsWith(".gps"))
			this.gpsFileName = gpsFileName;
		else
			this.gpsFileName = "";
	}

	/**
	 * @return the GPS file path
	 */
	public abstract String getGPSFileName();

	/**
	 * This method draw the sensor with another look in order to view if an
	 * algorithm has marked them or not
	 * 
	 * @param g
	 *            Graphics
	 */
	public void drawMarked(Graphics g) {
	}

	public void drawSensorUnit(Graphics g) {

	}

	public void drawRadioRange(Graphics g) {

	}

	/**
	 * Consume
	 * 
	 * @param v
	 */
	public void consume(int v) {
		getBattery().consume(v);
	}

	/**
	 * consumeTx
	 * 
	 * @param v
	 */
	// ------------------------------------------------------------------------------------------KADJOUH
	public void consumeTx(int v) {

		if (WisenSimulation.weather) {
			double currentTemperature = WeatherScenario.getCurrentTemperature();
			int capacity = this.getBattery().getBatteryModel().getCapacity();
			double tension = this.getBattery().getBatteryModel().getTension();
			String dischargeCurrentModel = this.getBattery().getBatteryModel().getDischargeCurrentModel();
			double tt1o = 0.0039; // time to transfert one octet in zigBee
									// transmission

			double dischargeCurrent = 5;
			try {
				dischargeCurrent = BatteryModel.getCurrentFromModel(currentTemperature, tension, dischargeCurrentModel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			capacity = (int) (this.getBattery().getInitialLevel());
			double lc = BatteryModel.levelConsumption(dischargeCurrent, capacity, tt1o);
			getBattery().consume(v * lc * pl / 100.);
		} else
			getBattery().consume(v * eTx * pl / 100.);

	}

	// ------------------------------------------------------------------------------------------KADJOUH

	/**
	 * consumeRx
	 * 
	 * @param v
	 */
	// ------------------------------------------------------------------------------------------KADJOUH
	public void consumeRx(int v) {

		if (WisenSimulation.weather) {
			double currentTemperature = WeatherScenario.getCurrentTemperature();
			int capacity = this.getBattery().getBatteryModel().getCapacity();
			double tension = this.getBattery().getBatteryModel().getTension();
			String dischargeCurrentModel = this.getBattery().getBatteryModel().getDischargeCurrentModel();
			double tt1o = 0.0039; // time to transfert one octet in zigBee
									// transmission

			double dischargeCurrent = 5;
			try {
				dischargeCurrent = BatteryModel.getCurrentFromModel(currentTemperature, tension, dischargeCurrentModel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			capacity = (int) (this.getBattery().getInitialLevel());
			double lc = BatteryModel.levelConsumption(dischargeCurrent, capacity, tt1o);
			getBattery().consume(v * lc);
		} else
			getBattery().consume(v * eRx);

	}

	// ------------------------------------------------------------------------------------------KADJOUH

	/**
	 * ConsumeTx v units
	 * 
	 * @param v
	 */
	public void consumeTx(double v) {
	}

	/**
	 * ConsumeRx v units
	 * 
	 * @param v
	 */
	public void consumeRx(double v) {
	}

	/**
	 * @return the id of the device
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id of the device
	 * 
	 * @param id
	 *            Identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	public void setMy(int my) {
		this.my = my;
	}

	public void setPl(int pl) {
		this.pl = pl;
	}

	public void setStd(String str) {
		if (str.equals("NONE")) {
			standard = Standard.NONE;
		}
		if (str.equals("802.15.4")) {
			standard = Standard.ZIGBEE_802_15_4;
		}
		if (str.equals("WIFI 802.11")) {
			standard = Standard.WIFI_802_11;
		}
		if (str.equals("LoRa")) {
			standard = Standard.LORA;
		}
		radioDataRate = Standard.getDataRate(standard);
	}

	public int getMy() {
		return my;
	}

	public int getPl() {
		return pl;
	}

	public void setCh(int ch) {
		this.ch = ch;
	}

	public int getCh() {
		return ch;
	}

	public void setNId(int nId) {
		this.nId = nId;
	}

	public int getNId() {
		return nId;
	}

	/**
	 * @return the user ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the user ID
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	// ------------------------------------------------------------------------------------------KADJOUH
	/**
	 * Set a path and name for the battery Model file
	 * 
	 * @param batteryModelFileName
	 */
	public void setBatteryModelFileName(String batteryModelFileName) {
		this.batteryModelFileName = batteryModelFileName;
	}

	/**
	 * @return the path of battery Model File
	 */
	public String getBatteryModelFileName() {
		return batteryModelFileName;
	}

	// ------------------------------------------------------------------------------------------KADJOUH

	/**
	 * Set a path and name for the script file
	 * 
	 * @param scriptFileName
	 */
	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
		// deviceSimulator.setScriptFile(scriptFileName);
	}

	/**
	 * @return the path of the communication script file
	 */
	public String getScriptFileName() {
		return scriptFileName;
	}

	/**
	 * Set the informations of a device
	 * 
	 * @param infos
	 */
	public void setInfos(String[][] infos) {
		this.infos = infos;
	}

	/**
	 * @return the informations about the device
	 */
	public String[][] getInfos() {
		infos[0][1] = "S" + id;
		infos[1][1] = "" + id;
		infos[2][1] = "" + my;
		infos[3][1] = "" + nId + " (" + Integer.toHexString(nId).toUpperCase() + ")";
		infos[4][1] = "" + ch + " (" + Integer.toHexString(ch).toUpperCase() + ")";
		infos[5][1] = scriptFileName;
		infos[6][1] = gpsFileName;
		infos[7][1] = getBatteryLevelInPercent() + " % [" + (int) getBatteryConsumption() + "]";
		// ------------------------------------------------------------------------------------------KADJOUH
		infos[8][1] = batteryModelFileName;
		// ------------------------------------------------------------------------------------------KADJOUH
		return infos;
	}

	/**
	 * An algorithm can select or not a device
	 * 
	 * @param b
	 *            Yes/No (to select the device by algorithms)
	 */
	public void setMarked(boolean b) {
		if (b)
			ledColor = 1;
		else
			ledColor = 0;
		this.marked = b;
	}

	/**
	 * Returns if the node is selected by the algorithm (yellow color)
	 * 
	 * @return marked
	 */
	public boolean isMarked() {
		return marked;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	/**
	 * Returns if the node is visited by the algorithm This parameter is used to
	 * simplify algorithms programming
	 * 
	 * @return visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Set if the device is dead or not
	 * 
	 * @param dead
	 */
	public void setDead(boolean dead) {
		if (dead) {
			this.marked = false;
			this.getBattery().setLevel(0);
		}
	}

	public boolean isDead() {
		if (getBatteryLevel() <= 0)
			return true;
		return false;
	}

	/**
	 * @return the sensor unit radius
	 */
	public double getSensorUnitRadius() {
		return 0.0;
	}

	public double getSensorUnitDeg() {
		return 0.0;
	}

	public double getSensorUnitDec() {
		return 0.0;
	}

	public int getSensorUnitN() {
		return 0;
	}

	/**
	 * @return the radio radius
	 */
	public double getRadioRadius() {
		return 0.0;
	}
	//
	// public boolean isDetected() {
	// for(Device device : DeviceList.getNodes()) {
	// if(device.detection(this)) {
	// device.setDetection(true);
	// device.setDetecting(true);
	// return true;
	// }
	// else {
	// device.setDetection(false);
	// device.setDetecting(false);
	// }
	// }
	// return false;
	// }

	/**
	 * @return if the device is with or without radio
	 */
	public boolean withRadio() {
		return withRadio;
	}

	/**
	 * Set the selection (select or not the device)
	 * 
	 * @param selection
	 */
	public void setSelection(boolean selection) {
		this.selected = selection;
	}

	/**
	 * Invert selection: Select devices that are not selected and de-select
	 * devices that are selected
	 */
	public void invSelection() {
		selected = !selected;
	}

	/**
	 * @return the radius of the device
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the radius of the device
	 * 
	 * @param radius
	 *            Radius of the device
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		// longitude = (int) (longitude * 10000000);
		// longitude = longitude / 10000000;
		return longitude;
	}

	/**
	 * Set the longitude
	 * 
	 * @param x
	 *            Longitude
	 */
	public void setLongitude(double x) {
		this.longitude = x;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		// latitude = (int) (latitude * 10000000);
		// latitude = latitude / 10000000;
		return latitude;
	}

	/**
	 * Set the latitude
	 * 
	 * @param y
	 *            Latitude
	 */
	public void setLatitude(double y) {
		this.latitude = y;
	}

	/**
	 * @return the elevation
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * Set the elevation
	 * 
	 * @param x
	 *            elevation
	 */
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	/**
	 * Set if it is possible to move or not the device
	 * 
	 * @param b
	 *            Move or not
	 */
	public void setMove(boolean b) {
		move = b;
	}

	/**
	 * @return the value of the radio in meter from a value given in pixels
	 */
	public double convertRadius() {
		return 40075017.0 * Math.cos(latitude) / Math.pow(2, (MapLayer.getMapViewer().getZoom() + 8));
	}

	/**
	 * Set the attribute inside to true or false depending the the point (xs,ys)
	 * if it is inside the device or not
	 * 
	 * @param xs
	 *            Longitude
	 * @param ys
	 *            Latitude
	 */
	public void inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
		GeoPosition gp2 = new GeoPosition(latitude, longitude);
		Point2D p2 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp2, MapLayer.getMapViewer().getZoom());
		double d1 = p1.getX() - p2.getX();
		double d2 = p1.getY() - p2.getY();
		inside = false;
		double v = Math.sqrt(d1 * d1 + d2 * d2);
		if (v < getInsideRadius()) { // MapCalc.radiusInPixels(getMaxRadius()))
										// {
			inside = true;
		}
	}

	public int getInsideRadius() {
		return 10;
	}

	/**
	 * @return radius
	 */
	public double getMaxRadius() {
		return radius;
	}

	/**
	 * @param device
	 *            A device
	 * @return the distance in pixels between the current device and the one
	 *         given as a parameter
	 */
	public double distanceInPixel(Device device) {
		double x2 = device.getLongitude();
		double y2 = device.getLatitude();
		return MapCalc.distanceInPixels(longitude, latitude, x2, y2);
	}

	/**
	 * @param device
	 * @return the distance in meters between the current device and the one
	 *         given as a parameter
	 */
	public double distance(Device device) {
		double x2 = device.getLongitude();
		double y2 = device.getLatitude();
		return MapCalc.distance(longitude, latitude, x2, y2);
	}

	public double distance(int id) {
		DeviceWithRadio device = (DeviceWithRadio) DeviceList.getNodeById(id);
		if (!radioDetect(device))
			return -1;
		double x2 = device.getLongitude();
		double y2 = device.getLatitude();
		return MapCalc.distance(longitude, latitude, x2, y2);
	}

	/**
	 * @param device
	 * @return the horizontal distance in meters between the current design and
	 *         the one given as a parameter
	 */
	public double distanceX(Device device) {
		double x2 = device.getLongitude();
		return MapCalc.distance(longitude, latitude, x2, latitude);
	}

	/**
	 * @param device
	 * @return the vertical distance in meters between the current design and
	 *         the one given as a parameter
	 */
	public double distanceY(Device device) {
		double y2 = device.getLatitude();
		return MapCalc.distance(longitude, latitude, longitude, y2);
	}

	/**
	 * @return if the device is selected or not
	 */
	public boolean isSelected() {
		return selected;
	}

	public void deviceRadioParametersEmpty() {
		if (selected) {
			DeviceParametersWindow.textField_Id.setText("");
			DeviceParametersWindow.tf_longitude.setText("");
			DeviceParametersWindow.tf_latitude.setText("");
			DeviceParametersWindow.tf_elevation.setText("");
			DeviceParametersWindow.tf_radius.setText("");
			DeviceParametersWindow.tf_suRadius.setText("");
			DeviceParametersWindow.tf_eMax.setText("");
			DeviceParametersWindow.tf_eS.setText("");
			DeviceParametersWindow.scriptComboBox.setSelectedItem("");
			DeviceParametersWindow.gpsPathNameComboBox.setSelectedItem("");

			RadioParametersWindow.textField_My.setText("");
			RadioParametersWindow.textField_Ch.setText("");
			RadioParametersWindow.textField_NId.setText("");
			RadioParametersWindow.tf_radioRadius.setText("");
			RadioParametersWindow.tf_eTx.setText("");
			RadioParametersWindow.tf_eRx.setText("");
			RadioParametersWindow.tf_eSlp.setText("");
			RadioParametersWindow.tf_eL.setText("");
			RadioParametersWindow.tf_radioDataRate.setText("");
			RadioParametersWindow.stdComboBox.setSelectedIndex(getStandard());

			DeviceParametersWindow.scriptComboBox.setSelectedItem("");
			DeviceParametersWindow.gpsPathNameComboBox.setSelectedItem("");
		}
	}

	/**
	 * Update the window of the device/radio parameters
	 */
	public void deviceRadioParametersUpdate() {
		if (selected) {
			DeviceParametersWindow.textField_Id.setText("" + id);
			DeviceParametersWindow.tf_longitude.setText("" + longitude);
			DeviceParametersWindow.tf_latitude.setText("" + latitude);
			DeviceParametersWindow.tf_elevation.setText("" + elevation);
			DeviceParametersWindow.tf_radius.setText("" + getRadius());
			DeviceParametersWindow.tf_suRadius.setText("" + getSensorUnitRadius());
			DeviceParametersWindow.tf_eMax.setText("" + this.getBatteryLevel());
			DeviceParametersWindow.tf_eS.setText("" + getES());

			// ------------------------------------------------------------------------------------------KADJOUH
			DeviceParametersWindow.batteryModelComboBox.setSelectedItem("");
			// ------------------------------------------------------------------------------------------KADJOUH

			DeviceParametersWindow.scriptComboBox.setSelectedItem("");
			DeviceParametersWindow.gpsPathNameComboBox.setSelectedItem("");
			// DeviceParametersWindow.stdComboBox.

			RadioParametersWindow.textField_My.setText("" + my);
			RadioParametersWindow.textField_Ch.setText("" + ch);
			RadioParametersWindow.textField_NId.setText("" + nId);
			RadioParametersWindow.tf_radioRadius.setText("" + getRadioRadius());
			RadioParametersWindow.tf_eTx.setText("" + getETx());
			RadioParametersWindow.tf_eRx.setText("" + getERx());
			RadioParametersWindow.tf_eSlp.setText("" + getESlp());
			RadioParametersWindow.tf_eL.setText("" + getEL());
			RadioParametersWindow.tf_radioDataRate.setText("" + getRadioDataRate());
			RadioParametersWindow.stdComboBox.setSelectedIndex(getStandard());

			String[] gpsFile;
			String gpsFileName;
			String[] scriptFile;
			String scriptFileName;

			// ------------------------------------------------------------------------------------------KADJOUH
			String[] batteryModelFile;
			String batteryModelFileName;

			if (!getBatteryModelFileName().equals("")) {

				batteryModelFile = getBatteryModelFileName().split(Pattern.quote(File.separator));
				batteryModelFileName = batteryModelFile[batteryModelFile.length - 1];
				DeviceParametersWindow.batteryModelComboBox.setSelectedItem(batteryModelFileName);
			}
			// ------------------------------------------------------------------------------------------KADJOUH

			if (!getScriptFileName().equals("")) {
				scriptFile = getScriptFileName().split(Pattern.quote(File.separator));
				scriptFileName = scriptFile[scriptFile.length - 1];
				DeviceParametersWindow.scriptComboBox.setSelectedItem(scriptFileName);
			}

			if (!getGPSFileName().equals("")) {
				gpsFile = getGPSFileName().split(Pattern.quote(File.separator));
				gpsFileName = gpsFile[gpsFile.length - 1];
				DeviceParametersWindow.gpsPathNameComboBox.setSelectedItem(gpsFileName);
			}
		}
	}

	/**
	 * Calculate the shift between the location of the device and the location
	 * of the mouse cursor
	 * 
	 * @param evx
	 *            Longitude of the mouse cursor
	 * @param evy
	 *            Latitude of the mouse cursor
	 */
	public void calculateDxDy(int evx, int evy) {
		evx = evx - (evx % MapLayer.magnetic_step);
		evy = evy - (evy % MapLayer.magnetic_step);
		Point p = new Point(evx, evy);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		double ex = gp.getLongitude();
		double ey = gp.getLatitude();
		dlongitude = ex - longitude;
		dlatitude = ey - latitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!inside && !ctrlDown) {
			selected = false;
			MapLayer.getMapViewer().repaint();
		}

		if (inside) {
			selected = !selected;
			MapLayer.getMapViewer().repaint();
		}

		if (move) {
			move = false;
			Visualisation.moveDevice(this);
			MapLayer.getMapViewer().repaint();
		}
		increaseNode = false;
		reduceNode = false;
		// calculateNeighbours();

		if (selected) {
			deviceRadioParametersUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		calculateDxDy(e.getX(), e.getY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent key) {
		lastKeyCode = key.getKeyCode();
		if (lastKeyCode == 27) {
			initSelection();
			MapLayer.getMapViewer().repaint();
		}
		if (key.isShiftDown())
			shiftDown = true;
		if (key.isAltDown())
			altDown = true;
		if (key.isControlDown())
			ctrlDown = true;
		// if (key.isMetaDown())
		// cmdDown = true;
		if (key.getKeyCode() == 65 && ctrlDown) {
			selected = true;
			if (move) {
				Visualisation.moveDevice(this);
				move = false;
			}
		}
		// if (key.getKeyCode() == 75 && ctrlDown) {
		// visible = true;
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		altDown = false;
		shiftDown = false;
		ctrlDown = false;
		// cmdDown = false;
	}

	/**
	 * Set the ID number
	 */
	public void setId() {
		id = ++DeviceList.number;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		key = e.getKeyChar();

		if (selected) {
			// deviceRadioParametersUpdate();
			if (key == 'c') {
				try {
					// Layer.addNode(this.clone());
					DeviceList.add(this.clone());
					move = false;
					/* For CTRL Z */
					// AddDevice action = new
					// AddDevice(DeviceList.getNodes().get(DeviceList.size()-1),
					// "Clone");
					// action.exec();
					/* ------ */
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
			}

			if (key == 'k') {
				visible = !visible;
			}

			if (key == 'h') {
				if (hide++ == 5)
					hide = 0;
			}
			if (key == 'j') {
				hide = 0;
			}
			if (key == ';') {
				move = false;
				radius += 5;
				reduceNode = false;
				increaseNode = !increaseNode;
			}

			if (key == ',') {
				move = false;
				increaseNode = false;
				reduceNode = !reduceNode;
				if (radius > 0)
					radius -= 5;
			}
		}

		if (key == 'S') {// && (!ctrlDown && !cmdDown)) {
			simulate();
		}

		if (key == 'q') {
			stopSimulation();
		}

		if (key == 'm') {
			// boolean typeDevice = false;
			if (selected) {
				// for (Device device : DeviceList.getNodes()) {
				// if(getId()==device.getId()) {
				// typeDevice = true;
				// break;
				// }
				// }
				move = true;
				increaseNode = false;
				reduceNode = false;
				// if(typeDevice){
				// actionMoveDevice(getNodeIdName());
				// }
				// else {
				// actionMoveMarker(getNodeIdName());
				// }
			}
		}

		// if (key == 'l') {
		// move = false;
		// }

		if (key == 'z') {
			selected = false;
		}

		if (key == 'i') {
			invSelection();
		}

		if (key == 'e') {
			displayRadius = !displayRadius;
		}

		if (key == 'r') {
			displayInfos = !displayInfos;
		}

		if (key == 'g') {
			if (selected)
				drawTxRx = !drawTxRx;
		}

		if (key == 'b') {
			if (selected)
				drawBatteryLevel = !drawBatteryLevel;
		}

		if (key == 'n') {
			if (selected) {
				if (getBatteryLevel() > 0) {
					getBattery().setLevel(0);
				} else
					getBattery().init();
				if (DeviceList.propagationsCalculated)
					DeviceList.calculatePropagations();
			}
		}

		if (key == 'w') {
			selected = false;
			if (MapLayer.selectType == getType())
				selected = true;
		}

		if (key == '1' || key == '2' || key == '3' || key == '4' || key == '5' || key == '6' || key == '7'
				|| key == '8') {
			move = false;
			selected = false;
		}

		MapLayer.getMapViewer().repaint();
	}

	/**
	 * Stop the simulation
	 */
	@SuppressWarnings("deprecation")
	public void stopSimulation() {
		if (thread != null) {
			thread.stop();
			longitude = longitude_ori;
			latitude = latitude_ori;
		}
		thread = null;
		underSimulation = false;
		// if(getType()==Device.SENSOR) getBattery().init();
		MapLayer.getMapViewer().repaint();
	}

	public void stopSimByAlgo() {
		thread = null;
		underSimulation = false;
		MapLayer.getMapViewer().repaint();
	}

	/**
	 * Start the simulation
	 */
	public void simulate() {
		move = false;
		increaseNode = false;
		reduceNode = false;
		if (selected) {
			selected = false;
			start();
		}
		/*
		 * if (isAlive()) //synchronized(this) { //this.notify(); //} resume();
		 * else if (isInterrupted()) System.out.println("Simulation"); else
		 * if(zzz++==0) start();
		 */
	}

	/**
	 * Initialize the selection
	 */
	public void initSelection() {
		selected = false;
		move = false;
		increaseNode = false;
		reduceNode = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	public abstract void initGeoZoneList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		int cx = e.getX();
		int cy = e.getY();

		if (MapLayer.magnetic) {
			cx = cx - (cx % MapLayer.magnetic_step);
			cy = cy - (cy % MapLayer.magnetic_step);
		}
		// Point p = new Point(e.getX(), e.getY());
		Point p = new Point(cx, cy);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		double ex = gp.getLongitude();
		double ey = gp.getLatitude();

		if (!move) {
			// calculateDxDy(e.getX(), e.getY());
			calculateDxDy(cx, cy);
		}

		boolean tmp_inside = inside;

		inside(e.getX(), e.getY());

		if (inside != tmp_inside) {
			// sensorParametersUpdate();
			MapLayer.getMapViewer().repaint();
		}

		if ((move && selected) && hide == 0) {

			if (geoZoneList != null && geoZoneList.size() > 0)
				initGeoZoneList();
			longitude = ex - dlongitude;
			latitude = ey - dlatitude;
			// calculateNeighbours();
			Visualisation.updatePosition(Device.SENSOR, id, this);
			MapLayer.getMapViewer().repaint();
		}

		if (increaseNode) {
			radius += 30;
			MapLayer.getMapViewer().repaint();
		}
		if (reduceNode) {
			if (radius > 0)
				radius -= 30;
			MapLayer.getMapViewer().repaint();
		}
	}

	/**
	 * Draw two red arrows to note that the device will be moved if the mouse
	 * cursor is moved
	 * 
	 * @param x
	 *            Longitude
	 * @param y
	 *            Latitude
	 * @param g
	 *            Graphics
	 */
	public void drawMoveArrows(int x, int y, Graphics g) {
		if (move && (inside || selected)) {
			// 2 fleches rouges
			g.setColor(UColor.RED);
			g.drawLine(x, y, x + 20, y);
			g.drawLine(x + 20, y, x + 14, y + 3);
			g.drawLine(x + 20, y, x + 14, y - 3);
			g.drawLine(x, y - 20, x, y);
			g.drawLine(x, y - 20, x - 3, y - 16);
			g.drawLine(x, y - 20, x + 3, y - 16);
		}
	}

	/**
	 * Draw +- symbol to note that the size of the device will be reduced or
	 * increased if the mouse cursor is moved or if the +- keys are used
	 * 
	 * @param x
	 *            Longitude
	 * @param y
	 *            Latitude
	 * @param g
	 *            Graphics
	 */
	public void drawIncRedDimNode(int x, int y, Graphics g) {
		if (reduceNode || increaseNode) {
			g.setColor(UColor.RED);
			g.drawLine(x - 10, y, x + 10, y);
		}
		if (increaseNode) {
			g.drawLine(x, y - 10, x, y + 10);
		}
	}

	/**
	 * Initialization
	 * 
	 * @param g
	 *            Graphics
	 */
	public void initDraw(Graphics g) {
		g.setFont(new Font("arial", 1, 10));
	}

	/**
	 * Draw the ID of the device
	 * 
	 * @param x
	 *            Longitude
	 * @param y
	 *            Latitude
	 * @param g
	 *            Graphics
	 */
	public void drawId(int x, int y, Graphics g) {
		if (DeviceParameters.displayDetails) {
			g.setColor(Color.BLACK);
			g.drawString(getNodeIdName() + " [" + my + "]", (int) (x + 10), (int) (y + 5));
			if (!message.equals("")) {
				g.setColor(Color.BLUE);
				g.drawString(">> " + message, (int) (x + 10), (int) (y + 15));
			}
		}
	}

	public void drawId2(int x, int y, Graphics g) {
		if (DeviceParameters.displayDetails) {
			g.setColor(Color.BLACK);
			g.drawString(getNodeIdName(), (int) (x + 10), (int) (y + 10));
		}
	}

	// /**
	// * @param device
	// * @return if a device is in the radio area of the current device
	// */
	// public boolean radioDetect(Device device) {
	// if (withRadio && device.withRadio()) {
	// // double dMax = Math.max(MapCalc.rayonEnPixel(getRadioRadius()),
	// // MapCalc.rayonEnPixel(node.getRadioRadius()));
	// double dMax = Math.max(getRadioRadius(), device.getRadioRadius());
	// return (dMax > (distance(device)));
	// }
	// return false;
	// }

	/**
	 * Draw the radius
	 * 
	 * @param x
	 *            Longitude
	 * @param y
	 *            Latitude
	 * @param r
	 *            Radius
	 * @param g
	 *            Graphics
	 */
	public void drawRadius(int x, int y, int r, Graphics g) {
	}

	public boolean displayInfos() {
		return displayInfos;
	}

	/**
	 * Draw the informations of the device
	 * 
	 * @param device
	 *            Device
	 * @param g
	 *            Graphics
	 */
	public void drawInfos(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.6f));
		int[] coord;
		if (displayInfos && selected && infos != null) {
			g.setFont(new Font("arial", 1, 10));
			coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int lx1 = coord[0];
			int ly1 = coord[1];
			g.setColor(UColor.WHITE_TRANSPARENT);
			g.fillRect(lx1 + 20, ly1 - 25, 150, 90);
			g.setColor(UColor.BLACK_TRANSPARENT);
			g.drawRect(lx1 + 20, ly1 - 25, 150, 90);
			g.setColor(Color.black);
			g.drawString(getInfos()[0][0] + getInfos()[0][1], lx1 + 30, ly1 - 10);
			g.drawString(getInfos()[1][0] + getInfos()[1][1], lx1 + 30, ly1);
			g.drawString(getInfos()[2][0] + getInfos()[2][1], lx1 + 30, ly1 + 10);
			g.drawString(getInfos()[3][0] + getInfos()[3][1], lx1 + 30, ly1 + 20);
			g.drawString(getInfos()[4][0] + getInfos()[4][1], lx1 + 30, ly1 + 30);
			g.drawString(getInfos()[5][0] + getInfos()[5][1], lx1 + 30, ly1 + 40);
			g.drawString(getInfos()[6][0] + getInfos()[6][1], lx1 + 30, ly1 + 50);
			g.drawString(getInfos()[7][0] + getInfos()[7][1], lx1 + 30, ly1 + 60);
			// ------------------------------------------------------------------------------------------KADJOUH
			g.drawString(getInfos()[8][0] + getInfos()[8][1], lx1 + 30, ly1 + 70);
			// ------------------------------------------------------------------------------------------KADJOUH
		}
	}

	@Override
	public Device clone() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.setId();
		newNode.move = true;
		
		//------------------------------------------------------------------------------------------KADJOUH			
		newNode.setBatteryModelFileName(batteryModelFileName);
		//------------------------------------------------------------------------------------------KADJOUH	
		
		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		MapLayer.getMapViewer().addMouseListener(newNode);
		MapLayer.getMapViewer().addMouseMotionListener(newNode);
		MapLayer.getMapViewer().addKeyListener(newNode);
		return newNode;
	}

	/**
	 * Start simulation thread
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		} else {
			System.out.println("Simulation is running for node " + getNodeIdName());
		}
	}

	/**
	 * @return the battery level
	 */
	public double getBatteryLevel() {
		if (getBattery() != null)
			return getBattery().getLevel();
		return 0;
	}

	/**
	 * Set the battery level
	 */
	public void setBatteryLevel(double level) {
		if (getBattery() != null)
			getBattery().setLevel(level);
	}

	/**
	 * @return the battery level in percent
	 */
	public int getBatteryLevelInPercent() {
		return getBattery().getLevelInPercent();
	}

	public double getBatteryConsumption() {
		return getBattery().getBatteryConsumption();
	}

	/**
	 * @return the battery object
	 */
	public Battery getBattery() {
		return null;
	}

	/**
	 * @return the state of the device
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * Set the state value
	 * 
	 * @param state
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * @return if the device is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	public Thread getThread() {
		return thread;
	}

	public static void initNumber() {
		DeviceList.number = 1;
	}

	public static void incNumber() {
		DeviceList.number++;
	}

	public void fixori() {
		longitude_ori = longitude;
		latitude_ori = latitude;
		elevation_ori = elevation;
	}

	public void toOri() {
		longitude = longitude_ori;
		latitude = latitude_ori;
		elevation = elevation_ori;
		// if (SimulationInputs.visibility && isMobile()) {
		// Vi_VisibilityZones vz = new Vi_VisibilityZones((SensorNode) this);
		// vz.calculate();
		// }
	}

	public int getHide() {
		return this.hide;
	}

	public abstract double getNextTime();

	public abstract void loadRouteFromFile();

	public abstract void moveToNext(boolean visual, int visualDelay);

	public abstract boolean hasNext();

	public abstract boolean canMove();

	public void setLedColor(int ledColor) {
		this.ledColor = ledColor;
	}

	public int getLedColor() {
		return ledColor;
	}

	public double getETx() {
		return eTx;
	}

	public void setETx(double eTx) {
		this.eTx = eTx;
	}

	public double getERx() {
		return eRx;
	}

	public void setERx(double eRx) {
		this.eRx = eRx;
	}

	public double getESlp() {
		return eSlp;
	}

	public void setESlp(double eSlp) {
		this.eSlp = eSlp;
	}

	public double getEL() {
		return eL;
	}

	public void setEL(double eL) {
		this.eL = eL;
	}

	public double getES() {
		return eS;
	}

	public void setES(double eS) {
		this.eS = eS;
	}

	public void setTrgetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetName() {
		return targetName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + id;
	}

	// ------
	public void creatValueList() {
		valueList = new LinkedList<Double>();
	}

	public Double getIthValue(int i) {
		return valueList.get(i);
	}

	public void addValue(Double value) {
		valueList.add(value);
	}

	// Remove the first occurence
	public void removeValue(Double value) {
		valueList.remove(value);
	}

	public void removeIthValue(int i) {
		valueList.remove(i);
	}

	public Script getScript() {
		return script;
	}

	public void setEvent(double event) {
		this.event = event;
	}

	public void setEvent(String event) {
		this.event = Double.parseDouble(event);
	}

	public double getEvent() {
		return event;
	}

	public void setEvent2(double event) {
		this.event2 = event;
	}

	public double getEvent2() {
		return event2;
	}

	public abstract void loadScript();

	public abstract void initBuffer();

	public abstract void initForSimulation();

	public void deviceMoveAction(String s) {
		try {
			DeviceList.add(cloneWithSameId());
			MoveDevice action = new MoveDevice(DeviceList.getNodes().get(DeviceList.size() - 1), "Move");
			action.exec();
			DeviceList.delete(DeviceList.size() - 1); // WithClone
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
	}

	public Device cloneWithSameId() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.move = true;

		// ------------------------------------------------------------------------------------------KADJOUH
		newNode.setBatteryModelFileName(batteryModelFileName);
		// ------------------------------------------------------------------------------------------KADJOUH

		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		MapLayer.getMapViewer().addMouseListener(newNode);
		MapLayer.getMapViewer().addMouseMotionListener(newNode);
		MapLayer.getMapViewer().addKeyListener(newNode);
		return newNode;
	}

	public Device cloneDeviceWithId() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.move = true;
		// ------------------------------------------------------------------------------------------KADJOUH
		newNode.setBatteryModelFileName(batteryModelFileName);
		// ------------------------------------------------------------------------------------------KADJOUH
		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		MapLayer.getMapViewer().addMouseListener(newNode);
		MapLayer.getMapViewer().addMouseMotionListener(newNode);
		MapLayer.getMapViewer().addKeyListener(newNode);
		return newNode;
	}

	// public void actionMoveDevice(String s) {
	// try {
	// DeviceList.add(this.cloneDeviceWithId());
	// MoveDevice action = new
	// MoveDevice(DeviceList.getNodes().get(DeviceList.size()-1), "Move");
	// action.exec();
	// DeviceList.delete(DeviceList.size()-1); // WithClone
	// } catch (CloneNotSupportedException e1) {
	// e1.printStackTrace();
	// }
	// }

	// public void actionMoveMarker(String s) {
	// try {
	// MarkerList.add((Marker)this.cloneMarker());
	// MoveMarker action = new
	// MoveMarker(MarkerList.getMarkers().get(MarkerList.size()-1),
	// "MoveMarker");
	// action.exec();
	// MarkerList.delete(MarkerList.size()-1);
	// } catch (CloneNotSupportedException e1) {
	// e1.printStackTrace();
	// }
	// }
	public Device cloneMarker() throws CloneNotSupportedException {
		Device newNode = (Marker) super.clone();
		newNode.move = true;

		// ------------------------------------------------------------------------------------------KADJOUH
		newNode.setBatteryModelFileName(batteryModelFileName);
		// ------------------------------------------------------------------------------------------KADJOUH

		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		MapLayer.getMapViewer().addMouseListener(newNode);
		MapLayer.getMapViewer().addMouseMotionListener(newNode);
		MapLayer.getMapViewer().addKeyListener(newNode);
		return newNode;
	}

	public int getIdm() {
		return idm;
	}

	public void setIdm() {
		idm = MarkerList.size();
	}

	public void init() {
		// packetEventList = new LinkedList<PacketEvent>();
		message = "";
		setMarked(false);
		setVisited(false);
		setDead(false);
		setLedColor(0);
		initBattery();
		initBuffer();
		pl = 100;
		// getBattery().init();
		if (getType() == Device.SENSOR) {
			setSending(false);
			setReceiving(false);
		}
	}

	public abstract void initBattery();

	public boolean isSending() {
		return sending;
	}

	public boolean isWriting() {
		return writing;
	}

	public boolean isReceiving() {
		return receiving;
	}

	public void setSending(boolean b) {
		sending = b;
	}

	public void setWriting(boolean b) {
		writing = b;
	}

	public void setReceiving(boolean b) {
		receiving = b;
	}

	public Color getRadioLinkColor() {
		return radioLinkColor;
	}

	public Color getACKLinkColor() {
		return ackLinkColor;
	}

	public void setRadioLinkColor(Color radioLinkColor) {
		this.radioLinkColor = radioLinkColor;
	}

	public void gotoTheNextInstruction() {
		// System.out.println(getId()+" -> "+script.getCurrent()+"
		// "+script.getIndex());
		if (!script.getCurrent().isExecuting()) {
			script.next();
		}
	}

	public void gotoTheNextEvent(double min) {
		if (event != Double.MAX_VALUE)
			event = event - min;
	}

	public long getDistanceModeDelay() {
		return distanceModeDelay;
	}

	public void setDistanceModeDelay(long distanceModeDelay) {
		this.distanceModeDelay = distanceModeDelay;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public long getUartDataRate() {
		return uartDataRate;
	}

	public void setUartDataRate(long uartDataRate) {
		this.uartDataRate = uartDataRate;
	}

	public int getRadioDataRate() {
		return radioDataRate;
	}

	public void setRadioDataRate(int radioDataRate) {
		this.radioDataRate = radioDataRate;
	}

	public int getStandard() {
		return standard;
	}

	public void setStandard(int standard) {
		this.standard = standard;
	}

	public void drift() {
		double d = random.nextGaussian() * sigmaOfDriftTime;
		if (d > (3.0 * sigmaOfDriftTime))
			d = 3.0 * sigmaOfDriftTime;
		if (d < (-3.0 * sigmaOfDriftTime))
			d = -3.0 * sigmaOfDriftTime;
		System.out.println(d);
		driftTime = 1 - d;

		// if(event != Double.MAX_VALUE)
		// event += deltaDriftTime;
	}

	public double getDriftTime() {
		return driftTime;
	}

	public abstract Polygon getRadioPolygon();

	public abstract void execute();

	public abstract void drawRadioLinks(Graphics g);

	public abstract void calculatePropagations();

	public abstract void resetPropagations();

	public abstract void drawRadioPropagations(Graphics g);

	public abstract boolean radioDetect(DeviceWithRadio device);

	public void setSigmaOfDriftTime(double sigmaOfDriftTime) {
		this.sigmaOfDriftTime = sigmaOfDriftTime;
	}

	public double getTimeToResend() {
		return timeToResend;
	}

	public void setTimeToResend(double timeToResend) {
		this.timeToResend = timeToResend;
	}

	public int getNumberOfSends() {
		return numberOfSends;
	}

	public void setNumberOfSends(int numberOfSends) {
		this.numberOfSends = numberOfSends;
	}

	// public static void main(String [] args) {
	// Random random = new Random();
	// double d = random.nextGaussian();
	// double sigma = 3;
	// double s = 0;
	// for(int i=0; i<10000; i++) {
	// d = random.nextGaussian() * sigma;
	// s += d;
	// }
	// System.out.println(s/10000);
	// }

	public boolean isMobile() {
		return !gpsFileName.equals("");
	}

}