package utilities;

import device.Device;
import geometry.DPoint;

public class Geometry {

	
	/**
	 * @param d1
	 * @param d2
	 * @param d3
	 * @return angle between (d2,d1) and (d2,d3) 
	 */
	public static double angle(Device d1, Device d2, Device d3) {
		double x1 = d1.getLatitude() - d2.getLatitude() ;
		double y1 = d1.getLongitude() - d2.getLongitude() ;
		double x2 = d3.getLatitude() - d2.getLatitude() ;
		double y2 = d3.getLongitude() - d2.getLongitude() ;
		double a = Math.atan2(x1, y1);
		if (a < 0)
			a = (2 * Math.PI) + a;
		double b = Math.atan2(x2, y2);
		if (b < 0)
			b = (2 * Math.PI) + b;
		b = b - a;
		if (b < 0)
			b = (2 * Math.PI) + b;
		return b;
	}
	
	public static boolean intersect(Device d0, Device d1, Device d2, Device d3) {
		double x0 = d0.getLatitude();
		double y0 = d0.getLongitude();
		double x1 = d1.getLatitude();
		double y1 = d1.getLongitude();
		double x2 = d2.getLatitude();
		double y2 = d2.getLongitude();
		double x3 = d3.getLatitude();
		double y3 = d3.getLongitude();
		if(x0==x2 && y0==y2) return false;
		if(x0==x3 && y0==y3) return false;
		if(x1==x2 && y1==y2) return false;
		if(x1==x3 && y1==y3) return false;
		double dx1, dy1, dx2, dy2;
		dx1 = x1 - x0;
		dy1 = y1 - y0;
		dx2 = x3 - x2;
		dy2 = y3 - y2;

		double s, t;
		s = (-dy1 * (x0 - x2) + dx1 * (y0 - y2)) / (-dx2 * dy1 + dx1 * dy2);
		t = (dx2 * (y0 - y2) - dy2 * (x0 - x2)) / (-dx2 * dy1 + dx1 * dy2);
		
		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			return true;
		}
		return false;
	}
	
	public static boolean intersect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		if(x0==x2 && y0==y2) return false;
		if(x0==x3 && y0==y3) return false;
		if(x1==x2 && y1==y2) return false;
		if(x1==x3 && y1==y3) return false;
		double dx1, dy1, dx2, dy2;
		dx1 = x1 - x0;
		dy1 = y1 - y0;
		dx2 = x3 - x2;
		dy2 = y3 - y2;

		double s, t;
		s = (-dy1 * (x0 - x2) + dx1 * (y0 - y2)) / (-dx2 * dy1 + dx1 * dy2);
		t = (dx2 * (y0 - y2) - dy2 * (x0 - x2)) / (-dx2 * dy1 + dx1 * dy2);
		
		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			return true;
		}
		return false;
	}
	
	public static boolean intersect(DPoint p0, DPoint p1, DPoint p2, DPoint p3) {
		double x0 = p0.getX();
		double y0 = p0.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double x3 = p3.getX();
		double y3 = p3.getY();
		if(x0==x2 && y0==y2) return false;
		if(x0==x3 && y0==y3) return false;
		if(x1==x2 && y1==y2) return false;
		if(x1==x3 && y1==y3) return false;
		double dx1, dy1, dx2, dy2;
		dx1 = x1 - x0;
		dy1 = y1 - y0;
		dx2 = x3 - x2;
		dy2 = y3 - y2;

		double s, t;
		s = (-dy1 * (x0 - x2) + dx1 * (y0 - y2)) / (-dx2 * dy1 + dx1 * dy2);
		t = (dx2 * (y0 - y2) - dy2 * (x0 - x2)) / (-dx2 * dy1 + dx1 * dy2);
		
		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			return true;
		}
		return false;
	}
	
	public static int [] getCentre(int x1, int y1, int z1, int x2, int y2, int z2) {
		int x = (int) (x2+((x1-x2)/2.0));
		int y = (int) (y2+((y1-y2)/2.0));
		int z = (int) (z1+((z2-z1)/2.0));
		int [] r = {x, y, z};
		return r;
	}
	
}
