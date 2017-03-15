package radio_module;

public class XBeeFrameGenerator {

	
	public static void main(String [] arts) {
		//System.out.println(at("ATCH"));
		//System.out.println(byteToBinaryStreem("F"));
		System.out.println(data16("Bonjour", "12", "23"));
		System.out.println(data16InBin("Bonjour", "12", "23", 500));
	}
	
	public static String at(String ats) {
		String code = "7E" ;
		code += "00" ;

		int n = ats.length()-2;
		String [] s = new String [n];		
		
		String szs = Integer.toHexString(4+(n/2));
		if (szs.length()==1) szs = "0"+szs;
		
		code += szs ;
		code += "09" ;
		code += "00" ;
		
		int v1 = ats.charAt(0);
		int v2 = ats.charAt(1);

		code += Integer.toHexString(v1) ;
		code += Integer.toHexString(v2) ;
		
		int x = 0x09 + v1 + v2;
		
		for (int i=0; i<(n/2); i++) {
			s[i] = ats.substring(i+2, i+4);
			code += s[i] ;
			x = x + Integer.parseInt(s[i],16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code +=  ss ;
		
		return code;
	}
	
	public static String data16InBin(String data, String dl, String dh, int n) {
		String s = byteToBinaryStreem(data16(data, dl, dh));
		int diff = n - s.length();
		for (int j=0; j<diff; j++) {
			s = s + "0";
		}			
		return s; 
	}
	
	public static String data16(String data, String dl, String dh) {
		
		if(dl.length()==1) dl = "0"+dl;
		if(dh.length()==1) dh = "0"+dh;
		
		String code = "";
		code += "7E" ;
		code += "00" ;

		int n = data.length()+5;
		
		String szs = Integer.toHexString(n);
		if (szs.length()==1) szs = "0"+szs;
		
		code += szs ;
		
		code += "01" ;
		code += "00" ;
		code += dl ;
		code += dh ;
		code += "00" ;
		
		int x = 0x01 + Integer.parseInt(dl,16) + Integer.parseInt(dh,16);
		
		String s ="";
		
		for (int i=0; i<data.length(); i++) {
			s = Integer.toHexString(data.charAt(i));
			if(s.length()==1) s="0"+s;
			code += s ;
			x = x + Integer.parseInt(s,16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += ss ;
		return  code.toUpperCase();
	}
	
	public static String data64(String data, String dl, String dh) {
		
		if(dl.length()==1) dl = "0"+dl;
		if(dh.length()==1) dh = "0"+dh;
		
		String code = "";
		code += "7E" ;
		code += "00" ;

		int n = data.length()+5;
		
		String szs = Integer.toHexString(n);
		if (szs.length()==1) szs = "0"+szs;
		
		code += szs ;
		
		code += "01" ;
		code += "00" ;
		code += dl ;
		code += dh ;
		code += "00" ;
		
		int x = 0x01 + Integer.parseInt(dl,16) + Integer.parseInt(dh,16);
		
		String s ="";
		
		for (int i=0; i<data.length(); i++) {
			s = Integer.toHexString(data.charAt(i));
			if(s.length()==1) s="0"+s;
			code += s ;
			x = x + Integer.parseInt(s,16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += ss ;
		return  code.toUpperCase();
	}
	
	public static String ack(String data, String dl, String dh) {
		
		if(dl.length()==1) dl = "0"+dl;
		if(dh.length()==1) dh = "0"+dh;
		
		String code = "";
		code += "7E" ;
		code += "00" ;

		int n = data.length()+5;
		
		String szs = Integer.toHexString(n);
		if (szs.length()==1) szs = "0"+szs;
		
		code += szs ;
		
		code += "01" ;
		code += "00" ;
		code += dl ;
		code += dh ;
		code += "00" ;
		
		int x = 0x01 + Integer.parseInt(dl,16) + Integer.parseInt(dh,16);
		
		String s ="";
		
		for (int i=0; i<data.length(); i++) {
			s = Integer.toHexString(data.charAt(i));
			if(s.length()==1) s="0"+s;
			code += s ;
			x = x + Integer.parseInt(s,16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += ss ;
		return  code.toUpperCase();
	}
	
	public static String byteToBinaryStreem(String byteStreem) {
		String result = "" ;
		String r = "";
		//Integer.Integer.parseInt("AA0F245C", 16);
		for(int i=0; i<byteStreem.length(); i++) {
			r = Integer.toBinaryString(Integer.parseInt(""+byteStreem.charAt(i),16));
			int n = 4-r.length();
			for (int j=0; j<n; j++) {
				r = "0" + r ;
			}			
			result += r;
		}
		return result;
	}

	public static String ackInBin(String data, String dl, String dh, int n) {
		return data16InBin(data, dl, dh, n);
	}
	
	
}
