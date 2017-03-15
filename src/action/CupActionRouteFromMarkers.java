package action;

import java.util.LinkedList;

import markers.Marker;
import markers.MarkerList;

public class CupActionRouteFromMarkers extends CupAction {	
	
	private LinkedList<Marker> oList;
	private LinkedList<Marker> nList;
	
	public CupActionRouteFromMarkers(LinkedList<Marker> oList,  LinkedList<Marker> nList) {
		super();
		this.oList = oList;
		this.nList = nList;
	}

	@Override
	public void execute() {
		MarkerList.markers = nList;
	}

	@Override
	public void antiExecute() {
		MarkerList.markers = oList;
	}

}
