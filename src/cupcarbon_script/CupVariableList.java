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

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import java.util.Iterator;
import java.util.LinkedList;

public class CupVariableList {
	
	private LinkedList<String> keys;
	private LinkedList<String> values;

	public CupVariableList(){
		keys = new LinkedList<String>();
		values = new LinkedList<String>();
	}
	
	public void put(String key, String value){
		boolean exist = false;
		
		for(int i = 0; i < keys.size() ; i++){
			if(key.equals(keys.get(i))){
				values.remove(i);
				values.add(i, value);
				exist = true;
			}
		}
		if(!exist){
			keys.addLast(key);
			values.addLast(value);
		}
		
	}
	
	public String getValue(String key){		
		for(int i = 0; i < keys.size() ; i++){
			if(key.compareTo(keys.get(i))==0){
				return values.get(i);
			}
		}
		return null;
	}
	
	public boolean exist(String v) {
		return keys.contains(v);
	}
	
	public void remove(String v) {
		int k = -1;
		for(int i=0; i<keys.size(); i++) {
			if(keys.get(i).equals(v)) {
				k = i;
				break;
			}
		}
		if (k>-1) {
			keys.remove(k);
			values.remove(k);
		}
	}
	
	@Override
	public String toString() {
		String s = super.toString()+"=[";
		Iterator<String> it1 = keys.iterator();
		Iterator<String> it2 = values.iterator();
		while(it1.hasNext()) {
			s += it1.next() + "="+ it2.next()+"\n";
		}
		s+="]";
		return s ;
	}
	
}
