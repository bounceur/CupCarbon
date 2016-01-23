/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.PrintStream;
import java.io.Serializable;
import javax.vecmath.Vector3d;

/**
 * An instance of the class <code>Edge</code> represents a face edge, able to 
 * produce diffraction.
 * @author pcombeau
 */

final public class Edge implements Comparable<Edge>, Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * This is the first edge point, the beginning of it
     */
    final Vector3d A; /// first extremal point of this edge instance
    /**
     * This is the second edge point, in others words its end
     */
    final Vector3d B; /// second extremal point of this edge instance
    /**
     * direction of this edge instance
     */
    final Vector3d u; /// unit vector of this edge instance
    /**
     * normal vector to the face that produces this edge
     */
    final Vector3d n; /// normal vector of this edge instance
    /**
     * outgoing parallel vector to the face that produces this edge
     */
    final Vector3d p; /// tangent vector of this edge instance
    /**
     * electromagnetic properties of the face that produces this edge
     */
    //final double epsilon;
    /**
     * electromagnetic properties of the face that produces this edge
     */
    //final double sigma;
    final Face face;
    
    // For use into a Diedre
    /**
     * Position of the point A on the dihedron that contains this edge. Only one 
     * dihedron can contains an edge !
     */
    double tA;
    /**
     * Position of the point B on the dihedron that contains this edge. Only one 
     * dihedron can contains an edge !
     */
    double tB;
    /**
     * This denotes the angle between the face of the first edge in the same dihedron
     * and this edge instance face ...
     */
    double angle;

    /**
     * Accessor: returns the second point defining this edge instance
     * @return the second edge point
     */
    public double getTB() {
        return tB;
    }

    /**
     * Accessor: returns the first point defining this edge instance
     * @return the first edge point
     */
    public double getTA() {
        return tA;
    }

    /**
     * Creates a new instance of Edge
     * @param A First point on the edge
     * @param B Second point on the edge
     * @param f Face instance that produce this edge
     */
    public Edge(Vector3d A, Vector3d B, Face f) {
        this.A = new Vector3d (A);
        this.B = new Vector3d (B);
        face = f;
        n = f.getNormal();
        u = new Vector3d (B);
        u.sub (A);
        u.normalize();
        p = new Vector3d ();
        p.cross (u, n); // p is outward the face f !!
    }

    /**
     * Comparison of two edges ...
     * @param e Another instance of Edge
     * @return <code>0</code> if the two edges are similar, another value in the others cases
     */
    @Override
    public int compareTo (Edge e) {
        return angle<e.angle ? -1 : angle>e.angle ? +1 : 0;
    }
    
    /**
     * Returns the first point that define this edge instance
     * @return the first edge point
     */
    public Vector3d getA() {
        return A;
    }

    /**
     * Returns the first point that define this edge instance
     * @return the first edge point
     */
    public Face getFace() {
        return face;
    }

    /**
     * Returns the second point that define this edge instance
     * @return The second point of this edge
     */
    public Vector3d getB() {
        return B;
    }

    /**
     * Accessor: returns the direction of this edge, with an orientation given by the
     * two points that define it.
     * @return edge direction 
     */
    public Vector3d getU() {
        return u;
    }

    /**
     * Accessor: returns the normal to the face that produces this edge
     * @return Normal to the face that produces this edge
     */
    public Vector3d getN() {
        return n;
    }

    /**
     * Accessor: returns the outgoing parallel to the face that produces this edge, 
     * perpendicular to the edge !
     * Notice that the three vectors U, N and P form a direct repere
     * @return outgoing parallel
     */
    public Vector3d getP() {
        return p;
    }

    /**
     * Print some info on a given PrintStream
     * @param ps PrintStream where to print info
     */
    public final void print(final PrintStream ps) {
        ps.print (toString());
    }
    
    
    /**
     * Print some info on a given PrintStream
     * @param ps PrintStream where to print info
     */
    public final void println(final PrintStream ps) {
        ps.println (toString());
    }

    /**
     * Give some information about this Edge instance
     * @return a String containing somes informations
     */
    @Override
    public String toString() {
        return tA+" to "+tB+", p="+p;
    }
}
