package action;

import buildings.Building;
import buildings.BuildingList;

public class CupActionDeleteBuilding extends CupAction {
	
	private Building building;
	
	public CupActionDeleteBuilding(Building building) {
		super();
		this.building = building;
	}

	@Override
	public void execute() {
		BuildingList.delete(building);
	}

	@Override
	public void antiExecute() {
		BuildingList.add(building);
	}

}
