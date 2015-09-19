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

package script;

import java.util.LinkedList;

public class TabList {
	
	private LinkedList<String> keys;
	private LinkedList<Object[][]> values;

	public TabList(){
		keys = new LinkedList<String>();
		values = new LinkedList<Object[][]>();
	}
	
	
	
	public void putTable(String key, int heigth,int width){
		boolean exist = false;
		Object[][] tab = new Object[heigth][width];
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
	
	
	
	public Object[][] getTable(String key){		
		for(int i = 0; i < keys.size() ; i++){
			if(key.compareTo(keys.get(i))==0){
				return values.get(i);
			}
		}
		return null;
	}

}
