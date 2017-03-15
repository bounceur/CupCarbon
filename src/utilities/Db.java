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

package utilities;

import java.util.LinkedList;

public class Db {

	private LinkedList<String> fichiers;
	private LinkedList<String> sensorsName;

	public Db() {
		fichiers = new LinkedList<String>();
		sensorsName = new LinkedList<String>();
	}

	public void add(String name, String path) {
		fichiers.addLast(path);
		sensorsName.addLast(name);
	}

	public String getPath(String name) {
		for (int i = 0; i < this.sensorsName.size(); i++) {
			if (name.compareTo(this.sensorsName.get(i)) == 0) {
				return fichiers.get(i);
			}
		}
		return null;
	}

}
