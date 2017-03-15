package math;

public class UFunctions {
	
	// ------------------------------------------------------
	// 2-bit decimal to complex conversion function (transmitter side)
	// ------------------------------------------------------
	
	// ------------------------------------------------------
	// Display an Array function
	// ------------------------------------------------------
	public static void display(byte[] t) {
		for (int i = 0; i < t.length; i++) {
			System.out.print(t[i]);
		}
		System.out.println();
	}
	
	// ------------------------------------------------------
	// Hamming Distance function
	// ------------------------------------------------------
	public static int getHammingDistance(String compOne, String compTwo) {
		if (compOne.length() != compTwo.length()) {
			return -1;
		}
		int counter = 0;
		for (int i = 0; i < compOne.length(); i++) {
			if (compOne.charAt(i) != compTwo.charAt(i))
				counter++;
		}
		return counter;
	}

	// ------------------------------------------------------
	// 2-bit decimal to complex conversion function (transmitter side)
	// ------------------------------------------------------
	public static Complex getComplexModulation(int v) {
		if (v == 0)
			return new Complex(0.7071, 0.7071);
		if (v == 1)
			return new Complex(0.7071, -0.7071);
		if (v == 2)
			return new Complex(-0.7071, 0.7071);
		if (v == 3)
			return new Complex(-0.7071, -0.7071);
		return null;
	}

	// ------------------------------------------------------
	// 0-3 decimal to 2-bit binary conversion function (receiver side)
	// ------------------------------------------------------
	public static String twoBitbinConversion(int w) {
		if (w == 0)
			return "00";
		if (w == 1)
			return "01";
		if (w == 2)
			return "10";
		if (w == 3)
			return "11";
		return "00";
	}

	// ------------------------------------------------------
	// complex to 0-3 decimal conversion function (receiver side)
	// ------------------------------------------------------
	public static int getComplexDemodulation(Complex u) {
		if (u.equals(new Complex(0.7071, 0.7071)))
			return 0;
		if (u.equals(new Complex(0.7071, -0.7071)))
			return 1;
		if (u.equals(new Complex(-0.7071, 0.7071)))
			return 2;
		if (u.equals(new Complex(-0.7071, -0.7071)))
			return 3;
		return 0;
	}
	
	// ------------------------------------------------------
	// Gamma Function
	// ------------------------------------------------------
	public static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
				+ 24.01409822 / (x + 2) - 1.231739516 / (x + 3) + 0.00120858003
				/ (x + 4) - 0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	public static double gamma(double x) {
		return Math.exp(logGamma(x));
	}

}
