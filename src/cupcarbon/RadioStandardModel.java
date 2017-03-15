package cupcarbon;

public class RadioStandardModel {

	private String name;
	private String std;
	
	public RadioStandardModel(String name, String std) {
		this.name = name;
		this.std = std;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getStd() {
		return std;
	}
	
}
