package frame;

public class Frame_802_15_4 {

	public static void main(String [] args) {
		String s = data16("Bonjour", "00", "01");
		s = s.toUpperCase();
		System.out.println(s);
	}
	
	public static String generateDataFrame(String data) {
		String frame = data ;
		return frame ;
	}
	
	public static String getDataFromFrame(String frame) {
		String data = frame ;
		return data ; 
	}
	
	public static String generateAtFrame(String data) {
		String frame = data ;
		return frame ;
	}
	
	public static String readAtFrame(String frame) {
		String data = frame ;
		return data ; 
	}
	
	public static String at(String ats) {
		String code = "";
		code += "7e";
		code += "00;" + "\n";

		int n = ats.length()-2;
		String [] s = new String [n];		
		
		String szs = Integer.toHexString(4+(n/2));
		if (szs.length()==1) szs = "0"+szs;
		
		code += ""+szs;
		code += "09";
		code += "00";
		
		int v1 = ats.charAt(0);
		int v2 = ats.charAt(1);

		code += ""+Integer.toHexString(v1)+"";
		code += ""+Integer.toHexString(v2)+"";
		
		int x = 0x09 + v1 + v2;
		
		for (int i=0; i<(n/2); i++) {
			s[i] = ats.substring(i+2, i+4);
			code += ""+s[i]+"";
			x = x + Integer.parseInt(s[i],16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += ""+ss+"";
		return code;
	}
		
	public static String data16(String data, String dl, String dh) {
		
		if(dl.length()==1) dl = "0"+dl;
		if(dh.length()==1) dh = "0"+dh;
		
		String code = "";
		code += "7e";
		code += "00";

		int n = data.length()+5;
		
		String szs = Integer.toHexString(n);
		if (szs.length()==1) szs = "0"+szs;
		
		code += ""+szs+"";
		
		code += "01";
		code += "00";
		code += ""+dl+"";
		code += ""+dh+"";
		code += "00";
		
		int x = 0x01 + Integer.parseInt(dl,16) + Integer.parseInt(dh,16);
		
		String s ="";
		
		for (int i=0; i<data.length(); i++) {
			s = Integer.toHexString(data.charAt(i));
			if(s.length()==1) s="0"+s;
			code += ""+s+"";
			x = x + Integer.parseInt(s,16);
		}
		String ss = Integer.toHexString(x);
		ss = ss.substring(ss.length()-2, ss.length());
		x = Integer.parseInt(ss,16);
		ss = Integer.toHexString(255-x);
		code += ""+ss+"";
		return code;
	}
	
}
