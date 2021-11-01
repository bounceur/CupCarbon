package iotlab;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client 
{
	
	/**
	 * ThingsBoars simple client
	 * user micheledicapua@gmail.com
	 * Sulla pagine Thingsboard aprire la dashboard IoT Lab
	 * @param data
	 */
	public static void sendInCloud(float data)
	{
		URL url = null;
		HttpURLConnection con = null;
		try
		{
			url = new URL ("http://demo.thingsboard.io/api/v1/ZJifi2d7hOZWhhPz3FJq/telemetry");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			//temperature è la chiave (KEY) del widget (lettura dati) di TensorBoard
			String jsonInputString = "{\"temperature\":"+data+"}";
			
			OutputStream os = con.getOutputStream(); 
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			BufferedReader br = new BufferedReader (new InputStreamReader(con.getInputStream(), "utf-8"));
			StringBuilder response = new StringBuilder();
			String responseLine    = null;
			while ((responseLine = br.readLine()) != null) 
			{
				response.append(responseLine.trim());
			}
			System.out.println("Response="+response.toString());
			System.out.println("Status="+con.getResponseCode());			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}	
}