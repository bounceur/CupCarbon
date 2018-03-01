package cupcarbon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import device.DeviceList;
import device.SensorNode;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import project.Project;

public class EnergyDrawController {

	@FXML
	public LineChart<String, Double> lineChart;
	
	@FXML
	public void battery() {
		lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);
        
		int idx = 1;
		double n = 0;
		double cv = 0;
		double sum = 0;
		for(SensorNode sensor : DeviceList.sensors) {
			if(sensor.isSelected()) {
				n++;
		        Series<String, Double> series = new Series<String, Double>();
		        series.setName(sensor.getName());
		        
		        String fileName = Project.getProjectResultPath()+File.separator+"wisen_simulation.csv";
				String [] t ;
				try {
					FileReader fr = new FileReader(fileName);
					BufferedReader br = new BufferedReader(fr);
					br.readLine();
					String s = "";
					while((s=br.readLine()) != null) {
						t=s.split(";");
						String sx = String.format("%2.2f" , Double.parseDouble(t[0])).replaceAll(",", ".");
						String sy = String.format("%2.2f" , Double.parseDouble(t[idx])) ;
						double y = Double.parseDouble(sy.replaceAll(",", "."));
						cv = y;
						Data<String, Double> data = new Data<String, Double>(sx, y); 
						series.getData().add(data);
					}
					br.close();
					sum += cv;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				lineChart.getData().add(series);
			}
			idx++;
		}
		System.out.println(sum/n);
	}
	
	@FXML
	public void consumption() {
		lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);        
		int idx = 1;
		for(SensorNode sensor : DeviceList.sensors) {
			if(sensor.isSelected()) {				
		        Series<String, Double> series = new Series<String, Double>();
		        series.setName(sensor.getName());
		        series.getData().removeAll(series.getData());
		        String fileName = Project.getProjectResultPath()+File.separator+"wisen_simulation.csv";
				String [] t ;
				try {
					FileReader fr = new FileReader(fileName);
					BufferedReader br = new BufferedReader(fr);
					br.readLine();
					String s = "";
					s=br.readLine();
					t=s.split(";");
					String sx = String.format("%2.2f" , Double.parseDouble(t[0])).replaceAll(",", ".");
					String sy = String.format("%2.2f" , Double.parseDouble(t[idx])) ;
					double y0 = Double.parseDouble(sy.replaceAll(",", "."));
					while((s=br.readLine()) != null) {
						t=s.split(";");
						sx = String.format("%2.2f" , Double.parseDouble(t[0])).replaceAll(",", ".");
						sy = String.format("%2.2f" , Double.parseDouble(t[idx])) ;
						double y = Double.parseDouble(sy.replaceAll(",", "."));
						Data<String, Double> data = new Data<String, Double>(sx, y0-y);
						y0=y;
						series.getData().add(data);
					}
					br.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				lineChart.getData().add(series);
			}
			idx++;
		}
	}
	
	@FXML
	public void batteryTotal() {
		//TODO
	}
	
	@FXML
	public void batteryAverage() {
		//TODO
	}
	
}
