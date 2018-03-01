package action;

import buildings.Building;
import buildings.BuildingList;
import visibility.VisibilityLauncher;

public class CupActionDeleteBuilding extends CupAction {
	
	private Building building;
	
	public CupActionDeleteBuilding(Building building) {
		super();
		this.building = building;
	}

	@Override
	public void execute() {
		BuildingList.delete(building);
		VisibilityLauncher.calculate();
	}

	@Override
	public void antiExecute() {
		BuildingList.add(building);
		VisibilityLauncher.calculate();
	}

}
