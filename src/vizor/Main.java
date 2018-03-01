package vizor;

public class Main {

	public void main2() {
		String s = "hello";
		String s2 = "";
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);		
			s2 += String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0");
		}
		s2 = s2.replaceAll("0", "\0");
		s2 = s2.replaceAll("1", "\1");
		byte [] b = s2.getBytes();
		for(int i=0; i<b.length; i++) {
			System.out.print(b[i]);
		}
		System.out.println();
		
		for(int i=0; i<b.length/8; i++) {
			for(int j=0; j<8; j++) {
				System.out.print(b[8*i+j]);
			}
			System.out.println();
		}
	}

}
