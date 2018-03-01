package radio_module;

public class XBeeToArduinoFrameGenerator {

	public static String at(String ats) {
		String code = "";
		code += "\t" + "Serial.write(0x7E);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";

		int n = ats.length()-2;
		String [] s = new String [n];		
		
		String szs = Integer.toHexString(4+(n/2));
		if (szs.length()==1) szs = "0"+szs;
		
		code += "\t" + "Serial.write(0x"+szs+");" + "\n";
		code += "\t" + "Serial.write(0x09);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";
		
		int v1 = ats.charAt(0);
		int v2 = ats.charAt(1);

		code += "\t" + "Serial.write(0x"+Integer.toHexString(v1)+");" + "\n";
		code += "\t" + "Serial.write(0x"+Integer.toHexString(v2)+");" + "\n";
		
		int x = 0x09 + v1 + v2;
		
		for (int i=0; i<(n/2); i++) {
			s[i] = ats.substring(i+2, i+4);
			code += "\t" + "Serial.write(0x"+s[i]+");" + "\n";
			x = x + Integer.parseInt(s[i],16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += "\t" + "Serial.write(0x"+ss+");" + "\n";
		code += "\tdelay(10);" + "\n";
		return code;
	}
	
	public static String nd() {
		String code = "";
		code += "\t" + "Serial.write(0x7E);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";		
		code += "\t" + "Serial.write(0x04);" + "\n";
		code += "\t" + "Serial.write(0x09);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";
		code += "\t" + "Serial.write(0x4E);" + "\n";
		code += "\t" + "Serial.write(0x44);" + "\n";
		code += "\t" + "Serial.write(0x64);" + "\n";

		return code;
	}
	
	public static void atc(String ats) {
		System.out.println("\t" + "Serial.write(0x7E);");
		System.out.println("\t" + "Serial.write(0x00);");

		int n = ats.length()-2;
		String [] s = new String [n];		
		
		String szs = Integer.toHexString(4+(n/2));
		if (szs.length()==1) szs = "0"+szs;
		
		System.out.println("\t" + "Serial.write(0x"+szs+");");
		System.out.println("\t" + "Serial.write(0x09);");
		System.out.println("\t" + "Serial.write(0x00);");
		
		int v1 = ats.charAt(0);
		int v2 = ats.charAt(1);

		System.out.println("\t" + "Serial.write(0x"+Integer.toHexString(v1)+");");
		System.out.println("\t" + "Serial.write(0x"+Integer.toHexString(v2)+");");
		
		int x = 0x09 + v1 + v2;
		
		for (int i=0; i<(n/2); i++) {
			s[i] = ats.substring(i+2, i+4);
			System.out.println("\t" + "Serial.write(0x"+s[i]+");");
			x = x + Integer.parseInt(s[i],16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		System.out.println("\t" + "Serial.write(0x"+ss+");");
		System.out.println("\tdelay(10);");
		System.out.println();
		
	}
	
	
	
	public static void at2(String ats) {
		System.out.print("7E 00");

		int n = ats.length()-2;
		String [] s = new String [n];		
		
		String szs = Integer.toHexString(4+(n/2));
		if (szs.length()==1) szs = "0"+szs;
		
		System.out.print(" "+szs);
		System.out.print(" 09 00");
		
		int v1 = ats.charAt(0);
		int v2 = ats.charAt(1);

		System.out.print(" "+Integer.toHexString(v1));
		System.out.print(" "+Integer.toHexString(v2));
		
		int x = 0x09 + v1 + v2;
		
		for (int i=0; i<(n/2); i++) {
			s[i] = ats.substring(i+2, i+4);
			System.out.print(" "+s[i]);
			x = x + Integer.parseInt(s[i],16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		System.out.print(" "+ss); 
		System.out.println();
		
	}
	
	public static String data16(String data, String dl, String dh) {		
		if(dl.length()==1) dl = "0"+dl;
		if(dh.length()==1) dh = "0"+dh;
		
		String code = "";
		code += "\t" + "Serial.write(0x7E);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";

		int n = data.length()+5;
		
		String szs = Integer.toHexString(n);
		if (szs.length()==1) szs = "0"+szs;
		
		code += "\t" + "Serial.write(0x"+szs+");" + "\n";
		
		code += "\t" + "Serial.write(0x01);" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";
		code += "\t" + "Serial.write(0x"+dl+");" + "\n";
		code += "\t" + "Serial.write(0x"+dh+");" + "\n";
		code += "\t" + "Serial.write(0x00);" + "\n";
		
		
		
		int x = 0x01 + Integer.parseInt(dl,16) + Integer.parseInt(dh,16);
		
		String s ="";
		
		for (int i=0; i<data.length(); i++) {
			s = Integer.toHexString(data.charAt(i));
			if(s.length()==1) s="0"+s;
			code += "\t" + "Serial.write(0x"+s+");" + "\n";
			x = x + Integer.parseInt(s,16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += "\t" + "Serial.write(0x"+ss+");" + "\n";
		code += "\tdelay(10);" + "\n";
		return code;
	}
	
}
