package interference;

import math.Complex;
import math.FFT;

public class WiFiTransceiver {
	
	public static double  getNumberOfReceivedErrorBits(String inputMsg, int noNeghboringNodes, int radioRange, int perActiveNodes) {
		// The number of received error bits to return
		double errBits = 0;
		// ------------------------------------------------------
		// Wi-Fi Parameters
		// ------------------------------------------------------
		int signalConstellation = 16;
		int symbolSize = signalConstellation/4;     //number of bits per symbol
		int subChannels = 64;
		int cp = 2;                                //cyclic prefix length
		int sizeout = subChannels * 2 + cp;
		// ------------------------------------------------------
		// Binary Source Generation
		//number of bits to be generated in WiFi are = subChannels*symbolLen*2
		byte [] binaryData = getStringToBinary(inputMsg);
		byte [] binarySource = new byte [subChannels*symbolSize*2];
		if (binaryData.length>=subChannels*symbolSize*2)
			System.out.print("Too Long Message");
		else {
			for(int i=0; i<subChannels*symbolSize*2; i++){
				if (i<binaryData.length)
					binarySource[i]=binaryData[i];
				else
					binarySource[i] = 0;
			}	
			// ------------------------------------------------------
			// Wi-Fi Transmitter
			// ------------------------------------------------------
			int lengthDecNo = binarySource.length / symbolSize;        // length of decimal numbers
			int[] DecNumbers = new int[lengthDecNo]; 
			int j = 0;
			for (int i = 0; i < binarySource.length; i += 4) {
				String s = "" + binarySource[i] + binarySource[i + 1] + binarySource[i + 2] + binarySource[i + 3];
				DecNumbers[j] = Integer.parseInt(s, 2);
				j++;
			}
			// ------------------------------------------------------
			// Mapping of Decimal Numbers on complex numbers // QAM Modulation
			Complex[] qMapping = new Complex[16];
			qMapping[0]  = new Complex(-3, -3);
			qMapping[1]  = new Complex(-3, -1);
			qMapping[2]  = new Complex(-3, 3);
			qMapping[3]  = new Complex(-3, 1);
			qMapping[4]  = new Complex(-1, -3);
			qMapping[5]  = new Complex(-1, -1);
			qMapping[6]  = new Complex(-1, 3);
			qMapping[7]  = new Complex(-1, 1);
			qMapping[8]  = new Complex(3, -3);
			qMapping[9]  = new Complex(3, -1);
			qMapping[10] = new Complex(3, 3);
			qMapping[11] = new Complex(3, 1);
			qMapping[12] = new Complex(1, -3);
			qMapping[13] = new Complex(1, -1);
			qMapping[14] = new Complex(1, 3);
			qMapping[15] = new Complex(1, 1);
			Complex[] modulatedSource = new Complex[lengthDecNo];
			for (int i = 0; i < lengthDecNo; i++) {
				modulatedSource[i] = qMapping[DecNumbers[i]];
			}
			// ------------------------------------------------------
			// Taking subChannels*2 point IFFT then adding Cyclic Preix
			Complex[] IFFTsource = FFT.ifft(modulatedSource, subChannels*2); 
			Complex[] cyclicPrefixedSource = new Complex[IFFTsource.length + cp];
			for (int i = 0; i < IFFTsource.length; i++) {
				cyclicPrefixedSource[i + cp] = IFFTsource[i];
			}
			cyclicPrefixedSource[0] = IFFTsource[IFFTsource.length - 2];
			cyclicPrefixedSource[1] = IFFTsource[IFFTsource.length - 1];
			// ------------------------------------------------------
			// Alpha-Stable Channel
			// ------------------------------------------------------
			// generating alpha-stable random variables
			double [] alpharnd1 = AlphaStableRandom.generateNext(noNeghboringNodes, radioRange, perActiveNodes, sizeout);
			double [] alpharnd2 = AlphaStableRandom.generateNext(noNeghboringNodes, radioRange, perActiveNodes, sizeout);
			Complex [] alpharnd = new Complex[sizeout];
			// generating them twice and then adding them as complex function
			for (int i = 0; i < sizeout; i++) {
				alpharnd[i] = new Complex(alpharnd1[i], alpharnd2[i]);
			}
			Complex [] rx = new Complex[sizeout];
			double [] noise_real = new double[sizeout];
			double [] noise_imag = new double[sizeout];
			for (int i = 0; i < sizeout; i++) {
				noise_real[i] = cyclicPrefixedSource[i].re() + alpharnd[i].re();
				noise_imag[i] = cyclicPrefixedSource[i].im() + alpharnd[i].im();
				rx[i] = new Complex(noise_real[i], noise_imag[i]);
			}
			// ------------------------------------------------------
			// Wi-Fi Receiver
			// ------------------------------------------------------
			// Removing Cyclic Prefix
			Complex[] rx_rcp = new Complex[rx.length - cp];
			for (int i = 0; i < IFFTsource.length; i++) {
				rx_rcp[i] = rx[i + cp];
			}
			// ------------------------------------------------------
			// Taking FFT
			Complex[] FFTsignal = FFT.fft(rx_rcp, subChannels*2);          // round these values
			int[] deModulatedSignal = new int[FFTsignal.length];
			for (int i = 0; i < FFTsignal.length; i++) {
				for (int h = 0; h < qMapping.length; h++) {
					if ((Math.round(FFTsignal[i].im()) == Math.round(qMapping[h].im())) && (Math.round(FFTsignal[i].re()) == Math.round(qMapping[h].re()))) {
						deModulatedSignal[i] = h;
					}
				}
			}
			// ------------------------------------------------------
			// De-Mapping or Demodulation
			String[] qDeMapping = new String[deModulatedSignal.length * 4];
			for (int i = 0; i < deModulatedSignal.length; i++) {
				switch (deModulatedSignal[i]) {
				case 0:
					deModulatedSignal[i] = 0;
					qDeMapping[i] = "0000";     // binary zero
					break;
				case 1:
					deModulatedSignal[i] = 1;
					qDeMapping[i] = "0001";     // binary 1
					break;
				case 2:
					deModulatedSignal[i] = 2;
					qDeMapping[i] = "0010";     // binary 2
					break;
				case 3:
					deModulatedSignal[i] = 3;
					qDeMapping[i] = "0011";     // binary 3
					break;
				case 4:
					deModulatedSignal[i] = 4;
					qDeMapping[i] = "0100";     // binary 4
					break;
				case 5:
					deModulatedSignal[i] = 5;
					qDeMapping[i] = "0101";     // binary 5
					break;
				case 6:
					deModulatedSignal[i] = 6;
					qDeMapping[i] = "0110";     // binary 6
					break;
				case 7:
					deModulatedSignal[i] = 7;
					qDeMapping[i] = "0111";     // binary 7
					break;
				case 8:
					deModulatedSignal[i] = 8;
					qDeMapping[i] = "1000";     // binary 8
					break;
				case 9:
					deModulatedSignal[i] = 9;
					qDeMapping[i] = "1001";     // binary 9
					break;
				case 10:
					deModulatedSignal[i] = 10;
					qDeMapping[i] = "1010";     // binary 10
					break;
				case 11:
					deModulatedSignal[i] = 11;
					qDeMapping[i] = "1011";     // binary 11
					break;
				case 12:
					deModulatedSignal[i] = 12;
					qDeMapping[i] = "1100";     // binary 12
					break;
				case 13:
					deModulatedSignal[i] = 13;
					qDeMapping[i] = "1101";     // binary 13
					break;
				case 14:
					deModulatedSignal[i] = 14;
					qDeMapping[i] = "1110";     // binary 14
					break;
				case 15:
					deModulatedSignal[i] = 15;
					qDeMapping[i] = "1111";     // binary 15
					break;
				}
			}
			// ------------------------------------------------------
			// Decimal to Binary Conversion
			byte[] decimalToBin = new byte[deModulatedSignal.length * 4];
			for (int i = 0; i < deModulatedSignal.length * 4; i++) {
				decimalToBin[i] = getNthBit(qDeMapping, i);
			}
			// ------------------------------------------------------
			// Calculating number of Errored Bits 
			byte[] cb = new byte[decimalToBin.length];
			for (int i = 0; i < decimalToBin.length; i++) {
				cb[i] = binarySource[i];
				errBits += (cb[i] == decimalToBin[i]) ? 0 : 1;
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
	public static byte getNthBit(String[] t, int n) {
		byte bit = (byte) Integer.parseInt((t[n / 4].charAt(n % 4)) + "");
		return bit;
	}
	// ------------------------------------------------------
	// GammaFunction
	static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
				+ 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
				+  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}
	static double gamma(double x) { return Math.exp(logGamma(x)); }
	// ------------------------------------------------------
	// converting Input String to Binary bits Function
	public static byte [] getStringToBinary(String inputMsg) {
		String s2 = "";
		for(int i=0; i<inputMsg.length(); i++) {
			char c = inputMsg.charAt(i);        
			s2 += String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0");
		}
		s2 = s2.replaceAll("0", "\0");
		s2 = s2.replaceAll("1", "\1");
		byte [] binarySource = s2.getBytes();
		return binarySource;	
	}

}
