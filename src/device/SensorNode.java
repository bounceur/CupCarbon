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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import battery.Battery;
import cupcarbon.CupCarbon;
import map.MapLayer;
import natural_events.Weather;
import project.Project;
import radio_module.RadioModule;
import senscript.SenScript;
import senscript.SenScriptAddCommand;
import sensorunit.SensorUnit;
import simulation.WisenSimulation;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public abstract class SensorNode extends DeviceWithRadio {
	
	protected SensorUnit sensorUnit;
	
	protected boolean messageLost = false;
	
	protected boolean comEdgeDrawn = false;
	protected Random rnd = new Random();
	protected double variation = rnd.nextGaussian();
		
	//
	protected int bufferSize = 102400;//100Ko or 1Mo=1048476;
	protected int bufferIndex = 0 ;
	protected byte [] buffer = new byte [bufferSize];
	protected boolean bufferReady = false;
	protected double drssi = 0; // rssi in distance (meter)
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public SensorNode() {
		super();
		
		addRadioModule("radio1", RadioModule.ZIGBEE_802_15_4);
		currentRadioModule = radioModuleList.get(0);
		
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery(this);
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
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
	public SensorNode(double x, double y, double z, double radius, double radioRadius, int id) {		
		super(x, y, z, radius, radioRadius, id);		
		addRadioModule("radio1", RadioModule.ZIGBEE_802_15_4);
		currentRadioModule = radioModuleList.get(0);	
		battery = new Battery(this);
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
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
	public SensorNode(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		addRadioModule("radio1", RadioModule.ZIGBEE_802_15_4);
		currentRadioModule = radioModuleList.get(0);	
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, suRadius, this);
		battery = new Battery(this);
		withRadio = true;
		withSensor = true;
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
	public SensorNode(String x, String y, String z, String radius, String radioRadius, String suRadius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), Double.valueOf(radioRadius), id);
		addRadioModule("radio1", RadioModule.ZIGBEE_802_15_4);
		currentRadioModule = radioModuleList.get(0);	
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, Double.valueOf(suRadius), this);
		battery = new Battery(this);
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
	}	 
	
	public void addRadioModule(String name, String sStandard) {
		int standard = RadioModule.getStandardByName(sStandard);		
		addRadioModule(name, standard);
	}
	
	public void addRadioModule(String name, int standard) {
		for(RadioModule radioModule : radioModuleList) {
			if(radioModule.getName().equals(name) || radioModule.getName().equals("")) 
				return ;
		}
		
		RadioModule radioModule = RadioModule.newRadioModule(this, name, standard);
		radioModuleList.add(radioModule);
	}
	
	
	public void addRadioModule(RadioModule radioModule) {
		for(RadioModule rm : radioModuleList) {
			if(rm.getName().equals(radioModule.getName()) || rm.getName().equals("")) 
				return ;
		}
		radioModuleList.add(radioModule);
	}
	
	public void calculateRadioSpace() {
		if(geoZoneList.isEmpty()) {
			nPoint = 60;	
			deg = 2.*Math.PI/nPoint;
			polyX = new int [nPoint];
			polyY = new int [nPoint];
			
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			
			int rayon = MapCalc.radiusInPixels(getCurrentRadioRangeRadius() * getCurrentRadioModule().getPl() / 100); 
			
			double r2=0;
			double r3=0;
			
			double i=0.0;
			for(int k=0; k<nPoint; k++) {				
				r2 = (rayon+variation)*Math.cos(i);
				r3 = (rayon+variation)*Math.sin(i);
				polyX[k]=(int)(x+r2);
				polyY[k]=(int)(y+r3);
				i+=deg;
			}
		}
		else {
			nPoint = 0;
			polyX = null;
			polyY = null;
		}
	}
	
	public void variateRadius() {
		variation = rnd.nextGaussian()/0.2;
	}

	/**
	 * Draw the (line) detection link
	 * 
	 * @param device
	 *            Device
	 * @param g
	 *            Graphics
	 */
	public void drawDetectionLink(Device device, Graphics g) {
		int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToPixelMapA(device.getLatitude(), device.getLongitude());
		int lx2 = coord[0];
		int ly2 = coord[1];

		g.setColor(Color.RED);
		g.drawLine(lx1, ly1, lx2, ly2);
	}
	
	@Override
	public void drawRadioRange(Graphics g) {
		if (visible) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(0.4f));

			if(hide == 0 || hide==5) {
				geoZoneList.setSelected(selected);
				geoZoneList.draw(g);
			}
						
			initDraw(g);
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = 10; 
	
			if (inside || selected) {
				g.setColor(Color.LIGHT_GRAY);
				if(selected) g.setColor(Color.GRAY);
				if(MapLayer.dark) g.setColor(Color.ORANGE);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);			
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}
			if(!isDead()) {
				calculateRadioSpace();
				if(hide == 0 || hide == 2 || hide == 3) {
					if (nPoint>0) {
						g.setColor(UColor.BLACK_TTRANSPARENT);
						if(MapLayer.dark == true) g.setColor(Color.GRAY);
						g.drawPolygon(polyX, polyY, nPoint);
					}
				}
				
				g.setColor(Color.DARK_GRAY);
				if(hide == 0 || hide==5) {	
					if (inside) {
						g.setColor(currentRadioModule.getRadioRangeColor2());
					} 
					else {						
						g.setColor(currentRadioModule.getRadioRangeColor1());
					}
					if(nPoint>0)
						g.fillPolygon(polyX, polyY, nPoint);
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g2) {
		if (visible) {
			
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.4f));
			
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = MapCalc.radiusInPixels(currentRadioModule.getRadioRangeRadius()) ; 
			int rayon2 = MapCalc.radiusInPixels(this.radius);

			if (selected) {
				g.setColor(Color.GRAY);
				if(nPoint == 0)
					g.drawOval(x - 2, y - 2, 4, 4);
				else
					g.drawOval(x - rayon - 6, y - rayon - 6, (rayon + 6) * 2, (rayon + 6) * 2);
				
			}

			drawRadius(x, y, rayon2, g);
			
			drawRadioRadius(x, y, rayon, g);

			drawTheCenter(g, x, y);
			
			if (drawBatteryLevel) {
				battery.draw(g, x, y);
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.fillRect(x-20, y-25, 6, 50);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(x-20, y-(int)(bufferIndex*1.0/bufferSize*100./2.)+25, 6, (int)(bufferIndex*1.0/bufferSize*100./2.));
				if(MapLayer.dark) g.setColor(Color.WHITE);
				g.drawRect(x-20, y-25, 6, 50);
				g.drawString("Buffer"+id+": " + bufferIndex+"/"+bufferSize, x-30, y+45);
			}
			
			drawId(x, y, g);
		}
	}
	
	public void drawTheCenter(Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.6f));
		int r ;
		if(!getGPSFileName().equals("")) {
			r = 6;
			//g.setColor(UColor.WHITE_TRANSPARENT);
			g.setColor(Color.ORANGE);			
			g.fillOval(x - r, y - r, r*2, r*2);
			g.setColor(UColor.BLACK_TTRANSPARENT);
			g.drawOval(x - r, y - r, r*2, r*2);
		}
		
		if (underSimulation) {
			g.setColor(new Color(38, 194, 27));
		} else {
			g.setColor(UColor.ORANGE);
			if(getScript() != null) {				
				if(getScript().isWaiting()) g.setColor(Color.RED);
			}
		}			

		if(getScriptFileName().equals(""))
			g.setColor(Color.LIGHT_GRAY);
		
		if(isDead())
			if(MapLayer.dark)
				g.setColor(Color.GRAY);
			else
				g.setColor(Color.GRAY);
		
		r = 3;
		g.fillOval(x - r, y - r, r*2, r*2);
		
		g.setColor(UColor.BLACK_TTRANSPARENT);
		g.drawOval(x - r, y - r, r*2, r*2);
	}	

	/**
	 * Set the battery
	 * 
	 * @param battery
	 */
	public void setBattery(Battery battery) {
		this.battery = battery;
	}

	@Override
	public String getName() {
		return getIdFL() + id;
	}		
	
	@Override
	public void loadScript() {
		script = new SenScript(this);	
		String projectScriptPath = Project.getProjectScriptPath() + File.separator + scriptFileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(projectScriptPath));
		
			String s = "";
			while ((s = br.readLine()) != null) {										
				addCommand(s);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("[CupCarbon ERROR] (S"+id+"): the SenScript file "+scriptFileName+ " does not exist.");
			CupCarbon.cupCarbonController.stopSimulation();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadScript(String fileName) {
		if(!fileName.endsWith(".csc"))
			fileName += ".csc";
		script = new SenScript(this);		
		String projectScriptPath = Project.getProjectScriptPath() + File.separator + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(projectScriptPath));
		
			String s = "";
			while ((s = br.readLine()) != null) {										
				addCommand(s);
			}
			br.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void loadScript2(String fileName, boolean reset) {
		if(!fileName.endsWith(".csc"))
			fileName += ".csc";
		if(reset)
			script.init();
		else
			script.init2();
		//script = new SenScript(this, true);
		String projectScriptPath = Project.getProjectScriptPath() + File.separator + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(projectScriptPath));
		
			String s = "";
			while ((s = br.readLine()) != null) {										
				addCommand(s);
			}
			br.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void addMessageToBuffer(String message) {		
		try {
			for(int i=0; i < message.length(); i++) {
				buffer[bufferIndex] = (byte) message.charAt(i);
				bufferIndex++;
				if(bufferIndex >= bufferSize) {
					System.err.println("S"+getId()+": ERROR FULL BUFFER!");
					break;
				}
			}		
			buffer[bufferIndex] = '\r';
			bufferIndex++;
		}
		catch(Exception e) {
			System.err.println("S"+getId()+" [EMPTY MESSAGE]");
		}		
	}
	
	public int readMessage(String var) {
		int i=0;
		String s ="";
		while(buffer[i]!='\r') {
			s += (char) buffer[i];
			i++;
		}
		
		WisenSimulation.simLog.add("S"+getId()+" is reading from its buffer \""+s+"\" and puts it in "+var);
		
		script.putVariable(var, s);

		int k = 0;
		for(int j=i+1;j<bufferSize; j++) {
			buffer[k] = buffer[j];
			k++;
		}
		bufferIndex = bufferIndex - (i+1);
		if(bufferIndex < 0){
			bufferIndex = 0;
			bufferReady = false ;
		}
		return i;
	}
	
	public int pickMessage(String var) {
		int i=0;
		String s ="";
		while(buffer[i]!='\r') {
			s += (char) buffer[i];
			i++;
		}
		WisenSimulation.simLog.add("S"+getId()+" pick from its buffer \""+s+"\" and put it in "+var);
		
		script.putVariable(var, s);

		return i;
	}
	
	public void initBuffer() {
		bufferIndex = 0 ;
		bufferReady = false;	
		for(int i=0; i<bufferSize; i++) {
			buffer[i] = '\r';
		}
	}
	
	public boolean dataAvailable() {
		verifyData();
		return bufferReady;
	}
	
	public void verifyData() {
		if((buffer[0]!='\r') && bufferIndex>0) {
			bufferReady = true;
		} 
		else {
			bufferReady = false ;
		}
	}	

	public int getDataSize() {
		int i=0;
		while(buffer[i]!='\r') {
			i++;
		}
		return i;
	}
	/**
	 * @return the sensor nodes having the same radio standard module
	 */
	public List<SensorNode> getSensorNodeNeighbors() {
		List<SensorNode> neighnodes = new LinkedList<SensorNode>();		
		for(SensorNode snode : DeviceList.sensors) {
			if(((radioDetect(snode))) && this!=snode && !this.isDead() && canCommunicateWith(snode)) {
				neighnodes.add(snode);
			}
		}
		return neighnodes;
	}
	
	/**
	 * @return if a given node is a neighbor of the current node (this)
	 */
	public boolean isNeighborOf(SensorNode node) {		
		for(SensorNode neighbor : getSensorNodeNeighbors()) {
			if(node == neighbor)
				return true;
		}
		return false;
	}

	public byte [] getBuffer() {
		return buffer;
	}
	
	
	
	public boolean isRadioDetecting() {
		for(DeviceWithRadio d : DeviceList.sensors) {
			if(radioDetect(d) && this!=d) return true;
		}
		return false ;
	}
	
	public void addCommand(String instStr) {
		SenScriptAddCommand.addCommand(instStr.trim(), this, script);
	}
	
	public boolean isComEdgeDrawn() {
		return comEdgeDrawn;
	}
	
	public void setComEdgeDrawn(boolean comEdgeDrawn) {
		this.comEdgeDrawn = comEdgeDrawn;
	}
	
	@Override
	public void initForSimulation() {		
		super.initForSimulation();
		
		message = "";
		setMarked(false);
		setVisited(false);
					
		setLedColor(0);
		initBattery();
		
		this.getCurrentRadioModule().setPl(100);
		
		radioLinkColor = new Color(221,0,0,190);
		
		driftTime = 1.0;
		initBuffer();
		setDead(false);
		loadScript();
		getScript().init();
		setEvent(0);
		setEvent2(0);
		setEvent3(0);
		setAckReceived(false);
		setAckWaiting(false);
		this.setSending(false);
		this.setReceiving(false);
	}
	
	@Override
	public void init() {
		super.init();
		setSending(false);
		setReceiving(false);
		if(getScript()!=null) getScript().setWaiting(false);
		getCurrentRadioModule().setPl(100);
	}

	public int getBufferIndex() {
		return this.bufferIndex ;
	}

	@Override
	public void execute() {
		if (event == 0) {																	
			boolean cont = true;
			while (cont) {
				script.executeCommand();
				if (script.getEvent() == 0) {
					script.next();
				}
				else 
					cont = false;
			}
			WisenSimulation.consolPrint(event+" : ");
			event = script.getEvent();
			WisenSimulation.consolPrint(event+" | ");			
		}		
	}
	
	public String getSensorValues() {
		String s = "";
		boolean first = true; 
		for(Device device : DeviceList.devices) {
			if(!device.getClass().equals(Weather.class)) {
				if(detect(device)) {
					if (!first) {
						s += "#";
					}
					s += device.getId()+"#"+device.getValue();
					first = false;
				}
			}
		}
		if(s.equals(""))
			s = "X";
		else
			s = "S#"+s;
		return s ;
	}	
		
	public int getBufferSize() {
		return bufferSize;
	}

	public double [] getIntPosition() {
		double [] position = new double [3];
		int [] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		position[0] = coord[0];
		position[1] = coord[1];
		position[2] = 10;
		return position;
	}
	
	public double [] getPosition() {
		double [] position = new double [3];
		position[0] = latitude;
		position[1] = longitude;
		position[2] = elevation;
		return position;
	}

	public void setPosition(double d, double e, double z) {
		this.longitude = d ;
		this.latitude = e ;
		this.elevation = z ;
	}
	
	public boolean canSee(SensorNode sn) {
		GeoPosition gp1 = new GeoPosition(sn.getLatitude(), sn.getLongitude());
		Point2D p = MapLayer.mapViewer.getTileFactory().geoToPixel(gp1, MapLayer.mapViewer.getZoom());
		return geoZoneList.contains((Point) p);
	}
	
	public boolean isSensorDetecting() {
		for(Device d : DeviceList.devices) {
			if(!d.getClass().equals(Weather.class))
				if(detect(d) && this!=d) return true;
		}
		return false ;
	}
	
	public SensorUnit getSensorUnit() {
		return sensorUnit;
	}
	
	public void setSensorUnit(SensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit; 
	}
	
	public boolean detect(Device device) {
		return sensorUnit.detect(device);
	}
	
	public void setSensorUnitRadius(double radius) {
		getSensorUnit().setRadius(radius);
	}
	
	public abstract SensorNode createNewWithTheSameType() ;
	
	@Override
	public SensorNode duplicate() {
		selected = false;
		SensorNode nSensor = createNewWithTheSameType();
		nSensor.setHide(hide);
		nSensor.setDrawBatteryLevel(drawBatteryLevel);
		nSensor.setSensorUnitRadius(sensorUnit.getRadius());
		nSensor.getBattery().setEMax(getBattery().getEMax());
		nSensor.setScriptFileName(scriptFileName);
		nSensor.setGPSFileName(gpsFileName);

		nSensor.getRadioModuleList().removeAll(nSensor.getRadioModuleList());
		
		for(RadioModule radioModule : radioModuleList) {
			RadioModule nRadioModule = radioModule.duplicate(nSensor);
			nSensor.addRadioModule(nRadioModule);
			
			if (radioModule.getName().equals(getCurrentRadioModule().getName())) {
				nSensor.selectCurrentRadioModule(getCurrentRadioModule().getName());				
			}
		}
		nSensor.setSelected(true);
		return nSensor;
	}
	
	@Override
	public SensorNode duplicateWithShift(double sLongitude, double sLatitude, double sElevation) {
		SensorNode nSensor = duplicate();
		nSensor.shift(sLongitude, sLatitude, sElevation);
		return nSensor;
	}

	@Override
	public int getStandard() {
		return getCurrentRadioModule().getStandard();
	}
	
	public int getNumberOfNeighbors() {
		if(DeviceList.propagationsCalculated)
			return neighbors.size();
		return 0;
	}		

	public double getESensing() {
		return this.getSensorUnit().getESensing();
	}
		
	public abstract boolean detectBuildings();	
	public abstract String getParamsStr();

	public void setMessageLost(boolean messageLost) {
		this.messageLost = messageLost;
	}
	
	public boolean getMessageLost() {
		return messageLost;
	}
	
	public Vector<RadioModule> getRadioModuleList() {
		return radioModuleList;
	}
	
	public RadioModule getRadioModuleByName(String name) {
		for (RadioModule radioModule : radioModuleList) {
			if(radioModule.getName().equals(name)) return radioModule;
		}
		return null;
	}
	
	public void saveRadioModule(String fileName) {
		try {
			PrintStream fos = null;
			fos = new PrintStream(new FileOutputStream(fileName));
			fos.println(" List of radio Modules for the Sensor"+getId());
			fos.println("------------------------------------------");
			
			for (RadioModule radioModule : getRadioModuleList()) {
				radioModule.save(fos, currentRadioModule);
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	public double getDrssi() {
		return drssi;
	}

	public void setDrssi(double drssi) {
		this.drssi = drssi;
	}
	
	public GeoPosition getGeoCenter() {
		return new GeoPosition(latitude, longitude);
	}
	
	public double getSUCoverage() {
		return getSensorUnit().getCoverage();
	}
	
	public double getSUDirection() {
		return getSensorUnit().getDirection();
	}
	
}