package weather;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import project.Project;
/**
 * @author Nabil Kadjouh
 * @version 2.8.3 (U-One)
 */

//—————————————————————————————————————————————————————————————————————————————————————————————————————————K A D J O U H
public  class GenerateWeather {
	
	

	public static void generate()
	{
		int minTempearature = (int)(Math.random()*70)-40;
		int during;
		double t,tt;
		String path = Project.getProjectWeather_eventsPath()+File.separator+"WeatherFile.wsc";
		PrintStream fos = null;
		try {
			fos = new PrintStream(new FileOutputStream(path));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		String s = "0"+" "+minTempearature;
		fos.print(s);
		fos.println();
		System.out.println(" "+minTempearature);	
		t = minTempearature;
		
		for (int i=1; i<119;i++)
		   {
			   during = (int)(Math.random()*360);
			   tt = (int)((t+0.1)*10);
			   t = tt/10; 
			   
			   fos.print(during+" "+t);
			   fos.println();
			     
		   }
		for (int i=120; i<240;i++)
		   {
			   during = (int)(Math.random()*360);
			   tt = (int)((t-0.1)*10);
			   t = tt/10; 
			   
			   fos.print(" "+during+" "+t);
			   fos.println();
			     
		   }
		
		   
		   fos.close();
       
	}

}
//—————————————————————————————————————————————————————————————————————————————————————————————————————————K A D J O U H