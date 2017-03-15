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

public class Graph {

	private LinkedList<Vertex> vertexList;

	public Graph() {
		vertexList = new LinkedList<Vertex>();
	}

	public void add(Vertex s) {
		vertexList.add(s);
	}

	public void addFirst(Vertex s) {
		vertexList.addFirst(s);
	}

	public Vertex get(int index) {
		return vertexList.get(index);
	}

	public int size() {
		return vertexList.size();
	}

	public void display() {
		for (Vertex s : vertexList) {
			s.display();
			System.out.println();
		}
		System.out.println("------------------");
	}

	public StringBuilder displayNames() {
		StringBuilder sb = new StringBuilder();
		for (Vertex s : vertexList) {
			sb.append(s.displayNames());
			sb.append("\n");
			System.out.println();
		}
		System.out.println("------------------");
		return sb;
	}

	public void remove(int idx) {
		vertexList.set(idx, null);
		vertexList.remove(idx);
	}

	public void removeWithNeighbors(int idx) {
		Vertex s = get(idx);
		vertexList.remove(idx);
		int v;
		for (int i = 0; i < s.getNbNeignbors(); i++) {
			v = s.getNeighbors().get(i);
			for (int j = 0; j < vertexList.size(); j++) {
				if (vertexList.get(j).hasNeighbor(v)) {
					vertexList.get(j).remove(v);
					if (vertexList.get(j).getNbNeignbors() == 0) {
						vertexList.remove(j);
						j--;
					}
					;
				}
			}
		}
	}
}
