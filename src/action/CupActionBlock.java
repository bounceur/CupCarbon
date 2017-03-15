package action;

import java.util.ArrayList;

public class CupActionBlock {

	public ArrayList<CupAction> actionBlock = new ArrayList<CupAction>();
	
	public void addAction(CupAction action) {
		actionBlock.add(action);
	}
	
	public void execute() {
		for(CupAction action : actionBlock) {
			action.execute();
		}
	}
	
	public void antiExecute() {
		for(CupAction action : actionBlock) {
			action.antiExecute();
		}
	}
	
	public int size() {
		return actionBlock.size();
	}
}
