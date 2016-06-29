 package weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import project.Project;

//------------------------------------------------------------------------------------------KADJOUH	
/**
 * @author Nabil Kadjouh
 * @version 2.8.3 (U-One)
 */
//------------------------------------------------------------------------------------------KADJOUH	
public class WeatherScenario {
	
	
	private static int weatherIndex ;
	      
	private static LinkedList<Integer> temperatureEvt = new LinkedList<Integer>();
	private static LinkedList<Double> temperature = new LinkedList<Double>();	
	
	private static int accumulatedTime;
 
//------------------------------------------------------------------------------------------KADJOUH	   
	public static boolean Init()
	{
		Project.setProjectWeather_eventsFileName(Project.getProjectWeather_eventsPath()+File.separator+"WeatherFile.wsc");
		if(!loadWeatherFromFile())
			return false;
		setWeatherIndex(0);	
		setAccumulatedTime(0);
		return true;
	}
//------------------------------------------------------------------------------------------KADJOUH		
	
	public static boolean loadWeatherFromFile()
	{ 
		
		String ErrorMsg ="";
		String weatherFileName = Project.getProjectWeather_eventsFileName();
		
		FileInputStream fis;
		BufferedReader b = null;
		String s="*/*/*/*";// initialization
		String[] ts;
		try {
			if (!weatherFileName.equals("")) 
				if ( FileManager.fileExists (weatherFileName))
						
				{

					temperature.clear();
					temperatureEvt.clear();
					
					fis = new FileInputStream(Project.getProjectWeather_eventsFileName());				
					b = new BufferedReader(new InputStreamReader(fis));
									
					while ((s = b.readLine()) != null) {
						ts = s.trim().split(" ");
						temperatureEvt.add(Integer.parseInt(ts[0]));
						temperature.add(Double.parseDouble(ts[1]));					 
						
					}
					b.close();
					fis.close();
	
				} 
				else 
					ErrorMsg += "\n#Weather file Path doesn't exist";
			else 
				ErrorMsg += "\n#Weather file Name is Empty (not selected)! ";
		} catch (Exception e) {
//			System.err.println("weatherSenario.loadWeatherFromFile(): parso error with s = |"+s+"|");
			e.printStackTrace(); 
			ErrorMsg += "\n#Syntax error in weather format";
			
		}
		if(ErrorMsg.isEmpty())
			return true;
		
		JOptionPane.showMessageDialog(null, ErrorMsg, "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	}
//------------------------------------------------------------------------------------------KADJOUH		
    public static int getAccumulatedTime() {
		return accumulatedTime;
	}

	public static void setAccumulatedTime(int accumulatedTime) {
		WeatherScenario.accumulatedTime = accumulatedTime;
	}

//------------------------------------------------------------------------------------------KADJOUH	
	public static  void setWeatherIndex(int ind) {
		if (!(temperature.size()==0))
		{
		weatherIndex = ind % temperature.size();
		}
	}
	
//------------------------------------------------------------------------------------------KADJOUH	
    public static int getweatherIndex()
	{
		return weatherIndex;
	}
//------------------------------------------------------------------------------------------KADJOUH	
	public static double getCurrentTemperature() {
		return temperature.get(weatherIndex) ;		
	}
//------------------------------------------------------------------------------------------KADJOUH	
	public static int getCurrentTemperatureEvt() {
		return temperatureEvt.get(weatherIndex) ;		
	}
	
//------------------------------------------------------------------------------------------KADJOUH	
	public static int getNextTemperatureEvt() {
		int tmp = getweatherIndex();
		   if(tmp < temperatureEvt.size()) {tmp++;}
		   else {tmp = 0;}
		return temperatureEvt.get(tmp);		
	}
	
//------------------------------------------------------------------------------------------KADJOUH	
	public static void changeWeather()	{
		accumulatedTime =+ getNextTemperatureEvt();
		setWeatherIndex(weatherIndex+1);
	}
//------------------------------------------------------------------------------------------KADJOUH	
	
	public static double getTemperatureAt(double time)
	{
		int i = 0;
		double tmp = 0;
		while ( tmp < time)
		{
			i = (i+1) % temperature.size();
			tmp += temperatureEvt.get(i);
		}
		  
		return temperature.get(i);
		
	}
	
}
