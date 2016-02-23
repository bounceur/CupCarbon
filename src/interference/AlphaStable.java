/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2016 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/

/** 
 * @author Umber Noreen
 * @author Ahcene Bounceur
 */

package interference;

import java.util.Random;

public class AlphaStable {

	public static void main(String[] args) {
		double[] v = new double[500];

		double alpha = 0.5;
		int beta = 0;
		double gamma = 0.001;
		int delta = 0;

		v = rAStable(alpha, beta, gamma, delta, 500);
		for (int i = 0; i < 500; i++) {
			System.out.println(("" + v[i]).replaceAll(new String("\\."), ","));
		}
	}
	
	public static double[] rAStable(double alpha, int beta, double gamma, int delta, int sizeOut) {

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

		double[] outputVector = new double[sizeOut];
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
				outputVector[i] = Math.sqrt(2) * outsizen[i];
			}

		} else if (alpha == 1 && beta == 0) { // Cauchy distribution
			for (int i = 0; i < sizeOut; i++) {
				outputVector[i] = Math.tan(Math.PI / 2 * (2 * outsize[i] - 1));
			}
		} else if (alpha == .5 && Math.abs(beta) == 1) {
			// Levy distribution (a.k.a. Pearson V)
			for (int i = 0; i < sizeOut; i++) {
				outputVector[i] = beta / Math.pow(outsizen[i], 2);
			}
		} else if (beta == 0) {
			// Symmetric alpha-stable
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
				outputVector[i] = chunk1[i] / chunk7[i];
			}
		} else if (alpha != 1) {
			// General case, alpha not 1
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

				outputVector[i] = chunk1[i] / chunk7[i];
			}

		} else {
			// General case, alpha = 1
			double[] sclshftV = new double[sizeOut];
			double piover2 = Math.PI / 2;
			for (int i = 0; i < sizeOut; i++) {
				V[i] = Math.PI / 2 * (2 * outsize[i] - 1);
				W[i] = -Math.log(outsize[i]);
				sclshftV[i] = piover2 + beta * V[i];
				outputVector[i] = 1 / piover2 * (sclshftV[i] * Math.tan(V[i])
						- beta * Math.log((piover2 * W[i] * Math.cos(V[i])) / sclshftV[i]));
			}
		}

		// Scale and shift
		if (alpha != 1) {
			for (int i = 0; i < sizeOut; i++) {
				outputVector[i] = gamma * outputVector[i] + delta;
			}
		} else {
			for (int i = 0; i < sizeOut; i++) {
				outputVector[i] = gamma * outputVector[i] + (2 / Math.PI) * beta * gamma * Math.log(gamma) + delta;
			}
		}
		return outputVector;
	}
}
