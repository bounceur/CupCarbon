package socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String [] args) throws IOException {
		Socket s = new Socket("localhost", 3000);
		PrintStream p = new PrintStream(s.getOutputStream());
		Scanner scan = new Scanner(System.in);
		String str ;
		System.out.print("sCupCarbon >> ");
		while(!(str = scan.nextLine()).equals("exit")) {
			System.out.println("> "+str);
			System.out.print("sCupCarbon >> ");
			p.print(str+"\n");
		}
		scan.close();
		p.close();
		s.close();		
	}	
}
