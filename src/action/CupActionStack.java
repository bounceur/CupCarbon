package action;

import java.util.ArrayList;

import cupcarbon.CupCarbon;
import cupcarbon_script.CupCommand;

public class CupActionStack {

	public static int MAX_ELEMENT = 100;
	public static ArrayList<CupActionBlock> actions ;
	public static int idx = -1;
	public static int n = 0;
	
	public static void init() {
		idx = -1;
		n = 0;
		CupCommand.isExecuted = true;
		actions = new ArrayList<CupActionBlock>();
		for(int i=0; i<MAX_ELEMENT+1; i++) {
			actions.add(null);
		}
	}
	
	public static void add(CupActionBlock actionList) {		
		idx++;		
		if(idx > MAX_ELEMENT-1) {
			idx = MAX_ELEMENT-1;
			actions.remove(0);
			actions.add(null);
		}
		n = idx+1;		
		actions.set(idx, actionList);
		idx--;
	}
	
	public static void execute() {
		CupCarbon.cupCarbonController.saveButton.setDisable(false);
		if(idx<n-1) {
			idx++;			
			actions.get(idx).execute();
		}
	}
	
	public static void antiExecute() {
		CupCarbon.cupCarbonController.saveButton.setDisable(false);
		if(idx>-1) {
			actions.get(idx).antiExecute();
			idx--;
		}
		
	}
}
