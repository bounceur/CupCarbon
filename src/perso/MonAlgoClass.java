package perso;

import java.util.LinkedList;
import java.util.List;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;


public class MonAlgoClass extends Thread {

	public void run() {
		DeviceList.initAll();
		MapLayer.repaint();
		
		
		List<SensorNode> capteurs = DeviceList.sensors;
		
		SensorNode startingNode = capteurs.get(0);
		lancer(startingNode);
	}
	
	public void lancer(SensorNode node) {
		node.mark();
		
		List<SensorNode> pVoisins = node.getNonMarkedNeighbors();
		
		if(pVoisins.size()>0) {
			for(int i=0; i<pVoisins.size(); i++) {
				SensorNode next = pVoisins.get(i);
				next.mark();
				DeviceList.edge(node, next);
				LinkedList<SensorNode> nodeL = new LinkedList<SensorNode>();
				nodeL.add(next);
				find(node, next, node, next, nodeL, pVoisins);
			}
		}
	}
	
	public void find(SensorNode start, SensorNode ref, SensorNode prec, SensorNode node, LinkedList<SensorNode> nodeL, List<SensorNode> pVoisins) {
		SensorNode next = getOneUnmarkedVoisinL(nodeL, pVoisins);
		if(next != null) {
			if(next.isNeighborOf(nodeL.getFirst()))
				nodeL.addFirst(next);
			if(next.isNeighborOf(nodeL.getLast()))
				nodeL.addLast(next);
			DeviceList.edge(node, next);
			next.mark();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			find(start, ref, node, next, nodeL, pVoisins);
		} else {
			if((node.isNeighborOf(ref)) && (prec != ref)) {
				DeviceList.edge(node, ref);
				DeviceList.noEdge(start, ref);
				start.unmark();
			}
			else {
				DeviceList.edge(node, start);
			}
			if(start.getNonMarkedNeighbors().size()>0) {
				lancer(start);
			}
			else {
				lancer(ref);
			}
		}
	}
	
	public SensorNode getOneUnmarkedVoisin(SensorNode node, List<SensorNode> pVoisins) {
		for(SensorNode s1 : node.getNonMarkedNeighbors()) {
			for(SensorNode s2 : pVoisins) {
				if(s1==s2)
					return s1;
			}
		}
		return null;
	}
	public SensorNode getOneUnmarkedVoisinL(List<SensorNode> nodeL, List<SensorNode> pVoisins) {
		for(SensorNode l : nodeL) {
			for(SensorNode s1 : l.getNonMarkedNeighbors()) {
				for(SensorNode s2 : pVoisins) {
					if(s1==s2)
						return s1;
				}
			}
		}
		return null;
	}
		
}
