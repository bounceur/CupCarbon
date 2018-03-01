package interference;

import java.util.Random;

public class LoRaTransceiver {

	public static double getNumberOfReceivedErrorBits(String inputMsg, int spreadingFactor) {
		System.out.println(spreadingFactor);
		// The number of received error bits to return
		double errBits = 0;
		// ------------------------------------------------------
		// LoRa Modem Settings
		int CR = 0; // [0 1 2 3 4] Coding Rate
		double BW = 125e3; // [7.8khz - 500khz] % BandWidth
		double CodingRate = 4 / (CR + 4); // Coding Rate
		// ------------------------------------------------------
		// Parameters for Chirp Spread Spectrum Modulation
		int A = 1; // Signal amplitude
		double fMin = 0; // Minimum frequency
		double fMax = BW; // Maximum frequency
		double fs = 5 * fMax; // Sampling frequency 2*fmax
		double Tchirp = 1e-3; // Time for one Chirp
		double inter = 1 / fs; // inter = 1/fs = 1.6000e-006
		double[] timeInter = new double[626];
		timeInter[0] = fMin;
		for (int i = 1; i < 626; i++) {
			timeInter[i] = timeInter[i - 1] + inter; // The time interval of a
														// signal of a duration
														// of a symbol time
		}
		// ------------------------------------------------------
		// generating Chirp signal
		double b = (fMax - fMin) / Tchirp;
		double[] f = new double[626];
		double[] chirp = new double[626];
		for (int i = 0; i < 626; i++) {
			f[i] = timeInter[i] * b + fMin;
			chirp[i] = A * Math.cos(2 * Math.PI * f[i] * timeInter[i]); // generating
																		// Chirp
																		// Signal
		}
		// ------------------------------------------------------
		// LoRa Transmitter
		// ------------------------------------------------------
		int symbolSize = 4;
		byte[] binaryData = getStringToBinary(inputMsg);
		byte[] binarySource = new byte[64];
		if (binaryData.length >= 512)
			System.out.print("Too Long Message");
		else {
			for (int i = 0; i < 64; i++) {
				if (i < binaryData.length)
					binarySource[i] = binaryData[i];
				else
					binarySource[i] = 0;
			}
			int tx_b = binarySource.length;
			// ------------------------------------------------------
			// FEC EnCoding
			int CodInB = 0; // codInB = coder input bits
			int CodOutB = 0; // codOutB = coder output bits
			if (CR == 0) {
				CodInB = 4;
				CodOutB = 4;
			} else if (CR == 1) {
				CodInB = 4;
				CodOutB = 5;
			} else if (CR == 2) {
				CodInB = 2;
				CodOutB = 3;
			} else if (CR == 3) {
				CodInB = 4;
				CodOutB = 7;
			} else if (CR == 4) {
				CodInB = 1;
				CodOutB = 2;
			} else {
				System.out.println("CR value should be b/w 1-4");
			}

			byte[] codedSource = null;
			codedSource = encoder(CR, binarySource, CodInB, CodOutB);

			// Performing NRZ
			int codedSourceNRZ[] = new int[tx_b];
			for (int i = 0; i < tx_b; i++) {
				codedSourceNRZ[i] = 2 * codedSource[i] - 1;
			}
			// ------------------------------------------------------
			// InterLeaving
			int codedSourceNRZmat[][] = new int[tx_b / symbolSize][CodOutB];
			for (int i = 0; i < tx_b / symbolSize; i++) {
				for (int j = 0; j < CodOutB; j++) {
					codedSourceNRZmat[i][j] = codedSourceNRZ[(i * CodOutB) + j]; // first
																					// converting
																					// coded
																					// data
																					// into
																					// matrix
				}
			}
			int[][] interleavedSource = new int[tx_b / symbolSize][CodOutB];
			for (int i = 0; i < tx_b / symbolSize; i++) {
				for (int j = 0; j < CodOutB; j++) {
					interleavedSource[i][j] = codedSourceNRZmat[(i + j) % (tx_b / symbolSize)][j]; // Interleaving
				}
			}
			int[] interleavedSourceSerial = new int[tx_b];
			int ss = 0;
			for (int i = 0; i < tx_b / symbolSize; i++) {
				for (int j = 0; j < CodOutB; j++) {
					interleavedSourceSerial[ss++] = interleavedSource[i][j]; // Again
																				// converting
																				// into
																				// serial
				}
			}
			// ------------------------------------------------------
			// Spreading
			int codelength = (int) Math.pow(2, spreadingFactor); // Length of generated code
													// bits
			double[] spreadingCode = getcode(codelength);
			int[] spreadedSeq = new int[tx_b * codelength];
			for (int i = 0; i < tx_b; i++) {
				for (int j = 0; j < codelength; j++) {
					spreadedSeq[i * codelength + j] = (int) (interleavedSourceSerial[i] * spreadingCode[j]);
				}
			}
			// ------------------------------------------------------
			// Chirp Modulation
			int rows = spreadedSeq.length;
			int cols = chirp.length;
			double[][] tx_modulated = new double[rows][cols];
			double[] txSignal = new double[rows * cols];
			for (int i = 0; i < spreadedSeq.length; i++) {
				for (int j = 0; j < chirp.length; j++) {
					tx_modulated[i][j] = spreadedSeq[i] * chirp[j];
					txSignal[i * cols + j] = tx_modulated[i][j];
				}
			}
			// ------------------------------------------------------
			// AWGN Channel
			// ------------------------------------------------------
			double[] rxSignal = AWGNchannel(spreadingFactor, CodingRate, spreadingCode, txSignal);
			// ------------------------------------------------------
			// LoRa Receiver
			// ------------------------------------------------------
			// De-Modulation
			double x;
			double[] rxDemodulated = new double[rxSignal.length / chirp.length];
			for (int i = 0; i < rxSignal.length / chirp.length; i++) {
				x = 0;
				for (int j = 0; j < chirp.length; j++) {
					x = x + rxSignal[i * chirp.length + j] * chirp[j];
				}
				rxDemodulated[i] = x;
			}
			// De-Spreading
			double[] rxDespread = new double[rxDemodulated.length / codelength];

			for (int i = 0; i < rxDemodulated.length / codelength; i++) {
				x = 0;
				for (int j = 0; j < codelength; j++) {
					x = x + spreadingCode[j] * rxDemodulated[i * codelength + j];
				}
				rxDespread[i] = x / codelength;
			}
			// De-Interleaving
			double[][] rxDespreadMat = new double[rxDespread.length / symbolSize][symbolSize];
			for (int i = 0; i < rxDespread.length / symbolSize; i++) {
				for (int j = 0; j < symbolSize; j++) {
					rxDespreadMat[i][j] = rxDespread[(i * symbolSize) + j]; // first
																			// converting
																			// de-spread
																			// data
																			// into
																			// matrix
				}
			}
			double[][] rxDeInterleave = new double[rxDespread.length / symbolSize][symbolSize];
			double s = rxDespread.length / symbolSize;
			for (int j = 0; j < symbolSize; j++) {
				for (int i = 0; i < rxDespread.length / symbolSize; i++) {
					if (i < j) {
						s = (rxDespread.length / symbolSize - j) + i;

					} else {
						s = i - j;
					}
					rxDeInterleave[i][j] = rxDespreadMat[(int) s][j];
				}
			}
			// De-Whitening
			double[][] rxDeNRZ = new double[rxDespread.length / symbolSize][symbolSize];
			for (int i = 0; i < rxDespread.length / symbolSize; i++) {
				for (int j = 0; j < symbolSize; j++) {
					if (rxDeInterleave[i][j] > 0)
						rxDeNRZ[i][j] = 1;
					else
						rxDeNRZ[i][j] = 0;
				}
			}
			// parallel to serial conversion
			double[] receivedData = new double[rxDespread.length / symbolSize * symbolSize];
			int ss1 = 0;
			for (int i = 0; i < rxDespread.length / symbolSize; i++) {
				for (int j = 0; j < symbolSize; j++) {
					receivedData[ss1++] = rxDeNRZ[i][j];
				}
			}
			// Calculating if Packet is Received Correctly			
			byte[] cb = new byte[receivedData.length];
			for (int i = 0; i < receivedData.length; i++) {
				cb[i] = binarySource[i];
				errBits += (cb[i] == receivedData[i]) ? 0 : 1;
			}
		}
		// errBits = Error Bits in the received data
		// if errBits == 0 -> Packet Received Correctly
		// otherwise -> Packet Not Received Correctly
		return errBits ;
	}

	// ------------------------------------------------------
	// Other Functions
	// ------------------------------------------------------
	// Encoder
	public static byte[] encoder(int CR, byte[] binarySource, int CodInB, int CodOutB) {
		byte[] reg1 = { 0, 0, 0, 0, 0 };
		byte[] reg2 = { 0, 0, 0, 0 };
		byte[] reg3 = { 0, 0, 0, 0, 0 };
		byte[] reg4 = { 0, 0, 0, 0 };
		byte[][] codedSource = null;
		byte[] codedSourceOut = null;
		int nSymbols = binarySource.length / CodInB;
		codedSource = new byte[nSymbols][CodOutB];
		codedSourceOut = new byte[nSymbols * CodOutB];

		switch (CR) {
		// ------------------------------------------------------
		case 0:
			codedSourceOut = binarySource;
			break;
		case 1: // coding rate 4/5
			for (int i = 0; i < nSymbols; i++) {
				reg1[0] = binarySource[i * CodInB];
				reg2[0] = binarySource[i * CodInB + 1];
				reg3[0] = binarySource[i * CodInB + 2];
				reg4[0] = binarySource[i * CodInB + 3];

				byte xor1 = (byte) ((reg1[0] + reg1[1] + reg1[2] + reg1[3] + reg1[4]) % 2);
				byte xor2 = (byte) ((reg1[0] + reg1[2] + reg1[3] + reg1[4]) % 2);
				byte xor3 = (byte) ((reg2[0] + reg2[1] + reg2[2] + reg2[3]) % 2);
				byte xor4 = (byte) ((reg3[0] + reg3[1] + reg3[3] + reg3[4]) % 2);
				byte xor5 = (byte) ((reg3[0] + reg3[2] + reg3[4]) % 2);
				byte xor6 = (byte) ((reg4[0] + reg4[2] + reg4[3]) % 2);
				byte xor7 = (byte) ((reg4[0] + reg4[1] + reg4[3]) % 2);

				codedSource[i][0] = xor1;
				codedSource[i][1] = (byte) ((xor2 + xor3) % 2);
				codedSource[i][2] = (byte) ((xor3 + xor4) % 2);
				codedSource[i][3] = (byte) ((xor5 + xor6) % 2);
				codedSource[i][4] = xor7;

				// one-bit Right Shift of each register
				reg1[4] = reg1[3];
				reg3[4] = reg3[3];

				for (int j = 3; j > 0; j--) {
					reg1[j] = reg1[j - 1];
					reg2[j] = reg2[j - 1];
					reg3[j] = reg3[j - 1];
					reg4[j] = reg4[j - 1];
				}
			}
			break;

		// ------------------------------------------------------
		case 2: // coding rate 2/3
			for (int i = 0; i < nSymbols; i++) {
				reg1[0] = binarySource[i * CodInB];
				reg2[0] = binarySource[i * CodInB + 1];

				byte xor1 = (byte) ((reg1[0] + reg1[3] + reg1[4]) % 2);
				byte xor2 = (byte) ((reg1[0] + reg1[1] + reg1[2] + reg1[4]) % 2);
				byte xor3 = (byte) ((reg2[1] + reg2[3]) % 2);
				byte xor4 = (byte) ((reg2[0] + reg2[2] + reg2[3]) % 2);

				codedSource[i][0] = xor1;
				codedSource[i][1] = (byte) ((xor2 + xor3) % 2);
				codedSource[i][2] = xor4;

				// one-bit Right Shift of each register
				reg1[4] = reg1[3];

				for (int j = 3; j > 0; j--) {
					reg1[j] = reg1[j - 1];
					reg2[j] = reg2[j - 1];
				}
			}
			break;
		// ------------------------------------------------------
		case 3: // coding rate 4/7
			for (int i = 0; i < nSymbols; i++) {
				reg1[0] = binarySource[i * CodInB];
				reg2[0] = binarySource[i * CodInB + 1];
				reg3[0] = binarySource[i * CodInB + 2];
				reg4[0] = binarySource[i * CodInB + 3];

				byte xor1 = (byte) ((reg1[0] + reg1[1] + reg1[2] + reg1[3] + reg1[4]) % 2);
				byte xor2 = (byte) ((reg1[0] + reg1[2] + reg1[3] + reg1[4]) % 2);
				byte xor3 = (byte) ((reg2[0] + reg2[1] + reg2[2] + reg2[3]) % 2);
				byte xor4 = (byte) ((reg3[0] + reg3[2] + reg3[4]) % 2);
				byte xor5 = (byte) ((reg3[0] + reg3[1] + reg3[2] + reg3[4]) % 2);
				byte xor6 = (byte) ((reg4[0] + reg4[2] + reg4[3]) % 2);
				byte xor7 = (byte) ((reg4[0] + reg4[1] + reg4[3]) % 2);
				byte xor8 = (byte) ((reg1[0] + reg1[1] + reg1[3] + reg1[4]) % 2);
				byte xor9 = (byte) ((reg2[0] + reg2[2] + reg2[3]) % 2);
				byte xor10 = (byte) ((reg3[0] + reg3[1] + reg3[2] + reg3[3] + reg3[4]) % 2);

				codedSource[i][0] = xor1;
				codedSource[i][1] = (byte) ((xor2 + xor3) % 2);
				codedSource[i][2] = (byte) ((xor3 + xor4) % 2);
				codedSource[i][3] = (byte) ((xor5 + xor6) % 2);
				codedSource[i][4] = xor7;
				codedSource[i][5] = (byte) ((xor7 + xor8) % 2);
				codedSource[i][6] = (byte) ((xor9 + xor10) % 2);

				// one-bit Right Shift of each register
				reg1[4] = reg1[3];
				reg3[4] = reg3[3];

				for (int j = 3; j > 0; j--) {
					reg1[j] = reg1[j - 1];
					reg2[j] = reg2[j - 1];
					reg3[j] = reg3[j - 1];
					reg4[j] = reg4[j - 1];
				}
			}
			break;
		// ------------------------------------------------------
		case 4: // coding rate 1/2
			for (int i = 0; i < nSymbols; i++) {
				reg1[0] = binarySource[i * CodInB];

				codedSource[i][0] = (byte) ((reg1[0] + reg1[3] + reg1[4]) % 2);
				codedSource[i][1] = (byte) ((reg1[0] + reg1[1] + reg1[2] + reg1[4]) % 2);

				// one-bit Right Shift of each register
				for (int j = 4; j > 0; j--) {
					reg1[j] = reg1[j - 1];
				}
			}
			break;
		}
		if (CR != 0) {
			for (int i = 0; i < nSymbols; i++) {
				for (int j = 0; j < CodOutB; j++) {
					codedSourceOut[i * CodOutB + j] = codedSource[i][j];
				}

			}
		}
		return codedSourceOut;
	}

	// ------------------------------------------------------
	// Hadamard Code Generator
	public static double[][] generateHadamard(int codelength) {
		double[][] hadamard;
		hadamard = new double[codelength][codelength];
		hadamard[0][0] = 1;
		for (int k = 1; k < codelength; k += k) {
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < k; j++) {
					hadamard[i + k][j] = hadamard[i][j];
					hadamard[i][j + k] = hadamard[i][j];
					hadamard[i + k][j + k] = -1 * hadamard[i][j];
				}
			}
		}
		return hadamard;
	}

	public static double[] getcode(int codelength) {
		return generateHadamard(codelength)[codelength - 1];
	}

	// ------------------------------------------------------
	// AWGN Channel
	public static double[] AWGNchannel(int SF, double codingRate, double[] spreadingCode, double[] txSignal) {
		// Energy Profile
		double eB = 0.5; // Energy per bit also Energy per symbol Es
		double SNR = -60; // Signal to noise ratio
		double eBN0 = SNR - Math.log(codingRate * (SF / spreadingCode.length));
		double N0 = eB * Math.pow(10, (-eBN0 / 10)); // Noise power
		// ------------------------------------------------------
		double[] h = { 1.0, 0, 0, 0, 0, 0, 0, 0, 0.7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.3 };
		double[] sm = filter(h, txSignal);
		double[] channelSignal = new double[txSignal.length];
		for (int i = 0; i < txSignal.length; i++) {
			Random random = new Random();
			channelSignal[i] = Math.sqrt(N0) * random.nextGaussian() + sm[i];
		}
		return channelSignal;
	}

	// ------------------------------------------------------
	// Filter
	public static double[] filter(double[] h, double[] txSignal) {
		double[] filteredData;
		int n = txSignal.length;
		int m = Math.min(h.length, txSignal.length);
		filteredData = new double[n];
		for (int k = 0; k < n; k++) {
			filteredData[k] = 0;
			int v = k < m ? k + 1 : m;
			for (int i = 0; i < v; i++) {
				filteredData[k] += h[i] * txSignal[k - i];
			}
		}
		return filteredData;
	}

	// ------------------------------------------------------
	// converting Input String to Binary bits Function
	public static byte[] getStringToBinary(String inputMsg) {
		String s2 = "";
		for (int i = 0; i < inputMsg.length(); i++) {
			char c = inputMsg.charAt(i);
			s2 += String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0");
		}
		s2 = s2.replaceAll("0", "\0");
		s2 = s2.replaceAll("1", "\1");
		byte[] binarySource = s2.getBytes();
		return binarySource;
	}
}