/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;
//import org.xlim.sic.ig.kernel.Edge;

/**
 *
 * @author Alwajeeh
 */
public class PixelMinMax {

    public int Xa;
    public int Ya;
    public int Xb;
    public int Yb;

 private double convert2GrilleX(Grille grille,double Xpoint){

        double x;
        x = (double) grille.Nb_C*(Xpoint-grille.Xmin) / grille.Largeur;

        if (x >=grille.Nb_C)
            x=grille.Nb_C-1;
        else if (x <0)
            x=0;

        return x;

    }

private double convert2GrilleY(Grille grille,double Ypoint){

        double y;
        y = (double) grille.Nb_L*(Ypoint-grille.Ymin) / grille.Hauteur;

        if (y >=grille.Nb_L)
            y=grille.Nb_L-1;
        else if (y <0)
            y=0;

        return y;

    }

public void Calcul_PixelMinMax(Grille grille,Edge_traited edge){


    double Temp=0;

    if (edge.getA().x <= edge.getB().x){
        Temp =this.convert2GrilleX(grille, edge.getA().x)-0.5;
        this.Xa= (int) Math.ceil(Temp);
        Temp =this.convert2GrilleX(grille, edge.getB().x)+0.5;
        this.Xb= (int) Math.floor(Temp);
        }
    else{ // Xa>Xb
        Temp =this.convert2GrilleX(grille, edge.getA().x)+0.5;
        this.Xa= (int) Math.floor(Temp);
        Temp =this.convert2GrilleX(grille, edge.getB().x)-0.5;
        this.Xb= (int) Math.ceil(Temp);
        }

     if (edge.getA().y <= edge.getB().y){
        Temp =this.convert2GrilleY(grille, edge.getA().y)-0.5;
        this.Ya= (int) Math.ceil(Temp);
        Temp =this.convert2GrilleY(grille, edge.getB().y)+0.5;
        this.Yb= (int) Math.floor(Temp);
        }
     else{ // Xa>Xb
        Temp =this.convert2GrilleY(grille, edge.getA().y)+0.5;
        this.Ya= (int) Math.floor(Temp);
        Temp =this.convert2GrilleY(grille, edge.getB().y)-0.5;
        this.Yb= (int) Math.ceil(Temp);
        }

    // Avoiding overflow
    if (this.Xa<0) this.Xa=0; else if (this.Xa>=grille.Nb_C) this.Xa= grille.Nb_C-1;
    if (this.Xb<0) this.Xb=0; else if (this.Xb>=grille.Nb_C) this.Xb= grille.Nb_C-1;

    if (this.Ya<0) this.Ya=0; else if (this.Ya>=grille.Nb_L) this.Ya= grille.Nb_L-1;
    if (this.Yb<0) this.Yb=0; else if (this.Yb>=grille.Nb_L) this.Yb= grille.Nb_L-1;

    //System.out.println(" Xa : "+Pmm.Xa+" Xb : " +Pmm.Xb+" Ya : " +Pmm.Ya+" Yb : "+ Pmm.Yb);

    }


}
