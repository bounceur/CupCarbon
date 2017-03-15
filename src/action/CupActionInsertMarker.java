package action;

import java.util.ArrayList;

import map.MapLayer;
import markers.Marker;
import markers.MarkerList;

public class CupActionInsertMarker extends CupAction {	
	
	private ArrayList<Integer> iList;
	private ArrayList<Marker> rList;
	
	public CupActionInsertMarker(ArrayList<Integer> iList) {
		super();
		this.iList = iList;
		rList = new ArrayList<Marker>();
		for(Integer i : iList) {
			Marker marker = Marker.getCentre(MarkerList.markers.get(i),MarkerList.get(i+1),true);
			rList.add(marker);
		}
	}

	@Override
	public void execute() {
		int d = 0;
		for(int i=0; i<iList.size(); i++) {
			MapLayer.addMarker(iList.get(i)+1+d,rList.get(i));
			d++;
		}
	}

	@Override
	public void antiExecute() {
		MarkerList.markers.removeAll(rList);
	}

}
