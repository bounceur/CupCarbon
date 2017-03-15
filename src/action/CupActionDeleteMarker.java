package action;

import markers.Marker;
import markers.MarkerList;

public class CupActionDeleteMarker extends CupAction {
	
	private Marker marker;
	private int index;
	
	public CupActionDeleteMarker(Marker marker, int index) {
		super();
		this.marker = marker ;
		this.index = index ;
	}

	@Override
	public void execute() {
		MarkerList.delete(marker);
	}

	@Override
	public void antiExecute() {
		MarkerList.add(index, marker);
	}

}
