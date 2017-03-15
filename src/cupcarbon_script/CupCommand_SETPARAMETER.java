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
 * This class allows assigning a value to an object parameter (sensor, device or marker) 
 * or a value to a radio parameter for a particular sensor
 * 
 * Command examples:
 * setparameter object S1 senscript value
 * -> It assigns the defined value to the senScript parameter of sensor S1
 * setparameter object S1 gps value
 * -> It assigns the defined value to the gps parameter of sensor S1
 * setparameter object S1 longitude value
 * -> It assigns the defined value to the longitude parameter of sensor S1
 * setparameter object S1 latitude value
 * -> It assigns the defined value to the latitude parameter of sensor S1
 * setparameter object S1 elevation value
 * -> It assigns the defined value to the elevation parameter of sensor S1
 * setparameter object S1 radius value
 * -> It assigns the defined value to the radius parameter of sensor S1
 * setparameter object S1 suradius value
 * -> It assigns the defined value to the sensor unit radius parameter of sensor S1
 * setparameter object S1 emax value
 * -> It assigns the defined value to the energy max parameter of sensor S1
 * setparameter object S1 esensing value
 * -> It assigns the defined value to the sesing consumption parameter of sensor S1
 * setparameter object S1 datarate value
 * -> It assigns the defined value to the UART Data Rate parameter of sensor S1
 * setparameter object S1 drift value
 * -> It assigns the defined value to the drift time parameter of sensor S1
 * setparameter object MS2 deg value //for media sensors
 * -> It assigns the defined value to the deg parameter of media sensor MS2
 * setparameter object MS2 dec value //for media sensors
 * -> It assigns the defined value to the dec parameter of media sensor MS2
 * setparameter radio S1 radio1 my value
 * -> It assigns the defined value to the my parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 ch value
 * -> It assigns the defined value to the ch parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 nid value
 * -> It assigns the defined value to the nid parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 radius value
 * -> It assigns the defined value to the radio radius parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 etx value
 * -> It assigns the defined value to the etx parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 erx value
 * -> It assigns the defined value to the erx parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 eslp value
 * -> It assigns the defined value to the eslp parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 elisten value
 * -> It assigns the defined value to the elisten parameter of the radio1 of the sensor S1
 * setparameter radio S1 radio1 radiorate value
 * -> It assigns the defined value to the data rate parameter of the radio1 of the sensor S1
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

import cupcarbon.CupCarbon;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.Device;
import device.DeviceList;
import device.MediaSensorNode;
import device.SensorNode;


public class CupCommand_SETPARAMETER extends CupCommand {

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
	public CupCommand_SETPARAMETER(CupScript script, String option, String device, String radio, String parameter, String value) {
		this.script = script ;
		this.option = option;
		this.device = device;
		this.radio = radio;
		this.parameter = parameter;
		this.value = value;		
	}

	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_SETPARAMETER(CupScript script, String option, String parameter, String value) {
		this.script = script ;
		this.option = option;
		this.parameter = parameter;
		this.value = value;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute setparameter
	// It assigns the defined value to an object parameter (sensor, device or marker) 
	// or a value to a radio parameter for a particular sensor
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
				if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
					CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
				if (CupCarbon.cupCarbonController != null) {
					CupCarbon.cupCarbonController.updateObjectListView();
					CupCarbon.cupCarbonController.getNodeInformations();
					CupCarbon.cupCarbonController.getRadioInformations();
					CupCarbon.cupCarbonController.updateSelectionInListView();
				}
				node.setSelected(true);
				switch (sParameter) {
				case("senscript") :
					if (node.getType() != Device.GAS || node.getType() != Device.MOBILE || node.getType() != Device.MARKER) {
						node.setScriptFileName(sValue);
						rep = "000 Device: "+ sDevice + " has a new senscript file = " + sValue;
						currentExecution = true;
					}
					break;
				case("gps") :
					if (node.getType() != Device.GAS || node.getType() != Device.MARKER) {
						node.setGPSFileName(sValue);
						rep = "000 Device: "+ sDevice + " has a new gps file = " + sValue;
						currentExecution = true;
					}
					break;
					case("longitude") :
					node.setLongitude(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new longitude value = " + sValue;
					currentExecution = true;
					break;
				case("latitude") :
					node.setLatitude(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new latidtude value = " + sValue;
					currentExecution = true;
					break;
				case("elevation") :
					node.setElevation(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new elevation value = " + sValue;
					currentExecution = true;
					break;
				case("radius") :
					node.setRadius(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new radius value = " + sValue;
					currentExecution = true;
					break;
				case("suradius") :
					sensorNode.setSensorUnitRadius(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new sensor unit radius value = " + sValue;
					currentExecution = true;
					break;
				case("emax") :
					node.getBattery().setEMax(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new energy value = " + sValue;
					currentExecution = true;
					break;
				case("esensing") :
					sensorNode.getSensorUnit().setESensing(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new consumption value = " + sValue;
					currentExecution = true;
					break;
				case("datarate") :
					node.setUartDataRate(Long.parseLong(sValue));
					rep = "000 Device: "+ sDevice + " has a new UART data rate value = " + sValue;
					currentExecution = true;
					break;
				case("drfit") :
					node.setSigmaOfDriftTime(Double.parseDouble(sValue));
					rep = "000 Device: "+ sDevice + " has a new drift time value = " + sValue;
					currentExecution = true;
					break;
				case("deg") :
					if (node.getType() == Device.MEDIA_SENSOR) {
						((MediaSensorNode) sensorNode).setSensorUnitDeg(Double.parseDouble(sValue));
						rep = "000 Device: "+ sDevice + " has a new drift time value = " + sValue;
						currentExecution = true;
					}
					break;
				case("dec") :
					if (node.getType() == Device.MEDIA_SENSOR) {
						((MediaSensorNode) sensorNode).setSensorUnitDec(Double.parseDouble(sValue));
						rep = "000 Device: "+ sDevice + " has a new drift time value = " + sValue;
						currentExecution = true;
					}
					break;
				default:
					rep = "[ERROR] Unknown paramerter or not accepted value ";
					currentExecution = false;
				}
				break;
			case("radio"):
				if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
					CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
				if (CupCarbon.cupCarbonController != null) {
					CupCarbon.cupCarbonController.updateObjectListView();
					CupCarbon.cupCarbonController.getNodeInformations();
					CupCarbon.cupCarbonController.getRadioInformations();
					CupCarbon.cupCarbonController.updateSelectionInListView();
				}
				sensorNode.setSelected(true);
				if (node.getType() != Device.GAS || node.getType() != Device.MOBILE || node.getType() != Device.MARKER) {
					switch (sParameter) {
					case("my") :
						sensorNode.getRadioModuleByName(sRadio).setMy(Integer.parseInt(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new MY value = " + sValue;
						currentExecution = true;
						break;
					case("ch") :
						sensorNode.getRadioModuleByName(sRadio).setCh(Integer.parseInt(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new CH value = " + sValue;
						currentExecution = true;
						break;
					case("nid") :
						sensorNode.getRadioModuleByName(sRadio).setNId(Integer.parseInt(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new NID value = " + sValue;
						currentExecution = true;
						break;
					case("radius") :
						sensorNode.getRadioModuleByName(sRadio).setRadioRangeRadius(Double.parseDouble(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new radius value = " + sValue;
						currentExecution = true;
						break;
					case("etx") :
						sensorNode.getRadioModuleByName(sRadio).setETx(Double.parseDouble(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new ETX value = " + sValue;
						currentExecution = true;
						break;
					case("erx") :
						sensorNode.getRadioModuleByName(sRadio).setERx(Double.parseDouble(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new ERX value = " + sValue;
						currentExecution = true;
						break;
					case("eslp") :
						sensorNode.getRadioModuleByName(sRadio).setESleep(Double.parseDouble(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new ESleeping value = " + sValue;
						currentExecution = true;
						break;
					case("elisten") :
						sensorNode.getRadioModuleByName(sRadio).setEListen(Double.parseDouble(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new EListening value = " + sValue;
						currentExecution = true;
						break;
					case("radiorate") :
						sensorNode.getRadioModuleByName(sRadio).setRadioDataRate(Integer.parseInt(sValue));
						rep = "000 Radio: "+ sRadio + " of device: " + sDevice + " has a new data rate value = " + sValue;
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
		return "SETPARAMETER";
	}
}
