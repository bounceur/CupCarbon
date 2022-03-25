/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

package device;
import java.util.LinkedList;

public abstract class MobileG extends DeviceWithoutRadio { 
		
	protected LinkedList<Double> valueTime;
	protected int valueIndex = 0;

	
	public MobileG(double x, double y, double z, double rayon, String gpsFileName, int id) {
		super(x, y, z, rayon, id);
		mobile = true ;		
		this.gpsFileName = gpsFileName ;
	}

	//Refactoring - Pull up method
	//Below method is pulled up from Child Class 
	
		public double getNextValueTime() {
			if (valueTime.size() > 0) {				
				return valueTime.get(valueIndex);
			}
			return 0;
		}
}