/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/
 
/** Definition:
 * This class allows obtaining the value of the object parameter (sensor, device or marker) 
 * or the value of the radio parameter for a particular sensor
 * 
 * Command examples:
 * getparameter object S1 senscript x
 * -> It obtains the value of the senScript parameter of sensor S1
 * getparameter object S1 gps x
 * -> It obtains the value of the gps parameter of sensor S1
 * getparameter object S1 longitude x
 * -> It obtains the value of the longitude parameter of sensor S1
 * getparameter object S1 latitude x
 * -> It obtains the value of the latitude parameter of sensor S1
 * getparameter object S1 elevation x
 * -> It obtains the value of the elevation parameter of sensor S1
 * getparameter object S1 radius x
 * -> It obtains the value of the radius parameter of sensor S1
 * getparameter object S1 suradius x
 * -> It obtains the value of the sensor unit radius parameter of sensor S1
 * getparameter object S1 emax x
 * -> It obtains the value of the energy max parameter of sensor S1
 * getparameter object S1 esensing x
 * -> It obtains the value of the sesing consumption parameter of sensor S1
 * getparameter object S1 datarate x
 * -> It obtains the value of the UART Data Rate parameter of sensor S1
 * getparameter object S1 drift x
 * -> It obtains the value of the drift time parameter of sensor S1
 * getparameter object MS2 deg x //for directional sensors
 * -> It obtains the value of the deg parameter of directional sensor MS2
 * getparameter object MS2 dec x //for directional sensors
 * -> It obtains the value of the dec parameter of directional sensor MS2
 * getparameter radio S1 radio1 my x
 * -> It obtains the value of the my parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 ch x
 * -> It obtains the value of the ch parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 nid x
 * -> It obtains the value of the nid parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 radius x
 * -> It obtains the value of the radio radius parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 etx x
 * -> It obtains the value of the etx parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 erx x
 * -> It obtains the value of the erx parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 eslp x
 * -> It obtains the value of the eslp parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 elisten x
 * -> It obtains the value of the elisten parameter of the radio1 of the sensor S1
 * getparameter radio S1 radio1 radiorate x
 * -> It obtains the value of the data rate parameter of the radio1 of the sensor S1
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.Device;
import device.DeviceList;
import device.DirectionalSensorNode;
import device.SensorNode;


public class CupCommand_GETPARAMETER extends CupCommand {

	protected String option = "";
	protected String device = "";
	protected String radio = "";
	protected String parameter = "";
	protected String value = "";
	
	protected String sOption = "";
	protected String sDevice = "";
	protected String sRadio = "";
	protected String sParameter = "";
	protected String sValue = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_GETPARAMETER(CupScript script, String option, String device, String radio, String parameter, String value) {
		this.script = script ;
		this.option = option;
		this.device = device;
		this.radio = radio;
		this.parameter = parameter;
		this.value = value;		
	}


	//---------------------------------------------------------------------------------------------------------------------
	// execute getparameter
	// It obtains the value of the object parameter (sensor, device or marker) 
	// or the value of the radio parameter for a particular sensor
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		sOption = script.getVariableValue(option);
		sDevice = script.getVariableValue(device);
		Device node = DeviceList.getNodeByName(sDevice);
		SensorNode sensorNode = (SensorNode) DeviceList.getNodeByName(sDevice);
		sRadio = script.getVariableValue(radio);
		sParameter = script.getVariableValue(parameter);
		sValue = script.getVariableValue(value);
		if (isExecuted) {
			switch (sOption) {
			case("object") :
				switch (sParameter) {
				case("senscript") :
					if (node.getType() != Device.GAS || node.getType() != Device.MOBILE || node.getType() != Device.MARKER) {
						script.addVariable(sValue, node.getScriptFileName());
						rep = "000 Device: "+ sDevice + " has a senscript file = " + script.getVariableValue(sValue) + " = " + node.getScriptFileName();
						currentExecution = true;
					}
					break;
				case("gps") :
					if (node.getType() != Device.GAS || node.getType() != Device.MARKER) {
						script.addVariable(sValue, node.getGPSFileName());
						rep = "000 Device: "+ sDevice + " has a gps file = " + script.getVariableValue(sValue) + " = " + node.getGPSFileName();
						currentExecution = true;
					}
					break;
					case("longitude") :
						script.addVariable(sValue, "" + node.getLongitude());
					rep = "000 Device: "+ sDevice + " has a longitude value = " + script.getVariableValue(sValue) + " = " + node.getLongitude();
					currentExecution = true;
					break;
				case("latitude") :
					script.addVariable(sValue, "" + node.getLatitude());
					rep = "000 Device: "+ sDevice + " has a latidtude value = " + script.getVariableValue(sValue) + " = " + node.getLatitude();
					currentExecution = true;
					break;
				case("elevation") :
					script.addVariable(sValue, "" + node.getElevation());
					rep = "000 Device: "+ sDevice + " has an elevation value = " + script.getVariableValue(sValue) + " = " + node.getElevation();
					currentExecution = true;
					break;
				case("radius") :
					script.addVariable(sValue, "" + node.getRadius());
					rep = "000 Device: "+ sDevice + " has a radius value = " + script.getVariableValue(sValue) + " = " + node.getRadius();
					currentExecution = true;
					break;
				case("suradius") :
					script.addVariable(sValue, "" + sensorNode.getSensorUnitRadius());
					rep = "000 Device: "+ sDevice + " has a sensor unit radius value = " + script.getVariableValue(sValue) + " = " + node.getLongitude();
					currentExecution = true;
					break;
				case("emax") :
					script.addVariable(sValue, "" + node.getBattery().getEMax());
					rep = "000 Device: "+ sDevice + " has an energy value = " + script.getVariableValue(sValue) + " = " + node.getSensorUnitRadius();
					currentExecution = true;
					break;
				case("esensing") :
					script.addVariable(sValue, "" + sensorNode.getSensorUnit().getESensing());
					rep = "000 Device: "+ sDevice + " has a consumption value = " + script.getVariableValue(sValue) + " = " + sensorNode.getSensorUnit().getESensing();
					currentExecution = true;
					break;
				case("datarate") :
					script.addVariable(sValue, "" + node.getUartDataRate());
					rep = "000 Device: "+ sDevice + " has an UART data rate value = " + script.getVariableValue(sValue) + " = " + node.getLongitude();
					currentExecution = true;
					break;
				case("drfit") :
					script.addVariable(sValue, "" + node.getDriftTime());
					rep = "000 Device: "+ sDevice + " has a drift time value = " + script.getVariableValue(sValue) + " = " + node.getUartDataRate();
					currentExecution = true;
					break;
				case("deg") :
					if (node.getType() == Device.DIRECTIONAL_SENSOR) {
						script.addVariable(sValue, "" + ((DirectionalSensorNode) sensorNode).getSensorUnitCoverage());
						rep = "000 Device: "+ sDevice + " has a drift time value = " + script.getVariableValue(sValue) + " = " + node.getLongitude();
						currentExecution = true;
					}
					break;
				case("dec") :
					if (node.getType() == Device.DIRECTIONAL_SENSOR) {
						script.addVariable(sValue, "" + ((DirectionalSensorNode) sensorNode).getSensorUnitDirection());
						rep = "000 Device: "+ sDevice + " has a drift time value = " + script.getVariableValue(sValue) + " = " + ((DirectionalSensorNode) sensorNode).getSensorUnitCoverage();
						currentExecution = true;
					}
					break;
				default:
					rep = "[ERROR] Unknown paramerter or not accepted value ";
					currentExecution = false;
				}
				break;
			case("radio"):
				if (node.getType() != Device.GAS || node.getType() != Device.MOBILE || node.getType() != Device.MARKER) {
					switch (sParameter) {
					case("my") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getMy());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a MY value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getMy();
						currentExecution = true;
						break;
					case("ch") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getCh());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a CH value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getCh();
						currentExecution = true;
						break;
					case("nid") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getNId());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a NID value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getNId();
						currentExecution = true;
						break;
					case("radius") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getRadioRangeRadius());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a radius value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getRadioRangeRadius();
						currentExecution = true;
						break;
					case("etx") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getETx());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has an ETX value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getETx();
						currentExecution = true;
						break;
					case("erx") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getERx());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has an ERX value = " + script.getVariableValue(sValue) + " = " + node.getLongitude();
						currentExecution = true;
						break;
					case("eslp") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getESleep());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has an ESleeping value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getERx();
						currentExecution = true;
						break;
					case("elisten") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getEListen());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has an EListening value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getEListen();
						currentExecution = true;
						break;
					case("radiorate") :
						script.addVariable(sValue, "" + sensorNode.getRadioModuleByName(sRadio).getRadioDataRate());
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a data rate value = " + script.getVariableValue(sValue) + " = " + sensorNode.getRadioModuleByName(sRadio).getRadioDataRate();
						currentExecution = true;
						break;
					default:
						rep = "[ERROR] Unknown paramerter or not accepted value ";
						currentExecution = false;
					}
				}
				break;
			default :
				rep = "001 [ERROR] No such map object: "+sOption;
				currentExecution = false;
			}
		}
		else {
			rep = "ERROR IN EXECUTING THE PREVIOUS STEP, THE SCRIPT WILL NOT CONTINUE TO EXECUTE";
			script.setBreaked(true);
		}
		
		System.out.println(rep);
		CupScript.slog.println(rep);
		isExecuted = currentExecution;
		return rep;
	}

	@Override
	public String toString() {
		return "GETPARAMETER";
	}
}
