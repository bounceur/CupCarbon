/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.Serializable;
import javax.vecmath.Vector3d;

/**
 * Bounding box useful for building Spatial Structure
 * @author pcombeau
 */
public class BoundingBox implements Serializable {
    private static final long serialVersionUID = 0x66001003;
    
    final private Vector3d minimum;
    final private Vector3d maximum;
    final private Vector3d center;
    
    
    /**
     * Creates an empty box. The minimum point will have all components set to
     * positive infinity, and the maximum will have all components set to
     * negative infinity.
     */
    public BoundingBox() {
        minimum = new Vector3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        maximum = new Vector3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        center  = new Vector3d ();
    }

    /**
     * Creates a copy of the given box.
     * 
     * @param b bounding box to copy
     */
    public BoundingBox(BoundingBox b) {
        minimum = new Vector3d(b.minimum);
        maximum = new Vector3d(b.maximum);
        center  = new Vector3d(b.center);
    }

    /**
     * Creates a bounding box containing only the specified point.
     * 
     * @param p point to include
     */
    public BoundingBox(Vector3d p) {
        this(p.x, p.y, p.z);
    }

    /**
     * Creates a bounding box containing only the specified point.
     * 
     * @param x x coordinate of the point to include
     * @param y y coordinate of the point to include
     * @param z z coordinate of the point to include
     */
    public BoundingBox(double x, double y, double z) {
        minimum = new Vector3d(x, y, z);
        maximum = new Vector3d(x, y, z);
        center  = new Vector3d(x, y, z);
    }

    /**
     * Creates a bounding box centered around the origin.
     * 
     * @param size half edge length of the bounding box
     */
    public BoundingBox(double size) {
        minimum = new Vector3d(-size, -size, -size);
        maximum = new Vector3d(size, size, size);
        center  = new Vector3d();
    }

    /**
     * Gets the minimum corner of the box. That is the corner of smallest
     * coordinates on each axis. Note that the returned reference is not cloned
     * for efficiency purposes so care must be taken not to change the
     * coordinates of the point.
     * 
     * @return a reference to the minimum corner
     */
    public final Vector3d getMinimum() {
        return new Vector3d(minimum);
    }

    /**
     * Gets the maximum corner of the box. That is the corner of largest
     * coordinates on each axis. Note that the returned reference is not cloned
     * for efficiency purposes so care must be taken not to change the
     * coordinates of the point.
     * 
     * @return a reference to the maximum corner
     */
    public final Vector3d getMaximum() {
        return new Vector3d(maximum);
    }

    /**
     * Gets the center of the box, computed as (min + max) / 2.
     * 
     * @return a reference to the center of the box
     */
    public final Vector3d getCenter() {
        return new Vector3d(center);
    }

    /**
     * Gets a corner of the bounding box. The index scheme uses the binary
     * representation of the index to decide which corner to return. Corner 0 is
     * equivalent to the minimum and corner 7 is equivalent to the maximum.
     * 
     * @param i a corner index, from 0 to 7
     * @return the corresponding corner
     */
    public final Vector3d getCorner(int i) {
        double x = (i & 1) == 0 ? minimum.x : maximum.x;
        double y = (i & 2) == 0 ? minimum.y : maximum.y;
        double z = (i & 4) == 0 ? minimum.z : maximum.z;
        return new Vector3d(x, y, z);
    }

    /**
     * Gets a corner of the bounding box. The index scheme uses the binary
     * representation of the index to decide which corner to return. Corner 0 is
     * equivalent to the minimum and corner 7 is equivalent to the maximum.
     * 
     * @param i a corner index, from 0 to 7
     * @param P
     */
    public final void getCorner(int i, Vector3d P) {
        P.x = (i & 1) == 0 ? minimum.x : maximum.x;
        P.y = (i & 2) == 0 ? minimum.y : maximum.y;
        P.z = (i & 4) == 0 ? minimum.z : maximum.z;
    }

    /**
     * Gets a specific coordinate of the surface's bounding box.
     * 
     * @param i index of a side from 0 to 5
     * @return value of the request bounding box side
     */
    public final double getBound(int i) {
        switch (i) {
            case 0:
                return minimum.x;
            case 1:
                return maximum.x;
            case 2:
                return minimum.y;
            case 3:
                return maximum.y;
            case 4:
                return minimum.z;
            case 5:
                return maximum.z;
            default:
                return 0;
        }
    }

    /**
     * Gets the extents vector for the box. This vector is computed as (max -
     * min). Its coordinates are always positive and represent the dimensions of
     * the box along the three axes.
     * 
     * @return a reference to the extent vector
     */
    public final Vector3d getExtents() {
        Vector3d res = new Vector3d (maximum);
        res.sub (minimum);
        return res;
    }

    /**
     * Gets the surface area of the box.
     * 
     * @return surface area
     */
    public final double getArea() {
        Vector3d w = getExtents();
        double ax = Math.max(w.x, 0.D);
        double ay = Math.max(w.y, 0.D);
        double az = Math.max(w.z, 0.D);
        return 2.D * (ax * ay + ay * az + az * ax);
    }

    /**
     * Gets the box's volume
     * 
     * @return volume
     */
    public final double getVolume() {
        Vector3d w = getExtents();
        double ax = Math.max(w.x, 0);
        double ay = Math.max(w.y, 0);
        double az = Math.max(w.z, 0);
        return ax * ay * az;
    }

    /**
     * Enlarge the bounding box by the minimum possible amount to avoid numeric
     * precision related problems.
     */
    public final void enlargeUlps() {
        final double eps = 1e-6D;
        minimum.x -= Math.max(eps, Math.ulp(minimum.x));
        minimum.y -= Math.max(eps, Math.ulp(minimum.y));
        minimum.z -= Math.max(eps, Math.ulp(minimum.z));
        maximum.x += Math.max(eps, Math.ulp(maximum.x));
        maximum.y += Math.max(eps, Math.ulp(maximum.y));
        maximum.z += Math.max(eps, Math.ulp(maximum.z));
        makeCenter();
    }

    /**
     * Returns <code>true</code> when the box has just been initialized, and
     * is still empty. This method might also return true if the state of the
     * box becomes inconsistent and some component of the minimum corner is
     * larger than the corresponding coordinate of the maximum corner.
     * 
     * @return <code>true</code> if the box is empty, <code>false</code>
     *         otherwise
     */
    public final boolean isEmpty() {
        return (maximum.x < minimum.x) || (maximum.y < minimum.y) || (maximum.z < minimum.z);
    }

    /**
     * Returns <code>true</code> if the specified bounding box intersects this
     * one. The boxes are treated as volumes, so a box inside another will
     * return true. Returns <code>false</code> if the parameter is
     * <code>null</code>.
     * 
     * @param b box to be tested for intersection
     * @return <code>true</code> if the boxes overlap, <code>false</code>
     *         otherwise
     */
    public final boolean intersects(BoundingBox b) {
        return ((b != null) 
            && (minimum.x <= b.maximum.x) && (maximum.x >= b.minimum.x)
            && (minimum.y <= b.maximum.y) && (maximum.y >= b.minimum.y) 
            && (minimum.z <= b.maximum.z) && (maximum.z >= b.minimum.z));
    }

    /**
     * Checks to see if the specified point3d is
     * inside the volume defined by this box. Returns <code>false</code> if
     * the parameter is <code>null</code>.
     * 
     * @param p point to be tested for containment
     * @return <code>true</code> if the point is inside the box,
     *         <code>false</code> otherwise
     */
    public final boolean contains(Vector3d p) {
        return ((p != null) 
            && (p.x >= minimum.x) && (p.x <= maximum.x) 
            && (p.y >= minimum.y) && (p.y <= maximum.y) 
            && (p.z >= minimum.z) && (p.z <= maximum.z));
    }

    /**
     * Check to see if the specified point is inside the volume defined by this
     * box.
     * 
     * @param x x coordinate of the point to be tested
     * @param y y coordinate of the point to be tested
     * @param z z coordinate of the point to be tested
     * @return <code>true</code> if the point is inside the box,
     *         <code>false</code> otherwise
     */
    public final boolean contains(double x, double y, double z) {
        return ((x >= minimum.x) && (x <= maximum.x) 
            && (y >= minimum.y) && (y <= maximum.y) 
            && (z >= minimum.z) && (z <= maximum.z));
    }

    /**
     * Recomputes the bbox center.
     */
    private final void makeCenter () {
        center.sub (maximum, minimum);
        center.scale (0.5);
    }

    /**
     * Changes the extents of the box as needed to include the given
     * Point3d into this box. Does nothing if the
     * parameter is <code>null</code>.
     * 
     * @param p point to be included
     */
    public final void include(Vector3d p) {
        if (p != null) {
            if (p.x < minimum.x) {
                minimum.x = p.x;
            }
            if (p.x > maximum.x) {
                maximum.x = p.x;
            }
            if (p.y < minimum.y) {
                minimum.y = p.y;
            }
            if (p.y > maximum.y) {
                maximum.y = p.y;
            }
            if (p.z < minimum.z) {
                minimum.z = p.z;
            }
            if (p.z > maximum.z) {
                maximum.z = p.z;
            }
            makeCenter();
        }
    }

    /**
     * Changes the extents of the box as needed to include the given point into
     * this box.
     * 
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @param z z coordinate of the point
     */
    public final void include(double x, double y, double z) {
        if (x < minimum.x) {
            minimum.x = x;
        }
        if (x > maximum.x) {
            maximum.x = x;
        }
        if (y < minimum.y) {
            minimum.y = y;
        }
        if (y > maximum.y) {
            maximum.y = y;
        }
        if (z < minimum.z) {
            minimum.z = z;
        }
        if (z > maximum.z) {
            maximum.z = z;
        }
        makeCenter();
    }

    /**
     * Changes the extents of the box as needed to include the given box into
     * this box. Does nothing if the parameter is <code>null</code>.
     * 
     * @param b box to be included
     */
    public final void include(BoundingBox b) {
        if (b != null) {
            if (b.minimum.x < minimum.x) {
                minimum.x = b.minimum.x;
            }
            if (b.maximum.x > maximum.x) {
                maximum.x = b.maximum.x;
            }
            if (b.minimum.y < minimum.y) {
                minimum.y = b.minimum.y;
            }
            if (b.maximum.y > maximum.y) {
                maximum.y = b.maximum.y;
            }
            if (b.minimum.z < minimum.z) {
                minimum.z = b.minimum.z;
            }
            if (b.maximum.z > maximum.z) {
                maximum.z = b.maximum.z;
            }
            makeCenter();
        }
    }

    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)", 
                             minimum.x, minimum.y, minimum.z, 
                             maximum.x, maximum.y, maximum.z);
    }
}
