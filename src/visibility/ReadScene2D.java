/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Alwajeeh
 */
public class ReadScene2D{

  /*    Liste de toutes les arêtes dans la scène    */
  public static ArrayList<Edge_traited []> ListeAretes ;
  
  /*    Liste de tous les bâtiment dans la scène    */
  public ArrayList<Face> ListPolygones ;
  
  /*    Liste associante tous les sommets diffractants et l'arbre de zones     */
  public static ArrayList<PointArbre> ListePointArbre ;

  /*    taille de la grille (1D grille)    */
  public static final int TAILLE_POLYGRID = 250;

  /*    Liste de tous les bâtiment  qui se trouve dans une case de la grille */
  public static ArrayList<Face> [] PolyGrid ;
 
  /*    Liste de tous les points diffractants  dans la scène */
  public static ArrayList<Point3d> PointsDiffractants ;


/*****************************************************************************/
/* le constructeur de la classe                                              */
/* Ajouter les batiments dans la liste ListPolygone                          */
/* Trouver les Points diffractants                                           */
/* entree : scene                                                            */
/* sortie : -                                                                */
/*                                                                           */
/*****************************************************************************/

  public ReadScene2D(Scene scene){

	  ListeAretes = new ArrayList<Edge_traited[]>();
	  ListPolygones = new ArrayList<Face>();
	  ListePointArbre = new ArrayList<PointArbre>();
	  PolyGrid = new ArrayList[TAILLE_POLYGRID] ;
	  PointsDiffractants = new ArrayList<Point3d>();
	  
        Face[] tabFaces;
        Edge[] tabEdge;

        double Height = scene.groundHeight;
        tabFaces = scene.getTabFaces();

        for(int i=0; i<tabFaces.length; i++) {

            Face f = tabFaces[i];


            boolean isToit=true;
            boolean isSol=true;

            for (int j=0; j<f.sommets.length; j++){
                isToit = isToit && (f.sommets[j].z > Height);
                isSol = isSol && (f.sommets[j].z <= Height);
            }

            if (isToit || isSol ){

                PolygoneAjouter(f);

                if (isToit)
                    PointDiffractant(f); // touver les Points diffractants dans la scène

                tabEdge = f.getEdges();

                // Convert from Edge --> Edge traited
                Edge_traited[] tabEdge_t =new Edge_traited[tabEdge.length];

                for (int j=0; j<tabEdge.length; j++)
                    tabEdge_t[j]=Edge_traited.Edge2Edge_traited(tabEdge[j]);// Convert from Edge --> Edge_traited                

                ListeAretes.add(tabEdge_t);
            }
        }
  }


/*****************************************************************************/
/* Ajouter Polygone dans la liste ListPolygone                               */
/*                                                                           */
/* entree : Polygone    (Face)                                               */
/* sortie : -                                                                */
/*                                                                           */
/*****************************************************************************/
  private void PolygoneAjouter (Face f) {

      ListPolygones.add(f);

      int j;

      // Plonger les bâtiments dans la grille PolyGrid
      for (int i=0; i<f.sommets.length; i++){

          j = PGindice(f.sommets[i]);
          assert (j < TAILLE_POLYGRID);

          // Ajout les faces dans la liste de polygones
          if ( PolyGrid[j]==null)
                PolyGrid[j] = new ArrayList<Face>();

          if(!PolyGrid[j].contains(f))
            PolyGrid[j].add(f);

      }

  }

/*****************************************************************************/
/*                                                                           */
/* Methhode qui détermine si les sommets d'un bâtiment sont diffractants     */
/*  Si oui ajouter le sommet dans la liste PointsDiffractant                 */
/*  entree : Polygone    (Face)                                              */
/*  sortie : void                                                            */
/*                                                                           */
/*****************************************************************************/

  private void PointDiffractant (Face f) {

        Point3d A1 = new Point3d();
        Point3d B1 = new Point3d();
        Point3d A2 = new Point3d();
        Point3d B2 = new Point3d();
        Point3d test = new Point3d();

        Edge[] tabEdge;
        tabEdge= f.getEdges();

        for (int i=0; i<tabEdge.length; i++){

            A1.set(tabEdge[i].getA());
            B1.set(tabEdge[i].getB());
            A2.set(tabEdge[(i+1)%tabEdge.length].getA());
            B2.set(tabEdge[(i+1)%tabEdge.length].getB());

            assert(B1.equals(A2));

            test.set((A1.x+B2.x)/2D,(A1.y+B2.y)/2D,0D);
            /*    veifier si le sommet est diffractant   */
            /*    au bord de la scène --> nondiffractant   */
            if (B1.x!=Zone2d.fenetre[2].x && B1.x!=Zone2d.fenetre[0].x && B1.y!=Zone2d.fenetre[0].y && B1.y!=Zone2d.fenetre[2].y)
                /*    test point à l'intérieur --> le sommet est diffractant    */
                if (Arbre.InsidePoly(test,f)){

                    Vector3d U= new Vector3d ();
                    Vector3d V= new Vector3d ();
                    U.set(-(B1.x-A1.x ), -(B1.y-A1.y), 0D);
                    V.set((B2.x-A2.x), (B2.y-A2.y), 0D);

                    double U_length=U.length();
                    double V_length=V.length();

                    double dot = V.dot(U);

                    double invCosineVal= dot/(U_length*V_length);
                    double angle = Math.acos(invCosineVal)*(180/Math.PI);

                      Point3d P=new Point3d ();
                      P.set(B1);

                      
                           if(!PointsDiffractants.contains(P)){

                               // Ajout les points dans la liste de PointArbre
                               if (angle<170)
                                   {
                                      PointsDiffractants.add(P);
                                      PointArbre PA = new PointArbre ();
                                      PA.P.set(P);
                                      //PA.A = null;   // pas d'arbre pour l'instant
                                      ListePointArbre.add(PA);
                                   }
                            }
                           else
                                System.out.println("This diffraction point is shared between two buildings ");
                  }
             }
    }





 /*********************************************************************************
 *   Methode qui calcule le diedre de travail. Elle retourne F ou T.            *
 ********************************************************************************/

// public static boolean WhichDihedra (Point3d D, Point3d E, Point3d P, Zone2d_BPoint zbp) {
//
//    assert (D != null);
//    assert (E != null);
//    assert (P != null);
//    assert (zbp!= null);
//
//    /* Recherche du diedre de travail */
//    /* On calcule le vecteur ED */
//    Vector3d V = new Vector3d();
//    V.set(D);V.sub(E);      // V = D - E
//    V.normalize();
//
//    /* on calcule le vecteur DP */
//    Vector3d U = new Vector3d();
//    U.set(P);U.sub(D);      // U = P - D
//    U.normalize();
//
//    // regarde si P est gauche (-1) ou à droite (+1)... ou aligné (0)
//    int       signeR;
//    signeR = sign (V, U);
//    
//    // U dot V
//    double       dr;
//    dr     = U.dot(V);//(U.x*V.x) +(U.y*V.y); /* proche 1 => loin -- proche -1 => pres */
//
//    System.out.println("U "+ U +"V " +V +"sign  " +signeR +"dr  " +dr );
//
//    if (signeR == 0) { // cas alignement ... 2 solutions
//        if (dr < 0) { // cas vers E, donc Rear
//          zbp.dotR = dr;
//          zbp.Rear = new Point3d();
//          zbp.Rear.set(P);
//        }
//        else { // cas loin de E, donc Front
//          zbp.dotF = dr;
//          zbp.Front = new Point3d();
//          zbp.Front.set(P);
//        }
//    }
//    else if (signeR == 1) { // vers la droite ...
//    // stocke d'abord vers l'arrière, puis vers l'avant ...
//    if (zbp.Fright == null) { // premier ajout ...
//        zbp.Fright = new Point3d();
//        zbp.Fright.set(P);
//        zbp.dotFR = dr;
//    }
//    else {
//      if (dr > zbp.dotFR) {
//	if (zbp.Rright == null) { // deuxième ajout !!
//	  zbp.Rright = new Point3d();
//          zbp.Rright.set(zbp.Fright) ;
//	  zbp.dotRR = zbp.dotFR;
//	}
//	// dans tout les cas on modifie RightFront
//        
//	zbp.Fright.set(P);
//	zbp.dotFR = dr;
//      }
//      else { // 2 cas : insertion (on fait rien) ou modifi
//	if (zbp.Rright == null || zbp.dotRR > dr) { // modif !
//          zbp.Rright = new Point3d();
//	  zbp.Rright.set(P);
//	  zbp.dotRR = dr;
//	}
//      }
//    }
//  }
//    else { // cas vers la gauche
//    assert (signeR == -1);
//    // stocke d'abord vers l'arrière, puis vers l'avant ...
//    if (zbp.Fleft == null) { // premier ajout ...
//      zbp.Fleft = new Point3d();
//      zbp.Fleft.set(P);
//      zbp.dotFL = dr;
//    }
//    else {
//      if (dr > zbp.dotFL) {
//	if (zbp.Rleft == null) { // deuxième ajout !!
//
//          zbp.Rleft = new Point3d();
//	  zbp.Rleft.set(zbp.Fleft) ;
//	  zbp.dotRL = zbp.dotFL;
//	}
//	// dans tout les cas on modifie LefttFront
//        
//	zbp.Fleft.set(P);
//	zbp.dotFL = dr;
//      }
//      else { // 2 cas : insertion (on fait rien) ou modifi
//	if (zbp.Rleft == null || zbp.dotRL > dr) { // modif !
//
//            zbp.Rleft = new Point3d();
//            zbp.Rleft.set(P);
//	    zbp.dotRL = dr;
//	}
//      }
//    }
//  }
//
//if (true)
//    {
//    if (zbp.Rleft   != null) System.out.println("Rleft "+zbp.Rleft); else System.out.println("Rleft null");
//    if (zbp.Fleft   != null) System.out.println("ZBP.Fleft "+zbp.Fleft); else System.out.println("ZBP.Fleft null");
//    if (zbp.Front   != null) System.out.println("ZBP.Front "+zbp.Front); else System.out.println("ZBP.Front null");
//    if (zbp.Rright    != null) System.out.println("ZBP.Rright  "+zbp.Rright ); else System.out.println("ZBP.Rright  null");
//    if (zbp.Fright    != null) System.out.println("ZBP.Fright  "+zbp.Fright ); else System.out.println("ZBP.Fright  null");
//    if (zbp.Rear    != null) System.out.println("ZBP.Rear  "+zbp.Rear ); else System.out.println("Rear  null");
//
//    System.out.println("ZBP.dotRL  "+zbp.dotRL );
//    System.out.println("ZBP.dotFL  "+zbp.dotFL );
//    System.out.println("ZBP.dotF  "+zbp.dotF );
//    System.out.println("ZBP.dotFR  "+zbp.dotFR );
//    System.out.println("ZBP.dotR  "+zbp.dotR );
//    System.out.println("ZBP.dotRR  "+zbp.dotRR );
//    }
//  return true;
//
//  }


/*********************************************************************************
 *   Methode qui retourne 1 si la face est haute, -1 sinon                       *
 ********************************************************************************/
 public static int sign (Vector3d P, Vector3d V) {

    double a;
    a =P.x*V.y-P.y*V.x; // P x V cross product

    return a < -10E-9 ? 1 : a > 10E-9 ? -1 : 0;
    }

  public static int sign (Point3d P, Point3d V) {

    double a;
    a =P.x*V.y-P.y*V.x; // P x V cross product

    return a < -10E-9 ? 1 : a > 10E-9 ? -1 : 0;
    }



/*********************************************************************************
 *   Methode qui retourne l'indice discrétisé du point dans une grille 1D        *
 ********************************************************************************/

   public static int PGindice (Vector3d P) {

    int indice;
    double Xmax= Zone2d.fenetre[0].x;
    double Xmin= Zone2d.fenetre[2].x;

    indice = (int) ((double)TAILLE_POLYGRID*(P.x - Xmin) / (Xmax-Xmin));

    if (indice >= TAILLE_POLYGRID)
        indice = TAILLE_POLYGRID-1;
    else if (indice < 0)
        indice = 0;

    return indice;
}

public static int PGindice (Point3d P) {

    int indice;
    double Xmax= Zone2d.fenetre[0].x;
    double Xmin= Zone2d.fenetre[2].x;

    indice = (int) ((double)TAILLE_POLYGRID*(P.x - Xmin) / (Xmax-Xmin));

    if (indice >= TAILLE_POLYGRID)
        indice = TAILLE_POLYGRID-1;
    else if (indice < 0)
        indice = 0;

    return indice;
}


}




