/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
/**
 *
 * @author pcombeau
 */
final public class Dihedron implements Serializable {
    private static final long serialVersionUID = 0x66001004;
    
    /**
     * Utility for storing the edges and find quickly those who can
     * be implied into diffraction at a given point.
     */
    public interface BinaryTree {
        /**
         * Print to a given <code>PrintStream</code>
         * @param ps
         */
        public void print(PrintStream ps) ;
    }

    /**
     * Internal node of a binary tree. This node gets a split value <code>t</code>,
     * and two children (a left, a right one).
     */
    final public class InternalNode implements BinaryTree, Serializable {
        private static final long serialVersionUID = 0x66031004;
        BinaryTree g, d;  
        final double min, max;
        final double t;
        InternalNode (double t, double min, double max) { 
            this.t = t; 
            this.min = min; 
            this.max = max; 
        }

        /**
         * Print this node to a given <code>PrintStream</code>
         * @param ps where to print
         */
        public void print(PrintStream ps) {
            ps.println ("InternalNode : from "+min+" to "+max+" with split="+t);
            g.print (ps);
            d.print (ps);
        }
    }
    /**
     * A binary tree leaf, containing somes edges between a given interval.
     */
    final public class Leaf implements BinaryTree, Serializable {
        private static final long serialVersionUID = 0x66021004;
        final Edge[] edges;
        final double min, max;
        Leaf (Edge[] es, double min, double max) {
            this.min = min; 
            this.max = max;
            edges = es.clone();
            // Sort the edges wrt their angle
            java.util.Arrays.sort (edges);
        }

        /**
         * Print this node to a given <code>PrintStream</code>
         * @param ps where to print
         */
        public void print(PrintStream ps) {            
            ps.println ("Leaf : from "+min+" to "+max+". Has "+edges.length+" edges");
        }
    }
    
    /**
     * An empty binary tree leaf. No value at all, only informative.
     */
    final public class EmptyLeaf implements BinaryTree, Serializable {
        private static final long serialVersionUID = 0x66011004;
        /**
         * Print this node to a given <code>PrintStream</code>
         * @param ps where to print
         */
        public void print(PrintStream ps) {
            ps.println ("EmptyLeaf");
        }
    }
    
    /// Implementation of the class Dihedron
    static final double PI2 = Math.PI*2.D;
    
    final Vector3d D;
    final public Vector3d getD () { return D; }

    final Vector3d u;
    final public Vector3d getU () { return u; }

    final Vector3d normale;
    BinaryTree btree;
    
    private Vector3d vector;
    
    private ArrayList<Edge> edges;
    private boolean tabOk;
    private Edge[] tabEdges;
    
    private final class MinMax {
        final double min;
        final double max;
        MinMax (final double min, final double max) { 
            this.min = min; 
            this.max = max; 
        }
    }
    final private MinMax computeMinMax (Edge[] edges) {
        double lmin, lmax;
        lmin = Math.min(edges[0].tA,edges[0].tB);
        lmax = lmin;
        for (int i=edges.length; --i>=0; ) {
            final double tA = edges[i].tA;
            final double tB = edges[i].tB;
            if (tA<=tB) {
                lmin = lmin > tA ? tA : lmin;
                lmax = lmax < tB ? tB : lmax;
            } else {
                lmin = lmin > tB ? tB : lmin;
                lmax = lmax < tA ? tA : lmax;
            }
        }
        return new MinMax (lmin, lmax);
    }
    
    /// Computes a binary tree, recursively
    final private BinaryTree computeBinaryTree (Edge[] edges) {
        if (edges == null || edges.length==0) {
            return new EmptyLeaf();
        }
        MinMax mm = computeMinMax(edges);
        return computeBinaryTree(edges, mm.min, mm.max);
    }
    
    final private BinaryTree computeBinaryTree (Edge[] edges, final Double rmin, final Double rmax) {
        // empty at left or at right ??
        final MinMax mm=computeMinMax(edges);
        final Double min = mm.min;
        final Double max = mm.max;
        if (min > rmin) {
            InternalNode res = new InternalNode(min, rmin, rmax);
            res.g = new EmptyLeaf();
            res.d = computeBinaryTree(edges, min, rmax);
            return res;
        }
        if (max < rmax) {
            InternalNode res = new InternalNode(max, rmin, rmax);
            res.g = computeBinaryTree(edges, min, max);
            res.d = new EmptyLeaf();
            return res;
        }
        
        // No, then split at the first solution !
        for (int i=0; i<edges.length; i++) {
            // is "i" a solution ??
            final Edge e = edges[i];
            if ((e.tA == rmax && e.tB == rmin) || (e.tA==rmin && e.tB==rmax)) { 
                continue; 
            }
            // yes ! can we split using tA ?
            double split = e.tA;
            int nbLeft = 0;
            int nbRight = 0;
            if (split > rmin && split < rmax) {
                for (int j=0; j<edges.length; j++) {
                    Edge ee = edges[j];
                    if (ee.tA < split || ee.tB < split) { nbLeft ++; }
                    if (ee.tA > split || ee.tB > split) { nbRight ++; }
                }
            }
            if (nbLeft == 0 ||nbRight == 0) {
                // No ... can we split using tB ?
                split = e.tB;
                nbLeft = 0;
                nbRight = 0;
                if (split > rmin && split < rmax) {
                    for (int j=0; j<edges.length; j++) {
                        Edge ee = edges[j];
                        if (ee.tA < split || ee.tB < split) { nbLeft ++; }
                        if (ee.tA > split || ee.tB > split) { nbRight ++; }
                    }
                }
            }    
            
            if (nbLeft>0 && nbRight>0){
                // Yes ! Do it !
                Edge[] leftEdges = new Edge[nbLeft];
                Edge[] rightEdges = new Edge[nbRight];
                nbLeft=nbRight=0;
                for (int j=edges.length; --j>=0; ) {                
                    final double tA = edges[j].tA;
                    final double tB = edges[j].tB;
                    if (tA<split || tB<split) {leftEdges[nbLeft++] = edges[j];}
                    if (tA>split || tB>split)  {rightEdges[nbRight++] = edges[j];}
                }
                InternalNode node = new InternalNode (split, rmin, rmax);
                node.g = computeBinaryTree(leftEdges, rmin, split);
                node.d = computeBinaryTree(rightEdges, split, rmax);
                return node;
            }

        }
        // No solution, so we make a leaf !
        return new Leaf (edges, rmin, rmax);
    }
    
    /**
     * Prepare the dihedra to use it.
     * IMPORTANT : use this before to solve diffraction, 
     *             and when all the edges are added.
     * 
     * @param faces 
     * @param hauteurSol
     * @return <code>true</code> iff somes Dihedron really exists, <code>false</code> otherwise
     */
    public final synchronized boolean finish(final Face[] faces, 
            final double hauteurSol)
    {
        // test if the dihedra if inside the ground
        if (u.z == 0.D && D.z <= hauteurSol) {
            return false;
        }
        // delete all strange edges ...
        for (Iterator<Edge> it=edges.iterator(); it.hasNext(); ) {
            Edge e = it.next();
            for (int i=0; i<faces.length; i++) {
                if (faces[i].inside(e)) {
                    it.remove();
                    //System.err.println ("\nremoving edge "+e.A+" to "+e.B+" from:");
                    break;
                }   
            }
        }
        if (edges.size() == 0) {return false;}
        // Compute the btree 
        btree = computeBinaryTree (getEdges());
        return true;
    }
    
    /**
     * Creates a new instance of Dihedron
     * @param e a first <see>Edge</see> in the Dihedron
     */
    public Dihedron(Edge e) {
        D = new Vector3d (e.getA());
        u = new Vector3d (e.getU());
        normale = new Vector3d ();
        normale.negate(e.getP());
        normale.normalize();
        vector = new Vector3d ();
        edges =  new ArrayList<Edge> ();        
        add (e);
    }
    
    /**
     * Print informations about this Dihedron on a given <code>PrintStream</code>
     * @param ps where to print informations
     */
    public final void print(PrintStream ps) {
        ps.println ("Dihedron");
        ps.println ("\t u = "+u);
        ps.println ("\t D = "+D);
        ps.println ("\thas "+edges.size()+" edges\n");
        for (int i=0; i<edges.size(); i++) {
            Edge e = edges.get(i);
            ps.print ("\tedge["+i+"] between "); e.println(ps);
        }
        if (btree!=null) { btree.print (ps); }
        ps.println();
    }
   
    /**
     * Print information about this dihedron on <code>System.err</code>
     */
    public final void print() {
        print (System.err);
    }
    
    /**
     * Add an edge e to the diffraction line
     * @param e the edge to add
     */
    public final synchronized void add(Edge e) {
        edges.add(e);        
        tabOk = false;
        // Compute tA and tB.
        e.tA = u.x*(e.A.x - D.x) + u.y*(e.A.y - D.y) + u.z*(e.A.z - D.z);
        e.tB = u.x*(e.B.x - D.x) + u.y*(e.B.y - D.y) + u.z*(e.B.z - D.z);
        vector.cross (normale,e.p);
        final double angle = Math.acos(normale.dot(e.p));
        e.angle = (vector.dot(u) < 0.D) ? PI2 - angle : angle;
        btree = null;
    }
    
    
    /**
     * Gives all the edges associated to the diffraction line
     * @return an array containing all the edges
     */
    public final Edge[] getEdges() {
        if (!tabOk) {
            // finish, convert to array
            tabOk = true;
            tabEdges = new Edge[edges.size()];
            edges.toArray(tabEdges);
        }
        return tabEdges;
    }
    
    
    /**
     * Gives the distance of a given position to the diffraction line
     * @param P the given position
     * @return the length
     */
    public final double distance(final Point3d P) {
        Vector3d v = new Vector3d (P);
        P.sub (D);
        v.cross (v, u);
        return v.length();
    }
    
    
    /**
     * Returns the projection of a given point on the Diffraction line
     * @param P the point to project on the Diffraction line
     * @return the relative position of the projected point P
     */
    public final double project(final Vector3d P) {
        return u.x*(P.x-D.x) + u.y*(P.y-D.y) + u.z*(P.z-D.z);
    }
    
    
    /**
     * Computes the projection of a given point on the Diffraction line
     * @param P the point to project on the Diffraction line
     * @param res where to store the resulting point
     */
    public final void project(final Vector3d P, final Vector3d res) {
        final double t = u.x*(P.x-D.x) + u.y*(P.y-D.y) + u.z*(P.z-D.z);
        res.scaleAdd (t, u, D);
    }
    
    
    /**
     * Returns a point on the diffraction line 
     * @param t the relative position of the Point to compute
     * @return The computed point
     */
    public final Vector3d getPoint(final double t) {
        Vector3d p = new Vector3d(u);
        p.scaleAdd (t, D);
        return p;
    }
    
    
    /**
     * Computes a point on the diffraction line 
     * @param t the relative position of the Point to compute
     * @param P where the point is stored
     */
    public final void getPoint(final double t, final Vector3d P) {
        P.scaleAdd (t, u, D);
    }
    
    
    /**
     * Is a point on the diffraction line corresponds to at least one edge ?
     * @param t the relative position on the Dihedron line
     * @return <code>true</code> iff at least one Edge surround the given position
     */
    public final boolean isDiffractionPoint(final double t) {
        // Is it an edge arround t ? May be two ??
        if (btree instanceof InternalNode) {
            InternalNode node = (InternalNode)btree;
            if (node.max < t || node.min > t) {
                return false;
            }
        }
        BinaryTree bt = btree;
        while (true) {
            //if (bt == null) return false;
            if (bt instanceof Leaf) {
                Leaf f = (Leaf)bt;
                return t<=f.max && t>=f.min;
            } 
            final InternalNode node = (InternalNode)bt;
            bt = node.t>t ? node.g : node.d;
        }
    }
    
    /** TODO
     * Ajouter une information sur la rotation autour de l'axe de diffraction
     * L'utiliser ensuite par "edge", pour trouver le diedre  de diffraction
     * Ajout du calcul de diffraction (mais il utilise la notion d'angle solide)
     * @param t
     * @param E 
     * @param R
     */
    
    /**
     * Is a point on the diffraction line corresponds to a valid diffraction ?
     * @param D the position of the diffraction Point
     * @param E the transmitter position
     * @param R the receiver position
     * @return <code>true</code> iff diffraction really occurs, <code>false</code> otherwise
     */
    public final boolean whichedges(final Vector3d E, final Vector3d D, final Vector3d R, int indices_edge[])
    {
        double t = this.compute_t(E, D, R);//Attention vérifier les paramètres!!!

        // Is it an edge arround t ? May be two ??
        final Edge[] edges = this.getEdges();
        Vector3d EpE = new Vector3d();
        Vector3d RRp = new Vector3d();
        
        this.project(E, EpE);
        if (E.equals(EpE)) { return false; }
        EpE.sub(E, EpE); // Notice that we compute the vector E->Ep ...
        EpE.normalize();
        // search the in face
        vector.cross(EpE, this.u); // for the side test ! == normale to plane Eu
        if (vector.x==0.D && vector.y==0.D && vector.z==0.D) { return false; }

        double aMinL=+2.D;
        double aMinR=+2.D;
        int i1L = -1;
        int i1R = -1;
        // search  the nearest left and right
        for (int i=0; i<edges.length; i++) {
            final Edge e = edges[i];
            if (e.tA<t && e.tB<t || e.tA>t && e.tB>t) {
                System.err.print ("Strange leaf for t ="+t+" :: ");
                e.println (System.err);
                continue;
            }
            final double minE = EpE.dot(e.p);
            // side ??
            if (vector.dot(e.p) >= 0.D) { // say left
                if (minE < aMinL) {
                    aMinL = minE;
                    i1L = i;
                }
            } else {
                if (minE < aMinR) {
                    aMinR = minE;
                    i1R = i;
                }
            }
        }
        if (i1L == -1 && i1R == -1) { return false; }

        // find the nearest edges ...
        if (i1L>=0 && i1R>=0) { // both side
            if (Math.acos(aMinL) + Math.acos(aMinR) >= Math.PI) {
                return false;
            }
            if (aMinL<aMinR) {
                indices_edge[0] = i1L;
                indices_edge[1] = i1R;
            } else {
                indices_edge[0] = i1R;
                indices_edge[1] = i1L;
            }
        }
        else if (i1L>=0) { // all is left
            final int i2 = i1L == 0 ? edges.length-1 : i1L-1;
            indices_edge[0] = i1L;
            indices_edge[1] = i2;
        }
        else { // all is right
            final int i2 = i1R == 0 ? edges.length-1 : i1R-1;
            indices_edge[0] = i1R;
            indices_edge[1] = i2;
       }
        // search the out primitives : only one side, or two ??
        this.project(R, RRp);
        if (R.equals(RRp)) { return false; }
        RRp.sub(R);
        RRp.normalize();

        final boolean RisLeft = vector.dot(RRp) >= 0.D;
        final double Rdot = EpE.dot(RRp);
        if (i1L>=0 && i1R>=0) { // two sides !
            if (RisLeft) { // R is Left !!
                if (Rdot >= aMinL) { return false; }
            } else { // R is right
                if (Rdot >= aMinR) { return false; }
            }
            return true;
        }

        // only one ...
        if (RisLeft) {
            // R is left, compare to left values
            if (i1L==-1) { return true; }
            final int i2 = i1L == 0 ? edges.length-1 : i1L-1;
            return Rdot <= aMinL || Rdot >= EpE.dot(edges[i2].p);
        }

        // R is right, compare to right ...
        if (i1R==-1) { return true; }
        final int i2 = i1R == edges.length-1 ? 0 : i1R+1;
        return Rdot <= aMinR || Rdot >= EpE.dot(edges[i2].p);

    }

    /**
     * compute a single diffraction
     * @param E
     * @param P
     * @param R
     * @return
     */
    private final double compute_t(final Vector3d E, final Vector3d P,
            final Vector3d R) {
        final Vector3d d = this.D;
        final Vector3d uu = this.u;
        Vector3d A = new Vector3d();

        A.sub(E, d);
        final double t1 = A.dot(uu);
        A.scaleAdd(t1, uu, d);

        A.sub(E, A);
        final double d1 = A.length();

        A.sub(R, d);
        final double t2 = A.dot(uu);        /* Calcul du point P1              */
        A.scaleAdd(t2, uu, d);
        A.sub(R, A);
        final double d2 = A.length();

        final double tt = (d1 * t2 + d2 * t1) / (d1 + d2);        /* !!!!                            */

        P.scaleAdd(tt, uu, d);

        return tt;
    }
}
