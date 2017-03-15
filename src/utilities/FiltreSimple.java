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

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltreSimple extends FileFilter {

	private String description;
	private String extension;

	public FiltreSimple(String description, String extension) {
		if (description == null || extension == null) {
			throw new NullPointerException(
					"Description or extension can not be null.");
		}
		this.description = description;
		this.extension = extension;
	}

	//
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		String nomFichier = file.getName().toLowerCase();

		return nomFichier.endsWith(extension);
	}

	public String getDescription() {
		return description;
	}
}