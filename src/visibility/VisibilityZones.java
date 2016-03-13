package visibility;

import javax.vecmath.Vector3d;

import device.SensorNode;
import geo_objects.Building;
import geo_objects.BuildingList;
import geo_objects.GeoZoneList;
import map.MapLayer;

public class VisibilityZones {

	private SensorNode sn ;
	
	public VisibilityZones(SensorNode sn) {
		this.sn = sn;
	}	

	public void calculate() {
		sn.setSelection(false);
		GeoZoneList geoZoneList = new GeoZoneList();
				
		double r = 0.001;
		double ymin = sn.getLongitude()-1.5*r;
		double xmin = sn.getLatitude()-r;
		double ymax = sn.getLongitude()+1.5*r;
		double xmax = sn.getLatitude()+r;
		
		double xref = xmin;
		double yref = ymin;
		
		double zm = 74000; // = 1 meter
		
		//sn.setLongitude((sn.getLongitude()-yref)*zm);
		//sn.setLatitude((sn.getLatitude()-xref)*zm);
		
		double [] position = new double [3];
		position[0] = (sn.getLatitude()-xref)*zm;
		position[1] = (sn.getLongitude()-yref)*zm;
		position[2] = sn.getElevation();
		
		Scene scene = new Scene(position);

		scene.xmin = xmin-xref ;
		scene.ymin = ymin-yref ;
		scene.xmax = (xmax-xref) * zm ; //(xmax-xref) * zm ;
		scene.ymax = (ymax-yref) * zm ; //(ymax-yref) * zm ;
		
		scene.groundHeight = 0;

		Building globalZone = new Building(4);
		globalZone.set(ymin, xmin, 0);
		globalZone.set(ymin, xmax, 1);
		globalZone.set(ymax, xmax, 2);
		globalZone.set(ymax, xmin, 3);
		
		Face face = new Face(null, globalZone.toVector3d());
		scene.addFace(face);
		
		for(Building building : BuildingList.buildingList) {
			if(globalZone.intersect(building)) {
				Vector3d [] zone = building.toVector3d(xref, yref, zm);
				face = new Face(null, zone);
				scene.addFace(face);
			}
		}
		
		scene.allFaceAreSet();		
				
		NbrInteractions nbr = new NbrInteractions(1,0,0);
		
		Zone2d z2d = new Zone2d();
		z2d.Calcul_Zone2d(nbr, scene, geoZoneList);
		
		//sn.setLongitude(sn.getLongitude()/zm+yref);
		//sn.setLatitude(sn.getLatitude()/zm+xref);
		
		geoZoneList.reduce(xref, yref, zm);

		geoZoneList.latitude = sn.getLatitude();
		geoZoneList.longitude = sn.getLongitude();
		
		geoZoneList.toOneGeoZone();
		
		sn.setGeoZoneList(geoZoneList);
		MapLayer.mapViewer.repaint();
	}

}
