package markers;

import java.util.ArrayList;

import geometry.DPoint;

public class NamedRoute {

	private String name = "";
	public ArrayList<DPoint> route;
	
	public NamedRoute(String name,  ArrayList<DPoint> route) {
		this.name = name ;
		this.route = route;
	}
	
	public String getName() {
		return name;
	}
	
	public  ArrayList<DPoint> getRoute() {
		return route;
	}
	
	public int size() {
		return route.size();
	}
	
}
