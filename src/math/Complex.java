package math;

public class Complex {

	private double real ;
    private double imaginary ;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public double real() {
    	return real; 
    }
    
    public double imaginary() {
    	return imaginary; 
    }

    public double abs() { 
    	return Math.hypot(real, imaginary); 
    }

    public double phase() {
    	return Math.atan2(imaginary, real); 
    }

    public Complex plus(Complex c) {
        double nreal = real + c.real;
        double nimaginary = imaginary + c.imaginary;
        return new Complex(nreal, nimaginary);
    }

    public Complex minus(Complex c) {
    	double nreal = real - c.real;
        double nimaginary = imaginary - c.imaginary;
        return new Complex(nreal, nimaginary);
    }

    public Complex times(Complex c) {
        double nreal = real * c.real - imaginary * c.imaginary;
        double nimaginary = real * c.imaginary + imaginary * c.real;
        return new Complex(nreal, nimaginary);
    }

    public Complex times(double alpha) {
        return new Complex(alpha * real, alpha * imaginary);
    }

    public Complex conjugate() {
    	return new Complex(real, -imaginary); 
    }

    public Complex reciprocal() {
        double scale = real*real + imaginary*imaginary;
        return new Complex(real / scale, -imaginary / scale);
    }

    public Complex divides(Complex c) {
        return times(c.reciprocal());
    }

    public Complex exp() {
        return new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
    }

    public Complex sin() {
        return new Complex(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
    }

    public Complex cos() {
        return new Complex(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
    }

    public Complex tan() {
        return sin().divides(cos());
    }
    
    public static Complex plus(Complex a, Complex b) {
        double real = a.real + b.real;
        double imaginaryag = a.imaginary + b.imaginary;
        Complex sum = new Complex(real, imaginaryag);
        return sum;
    }


    @Override
    public String toString() {
        if (imaginary == 0) return real + "";
        if (real == 0) return imaginary + "i";
        if (imaginary <  0) return real + " - " + (-imaginary) + "i";
        return real + " + " + imaginary + "i";
    }
    
}
