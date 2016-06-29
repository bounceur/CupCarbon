package battery;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

//------------------------------------------------------------------------------------------KADJOUH
/**
 * @author Nabil Kadjouh
 */
// ------------------------------------------------------------------------------------------KADJOUH
public class BatteryModel implements Cloneable {

	private String modelName;
	private int capacity; // mAH
	private double tension; // Volts
	private String dischargeCurrentModel; // mA

	public BatteryModel() {
		setModelName("Standard");
		setCapacity(8000);
		setTension(3.0);
		setDischargeCurrentModel("5");

	}

	public BatteryModel(String modelName, int capacity, double tension, String dischargeCurrentModel,
			double dischargeCurrent) {
		setModelName(modelName);
		setCapacity(capacity);
		setTension(tension);
		setDischargeCurrentModel(dischargeCurrentModel);

	}
	// ------------------------------------------------------------------------------------------KADJOUH

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setTension(double tension) {
		this.tension = tension;
	}

	public void setDischargeCurrentModel(String dischargeCurrentModel) {
		this.dischargeCurrentModel = dischargeCurrentModel;
	}

	// ------------------------------------------------------------------------------------------KADJOUH
	public String getModelName() {
		return modelName;
	}

	public int getCapacity() {
		return capacity;
	}

	public double getTension() {
		return tension;
	}

	public String getDischargeCurrentModel() {
		return dischargeCurrentModel;
	}

	// ------------------------------------------------------------------------------------------KADJOUH
	public static double getCurrentFromModel(double temperature, double tension, String dischargeCurrentModel)
			throws Exception {

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		engine.eval("v=" + tension + "");
		engine.eval("temperature= " + temperature + "");
		String foo = dischargeCurrentModel;
		return (double) engine.eval(foo);
	}

	// ------------------------------------------------------------------------------------------KADJOUH
	public static double levelConsumption(double dischargeCurrent, int capacity, double time) {
		return ((dischargeCurrent * time) / (capacity * 36));

	}

	// ------------------------------------------------------------------------------------------KADJOUH
	@Override
	public BatteryModel clone() throws CloneNotSupportedException {
		BatteryModel newBatteryModel = (BatteryModel) super.clone();

		return newBatteryModel;
	}

}