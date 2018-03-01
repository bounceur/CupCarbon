package senscript;

import device.SensorNode;

public abstract class SenScriptCondition {

	protected SensorNode sensor ;
	protected String arg1 = "";
	protected String arg2 = "";
	
	public SenScriptCondition() {}
	
	public abstract boolean evaluate() ;
	
	public static String [] getTwoParts(String condition) {
		condition = condition.replaceAll(" ", "");
		int frst = condition.indexOf("(");
		int last = condition.lastIndexOf(")");
		if (frst !=-1 && last !=-1)
			condition = condition.substring(frst+1,last);
		
		String [] tReturn = new String [3];
		String[] tCondition = condition.split("<=",2);
		
		tReturn[2] = "<="; 
		
		if(tCondition.length==1) {
			tCondition = tCondition[0].split(">=");
			tReturn[2] = ">=";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("==");
			tReturn[2] = "==";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("!=");
			tReturn[2] = "!=";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split(">");
			tReturn[2] = ">";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("<");
			tReturn[2] = "<";
		}
		tReturn[0] = tCondition[0];
		tReturn[1] = tCondition[1].equals("\\")?"":tCondition[1]; 
		
		return tReturn;
	}
	
	@Override
	public String toString() {		
		return "-";
	}
	
}
