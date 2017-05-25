package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CupClient {
	public static void main(String args[]) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 3000);
    PrintStream p = new PrintStream(socket.getOutputStream());
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
    socket.close();        
	}
}
