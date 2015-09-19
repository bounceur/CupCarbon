/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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
 *----------------------------------------------------------------------------------------------------------------*/

package utilities;

import java.awt.Point;
import java.awt.geom.Point2D;

import map.Layer;

import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * @author Ahcene Bounceur
 * 
 */
public class MapCalc {

	/**
	 * This function returns the coordinates of the pixel of the map that
	 * corresponds to a given GPS coordinates (x, y). Note that this pixel is
	 * different to the one of the JFram/JPanel/... For each zoom, we have a map
	 * that is represented by a pixels The size of this map (in pixels) depends
	 * on the chosen zoom.
	 * 
	 * @param x
	 *            The longitude
	 * @param y
	 *            The latitude
	 * @return The pixel coordinates (Point2D) corresponding to the GPS
	 *         coordinates (x, y)
	 */
	public static Point2D geoXYToPixelMap(double x, double y) {
		return Layer
				.getMapViewer()
				.getTileFactory()
				.geoToPixel(new GeoPosition(x, y),
						Layer.getMapViewer().getZoom());
	}

	// public static GeoPosition geoXYToPixel(int x, int y) {
	// return Layer.getMapViewer().getTileFactory()
	// .pixelToGeo(new Point(x, y), Layer.getMapViewer().getZoom());
	// }

	/**
	 * Calculate the pixel of the map from a given pixel of the window (JFrame,
	 * JPanel, etc.)
	 * 
	 * @param x
	 *            The x coordinate of the pixel of the window
	 * @param y
	 *            The y coordinate of the pixel of the window
	 * @return The pixel of the map from a given pixel of the window
	 */
	public static Point2D pixelPanelToPixelMap(int x, int y) {
		return Layer
				.getMapViewer()
				.getTileFactory()
				.geoToPixel(
						Layer.getMapViewer().convertPointToGeoPosition(
								new Point(x, y)),
						Layer.getMapViewer().getZoom());
	}

	// public static int geoToIntPixelMapX(double x, double y) {
	// return (int) geoXYToPixelMap(x, y).getX();
	// }

	// public static int geoToIntPixelMapY(double x, double y) {
	// return (int) geoXYToPixelMap(x, y).getY();
	// }

	// public static Point2D geoToPixelMap(GeoPosition gp) {
	// return Layer.getMapViewer().getTileFactory()
	// .geoToPixel(gp, Layer.getMapViewer().getZoom());
	// }

	/**
	 * This function returns the integer coordinates of the pixel of the map
	 * that corresponds to a given GPS coordinates (x, y).
	 * 
	 * @param x
	 *            The longitude
	 * @param y
	 *            The latitude
	 * @return the integer coordinates of the pixel of the map that corresponds
	 *         to a given GPS coordinates (x, y).
	 */
	public static int[] geoToIntPixelMapXY(double x, double y) {
		Point2D p = geoXYToPixelMap(x, y);
		int[] v = { (int) p.getX(), (int) p.getY() };
		return v;
	}

	/**
	 * Calculate the distance in pixels between two GPS coordinates.
	 * 
	 * @param x1
	 *            Longitude of the first point
	 * @param y1
	 *            Latitude of the first point
	 * @param x2
	 *            Longitude of the second point
	 * @param y2
	 *            Latitude of the second point
	 * @return The distance in meters between two GPS coordinates.
	 */
	public static double distanceEnPixels(double x1, double y1, double x2,
			double y2) {
		Point2D p1 = geoXYToPixelMap(x1, y1);
		Point2D p2 = geoXYToPixelMap(x2, y2);
		return p1.distance(p2);
	}

	/**
	 * Calculate the distance in meters between two GPS coordinates.
	 * 
	 * @param x1
	 *            Longitude of the first point
	 * @param y1
	 *            Latitude of the first point
	 * @param x2
	 *            Longitude of the second point
	 * @param y2
	 *            Latitude of the second point
	 * @return The distance in meters between two GPS coordinates.
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		double earth_radius = 6378137; // Earth sphere radius: 6378km
		double rla1 = Math.toRadians(x1);
		double rlo1 = Math.toRadians(y1);
		double rla2 = Math.toRadians(x2);
		double rlo2 = Math.toRadians(y2);

		double dla = (rla2 - rla1) / 2.0;
		double dlo = (rlo2 - rlo1) / 2.0;

		double a = (Math.sin(dla) * Math.sin(dla)) + Math.cos(rla1)
				* Math.cos(rla2) * (Math.sin(dlo) * Math.sin(dlo));
		double d = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (earth_radius * d);
	}

	/**
	 * Calculate the number of pixels that corresponds to a given radius
	 * 
	 * @param radius
	 * @return The number of pixels that corresponds to a given radius
	 */
	public static int radiusInPixels(double radius) {
		GeoPosition p1 = new GeoPosition(36.000000, 40.000000);
		Point2D pt1 = Layer.getMapViewer().getTileFactory().geoToPixel(p1, Layer.getMapViewer().getZoom());
		int v1 = (int) pt1.getX();
		GeoPosition p2 = new GeoPosition(36.000000, 40.0011125);
		Point2D pt2 = Layer.getMapViewer().getTileFactory().geoToPixel(p2, Layer.getMapViewer().getZoom());
		int v2 = (int) pt2.getX();
		int rp = (int) (radius * (v2 - v1) / 100);
		return rp;
	}
}
