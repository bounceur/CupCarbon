package action;

import radio_module.RadioModule;

public class CupActionModifRadioPl extends CupAction {	
	
	private RadioModule radioModule;
	private double pl;
	private double cPl;
	
	public CupActionModifRadioPl(RadioModule radioModule, double cPl, double pl) {
		super();
		this.radioModule = radioModule;
		this.pl = pl;
		this.cPl = cPl;
	}

	@Override
	public void execute() {
		radioModule.setPl(pl);
	}

	@Override
	public void antiExecute() {
		radioModule.setPl(cPl);
	}

}
