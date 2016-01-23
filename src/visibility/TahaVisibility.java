package visibility;

import java.util.LinkedList;

import javax.vecmath.Vector3d;

import device.DeviceList;
import device.SensorNode;
import geo_objects.Building;
import geo_objects.BuildingList;
import geo_objects.GeoZone;
import geo_objects.GeoZoneList;
import map.MapLayer;

public class TahaVisibility {

	
	
	public TahaVisibility() {
		
	}
	
	public void execute() throws CloneNotSupportedException {	
		calculateVisibility();
		MapLayer.mapViewer.repaint();
	}
	
//	public void calculateVisibility() throws CloneNotSupportedException {
//		
//		GeoZoneList geoZoneList = new GeoZoneList();
//		
//		SensorNode sn1 = DeviceList.getSensorNodes().get(0);
//		SensorNode sn2 = DeviceList.getSensorNodes().get(1);
//		
//		double r = 0.005;
//		//Marker marker1 = MarkerList.getMarkers().get(0);
//		//Marker marker2 = MarkerList.getMarkers().get(1);
//		double ymin = sn1.getLongitude()-r;//marker1.getLongitude();
//		double xmin = sn1.getLatitude()-r;//marker1.getLatitude();
//		double ymax = sn1.getLongitude()+r;//marker2.getLongitude();
//		double xmax = sn1.getLatitude()+r;//marker2.getLatitude();
//		
//		
//		
//		double xref = xmin;
//		double yref = ymin;
//		
//		double zm = 100000;
//		
//		sn1.setLongitude((sn1.getLongitude()-yref)*zm);
//		sn1.setLatitude((sn1.getLatitude()-xref)*zm);
//		sn2.setLongitude((sn2.getLongitude()-yref)*zm);
//		sn2.setLatitude((sn2.getLatitude()-xref)*zm);
//		
//		scene = new Scene(sn1, sn2);
//
//		scene.xmin = xmin-xref ;
//		scene.ymin = ymin-yref ;
//		scene.xmax = (xmax-xref) * zm ;
//		scene.ymax = (ymax-yref) * zm ;
//		
////		System.out.println(xmin-xref);
////		System.out.println(ymin-yref);
////		System.out.println((xmax-xref)*zm);
////		System.out.println((ymax-yref)*zm );
////		System.out.println(sn1.getLongitude());
////		System.out.println(sn1.getLatitude());
////		System.out.println(sn2.getLongitude());
////		System.out.println(sn2.getLatitude());		
//		
//		scene.groundHeight = 0;
//		
//		Brdf brdf = new Brdf() {
//
//			@Override
//			public void reflect(Vector3d in, Vector3d reflected, Vector3d normal, Vector3d tangent,
//					double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void transmit(Vector3d in, Vector3d refracted, Vector3d normal, Vector3d tangent,
//					double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		};
//		for(Building building : BuildingList.buildingList) {
//			Vector3d [] zone = building.toVector3d(xref, yref, zm);
//			Face face = new Face(brdf, zone);
//			scene.addFace(face);
//		}
//		Building sol = new Building(4);
//		sol.set(xmin-xref, ymin-yref, 0);
//		sol.set(xmin-xref, ymax-yref, 1);
//		sol.set(xmax-xref, ymax-yref, 2);
//		sol.set(xmax-xref, ymin-yref, 3);
//		
//		Face face = new Face(brdf, sol.toVector3d());
//		scene.addFace(face);
//		scene.allFaceAreSet();
//				
//		NbrInteractions nbr = new NbrInteractions(1,0,0);
//		Zone2d.Calcul_Zone2d(nbr, scene);
//		
//		sn1.setLongitude(sn1.getLongitude()/zm+yref);
//		sn1.setLatitude(sn1.getLatitude()/zm+xref);
//		sn2.setLongitude(sn2.getLongitude()/zm+yref);
//		sn2.setLatitude(sn2.getLatitude()/zm+xref);
//		
//		for(GeoZone geo : geoZoneList.getGeoZoneList()) {
//			geo.reduce(xref, yref, zm);
//		}
//		
//	}
	
	public void calculateVisibility() throws CloneNotSupportedException {
		for(SensorNode sn : DeviceList.getSensorNodes()) {
			GeoZoneList gzl = new GeoZoneList();
			
	//		SensorNode sn1 = DeviceList.getSensorNodes().get(0);
	//		SensorNode sn2 = DeviceList.getSensorNodes().get(1);
			
			double r = 0.002;
			double ymin = sn.getLongitude()-r;
			double xmin = sn.getLatitude()-r;
			double ymax = sn.getLongitude()+r;
			double xmax = sn.getLatitude()+r;
			
			double xref = xmin;
			double yref = ymin;
			
			double zm = 10000;
			
			sn.setLongitude((sn.getLongitude()-yref)*zm);
			sn.setLatitude((sn.getLatitude()-xref)*zm);
			
			Scene scene = new Scene(sn, null);
	
			scene.xmin = xmin-xref ;
			scene.ymin = ymin-yref ;
			scene.xmax = (xmax-xref) * zm ;
			scene.ymax = (ymax-yref) * zm ;
			
			scene.groundHeight = 0;
			
			Brdf brdf = new Brdf() {
				@Override
				public void reflect(Vector3d in, Vector3d reflected, Vector3d normal, Vector3d tangent,
						double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				}
				@Override
				public void transmit(Vector3d in, Vector3d refracted, Vector3d normal, Vector3d tangent,
						double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				}
				
			};
			for(Building building : BuildingList.buildingList) {
				Vector3d [] zone = building.toVector3d(xref, yref, zm);
				Face face = new Face(brdf, zone);
				scene.addFace(face);
			}
			Building sol = new Building(4);
			sol.set(xmin-xref, ymin-yref, 0);
			sol.set(xmin-xref, ymax-yref, 1);
			sol.set(xmax-xref, ymax-yref, 2);
			sol.set(xmax-xref, ymin-yref, 3);
			
			Face face = new Face(brdf, sol.toVector3d());
			scene.addFace(face);
			scene.allFaceAreSet();
					
			NbrInteractions nbr = new NbrInteractions(1,0,0);
			Zone2d z2d = new Zone2d();
			z2d.Calcul_Zone2d(nbr, scene);
			
			sn.setLongitude(sn.getLongitude()/zm+yref);
			sn.setLatitude(sn.getLatitude()/zm+xref);
			
			for(GeoZone geo : GeoZoneList.geoZoneList) {
				geo.reduce(xref, yref, zm);
			}
		
			LinkedList<GeoZone> geoZoneList = GeoZoneList.geoZoneList;
			
			sn.setGeoZoneList(geoZoneList);
		}
	}
	
	public static void calculateVisibility(SensorNode sn) throws CloneNotSupportedException {
		GeoZoneList gzl = new GeoZoneList();
				
		double r = 0.001;
		double ymin = sn.getLongitude()-r;
		double xmin = sn.getLatitude()-r;
		double ymax = sn.getLongitude()+r;
		double xmax = sn.getLatitude()+r;
		
		double xref = xmin;
		double yref = ymin;
		
		double zm = 10000;
		
		sn.setLongitude((sn.getLongitude()-yref)*zm);
		sn.setLatitude((sn.getLatitude()-xref)*zm);
		
		Scene scene = new Scene(sn, null);

		scene.xmin = xmin-xref ;
		scene.ymin = ymin-yref ;
		scene.xmax = (xmax-xref) * zm ;
		scene.ymax = (ymax-yref) * zm ;
		
		scene.groundHeight = 0;
		
		Brdf brdf = new Brdf() {
			@Override
			public void reflect(Vector3d in, Vector3d reflected, Vector3d normal, Vector3d tangent,
					double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
			}
			@Override
			public void transmit(Vector3d in, Vector3d refracted, Vector3d normal, Vector3d tangent,
					double[] wavelengths, Field[] result) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
			}
			
		};
		for(Building building : BuildingList.buildingList) {
			Vector3d [] zone = building.toVector3d(xref, yref, zm);
			Face face = new Face(brdf, zone);
			scene.addFace(face);
		}
		Building sol = new Building(4);
		sol.set(xmin-xref, ymin-yref, 0);
		sol.set(xmin-xref, ymax-yref, 1);
		sol.set(xmax-xref, ymax-yref, 2);
		sol.set(xmax-xref, ymin-yref, 3);
		
		Face face = new Face(brdf, sol.toVector3d());
		scene.addFace(face);
		scene.allFaceAreSet();
				
		NbrInteractions nbr = new NbrInteractions(1,1,0);
		Zone2d z2d = new Zone2d();
		z2d.Calcul_Zone2d(nbr, scene);
		
		sn.setLongitude(sn.getLongitude()/zm+yref);
		sn.setLatitude(sn.getLatitude()/zm+xref);
		
		for(GeoZone geo : GeoZoneList.geoZoneList) {
			geo.reduce(xref, yref, zm);
		}
	
		LinkedList<GeoZone> geoZoneList = GeoZoneList.geoZoneList;
		
		sn.setGeoZoneList(geoZoneList);
	}
	
}
