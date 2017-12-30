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
 * This class allows dealing with the simulation part of CupCarbon
 *  
 * Command examples:
 * simulation run
 * -> It runs the simulation with the defined simulation parameters
 * simulation stop
 * -> It stops the sumulation
 * simulation set time value
 * simulation set speed value
 * simulation set aspeed value
 * simulation set symlinks value
 * simulation set visibility value
 * simulation set results value
 * simulation set log value
 * simulation set drift value
 * simulation set ack value
 * simulation set ackshow value
 * simulation set interference value
 * simulation set probability value
 * -> It assigns the defined value to the defined simulation parameter
 * simulation get time x
 * simulation get speed x
 * simulation get aspeed x
 * simulation get symlinks x
 * simulation get visibility x
 * simulation get results x
 * simulation get log x
 * simulation get drift x
 * simulation get ack x
 * simulation get ackshow x
 * simulation get interference x
 * simulation get probability x
 * -> It obtains the value of the defined simulation parameter
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

import cupcarbon.CupCarbon;
import javafx.application.Platform;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import simulation.SimulationInputs;
import simulation.WisenSimulation;


public class CupCommand_SIMULATION extends CupCommand {

	private WisenSimulation wisenSimulation;
	
	protected String option = "";
	protected String parameter = "";
	protected String value = "";
	
	protected String sOption = "";
	protected String sParameter = "";
	protected String sValue = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_SIMULATION(CupScript script,  String option, String parameter, String value) {
		this.script = script ;
		this.option = option;
		this.parameter = parameter;
		this.value = value;		
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_SIMULATION(CupScript script,  String option) {
		this.script = script ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute simulation
	// It either runs the simulator
	// or stops running the simulator
	// or assigns the defined value to the defined simulation parameter
	// or obtains the value of the defined simulation parameter
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		wisenSimulation = new WisenSimulation();
		currentExecution = false;
		String rep = "";
		sOption = script.getVariableValue(option);
		sParameter = script.getVariableValue(parameter);
		sValue = script.getVariableValue(value);
		if (isExecuted) {
			switch (sOption) {
			case("run") :
				if(wisenSimulation.ready()) {					
					Thread th = new Thread(wisenSimulation);
					th.start();
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("SenScript");
					alert.setHeaderText(null);
					alert.setContentText("Sensors without Script!");
					alert.showAndWait();
				}
				currentExecution = true;
				break;
			case("stop") :
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						CupCarbon.cupCarbonController.runSimulationButton.setDisable(false);
						CupCarbon.cupCarbonController.qRunSimulationButton.setDisable(false);
						wisenSimulation.stopSimulation();
						CupCarbon.cupCarbonController.mapFocus();
					}
				});
				currentExecution = true;
				break;
			case("set") :
				switch (sParameter) {
				case("time") :
					SimulationInputs.simulationTime = Double.parseDouble((sValue));
					rep = "000 The Simulation Time is set to " + sValue;
					currentExecution = true;
					CupCarbon.cupCarbonController.simulationTimeTextField.setText(sValue);
					break;
				case("speed") :
					SimulationInputs.visualDelay = Integer.parseInt(sValue);
					rep = "000 The Simulation Speed is set to " + sValue;
					currentExecution = true;
					CupCarbon.cupCarbonController.simulationSpeedTextField.setText(sValue);
					break;
				case("aspeed") :
					SimulationInputs.arrowsDelay = Integer.parseInt(sValue);
					rep = "000 The Simulation Arrow Speed is set to " + sValue;
					currentExecution = true;
					CupCarbon.cupCarbonController.arrowSpeedTextField.setText(sValue);
					break;
				case("symlinks") :
					SimulationInputs.symmetricalLinks = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Symmetrical Link is " + (Boolean.parseBoolean(sValue)? "Selected": "Not Selected");
					currentExecution = true;
					CupCarbon.cupCarbonController.symmetricalLinkCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("visibility") :
					SimulationInputs.visibility = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Visibility is " + (Boolean.parseBoolean(sValue)? "Selected": "Not Selected");
					currentExecution = true;
					CupCarbon.cupCarbonController.visibilityCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("results") :
					SimulationInputs.displayResults = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Results is " + (Boolean.parseBoolean(sValue)? "is Displayed": "is Not Displayed");
					currentExecution = true;
					CupCarbon.cupCarbonController.resultsCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("log") :
					SimulationInputs.displayLog = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Log " + (Boolean.parseBoolean(sValue)? "is Displayed": "is Not Displayed");
					currentExecution = true;
					CupCarbon.cupCarbonController.logCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("mobility") :
					SimulationInputs.mobilityAndEvents = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Mobility / Events " + (Boolean.parseBoolean(sValue)? "is Displayed": "is Not Displayed");
					currentExecution = true;
					CupCarbon.cupCarbonController.mobilityCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("drift") :
					SimulationInputs.clockDrift = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation Clock Drift " + (Boolean.parseBoolean(sValue)? "is Displayed": "is Not Displayed");
					currentExecution = true;
					CupCarbon.cupCarbonController.clockDriftCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("ack") :
					SimulationInputs.ack = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation ACK " + (Boolean.parseBoolean(sValue)? "is Displayed": "is Not Displayed");
					currentExecution = true;
					CupCarbon.cupCarbonController.ackCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("ackshow") :
					SimulationInputs.showAckLinks = Boolean.parseBoolean(sValue);
					rep = "000 The Simulation ACK Links " + (Boolean.parseBoolean(sValue)? "is Shown": "is Not Shown");
					currentExecution = true;
					CupCarbon.cupCarbonController.ackShowCheckBox.setSelected(Boolean.parseBoolean(sValue));
					break;
				case("interference") :
					SimulationInputs.ackType = Double.parseDouble((sValue));
					rep = "000 The Simulation ACK Type is Selected";
					currentExecution = true;
					CupCarbon.cupCarbonController.probaComboBox.getSelectionModel().select((int) Double.parseDouble(sValue));
					break;
				case("probability") :
					SimulationInputs.ackProba = Double.parseDouble(sValue);
					rep = "000 The Simulation Probability is set to " + sValue;
					currentExecution = true;
					CupCarbon.cupCarbonController.probaTextField.setText(sValue);
					break;
				default:
					rep = "[ERROR] Unknown paramerter or not accepted value ";
					currentExecution = false;
				}
				if(!CupCarbon.cupCarbonController.acc1.isExpanded())
				CupCarbon.cupCarbonController.acc1.setExpanded(true);
				break;
			case("get"):
				switch (sParameter) {
				case("time") :
					script.addVariable(sValue, "" + SimulationInputs.simulationTime);
					rep = "000 The Simulation Time is " + sValue;
					currentExecution = true;
					break;
				case("speed") :
					script.addVariable(sValue, "" + SimulationInputs.visualDelay);
					rep = "000 The Simulation Speed is " + sValue;
					currentExecution = true;
					break;
				case("aspeed") :
					script.addVariable(sValue, "" + SimulationInputs.arrowsDelay);
					rep = "000 The Simulation Arrow Speed is " + sValue;
					currentExecution = true;
					break;
				case("symlinks") :
					script.addVariable(sValue, "" + SimulationInputs.symmetricalLinks);
					rep = "000 The Simulation Symmetrical Link is " + sValue;
					currentExecution = true;
					break;
				case("visibility") :
					script.addVariable(sValue, "" + SimulationInputs.visibility);
					rep = "000 The Simulation Visibility is " + sValue;
					currentExecution = true;
					break;
				case("results") :
					script.addVariable(sValue, "" + SimulationInputs.displayResults);
					rep = "000 The Simulation Results is " + sValue;
					currentExecution = true;
					break;
				case("log") :
					script.addVariable(sValue, "" + SimulationInputs.displayLog);
					rep = "000 The Simulation Log is " + sValue;
					currentExecution = true;
					break;
				case("mobility") :
					script.addVariable(sValue, "" + SimulationInputs.mobilityAndEvents);
					rep = "000 The Simulation Mobility / Events is " + sValue;
					currentExecution = true;
					break;
				case("drift") :
					script.addVariable(sValue, "" + SimulationInputs.clockDrift);
					rep = "000 The Simulation Clock Drift is " + sValue;
					currentExecution = true;
					break;
				case("ack") :
					script.addVariable(sValue, "" + SimulationInputs.ack);
					rep = "000 The Simulation ACK is " + sValue;
					currentExecution = true;
					break;
				case("ackshow") :
					script.addVariable(sValue, "" + SimulationInputs.showAckLinks);
					rep = "000 The Simulation ACK Links is " + sValue;
					currentExecution = true;
					break;
				case("interference") :
					script.addVariable(sValue, "" + SimulationInputs.ackType);
					rep = "000 The Simulation ACK Type is " + sValue;
					currentExecution = true;
					break;
				case("probability") :
					script.addVariable(sValue, "" + SimulationInputs.ackProba);
					rep = "000 The Simulation Probability is " + sValue;
					currentExecution = true;
					break;
				default:
					rep = "[ERROR] Unknown paramerter or not accepted value ";
					currentExecution = false;
				}
				break;
			default:
				rep = "[ERROR] Unknown option "+sOption;
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
		return "SIMULATION";
	}
}
