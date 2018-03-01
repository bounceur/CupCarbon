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

public class VertexStd {

	private String name = "";
	
	private int number;
	
	private boolean marque = false;
	
	private LinkedList<VertexStd> voisins;
	
	private LinkedList<Double> distances;

	public VertexStd(int number, String name) {
		voisins = new LinkedList<VertexStd>();
		distances = new LinkedList<Double>();
		this.number = number;
		this.name = name;
	}

	public void ajouterVoisin(VertexStd s, double distance) {
		voisins.addFirst(s);
		distances.addFirst(distance);
	}

	public void marquer() {
		marque = true;
	}

	public boolean etat() {
		return marque;
	}

	public double distance(VertexStd s) {
		for (int i = 0; i < voisins.size(); i++) {
			if (s == voisins.get(i)) {
				return distances.get(i);
			}
		}
		return -1;
	}

	public LinkedList<VertexStd> getVoisins() {
		return voisins;
	}

	public void display() {
		//System.out.print(number + "("+name+")"+ ": ");
		System.out.print((number+1) + ": ");
		for (VertexStd s : voisins) {
			//System.out.print(s.getNumber() + "("+s.getName()+")"+ " ");
			System.out.print((s.getNumber()+1) + " ");
		}
	}

	public StringBuilder displayNames() {
		StringBuilder sb = new StringBuilder() ;
		sb.append(getName() + ": ");
		System.out.print(getName() + ": ");
		for (VertexStd s : voisins) {
			sb.append(s.getName() + "(" + (int)distance(s) + ") ");
			System.out.print(s.getName() + "(" + distance(s) + ") ");
		}
		return sb ;
	}

	public int getNbVoisins() {
		return voisins.size();
	}

	public boolean hasVoisin(VertexStd s) {
		for (int i = 0; i < voisins.size(); i++) {
			if (this.getVoisins().get(i) == s)
				return true;
		}
		return false;
	}

	//public String toString() {
	//	return "" + name;
	//}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}