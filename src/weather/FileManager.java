package weather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
 
/**
 * @author Nabil kadjouh
 * @version 2.8.3 (U-One)
 */

public class FileManager {

	/**
	 * this function read file contents
	 * @param FileName : name of file
	 * @return result : list of lines
	 */
	public static List<String> FileRead(String FileName){
		BufferedReader InFlux=null;
		String ReadLine;
		List<String> lines = new ArrayList<String>();
		try{
			InFlux = new BufferedReader(new FileReader(FileName));
			ReadLine = InFlux.readLine();
			while(ReadLine!=null){
				lines.add(ReadLine);
				ReadLine = InFlux.readLine();
			}
				
		}
		catch(IOException exc){
			exc.printStackTrace();
		}
		try{
			InFlux.close();
			
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return lines;
		
		
	}
//------------------------------------------------------------------------------------------KADJOUH		

	/**
	 * this function write the file from liste
	 * @param FileName : name of file
	 * @param lines : list  lines 
	 */
	public static void FileWrite(String FileName, List<String> lines){
		Writer OutFlux=null;
		try{
			OutFlux = new PrintWriter(new BufferedWriter(new FileWriter(FileName)));
			for(int i=0;i<lines.size()-1;i++){
				OutFlux.write(lines.get(i)+"\n");
			}
			OutFlux.write(lines.get(lines.size()-1));
			
		}
		catch(IOException exc){
			exc.printStackTrace();
		}
		try{
			OutFlux.close();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

//------------------------------------------------------------------------------------------KADJOUH	
		public static boolean fileExists (String filePath)
		{
			File f = new File(filePath);
			return (f.exists()&& !f.isDirectory());
					
		}
//------------------------------------------------------------------------------------------KADJOUH	
		public static boolean pathExists (String filePath)
		{
			File f = new File(filePath);
			return f.isDirectory();
			
		}
		
		
		
		
		
}