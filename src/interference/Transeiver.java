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

import device.Device;
import math.Complex;
import radio.Standard;

public class Transeiver {

	public static void main(String[] args) {
		
//		String data = "AAAAAA";
//		
//		System.out.println(data);
//		
//		int std = Standard.ZIGBEE_802_15_4;
//		
//		System.out.println(std);
//		
//		int subch = Standard.getSubChannel(std);
//		
//		System.out.println(subch);
//		
//		String packet = XBeeFrameGenerator.data16InBin(data, "00", "00", subch*8);
//		System.out.println(packet);
//		String [] received = receviedPacketWithAlphaD(packet, subch);
//		System.out.println(received[0]);
//		System.out.println(received[1]);

	}
	
	public static String [] receviedPacketWithAlphaD(String packet, Device device) {
		// We use device here in order to get the standard which allows to calculate the sub-channel
		// 
		Complex[] ch = new Complex[2];
		ch[0] = new Complex(1, 0);
		ch[1] = new Complex(0.8, 0);
		
		int m = 16; 
		int k = m/4; 
		int subch = Standard.getSubChannel(device.getStandard());
		int cp = ch.length; //k is number of bits per symbol
		
		int tx_b = subch*k*2; //number of bits to be generated
		
		int tx_d = tx_b/k; // length of decimal numbers
		
		double alpha=1.8; 
		int beta=0; 
		double gamma=0.001; 
		int delta=0; 
		int sizeout=subch*2+cp;

		int[] tx_dn = new int[tx_d];
		int j = 0;
		for (int i = 0; i < tx_b; i += 4) {
			String s = "" + packet.charAt(i) + packet.charAt(i+1) + packet.charAt(i+2) + packet.charAt(i+3);
			tx_dn[j] = Integer.parseInt(s, 2);
			j++;
		}
				
		Complex[] qmap = new Complex[16];
		qmap[0] = new Complex(-3, -3);
		qmap[1] = new Complex(-3, -1);
		qmap[2] = new Complex(-3, 3);
		qmap[3] = new Complex(-3, 1);
		qmap[4] = new Complex(-1, -3);
		qmap[5] = new Complex(-1, -1);
		qmap[6] = new Complex(-1, 3);
		qmap[7] = new Complex(-1, 1);
		qmap[8] = new Complex(3, -3);
		qmap[9] = new Complex(3, -1);
		qmap[10] = new Complex(3, 3);
		qmap[11] = new Complex(3, 1);
		qmap[12] = new Complex(1, -3);
		qmap[13] = new Complex(1, -1);
		qmap[14] = new Complex(1, 3);
		qmap[15] = new Complex(1, 1);

		Complex[] tx_mod = new Complex[tx_d];
		for (int i = 0; i < tx_d; i++) {
			tx_mod[i] = qmap[tx_dn[i]];
		}
		
		Complex[] tx_ifft = FFT.ifft(tx_mod, subch*2); 
				 
		Complex[] tx_cp = new Complex[tx_ifft.length + cp];
		for (int i = 0; i < tx_ifft.length; i++) {
			tx_cp[i + cp] = tx_ifft[i];
		}
		tx_cp[0] = tx_ifft[tx_ifft.length - 2];
		tx_cp[1] = tx_ifft[tx_ifft.length - 1];
	
		//generating alpha-stable random variables
		double [] alpharnd1 = AlphaDistribution.rAlphaD(alpha, beta, gamma, delta, sizeout);
		double [] alpharnd2 = AlphaDistribution.rAlphaD(alpha, beta, gamma, delta, sizeout);
		Complex [] alpharnd = new Complex[sizeout];
		for (int i = 0; i < sizeout; i++) {
			alpharnd[i] = new Complex(alpharnd1[i], alpharnd2[i]);
			}
		
		Complex [] rx = new Complex[sizeout];
		double [] noise_real = new double[sizeout];
		double [] noise_imag = new double[sizeout];
		for (int i = 0; i < sizeout; i++) {
			noise_real[i] = tx_cp[i].real() + alpharnd[i].real();
			noise_imag[i] = tx_cp[i].imaginary() + alpharnd[i].imaginary();
			rx[i] = new Complex(noise_real[i], noise_imag[i]);
			}
		
		Complex[] rx_rcp = new Complex[rx.length - cp];
		for (int i = 0; i < tx_ifft.length; i++) {
			rx_rcp[i] = rx[i + cp];
		}

		Complex[] rx_fft = FFT.fft(rx_rcp, subch*2); // round these values

		int[] rx_dmod = new int[rx_fft.length];
		for (int i = 0; i < rx_fft.length; i++) {
			for (int h = 0; h < qmap.length; h++) {
				if ((Math.round(rx_fft[i].imaginary()) == Math.round(qmap[h].imaginary())) && (Math.round(rx_fft[i].real()) == Math.round(qmap[h].real()))) {
					rx_dmod[i] = h;
				}
			}
		}
	
		String [] qdmap = new String[rx_dmod.length * 4];
		for (int i = 0; i < rx_dmod.length; i++) {
			switch (rx_dmod[i]) {
			case 0:
				rx_dmod[i] = 0;
				qdmap[i] = "0000"; // zero is binary zero
				break;
			case 1:
				rx_dmod[i] = 1;
				qdmap[i] = "0001"; // 1 is binary 1
				break;
			case 2:
				rx_dmod[i] = 2;
				qdmap[i] = "0010"; // this is binary two
				break;
			case 3:
				rx_dmod[i] = 3;
				qdmap[i] = "0011"; // binary three
				break;
			case 4:
				rx_dmod[i] = 4;
				qdmap[i] = "0100";
				break;
			case 5:
				rx_dmod[i] = 5;
				qdmap[i] = "0101";
				break;
			case 6:
				rx_dmod[i] = 6;
				qdmap[i] = "0110";
				break;
			case 7:
				rx_dmod[i] = 7;
				qdmap[i] = "0111";
				break;
			case 8:
				rx_dmod[i] = 8;
				qdmap[i] = "1000";
				break;
			case 9:
				rx_dmod[i] = 9;
				qdmap[i] = "1001";
				break;
			case 10:
				rx_dmod[i] = 10;
				qdmap[i] = "1010";
				break;
			case 11:
				rx_dmod[i] = 11;
				qdmap[i] = "1011";
				break;
			case 12:
				rx_dmod[i] = 12;
				qdmap[i] = "1100";
				break;
			case 13:
				rx_dmod[i] = 13;
				qdmap[i] = "1101";
				break;
			case 14:
				rx_dmod[i] = 14;
				qdmap[i] = "1110";
				break;
			case 15:
				rx_dmod[i] = 15;
				qdmap[i] = "1111";
				break;
			}
		}

		String receivedPacketStr = "";
		for (int i = 0; i < rx_dmod.length * 4; i++) {
			receivedPacketStr += getNthBit(qdmap, i);
		}
		
		byte[] rx_d2b = new byte[rx_dmod.length * 4];
		for (int i = 0; i < rx_dmod.length * 4; i++) {
			rx_d2b[i] = getNthBit(qdmap, i);
		}

		int errBits = 0;
		byte [] cb = new byte[rx_d2b.length];
		for (int i = 0; i < rx_d2b.length; i++) {
			cb[i] = Byte.parseByte(""+packet.charAt(i));
			errBits += (cb[i] == rx_d2b[i]) ? 0 : 1;
		}

		String [] toReturn = new String [2];
		toReturn[0] = receivedPacketStr ;

		toReturn[1] = errBits+"";
		return toReturn;

	}

	public static byte getNthBit(String[] t, int n) {
		byte bit = (byte) Integer.parseInt((t[n / 4].charAt(n % 4)) + "");
		return bit;
	}

	public static void display(byte[] t) {
		for (int i = 0; i < t.length; i++) {
			System.out.print(t[i]);
		}
		System.out.println();
	}

}
