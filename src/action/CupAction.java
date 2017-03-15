package action;

public abstract class CupAction {

	//protected CupCommand command ;
	//protected CupCommand aCommand ;
	//public static long tref = 0;
	
	public CupAction() {}
	
	public abstract void execute();
	public abstract void antiExecute();
	
}
