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

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author BOYER Yoann
 * @version 1.0
 */
@XmlRootElement
public class OsmWay {  
	private String id;
	private List<OsmNd> nds;
	private List<OsmTag> tags = null;
	public OsmWay(){}
	

    @XmlAttribute
	public String getId() {  
	    return id;  
	}  
	public void setId(String id) {
	    this.id = id;  
	}
	
	@XmlElement
    public List<OsmNd> getNd() {
        return nds;
    }  
    public void setNd(List<OsmNd> nds) {
        this.nds = nds;
    }
    
	@XmlElement
    public List<OsmTag> getTag() {
        return tags;  
    }  
    public void setTag(List<OsmTag> tags) {
        this.tags = tags;
    }

    
    public boolean isBuilding(){
    	if (tags != null)
	    	for (OsmTag tag:tags)
	    		if (tag.haveBuildingKey())
	    			return true;

    	return false;
    }
}  