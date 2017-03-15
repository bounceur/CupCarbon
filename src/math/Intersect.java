package math;

public class Intersect {

	public static boolean intersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		if(x1==x3 && y1==y3) return false;
		if(x1==x4 && y1==y4) return false;
		if(x2==x3 && y2==y3) return false;
		if(x2==x4 && y2==y4) return false;
		double dx2, dy2, dx3, dy3;
		dx2 = x2 - x1;
		dy2 = y2 - y1;
		dx3 = x4 - x3;
		dy3 = y4 - y3;

		double s, t;
		s = (-dy2 * (x1 - x3) + dx2 * (y1 - y3)) / (-dx3 * dy2 + dx2 * dy3);
		t = (dx3 * (y1 - y3) - dy3 * (x1 - x3)) / (-dx3 * dy2 + dx2 * dy3);
		
		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			return true;
		}
		return false;
	}
	
	public double [] pointIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double [] point = new double[2];
		point [0] = ((x3 * y4 - x4 * y3) * (x1 - x2) - (x1 * y2 - x2 * y1) * (x3 - x4)) / ((y1 - y2) * (x3 - x4) - (y3 - y4) * (x1 - x2));
		point [1] = point [0] * ((y1 - y2)/(x1 - x2)) + ((x1 * y2 - x2 * y1)/(x1 - x2));
		return point;
	}
	
}
