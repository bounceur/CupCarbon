/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.Serializable;
import java.util.ArrayList;
import javax.vecmath.Vector3d;
//import org.openide.awt.StatusDisplayer;
//import org.openide.windows.IOProvider;
/**
 * Factory Pattern for Dihedron. A dihedron is made using faces connected 
 * to a line where diffraction can occur.
 * @author pcombeau
 */
final public class DihedronFactory implements Serializable {
    private static final long serialVersionUID = 0x66001005;
    
    private final double eps = 1e-6D;
    
    private ArrayList<Dihedron> diedres = new ArrayList<Dihedron>();
    private Dihedron[] tabDiedres = null;
    
    /**
     * Creates a new instance of DihedronFactory
     */
    public DihedronFactory() {    }
    
    /** Return all the dihedron made by this factory
     * @return Dihedron made by this factory
     */
    public final Dihedron[] getTabDiedres() {        
        return tabDiedres;
    }
    
    /** Add an edge to this factory. This either result in a new dihedra,
     * or in adding the edge to an existing dihedra.
     * @param e 
     */
    public void add(Edge e) {
        final int size = (diedres == null ? 0 : diedres.size());
        // First, check if a dihedra corresponds to this edge.
        // it is the case when the straigth line are the same ...
        // If yes, add the edge to this dihedra, and returns
        Vector3d u = e.getU();
        Vector3d A = e.getA();
        for (int i=0; i<size; i++) {
            Dihedron d = diedres.get(i);
            // The "u" vector are normalized ...
            double dot = d.u.dot (u);
            if (Math.abs (dot) >= (1.D-eps)) {
                // directions are egals. Is A in the straigth line ?
                Vector3d v = new Vector3d (d.D);
                v.sub (A);
                v.cross (v, d.u);
                if (A.equals(d.D) || v.lengthSquared() < eps) {
                    // We found the dihedra !!
                    d.add (e);
                    //System.out.println("Push edge to already existing dihedra");
                    //System.out.printf ("  u = %f %f %f -- A = %f %f %f\n", u.x, u.y, u.z, A.x, A.y, A.z);
                    //System.out.printf ("  u = %f %f %f -- D = %f %f %f\n", d.u.x, d.u.y, d.u.z, d.D.x, d.D.y, d.D.z);
                    return ;
                }
            }
        }
        //System.out.println("Add a new dihedra :");
        // No ? Then add a new dihedra !
        diedres.add (new Dihedron(e));
        //diedres.get(size).print(System.out);
        // TODO : I need to add an acceleration structure for scan the dihedron ...
    }
    
    
    /**
     * Prepare the dihedron to be used for diffraction search ...
     * @param faces 
     * @param hauteurSol
     */
    public synchronized void finish(final Face[] faces, final double hauteurSol) {
        if (diedres == null) {
            return;
        }
        int begin = 0;
        int end = diedres.size();
        tabDiedres = new Dihedron[end];
        tabDiedres = diedres.toArray(tabDiedres);
        while (begin<end) {
            if (tabDiedres[begin].finish (faces, hauteurSol)) {
                begin++;
            }
            else {
                // swap end and begin ...
                tabDiedres[begin] = tabDiedres[--end];
                tabDiedres[  end] = null;
            }
        }
        if (end < tabDiedres.length) {
            //IOProvider.getDefault().getStdOut().println("Reducing the number of Dihedron, from "+tabDiedres.length+" to "+end);
            //StatusDisplayer.getDefault().setStatusText("Reducing the number of Dihedron, from "+tabDiedres.length+" to "+end);
            // array's size has changed
            if (end == 0) { tabDiedres = null; }
            else {
                Dihedron [] nDiedres = new Dihedron[end];
                System.arraycopy (tabDiedres, 0, nDiedres, 0, nDiedres.length);
                tabDiedres = nDiedres;
            }
        }
        diedres = null;
    }
}
