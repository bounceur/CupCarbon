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

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.Project;
import script.Script;
import script.SensorAddCommand;
import sensorunit.SensorUnit;
import utilities.MapCalc;
import utilities.UColor;
import wisen_simulation.SimLog;
import wisen_simulation.SimulationInputs;
import wisen_simulation.WisenSimulation;
import battery.Battery;
import flying_object.FlyingGroup;
import flying_object.FlyingObject;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */

public class SensorNode extends DeviceWithRadio {

	protected SensorUnit sensorUnit;
	protected int type = Device.SENSOR;
	protected boolean comEdgeDrawn = false;
	protected Random rnd = new Random();
	protected double variation = rnd.nextGaussian();	
	
	//
	protected int bufferSize = 1024;
	protected int bufferIndex = 0 ;
	protected byte [] buffer = new byte [bufferSize];
	protected boolean bufferReady = false;	

	protected Color radioRangeColor1 = UColor.MAUVE_TRANSPARENT;
	protected Color radioRangeColor2 = UColor.MAUVEF_TRANSPARENT;
	
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public SensorNode() {
		super();
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery(sensorUnit);
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
	public SensorNode(double x, double y, double radius, double radioRadius, int id) {
		super(x, y, radius, radioRadius, id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery(sensorUnit);
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
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public SensorNode(double x, double y, double radius, double radioRadius,
			double cuRadius, int id) {
		super(x, y, radius, radioRadius, id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, cuRadius, this);
		battery = new Battery(sensorUnit);
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
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
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param sb
	 *            A two dimensional table that contains a set of informations
	 *            about the sensor (temperature, co2, etc.) The first column
	 *            contains the name of the parameter The second column contains
	 *            the value of the corresponding parameter
	 */
	public SensorNode(double x, double y, double radius, double radioRadius,
			double cuRadius, String[][] sb, int id) {
		this(x, y, radius, radioRadius, cuRadius, id);
		this.setInfos(sb);
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
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 */
	public SensorNode(String x, String y, String radius, String radioRadius,
			String cuRadius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius),
				Double.valueOf(radioRadius), id);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, Double.valueOf(cuRadius),
				this);
		battery = new Battery(sensorUnit);
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
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
	 * @param cuRadius
	 *            Radius of the sensor unit (default value = 10 meters)
	 * @param gpsFileName
	 *            The path of the GPS file
	 * @param scriptFileName
	 *            The path of the script file
	 */
	public SensorNode(String id, String rdInfos, String x, String y, String radius, String radioRadius,
			String cuRadius, String gpsFileName, String scriptFileName) {
		this(x, y, radius, radioRadius, cuRadius, Integer.valueOf(id));
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
	

	public void calculateRadioSpace() {
		int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
		int x = coord[0];
		int y = coord[1];
		int rayon = MapCalc.radiusInPixels(radioRangeRadius * pl / 100) ; 
		
		double r2=0;
		double r3=0;
		
		//variateRadius();
		double i=0.0;
		for(int k=0; k<nPoint; k++) {
			//variateRadius();
			r2 = (rayon+variation)*Math.cos(i);
			r3 = (rayon+variation)*Math.sin(i);
			for (int j=0; j<nZone; j++) {
				polyX1[j][k]=(int)(x+r2*(nZone-j)/nZone);
				polyY1[j][k]=(int)(y+r3*(nZone-j)/nZone);
			}	
			i+=deg;
		}
	}
	
	public void variateRadius() {
		variation = rnd.nextGaussian()/0.8;
	}

	@Override
	public void setSensorUnitRadius(double captureRadio) {
		sensorUnit.setRadius(captureRadio);
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
		int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToIntPixelMapXY(device.getLongitude(), device.getLatitude());
		int lx2 = coord[0];
		int ly2 = coord[1];

		g.setColor(Color.RED);
		g.drawLine(lx1, ly1, lx2, ly2);
	}
	
	@Override
	public void drawRadioRange(Graphics g) {
		if (visible) {
			calculateRadioSpace();
			initDraw(g);
			int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = MapCalc.radiusInPixels(radioRangeRadius) ; 
	
			if (inside || selected) {
				g.setColor(UColor.NOIR_TRANSPARENT);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon + 2, y
						- rayon - 3);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon - 3, y
						- rayon + 2);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon + 2, y
						+ rayon + 3);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon - 3, y
						+ rayon - 2);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon - 2, y
						- rayon - 3);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon + 3, y
						- rayon + 2);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon - 2, y
						+ rayon + 3);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon + 3, y
						+ rayon - 2);
			}
			if(!isDead()) {
				g.setColor(Color.DARK_GRAY);
				if(hide == 0 || hide==4) {	
					if (inside) {
						g.setColor(radioRangeColor2);
					} 
					else {
						g.setColor(radioRangeColor1);
					}				
					for (int i=0; i<nZone; i++) 
						g.fillPolygon(polyX1[i], polyY1[i], nPoint);
					drawSendingReceiving(g, x, y);								
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (visible) {
			calculateRadioSpace();
			initDraw(g);
			int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = MapCalc.radiusInPixels(radioRangeRadius) ; 
			int rayon2 = MapCalc.radiusInPixels(this.radius);

			if (selected) {
				g.setColor(UColor.NOIR_TTRANSPARENT);
				g.drawOval(x - rayon - 8, y - rayon - 8, (rayon + 8) * 2, (rayon + 8) * 2);
			}

			drawIncRedDimNode(x, y, g);
			drawIncDimRadio(x, y, g);
			drawRadius(x, y, rayon2, g);
			
			drawRadioRadius(x, y, rayon, g);

			drawTheCenter(g, x, y);
			
			if (displayDetails && drawBatteryLevel) {
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.fillRect(x-30, y-25, 6, 50);
				g.setColor(UColor.GREEN);
				g.fillRect(x-30, y-(int)(battery.getLevel()/1000000./2.)+25, 6, (int)(battery.getLevel()/1000000./2.));
				g.setColor(Color.DARK_GRAY);
				g.drawRect(x-30, y-25, 6, 50);
				g.drawString("Battery"+id+": " + battery.getLevel(), x-30, y+35);
				
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.fillRect(x-20, y-25, 6, 50);
				g.setColor(UColor.RED);
				g.fillRect(x-20, y-(int)(bufferIndex*1.0/bufferSize*100./2.)+25, 6, (int)(bufferIndex*1.0/bufferSize*100./2.));
				g.setColor(Color.DARK_GRAY);
				g.drawRect(x-20, y-25, 6, 50);
				g.drawString("Buffer"+id+": " + bufferIndex+"/"+bufferSize, x-30, y+45);
			}

			drawMoveArrows(x, y, g);
			
			drawId(x, y, g);

		}
	}
	
	public void drawTheCenter(Graphics g, int x, int y) {
		if (underSimulation) {
			g.setColor(UColor.GREEN);
		} else {
			g.setColor(UColor.RED);
		}			
		if(isDead()) g.setColor(Color.BLACK);
		if(!getScriptFileName().equals(""))
			g.fillOval(x - 3, y - 3, 6, 6);
		
		g.setColor(UColor.NOIR_TTRANSPARENT);
		g.drawOval(x - 3, y - 3, 6, 6);
	}
	
	@Override
	public void drawSensorUnit(Graphics g) {
		int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
		int x = coord[0];
		int y = coord[1];
		if(hide == 0 || hide == 1) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 0, isSensorDetecting());
		}

		if(hide == 0 || hide == 3) {
			//g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			if (!isDead())
				g.drawPolygon(polyX1[0], polyY1[0], nPoint);
		}
		if(hide == 2) {
			sensorUnit.setPosition(x, y);
			sensorUnit.draw(g, 1, isSensorDetecting());
		}
	}

	@Override
	public double getSensorUnitRadius() {
		return sensorUnit.getRadius();
	}	

	// ------------------------------------------------------------------------
	// Return the type which is a SENSOR
	// In particular for this object (SensorNode) the type can be also 
	// a target
	// ------------------------------------------------------------------------
	@Override
	public int getType() {
		return type;
	}
	
	// Set type to SENSOR
	public void setAsSensor() {
		type = Device.SENSOR;
	}

	// Set type to SENSOR
	public void setAsTarget() {
		type = Device.TARGET;
	}


	@Override
	public String getIdFL() {
		return "S";
	}

	/**
	 * Set the capture unit
	 * 
	 * @param sensorUnit
	 */
	public void setCaptureUnit(SensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit;
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
	public SensorNode clone() throws CloneNotSupportedException {
		SensorNode newSensor = (SensorNode) super.clone();
		SensorUnit newCaptureUnit = (SensorUnit) sensorUnit.clone();
		Battery newBattery = (Battery) battery.clone();
		newSensor.setCaptureUnit(newCaptureUnit);
		newCaptureUnit.setNode(newSensor);
		newSensor.setBattery(newBattery);
		return newSensor;
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}		
	
	public void loadScript() {
		script = new Script(this);
		String projectTmpScriptPath = Project.getProjectScriptPath() + File.separator + scriptFileName;
		String tmp = projectTmpScriptPath;
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmp));
		
			String s = "";
			while ((s = br.readLine()) != null) {										
				addCommand(s);	
			}
			br.close();
			
		} catch (Exception e) {e.printStackTrace();}
	}


	protected boolean receivedEvent = false;		
	
	public void addMessageToBuffer(int v, String message) {
		setRxConsumption(1);
		consumeRx(v);
		initRxConsumption(); 
		
		try {
			for(int i=0; i< message.length(); i++) {
				buffer[bufferIndex] = (byte) message.charAt(i);
				bufferIndex++;
				if(bufferIndex >= bufferSize)
					System.err.println("S"+getId()+": ERROR FULL BUFFER!");
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
		
		SimLog.add("S"+getId()+" is reading from its buffer \""+s+"\" and puts it in "+var);
		
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
		SimLog.add("S"+getId()+" pick from its buffer \""+s+"\" and put it in "+var);
		
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
		if(buffer[0]!='\r') {
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
	
	public List<SensorNode> getSensorNodeNeighbors() {
		List<SensorNode> neighnodes = new ArrayList<SensorNode>();
		
		for(SensorNode snode : DeviceList.getSensorNodes()) {
			if(((radioDetect(snode))) && this!=snode && !this.isDead() && !snode.isDead() && sameCh(snode) && sameNId(snode)) {
				neighnodes.add(snode);
			}
		}
		return neighnodes;
	}

	public byte [] getBuffer() {
		return buffer;
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
	
	public boolean isRadioDetecting() {
		for(Device d : DeviceList.getNodes()) {
			if(radioDetect(d) && this!=d) return true;
		}
		return false ;
	}
	
	public boolean sameCh(SensorNode sn) {
		return (ch == sn.getCh());
	}
	
	public boolean sameNId(SensorNode sn) {
		return (nId == sn.getNId());
	}
	
	public void addCommand(String instStr) {
		SensorAddCommand.addCommand(instStr.trim(), this, script);
	}
	
	public boolean isComEdgeDrawn() {
		return comEdgeDrawn;
	}
	
	public void setComEdgeDrawn(boolean comEdgeDrawn) {
		this.comEdgeDrawn = comEdgeDrawn;
	}

	/**
	 * @param device
	 * @return if a device is in the sensor unit area of the current device
	 */
	public boolean detect(Device device) {		
		if(device.getType()==Device.FLYING_OBJECT) {
			for(FlyingObject d : ((FlyingGroup)device).getFlyingObjects()) {
				double dMax = getSensorUnitRadius() + d.getRadius() ;
				if ((distance(d) < dMax)) {
					return true;
				}
			}
			return false;
		}
		else {
			double dMax = getSensorUnitRadius() + device.getRadius() ;
			if ((distance(device) < dMax ) && device.getRadius()>0) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void drawSendingReceiving(Graphics g, int x, int y) {
		if(drawTxRx) {
			for(int i=0; i<nPoint; i++) {
				g.drawLine(x, y, polyX1[0][i], polyY1[0][i]);
			}
			if (sending || receiving) {
				double alpha = 0.0;
				double alpha2 = Math.toDegrees(3.14);
				int as = 10;
				int sz = 30;
				for(int i=0; i<nPoint; i++) {				
					if (sending) {
						g.setColor(Color.BLUE);
						as = 10;
						g.fillArc(polyX1[0][i] - as, polyY1[0][i] - as, as*2, as*2,180 - (int) alpha - as, as*2);
						as = 1;					
						g.fillArc(polyX1[0][i] - as*sz, polyY1[0][i] - as*sz, as*sz*2, as*sz*2,180 - (int) alpha - as, as*2);
						alpha += Math.toDegrees(deg);
					}
					if(sending) {
						g.setColor(Color.RED);
						as = 10;
						g.fillArc(polyX1[0][i] - as, polyY1[0][i] - as, as*2, as*2,180 + (int) alpha2 - as, as*2);
						as = 1;
						g.fillArc(polyX1[0][i] - as*sz, polyY1[0][i] - as*sz, as*sz*2, as*sz*2,180 + (int) alpha2 - as, as*2);
						alpha2 -= Math.toDegrees(deg);
					}
				}
			}
		}		
	}
	
	@Override
	public void initForSimulation() {
		DeviceList.initAll();
		initBuffer();
		setDead(false);		
		getBattery().init(SimulationInputs.energyMax);
		loadScript();
		getScript().init();
		setEvent(0);
		super.initForSimulation();
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

	@Override
	public void gotoTheNextInstruction() {
		if(!script.getCurrent().isExecuting()) {			
			script.next();
		}		
	}

	@Override
	public void gotoTheNextEvent(long min) {
		event = event - min;
	}
	
}