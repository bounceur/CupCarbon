package device;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import geo_objects.GeoZoneList;
import map.MapLayer;
import markers.MarkerList;
import project.Project;
import visibility.VisibilityZones;


public abstract class MapObject implements Runnable, Cloneable {

	public static final int NONE = 0;
	public static final int SENSOR = 1;
	public static final int GAS = 2;
	public static final int FLYING_OBJECT = 3;
	public static final int BASE_STATION = 4;
	public static final int DIRECTIONAL_SENSOR = 5;
	public static final int MOBILE = 6;
	public static final int WEATHER = 7;
	public static final int MARKER = 8;
	public static final int VERTEX = 9;
	public static final int BUILDING = 10;
	public static final int GEOZONE = 11;
	public static final int RSENSOR = 12;
	
	protected int id = 0;
	protected String userId = "";
	protected double longitude, latitude, elevation;
	protected double longitude_ori, latitude_ori, elevation_ori;
	protected double dLongitude, dLatitude, dElevation;
	protected double prevLongitude, prevLatitude, prevElevation;
	protected double radius = 0;
	protected double radiusOri = 0;
	
	protected boolean selected = false;
	protected boolean visible = true;
	protected boolean inside = false;
	protected int hide = 0;
	protected GeoZoneList geoZoneList = null;
	
	protected boolean altDown = false;
	protected boolean shiftDown = false;
	
	protected int lastKeyCode = 0;	
	
	protected int idm = 0;
	
	protected char key = 0;
	
	/**
	 * Constructor with the 3 main parameters of a Map Object
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
	public MapObject(double x, double y, double z, double radius, int id) {
		if(id==-1) 
			this.id = DeviceList.number++;
		else 
			this.id = id;
		userId = "_" + id;
		this.longitude = x;
		this.latitude = y;
		this.elevation = z;
		this.radius = radius;
		radiusOri = radius;
	}	
	
	/**
	 * Empty constructor
	 */
	public MapObject(){
		
	}
	
	/**
	 * Initialize the selection
	 */
	public void initSelection() {
		selected = false;
	}
	
	/**
	 * ID First Letter
	 * 
	 * @return the first letter of the identifier
	 */
	public void inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.mapViewer.convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.mapViewer.getTileFactory().geoToPixel(gp, MapLayer.mapViewer.getZoom());
		GeoPosition gp2 = new GeoPosition(latitude, longitude);
		Point2D p2 = MapLayer.mapViewer.getTileFactory().geoToPixel(gp2, MapLayer.mapViewer.getZoom());
		double d1 = p1.getX() - p2.getX();
		double d2 = p1.getY() - p2.getY();
		inside = false;
		double v = Math.sqrt(d1 * d1 + d2 * d2);
		if (v < getInsideRadius()) { //MapCalc.radiusInPixels(getMaxRadius())) {
			inside = true;
		}
	}
	
	public int getInsideRadius() {
		return 10;
	}
	
	public abstract String getIdFL();

	/**
	 * @return the name of the identifier
	 */
	public abstract String getName();

	/**
	 * @return the type of the map object
	 */
	public abstract int getType();

	/**
	 * Draw the map object
	 * 
	 * @param g
	 *            Graphics
	 */
	public abstract void draw(Graphics g);	
	
	public double getDLongitude() {
		return dLongitude;
	}
	
	public double getDLatitude() {
		return dLatitude;
	}
	
	public double getDElevation() {
		return dElevation;
	}
	
	
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
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
	 * @return the longitude
	 */
	public double getPrevLongitude() {
		return prevLongitude;
	}

	/**
	 * Set the longitude
	 * 
	 * @param x
	 *            Longitude
	 */
	public void setPrevLongitude(double x) {		
		this.prevLongitude = x;
	}

	/**
	 * @return the latitude
	 */
	public double getPrevLatitude() {
		return prevLatitude;
	}

	/**
	 * Set the latitude
	 * 
	 * @param y
	 *            Latitude
	 */
	public void setPrevLatitude(double y) {
		this.prevLatitude = y;
	}

	/**
	 * @return the elevation
	 */
	public double getPrevElevation() {
		return prevElevation;
	}

	/**
	 * Set the elevation
	 * 
	 * @param x
	 *            elevation
	 */
	public void setPrevElevation(double elevation) {		
		this.prevElevation = elevation;
	}
	
	public int getIdm() {
		return idm;
	}
	
	public void setIdm() {
		idm = MarkerList.size();
	}
	
	/**
	 * Set the selection (select or not the map object)
	 * 
	 * @param selection
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	/**
	 * @return if the map object is selected or not
	 */
	public boolean isSelected() {
		return selected;
	}
	
	public void invSelection() {
		selected = !selected;
	}
	
	/**
	 * @return the id of the map object
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id of the map object
	 * 
	 * @param id
	 *            Identifier
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Initialization
	 * 
	 * @param g
	 *            Graphics
	 */
	public void initDraw(Graphics g) {

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
		if (MapLayer.magnetic) {
			evx = evx - (evx % MapLayer.magnetic_step);
			evy = evy - (evy % MapLayer.magnetic_step);
		}
		Point p = new Point(evx, evy);
		GeoPosition gp = MapLayer.mapViewer.convertPointToGeoPosition(p);
		double ex = gp.getLongitude();
		double ey = gp.getLatitude();
		dLongitude = ex - longitude;
		dLatitude = ey - latitude;
	}
	
	public void calculateGpsDxDy(double ex, double ey) {
		dLongitude = ex - longitude;
		dLatitude = ey - latitude;
	}
	
	public abstract void initGeoZoneList() ;

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
	
	public boolean isInside() {
		return inside;
	}
	
	public static void listSensorParameters(String fileName) {
		try {
			String[] str = null;
			String line;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {							
				case "device_id":
					System.out.println("Device id is: " + str[1]);
					break;
				case "device_longitude":
					System.out.println("Device longitude is: " + str[1]);
					break;
				case "device_latitude":
					System.out.println("Device latitude is: " + str[1]);
					break;
				case "device_elevation":
					System.out.println("Device elevation is: " + str[1]);
					break;
				case "device_radius":
					System.out.println("Device radius is: " + str[1]);
					break;
				case "device_sensor_unit_radius":
					System.out.println("Device sensor unit radius is: " + str[1]);
					break;
				case "device_gps_file_name":
					System.out.println("Device gps file name is: " + str[1]);
					break;
				case "device_script_file_name":
					System.out.println("Device script filename is: " + str[1]);
					break;
				case "device_type":
					System.out.println("Device type is: " + str[1]);
					break;
				case "device_hide":
					System.out.println("Device hide is: " + str[1]);
					break;
				case "device_draw_battery":
					System.out.println("Device draw battery is: " + str[1]);
					break;								
				}
			}
			br.close();
			System.out.println("------------------------------------");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void listGasParameters(String fileName) {
		try {
			String[] str = null;
			String line;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_id":
					System.out.println("Device id is: " + str[1]);					
					break;
				case "device_longitude":
					System.out.println("Device longitude is: " + str[1]);					
					break;
				case "device_latitude":
					System.out.println("Device latitude is: " + str[1]);					
					break;
				case "device_elevation":
					System.out.println("Device elevation is: " + str[1]);					
					break;
				case "device_radius":
					System.out.println("Device radius is: " + str[1]);					
					break;
				case "device_gps_file_name":
					System.out.println("Device gps file name is: " + str[1]);					
					break;
				case "device_type":
					System.out.println("Device type is: " + str[1]);					
					break;
				case "device_hide":
					System.out.println("Device hide is: " + str[1]);					
					break;
				case "device_draw_battery":
					System.out.println("Device draw battery is: " + str[1]);
					break;				
				}
			}
			br.close();
			System.out.println("------------------------------------");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listMobileparameters(String fileName) {
		try {
			String[] str = null;
			String line;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();			
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "device_id":
					System.out.println("Device id is: " + str[1]);					
					break;
				case "device_longitude":
					System.out.println("Device longitude is: " + str[1]);					
					break;
				case "device_latitude":
					System.out.println("Device latitude is: " + str[1]);					
					break;
				case "device_elevation":
					System.out.println("Device elevation is: " + str[1]);					
					break;
				case "device_radius":
					System.out.println("Device radius is: " + str[1]);					
					break;
				case "device_gps_file_name":
					System.out.println("Device gps file name is: " + str[1]);					
					break;
				case "device_type":
					System.out.println("Device type is: " + str[1]);					
					break;
				case "device_hide":
					System.out.println("Device hide is: " + str[1]);					
					break;
				case "device_draw_battery":
					System.out.println("Device draw battery is: " + str[1]);
					break;				
				}							
			}
			br.close();
			System.out.println("------------------------------------");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listRadioModuleParameters(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str = null;
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(":");
				switch (str[0]) {
				case "radio_name":
					System.out.println("The Radio name is: " + str[1]);	
					break;
				case "current_radio_name":
					System.out.println("The Current Radio name is: " + str[1]);	
					break;
				case "radio_standard":
					System.out.println("The Radio Standard is: " + str[1]);	
					break;
				case "radio_my":
					System.out.println("The Radio My is: " + str[1]);	
					break;
				case "radio_channel":
					System.out.println("The Radio Channel is: " + str[1]);	
					break;
				case "radio_network_id":
					System.out.println("The Radio Network Id is: " + str[1]);	
					break;
				case "radio_radius":
					System.out.println("The Radio Radius is: " + str[1]);	
					break;
				case "radio_etx":
					System.out.println("The Radio Etx is: " + str[1]);	
					break;
				case "radio_erx":
					System.out.println("The Radio Erx is: " + str[1]);	
					break;
				case "radio_esleep":
					System.out.println("The Radio Energy Sleeping: " + str[1]);	
					break;
				case "radio_elisten":
					System.out.println("The Radio Elisten is: " + str[1]);	
					break;
				case "radio_data_rate":
					System.out.println("The Radio data rate is: " + str[1]);	
					break;
				}						
			}
			br.close();
			System.out.println("------------------------------------");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listNodesParameters () {
		try {
			File nodeFolder = new File(Project.getProjectNodePath());
			File [] nodeFiles = nodeFolder.listFiles();
			int deviceType = -1;
			String line;
			for(int i=0; i<nodeFiles.length; i++){	
				if(!(nodeFiles[i].getName().split("_")[0].startsWith("."))) {
					BufferedReader br = new BufferedReader(new FileReader(nodeFiles[i]));
					line = br.readLine();
					line = br.readLine();
					line = br.readLine();
					deviceType = Integer.parseInt(line.split(":")[1]);					
					switch (deviceType) {
					case MapObject.SENSOR:
						listSensorParameters(nodeFiles[i].getAbsolutePath());						
						break;
					case MapObject.GAS:						
						listGasParameters(nodeFiles[i].getAbsolutePath());						
						break;
					case MapObject.BASE_STATION:
						listSensorParameters(nodeFiles[i].getAbsolutePath());						
						break;
					case MapObject.DIRECTIONAL_SENSOR:
						listSensorParameters(nodeFiles[i].getAbsolutePath());						
						break;
					case MapObject.MOBILE:						
						listMobileparameters(nodeFiles[i].getAbsolutePath());
						break;
					}
					br.close();
				}				
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void listRadioParameters () {
			File radioFolder = new File(Project.getProjectRadioPath());
			File [] radioFiles = radioFolder.listFiles();
			for(int i=0; i<radioFiles.length; i++){	
				if(!(radioFiles[i].getName().split("_")[0].startsWith("."))) {
					listRadioModuleParameters(radioFiles[i].getAbsolutePath());
				}				
			}
	}
	
	public static void listMarkerParameters() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Project.getProjectMarkerPath()));
			String line;
			String[] str;
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				System.out.println("Marker longitude is: " + str[1] + "Marker latitude is: " + str[2] + "Marker elevation is: " + str[3] + "Device radius is: " + str[4]);
			}
			br.close();
			System.out.println("------------------------------------");
		} 
		catch (FileNotFoundException e) {
			System.out.println("No Markers!");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void gpsMoveTo(double lo, double la, double el) {
		longitude = lo;
		latitude = la ;
		elevation = el;
		if(geoZoneList!=null && geoZoneList.size()>0)
			if(getType()==Device.SENSOR) {
				VisibilityZones vz = new VisibilityZones((SensorNode) this);
				vz.run();
			}
		if(DeviceList.sensors.size()<=100)
			if(DeviceList.propagationsCalculated)					
					DeviceList.calculatePropagations();
	}
	
	public void moveTo(int cx, int cy, int z) {
		if (MapLayer.magnetic) {
			cx = cx - (cx % MapLayer.magnetic_step);
			cy = cy - (cy % MapLayer.magnetic_step);
		}
		Point p = new Point(cx, cy);
		GeoPosition gp = MapLayer.mapViewer.convertPointToGeoPosition(p);
		double ex = gp.getLongitude();
		double ey = gp.getLatitude();			
		
		setLongitude(ex - getDLongitude());
		setLatitude(ey - getDLatitude());
		
		if(geoZoneList!=null && geoZoneList.size()>0)
			if(getType()==Device.SENSOR) {
				VisibilityZones vz = new VisibilityZones((SensorNode) this);
				vz.run();
			}
		MapLayer.repaint();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void switchVisible() {
		visible = !visible;
	}
	
	public void incHide() {
		if (hide++ == 5)
			hide = 0;
	}
	
	public void incRadius(int v) {
		radius += v;
	}
	
	public void shift(double sLongitude, double sLatitude, double sElevation) {
		this.longitude += sLongitude;
		this.latitude += sLatitude;
		this.elevation += sElevation;
	}
		
}
