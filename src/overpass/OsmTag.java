/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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

package overpass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author BOYER Yoann
 * @version 1.0
 */
@XmlRootElement
public class OsmTag {  
	private String key;
	private String value;
	public OsmTag() {}  

	
	@XmlAttribute
	public String getK() {  
	    return this.key;  
	}
	public void setK(String key){
	    this.key = key;
	}
	
	@XmlAttribute
	public String getV() {  
	    return this.value;  
	}
	public void setV(String value) {  
	    this.value = value;
	}
	
	
	public boolean haveBuildingKey(){
		if (key.equals("building"))
			return true;
		return false;
	}
}  