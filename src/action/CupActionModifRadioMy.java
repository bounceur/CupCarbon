package action;

import radio_module.RadioModule;

public class CupActionModifRadioMy extends CupAction {	
	
	private RadioModule radioModule;
	private int my;
	private int cMy;
	
	public CupActionModifRadioMy(RadioModule radioModule, int cMy, int my) {
		super();
		this.radioModule = radioModule;
		this.my = my;
		this.cMy = cMy;
	}

	@Override
	public void execute() {
		radioModule.setMy(my);
	}

	@Override
	public void antiExecute() {
		radioModule.setMy(cMy);
	}

}
