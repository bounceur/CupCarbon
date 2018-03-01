package interference;

import java.util.Random;

import math.UFunctions;

public class AlphaStableRandom {

	public static double[] generateNext(int noTotalNodes, int radio_range, double perActiveNodes, int sizeOut) {
		// ------------------------------------------------------
		// Parameters of Alpha-Stable distribution
		// ------------------------------------------------------
		double b = 2.0;
		double alpha = 2.0 / b;
		double area = Math.PI * radio_range * radio_range; // circular area
															// around a single
															// node
		double activeNodes = perActiveNodes / 100;
		double lambda = (noTotalNodes / area) * activeNodes; // spatial density
																// of network
		double temp = 0; // constant in alpha-stable distribution formula
		double c1 = 0;
		double c2 = 0;
		double c3 = 0;

		if (alpha == 1) {
			temp = 2 / Math.PI;
		} else {
			c1 = 1 - alpha;
			c2 = UFunctions.gamma(Math.ceil(2 - alpha));
			c3 = Math.cos((Math.PI * alpha) / 2);
			temp = c1 / (c2 * c3);
		}
		double scale_para = 3.1623; // scale parameter of rayleigh distribution
		double expected_value_Q = Math.pow(2, 1 / b) * Math.pow(scale_para, 2.0 / b) * UFunctions.gamma((1 / b) + 1);
		double sigma = (lambda) * (Math.PI * temp) * (expected_value_Q);
		int beta = 0;
		int delta = 0;

		// ------------------------------------------------------
		// Alpha-Stable distribution Function
		// ------------------------------------------------------

		double[] outsizen = new double[sizeOut];
		double mean = 0.0, std = 1.0;
		Random rng = new Random();
		for (int i = 0; i < sizeOut; i++) {
			outsizen[i] = mean + std * rng.nextGaussian();
		}

		double[] outsize = new double[sizeOut];
		for (int i = 0; i < outsize.length; i++) {
			outsize[i] = Math.random();
		}
		double[] r = new double[sizeOut];
		double[] V = new double[sizeOut];
		double[] W = new double[sizeOut];
		double[] chunk1 = new double[sizeOut];
		double[] chunk2 = new double[sizeOut];
		double[] chunk3 = new double[sizeOut];
		double[] chunk4 = new double[sizeOut];
		double[] chunk5 = new double[sizeOut];
		double[] chunk6 = new double[sizeOut];
		double[] chunk7 = new double[sizeOut];

		if (alpha == 2) {
			for (int i = 0; i < sizeOut; i++) {
				r[i] = Math.sqrt(2) * outsizen[i];
			}
		} else if (alpha == 1 && beta == 0) { // Cauchy distribution
			for (int i = 0; i < sizeOut; i++) {
				r[i] = Math.tan(Math.PI / 2 * (2 * outsize[i] - 1));
			}
		} else if (alpha == .5 && Math.abs(beta) == 1) { // Levy distribution
															// (a.k.a. Pearson
															// V)
			for (int i = 0; i < sizeOut; i++) {
				r[i] = beta / Math.pow(outsizen[i], 2);
			}
		} else if (beta == 0) { // Symmetric alpha-stable
			for (int i = 0; i < sizeOut; i++) {
				V[i] = Math.PI / 2 * (2 * outsize[i] - 1);
				W[i] = -Math.log10(outsize[i]);
				chunk1[i] = Math.sin(alpha * V[i]);
				chunk2[i] = Math.pow(Math.cos(V[i]), (1 / alpha));
				chunk3[i] = Math.cos(V[i] * (1 - alpha));
				chunk4[i] = (1 - alpha) / alpha;
				chunk5[i] = chunk3[i] / W[i];
				chunk6[i] = Math.pow(chunk5[i], chunk4[i]);
				chunk7[i] = chunk2[i] * chunk6[i];
				r[i] = chunk1[i] / chunk7[i];
			}
		} else if (alpha != 1) { // General case, alpha not 1
			double constant = beta * Math.tan(Math.PI * alpha / 2);
			double B = Math.atan(constant);
			double S = Math.pow((1 + constant * constant), (1 / (2 * alpha)));
			for (int i = 0; i < sizeOut; i++) {
				V[i] = Math.PI / 2 * (2 * outsize[i] - 1);
				W[i] = -Math.log(outsize[i]);
				chunk1[i] = S * Math.sin(alpha * V[i] + B);
				chunk2[i] = Math.pow((Math.cos(V[i])), (1 / alpha));
				chunk3[i] = Math.cos((1 - alpha) * V[i] - B);
				chunk4[i] = chunk3[i] / W[i];
				chunk5[i] = ((1 - alpha) / alpha);
				chunk6[i] = Math.pow(chunk4[i], chunk5[i]);
				chunk7[i] = chunk2[i] * chunk6[i];

				r[i] = chunk1[i] / chunk7[i];
			}
		} else { // General case, alpha = 1
			double[] sclshftV = new double[sizeOut];
			double piover2 = Math.PI / 2;
			for (int i = 0; i < sizeOut; i++) {
				V[i] = Math.PI / 2 * (2 * outsize[i] - 1);
				W[i] = -Math.log(outsize[i]);
				sclshftV[i] = piover2 + beta * V[i];
				r[i] = 1 / piover2 * (sclshftV[i] * Math.tan(V[i])
						- beta * Math.log((piover2 * W[i] * Math.cos(V[i])) / sclshftV[i]));
			}
		}
		// Scale and shift
		if (alpha != 1) {
			for (int i = 0; i < sizeOut; i++) {
				r[i] = sigma * r[i] + delta;
			}
		} else {
			for (int i = 0; i < sizeOut; i++) {
				r[i] = sigma * r[i] + (2 / Math.PI) * beta * sigma * Math.log(sigma) + delta;
			}
		}

		return r;
	}

}
