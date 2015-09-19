package solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import project.Project;
import device.Device;
import device.DeviceList;
import device.DeviceWithWithoutRadio;

public class CharlySchedul {

	public static void run() {
		Socket s;
		try {
			System.out.println("Connection ...");
			s = new Socket("172.25.49.90", 5005);
			System.out.println("Connected !");
			InputStream is = s.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			PrintStream ps = new PrintStream(s.getOutputStream());
			String str = br.readLine();
			if(str.equals("orsen"))
				System.out.println("Communication OK !");
			else 
				throw new Exception("Communication ERROR !");
			ps.println("ALG001");					
			str = br.readLine();
			if(str.equals("OK"))
				System.out.println("ALGO OK !");
			else 
				throw new Exception("ALGO ERROR !");
			System.out.println("Sending data ...");
			for(Device d : DeviceList.getNodes()) {
				if(d.getType() == Device.SENSOR) {
					System.out.print("SENSOR ");
					ps.print("SENSOR ");
					System.out.print(d.getNodeIdName() + " ");
					ps.print(d.getNodeIdName() + " ");
					System.out.print(d.getLatitude() + " ");
					ps.print(d.getLatitude() + " ");
					System.out.print(d.getLongitude() + " ");
					ps.print(d.getLongitude() + " ");
					System.out.print(d.getBatteryLevel() + " ");
					ps.print(d.getBatteryLevel() + " ");
					System.out.print(d.getES() + " ");
					ps.print(d.getES() + " ");
					System.out.print(d.getERx() + " ");
					ps.print(d.getERx() + " ");
					System.out.print(d.getETx() + " ");
					ps.print(d.getETx() + " ");
					System.out.print(d.getSensorUnitRadius() + " ");
					ps.print(d.getSensorUnitRadius() + " ");
					System.out.print(d.getRadioRadius() + " ");
					ps.print(d.getRadioRadius() + " ");
					System.out.print(d.getBeta() + " ");
					ps.print(d.getBeta() + " ");
					System.out.println();
					ps.println();
				}
			}
			
			for(Device d : DeviceList.getNodes()) {
				if(d.getType() == Device.MOBILE_WR) {
					System.out.print("TARGET ");
					ps.print("TARGET ");
					System.out.print(d.getNodeIdName() + " ");
					ps.print(d.getNodeIdName() + " ");
					d.loadRouteFromFile();
					int n = ((DeviceWithWithoutRadio)d).getRouteX().size();
					System.out.print(n + " ");
					ps.print(n + " ");
					for (int i=0; i<n; i++) {
						System.out.print(
							((DeviceWithWithoutRadio)d).getRouteTime().get(i) + " " +
							((DeviceWithWithoutRadio)d).getRouteY().get(i) + " " +
							((DeviceWithWithoutRadio)d).getRouteX().get(i) + " "
						);
						ps.print(
							((DeviceWithWithoutRadio)d).getRouteTime().get(i) + " " +
							((DeviceWithWithoutRadio)d).getRouteY().get(i) + " " +
							((DeviceWithWithoutRadio)d).getRouteX().get(i) + " "
						);
					}
					System.out.println();
					ps.println();
				}
			}
			
			for(Device d : DeviceList.getNodes()) {
				if(d.getType() == Device.BASE_STATION) {
					System.out.print("BASE ");
					ps.print("BASE ");
					System.out.print(d.getNodeIdName() + " ");
					ps.print(d.getNodeIdName() + " ");
					System.out.print(d.getLatitude() + " ");
					ps.print(d.getLatitude() + " ");
					System.out.print(d.getLongitude() + " ");
					ps.print(d.getLongitude() + " ");
					System.out.println();
					ps.println();
				}
			}	
								
		//} catch (UnknownHostException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
			System.out.println("END");
			ps.println("END");
			
			
			//CupCarbonParameters
			str = br.readLine();
			FileOutputStream fos = new FileOutputStream(
				Project.getProjectResultsPath() +
				File.separator + "scheduling.txt"
			);
			PrintStream ps2 = new PrintStream(fos);
			while(!((str = br.readLine()).equals("END"))) {
				ps2.println(str);
				System.out.println(str);
			}
			System.out.println("Communication finished.");
			ps.close();
			br.close();
			ps2.close();
			s.close();
			System.out.println("Communication closed.");
		} catch (Exception e) {
			System.out.println("Connexion probelm !");
		}
	}
	
}
