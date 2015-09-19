package script_functions;

public class Functions {

	public static String min(String [] args) {
		double min = Integer.MAX_VALUE;
		for(int i=0; i<args.length; i++) {
			if (Double.valueOf(args[i]) < min)
				min = Double.valueOf(args[i]);
		}
		return  ""+min;
	}
	
	public static String smin(String [] args) {
		double min = Integer.MAX_VALUE;
		String s = "";
		for(int i=0; i<args.length; i++) {
			String [] str = args[i].split("#");
			if (Double.valueOf(str[0]) < min) {
				min = Double.valueOf(str[0]);
				s = str[1];
			}
		}
		return  min+"#"+s;
	}
	
	public static String angle(String [] args) {
		if(args.length < 6)
			try {
				throw new Exception("angle (Functions class): argument number is < 6: "+args.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		double prec_X = Double.valueOf(args[0]);
		double prec_Y = Double.valueOf(args[1]);
		double local_X = Double.valueOf(args[2]);
		double local_Y = Double.valueOf(args[3]);
		double next_X = Double.valueOf(args[4]);
		double next_Y = Double.valueOf(args[5]);
		prec_X = prec_X - local_X;
		prec_Y = prec_Y - local_Y;
		next_X = next_X - local_X;
		next_Y = next_Y - local_Y;
		
		double b = 0;
		double a = 0;
		if(prec_X==next_X && prec_Y==next_Y) 
			b = Math.PI*2;
		else{
			a = Math.atan2(prec_X ,prec_Y);
			if (a < 0)
				a = (2 * Math.PI) + a;
			b = Math.atan2(next_X, next_Y);
			if (b < 0)
				b = (2 * Math.PI) + b;
			b = b - a;
			if (b < 0)
				b = (2 * Math.PI) + b;
		}
		return ""+b;
	}
	
	public static String myf(String [] args) {
		String valToReturn = "";
		
		// String s = args[0] ; // -> is the name of the first argument of the function myf
		// double x =  Double.valueOf(args[5]); // -> is the (double) value of the first argument of the function myf 
		
		//TODO
		// Your program here
		
		return valToReturn;
	}
	
}
