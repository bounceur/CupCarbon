package script;

import java.util.Iterator;
import java.util.LinkedList;

public class VariableList {
	
	private LinkedList<String> keys;
	private LinkedList<String> values;

	public VariableList(){
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
