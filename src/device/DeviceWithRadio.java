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
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import battery.Battery;
import geo_objects.GeoZoneList;
import map.MapLayer;
import map.NetworkParameters;
import radio_module.RadioDetection;
import radio_module.RadioModule;
import simulation.SimulationInputs;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public abstract class DeviceWithRadio extends DeviceWithWithoutRadio {
		
	protected double porteeErr = .4 ;
	protected Random random = new Random() ;
	
	protected Vector<RadioModule> radioModuleList = new Vector<RadioModule>();	
	protected RadioModule currentRadioModule = null;
	
	protected Vector<SensorNode> neighbors = new Vector<SensorNode> () ;
	
	protected int nPoint = 30;	
	protected double deg = 2.*Math.PI/nPoint;
	
	protected int [] polyX = new int [nPoint];
	protected int [] polyY = new int [nPoint];
	
	protected int numberOfNeighbors = 0;
	
	protected boolean ackReceived = false;
	protected boolean ackWaiting = false;
	
	/**
	 * 
	 */
	public DeviceWithRadio() {
		this(0, 0, 0, 0, 0, -1);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @param radioRangeRadius
	 */
	public DeviceWithRadio(double x, double y, double z, double radius, double radioRangeRadius, int id) {
		super(x, y, z, radius, id);
		initGeoZoneList(); 
	}
	
	public void setGeoZoneList(GeoZoneList geoZoneList) {
		this.geoZoneList = geoZoneList ;
	}
	
	public void initGeoZoneList() {
		geoZoneList = new GeoZoneList();
	}
	
	public Polygon getRadioPolygon() {
		return new Polygon(polyX, polyY, nPoint);	
	}
	
	public boolean contains(Point2D p) {
		if(nPoint>0)
			return (getRadioPolygon().contains(p));
		return (geoZoneList.contains((Point) p));
	}
	
	public boolean contains(DeviceWithRadio device) {
		GeoPosition gp = new GeoPosition(device.getLatitude(), device.getLongitude());
		Point2D p = MapLayer.mapViewer.getTileFactory().geoToPixel(gp, MapLayer.mapViewer.getZoom());
		if(nPoint>0)
			return ((new Polygon(polyX, polyY, nPoint)).contains(p));
		return (geoZoneList.contains(p));
	}
	
	@Override
	public Battery getBattery() {
		return battery;
	}
	
	public double getRadioRadiusOri() {
		return currentRadioModule.getRadioRangeRadiusOri() ;
	}
	
	public void setRadioRadius(double radioRadius) {
		currentRadioModule.setRadioRangeRadius(radioRadius) ;
	}

	@Override
	public void initSelection() {		
		super.initSelection() ;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 */
	public void drawRadioRadius(int x, int y, int r, Graphics g) {
		this.getCurrentRadioModule().drawRadioRadius(x, y, r, g);
	}
		
	@Override
	public void drawMarked(Graphics g2) {
		if (!isDead()) {
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.4f));
			if (ledColor==1) {	
				int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
				int x = coord[0];
				int y = coord[1];
				//g.setColor(Color.ORANGE);
				int r2 = 8;
				g.setColor(UColor.GREEND_TRANSPARENT);
				g.fillOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
				g.setColor(Color.GRAY);
				g.drawOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
			}

			if(ledColor>1) {				
				int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
				int x = coord[0];
				int y = coord[1];
				//g.setColor(Color.ORANGE);			
				int r2 = 8;
				g.setColor(UColor.colorTab[ledColor-1]);
				g.fillOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
				g.setColor(Color.GRAY);
				g.drawOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
			}
		}
	}
	
	public List<SensorNode> getNeighbors() {
		if(DeviceList.propagationsCalculated)
			return neighbors;
		else {
			List<SensorNode> neighnodes = new LinkedList<SensorNode>();
			//for(SensorNode sensorNode : DeviceList.sensors) {
			for(int i=0; i<DeviceList.sensors.size(); i++) {
				SensorNode sensorNode = DeviceList.sensors.get(i);
				if(radioDetect(sensorNode) && this!=sensorNode && !isDead() && !sensorNode.isDead()) {
					neighnodes.add(sensorNode);
				}
			}	
			return neighnodes;
		}
	}
	
	public List<SensorNode> getNonMarkedNeighbors() {
		if(DeviceList.propagationsCalculated)
			return neighbors;
		else {
			List<SensorNode> neighnodes = new LinkedList<SensorNode>();
			//for(SensorNode sensorNode : DeviceList.sensors) {
			for(int i=0; i<DeviceList.sensors.size(); i++) {
				SensorNode sensorNode = DeviceList.sensors.get(i);
				if(radioDetect(sensorNode) && this!=sensorNode && !isDead() && !sensorNode.isDead() && !sensorNode.isMarked()) {
					neighnodes.add(sensorNode);
				}
			}	
			return neighnodes;
		}
	}
	
	public List<SensorNode> getMarkedNeighbors() {
		if(DeviceList.propagationsCalculated)
			return neighbors;
		else {
			List<SensorNode> neighnodes = new LinkedList<SensorNode>();
			//for(SensorNode sensorNode : DeviceList.sensors) {
			for(int i=0; i<DeviceList.sensors.size(); i++) {
				SensorNode sensorNode = DeviceList.sensors.get(i);
				if(radioDetect(sensorNode) && this!=sensorNode && !isDead() && !sensorNode.isDead() && sensorNode.isMarked()) {
					neighnodes.add(sensorNode);
				}
			}	
			return neighnodes;
		}
	}
	
	public List<SensorNode> getActiveNodes() {
		List<SensorNode> neighActiveNodes = new LinkedList<SensorNode>(); 
		//for(SensorNode sensorNode : getNeighbors()) {
		for(int i=0; i<getNeighbors().size(); i++) {
			SensorNode sensorNode = getNeighbors().get(i);
			if(sensorNode.isSending())
				neighActiveNodes.add(sensorNode);
		}
		return neighActiveNodes;
	}
	
	public int getPerActiveNodes() {
		int n = getNeighbors().size();
		int p = 0;
		//for(SensorNode sensorNode : getNeighbors()) {
		for(int i=0; i<getNeighbors().size(); i++) {
			SensorNode sensorNode = getNeighbors().get(i);
			if(sensorNode.isSending())
				p++;
		}
		return (p/n);
	}
	
	public void displayNeighbors() {
		System.out.print(id+" : ");
		for (int i = 0; i < DeviceList.sensors.size(); i++) {
			if(this != DeviceList.sensors.get(i)) 
				if((DeviceList.sensors.get(i).radioDetect(this)) || (radioDetect((DeviceWithRadio)DeviceList.sensors.get(i)))) {
					System.out.print(DeviceList.sensors.get(i)+" ");
				}
		}
		System.out.println();
	}
	
	public void drawRadioLinks2(Graphics g) {
		numberOfNeighbors = 0;
		//for(SensorNode sensor : DeviceList.sensors) {
		for(int i=0; i<DeviceList.sensors.size(); i++) {
			SensorNode sensorNode = DeviceList.sensors.get(i);
			if(this!=sensorNode) {
				if(radioDetect(sensorNode) && !isDead() && !sensorNode.isDead()) {
					drawRadioLink(sensorNode, g, 1);
					numberOfNeighbors++;
					if (NetworkParameters.displayRLDistance) {
						MapLayer.drawDistance(longitude, latitude, elevation, sensorNode.getLongitude(), sensorNode.getLatitude(), sensorNode.getElevation(), g);
					}
				}
			}
		}
	}
	
	public void drawRadioLinks(int k1, Graphics g) {
		numberOfNeighbors = 0;
		//for(SensorNode sensor : DeviceList.sensors) {
		for(int i=0; i<DeviceList.sensors.size(); i++) {
			SensorNode sensor = DeviceList.sensors.get(i);
			if(this!=sensor) {
				if(radioDetect(sensor) && !isDead() && !sensor.isDead()) {
					drawRadioLink(sensor, g, 1);
					numberOfNeighbors++;
					if (NetworkParameters.displayRLDistance) {
						MapLayer.drawDistance(longitude, latitude, elevation, sensor.getLongitude(), sensor.getLatitude(), sensor.getElevation(), g);
					}
				}
			}
		}
	}
	
	public void drawRadioLinkArrows(Graphics g) {
		//for(SensorNode sensor : DeviceList.sensors) {
		for(int i=0; i<DeviceList.sensors.size(); i++) {
			SensorNode sensor = DeviceList.sensors.get(i);
			if(this!=sensor) {
				if(radioDetect(sensor) && !isDead() && !sensor.isDead()) {
					drawRadioLinkArrows(sensor, g, 1);
				}
			}
		}
	}
	
	public void drawRadioPropagations(Graphics g) {
		SensorNode sensor;
		for(int i=0; i<neighbors.size(); i++) {
			sensor = neighbors.get(i);
			drawRadioLink(sensor, g, 0);
			if (NetworkParameters.displayRLDistance) {
				MapLayer.drawDistance(longitude, latitude, elevation, sensor.getLongitude(), sensor.getLatitude(), sensor.getElevation(), g);
			}
		}
	}
	
	/**
	 * Draw the (line) radio link
	 * 
	 * @param device
	 *            Device
	 * @param g
	 *            Graphics
	 */
	public void drawRadioLink(Device device, Graphics g, int type) {				
		if(NetworkParameters.drawRadioLinks) {
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int lx1 = coord[0];
			int ly1 = coord[1];
			coord = MapCalc.geoToPixelMapA(device.getLatitude(), device.getLongitude());
			int lx2 = coord[0];
			int ly2 = coord[1];
			
			setColor(g, NetworkParameters.radioLinksColor);
			
			Graphics2D g2 = (Graphics2D) g;
			
			Stroke line = new BasicStroke(NetworkParameters.linkWidth);	//0.3
			//if(type==1)
			//	dashed = new BasicStroke(1.5f);
				//dashed = new BasicStroke(0.3f);
			//dashed = new BasicStroke(0.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3,3}, 0);
				
	        g2.setStroke(line);
			g2.drawLine(lx1, ly1, lx2, ly2);
		}
	}
	
	/**
	 * Draw the (line) radio link
	 * 
	 * @param device
	 *            Device
	 * @param g
	 *            Graphics
	 */
	public void drawRadioLinkArrows(Device device, Graphics g, int type) {
		if(NetworkParameters.drawRadioLinks) {	
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int lx1 = coord[0];
			int ly1 = coord[1];
			coord = MapCalc.geoToPixelMapA(device.getLatitude(), device.getLongitude());
			int lx2 = coord[0];
			int ly2 = coord[1];
			
			setColor(g, NetworkParameters.radioLinksColor);
			
			if(NetworkParameters.drawSensorArrows) {
				double dx = 0;
				double dy = 0;
				double alpha = 0;
				coord = MapCalc.geoToPixelMapA(latitude, longitude);
				lx1 = coord[0];
				ly1 = coord[1];		
				coord = MapCalc.geoToPixelMapA(device.getLatitude(), device.getLongitude());
				lx2 = coord[0];
				ly2 = coord[1];
				dx = lx2 - lx1;
				dy = ly2 - ly1;
				alpha = Math.atan(dy / dx);
				alpha = 180 * alpha / Math.PI;
				int as = 11;
				if (dx >= 0)	
					g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2,180 - (int) alpha - as, as*2);
				else
					g.fillArc((int) lx2 - as, (int) ly2 - as, as*2, as*2, -(int) alpha - as, as*2);
			}
		}
	}
	
	public void setColor(Graphics g, int intColor) {
		switch(intColor) {
		case 0 : g.setColor(UColor.DARK_GRAY_T); if(MapLayer.dark) g.setColor(UColor.LIGHT_GRAY_T); break;
		case 1 : g.setColor(UColor.BLACK_T); if(MapLayer.dark) g.setColor(UColor.DARK_GRAY_T); break;
		case 2 : g.setColor(UColor.GRAY_T); if(MapLayer.dark) g.setColor(UColor.DWHITE_T); break;		
		case 3 : g.setColor(UColor.REDD_TRANSPARENT); break;
		case 4 : g.setColor(UColor.BLUEMT); if(MapLayer.dark) g.setColor(UColor.BLUEM); break;
		case 5 : g.setColor(UColor.ORANGE_TRANSPARENT2); if(MapLayer.dark) g.setColor(UColor.ORANGE_TRANSPARENT); break;
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
		if(r>0 && displayRadius) {
			g.setColor(UColor.WHITE_TRANSPARENT);
			int lr2 = (int) (r*Math.cos(Math.PI/4.));
			g.drawLine(x,y,x-lr2,y-lr2);
			g.drawString(""+r,x-lr2,y-lr2);
		}
	}
	
	/**
	 * @param device
	 * @return if a neighbor device is in the radio area of the current device
	 */
	public boolean radioDetect(DeviceWithRadio device) {		
		if (!DeviceList.propagationsCalculated)
			return RadioDetection.simpleDetection(this, device);
		else {
			if (SimulationInputs.radioDetectionType == RadioDetection.POWER_RECEPTION_DETECTION)
				return RadioDetection.powerReceptionDetection(this, device);
			if (SimulationInputs.radioDetectionType == RadioDetection.THREED_DETECTION)
				return RadioDetection.threeDDetection(this, device);
		}
		return true;
	}
	
	public boolean radioDetect_vt(DeviceWithRadio device) {		
		if (!DeviceList.propagationsCalculated)
			return RadioDetection.simpleDetection(this, device);
		else {
			// YOU
		}
		return true;
	}
	
	/**
	 * @param device
	 * @return if a neighbor device is in the propagation area of the current device
	 */
	public boolean propagationDetect(DeviceWithRadio device) {
		if (DeviceList.propagationsCalculated)
			return neighbors.contains(device);
		else
			return radioDetect(device);
	}
	
	
	public void calculatePropagations() {
		SimulationInputs.radioDetectionType = RadioDetection.POWER_RECEPTION_DETECTION;
		neighbors = new Vector<SensorNode> () ;
		//for(SensorNode sensorNode : DeviceList.sensors) {
		for(int i=0; i<DeviceList.sensors.size(); i++) {
			SensorNode sensorNode = DeviceList.sensors.get(i);
			if(radioDetect(sensorNode) && !isDead() && !sensorNode.isDead()) {
				neighbors.add(sensorNode);
			}					
		}
	}
	
	public void resetPropagations() {
		SimulationInputs.radioDetectionType = RadioDetection.SIMPLE_DETECTION;
		neighbors = new Vector<SensorNode> () ;
	}
	
	public double getRequiredQuality() {
		return this.getCurrentRadioModule().getRequiredQuality();
	}

	public void setRequiredQuality(double requiredQuality) {
		this.getCurrentRadioModule().setRequiredQuality(requiredQuality);
	}

	public double getTransmitPower() {
		return this.getCurrentRadioModule().getTransmitPower();
	}

	public void setTransmitPower(double transmitPower) {
		this.getCurrentRadioModule().setTransmitPower(transmitPower);
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
	@Override
	public void drawId(int x, int y, Graphics g) {
		if (NetworkParameters.displayDetails) {
			g.setColor(UColor.PURPLE);
			if(MapLayer.dark) g.setColor(new Color(198,232,106));
			g.drawString(getName()+" ["+currentRadioModule.getMy()+"]", (int) (x + 10), (int) (y + 5));			
		}
		
		if(!scriptFileName.equals("") && NetworkParameters.drawScriptFileName) {
			g.setColor(Color.DARK_GRAY);
			if(MapLayer.dark) g.setColor(Color.LIGHT_GRAY);
			g.drawString(scriptFileName.substring(0, scriptFileName.indexOf('.')), (int) (x + 10), (int) (y - 6));
			
//			g.drawString("S : "+this.isSending(), (int) (x + 10), (int) (y + 26));
//			g.drawString("R : "+this.isReceiving(), (int) (x + 10), (int) (y + 36));
//			g.drawString("AW: "+this.isAckWaiting(), (int) (x + 10), (int) (y + 46));
//			g.drawString("AR: "+this.isAckReceived(), (int) (x + 10), (int) (y + 56));
		}
		
		if(NetworkParameters.displayPrintMessage) {
			g.setColor(new Color(28,64,123));
			if(MapLayer.dark) g.setColor(new Color(116,186,209));
			if(!message.equals("")) {				
				g.drawString(message, (int) (x + 10), (int) (y + 15));
			}
			else
				g.drawString(">", (int) (x + 10), (int) (y + 15));
		}
	}
	
	public void setMy(int my) {
		this.getCurrentRadioModule().setMy(my);
	}
	
	public void setPl(double  pl) {
		this.getCurrentRadioModule().setPl(pl);
	}
	
	public double getPl() {
		return this.getCurrentRadioModule().getPl();
	}
		
	public RadioModule getCurrentRadioModule() {
		return currentRadioModule;
	}
	
	public int getAttempts() {
		return currentRadioModule.getAttempts();
	}
	
	public void setAttempts(int attempts) {
		currentRadioModule.setAttempts(attempts);
	}
	
	public void incAttempts() {
		currentRadioModule.incAttempts();
	}
	
	public int getStandard() {
		return getCurrentRadioModule().getStandard();
	}
		
	public void selectCurrentRadioModule(String name) {
		for(RadioModule rm : radioModuleList) {
			if(rm.getName().equals(name)) {
				currentRadioModule = rm;
				return;
			}
		}
	}
	
	public void removeRadioModule(String name) {
		for(RadioModule rm : radioModuleList) {
			if(rm.getName().equals(name) && rm!=this.getCurrentRadioModule()) {
				radioModuleList.remove(rm);
				return;
			}
		}
	}
	
	public void removeRadioModule(RadioModule radioModule) {
		for(RadioModule rm : radioModuleList) {
			if(rm.getName().equals(radioModule.getName()) && rm!=this.getCurrentRadioModule()) {
				radioModuleList.remove(rm);
				return;
			}
		}
	}
	
	public double getTimeToResend() {
		return getCurrentRadioModule().getTimeToResend();
	}
	
	public void setTimeToResend(double timeToResend) {
		getCurrentRadioModule().setTimeToResend(timeToResend);
	}

	public int getNumberOfSends() {
		return getCurrentRadioModule().getNumberOfSends();
	}

	public void setNumberOfSends(int numberOfSends) {
		getCurrentRadioModule().setNumberOfSends(numberOfSends);
	}	
	
	
	/**
	 * consumeTx
	 * 
	 * @param v
	 */
	public void consumeTx(int v) {
		getCurrentRadioModule().consumeTx(v);
	}
	
	/**
	 * consumeRx
	 * 
	 * @param v
	 */
	public void consumeRx(int v) {
		getCurrentRadioModule().consumeRx(v);
	}
	
	
	public double getETx() {
		return getCurrentRadioModule().getETx();
	}

	public void setETx(double eTx) {
		getCurrentRadioModule().setETx(eTx);
	}

	public double getERx() {
		return getCurrentRadioModule().getERx();
	}

	public void setERx(double eRx) {
		getCurrentRadioModule().setERx(eRx);
	}
	
	public double getEL() {
		return getCurrentRadioModule().getEListen();
	}

	public void setEL(double eL) {
		getCurrentRadioModule().setEListen(eL);
	}	
	
	public void initRadioModule(){
		radioModuleList = new Vector<RadioModule>();
	}
	
	public double getCurrentRadioRangeRadius() {
		return currentRadioModule.getRadioRangeRadius();
	}
	
	/**
	 * @return the informations about the device
	 */
	public String[][] getInfos() {
		super.getInfos();
		infos[2][1] = ""+ getCurrentRadioModule().getMy();
		infos[3][1] = ""+ getCurrentRadioModule().getNId()+" ("+Integer.toHexString(getCurrentRadioModule().getNId()).toUpperCase()+")";
		infos[4][1] = ""+ getCurrentRadioModule().getCh()+" ("+Integer.toHexString(getCurrentRadioModule().getCh()).toUpperCase()+")";
		return infos;
	}

	@Override
	public void initForSimulation() {
		super.initForSimulation();
		//initRadioModule();
	}
	
	public boolean canCommunicateWith(DeviceWithRadio device) {
		return (
					!isDead() && 
					!device.isDead() &&
					sameCh(device) && 
					sameNId(device) && 
					sameStandard(device)
			);
	}
				
	public boolean sameCh(DeviceWithRadio device) {
		return (this.getCurrentRadioModule().getCh() == device.getCurrentRadioModule().getCh());
	}
	
	public boolean sameNId(DeviceWithRadio device) {
		return (this.getCurrentRadioModule().getNId() == device.getCurrentRadioModule().getNId());
	}
	
	public boolean sameStandard(DeviceWithRadio device) {
		return (getStandard() == device.getStandard());
	}
	
	public int getNumberOfNeighbors() {
		return numberOfNeighbors;
	}
	
	public boolean isAckReceived() {
		return ackReceived;
	}
	
	public void setAckReceived(boolean ackReceived) {
		this.ackReceived = ackReceived;
	}
	
	public boolean isAckWaiting() {
		return ackWaiting;
	}
	
	public void setAckWaiting(boolean b) {
		this.ackWaiting = b;
	}
	
}

