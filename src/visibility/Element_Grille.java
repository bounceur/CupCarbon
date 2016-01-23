
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.util.ArrayList;

/**
 *
 * @author Alwajeeh
 */
public class Element_Grille {
     
     public  ArrayList<Edge_traited> ListeArete;
     public int affichage; // seulemenet pour afficher la grille
     

     public Element_Grille(){
         ListeArete= new ArrayList<Edge_traited> ();
         affichage=0;
         
     }
}
