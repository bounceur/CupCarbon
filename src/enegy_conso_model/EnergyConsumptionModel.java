package enegy_conso_model;

import device.DeviceList;

public class EnergyConsumptionModel {

	public static double p = -1;
	public static int n = -1;
	public static double e = -1;
	public static double r = -1;
	public static int id = -1; // id of the corresponding sensor node
	public static String rn = ""; // the current radio name of the sensor node
	public static String type = ""; // type of the evaluation (TX/RX)
	
	public static double evaluate(String type, int id, String rn, String arg, double p, int n, double e, double r) {
		arg = arg.trim();
		EnergyConsumptionModel.p = p;
		EnergyConsumptionModel.n = n;
		EnergyConsumptionModel.e = e;
		EnergyConsumptionModel.r = r;
		EnergyConsumptionModel.id = id;
		EnergyConsumptionModel.rn = rn;
		EnergyConsumptionModel.type = type;
		
		boolean contained = false;
		String rarg = "";
		String[] match={"(", ")", "+", "-", "*", "/", "%", "^"};
		int i = 0;
		while (!contained && i< match.length) {
			if (arg.contains(match[i]))
					contained = true;
			i=i+1;
		}
		if (contained) { 
			EnergyConsumptionExpression calculator = new EnergyConsumptionExpression();
			rarg = calculator.processInput(arg);
		}
		else 
			rarg = "" + EnergyConsumptionModel.getVariableValue(arg);

		return Double.valueOf(rarg) ;
	}

	public static double getVariableValue(String arg) {
		if(arg.equals("p"))
			return p;
		if(arg.equals("t")) {
			if(DeviceList.weather == null)
				System.err.println("[CupCarbon ERROR]: There is now Weather module.");
			return DeviceList.weather.getValue();
		}
		if(arg.equals("n"))
			return n;
		if(arg.equals("r"))
			return r;
		if(arg.equals("etx"))
			return e;
		if(arg.equals("erx"))
			return e;
		
		double result = 0;
		try{
			result = Double.parseDouble(arg);
		}
		catch(NumberFormatException e) {
			System.err.println("[CupCarbon ERROR - EnergyConsumptionModel]: the varible "+arg+" does not exist.");
		}
		return result;
	}
		
}
