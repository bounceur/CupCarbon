package perso;


public class MyClass extends Thread {

	private String s = null;

	public MyClass() {
		s = "Hello CupCarbon !";
	}

	@Override
	public void run() {
		System.out.println("--> " + s);
	}

}
