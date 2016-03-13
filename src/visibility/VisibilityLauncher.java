package visibility;

import javax.swing.JOptionPane;

import cupcarbon.CupCarbon;
import device.DeviceList;
import device.SensorNode;
import map.MapLayer;

public class VisibilityLauncher extends Thread {

	@Override
	public void run() {
		int n = DeviceList.getSelectedSensorNodes().size();
		int i = 0;
		for(SensorNode sn : DeviceList.getSelectedSensorNodes()) {
			i++;
			CupCarbon.lblSimulation.setText(" | Visibility: "+((int)(i*1.0/n*100))+"%");
			VisibilityZones vz = new VisibilityZones(sn);
			vz.calculate();
			MapLayer.mapViewer.repaint();
		}
		JOptionPane.showMessageDialog(null, "Visibility Ok!", "Visibility", JOptionPane.INFORMATION_MESSAGE);
		System.out.println("Visibility Finished!");
	}
	
}
