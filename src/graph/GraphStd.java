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

public class GraphStd {

	private LinkedList<VertexStd> liste;

	public GraphStd() {
		liste = new LinkedList<VertexStd>();
	}

	// public Graph(Graph graphe) {
	// liste = new LinkedList<Vertex>() ;
	// for(int i=0; i<graphe.size(); i++) {
	// liste.add(graphe.get(i));
	// }
	// }

	public void add(VertexStd s) {
		liste.add(s);
	}

	public void addFirst(VertexStd s) {
		liste.addFirst(s);
	}

	public VertexStd get(int index) {
		// System.out.println(liste.size());
		// System.out.println(index);
		return liste.get(index);
	}

	// public int getIndex(Vertex s) {
	// for(int i=0; i<liste.size(); i++) {
	// if(liste.get(i).getNumber() == s.getNumber())
	// return i ;
	// }
	//
	// int i = 0 ;
	// for (Iterator<Vertex> iterator = liste.iterator(); iterator.hasNext();) {
	// if(iterator.next() == ;
	//
	// }
	//
	//
	// return -1;
	// }

	public int size() {
		return liste.size();
	}

	public VertexStd getSommetParNumero(int number) {
		for (VertexStd s : liste) {
			if (s.getNumber() == number)
				return s;
		}
		return null;
	}

	public double getDistance(VertexStd s1, VertexStd s2) {
		return (s1.distance(s2));
	}

	public void display() {
		for (VertexStd s : liste) {
			s.display();
			System.out.println();
		}
		System.out.println("------------------");
	}

	public StringBuilder displayNames() {
		StringBuilder sb = new StringBuilder();
		for (VertexStd s : liste) {
			sb.append(s.displayNames());
			sb.append("\n");
			System.out.println();
		}
		System.out.println("------------------");
		return sb;
	}

	public void supprimer(int idx) {
		liste.set(idx, null);
		liste.remove(idx);

	}

	public void supprimerAvecVoisins(int idx) {
		VertexStd s = get(idx);
		liste.remove(idx);
		int n = liste.size();
		for (int i = 0; i < s.getNbVoisins(); i++) {
			for (int j = 0; j < n; j++) {
				if (liste.get(j).hasVoisin(s)) {
					liste.remove(j);
					j--;
					n--;
				}
			}
		}
	}

	public void supprimerAvecVoisins(int idx, int nn) {
		VertexStd s = get(idx);
		liste.remove(idx);
		int n = liste.size();
		for (int i = 0; i < s.getNbVoisins(); i++) {
			for (int j = 0; j < n; j++) {
				if (liste.get(j).hasVoisin(s)) {
					liste.remove(j);
					j--;
					n--;
				}
			}
		}
	}
}
