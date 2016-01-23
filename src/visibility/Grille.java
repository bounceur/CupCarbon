/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

/**
 *
 * @author Alwajeeh
 */
public class Grille  {

       public double Xmin,Ymin;
       public int Nb_L, Nb_C;
       public double Largeur, Hauteur;
       public int Pas;
       public Element_Grille element[][];//=new Element_Grille[][];


/**
 * Constructor a grid with respect to the scene with a fixed step of 5
 */
       public Grille(Scene scene){
           Xmin=scene.xmin;
           Ymin=scene.ymin;
           Largeur = Math.ceil(Math.abs(scene.xmax - scene.xmin));
           Hauteur = Math.ceil(Math.abs(scene.ymax - scene.ymin));
           Pas = 5;
           Nb_C = (int)((double)Largeur/Pas) + 2;
           Nb_L = (int)((double)Hauteur/Pas) + 2;

           element=new Element_Grille[Nb_C][Nb_L];
           // Intialization
           for( int i=0; i<Nb_C; i++)
             for( int j=0; j<Nb_L; j++)
                 if(element[i][j] == null)
                     element[i][j]= new Element_Grille();

       }

/**
*  Constructs an empty grid
*/
   public Grille(){   
       };

/**
 * Constructor a grid
 */
       public Grille(double xmin,double ymin,double l,double h){
           Xmin=xmin;
           Ymin=ymin;
           Largeur = l;
           Hauteur = h;
           Pas=5;
           Nb_C = (int)((double)Largeur/Pas) + 2;
           Nb_L = (int)((double)Hauteur/Pas) + 2;

           element=new Element_Grille[Nb_C][Nb_L];
           // Intialization
           for( int i=0; i<Nb_C; i++)
             for( int j=0; j<Nb_L; j++)
                 if(element[i][j] == null)
                     element[i][j]= new Element_Grille();
       }

/**
*  
*/
   public void Modifer_Pas(int pas) {

        this.Pas=pas;

        this.Nb_C = (int)((double)this.Largeur/this.Pas) + 2;
        this.Nb_L = (int)((double)this.Hauteur/this.Pas) + 2;
       
        this.element=new Element_Grille[this.Nb_C][this.Nb_L];
        // Intialization
        for( int i=0; i<this.Nb_C; i++)
           for( int j=0; j<this.Nb_L; j++)
                if(this.element[i][j] == null)
                   this.element[i][j]= new Element_Grille();
 
    }

  
public void Plonger_Arete(Edge_traited edge){

    int Pente_x=0;
    int Pente_y=0;

    double a=0.0;
    double b=0.0;
    double c=0.0;

    double Delta=0.0;
    double Alpha=0.0;
    double Beta=0.0;

    boolean Test_x  = false;
    boolean Test_y  = false;

    Pixel PCourant= new Pixel();    // Current pixel
    PixelMinMax Pmm= new PixelMinMax();
    Pmm.Calcul_PixelMinMax(this, edge);

    // The sign of the slope
    Pente_x = (Pmm.Xa <= Pmm.Xb) ? 1 : -1;
    Pente_y = (Pmm.Ya <= Pmm.Yb) ? 1 : -1;

    if (Pmm.Ya==Pmm.Yb||edge.getA().y==edge.getB().y){
        for (int i= Pmm.Xa; i!=Pmm.Xb; i+=Pente_x){
            if(!this.element[i][Pmm.Ya].ListeArete.contains(edge))
                this.element[i][Pmm.Ya].ListeArete.add(edge);
            this.element[i][Pmm.Ya].affichage=-1;
        }
        if(!this.element[Pmm.Xb][Pmm.Ya].ListeArete.contains(edge))
            this.element[Pmm.Xb][Pmm.Ya].ListeArete.add(edge);
       this.element[Pmm.Xb][Pmm.Ya].affichage=-1;
    }

    else if(Pmm.Xa == Pmm.Xb || edge.getA().x == edge.getB().x){
        for (int i= Pmm.Ya; i!=Pmm.Yb; i+=Pente_y){
            if(!this.element[Pmm.Xa][i].ListeArete.contains(edge))
                this.element[Pmm.Xa][i].ListeArete.add(edge);
            this.element[Pmm.Xa][i].affichage=-1;

        }
    if(!this.element[Pmm.Xa][Pmm.Yb].ListeArete.contains(edge))
        this.element[Pmm.Xa][Pmm.Yb].ListeArete.add(edge);

        this.element[Pmm.Xa][Pmm.Yb].affichage=-1;
    }

    else {
        PCourant.X=Pmm.Xa;
        PCourant.Y=Pmm.Ya;

        // Calculate a, b, c, alpha, Beta, et delta
        a=Calcul_a(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);
        b=Calcul_b(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);
        c=Calcul_c(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);

        Delta=Calcul_Delta((double)Pmm.Xa,(double) Pmm.Ya, a, b,c);
        Alpha=Calcul_Alpha(a,b);
        Beta=Calcul_Beta(a,b);

        //int i=0;
        while(Pente_x*PCourant.X < Pente_x*Pmm.Xb || Pente_y*PCourant.Y < Pente_y*Pmm.Yb){
            
            
            //System.out.println(" i : "+(i++)+" x "+PCourant.X+" y "+PCourant.Y);
            //System.out.println("a "+a+"b "+b+" c "+c+" Delta "+Delta+" Alpha "+Alpha+" Beta "+Beta);
            //System.out.println(" Pente_x "+Pente_x+" Pente_y "+Pente_y);
           
            if ( Pente_x * Pente_y > 0 ) {
                if (( Delta + ( a * Pente_x ) ) >= Alpha) Test_x = true; else Test_x = false;
                if (( Delta + ( b * Pente_y ) ) <= Beta)  Test_y = true; else Test_y = false;
            }
            else {
                if (( Delta + ( a * Pente_x ) ) <= Beta  ) Test_x = true; else Test_x = false;
                if (( Delta + ( b * Pente_y ) ) >= Alpha ) Test_y = true; else Test_y = false;
            }
            // On teste si le pixel de droite (ou gauche) contribue a la couverture
            if (Test_x) {
                // On teste si le pixel du dessus (ou dessous) contribue a la couverture
                if (Test_y) {
                    // Cas d'une 2-bulle
                    Delta += (a * Pente_x) + (b* Pente_y);
                    this.element[PCourant.X][PCourant.Y].ListeArete.add(edge);
                    this.element[PCourant.X][PCourant.Y].affichage=-1;

                    this.element[PCourant.X+Pente_x][PCourant.Y].ListeArete.add(edge);
                    this.element[PCourant.X+Pente_x][PCourant.Y].affichage=-1;

                    this.element[PCourant.X][PCourant.Y+Pente_y].ListeArete.add(edge);
                    this.element[PCourant.X][PCourant.Y+Pente_y].affichage=-1;

                    PCourant.X += Pente_x;
                    PCourant.Y += Pente_y;
                }
            else{ // Cas d'un déplacement en x
                Delta += ( a * Pente_x );
                this.element[PCourant.X][PCourant.Y].ListeArete.add(edge);
                this.element[PCourant.X][PCourant.Y].affichage=-1;
                PCourant.X += Pente_x;
                }
            }
            else{ // Cas d'un déplacement en y
                Delta += ( b * Pente_y );
                this.element[PCourant.X][PCourant.Y].ListeArete.add(edge);
                this.element[PCourant.X][PCourant.Y].affichage=-1;
                PCourant.Y += Pente_y;
                }
        }
        this.element[PCourant.X][PCourant.Y].ListeArete.add(edge);
        this.element[PCourant.X][PCourant.Y].affichage=-1;
    }

}

public static double Calcul_a( double Xa,double Ya,double Xb,double Yb){
        return Ya-Yb;
    }

public static double Calcul_b( double Xa,double Ya,double Xb,double Yb){
    return Xb-Xa;
    }

public static  double Calcul_c( double Xa,double Ya,double Xb,double Yb){
    return Xa*Yb-Ya*Xb;
    }

public static  double Calcul_Delta( double X, double Y, double a,double b,double c){
    return a*X+b*Y+c;
    }

public static  double Calcul_Alpha( double a,double b){
    return -(Math.abs(a)+Math.abs(b))/2.0;
    }

public static double Calcul_Beta( double a,double b){
    return (Math.abs(a)+Math.abs(b))/2.0;
    }


public void Affichage_grille_ListeArete_size()
{
       for (int jj=this.Nb_L-1; jj>=0; jj--){
         for (int ii=0; ii<this.Nb_C; ii++){
            System.out.print(this.element[ii][jj].ListeArete.size() +" ");
           }
          System.out.println();
        }
}

public void Affichage_grille()
{

       for (int jj=this.Nb_L-1; jj>=0; jj--){
         for (int ii=0; ii<this.Nb_C; ii++){
                System.out.print(this.element[ii][jj].affichage+" "); // premier element dans la liste --> 0
           }
          System.out.println();
        }
}

}

// New tiny class
class Pixel {
    int X;
    int Y;
}
