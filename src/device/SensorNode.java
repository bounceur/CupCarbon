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
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import battery.Battery;
import map.MapLayer;
import project.Project;
import script.Script;
import script.SensorAddCommand;
import utilities.MapCalc;
import utilities.UColor;
import wisen_simulation.SimLog;
import wisen_simulation.WisenSimulation;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */

public abstract class SensorNode extends DeviceWithRadio {
	
	//public static Channels channels = null;
	
	protected boolean comEdgeDrawn = false;
	protected Random rnd = new Random();
	protected double variation = rnd.nextGaussian();
	
	protected boolean ackOk = false; 

	//
	protected int bufferSize = 1024;
	protected int bufferIndex = 0 ;
	protected byte [] buffer = new byte [bufferSize];
	protected boolean bufferReady = false;	

	protected Color radioRangeColor1 = UColor.PURPLE_TRANSPARENT;
	protected Color radioRangeColor2 = UColor.PURPLE_DARK_TRANSPARENT;	
		
	/**
	 * Constructor 1 Instanciate the sensor unit 
	 * Instanciate the battery
	 */
	public SensorNode() {
		super();
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery();
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
	}
	
	public SensorNode(SensorNode sensorNode) {
		this.longitude = sensorNode.getLongitude();
		this.latitude = sensorNode.getLatitude();
		this.elevation = sensorNode.getElevation();
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
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery();
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
	public SensorNode(double x, double y, double z, double radius, double radioRadius,
			double suRadius, int id) {
		super(x, y, z, radius, radioRadius, id);
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, suRadius, this);
		battery = new Battery();
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
	public SensorNode(String x, String y, String z, String radius, String radioRadius,
			String suRadius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius),
				Double.valueOf(radioRadius), id);
		//sensorUnit = new SensorUnit(this.longitude, this.latitude, Double.valueOf(suRadius), this);
		battery = new Battery();
		withRadio = true;
		withSensor = true;
		calculateRadioSpace();
		initBuffer();
	}	
	
	public void calculateRadioSpace() {
		if(geoZoneList.isEmpty()) {
			nPoint = 30;	
			deg = 2.*Math.PI/nPoint;
			polyX = new int [nPoint];
			polyY = new int [nPoint];
			
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
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
		variation = rnd.nextGaussian()/0.8;
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

			if(hide == 0) {
				geoZoneList.setSelected(selected);
				geoZoneList.draw(g);
			}
			
			calculateRadioSpace();
			initDraw(g);
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = 10;//MapCalc.radiusInPixels(radioRangeRadius) ; 
	
			if (inside || selected) {
				g.setColor(UColor.BLUE);//.BLACK_TRANSPARENT);
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
				if(hide == 0 || hide == 2 || hide == 3) {
					if (nPoint>0) {
						g.setColor(UColor.BLACK_TTRANSPARENT);
						g.drawPolygon(polyX, polyY, nPoint);
					}
				}
				
				g.setColor(Color.DARK_GRAY);
				if(hide == 0 || hide==5) {	
					Point2D center = new Point2D.Float(x, y);
					float radius = 300;
					float[] dist = {0.0f, 0.5f};
					Color[] colors = {radioRangeColor1, UColor.TRANSPARENT};				
					if (inside) {
						colors[0] = radioRangeColor2;
						//g.setColor(radioRangeColor2);
					} 
//					else {
//						
//						//g.setColor(radioRangeColor1);
//					}
					RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
					g2.setPaint(p);
					if(nPoint>0)
						g.fillPolygon(polyX, polyY, nPoint);
					//drawSendingReceiving(g, x, y);								
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (visible) {			
			//calculateRadioSpace();
			//initDraw(g);
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = MapCalc.radiusInPixels(radioRangeRadius) ; 
			int rayon2 = MapCalc.radiusInPixels(this.radius);

			if (selected) {
				g.setColor(UColor.PURPLE);//.BLACK_TTRANSPARENT);
				if(nPoint == 0)
					g.drawOval(x - 8, y - 8, 16, 16);
				else
					g.drawOval(x - rayon - 8, y - rayon - 8, (rayon + 8) * 2, (rayon + 8) * 2);
				
			}

			drawIncRedDimNode(x, y, g);
			drawIncDimRadio(x, y, g);
			drawRadius(x, y, rayon2, g);
			
			drawRadioRadius(x, y, rayon, g);

			drawTheCenter(g, x, y);
			
			if (drawBatteryLevel) {
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.fillRect(x-30, y-25, 6, 50);
				g.setColor(UColor.GREEN);
				if (battery.getLevel()/battery.getInitialLevel()<0.5) g.setColor(UColor.ORANGE);
				if (battery.getLevel()/battery.getInitialLevel()<0.2) g.setColor(UColor.RED);
				//System.out.println();
				g.fillRect(x-30, y-(int)(battery.getLevel()/battery.getInitialLevel()*100./2.)+25, 6, (int)(battery.getLevel()/battery.getInitialLevel()*100./2.));
				g.setColor(Color.DARK_GRAY);
				g.drawRect(x-30, y-25, 6, 50);
				g.drawString("Battery"+id+": " + (int)battery.getLevel(), x-30, y+35);
				
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
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.6f));
		if (underSimulation) {
			g.setColor(UColor.GREEN);
		} else {
			g.setColor(UColor.RED);
		}					
		if(getScriptFileName().equals(""))
			g.setColor(Color.LIGHT_GRAY);
		
		if(isDead()) g.setColor(Color.BLACK);
		
		g.fillOval(x - 3, y - 3, 6, 6);		
		
		g.setColor(UColor.BLACK_TTRANSPARENT);
		g.drawOval(x - 3, y - 3, 6, 6);
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
	
	public void addMessageToBuffer(String message) {
//		setRxConsumption(1);
//		consumeRx(v);
//		initRxConsumption(); 
		
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
		List<SensorNode> neighnodes = new LinkedList<SensorNode>();		
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
	
	
	
	public boolean isRadioDetecting() {
		for(DeviceWithRadio d : DeviceList.getSensorNodes()) {
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
	/*
	public void drawSendingReceiving(Graphics g, int x, int y) {
		if(drawTxRx) {
			for(int i=0; i<nPoint; i++) {
				g.drawLine(x, y, polyX[0][i], polyY[0][i]);
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
						g.fillArc(polyX[0][i] - as, polyY[0][i] - as, as*2, as*2,180 - (int) alpha - as, as*2);
						as = 1;					
						g.fillArc(polyX[0][i] - as*sz, polyY[0][i] - as*sz, as*sz*2, as*sz*2,180 - (int) alpha - as, as*2);
						alpha += Math.toDegrees(deg);
					}
					
					if(sending) {
						g.setColor(Color.RED);
						as = 10;
						g.fillArc(polyX[0][i] - as, polyY[0][i] - as, as*2, as*2,180 + (int) alpha2 - as, as*2);
						as = 1;
						g.fillArc(polyX[0][i] - as*sz, polyY[0][i] - as*sz, as*sz*2, as*sz*2,180 + (int) alpha2 - as, as*2);
						alpha2 -= Math.toDegrees(deg);
					}
				}
			}
		}		
	}*/
	
	@Override
	public void initForSimulation() {		
		super.initForSimulation();
		
		message = "";
		setMarked(false);
		setVisited(false);
					
		setLedColor(0);
		initBattery();
		
		pl = 100;
		
		
		this.deltaDriftTime = 0.0;
		initBuffer();
		setDead(false);
		loadScript();
		getScript().init();
		setEvent(0);
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
	
	public abstract boolean detect(Device device);
	
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
	
	public boolean getAckOk() {
		return ackOk;
	}
	
	public void setAckOk(boolean ackOk) {
		this.ackOk = ackOk;
	}
		
	public int getBufferSize() {
		return bufferSize;
	}

	public boolean sameStandard(Device device) {
		return (getStandard() == device.getStandard());
	}
	
	public boolean canCommunicateWith(SensorNode rNode) {
		return (!rNode.isDead() && sameCh(rNode) && sameNId(rNode) && sameStandard(rNode));
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
		Point2D p = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp1, MapLayer.getMapViewer().getZoom());
		return geoZoneList.contains((Point) p);
	}
	
	public boolean isSensorDetecting() {
		for(Device d : DeviceList.getNodes()) {
			if(detect(d) && this!=d) return true;
		}
		return false ;
	}
	
}