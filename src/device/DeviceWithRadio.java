
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
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import battery.Battery;
import map.MapLayer;
import propagation.RadioDetection;
import utilities.MapCalc;
import utilities.UColor;
import visualisation.Visualisation;
import wisen_simulation.SimulationInputs;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public abstract class DeviceWithRadio extends DeviceWithWithoutRadio {
	protected Battery battery ;
	protected double porteeErr = .4 ;
	protected Random random = new Random() ;
	
	protected double radioRangeRadius = 0 ;
	protected double radioRangeRadiusOri = 0 ;
	protected boolean augmenterRadio = false ;
	protected boolean reduireRadio = false ;
	
	// ------ Simple Propagation
	public double requiredQuality = -80.0; // dB
	public double transmitPower = 0 ; // dBm
	
	protected int nZone = 1;	
	
	protected LinkedList<SensorNode> neighbors = new LinkedList<SensorNode> () ;
	
	protected int nPoint = 30;;	
	protected double deg = 2.*Math.PI/nPoint;
	
	protected int [][] polyX = new int[nZone][nPoint];
	protected int [][] polyY = new int[nZone][nPoint];
	
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
		this.radioRangeRadius = radioRangeRadius ;
		radioRangeRadiusOri = radioRangeRadius ;		
	}
	
	public Polygon getRadioPolygon() {
		return new Polygon(polyX[0],polyY[0],nPoint);
	}

	@Override
	public double getRadioRadius() {
		return radioRangeRadius;
	}
	
	@Override
	public Battery getBattery() {
		return battery;
	}
	
	public double getRadioRadiusOri() {
		return radioRangeRadiusOri ;
	}
	
	@Override
	public void setRadioRadius(double radioRadius) {
		this.radioRangeRadius = radioRadius ;
	}
	
	@Override 
	public void setSensorUnitRadius(double captureRadio) {}
	
	/* (non-Javadoc)
	 * @see device.Device#getMaxRadius()
	 */
	@Override
	public double getMaxRadius() {
		return Math.max(radius, getRadioRadius()) ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#getGPSFileName()
	 */
	@Override
	public String getGPSFileName() {
		return gpsFileName ;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param g
	 */
	public void drawIncDimRadio(int x, int y, Graphics g) {
		if(reduireRadio || augmenterRadio) {
			g.setColor(UColor.BLUE);
			g.drawLine(x-10, y-2, x+10, y-2);
			g.drawLine(x-10, y+2, x+10, y+2);
		}
		if(augmenterRadio) {
			g.drawLine(x-2, y-10, x-2, y+10);
			g.drawLine(x+2, y-10, x+2, y+10);
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		augmenterRadio = false ;
		reduireRadio = false ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		if (augmenterRadio) {			
			radioRangeRadius += 1;
			radioRangeRadiusOri += 1 ;
			MapLayer.getMapViewer().repaint();
		}
		if (reduireRadio) {
			if(radioRangeRadius>0) { 
				radioRangeRadius -= 1 ;
				radioRangeRadiusOri -= 1 ;
			}
			MapLayer.getMapViewer().repaint();
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent key) {
		super.keyTyped(key);
		
		if(selected) {
			if(key.getKeyChar()=='+') {
				move = false ;
				reduireRadio = false ;
				augmenterRadio = !augmenterRadio ;
				radioRangeRadius+=1 ;
				radioRangeRadiusOri+=1 ;
				//ThreeDUnityIHM.updatePosition(this);
				Visualisation.updateRadioRadius(Device.SENSOR, id, radioRangeRadius);
				MapLayer.getMapViewer().repaint();
			}
			if(key.getKeyChar()=='-') {
				move = false ;
				augmenterRadio = false ;
				reduireRadio = !reduireRadio ;
				if(radioRangeRadius>0) { 
					radioRangeRadius-=1 ;
					radioRangeRadiusOri-=1 ;
				}
				Visualisation.updateRadioRadius(Device.SENSOR, id, radioRangeRadius);
				MapLayer.getMapViewer().repaint();
				
			}
			if(key.getKeyChar() == 'a') {
				setDrawArrows(!getDrawArrows());
				MapLayer.getMapViewer().repaint();
			}
		}
					
	}
	
	@Override
	public void initSelection() {		
		super.initSelection() ;
		augmenterRadio = false ;
		reduireRadio = false ;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 */
	public void drawRadioRadius(int x, int y, int r, Graphics g) {
		if (r > 0 && displayRadius) {
			g.setColor(UColor.WHITE_TRANSPARENT);
			int lr1 = (int) (r * Math.cos(Math.PI / 4.));
			g.drawLine(x, y, (int) (x + lr1), (int) (y - lr1));
			//g.drawString("" +requiredQuality+" dB", x + (lr1 / 2), (int) (y - (lr1 / 4.)));
		}		
	}
		
	@Override
	public void drawMarked(Graphics g) {
		if (!isDead()) {
			if (ledColor==1) {	
				int[] coord = MapCalc.geoToIntPixelMapXY(latitude, longitude);
				int x = coord[0];
				int y = coord[1];
				g.setColor(Color.ORANGE);			
				int r1 = 12;
				int r2 = 8;
				if(hide == 0 || hide == 4) {
					g.drawOval(x-(r1+1), y-(r1+1), (r1+1)*2, (r1+1)*2);
					g.setColor(UColor.GREEND_TRANSPARENT);
					g.fillOval(x-(r1+1), y-(r1+1), (r1+1)*2, (r1+1)*2);
				}
				g.setColor(UColor.GREEND_TRANSPARENT);
				g.fillOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
				g.setColor(Color.GRAY);
				g.drawOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
			}

			if(ledColor>1) {				
				int[] coord = MapCalc.geoToIntPixelMapXY(latitude, longitude);
				int x = coord[0];
				int y = coord[1];
				g.setColor(Color.ORANGE);			
				int r1 = 12;
				int r2 = 8;
				if(hide == 0 || hide == 4) {
					g.drawOval(x-(r1+1), y-(r1+1), (r1+1)*2, (r1+1)*2);
					g.setColor(UColor.GREEND_TRANSPARENT);
					g.fillOval(x-(r1+1), y-(r1+1), (r1+1)*2, (r1+1)*2);
				}
				g.setColor(UColor.colorTab[ledColor-1]);
				g.fillOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
				g.setColor(Color.GRAY);
				g.drawOval(x-(r2+1), y-(r2+1), (r2+1)*2, (r2+1)*2);
			}
		}
	}	
	
//	public void calculateNeighbours() {
//		neighbors = new LinkedList<Device> () ;
//		for(SensorNode device : DeviceList.getSensorNodes()) {
//			if(radioDetect(device) && this!=device) {
//				if (!isDead() && !device.isDead()) 
//					neighbors.add(device);
//				if (device.radioDetect(this))
//					if (!this.isDead())
//						device.addNeighbor(this);
//					else
//						device.removeNeighbor(this);				
//			}
//		}
//		Layer.getMapViewer().repaint();
//	}
	
//	public void addNeighbor(Device device) {
//		if(!neighbors.contains(device) && !device.isDead()) {
//			neighbors.add(device);
//		}
//		//Layer.getMapViewer().repaint();
//	}
//	
//	public void removeNeighbor(Device device) {
//		if(neighbors.contains(device)) {
//			neighbors.remove(device);
//		}
//		//Layer.getMapViewer().repaint();
//	}
	
	public List<SensorNode> getNeighbors() {
		if(DeviceList.propagationsCalculated)
			return neighbors;
		else {
			List<SensorNode> neighnodes = new LinkedList<SensorNode>();
			for(SensorNode sensorNode : DeviceList.getSensorNodes()) {
				if(radioDetect(sensorNode) && this!=sensorNode && !isDead() && !sensorNode.isDead()) {
					neighnodes.add(sensorNode);
				}
			}	
			
//			for (int i = 0; i < DeviceList.size(); i++) {
//				if(this != DeviceList.getNodes().get(i)) 
//					if((DeviceList.getNodes().get(i).radioDetect(this)) || (radioDetect(DeviceList.getNodes().get(i)))) {
//						neighnodes.add(DeviceList.getNodes().get(i));
//					}
//			}
			return neighnodes;
		}
	}
	
	public void displayNeighbors() {
		System.out.print(id+" : ");
		for (int i = 0; i < DeviceList.size(); i++) {
			if(this != DeviceList.getNodes().get(i)) 
				if((DeviceList.getNodes().get(i).radioDetect(this)) || (radioDetect(DeviceList.getNodes().get(i)))) {
					System.out.print(DeviceList.getNodes().get(i)+" ");
				}
		}
		System.out.println();
	}
	
	public void drawRadioLinks(Graphics g) {
		for(Device device : DeviceList.getSensorNodes()) {
			if(radioDetect(device) && this!=device && !isDead() && !device.isDead()) {
				Visualisation.arrowDrawing((SensorNode)this, (SensorNode)device, 1, 0, 1);
				drawRadioLink(device, g, 1);
				if (MapLayer.displayRLDistance) {
					drawDistance(longitude, latitude, elevation, device.getLongitude(), device.getLatitude(), device.getElevation(), (int) distance(device), 0/*getPowerReception(device)*/, g);
				}
			}
		}
	}
	
	public void drawRadioPropagations(Graphics g) {
		for(Device device : neighbors) {
			Visualisation.arrowDrawing((SensorNode)this, (SensorNode)device, 0, 0, 1);
			drawRadioLink(device, g, 0);
			if (MapLayer.displayRLDistance) {
				drawDistance(longitude, latitude, elevation, device.getLongitude(), device.getLatitude(), device.getElevation(), (int) distance(device), 0/*getPowerReception(device)*/, g);
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
		
		if(drawRadioLinks) {	
			int[] coord = MapCalc.geoToIntPixelMapXY(latitude, longitude);
			int lx1 = coord[0];
			int ly1 = coord[1];
			coord = MapCalc.geoToIntPixelMapXY(device.getLatitude(), device.getLongitude());
			int lx2 = coord[0];
			int ly2 = coord[1];
			
			switch(drawRadioLinksColor) {
			case 0 : g.setColor(Color.DARK_GRAY); break;
			case 1 : g.setColor(Color.DARK_GRAY); break;
			case 2 : g.setColor(Color.LIGHT_GRAY); break;
			case 3 : g.setColor(Color.RED); break;
			case 4 : g.setColor(Color.BLUE); break;
			}
			
			Graphics2D g2 = (Graphics2D) g;
			Stroke dashed = new BasicStroke(0.6f);
			if(type==1)
				dashed = new BasicStroke(0.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
	        g2.setStroke(dashed);
			g.drawLine(lx1, ly1, lx2, ly2);
		
			

			if(drawArrows) {
				Visualisation.arrowDrawing((SensorNode)this, (SensorNode)device, type, drawRadioLinksColor, 1);
				double dx = 0;
				double dy = 0;
				double alpha = 0;
				coord = MapCalc.geoToIntPixelMapXY(latitude, longitude);
				lx1 = coord[0];
				ly1 = coord[1];		
				coord = MapCalc.geoToIntPixelMapXY(device.getLatitude(), device.getLongitude());
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

	public void drawDistance(double longitude1, double latitude1, double elevation1, double longitude2, double latitude2, double elevation2, int d, double pr, Graphics g) {
		int[] coord = MapCalc.geoToIntPixelMapXY(latitude1, longitude1);
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToIntPixelMapXY(latitude2, longitude2);
		int lx2 = coord[0];
		int ly2 = coord[1];
		g.setColor(Color.DARK_GRAY);		
		g.drawString("" + d + " ["+(int)pr+"]", ((lx1 + lx2) / 2), ((ly1 + ly2) / 2));
		Visualisation.drawText(((lx1 + lx2) / 2.), ((ly1 + ly2) / 2.), (elevation+elevation2)/2.);
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
	public boolean radioDetect(Device device) {		
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
	
	/**
	 * @param device
	 * @return if a neighbor device is in the propagation area of the current device
	 */
	public boolean propagationDetect(Device device) {
		if (DeviceList.propagationsCalculated)
			return neighbors.contains(device);
		else
			return radioDetect(device);
	}
	
	public void calculatePropagations() {
		SimulationInputs.radioDetectionType = RadioDetection.POWER_RECEPTION_DETECTION;
		neighbors = new LinkedList<SensorNode> () ;
		for(SensorNode device : DeviceList.getSensorNodes()) {
			if(radioDetect(device) && this!=device && !isDead() && !device.isDead()) {
				neighbors.add(device);
			}
			//neighbors.add(device);						
		}
	}
	
	public void resetPropagations() {
		SimulationInputs.radioDetectionType = RadioDetection.SIMPLE_DETECTION;
		neighbors = new LinkedList<SensorNode> () ;
	}
	
	public double getRequiredQuality() {
		return requiredQuality;
	}

	public void setRequiredQuality(double requiredQuality) {
		this.requiredQuality = requiredQuality;
	}

	public double getTransmitPower() {
		double tpW = (Math.pow(10, transmitPower/10.))/1000.;
		double nTpW = tpW * 1e6;
		nTpW = nTpW * pl /100.;
		double nTpDbm = 10*Math.log10(nTpW/1000.);
		return nTpDbm;
	}

	public void setTransmitPower(double transmitPower) {
		this.transmitPower = transmitPower;
	}

}
