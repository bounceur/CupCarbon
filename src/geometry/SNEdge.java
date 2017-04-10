package geometry;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import device.DeviceList;
import device.SensorNode;
import utilities.MapCalc;
import utilities.UColor;

public class SNEdge {

	private SensorNode sn1 = null;
	private SensorNode sn2 = null;

	public SNEdge() {
		super();
	}

	public SNEdge(SensorNode sn1, SensorNode sn2) {
		this.sn1 = sn1;
		this.sn2 = sn2;
	}
	
	public SNEdge(int id1, int id2) {
		sn1 = DeviceList.getSensorNodeById(id1);
		sn2 = DeviceList.getSensorNodeById(id2);
	}
	
	/**
	 * @return sn1
	 */
	public SensorNode getSN1() {
		return sn1;
	}

	/**
	 * @return sn21
	 */
	public SensorNode getSN2() {
		return sn2;
	}

	/**
	 * @param sn1
	 */
	public void setSN1(SensorNode sn1) {
		this.sn1 = sn1;
	}

	/**
	 * @param sn2
	 */
	public void setSN2(SensorNode sn2) {
		this.sn2 = sn2;
	}

	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(3.4f));
		int[] coord;
		coord = MapCalc.geoToPixelMapA(sn1.getLatitude(), sn1.getLongitude());
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToPixelMapA(sn2.getLatitude(), sn2.getLongitude());
		int lx2 = coord[0];
		int ly2 = coord[1];
		g.setColor(UColor.BLUEM);
		g.drawLine(lx1, ly1, lx2, ly2);
	}

	@Override
	public String toString() {
		return "Point [p1=" + sn1 + ", p2=" + sn2 + "]";
	}

}
