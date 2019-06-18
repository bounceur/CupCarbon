package action;

import buildings.Building;
import buildings.BuildingList;
import visibility.VisibilityLauncher;

public class CupActionAddBuilding extends CupAction {	
	
	private Building building;
	
	public CupActionAddBuilding(Building building) {
		super();
		this.building = building;
	}

	@Override
	public void execute() {
		BuildingList.add(building);
		VisibilityLauncher.calculate();
	}

	@Override
	public void antiExecute() {
		BuildingList.delete(building);
		VisibilityLauncher.calculate();
	}

}
