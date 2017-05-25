package cupcarbon;

public class Test {

	public static void main(String[] args) {
		String s0 = "setp object (s1,s2,s3)";
		String s = s0.split(" ")[2];
		s = s.substring(s.indexOf('(')+1, s.indexOf(')'));
		String [] t = s.split(",");
		System.out.println(t[0]);
		System.out.println(t[1]);
		System.out.println(t[2]);
		System.out.println(t.length);

	}

}
