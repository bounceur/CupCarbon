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
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.Random;

import battery.Battery;
//import cupcarbon.RadioModuleWindow;
import map.MapLayer;
import map.NetworkParameters;
import senscript.SenScript;
import simulation.SimulationInputs;
import utilities.MapCalc;
import utilities.UColor;
import visibility.VisibilityZones;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public abstract class Device extends MapObject implements Cloneable {
	
	public static final boolean DEAD = false;
	public static final boolean ALIVE = true;
	public static final boolean SLEEP = false;

	protected double sigmaOfDriftTime = 0.00003;
	protected double driftTime = 1.0; 
	
	public static int moveSpeed = 100;

	protected int idm = 0;
	
	protected Battery battery ;
	
	protected long uartDataRate = 9600;//1000000000; //38400;	
		
	protected boolean underSimulation = false;
	
	protected boolean withRadio = false;
	protected boolean withSensor = false;
	protected boolean mobile = false;
	protected boolean displayRadius = false;	
	protected boolean visited = false;	
	protected boolean drawBatteryLevel = false;

	protected String [][] infos = {{"SENSOR: ",""},{"ID: ",""},{"MY: ",""},{"Network ID: ",""},{"Channel: ",""},{"Script: ",""},{"GPS: ",""},{"Battery: ",""}};
	protected boolean displayInfos = false;
	
	protected String scriptFileName = "";
	protected String gpsFileName = "";
	protected String gpsFileName_ori = "";	
	protected String nateventFileName = "";
	protected SenScript script = null;
	protected String targetName = ""; 
	protected int ledColor = 0;

	protected Color radioLinkColor = new Color(221,0,0,190);

	protected boolean receiving = false;
	protected boolean sending = false;
	protected boolean writing = false;
	protected long distanceModeDelay = 2000 ;
	
	protected boolean state = ALIVE; 
	
	//----------------------------------
	// For Algorithms
	//----------------------------------
	protected boolean marked = false;
	protected double value = 0;
	protected LinkedList<Double> valueList; 
	//----------------------------------
	
	protected double event = Double.MAX_VALUE;		// Event relied to actions = sending/receiving
	protected double event2 = Double.MAX_VALUE;		// Event relied to mobility
	protected double event3 = Double.MAX_VALUE;		// Event relied to natural events
	
	protected Thread thread;	
	
	protected String message = "";
	
	private Random random = new Random();
	
	/**
	 * Empty constructor
	 */
	public Device() {
		super();
	}


	public Device(double x, double y, double z, double radius, int id) {
		super(x, y, z, radius, id);
	}

	/**
	 * Set the GPS file path of the device
	 * 
	 * @param gpsFileName
	 */
	public void setGPSFileName(String gpsFileName) {
		if(gpsFileName.endsWith(".gps"))
			this.gpsFileName = gpsFileName;
		else
			this.gpsFileName = "";
	}

	/**
	 * @return the GPS file name
	 */
	public String getGPSFileName() {
		return gpsFileName;
	}
	
	/**
	 * @return the Natural Event file name
	 */
	public String getNatEventFileName() {
		return nateventFileName;
	}
	
	/**
	 * Set the GPS file path of the device
	 * 
	 * @param gpsFileName
	 */
	public void setNatEventFileName(String nateventFileName) {
		if(nateventFileName.endsWith(".evt")) {
			this.nateventFileName = nateventFileName;
		}
		else {
			this.nateventFileName = "";
		}
	}

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

	/**
	 * Set a path and name for the script file
	 * 
	 * @param scriptFileName
	 */
	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
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
		infos[0][1] = "S"+id;
		infos[1][1] = ""+id;
		infos[5][1] = scriptFileName;
		infos[6][1] = gpsFileName;
		infos[7][1] = getBatteryLevelInPercent()+" % ["+(int)getBatteryConsumption()+"]";
		return infos;
	}

	/**
	 * An algorithm can select or not a device
	 * 
	 * @param b
	 *            Yes/No (to select the device by algorithms)
	 */
	public void setMarked(boolean b) {
		if(b) 
			ledColor = 1;
		else
			ledColor = 0;
		this.marked = b;
	}
	
	/**
	 * Mark a node and refresh the MapLayer
	 */
	public void mark() {
		ledColor = 1;
		marked = true;
		MapLayer.repaint();
	}
	
	/**
	 * Unmark a node and refresh the MapLayer
	 */
	public void unmark() {
		ledColor = 0;
		marked = false;
		MapLayer.repaint();
	}
		
	/**
	 * Returns if the node is selected by the algorithm (yellow color)
	 * @return marked
	 */
	public boolean isMarked() {
		return marked;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited ;
	}
	
	/**
	 * Returns if the node is visited by the algorithm
	 * This parameter is used to simplify 
	 * algorithms programming
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
		if(dead) {
			this.marked = false;
			this.getBattery().setLevel(0);
		} 
	}
	
	public boolean isDead() {
		if(getBatteryLevel()<=0) return true;
		return false;
	}
		
	/**
	 * @return if the device is with or without radio
	 */
	public boolean withRadio() {
		return withRadio;
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
	 * @param point
	 * @return the distance in meters between the current device and the point
	 *         given as parameters (x,y)
	 */
	public double distance(double x, double y) {
		return MapCalc.distance(longitude, latitude, x, y);
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
	
	/**
	 * @param id
	 * @return the distance in meters between the current device and the one
	 *         having the id given as a parameter
	 */
	public double distance(int id) {
		DeviceWithRadio device = (DeviceWithRadio) DeviceList.getNodeById(id);
		if (!radioDetect(device)) return -1;
		double x2 = device.getLongitude();
		double y2 = device.getLatitude();
		return MapCalc.distance(longitude, latitude, x2, y2);
	}
	
	public double distance2(int id) {
		DeviceWithRadio device = (DeviceWithRadio) DeviceList.getNodeById(id);
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
	 * Set the ID number
	 */
	public void setId() {
		id = DeviceList.number++;
	}

	/**
	 * Stop the simulation
	 */
	@SuppressWarnings("deprecation")
	public void stopAgentSimulation() {
		if (thread != null) {
			thread.stop();
			longitude = longitude_ori;
			latitude = latitude_ori;
		}		
		thread = null;
		underSimulation = false;
		MapLayer.repaint();
	}
	
	public void stopSimByAlgo() {
		thread = null;
		underSimulation = false;
		MapLayer.repaint();
	}

	/**
	 * Start the simulation
	 */
	public void agentSimulation() {
		if (selected) {
			selected = false;
			start();
		}
	}

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
		}
	}

//	public Device duplicate() {
//		try {
//			DeviceList.add(clone());
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//	}	
	
	public Device duplicateByConsole() throws CloneNotSupportedException {
	    Device newNode = (Device) super.clone();
	    newNode.setId();
	    newNode.setScriptFileName(scriptFileName);
	    newNode.setGPSFileName(gpsFileName);
	    newNode.setLongitude(this.getLongitude()+0.0001);
	    newNode.setLatitude(this.getLatitude()+0.0001);
	    newNode.setElevation(this.getElevation());
	    return newNode;
    }
	
	@Override
	public Device clone() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.setId();
		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
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
			System.out.println("Simulation is running for node " + getName());
		}
	}

	/**
	 * @return the battery level
	 */
	public double getBatteryLevel() {
		if(getBattery() != null)
			return getBattery().getLevel();
		return 0;
	}
	
	/**
	 * Set the battery level
	 */
	public void setBatteryLevel(double level) {
		if(getBattery() != null)
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
		return battery;
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
		gpsFileName_ori = gpsFileName;
		if (SimulationInputs.visibility) {
			if(getType()==Device.SENSOR || getType()==Device.DIRECTIONAL_SENSOR || getType()==Device.BASE_STATION) {
				VisibilityZones vz = new VisibilityZones((SensorNode) this);
				vz.run();
			}
		}
	}
	
	public void toOri() {
		longitude = longitude_ori;
		latitude = latitude_ori;
		elevation = elevation_ori;
		gpsFileName = gpsFileName_ori;
		if (SimulationInputs.visibility) {
			if(getType()==Device.SENSOR || getType()==Device.DIRECTIONAL_SENSOR || getType()==Device.BASE_STATION) {
				VisibilityZones vz = new VisibilityZones((SensorNode) this);
				vz.run();
			}
		}
	}
	
	public int getHide() {
		return hide;
	}

	public abstract double getNextTime();
	public abstract void loadRouteFromFile();
	public abstract void moveToNext(boolean visual, int visualDelay);
	public abstract boolean hasNext() ;
		
	public abstract double getNextValueTime();
	public abstract void generateNextValue();

	public void setLedColor(int ledColor) {
		this.ledColor = ledColor;
	}
	
	public int getLedColor() {
		return ledColor;
	}
	
	public void setTrgetName(String targetName) {
		this.targetName = targetName;
	}
	
	public String getTargetName() {
		return targetName ;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}	
	
	@Override
	public String toString() {
		return ""+id;
	}
	
	//------
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
	
	public SenScript getScript() {
		return script;
	}
	
	public void setEvent(double event) {
		this.event = event ;
	}
	
	public void setEvent(String event) {
		this.event = Double.parseDouble(event) ;
	}
	
	public double getEvent() {
		return event;
	}
	
	public void setEvent2(double event) {
		this.event2 = event ;
	}
	
	public double getEvent2() {
		return event2;
	}
	
	public void setEvent3(double event) {
		this.event3 = event ;
	}
	
	public double getEvent3() {
		return event3;
	}

	public abstract void loadScript();
	
	public abstract void initBuffer() ;
	
	public abstract void initForSimulation();
		
	public Device cloneWithSameId() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		return newNode;
	}
	
	public Device cloneDeviceWithId() throws CloneNotSupportedException {
		Device newNode = (Device) super.clone();
		newNode.setScriptFileName(scriptFileName);
		newNode.setGPSFileName(gpsFileName);
		return newNode;
	}	
	
	public void init() {
		message = "";
		setMarked(false);
		setVisited(false);
		setDead(false);			
		setLedColor(0);
		initBattery();
		initBuffer();
	}
	
	public abstract void initBattery() ;
		
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
	
	public void setRadioLinkColor(Color radioLinkColor) {
		this.radioLinkColor = radioLinkColor;
	}	

	public void gotoTheNextInstruction() {
		if(!script.getCurrent().isExecuting()) {
			script.next();
		}		
	}

	public void gotoTheNextEvent(double min) {
		if(event != Double.MAX_VALUE)
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

	public void drift() {
		double d = random.nextGaussian() * sigmaOfDriftTime;		
		if (d > (3.0*sigmaOfDriftTime))
			d = 3.0*sigmaOfDriftTime;
		if (d < (-3.0*sigmaOfDriftTime))
			d = -3.0*sigmaOfDriftTime;
		System.out.println(d);
		driftTime = 1 - d; 
	}
	
	public double getDriftTime() {
		return driftTime;
	}
	
	public double getSigmaOfDriftTime() {
		return sigmaOfDriftTime;
	}
	
	public abstract Polygon getRadioPolygon();
	
	public abstract void execute();
	public abstract void drawRadioLinks(int k, Graphics g) ;
	public abstract void calculatePropagations();
	public abstract void resetPropagations();
	public abstract void drawRadioPropagations(Graphics g) ;
	public abstract boolean radioDetect(DeviceWithRadio device) ;

	public void setSigmaOfDriftTime(double sigmaOfDriftTime) {
		this.sigmaOfDriftTime = sigmaOfDriftTime;
	}

	public boolean isMobile() {
		return !gpsFileName.equals("");
	}
	
	public boolean getDisplaydRadius() {
		return displayRadius;
	}
	
	public void setDisplaydRadius(boolean b) {
		displayRadius = b;
	}
	
	public boolean getDisplaydInfos() {
		return displayInfos;
	}
	
	public void setDisplaydInfos(boolean b) {
		displayInfos = b;
	}
	
	public void setHide(int hide) {
		this.hide = hide ;
	}
		
	public boolean getDrawBatteryLevel() {
		return drawBatteryLevel;
	}
	
	public void setDrawBatteryLevel(boolean b) {
		drawBatteryLevel = b;
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
		if (NetworkParameters.displayDetails) {
			g.setColor(Color.BLACK);
			if(MapLayer.dark) g.setColor(new Color(179, 221, 67));
			g.drawString(getName(), (int) (x + 10), (int) (y + 6));
		}
	}	
	
	public void increaseRadius(int d) {
		radius += d ;
	}
	
	public abstract double getSensorUnitRadius();
	public abstract double getESensing();
	
	public void invertDrawBatteryLevel() {
		drawBatteryLevel = !drawBatteryLevel;
	}
	
	public abstract Device duplicate() ;
	public abstract Device duplicateWithShift(double sh1, double sh2, double sh3) ;
	public abstract void save(String fileName) ;	
	
	public double getSUCoverage() {
		return 0;
	}
	
	public double getSUDirection() {
		return 0;
	}
	
}