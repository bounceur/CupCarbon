/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the
 * French Agence Nationale de la Recherche ANR
 * under the reference ANR-14-CE24-0017-01.
 * ----------------------------------------------------------------------------------------------------------------
 **/

package cupcarbon_script;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

public class CupCarbonServer extends Thread {

	@Override
	public void run() {
			try {
				System.out.println("CupCarbon >> Waiting");
	      ServerSocket serverSocket = new ServerSocket(3000);
	      Socket socket = serverSocket.accept();
	      
	      System.out.println("CupCarbon >> Connexion");
	      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      String str ;
	      CupScript script = new CupScript();  		
	      while((str = br.readLine()) != null) {
	          System.out.println("CupCarbon >> " + str);
	          CupScriptAddCommand.addCommand(script, str.trim());
	  				script.nextAndExecute();
	  				MapLayer.repaint();
	      }
	      System.out.println("CupCarbon >> Deconnexion");
	      
	      br.close();
	      socket.close();
	      serverSocket.close();
			}
			catch(Exception e) {
				System.out.println(e);
			}
	}
	
}
