package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static void main(String[] args) {
		try {
			System.out.println("CupCarbon >> Waiting");
			ServerSocket ss = new ServerSocket(3000);
			Socket s = ss.accept();
			System.out.println("CupCarbon >> Connexion");
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str ;
			while((str = br.readLine()) != null) {
				System.out.println("CupCarbon >> " + str);
			}
			System.out.println("CupCarbon >> Deconnexion");
			br.close();
			s.close();
			ss.close();
		}
		catch(Exception e) {System.out.println("ERROR");}
	}
	
}
