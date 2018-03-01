package geometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;
import utilities.MapCalc;

public class DEdge2 {

	private DPoint p1 = null;
	private DPoint p2 = null;

	public DEdge2() {
		super();
	}

	public DEdge2(DPoint p1, DPoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public DEdge2(SensorNode sn1, SensorNode sn2) {
		p1 = new DPoint(sn1.getLatitude(), sn1.getLongitude());
		p2 = new DPoint(sn2.getLatitude(), sn2.getLongitude());
	}
	
	public DEdge2(int id1, int id2) {
		SensorNode sn1 = DeviceList.getSensorNodeById(id1);
		SensorNode sn2 = DeviceList.getSensorNodeById(id2);
		p1 = new DPoint(sn1.getLatitude(), sn1.getLongitude());
		p2 = new DPoint(sn2.getLatitude(), sn2.getLongitude());
	}
	
	public DEdge2(double x1, double y1, double x2, double y2) {
		p1 = new DPoint(x1, y1);
		p2 = new DPoint(x2, y2);
	}

	public DEdge2(String sx1, String sy1, String sx2, String sy2) {
		p1 = new DPoint(Double.parseDouble(sx1), Double.parseDouble(sy1));
		p2 = new DPoint(Double.parseDouble(sx2), Double.parseDouble(sy2));
	}

	/**
	 * @return p1
	 */
	public DPoint getP1() {
		return p1;
	}

	/**
	 * @return the p2
	 */
	public DPoint getP2() {
		return p2;
	}

	/**
	 * @param p1
	 */
	public void setP1(DPoint p1) {
		this.p1 = p1;
	}

	/**
	 * @param p2
	 */
	public void setP2(DPoint p2) {
		this.p2 = p2;
	}

	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(2.0f));
		int[] coord;
		coord = MapCalc.geoToPixelMapA(p1.getX(), p1.getY());
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToPixelMapA(p2.getX(), p2.getY());
		int lx2 = coord[0];
		int ly2 = coord[1];
		g.setColor(Color.BLUE);
		if(MapLayer.dark)
			g.setColor(Color.CYAN);
		g.drawLine(lx1, ly1, lx2, ly2);
	}

	@Override
	public String toString() {
		return "Point [p1=" + p1 + ", p2=" + p2 + "]";
	}

}
