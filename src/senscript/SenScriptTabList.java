/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

package senscript;

import java.util.LinkedList;

public class SenScriptTabList {
	
	private LinkedList<String> keys;
	private LinkedList<String[][]> values;
	private LinkedList<String[]> vvalues;

	public SenScriptTabList(){
		keys = new LinkedList<String>();
		values = new LinkedList<String[][]>();
		vvalues = new LinkedList<String[]>();
	}
	
	public void putTable(String key, int heigth,int width){
		boolean exist = false;
		String[][] tab = new String[heigth][width];
		for(int i = 0; i < keys.size() ; i++){
			if(key.equals(keys.get(i))){
				values.remove(i);
				values.add(i, tab);
				exist = true;
			}
		}
		if(!exist){
			keys.addLast(key);
			values.addLast(tab);
		}	
	}
	
	public void putVector(String key, int heigth){
		boolean exist = false;
		String[] tab = new String[heigth];
		for(int i = 0; i < keys.size() ; i++){
			if(key.equals(keys.get(i))){
				vvalues.remove(i);
				vvalues.add(i, tab);
				exist = true;
			}
		}
		if(!exist){
			keys.addLast(key);
			vvalues.addLast(tab);
		}	
	}
	
	public String[][] getTable(String key){		
		for(int i = 0; i < keys.size() ; i++){
			if(key.compareTo(keys.get(i))==0){
				return values.get(i);
			}
		}
		return null;
	}
	
	public String[] getVector(String key){		
		for(int i = 0; i < keys.size() ; i++){
			if(key.compareTo(keys.get(i))==0){
				return vvalues.get(i);
			}
		}
		return null;
	}

}
