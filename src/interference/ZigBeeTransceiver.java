package interference;

import math.Complex;
import math.UFunctions;

public class ZigBeeTransceiver {

	public static double getNumberOfReceivedErrorBits(String inputMsg, int noNeghboringNodes, int radioRange, double perActiveNodes) {
		// The number of received error bits to return
		double errBits = 0;
		// ------------------------------------------------------
		// ZigBee Parameters
		// ------------------------------------------------------
		int signalConstellation = 16;
		int symbolSize = signalConstellation/4;
		int maxBitsAllowedToSend = 512;          // 64 bytes
		// ------------------------------------------------------
		// Transmitter
		// ------------------------------------------------------
		byte [] binaryData = getStringToBinary(inputMsg);
		byte [] binarySource = new byte [maxBitsAllowedToSend];	
		if (binaryData.length>=maxBitsAllowedToSend)
			System.out.print("Too Long Message");
		else {
			for(int i=0; i<maxBitsAllowedToSend; i++){
				if (i<binaryData.length)
					binarySource[i]=binaryData[i];
				else
					binarySource[i] = 0;
			}	
			// ------------------------------------------------------
			// generating Decimal numbers
			int lenDecNo = binarySource.length / symbolSize;    // gives length for decimal numbers
			int[] DecNumbers = new int[lenDecNo];
			int jj = 0;
			for (int i = 0; i < binarySource.length; i += 4) {
				String s = "" + binarySource[i] + binarySource[i + 1] + binarySource[i + 2] + binarySource[i + 3];
				DecNumbers[jj] = Integer.parseInt(s, 2);
				jj++;
			}
			// ------------------------------------------------------
			// mapping Decimal numbers on PN_tables
			String s = "";
			String S_I = "";
			String S_Q = "";
			for (int i = 0; i < lenDecNo; i++) {
				s = s + getPN(DecNumbers[i]);
			}
			// ------------------------------------------------------
			// taking odd and even bits and repeating them at the same time
			for (int i = 0; i <= s.length() / 2 - 1; i++) { 
				S_I = S_I + s.charAt(i * 2) + s.charAt(i * 2);
				S_Q = S_Q + s.charAt((i * 2) + 1) + s.charAt((i * 2) + 1);
			}
			// ------------------------------------------------------
			// O-QPSK Modulation
			int map_dec;
			Complex[] oqpskSource = new Complex[S_I.length() + 1];
			for (int i = 0; i < S_I.length() - 1; i++) {
				// converting from 2-bit binary to decimal numbers
				map_dec = (S_I.charAt(i + 1) - '0') * 2 + (S_Q.charAt(i) - '0'); 
				// mapping 0,1,2 and 3 on complex numbers
				oqpskSource[i + 1] = UFunctions.getComplexModulation(map_dec);
			}
			// ------------------------------------------------------
			// first value of I part and last value of Q part would be single
			// because of 1-bit delay in Q-part
			if (S_I.charAt(0) == '0')
				oqpskSource[0] = new Complex(0.7071, 0.0);
			else
				oqpskSource[0] = new Complex(-0.7071, 0.0);
			if (S_Q.charAt(0) == '0')
				oqpskSource[S_I.length()] = new Complex(0.0, 0.7071);
			else
				oqpskSource[S_I.length()] = new Complex(0.0, -0.7071);
			// ------------------------------------------------------
			// Alpha-Stable Channel
			// ------------------------------------------------------
			// Generating alpha-stable random variables
			int sizeout = oqpskSource.length; // number of alpha_stable random variables
			double [] alpharnd1 = AlphaStableRandom.generateNext(noNeghboringNodes, radioRange, perActiveNodes, sizeout);
			double [] alpharnd2 = AlphaStableRandom.generateNext(noNeghboringNodes, radioRange, perActiveNodes, sizeout);
			Complex [] alpharnd = new Complex[sizeout];
			// generating them twice and then adding them as complex function
			for (int i = 0; i < sizeout; i++) {
				alpharnd[i] = new Complex(alpharnd1[i], alpharnd2[i]);
			}
			Complex[] rx = new Complex[sizeout];
			double [] noise_real = new double[sizeout];
			double [] noise_imag = new double[sizeout];
			for (int i = 0; i < sizeout; i++) {
				noise_real[i] = oqpskSource[i].re() + alpharnd[i].re();
				noise_imag[i] = oqpskSource[i].im() + alpharnd[i].im();
				rx[i] = new Complex(noise_real[i], noise_imag[i]);
			}
			// ------------------------------------------------------
			// RECEIVER
			// ------------------------------------------------------			
			// O-QPSK Demodulation
			int[] OQPSKdemod = new int[rx.length];
			if (rx[0].equals(new Complex(0.7071, 0.0)))
				OQPSKdemod[0] = 0;
			if (rx[0].equals(new Complex(-0.7071, 0.0)))
				OQPSKdemod[0] = 2;
			if (rx[rx.length - 1].equals(new Complex(0.0, 0.7071)))
				OQPSKdemod[rx.length - 1] = 0;
			if (rx[rx.length - 1].equals(new Complex(0.0, -0.7071)))
				OQPSKdemod[rx.length - 1] = 1;
			for (int i = 0; i < oqpskSource.length - 1; i++) {
				OQPSKdemod[i + 1] = getComplexDemodulation(oqpskSource[i + 1]);
			}
			// ------------------------------------------------------
			// converting into 2-bit binary number
			String[] rx_rbin = new String[OQPSKdemod.length];
			for (int i = 0; i < OQPSKdemod.length; i++) {
				rx_rbin[i] = twoBitbinConversion(OQPSKdemod[i]);
			}
			// ------------------------------------------------------
			// converting binary data from parallel to serial
			byte[] rx_p2s = new byte[rx_rbin.length * 2];
			for (int i = 0; i < rx_rbin.length * 2; i += 2) { 
				rx_p2s[i] = (byte) (rx_rbin[i / 2].charAt(0) - '0');
				rx_p2s[i + 1] = (byte) (rx_rbin[i / 2].charAt(1) - '0');
			}
			// ------------------------------------------------------
			//separating odd and even values
			byte[] rxOdd = new byte[rx_p2s.length / 4];
			byte[] rxEven = new byte[rx_p2s.length / 4];
			for (int i = 0; i < rxOdd.length; i++) { 
				rxOdd[i] = rx_p2s[i * 4];
				rxEven[i] = rx_p2s[(i + 1) * 4 - 1];
			}
			// ------------------------------------------------------
			// combining odd and even values one by one
			String[] rx_rpn = new String[rx_p2s.length / 64];
			for (int i = 0; i < rx_p2s.length / 64; i++) { 
				rx_rpn[i] = "";
				for (int k = 0; k < 16; k++) {
					rx_rpn[i] += rxOdd[i * 16 + k];
					rx_rpn[i] += rxEven[i * 16 + k];
				}
			}
			// ------------------------------------------------------
			// De-mapping values over PN-Sequence Table by using Hamming Distance
			byte[] rx_hamd = new byte[rx_rpn.length];
			int min;
			int imin;
			int h;
			for (int i = 0; i < rx_rpn.length; i++) {
				min = 32;
				imin = 16;
				for (int j = 0; j < 16; j++) {
					h = getHammingDistance(rx_rpn[i], getPN(j));
					if (h < min) {
						min = h;
						imin = j;
					}
				}
				rx_hamd[i] = (byte) imin;
			}
			// ------------------------------------------------------
			// converting from decimal to binary
			byte [] rx_d2b = getDecimalToBinary(rx_hamd);

			// ------------------------------------------------------
			// Finding Errored Bits
			byte[] cb = new byte[rx_d2b.length];
			for (int i = 0; i < rx_d2b.length; i++) {
				cb[i] = binarySource[i];
				errBits += (cb[i] == rx_d2b[i]) ? 0 : 1;
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
	// PN Sequence Table
	public static String getPN(int i) {
		String[] PNTable = new String[16]; //PN chip sequence of size 16*32. know at transmitter and receiver
		PNTable[0]  = "11011001110000110101001000101110";
		PNTable[1]  = "11101101100111000011010100100010";
		PNTable[2]  = "00101110110110011100001101010010";
		PNTable[3]  = "00100010111011011001110000110101";
		PNTable[4]  = "01010010001011101101100111000011";
		PNTable[5]  = "00110101001000101110110110011100";
		PNTable[6]  = "11000011010100100010111011011001";
		PNTable[7]  = "10011100001101010010001011101101";
		PNTable[8]  = "10001100100101100000011101111011";
		PNTable[9]  = "10111000110010010110000001110111";
		PNTable[10] = "01111011100011001001011000000111";
		PNTable[11] = "01110111101110001100100101100000";
		PNTable[12] = "00000111011110111000110010010110";
		PNTable[13] = "01100000011101111011100011001001";
		PNTable[14] = "10010110000001110111101110001100";
		PNTable[15] = "11001001011000000111011110111000";
		return PNTable[i];
	}
	// ------------------------------------------------------
	// 2-bit decimal to complex conversion
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
	// complex to 0-3 decimal conversion on receiver side
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
	// 0-3 decimal to 2-bit binary conversion
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
	// Hamming Distance Function
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
	// ------------------------------------------------------
	// converting [0 - 15] to 4 bit binary
	public static byte [] getDecimalToBinary(byte [] decimalVector) {
		byte [] binaryVector = new byte [decimalVector.length*4];
		int k = 0;
		for (int i = 0; i < decimalVector.length; i++) {
			String s = Integer.toBinaryString(decimalVector[i]);
			for(int j = 0; j<(4-s.length()); j++ ) {
				binaryVector[k++] = 0;
			}
			for(int j = 0; j<s.length(); j++ ) {
				binaryVector[k++] = (byte) (s.charAt(j) - '0');
			}
		}
		return binaryVector;
	}

}

