package radio_module;

public class XbeeTest {

	public static void main(String [] arg) {
//		System.out.println("void setup() {");
//		System.out.println("\tdelay(1000);");
//		System.out.println("\tpinMode(13, OUTPUT);");
//		System.out.println("\tSerial.begin(9600);");
//		System.out.println("\tdelay(1000);");
//		System.out.println();
//		
//		//at("CH0E");		
//		//at("WR");
//		//at("AC");
//		
//		System.out.println("}\n");
//		System.out.println("void loop() {");
//		System.out.println("\tdigitalWrite(13, 1);");
//		System.out.println("\tdelay(1000);");
//		System.out.println("\tdigitalWrite(13, 0);");
//		System.out.println("\tdelay(1000);");
//		System.out.println("}");
		
		//System.out.println(XBeeFrameGenerator.data16("bonjour", "00", "01"));
		System.out.println(XBeeFrameGenerator.at("ch"));
	}
	
}
