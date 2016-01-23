/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.Serializable;
import javax.vecmath.Vector3d;

/**
 *
 * @author pcombeau
 */
public class Triangle implements Serializable {
    private static final long serialVersionUID = 0x66001015;
    private final Vector3d A;
    private final Vector3d B;
    private final Vector3d C;
    private final Face     F;

    private final BoundingBox bbox;

    private final Vector3d AB;
    private final Vector3d AC;
    
    /** Creates a new instance of Triangle
     * @param f
     * @param a 
     * @param b
     * @param c 
     */
    public Triangle(final Face f, final Vector3d a, 
                    final Vector3d b, final Vector3d c) {
        F = f;
        A = new Vector3d(a);
        B = new Vector3d(b);
        C = new Vector3d(c);
        Vector3d v=new Vector3d ();
        v.sub(B, A);
        AB = v;
        v=new Vector3d ();
        v.sub(C, A);
        AC = v;
        bbox = new BoundingBox (A);
        bbox.include(B);
        bbox.include(C);
    }
    
    /**
     * 
     * @param ray
     * @return
     */
//    public final double intersection(Ray ray) {
//        /* begin calculating determinant - also used to calculate U parameter */
//        Vector3d pvec = new Vector3d();
//        pvec.cross(ray.direction, AC);
//        
//        /* if determinant is near zero, ray lies in plane of triangle */
//        double det = AB.dot (pvec);
//        if (det > -Face.DISTANCE_MIN && det < Face.DISTANCE_MIN) { return Double.MAX_VALUE; }
//        det = 1.0D / det;
//        
//        /* calculate distance from vert0 to ray origin */
//        Vector3d tvec = new Vector3d();
//        tvec.sub(ray.from, A);
//        
//        /* calculate U parameter and test bounds */
//        final double u = tvec.dot (pvec) * det;
//        if (u < 0.D || u > 1.D) { return Double.MAX_VALUE; }
//        
//        /* prepare to test V parameter */
//        pvec.cross(tvec, AB);
//        
//        /* calculate V parameter and test bounds */
//        final double v = ray.direction.dot (pvec) * det;
//        if (v < 0.0D || u + v > 1.0D) {
//            return Double.MAX_VALUE;
//        }
//        
//        /* calculate t, ray intersects triangle */
//        final double t = AC.dot(pvec) * det;
//        //System.err.println ("det = "+det+", u="+u+", v="+v+", t="+t+", dist="+ray.distance);
//        
//        return t>Face.DISTANCE_MIN ? t : Double.MAX_VALUE;
//    }

    
    /**
     * 
     * @param pt
     * @return
     */
    public final boolean inside(final Vector3d pt) {
        final Vector3d AP = new Vector3d (pt);
        AP.sub(A);
        final Vector3d abc = new Vector3d();
        abc.cross (AB,AC);
        final Vector3d cross = new Vector3d();
        cross.cross(AB, AP);
        final double inv_abcl = 1.D / abc.length();
        final double u = cross.length() * inv_abcl;
        if (u<0.D ||u>1.D) {return false;}
        cross.cross(AP, AC);
        final double v = cross.length() * inv_abcl;
        return u+v<=1.D && v>=0.D;
    }
    
    /**
     * 
     * @return
     */
    public final BoundingBox getBBox() {
        return bbox;
    }
    
    
    public final Face getFaceSupport () {
        return F;
    }

    public Vector3d getA() {
        return new Vector3d(A);
    }

    public Vector3d getB() {
        return new Vector3d(B);
    }

    public Vector3d getC() {
        return new Vector3d(C);
    }
}
