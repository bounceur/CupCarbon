/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.j3d.utils.geometry.Primitive;

import device.SensorNode;

/**
 * A scene contains mainly a geometry, simulation results, and some
 * informations for simulation.
 * @author pcombeau
 */
public final class Scene implements Serializable {
    static private final long serialVersionUID = 0x66001014;
    
    private String name = "";
    
    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    
    private boolean modified;
    
    private transient ArrayList<Face> faces = new ArrayList<Face> ();

    private DihedronFactory diedres = new DihedronFactory();
    
    private Face[]   tabFaces;

    // A physics is declared as a generic for F that extends Field,
    // so the cast is good!
     //@SuppressWarnings("unchecked")
    //private Physics<? extends Field> physics =
     //       (Physics<? extends Field>) Lookup.getDefault().lookup(Physics.class);

//    public Physics<? extends Field> getPhysics() {
//        return physics;
//    }

    /**
     * Associate a Physical model to this scene
     * @param physics
     */
//    public void setPhysics(Physics<? extends Field> physics) {
//        this.physics = physics;
//    }
    
    
    /**
     * The floor height, useful for instance to place antenna
     */
    public double groundHeight;
    /**
     * Minimum abcissa of the geometric elements
     */
    public transient double xmin;
    /**
     * Maximum abcissa of the geometric elements
     */
    public transient double xmax;
    /**
     * Minimum y of the geometric elements
     */
    public transient double ymin;
    /**
     * Maximum y of the geometric elements
     */
    public transient double ymax;
    
    //private SpatialStructure spatialStructure;
    
    /**
     * The main transmitter
     */
    public SensorNode transmitter;
    
    /**
     * The main receiver
     */
    public SensorNode receiver;
    
    /**
     * The receivers list, for receivers path account
     */
    public ArrayList<SensorNode> transmitters = new ArrayList<SensorNode>();
    public ArrayList<SensorNode> receivers = new ArrayList<SensorNode>();
    
    private SensorNode [] receiverCoverage;
    private int receiversWidth;
    private int receiversHeight;
    private boolean coverMode = false;

    
    private double coverStep = 1.5D; // par defaut, 5 metres entre 2 points ...


    /**
     * Number of reflexion currently into account
     */
    public int reflectionNumber;
    
    /**
     * Number of diffraction currently into account
     */
    public int diffractionNumber;
    
    /**
     * Number of transmission currently into account
     */
    public int transmissionNumber;
    
    /**
     * 
     */
    //public transient Path paths[];
    /**
     * 
     */
    //public transient Path coverPaths[][];
    /**
     * 
     */
    public transient double powers[];
    
    /**
     * The path number to enhance in the 2d View
     */
    public transient int enhancedPath = -1;
    
    /**
     * Frequency values
     */
    public double lambda;
    
    /** Largeur de la fenetre d'affichage */
    private transient int width;
    /** Hauteur de la fenetre d'affichage */
    private transient int height;
    
    
    
    
    /** Retourne la liste des receivers d'une zone de couverture
     * @return an array of receivers
     */
    public final synchronized SensorNode[] getRecepteur_couverture() {
        if (!coverMode) { return null; }
        return receiverCoverage;
    }
    
    
    
    /** Retourne le nombre de receiver en largeur d'une zone de couverture
     * @return the number of receiver in the cover width
     */
    public int getRecepteurCouvertureWidth() {
        return receiverCoverage != null ? receiversWidth : 0;
    }
    
    
    
    /** Retourne le nombre de receiver en hauteur d'une zone de couverture
     * @return 
     */
    public int getRecepteurCouvertureHeigth() {
        return receiverCoverage != null ? receiversHeight : 0;
    }
    
    
    
    /** Retourne la distance L1 entre deux receivers.
     * @return 
     */
    public double getCouverture_pas() {
        return coverStep;
    }
    
    
    
    /** Donne les faces de la ssene
     * @return 
     */
    public Face[] getTabFaces() {
        if (modified) {
            modified = false;
            tabFaces = new Face[faces.size()];
            faces.toArray(tabFaces);
        }
        return tabFaces;
    }
    
    
    
    /** Donne les faces de la scene
     * @return 
     */
//    public Dihedron[] getTabDiedres() {
//        return diedres.getTabDiedres();
//    }
    
    
    
    /** Modifie la distance L1 entre deux receivers en mode zone de couverture
     * @param couverture_pas la distance L1 entre deux points de receptions
     */
//    public void setCouverture_pas(Double couverture_pas) {
//        this.coverStep = couverture_pas;
//        if (coverMode) {
//            coverPaths = null;
//            calculerRecepteursCouverture();
//        }
//        changeMode();
//    }
    
    
    
    /** Changer le modele d'antenne des receivers */
//    private void changeModelReceivers () {
//        System.err.println ("changeModelReceivers() called");
//        try {
//            ListIterator<Receiver> iter = receivers.listIterator();
//            while (iter.hasNext()) {
//                Receiver r = iter.next();
//                iter.remove();
//                Receiver rn = receiver.clone ();
//                rn.setPosition(r.getPosition());
//                rn.getDirection().set(r.getDirection());
//                iter.add(rn);
//            }    
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//        }
//    }
    
    
                
    // Calcul les receivers ...
//    private void calculerRecepteursCouverture() {
//        // au passage, modifier les receivers de la liste
//        //changeModelReceivers ();
//        
//        // le calcul est-il possible ???
//        coverMode = isModeCouverture()
//                && xmax-xmin > coverStep
//                && ymax-ymin > coverStep;
//        if (!isModeCouverture()) { return ; }
//        
//        // calcul du nombre de points de reception
//        
//        // Ou est le premier point ???
//        double dx = transmitter.getPosition()[0] - coverStep/2. - xmin;
//        double dy = transmitter.getPosition()[1] - coverStep/2. - ymin;
//        Vector3d first = 
//            new Vector3d(xmin+dx-Math.floor(dx/coverStep)*coverStep,
//                         ymin+dy-Math.floor(dy/coverStep)*coverStep,
//                         receiver.getPosition()[2]);
//        
//        receiversWidth  = ((int)Math.floor((xmax-first.x)/coverStep)) + 1;
//        receiversHeight = ((int)Math.floor((ymax-first.y)/coverStep)) + 1;
//        final int nbRecepteurs = receiversWidth * receiversHeight;
//        
//        receiverCoverage = new SensorNode [nbRecepteurs];
//        powers = new double[nbRecepteurs];
//        
//        for (int x=0; x<receiversWidth; x++)
//        {            
//            for (int y=0; y<receiversHeight; y++)
//            {
//                final int pos = x*receiversHeight+y;
//
//				receiverCoverage[pos] = new StdSensorNode(receiver) ; //(SensorNode) receiver.clone();
//                receiverCoverage[pos].setPosition (first.x+coverStep*x,
//                                                       first.y+coverStep*y,
//                                                       first.z);
//            }
//        }
//    }
    
    
    
    /** Creates a new instance of Scene
     * @param transmitter
     * @param receiver
     */
    public Scene(SensorNode transmitter, SensorNode receiver)
    {
    	this.transmitter = transmitter;
    	this.receiver = receiver;
    	modified = false;
    	
//        try {
//			this.transmitter  = (SensorNode) transmitter.clone();
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        try {
//			this.receiver = (SensorNode) receiver.clone();
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
    
    
    
    /** Add a receiver in the receiver list
     * @param r 
     */
    public void add_receiver (SensorNode r) {
        receivers.add (r);
    }
    
    
    
    /** Add a transmitter in the transmitter list
     * @param e 
     */
    public void add_transmitter (SensorNode e) {
        transmitters.add (e);
    }
    
    
    
    /** Add some geometry ...
     * @param f 
     */
    public void addFace(Face f) {
        modified = true;
        faces.add(f);
        Edge[] edges = f.getEdges();
        for (int i=0; i<edges.length; i++) {
            diedres.add(edges[i]);
        }
    }
    
    
    
    /** Add some geometry ...
     * @param f 
     */
    public void addFaceSol(Face f) {
        modified = true;
        faces.add(0, f);
    }
    
    
    
    /** Idem, mais pour des faces
     * @param f 
     */
    public void addFaces(Face f[]) {
        modified = true;
        for (int i=0; i<f.length; i++) {
            faces.add(f[i]);
        }
    }
    
    
    
    /**
     * Use this when all the faces are added !!
     */
    public void allFaceAreSet() 
    {
        diedres.finish(getTabFaces(), groundHeight);
//        if ( (getTabFaces() != null) && (getTabFaces().length < 100000) ) {
//            spatialStructure = new NoStructure();
//        }
//        else {
//            spatialStructure = new Grid();
//        }
//        spatialStructure.build(getTabFaces());
    }
    
    
    
    /**
     * Get a new Iterator on the faces. Using a different iterator is necessary
     * for each thread that works with a same instance of Scene.
     * @return a new Iterator
     */
//    public SpatialStructure.Iterator getIterator() {
//        return spatialStructure.getIterator();
//    }
            
    
    
    /**
     * Displays all the faces 
     * @param out writer to use for display
     */
//    public void printFaces (OutputWriter out) 
//    {
//        for (int i=0; i<faces.size(); i++) {
//            out.println ("FACE "+i+":");
//            //System.out.printf("FACE %d : \n", i);
//            faces.get(i).printFace();
//        }
//    }
    
    
    
    /**
     * 
     * @param width
     * @param height
     */
    public void drawSetWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    
    
    /**
     * 
     * @param g
     */
//    public void drawFaces2d(Graphics g) 
//    {
////            System.err.println("drawFaces2d called");
//        faces_loop: for (int i=0; i<faces.size(); i++) 
//        {
//            boolean isSol = true;
//            final Face f = faces.get(i);
//            // is this face f mask some receivers ?
//            if (mode == SimulationMode.SISO && coverMode && coverPaths != null)
//            {
//                final BoundingBox bbox = f.getBBox();
//                // find first Rx coordinates
//                int firstx=0;
//                int lastx=0;
//                int lasty=0;                
//                int firsty=0;
//                for (int p = 0; p < receiversHeight; p++)
//                {
//                    if (receiverCoverage[p].getPosition().y >= bbox.getMinimum().y )
//                    {
//                        firsty = p;
//                        while (receiverCoverage[p].getPosition().y <= bbox.getMaximum().y) {
//                            p++;
//                            if (p >= receiversHeight) {
//                                break;
//                            }
//                        }
//                        lasty = p;
//                        break;
//                    }
//                }
//                for (int p = 0; p < receiversWidth; p++)
//                {
//                    if (receiverCoverage[p*receiversHeight].getPosition().x >= bbox.getMinimum().x )
//                    {
//                        firstx = p;
//                        while (receiverCoverage[p*receiversHeight].getPosition().x <= bbox.getMaximum().x) {
//                            p++;
//                            if (p >= receiversWidth) {
//                                break;
//                            }
//                        }
//                        lastx = p;
//                        break;
//                    }
//                }
//                final Ray ray = Ray.createFromDirectionAndStart(new Vector3d (0,0,1), new Vector3d(0,0,0));
//                for (int x=firstx; x<lastx; x++) {
//                    for (int y = firsty; y < lasty; y++) {
//                        final int pos = x * receiversHeight + y;
//                        if (coverPaths[pos] != null && coverPaths[pos].length == 0) {
//                            continue;
//                        }
//                        final Receiver<?> Rx = receiverCoverage[pos];
//                        ray.getFrom().set(Rx.getPosition());
//                        //ray.getFrom().setZ(groundHeight - 1.D);
//                        if (f.intersection(ray) != null) {
//                            continue faces_loop;
//                        }
//                    }
//                }
//            }
//            final int xPoints[] = new int[f.sommets.length];
//            final int yPoints[] = new int[f.sommets.length];
//            //double z = f.sommets[0].z;
//            for (int j=0; j<f.sommets.length; j++) {
//                xPoints[j] = convertX(f.sommets[j].x);
//                yPoints[j] = convertY(f.sommets[j].y);
//                 isSol = isSol && (f.sommets[j].z <= groundHeight);
//            }
////            System.err.println("face isSol ? "+isSol);
//
//            if (!isSol)
//            {
////                System.err.println("Draw a face");
//                g.fillPolygon(xPoints, yPoints, f.sommets.length);
//            }
//
//        }
//    }
    
    
    
    /**
     * 
     * @param g
     */
//    public void drawEmetteur(Graphics g) {
//        if ( transmitters==null || mode == SimulationMode.SISO || mode == SimulationMode.SIMO)
//        {
//            g.fillArc(convertX(transmitter.getPosition().x)-3,
//                      convertY(transmitter.getPosition().y)-3,
//                      5, 5,
//                      0, 360);
//        }
//        else {
//            for (Transmitter<?> Tx : transmitters) {
//                g.fillArc(convertX(Tx.getPosition().x) - 3, convertY(Tx.getPosition().y) - 3, 5, 5, 0, 360);
//            }
//        }
//    }
//    
//    
//    
//    /**
//     * 
//     * @param g
//     */
//    public void drawRecepteur(Graphics g) {
//        if (isModeCouverture() && coverPaths != null)
//        {
//            int px = convertX(receiverCoverage[receiversHeight].getPosition().x)
//                   - convertX(receiverCoverage[0].getPosition().x);
//            int py = convertY(receiverCoverage[0].getPosition().y)
//                   - convertY(receiverCoverage[1].getPosition().y);
//            for (int x=0; x<receiversWidth; x++) {
//                for (int y = 0; y < receiversHeight; y++) {
//                    final int pos = x * receiversHeight + y;
//                    if (coverPaths[pos] != null && coverPaths[pos].length > 0) {
//                        g.setColor(Jet.getColor(powers[pos]));
//                        g.fillRect(convertX(receiverCoverage[pos].getPosition().x) - px / 2, convertY(receiverCoverage[pos].getPosition().y) - py / 2, px + 1, py + 1);
//                    }
//                }
//            }
//        } 
//        else if (/*coverPaths != null && */(mode==SimulationMode.SIMO || mode==SimulationMode.MIMO))
//        {
//            for (Receiver<?> Rx : receivers) {
//                g.fillArc(convertX(Rx.getPosition().x) - 3, convertY(Rx.getPosition().y) - 3, 5, 5, 0, 360);
//            }
//        }
//        else {
//            g.fillArc(convertX(receiver.getPosition().x) - 3, convertY(receiver.getPosition().y) - 3, 5, 5, 0, 360);
//        }
//    }
//    
//    
//    private void drawTrajet(Graphics g, Path tr) {
//        if (tr.points == null || tr.points.length==0) {
//            g.drawLine(convertX(tr.transmitter.getPosition().x), convertY(tr.transmitter.getPosition().y), convertX(tr.receiver.getPosition().x), convertY(tr.receiver.getPosition().y));
//        }
//        else {
//            g.drawLine(convertX(tr.transmitter.getPosition().x),
//                    convertY(tr.transmitter.getPosition().y),
//                    convertX(tr.points[0].x),
//                    convertY(tr.points[0].y));
//            int last = tr.points.length-1;
//            for (int n=0; n<last; n++) {
//                g.drawLine(convertX(tr.points[n].x), convertY(tr.points[n].y), convertX(tr.points[n + 1].x), convertY(tr.points[n + 1].y));
//            }
//            g.drawLine(convertX(tr.points[last].x),
//                    convertY(tr.points[last].y),
//                    convertX(tr.receiver.getPosition().x),
//                    convertY(tr.receiver.getPosition().y));
//        }
//    }
//    
//    /**
//     * 
//     * @param g
//     */
//    public void drawTrajets(Graphics g) 
//    {
//        if ( isModeCouverture() ) {
//            return;
//        }
//        
//        if (mode != SimulationMode.SISO) {
//            if (coverPaths == null) { return ; }
//            for (Path[] pts : coverPaths) {
//                if (pts == null) {
//                    continue;
//                }
//                for (Path pt : pts) {
//                    g.setColor(Jet.getColor(pt.color));
//                    drawTrajet(g, pt);
//                }
//            }
//        }
//        else {
//            if (paths == null) {
//                return;
//            }
//            for (Path tr : paths) {
//                g.setColor(Jet.getColor(tr.color));
//                drawTrajet(g, tr);
//            }
//            if (enhancedPath >= 0 && enhancedPath < paths.length) {
//                g.setColor(javax.swing.UIManager.getDefaults().getColor("TextArea.selectionBackground"));
//                drawTrajet(g, paths[enhancedPath]);
//            }
//        }
//    }
    
    /** Conversion d'une abscisse vers le monde de la scène
     * @param x
     * @return 
     */
    public int convertX(final double x) {
        double translate = x - xmin;
        double scale = translate / (xmax-xmin);
        return (int)(((double)width) * scale);
    }
    /**
     * 
     * @param x
     * @return
     */
    public double convertX(final int x) {
        return xmin + x * (xmax-xmin) / width;
    }
    
    /** Conversion d'une ordonnée vers le monde de la scène
     * @param y
     * @return 
     */
    public int convertY(final double y) {
        double translate = y - ymin;
        double scale = translate / (ymax-ymin);
        return height-(int)(((double)height) * scale);
    }
    
    /**
     * 
     * @param y
     * @return
     */
    public double convertY(final int y) {
        return ymin + (height-y) * (ymax-ymin) / height;
    }
    
    /**
     * 
     * @param value
     */
//    public void setEmetteurX(final Object value) {
//        if (value instanceof Double) {
//            transmitter.setPosition(((Double)value).doubleValue(),transmitter.getPosition().y,transmitter.getPosition().z);
////            transmitter.getPosition().x = ((Double)value).doubleValue();
//            calculerRecepteursCouverture();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            changeMode();
//        } else {
//            System.out.println("pas cool !");
//        }
//    }
    
    /**
     * 
     * @param value
     */
//    public void setEmetteurY(Object value) {
//        if (value instanceof Double) {
//            transmitter.setPosition(transmitter.getPosition().x,((Double)value).doubleValue(),transmitter.getPosition().z);
////            transmitter.getPosition().y = ((Double)value).doubleValue();
//            calculerRecepteursCouverture();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            changeMode();
//        }
//    }
    
    /**
     * 
     * @param x
     * @param y
     */
//    public void setEmetteurXY(int x, int y) {
//        transmitter.setPosition(convertX(x),convertY(y),transmitter.getPosition().z);
//        //transmitter.getPosition().x = convertX(x);
//        //transmitter.getPosition().y = convertY(y);
//        calculerRecepteursCouverture();
//        paths = null;
//        enhancedPath = -1;
//        coverPaths = null;
//        changeMode();
//    }
//    
    /**
     * 
     * @param x
     * @param y
     */
//    public void setRecepteurXY(int x, int y) {
//        if (isModeCouverture()) {
//            return;
//        }
//        receiver.setPosition(convertX(x),convertY(y),receiver.getPosition().z);
////        receiver.getPosition().x = convertX(x);
////        receiver.getPosition().y = convertY(y);
//        calculerRecepteursCouverture();
//        paths = null;
//        enhancedPath = -1;
//        coverPaths = null;
//        changeMode();
//    }
//    
//    /**
//     * 
//     * @param x
//     */
//    public void setEmetteurX(int x) {
//        transmitter.setPosition(convertX(x),transmitter.getPosition().y,transmitter.getPosition().z);
////        transmitter.getPosition().x = convertX(x);
//        calculerRecepteursCouverture();
//        paths = null;
//        enhancedPath = -1;
//        coverPaths = null;
//        changeMode();
//    }
//    
//    /**
//     * 
//     * @param y
//     */
//    public void setEmetteurY(int y) {
//        transmitter.setPosition(transmitter.getPosition().x,convertX(y),transmitter.getPosition().z);
////        transmitter.getPosition().y = convertY(y);
//        calculerRecepteursCouverture();
//        paths = null;
//        enhancedPath = -1;
//        coverPaths = null;
//        changeMode();
//    }
//    
//    /**
//     * 
//     * @param value
//     */
//    public void setEmetteurZ(Object value) {
//        if (value instanceof Double) {
//            transmitter.setPosition(transmitter.getPosition().x,transmitter.getPosition().y,((Double)value).doubleValue());
////            transmitter.getPosition().z = ((Double)value).doubleValue();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            calculerRecepteursCouverture();
//            changeMode();
//        }
//    }
    
    /**
     * 
     * @return
     */
    public boolean isModeCouverture() {
        return coverMode;
    }
    
//    /**
//     * 
//     * @param value
//     */
//    public void setRecepteurX(Object value) {
//        if (value instanceof Double) {
//            receiver.setPosition(((Double)value).doubleValue(),receiver.getPosition().y,receiver.getPosition().z);
////            receiver.getPosition().x = ((Double)value).doubleValue();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            changeMode();
//        }
//    }
//    
//    /**
//     * 
//     * @param modeCouverture
//     */
//    public void setModeCouverture(boolean modeCouverture) {
//        this.coverMode = modeCouverture;
//        if (modeCouverture) {
//            coverPaths = null;
//            calculerRecepteursCouverture();
//        }
//        changeMode();
//    }
//    
//    
//    /**
//     * 
//     * @param mode
//     */
////    public void setModeRxPath(boolean mode) {
////        this.modeRxPath = mode;
////        if (mode) {
////            //changeModelReceivers();
////        }
////        if (mode && modeCouverture) {
////            setModeCouverture (false);
////        }
////        changeMode();
////    }
//    
//    /**
//     * 
//     * @return
//     */
////    public boolean getModeRxPath() {
////        return modeRxPath;
////    }
////    
//
//    /**
//     * 
//     * @param value
//     */
//    public void setRecepteurY(Object value) {
//        if (value instanceof Double) {
//            receiver.setPosition(receiver.getPosition().x,((Double)value).doubleValue(),receiver.getPosition().z);
////            receiver.getPosition().y = ((Double)value).doubleValue();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            changeMode();
//        }
//    }
//    
//    /**
//     * 
//     * @param value
//     */
//    public void setRecepteurZ(Object value) {
//        if (value instanceof Double) {
//            receiver.setPosition(receiver.getPosition().x,receiver.getPosition().y,((Double)value).doubleValue());
////            receiver.getPosition().z = ((Double)value).doubleValue();
//            paths = null;
//            enhancedPath = -1;
//            coverPaths = null;
//            changeMode();
//        }
//    }
//    
//    /**
//     * 
//     * @param tabTrajets
//     */
//    public void setTrajetsCouverture(Path tabTrajets[][]) {
//        coverPaths = tabTrajets;
//        for (int x=0; x<receiversWidth; x++) {
//            for (int y = 0; y < receiversHeight; y++) {
//                final int pos = x * receiversHeight + y;
//                if (pos >= tabTrajets.length) {
//                    System.err.println ("setTrajetsCouverture::index out of bounds");
//                    continue;
//                }
//                if (tabTrajets[pos] != null) {
////                    EField ch = new EField();
////                    for (int i = 0; i < tabTrajets[pos].length; i++) {
////                        ch.add(tabTrajets[pos][i].amplitude);
////                    }
//                    powers[pos] = 1D;//ch.getPower();
//                } else {
//                    powers[pos] = 0D;
//                }
//            }
//        }
//        changeMode();
//    }
//    
//    
//    
//    private Simulator simulator = Lookup.getDefault().lookup(SimulatorFactory.class).create(this);
//
//    public Simulator getSimulator() {
//        return simulator;
//    }
//
//    public void setSimulator(Simulator simulator) {
//        this.simulator = simulator;
//        changeMode();
//    }
    
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public void addChangeListener (final ChangeListener cl) {
        listeners.add(cl);
    }
    
    public void removeChangeListener (final ChangeListener cl) {
        listeners.remove(cl);
    }
    
    public void changeMode () {
        final ChangeEvent ce = new ChangeEvent (this);
        for (ChangeListener cl : listeners) {
            cl.stateChanged (ce);
        }
    }
    
    //private Sheet sheet = null;
    static final public double CELERITE = 3e-1D;
    static boolean simulationInProgress = false;
    
//    public Sheet createSheet() {
//        if (sheet == null) {
//            sheet = new Sheet();
//            //sheet = Sheet.createDefault();
//            Sheet.Set set = Sheet.createPropertiesSet();
//            set.setName("Geometrical Properties");
//            set.setDisplayName("Geometrical Properties");
//
//            // add number of faces ...
//            Property<Integer> faceNumberProp = (Property<Integer>) new PropertySupport.ReadOnly<Integer>("face number", Integer.class,
//                    "Face Number", "Displays the number of faces included") {
//
//                @Override
//                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
//                    return tabFaces != null ? tabFaces.length : 0;
//                }
//            };
//            faceNumberProp.setName("face number");
//            set.put(faceNumberProp);
//            sheet.put(set);
//            // add number of dihedron ...
//            Property<Integer> dihedronNumberProp = (Property<Integer>) new PropertySupport.ReadOnly<Integer>("dihedron number", Integer.class,
//                    "Dihedron Number", "Displays the number of dihedron included") {
//
//                @Override
//                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
//                    return diedres.getTabDiedres() != null ? diedres.getTabDiedres().length : 0;
//                }
//            };
//            dihedronNumberProp.setName("dihedron number");
//            set.put(dihedronNumberProp);
//            sheet.put(set);
//            // add groundHeight
//            Property<Double> heigthProp = (Property<Double>) new PropertySupport.ReadOnly<Double>("ground heigth", Double.class,
//                    "Ground Height", "Height of the ground") {
//
//                @Override
//                public Double getValue() throws IllegalAccessException, InvocationTargetException {
//                    return groundHeight;
//                }
//            };
//            heigthProp.setName("ground heigth");
//            set.put(heigthProp);
//            sheet.put(set);
//
//            // A second set of properties, for simulation
//            set = Sheet.createPropertiesSet();
//            set.setName("Simulation Properties");
//            set.setDisplayName("Simulation Properties");
//            
////            // add lambda (in fact the frequency, so CELERITE on lambda !)
////            Property<Double> freqProp =
////                new PropertySupport.ReadWrite<Double>("frequencies", Double.class,
////                        "frequency", "set the frequency band in Ghz") {
////                @Override
////                public Double getValue() throws IllegalAccessException, InvocationTargetException {
////                    return (CELERITE/lambda);
////                }
////                @Override
////                public void setValue(Double arg0) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
////                    lambda = CELERITE / arg0;
////                    StatusDisplayer.getDefault().setStatusText("lambda is now ");//+lambda);
////                }
////            };
////            freqProp.setName ("frequency");
////            set.put(freqProp);
//
// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////
//            double[] dataf = getPhysics().getFrequencies();
//            Vector3d fb = new Vector3d();
//            if (dataf.length == 1) {
//                fb.set(dataf[0], dataf[0], 0D);
//            }
//            else {
//                fb.set(dataf[0], dataf[dataf.length-1], dataf[1] - dataf[0]);
//            }
//            Vector3dProperty freqband = new Vector3dProperty(fb, "Freq. in GHz (fmin, fmax, step)");
//            freqband.setName("Frequency bandwidth");
//            set.put(freqband);
//            sheet.put(set);
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//            // add couverture_pas
//            try {
//                final Property<Double> coverProp = new PropertySupport.Reflection<Double>(this, Double.class, "couverture_pas");
//                coverProp.setName ("cover step");
//                set.put (coverProp);
//            }
//            catch (NoSuchMethodException e) { 
//                Exceptions.printStackTrace(e);//e.printStackTrace(IOProvider.getDefault().getStdOut());
//            }
//            
//            final Property<Boolean> propCover = new PropertySupport.ReadWrite<Boolean>("cover mode", Boolean.class, "coverage", "set the coverage mode, or SIMO mode, where simulation are produced for a covering area") {
//                @Override
//                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
//                    return coverMode;
//                }
//                @Override
//                public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                    coverMode = val;
//                    if (coverMode) {
//                        calculerRecepteursCouverture();
//                    }
//                }
//            };
//            set.put(propCover);
//            
////            final Property<Boolean> propRxPath = new PropertySupport.ReadWrite<Boolean>
////                ("Rx path", Boolean.class, "Rx path", 
////                "set the Rx mode, or SIMO mode, where simulation are produced for a list of receivers") 
////            {
////                @Override
////                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
////                    return modeRxPath;
////                }
////                @Override
////                public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
////                    setModeRxPath(val);
////                }
////            };
////            set.put(propRxPath);            
//
//            set.put(new ModeProperty());            
//            
//            // Number of reflexion
//            final Property<Integer> nbR = new PropertySupport.ReadWrite<Integer>("reflexion number", Integer.class, 
//                "reflexion number", 
//                "express the number of reflexion taken into account for simulators") {
//                @Override
//                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
//                    return reflectionNumber;
//                }
//                @Override
//                public void setValue(Integer i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                    reflectionNumber = i;
//                }
////                @Override
////                public PropertyEditor getPropertyEditor() {
////                    return ipe;
////                }
////                final IntegerPropertyEditor ipe = new IntegerPropertyEditor(reflectionNumber);
//            };
//            nbR.setName("reflexion number");
//            set.put(nbR);
//            
//            final Property<Integer> nbD = new PropertySupport.ReadWrite<Integer>("diffraction number", Integer.class, 
//                "diffraction number", 
//                "express the number of diffraction taken into account for simulators") {
//                @Override
//                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
//                    return diffractionNumber;
//                }
//                @Override
//                public void setValue(Integer arg0) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                    diffractionNumber = arg0;
//                }
//            };
//            nbD.setName("diffraction number");
//            set.put(nbD);
//            
//            final Property<Integer> nbT = new PropertySupport.ReadWrite<Integer>("transmission number", Integer.class, 
//                "transmission number", 
//                "express the number of transmission taken into account for simulators") {
//                @Override
//                public Integer getValue() throws IllegalAccessException, InvocationTargetException {
//                    return transmissionNumber;
//                }
//                @Override
//                public void setValue(Integer arg0) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                    transmissionNumber = arg0;
//                }
//            };
//            nbT.setName("transmission number");
//            set.put(nbT);           
//
//            sheet.put(set);
//        }
//        return simulationInProgress?null:sheet;
//    }
//
//    /**
//     *
//     */
//    @Override
//    public void SimulationStart() {
//        simulationInProgress=true;
//    }

    /**
     *
     */
//    @Override
//    public void SimulationEnded() {
//        simulationInProgress=false;
//    }
    
    
    // ADD this mainly for tunnel treatment ...
    private List<Primitive> userPrimitives = new ArrayList<Primitive>();
    /**
     * Provides the collection of Primitives that the user furnish to this scene.
     * This concerns the ones that have been read from user XML nodes.
     * @return the user Primitive list
     */
    public List<Primitive> getUserPrimitives () {
        return userPrimitives;
    }
    /**
     * Add to this scene instance a Primitive. This is not used in the main BIH,
     * so the treatment must be provided by users.
     * @param prim the primitive to add in this scene
     */
//    public void addUserPrimitive (Primitive prim) {
//        userPrimitives.add(prim);
//        Face[] primFaces = prim.getFaces();
//        if (primFaces != null) {
//            addFaces(primFaces);
//        }
//    }
    
//    private class ModeProperty extends PropertySupport.ReadWrite<SimulationMode> 
//    {
//        ModeProperty () {
//            super ("mode", SimulationMode.class, "Simulation Mode", "Simulation Mode");
//        }
//        
//        @Override
//        public SimulationMode getValue() 
//                throws IllegalAccessException, 
//                       InvocationTargetException {
//            return mode;
//        }

//        @Override
//        public PropertyEditor getPropertyEditor() {
//            return new SimulationModeEditor();
//        }
//
//        @Override
//        public void setValue(SimulationMode mode) 
//                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
//        {
//            switch (mode)
//            {
//                case SIMO:
//                    if (receivers==null || receivers.size()==0) {
//                return;
//            }
//                    break;
//                case MISO:
//                    if (transmitters==null || transmitters.size()==0) {
//                return;
//            }
//                    break;
//                case MIMO:
//                    if (transmitters==null || transmitters.size()==0) {
//                return;
//            }
//                    if (receivers==null || receivers.size()==0) {
//                return;
//            }
//                    break;
//                default: 
//                    break;
//            }
//            Scene.this.mode = mode;
//            changeMode();
//        }
//    }
}
