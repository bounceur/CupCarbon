package solver;

import graph.Graph;
import graph.GraphStd;
import graph.Vertex;
import graph.VertexStd;

import java.util.List;
import java.util.ListIterator;

import device.Device;
import device.SensorNode;

public class SensorGraph {

	public static GraphStd toSensorGraph(List<SensorNode> nodes, int size) {
		SensorNode n1 = null;
		SensorNode n2 = null;
		double distance = 0;
		GraphStd graphe = new GraphStd();
		int i = 0;
		int j = 0;
		ListIterator<SensorNode> iterator = nodes.listIterator();
		ListIterator<SensorNode> iterator2;
		while (iterator.hasNext()) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				// System.out.println(n1.getNodeName()+" : "+i);
				graphe.add(new VertexStd(i++, n1.getName()));
				n1.setMarked(false);
			}
		}

		iterator = nodes.listIterator();
		i = 0;
		while (iterator.hasNext() && iterator.nextIndex() < size - 1) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				iterator2 = nodes.listIterator(iterator.nextIndex());
				j = i + 1;
				while (iterator2.hasNext()) {
					n2 = iterator2.next();
					if (n1.radioDetect(n2) && n2.getType() == Device.SENSOR) {
						distance = n1.distance(n2);
						graphe.get(i).ajouterVoisin(graphe.get(j), distance);
						graphe.get(j).ajouterVoisin(graphe.get(i), distance);
					}
					j++;
				}
				i++;
			}
		}
		return graphe;
	}

	public static Graph toSensorTargetGraph(List<SensorNode> nodes, int size) {
		Device n1 = null;
		Device n2 = null;
		// double distance = 0;
		Graph graphe = new Graph();
		int i = 0;
		int j = 0;
		ListIterator<SensorNode> iterator = nodes.listIterator();
		ListIterator<SensorNode> iterator2;
		while (iterator.hasNext()) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				// System.out.println(n1.getNodeName()+" : "+i);
				graphe.add(new Vertex(i++, n1.getName()));
				n1.setMarked(false);
			}
		}

		iterator = nodes.listIterator();
		i = 0;
		while (iterator.hasNext() && iterator.nextIndex() < size - 1) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				iterator2 = nodes.listIterator();
				j = 0;
				while (iterator2.hasNext()) {
					n2 = iterator2.next();
					if (((SensorNode) n1).detect(n2) && n2.getType() != Device.SENSOR) {
						// distance = n1.distance(n2);
						graphe.get(i).addNeighbor(j);
					}
					j++;
				}
				i++;
			}
		}
		return graphe;
	}

}
