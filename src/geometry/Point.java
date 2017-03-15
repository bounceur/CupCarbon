package geometry;

import java.io.Serializable;

public class Point implements Serializable {

	private static final long serialVersionUID = 1L;

	private double x;	
	private  double y;
	public static Point lastPoint ;

	public Point() {
		super();
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the lastPoint
	 */
	public static Point getLastPoint() {
		return lastPoint;
	}

	/**
	 * @param lastPoint the lastPoint to set
	 */
	public static void setLastPoint(Point lastPoint) {
		Point.lastPoint = lastPoint;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	
}
