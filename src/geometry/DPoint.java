package geometry;

public class DPoint {

	private double x;	
	private  double y;

	public DPoint() {
		super();
	}

	public DPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public DPoint(String sx, String sy) {
		x = Double.parseDouble(sx);
		y = Double.parseDouble(sy);
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
