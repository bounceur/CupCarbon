/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2015 CupCarbon
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package actions_ui;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Ahcene Bounceur
 * @author Tangy Clech
 * @author Nabil Mohammed Boudarbala
 * @version 1.0
 */

public class Historic {
	private static ArrayList<Actions> historiqueActions = new ArrayList<Actions>();
	private static int currentIndice = 0;
	
	public static void add(Actions action, boolean isRedo){
		if(isRedo == false){
			if(currentIndice == historiqueActions.size()){
				historiqueActions.add(action);
				currentIndice++;
			}
			else if(currentIndice < historiqueActions.size()){
				historiqueActions.add(currentIndice,action);
				currentIndice++;
				
				while(currentIndice != historiqueActions.size()){
					historiqueActions.remove(historiqueActions.size()-1);
				}
			}
		}
		
		if(isRedo == true){
			currentIndice++;
		}
	}
	
	public static void remove(){
		if(currentIndice >= 1)
			currentIndice--;
	}
	
	public static void swapit(int i ,int index)
	{
		Collections.swap(historiqueActions,historiqueActions.size()-i,index);
	}
	
	public static void view(){
		System.out.println("-- Historic --");
		System.out.println("Size: " + historiqueActions.size());
		System.out.println("Current: " + currentIndice);
		
		for(int i=0; i<historiqueActions.size(); i++){

			if(i == currentIndice-1){
				System.out.println("Action No" + (i) + ": " + historiqueActions.get(i).name + "S" + " <-");
			}
			else
				System.out.println("Action No" + (i) + ": " + historiqueActions.get(i).name + "S");
		}
		System.out.println("------------");
	}
	
	public static int size()
	{
		return historiqueActions.size();
	}
	
	public static void undo(){
		if(currentIndice >= 1)
			historiqueActions.get(currentIndice-1).undo();
	}
	
	public static void redo(){
		if(currentIndice < historiqueActions.size())
			historiqueActions.get(currentIndice).redo();
	}	
}
