package action;

import buildings.Building;
import buildings.BuildingList;

public class CupActionAddBuilding extends CupAction {	
	
	private Building building;
	
	public CupActionAddBuilding(Building building) {
		super();
		this.building = building;
	}

	@Override
	public void execute() {
		BuildingList.add(building);
	}

	@Override
	public void antiExecute() {
		BuildingList.delete(building);
	}

}
