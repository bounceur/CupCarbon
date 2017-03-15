package solver;

import java.util.LinkedList;


public class Test {

	public static void main(String[] artgs) {
		//calculAngle();
		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(10);
		l.add(5);
		l.add(7);
		l.add(8);
		System.out.println(l);
		l.clear();
		System.out.println(l);
	}
	
	public void det() {
		int a = 2;
		int b = 1;
		int c = 1;
		int d = 2;
		int det = (a*d)-(c*b);
		System.out.println(det);
	}
	
	public static void calculAngle() {
		double x1 = -4.4416844844818115;
		double y1 = 48.39019447350543;
		
		double xc = -4.4424355030059814;
		double yc = 48.38995223896215;
		
		//double x2 = -4.442253112792969;
		//double y2 = 48.39051507627499;
		
		double x2 = -4.442886114120483;
		double y2 = 48.3903084658327;
		
		
		x1=-4.444001913070679; y1=48.39292309473598;
		xc=-4.444001913070679; yc=48.39292309473598;
		x2=-4.443175792694092; y2=48.39298721072846;
		System.out.println(x1-xc); 
		System.out.println(y1-yc);
		System.out.println(x2-xc); 
		System.out.println(y2-yc);
		
		//double a = getAngle(x1-xc, y1-yc, x2-xc, y2-yc);
		double a = getAngle2(x1-xc, y1-yc, x2-xc, y2-yc);
		System.out.println(Math.toDegrees(a));
	}
	
	public void calcul() {
		/*
		int [] t = {1, 0, 3, 9, 6, 2, 7}; 
		
		int min1=1000, min2=1000;
		int imin1=-1, imin2=-1;
		for(int i=0; i<6; i++) {
			if(t[i]<min1) {
				min2=min1;
				imin2=imin1;
				min1=t[i];
				imin1=i;
			}				
			if(t[i]<min2 && t[i]>min1) {
				min2=t[i];
				imin2=i;
			}
		}
		System.out.println(min1+" "+imin1);
		System.out.println(min2+" "+imin2);
		*/
		
		//48.39319389692116 48.39315098157692
		
		double x1 = -4.444012641906738 ;
		double y1 = 48.39287322568674 ;
		
		//double x2 = -4.442918300628662 ;
		//double y2 = 48.392559761972336 ;
		
		double x2 = -4.444152116775513 ;
		double y2 = 48.39201832009902 ;
		
		
		//4.991345542952473
		//0.16170730709900782
		
		double a = Math.atan2(x1, y1);
		if (a<0) a = (2*Math.PI)+a;		
		double b = Math.atan2(x2, y2);
		if (b<0) b = (2*Math.PI)+b;
		b = b - a;
		if (b<0) b = (2*Math.PI)+b; 
		
		System.out.println(b);
		System.out.println(Math.toDegrees(b));
		System.out.println();
		//double a1 = - x2 + (x1 + y1);
		//double a2 = x2 - (x1 - y1);
		int m = 2;
		double a1 = y1-m*(x2-x1);
		double a2 = y1+m*(x2-x1);
		double b1 = y1-(1./m)*(x2-x1);
		double b2 = y1+(1./m)*(x2-x1); 
		
		System.out.println(x1+" "+y1);
		System.out.println(x2+" "+y2);
		System.out.println(a1+" "+a2);
		System.out.println(b1+" "+b2);
		System.out.println("DROITE : "+(y2>b1 && y2<b2));
		System.out.println("  HAUT : "+(y2>a1 && y2>a2));
		System.out.println("GAUCHE : "+(y2<b1 && y2>b2));
		System.out.println("   BAS : "+(y2<a1 && y2<a2));
	}
	
	public static double getAngle(double x1, double x2, double y1, double y2) {
		double a = Math.atan2(x1, y1);
		if (a<0) a = (2*Math.PI)+a;		
		double b = Math.atan2(x2, y2);
		if (b<0) b = (2*Math.PI)+b;
		b = b - a;
		if (b<0) b = (2*Math.PI)+b; 
		return b;
	}
	
	public static double getAngle2(double x1, double y1, double x2, double y2) {
		x2 = x2 - x1 ;
		y2 = y2 - y1 ;		
		double a = Math.atan2(x2, y2);
		if (a<0) a = (2*Math.PI)+a;		
		return a;
	}

	// Returns 1 if the lines intersect, otherwise 0. In addition, if the lines 
		// intersect the intersection point may be stored in the doubls i_x and i_y.
		public static boolean get_line_intersection(double p0_x, double p0_y, double p1_x, double p1_y, 
		    double p2_x, double p2_y, double p3_x, double p3_y) {
		    double s1_x, s1_y, s2_x, s2_y;
		    s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
		    s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

		    double s, t;
		    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
		    t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

		    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
		    {
		        // Collision detected
		        return true;
		    }

		    return false; // No collision
		}
}
