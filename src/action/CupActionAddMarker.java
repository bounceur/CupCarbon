package action;

import markers.Marker;
import markers.MarkerList;

public class CupActionAddMarker extends CupAction {	
	
	private Marker marker;
	
	public CupActionAddMarker(Marker marker) {
		super();
		this.marker = marker;
	}

	@Override
	public void execute() {
		MarkerList.add(marker);		
	}

	@Override
	public void antiExecute() {
		MarkerList.delete(marker);
	}

}
