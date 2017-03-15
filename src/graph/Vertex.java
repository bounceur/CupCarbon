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

package graph;

import java.util.LinkedList;

public class Vertex {

	private String name = "";
	private int number;
	private LinkedList<Integer> neighbors;
	private boolean selected = false ;

	public Vertex(int number, String name) {
		neighbors = new LinkedList<Integer>();
		this.number = number;
		this.name = name;
	}

	public void addNeighbor(int s) {
		neighbors.addFirst(s);
	}
	
	public LinkedList<Integer> getNeighbors() {
		return neighbors;
	}

	public void display() {
		System.out.print(getNumber()+ " : ");
		for (int s : neighbors) {
			System.out.print(s + " ");
		}
	}

	public StringBuilder displayNames() {
		StringBuilder sb = new StringBuilder() ;
		sb.append(getNumber() + ": ");
		System.out.print(getNumber() + ": ");
		for (int s : neighbors) {
			sb.append(s + " ");
			System.out.print(s + " ");
		}
		return sb ;
	}

	public int getNbNeignbors() {
		return neighbors.size();
	}

	public boolean hasNeighbor(int s) {
		for (int i = 0; i < neighbors.size(); i++) {
			if (getNeighbors().get(i) == s)
				return true;
		}
		return false;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	
	public void remove(int i) {
		neighbors.remove(neighbors.indexOf(i));
	}

	public void setSelection(boolean b) {
		this.selected = b ;
	}
	
	public boolean getSelection() {
		return selected ;
	}
	
	public int size() {
		return neighbors.size();
	}
}