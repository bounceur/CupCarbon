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

package natural_events;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import device.Device;
import device.DeviceList;
import device.DeviceWithRadio;
import device.MobileG;
import map.MapLayer;
import map.NetworkParameters;
import project.Project;
import utilities.MapCalc;
import utilities.UColor;

public class Weather extends MobileG {
	
	//http://a.basemaps.cartocdn.com/light_all/0/0/0.png
	protected LinkedList<Integer> valueTime;
	protected LinkedList<Double> values;
	protected int valueIndex = 0;

	private String idFL = "T"; // ID First Letter (Temperature of the weather)

	public Weather(double x, double y, double z, double radius) {
		super(x, y, z, radius, "", DeviceList.number++);
	}

	public Weather(double x, double y, double z, double radius, int id) {
		super(x, y, z, radius, "", id);
	}

	public Weather(double x, double y, double z, double radius, String gpsFileName, int id) {
		super(x, y, z, radius, gpsFileName, id);
	}

	public Weather(String x, String y, String z, String radius, String gpsFileName, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), gpsFileName, id);
	}


	public void draw(Graphics g2) {		
		if (visible) {
			Graphics2D g = (Graphics2D) g2;
			g.setStroke(new BasicStroke(0.6f));
			initDraw(g);
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			int radius = MapCalc.radiusInPixels(this.radius);

			if (inside || selected) {
				g.setColor(UColor.ORANGE);
				g.drawLine(x - radius - 30, y - radius - 30, x - radius - 25, y - radius - 30);
				g.drawLine(x - radius - 30, y - radius - 30, x - radius - 30, y - radius - 25);
				g.drawLine(x - radius - 30, y + radius + 30, x - radius - 25, y + radius + 30);
				g.drawLine(x - radius - 30, y + radius + 30, x - radius - 30, y + radius + 25);
				g.drawLine(x + radius + 30, y - radius - 30, x + radius + 25, y - radius - 30);
				g.drawLine(x + radius + 30, y - radius - 30, x + radius + 30, y - radius - 25);
				g.drawLine(x + radius + 30, y + radius + 30, x + radius + 25, y + radius + 30);
				g.drawLine(x + radius + 30, y + radius + 30, x + radius + 30, y + radius + 25);
			}

//			g.setColor(UColor.ORANGE_TRANSPARENT);
//			g.fillRoundRect(x-32, y-11, 64, 23, 15, 15);
//
//			g.setColor(UColor.ORANGE);
//			g.drawRoundRect(x-32, y-11, 64, 23, 15, 15);

			//Image image = new ImageIcon("src/images/sun.png").getImage();
			Image image = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/sun.png")).getImage();
			g.drawImage(image, x-50, y-50, null);
			
			if (selected) {
				g.setColor(Color.GRAY);
				//g.drawRect(x - radius -25, y - radius - 5, (radius + 25) * 2, (radius + 5) * 2);
				//g.drawRoundRect(x-32, y-11, 64, 23, 15, 15);
				g.drawOval(x-25, y-25, 50, 50);
			}
			
			drawRadius(x, y, radius, g);
			
			g.setColor(Color.BLACK);
			String s = String.format("%2.2f", getValue());
			g.drawString("WEATHER", x-20 , y-10 );
			g.drawString("" + s , x-8 , y+3 );

			if (this.nateventFileName.equals(""))
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.ORANGE);
			
			g.fillOval(x - 6, y + 7, 12, 12);
			
			g.setColor(Color.DARK_GRAY);
			g.drawOval(x - 6, y + 7, 12, 12);
			
		}
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
			g.setColor(Color.BLACK);
			if (MapLayer.dark)
				g.setColor(new Color(179, 221, 67));
			g.drawString(getName(), (int) (x + 10), (int) (y + 6));
		}
	}

	@Override
	public int getType() {
		return Device.WEATHER;
	}

	@Override
	public String getIdFL() {
		return idFL;
	}

	@Override
	public String getName() {
		return getIdFL() + id;
	}

	public void loadScript() {
	}

	public boolean isDead() {
		return false;
	}

	@Override
	public void execute() {
	}

	@Override
	public void drawRadioLinks(int k, Graphics g) {
	}

	@Override
	public void drawRadioPropagations(Graphics g) {
	}

	@Override
	public void initBattery() {

	}

	public Polygon getRadioPolygon() {
		return null;
	}

	@Override
	public void calculatePropagations() {
	}

	@Override
	public void resetPropagations() {
	}

	@Override
	public boolean radioDetect(DeviceWithRadio device) {
		return false;
	}

	@Override
	public void initGeoZoneList() {

	}

	@Override
	public void initBuffer() {

	}

	@Override
	public Weather duplicate() {
		selected = false;
		Weather gas = new Weather(longitude, latitude, elevation, radius);
		gas.setHide(hide);
		gas.setDrawBatteryLevel(drawBatteryLevel);
		gas.setScriptFileName(scriptFileName);
		gas.setSelected(true);
		gas.setGPSFileName(this.gpsFileName);
		gas.setNatEventFileName(nateventFileName);
		return gas;
	}

	@Override
	public Weather duplicateWithShift(double sLongitude, double sLatitude, double sElevation) {
		Weather gas = duplicate();
		gas.shift(sLongitude, sLatitude, sElevation);
		return gas;
	}

	@Override
	public double getSensorUnitRadius() {
		return 0;
	}

	@Override
	public double getESensing() {
		return 0;
	}

	@Override
	public void save(String fileName) {
		try {
			PrintStream fos = null;
			fos = new PrintStream(new FileOutputStream(fileName + File.separator + "weather_" + getId()));
			fos.println("List of parameters");
			fos.println("------------------------------------------");
			fos.println("device_type:" + getType());
			fos.println("device_id:" + getId());
			fos.println("device_longitude:" + getLongitude());
			fos.println("device_latitude:" + getLatitude());
			fos.println("device_elevation:" + getElevation());
			fos.println("device_radius:" + getRadius());
			fos.println("device_hide:" + getHide());
			if (!getGPSFileName().equals(""))
				fos.println("device_gps_file_name:" + getGPSFileName());
			if (!getNatEventFileName().equals(""))
				fos.println("natural_event_file_name:" + getNatEventFileName());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadValuesFromFile() {
		valueIndex = 0;
		valueTime = new LinkedList<Integer>();
		values = new LinkedList<Double>();
		FileInputStream fis;
		BufferedReader b = null;
		String s;
		String[] ts;
		try {
			if (!nateventFileName.equals("")) {
				fis = new FileInputStream(Project.getProjectNatEventPath() + File.separator + nateventFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				b.readLine();
				while ((s = b.readLine()) != null) {
					ts = s.split(" ");
					valueTime.add(Integer.parseInt(ts[0]));
					values.add(Double.parseDouble(ts[1]));
				}
				b.close();
				fis.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// 
	// ------------------------------------------------------------------------
	public void initForSimulation() {
		super.initForSimulation();
		loadValuesFromFile();
		valueIndex = 0;
		generateNextValue();
	}
	
	@Override 
	public void toOri() {
		super.toOri();
		value = 0;
		valueIndex = 0;
	}

	// ------------------------------------------------------------------------
	// Duration to the Next Time
	// ------------------------------------------------------------------------
	@Override
	public double getNextValueTime() {
		if (valueTime.size() > 0) {
			return valueTime.get(valueIndex);
		}
		return 0;
	}
	
	// ------------------------------------------------------------------------
	// Generate the next value (event)
	// ------------------------------------------------------------------------
	@Override
	public void generateNextValue() {
		if (valueTime != null) {			
			if ((valueIndex == (valueTime.size()))) {
				valueIndex = 0;
			}
			value = values.get(valueIndex);
			valueIndex++;
			if ((valueIndex >= (valueTime.size()))) {
				valueIndex = 1;
			}
		}
	}

}