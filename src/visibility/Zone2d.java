/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.util.ArrayList;
import java.util.Arrays;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Alwajeeh
 */
public class Zone2d implements Cloneable {
	public Point3d[] sommets = new Point3d[4];
	public Point3d source = new Point3d();
	public Edge_traited support;
	public boolean valide;
	public int type;

	/* localisation du point source par rapport a la zone 2D */
	private static final int XMIN_YMIN = 1;
	private static final int XMIN = 2;
	private static final int XMIN_YMAX = 3;
	private static final int YMIN = 4;
	private static final int CENTER = 5;
	private static final int YMAX = 6;
	private static final int XMAX_YMIN = 7;
	private static final int XMAX = 8;
	private static final int XMAX_YMAX = 9;

	/* permet de connaitre la nature de l'illumination de la zone */
	public static final int VISIBLE = 0;
	public static final int REFLEXION = 1;
	public static final int DIFFRACTION = 2;
	public static final int TRANSMISSION = 3;
	public static final int INVISIBLE = 4;

	/* type de la zone */
	private static final int TRIANGLE = 3;
	private static final int QUADRILATERE = 4;

	private static int Num_Zone = 0;
	private static final double EPS = 1E-7;
	public static final Point3d[] fenetre = new Point3d[4];
	public static Grille grille = new Grille();
	public static boolean PRINT = false; // Affichage des valeurs

	public Zone2d(Point3d P0, Point3d P1, Point3d P2, Point3d P3, Point3d s, int t, Edge_traited edge) {

		sommets[0] = new Point3d();
		sommets[0].set(P0);
		sommets[1] = new Point3d();
		sommets[1].set(P1);
		sommets[2] = new Point3d();
		sommets[2].set(P2);
		sommets[3] = new Point3d();
		sommets[3].set(P3);
		source = new Point3d();
		source.set(s);
		type = t;
		valide = true;

		if (edge != null)
			support = new Edge_traited(edge.getA(), edge.getB(), edge.getFace());
		else
			support = null;
	}

	public Zone2d() {
		// Empty constructor
	}

	public void setZone2d(Zone2d zone) {

		this.sommets[0] = new Point3d();
		this.sommets[0].set(zone.sommets[0]);
		this.sommets[1] = new Point3d();
		this.sommets[1].set(zone.sommets[1]);
		this.sommets[2] = new Point3d();
		this.sommets[2].set(zone.sommets[2]);
		this.sommets[3] = new Point3d();
		this.sommets[3].set(zone.sommets[3]);
		this.source = new Point3d();
		source.set(zone.source);
		this.type = zone.type;

		if (zone.support != null)
			this.support = new Edge_traited(zone.support.getA(), zone.support.getB(), zone.support.getFace());
		else
			this.support = null;

		this.valide = ZoneValide(zone);
	}

	public void Calcul_Zone2d(NbrInteractions NbI, Scene scene) {

		fenetre[0] = new Point3d();
		fenetre[1] = new Point3d();
		fenetre[2] = new Point3d();
		fenetre[3] = new Point3d();

		// z=0 --> 2D
		fenetre[0].set(scene.xmax, scene.ymax, 0.0);
		fenetre[1].set(scene.xmax, scene.ymin, 0.0);
		fenetre[2].set(scene.xmin, scene.ymin, 0.0);
		fenetre[3].set(scene.xmin, scene.ymax, 0.0);

		Point3d L_source = new Point3d(); // source locale
		L_source.set(scene.transmitter.getPosition());
		L_source.z = 0.0;

		Zone2d zone = new Zone2d(fenetre[0], fenetre[1], fenetre[2], fenetre[3], L_source, INVISIBLE, null); // 4
																												// =>
																												// Non-Visible

		/* une liste contenante toutes les zones visibles */
		ArrayList<Zone2d> ListeZone2d = new ArrayList<Zone2d>();
		ListeZone2d.add(zone);

		Arbre ArbreDeVisibilite = new Arbre();
		ArbreDeVisibilite.setElementArbre(zone);

		// Premier niveau
		for (int i = 0; i < 4; i++) {
			zone = new Zone2d(L_source, fenetre[i], fenetre[(i + 1) % 4], L_source, L_source, VISIBLE, null); // 0
																												// =>
																												// Visible
			Zone2DAjouter(zone, ListeZone2d);
		}

		// Extraire la scène en 2D
		ReadScene2D Scene2D = new ReadScene2D(scene);
		ArrayList<Edge_traited []> ListeAretes2d = new ArrayList<Edge_traited []>(ReadScene2D.ListeAretes);

		grille = new Grille(scene);
		// grille.Modifer_Pas(1);// change step size

		/* Plonger les arêtes dans une grille regulière */
		for (int j = 0; j < ListeAretes2d.size(); j++)
			for (int i = 0; i < ListeAretes2d.get(j).length; i++)
				grille.Plonger_Arete(ListeAretes2d.get(j)[i]);

		/* Affichage des arêtes plongées dans la grille */
		if (PRINT) {
			System.out.println("Affichage des arêtes plongées dans la grille regulière");
			grille.Affichage_grille_ListeArete_size();
			System.out.println();
		}

		/*
		 * Decoupage du premier niveau selon la géométrie pour avoir toutes les
		 * zones en visibilité directe
		 */
		zone = ListeZone2d.get(0);
		ListeZone2d.remove(0); // pas besoin de decouper la zone pour toute la
								// scène
		analyseScene(grille, ListeZone2d);
		ListeZone2d.add(0, zone); // rajouter la permière zone

		if (PRINT) // affichage des zones en visibilité directe (premier niveau
					// de l'arbre)
			for (int i = 0; i < ListeZone2d.size(); i++) {
				System.out.print("zone" + i + "=[");
				for (int j = 0; j < Type_De_Zone2D(ListeZone2d.get(i)); j++) {
					System.out.print(ListeZone2d.get(i).sommets[j]);
					if (j != Type_De_Zone2D(ListeZone2d.get(i)) - 1)
						System.out.print(" ; ");
				}
				System.out.print("]");
				System.out.println();
			}

		for (int i = 1; i < ListeZone2d.size(); i++) // la première zone est le
														// père --> i=1
			if (ZoneValide(ListeZone2d.get(i)))
				ArbreDeVisibilite.ArbreAjouter(ListeZone2d.get(i));

//		if (true) {
//			// Affichage
//			Zone2d test = new Zone2d();
//			for (int i = 0; i < ListeZone2d.size(); i++) {
//				System.out.print("zone" + i + "=[");
//				test = ListeZone2d.get(i);
//
//				Building zoneGeo = new Building(Zone2d.Type_De_Zone2D(test));
//				for (int j = 0; j < Zone2d.Type_De_Zone2D(test); j++) {
//					System.out.print(test.sommets[j]);
//					zoneGeo.set(test.sommets[j].x, test.sommets[j].y, j);
//					if (j != Zone2d.Type_De_Zone2D(test) - 1) {
//						System.out.print(" ; ");
//					}
//				}
//				BuildingList.add(zoneGeo);
//				System.out.print("]; ");
//				System.out.print(" type" + i + "=[");
//				Zone2d.TypeInteraction(test.type);
//				System.out.print("]; ");
//				System.out.println();
//			}
//		}

		ArbreDeVisibilite.Calculer_Arbre(NbI);

		// Arbre.ArbreDiffractionPreCalcul();

	}

	/*****************************************************************************/
	/*                                                                           */
	/*
	 * traitement de la scene, on prend les aretes une par une et on les plonge
	 */
	/* successivement dans toute les zones concernees */
	/* entree : une grille qui contient une liste des aretes plongés */
	/* liste des zones est une variable globle */
	/* sortie : liste des zones visibles par la source */
	/*                                                                           */
	/*****************************************************************************/

	public static void analyseScene(Grille grille, ArrayList<Zone2d> ListeZone2d) {

		// nouvelle grille pour l'affichage seulement
		// Grille grilleAffichage = new Grille
		// (grille.Xmin,grille.Ymin,grille.Largeur,grille.Hauteur);

		for (int i = 0; i < ListeZone2d.size(); i++) {
			Num_Zone++;
			int no = 1;

			// if (PRINT){
			// System.out.println("Intially Zone: # "+ (i));
			//
			// // System.out.println("Zone validation: "+
			// ZoneValide(ListeZone2d.get(i)));
			// }
			//
			// //Building zone = new
			// Building(Type_De_Zone2D(ListeZone2d.get(i)));
			// for (int j=0; j<Type_De_Zone2D(ListeZone2d.get(i)); j++){
			// System.out.print(ListeZone2d.get(i).sommets[j]);
			// //zone.set(ListeZone2d.get(i).sommets[j].x,
			// ListeZone2d.get(i).sommets[j].y, j);
			// if (j!=Type_De_Zone2D(ListeZone2d.get(i))-1)
			// System.out.print(" ; ");
			// }
			// //BuildingList.add(zone);
			// System.out.print("]; ");
			// System.out.print(" type"+ i+"=[");
			// Zone2d.TypeInteraction(ListeZone2d.get(i).type);
			// System.out.print("]; ");
			// System.out.println();

			if (ZoneValide(ListeZone2d.get(i))) // Aller sur la liste des zones
				for (;;) {

					// nouvelle verification car la zone pourrait avoir été
					// modifié
					if (!(ListeZone2d.get(i).valide))
						break;

					Tab_Min_Max T = new Tab_Min_Max();
					T = Plonger_Zone(ListeZone2d.get(i), grille);

					// if (PRINT)
					// Affichage_Tableau_Min_Max_Grille(T,grilleAffichage
					// ,Num_Zone);// Affichage

					// recherche de l'arete la plus proche
					ArrayList<Edge_traited> A = new ArrayList<Edge_traited>();

					A = Aspirateur_Zone(T, grille, Num_Zone);
					// grille.Affichage_grille_traite();System.out.println();
					//
					if (PRINT)
						if (A != null)
							System.out.println("Not  NULL ");
						else
							System.out.println("is NULL ");

					if (A == null)
						break; // si toutes les zones ont été divisées => pas de
								// nouvelle zone => sortir

					int index[] = new int[A.size()];
					if (A.size() > 1) {

						double dist[] = new double[A.size()];
						double temp[] = new double[A.size()];

						Vector3d distA = new Vector3d();
						Vector3d distB = new Vector3d();

						for (int m = 0; m < A.size(); m++) {
							distA.set(ListeZone2d.get(i).source);
							distA.sub(A.get(m).A);
							distB.set(ListeZone2d.get(i).source);
							distB.sub(A.get(m).B);
							dist[m] = distA.length() + distB.length();
						}
						System.arraycopy(dist, 0, temp, 0, A.size());

						Arrays.sort(dist);
						for (int m = 0; m < A.size(); m++) {
							for (int m2 = 0; m2 < A.size(); m2++) {
								if (temp[m] == dist[m2])
									index[m] = m2;
							}
						}

					} else
						index[0] = 0; // seulement une arête

					// decoupage de la zone à neaveua si on trouve une arête
					// dans la zone
					if (A != null) // analyseSceneAux(A.get(0),ListeZone2d.get(i),ListeZone2d);
									// //
						for (int j = 0; j < A.size(); j++) {
							// System.out.println(" arrete no "+j +" A " +
							// A.get(j).A +" B " + A.get(j).B);
							analyseSceneAux(A.get(index[j]), ListeZone2d.get(i), ListeZone2d);

						}
					if (PRINT)
						System.out.println(" number of times " + (no++) + " size of the list " + ListeZone2d.size());

				}

		}

	}

	public static void analyseSceneAux(Edge_traited A, Zone2d zone, ArrayList<Zone2d> ListeZone2d) {

		assert (A != null);
		double[] t, v;

		Point3d AA = new Point3d();
		Point3d AB = new Point3d();
		AA.set(A.getA());
		AA.z = (0);
		AB.set(A.getB());
		AB.z = (0);

		Point3d S0 = new Point3d();
		S0.set(zone.sommets[0].x, zone.sommets[0].y, 0D);
		Point3d S1 = new Point3d();
		S1.set(zone.sommets[1].x, zone.sommets[1].y, 0D);
		Point3d S2 = new Point3d();
		S2.set(zone.sommets[2].x, zone.sommets[2].y, 0D);
		Point3d S3 = new Point3d();
		S3.set(zone.sommets[3].x, zone.sommets[3].y, 0D);
		Point3d Szone = new Point3d();
		Szone.set(zone.source.x, zone.source.y, 0D);

		if (!(S0.equals(S3))) { // !=TRAINGLE --> = QUADRILETERE
			t = intersection(S0, S3, AA, AB);
			if ((t[0] == 10D && t[1] == 10D)) {
				// arete parallele ... l'arête est-elle du bon côté pour être
				// dedans ??
				t = intersection(S0, S3, Szone, AA);
				if (t[1] - EPS <= -1)
					return; // intersection après A, donc S[0]-S[3] plus loin
							// !!!
			}
		}

		if (!(S1.equals(S2))) {
			t = intersection(S1, S2, AA, AB);
			if ((t[0] == 10D && t[1] == 10D)) {
				// arete parallele ... l'arête est-elle du bon côté pour être
				// dedans ??
				t = intersection(S1, S2, Szone, AA);
				if (t[1] < 0 && t[1] >= -1D)
					return; // intersection avant A, donc S[1]-S[2] plus proche
							// !!!
			}
		}

		if (ZoneValide(zone)) {

			if (coupeArete(AA, AB, zone)) {

				// on teste l'intersection entre [ S1 ; S2 ] et [ O ; A ]
				t = intersection(S1, S2, Szone, AA);
				if (t[0] == 10D && t[1] == 10D)
					return;

				// on teste l'intersection entre [ S1 ; S2 ] et [ O ; B ]
				v = intersection(S1, S2, Szone, AB);
				if (v[0] == 10D && v[1] == 10D)
					return;

				if (t[0] < -1D)
					t[0] = -1D;
				else if (t[0] > 0D)
					t[0] = 0D;
				if (v[0] < -1D)
					v[0] = -1D;
				else if (v[0] > 0D)
					v[0] = 0D;

				// on regarde si l'orientation de l'arete est correcte ...
				double siam1 = SIAM(Szone, S1, S2);
				double siam2 = SIAM(Szone, AA, AB); // !!!

				// sinon on echange les points A et B
				if (siam1 * siam2 < 0D) {
					double tt;

					EchangePoint(AA, AB);
					tt = t[0];
					t[0] = v[0];
					v[0] = tt;
				}

				if (PTEGAUX(AA, S1) && PTEGAUX(AB, S2))
					return;

				// but : eviter que les arêtes sur un bord ne créent des pbs :

				Point3d Pz1 = calc_point_2d(t[0], S1, S2);
				Point3d Pz2 = calc_point_2d(v[0], S1, S2);

				boolean b1 = COMP(Pz1, S2);
				boolean b2 = COMP(Pz2, S1);

				boolean b3 = COMP(Pz1, S1);
				boolean b4 = COMP(Pz2, S2);

				if (b1 && b2 || b3 && b4) {
					zone.sommets[1].set(AA);
					zone.sommets[2].set(AB);
					zone.support = new Edge_traited(A.getA(), A.getB(), A.getFace());

					return; // arete clipant complètement z
				}
				if (b1 || b2)
					return; // on est sur un bord unique !

				// attention au cas ou l'arete AB est plus ou moins invisible !!
				double w[] = intersection(Szone, AA, Szone, AB);
				if (w[0] == 10. && w[1] == 10.)
					return;

				boolean zIsTriangle = PTEGAUX(S0, S3);

				boolean modifPz0 = false;
				boolean modifPz3 = false;

				// creation d'une nouvelle zone ... celle de gauche
				Point3d Pz0 = new Point3d();
				Point3d Pz = calc_point_2d(t[0], S1, S2);

				if (!COMP_EPS(S1, Pz)) {
					if (zIsTriangle)
						Pz0.set(Szone);
					else {
						double tt[];
						// On teste l'intersection entre [ S0 ; S3 ] et [ O ; A
						// ]
						tt = intersection(S0, S3, Szone, AA);
						Pz0 = calc_point_2d(tt[0], S0, S3);
					}

					Zone2d nouvelle = new Zone2d(S0, S1, Pz, Pz0, Szone, zone.type, zone.support);

					if (egalZone2D(zone, nouvelle))
						return; // on risque de boucler infiniment

					// Ajout nouvelle zone to liste
					Zone2DAjouter(nouvelle, ListeZone2d);
					modifPz0 = true;
				}

				// creation d'une nouvelle zone ... celle de droite
				Point3d Pz3 = new Point3d();
				Pz = calc_point_2d(v[0], S1, S2);
				if (!COMP_EPS(S2, Pz)) {
					if (zIsTriangle)
						Pz3.set(Szone);
					else {
						double tt[];
						// On teste l'intersection entre [ S0 ; S3 ] et [ O ; B
						// ]
						tt = intersection(S0, S3, Szone, AB);
						Pz3 = calc_point_2d(tt[0], S0, S3);
					}

					Zone2d nouvelle = new Zone2d(Pz3, Pz, S2, S3, Szone, zone.type, zone.support);
					if (egalZone2D(zone, nouvelle))
						return; // on risque de boucler infiniment

					// Ajout nouvelle zone to liste
					Zone2DAjouter(nouvelle, ListeZone2d);
					modifPz3 = true;
				}

				if (modifPz0)
					zone.sommets[0].set(Pz0);
				zone.sommets[1].set(AA);

				if (modifPz3)
					zone.sommets[3].set(Pz3);
				zone.sommets[2].set(AB);
				zone.support = A;
			}
		}

	}

	/**
	 * Methode qui plonge une zone dans une grille reguliere
	 * 
	 * @param grille
	 *            : La grille dans laquelle on veut plonger la zone
	 * @param zone
	 *            : La zone a plonger
	 * @return Le tableau de min et de max
	 **/
	public static Tab_Min_Max Plonger_Zone(Zone2d zone, Grille grille) {

		int ZPs = -1;
		int Max_Sommets = 0;

		// Determination de la zone dans lequel se trouve le point source
		ZPs = Calcul_Zone_Point_Source(zone);

		// On determine le nombre de sommets max de la zone
		Max_Sommets = Type_De_Zone2D(zone);

		// Creation du tableau
		Tab_Min_Max T = new Tab_Min_Max();

		// On determine la taille du tableau en fonction du sens
		T = Taille_Tableau(grille, T, zone, ZPs);

		// On plonge chaque zone et on cree le tableau de min max
		for (int i = 0; i < Max_Sommets; i++)
			T = Plonger_Arete_Zone(grille, T, zone.sommets[i], zone.sommets[(i + 1) % Max_Sommets], ZPs);

		// Verification du tableau on enleve les -1
		// Check if you still need it

		return T;

	}

	public static int Calcul_Zone_Point_Source(Zone2d zone) {

		double Sx = zone.source.x;
		double Sy = zone.source.y;

		// Variables de test
		double Vx = 0.0;
		double Vy = 0.0;

		int ZPs = -1;

		int Max_Sommets = Type_De_Zone2D(zone);

		// on teste le type de la zone et on calcule les coefs
		for (int i = 0; i < Max_Sommets; i++) {
			Vx += (Sx > zone.sommets[i].x) ? 1.0 : (Sx < zone.sommets[i].x) ? -1.0 : 0.0;
			Vy += (Sy > zone.sommets[i].y) ? 1.0 : (Sy < zone.sommets[i].y) ? -1.0 : 0.0;
		}

		Vx = (Vx > 0.0) ? 1.0 : (Vx < 0.0) ? -1.0 : 0.0;
		Vy = (Vy > 0.0) ? 1.0 : (Vy < 0.0) ? -1.0 : 0.0;

		// On localise le point source
		switch ((int) Vx) {
		case 0: {
			switch ((int) Vy) {
			case 0: {
				ZPs = CENTER;
				break;
			}
			case 1: {
				ZPs = YMAX;
				break;
			}
			case -1: {
				ZPs = YMIN;
				break;
			}
			default:
				System.out.println("ERREUR : La zone du point source ne peut etre trouvée...");
			}
			break;
		}
		case 1: {
			switch ((int) Vy) {
			case 0: {
				ZPs = XMAX;
				break;
			}
			case 1: {
				ZPs = XMAX_YMAX;
				break;
			}
			case -1: {
				ZPs = XMAX_YMIN;
				break;
			}
			default:
				System.out.println("ERREUR : La zone du point source ne peut etre trouvée...");
			}
			break;
		}
		case -1: {
			switch ((int) Vy) {
			case 0: {
				ZPs = XMIN;
				break;
			}
			case 1: {
				ZPs = XMIN_YMAX;
				break;
			}
			case -1: {
				ZPs = XMIN_YMIN;
				break;
			}
			default:
				System.out.println("ERREUR : La zone du point source ne peut etre trouvée...");
			}
			break;
		}
		default:
			System.out.println("ERREUR : La zone du point source ne peut etre trouvée...");
		}

		return (ZPs);
	}

	/******************************************************************************/
	/*                                                                            */
	/*
	 * Methode qui indique quel est le type de zone 2D et donc le nombre de
	 * sommet
	 */
	/* entrée zone La zone dont on souhaite avoir le type. */
	/* sortie Un entier : 3 si c'est un TRIANGLE, 4 si c'est un QUADRILATERE */
	/*                                                                            */
	/******************************************************************************/
	public static int Type_De_Zone2D(Zone2d zone) {

		// On teste le premier et le dernier point de la zone pour savoir si ils
		// sont egaux
		// si c'est le cas alors on a un triangle
		// sinon c'est un quadrilatere

		int Nb_Sommet;
		if ((zone.sommets[0].x == zone.sommets[3].x) && (zone.sommets[0].y == zone.sommets[3].y))
			Nb_Sommet = TRIANGLE;
		else
			Nb_Sommet = QUADRILATERE;

		return Nb_Sommet;
	}

	/**
	 * Methode qui plonge une zone dans une grille reguliere
	 * 
	 * @param Grille
	 *            : La grille dans laquelle on veut plonger la zone
	 * @param T
	 *            : Le tableau de Min Max a modifier
	 * @param Zone
	 *            : La zone a plonger
	 * @param ZPs
	 *            : La localisation du point source
	 * @return Le tableau de min et de max
	 **/
	public static Tab_Min_Max Taille_Tableau(Grille grille, Tab_Min_Max T, Zone2d zone, int ZPs) {

		// On determine le nombre de sommets max de la zone
		int Max_Sommets = Type_De_Zone2D(zone);

		// Just to use the intialize the Edge
		Vector3d[] vecT = new Vector3d[3];
		vecT[0] = new Vector3d(0, 0, 0);
		vecT[1] = new Vector3d(1, 1, 0);
		vecT[2] = new Vector3d(2, 0, 0);

		Brdf brdf = new Brdf() {

			@Override
			public void reflect(Vector3d vctrd, Vector3d vctrd1, Vector3d vctrd2, Vector3d vctrd3, double[] doubles,
					Field[] fs) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void transmit(Vector3d vctrd, Vector3d vctrd1, Vector3d vctrd2, Vector3d vctrd3, double[] doubles,
					Field[] fs) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};

		Face f = new Face(brdf, vecT);
		Edge_traited arete = new Edge_traited(vecT[0], vecT[1], f);

		// On plonge on plonge chaque sommets de la zone
		PixelMinMax[] Pmm = new PixelMinMax[Max_Sommets];

		for (int i = 0; i < Max_Sommets; i++) {
			arete.getA().x = zone.sommets[i].x;
			arete.getA().y = zone.sommets[i].y;
			arete.getB().x = (zone.sommets[(i + 1) % Max_Sommets].x);
			arete.getB().y = (zone.sommets[(i + 1) % Max_Sommets].y);
			// arete.getA().x=(zone.sommets[i].x);
			// arete.getA().y=(zone.sommets[i].y);
			// arete.getB().x=(zone.sommets[(i+1)%Max_Sommets].x);
			// arete.getB().y=(zone.sommets[(i+1)%Max_Sommets].y);

			Pmm[i] = new PixelMinMax();
			Pmm[i].Calcul_PixelMinMax(grille, arete); // tested ok
		}

		// Recherche de la taille du tableau

		int Ymin = grille.Nb_L + 1;
		int Ymax = -1;
		int Xmin = grille.Nb_C + 1;
		int Xmax = -1;

		for (int i = 0; i < Max_Sommets; i++) {
			if (Xmin > Pmm[i].Xa)
				Xmin = Pmm[i].Xa;
			if (Xmin > Pmm[i].Xb)
				Xmin = Pmm[i].Xb;

			if (Xmax < Pmm[i].Xa)
				Xmax = Pmm[i].Xa;
			if (Xmax < Pmm[i].Xb)
				Xmax = Pmm[i].Xb;

			if (Ymin > Pmm[i].Ya)
				Ymin = Pmm[i].Ya;
			if (Ymin > Pmm[i].Yb)
				Ymin = Pmm[i].Yb;

			if (Ymax < Pmm[i].Ya)
				Ymax = Pmm[i].Ya;
			if (Ymax < Pmm[i].Yb)
				Ymax = Pmm[i].Yb;
		}

		if ((ZPs == YMIN) || (ZPs == YMAX)) {
			// On met a jour les variables min et max
			T.Min = (Ymin < Ymax) ? Ymin : Ymax;
			T.Max = (Ymin > Ymax) ? Ymin : Ymax;
		} else {
			// On met a jour les variables min et max
			T.Min = (Xmin < Xmax) ? Xmin : Xmax;
			T.Max = (Xmin > Xmax) ? Xmin : Xmax;
		}

		T.Taille_Tab = Math.abs(T.Max - T.Min) + 1;

		// On l'initialise
		T.Element = new Elt_Tab_Min_Max[T.Taille_Tab];

		for (int i = 0; i < T.Taille_Tab; i++) {
			T.Element[i] = new Elt_Tab_Min_Max();
			T.Element[i].Min = T.Element[i].Max = -1;
		}
		// Tested OK

		return T;

	}
	// Une fonction qui plonge les aretes d'une zone

	public static Tab_Min_Max Plonger_Arete_Zone(Grille grille, Tab_Min_Max T, Point3d Pa, Point3d Pb,
			int Sens_traitement) {

		int Pente_x = 0;
		int Pente_y = 0;

		double a = 0.0;
		double b = 0.0;
		double c = 0.0;

		double Delta = 0.0;
		double Alpha = 0.0;
		double Beta = 0.0;

		boolean Test_x = false;
		boolean Test_y = false;

		// Current pixel
		Pixel PCourant = new Pixel();
		PixelMinMax Pmm = new PixelMinMax();

		// Just to use the intialize the Edge
		Vector3d[] vecT = new Vector3d[3];
		vecT[0] = new Vector3d(0, 0, 0);
		vecT[1] = new Vector3d(1, 1, 0);
		vecT[2] = new Vector3d(2, 0, 0);

		Brdf brdf = new Brdf() {

			@Override
			public void reflect(Vector3d vctrd, Vector3d vctrd1, Vector3d vctrd2, Vector3d vctrd3, double[] doubles,
					Field[] fs) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void transmit(Vector3d vctrd, Vector3d vctrd1, Vector3d vctrd2, Vector3d vctrd3, double[] doubles,
					Field[] fs) throws ExceptionWavelengthOutOfScope, ExceptionArrayLengths {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};

		Face f = new Face(brdf, vecT);
		Edge_traited arete = new Edge_traited(vecT[0], vecT[1], f);

		arete.getA().x = (Pa.x);
		arete.getA().y = (Pa.y);
		arete.getB().x = (Pb.x);
		arete.getB().y = (Pb.y);

		// On calcule les cordonnées des pixels de début et de fin du segment
		Pmm.Calcul_PixelMinMax(grille, arete);

		if (Sens_traitement == YMIN || Sens_traitement == YMAX)
			assert (Math.abs(Pmm.Ya - Pmm.Yb) <= T.Max);
		else
			assert (Math.abs(Pmm.Xa - Pmm.Xb) <= T.Max);

		// The sign of the slope
		Pente_x = (Pmm.Xa <= Pmm.Xb) ? 1 : -1;
		Pente_y = (Pmm.Ya <= Pmm.Yb) ? 1 : -1;

		int i = 0;
		// Si les deux pixels sont alignés horizontalement
		if (Pmm.Ya == Pmm.Yb || arete.getA().y == arete.getB().y) {
			i = Pmm.Xa - Pente_x;
			do {
				i += Pente_x;
				T = MAJ_Tableau(T, i, Pmm.Ya, Sens_traitement);
			} while (i != Pmm.Xb);
		} else if (Pmm.Xa == Pmm.Xb || arete.getA().x == arete.getB().x) {
			// Si les deux pixels sont alignés verticalement
			i = Pmm.Ya - Pente_y;
			do {
				i += Pente_y;
				T = MAJ_Tableau(T, Pmm.Xa, i, Sens_traitement);
			} while (i != Pmm.Yb);
		} else {
			// On initialise le pixel courant avec le pixel de debut
			PCourant.X = Pmm.Xa;
			PCourant.Y = Pmm.Ya;

			// On calcule les vaiables a, b, alpha, beta et delta
			a = Grille.Calcul_a(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);
			b = Grille.Calcul_b(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);
			c = Grille.Calcul_c(Pmm.Xa, Pmm.Ya, Pmm.Xb, Pmm.Yb);

			Delta = Grille.Calcul_Delta((double) Pmm.Xa, (double) Pmm.Ya, a, b, c);
			Alpha = Grille.Calcul_Alpha(a, b);
			Beta = Grille.Calcul_Beta(a, b);

			while (Pente_x * PCourant.X < Pente_x * Pmm.Xb || Pente_y * PCourant.Y < Pente_y * Pmm.Yb) {

				// Calcul des inequations en fonction de la pente
				if (Pente_x * Pente_y > 0) {
					if ((Delta + (a * Pente_x)) >= Alpha)
						Test_x = true;
					else
						Test_x = false;
					if ((Delta + (b * Pente_y)) <= Beta)
						Test_y = true;
					else
						Test_y = false;
				} else {
					if ((Delta + (a * Pente_x)) <= Beta)
						Test_x = true;
					else
						Test_x = false;
					if ((Delta + (b * Pente_y)) >= Alpha)
						Test_y = true;
					else
						Test_y = false;
				}
				// On teste si le pixel de droite (ou gauche) contribue a la
				// couverture
				if (Test_x) {
					// On teste si le pixel du dessus (ou dessous) contribue a
					// la couverture
					if (Test_y) {
						// Cas d'une 2-bulle
						Delta += (a * Pente_x) + (b * Pente_y);
						T = MAJ_Tableau(T, PCourant.X, PCourant.Y, Sens_traitement);
						T = MAJ_Tableau(T, PCourant.X + Pente_x, PCourant.Y, Sens_traitement);
						T = MAJ_Tableau(T, PCourant.X, PCourant.Y + Pente_y, Sens_traitement);

						PCourant.X += Pente_x;
						PCourant.Y += Pente_y;
					} else { // Cas d'un déplacement en x
						Delta += (a * Pente_x);
						T = MAJ_Tableau(T, PCourant.X, PCourant.Y, Sens_traitement);

						PCourant.X += Pente_x;
					}
				} else { // Cas d'un déplacement en y
					Delta += (b * Pente_y);
					T = MAJ_Tableau(T, PCourant.X, PCourant.Y, Sens_traitement);

					PCourant.Y += Pente_y;
				}
				// System.out.println( "End While");
				// if (Pente_x*PCourant.X < Pente_x*Pmm.Xb || Pente_y*PCourant.Y
				// < Pente_y*Pmm.Yb) break;
			} // End While

			T = MAJ_Tableau(T, PCourant.X, PCourant.Y, Sens_traitement);

		}
		return T;

	}

	public static Tab_Min_Max MAJ_Tableau(Tab_Min_Max T, int x, int y, int Sens_traitement) {

		assert (x >= 0 && y >= 0);
		Elt_Tab_Min_Max Elt = new Elt_Tab_Min_Max();

		switch (Sens_traitement) {
		case XMIN_YMIN:
		case XMIN:
		case XMIN_YMAX:
		case CENTER:
			if (x > T.Max || x < T.Min)
				break;

			Elt = T.Element[x - T.Min];
			if (Elt.Min == -1 || Elt.Min >= y)
				Elt.Min = y;
			if (Elt.Max <= y)
				Elt.Max = y;
			T.Sens_Par = 1;
			T.Sens_Bal = 1;
			T.Element[x - T.Min] = Elt;
			break;

		case YMIN:
			if (y > T.Max || y < T.Min)
				break;

			Elt = T.Element[y - T.Min];
			if (Elt.Min == -1 || Elt.Min >= x)
				Elt.Min = x;
			if (Elt.Max <= x)
				Elt.Max = x;
			T.Sens_Par = 1;
			T.Sens_Bal = -1;
			T.Element[y - T.Min] = Elt;
			break;

		case YMAX:
			if (y > T.Max || y < T.Min)
				break;

			Elt = T.Element[Math.abs(y - T.Max)];
			if (Elt.Min == -1 || Elt.Min >= x)
				Elt.Min = x;
			if (Elt.Max <= x)
				Elt.Max = x;
			T.Sens_Par = -1;
			T.Sens_Bal = -1;
			T.Element[Math.abs(y - T.Max)] = Elt;
			break;

		case XMAX_YMIN:
		case XMAX:
		case XMAX_YMAX:
			if (x > T.Max || x < T.Min)
				break;

			Elt = T.Element[Math.abs(x - T.Max)];
			if (Elt.Min == -1 || Elt.Min >= y)
				Elt.Min = y;
			if (Elt.Max <= y)
				Elt.Max = y;
			T.Sens_Par = -1;
			T.Sens_Bal = 1;
			T.Element[Math.abs(x - T.Max)] = Elt;
			break;

		default:
			System.out.println("Le sens indiqué n'est pas correct => le tableau n'a pu etre mis à jour..");
		}

		return T;
	}

	public static void Affichage_Tableau_Min_Max_Grille(Tab_Min_Max T, Grille grille, int Num_Zone) {
		for (int i = 0; i < T.Taille_Tab; i++)
			for (int j = T.Element[i].Min; j <= T.Element[i].Max; j++)
				if (T.Sens_Par == 1 && T.Sens_Bal == 1)
					grille.element[i + T.Min][j].affichage = Num_Zone;
				else if (T.Sens_Par == -1 && T.Sens_Bal == -1)
					grille.element[j][T.Max - i].affichage = Num_Zone;
				else if (T.Sens_Par == -1 && T.Sens_Bal == 1)
					grille.element[T.Max - i][j].affichage = Num_Zone;
				else
					grille.element[j][i + T.Min].affichage = Num_Zone;

		grille.Affichage_grille();
		System.out.println();
	}

	/*****************************************************************************/
	/* Methode qui balaye une zone pour trouver l'arete la plus proche */
	/* du point source */
	/* Entrée: Tableau T : Le tableau de min max de la zone a balayer */
	/* Entrée: Grille G : La grille reguliere */
	/* Entrée: Marque : La marque de la zone */
	/* Sortie: L'arete trouvée */
	/****************************************************************************/

	public static ArrayList<Edge_traited> Aspirateur_Zone(Tab_Min_Max T, Grille G, int Marque) {

		ArrayList<Edge_traited> A;
		assert (T.Min <= T.Max);

		if (T.Sens_Par > 0) {
			if (T.Sens_Bal > 0) {
				// parcours croissant en X pour i et en Y pour j
				for (int i = T.Min; i <= T.Max; i++) {
					assert (i - T.Min >= 0 && i - T.Min < T.Taille_Tab);
					for (int j = T.Element[i - T.Min].Min; j <= T.Element[i - T.Min].Max; j++) {
						assert (j >= 0 && j < G.Nb_L);
						A = Liste_Verif(G.element[i][j], Marque);

						if (A != null)
							return A;
					}
				}
			} else {
				for (int j = T.Min; j <= T.Max; j++) {
					assert (j - T.Min >= 0 && j - T.Min < T.Taille_Tab);
					for (int i = T.Element[j - T.Min].Min; i <= T.Element[j - T.Min].Max; i++) {
						assert (i >= 0 && i < G.Nb_C);
						A = Liste_Verif(G.element[i][j], Marque);
						if (A != null)
							return A;
					}
				}
			}
		}

		else {
			// le tableau contient des valeurs décroissantes ...
			if (T.Sens_Bal > 0) {
				// parcourt décroissant en X pour i et croissant en Y pour j
				for (int i = 0; i <= Math.abs(T.Min - T.Max); i++) {
					assert (i >= 0 && i < T.Taille_Tab);
					for (int j = T.Element[i].Min; j <= T.Element[i].Max; j++) {
						assert (j >= 0 && j < G.Nb_L);
						A = Liste_Verif(G.element[T.Max - i][j], Marque);
						if (A != null)
							return A;
					}
				}
			} else {
				// parcourt décroissant en Y pour j et croissant en X pour i
				for (int j = 0; j <= Math.abs(T.Max - T.Min); j++) {
					assert (j >= 0 && j < T.Taille_Tab);
					for (int i = T.Element[j].Min; i <= T.Element[j].Max; i++) {
						assert (i >= 0 && i < G.Nb_C);
						A = Liste_Verif(G.element[i][T.Max - j], Marque);
						if (A != null)
							return A;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Mothode qui verifie si il exite au moins une arete non traite dans la
	 * liste
	 * 
	 * @param Liste
	 *            : La liste a verifier
	 * @return NULL si il n'existe pas d'arete non traite dans la liste ou
	 *         l'aret sinon
	 **/
	public static ArrayList<Edge_traited> Liste_Verif(Element_Grille Elm_Grille, int Marque) {

		assert (Marque >= 0);
		int j = 0;

		if (!Elm_Grille.ListeArete.isEmpty()) {
			do {
				// eviter les coins (plusieur arêtes)
				// if (Elm_Grille.ListeArete.size()>1) return null;

				assert (Elm_Grille.ListeArete.get(j).traited <= Marque);
				// si l'arete n'a pas deja ete traitée
				if (Elm_Grille.ListeArete.get(j).traited < Marque) {
					// On marke l'arete
					Elm_Grille.ListeArete.get(j).traited = Marque;
					// On retourne cette arete
					assert (!Elm_Grille.ListeArete.isEmpty());
					return Elm_Grille.ListeArete;
				}
				j++;
			} while (j < Elm_Grille.ListeArete.size());
		}

		return null;

		// Il n'y a pas d'arete ou si elles ont toute ete traitée
		// Si il n'y a pas d'arete ou si elles ont toute ete traitée, on revoie
		// NULL

	}

	public static boolean ZoneValide(Zone2d zone) {

		boolean isT; // une variable de test

		// soit les sommets 0 et 3 sont différents de source, soit ils sont tous
		// les 2 égaux ...
		isT = zone.sommets[0].equals(zone.sommets[3]); // 3 sommets

		zone.valide = PTEGAUX(zone.source, zone.sommets[0]) && PTEGAUX(zone.source, zone.sommets[3]) || !isT; // !isT=>
																												// 4
																												// sommets
																												// ==>
																												// valide
		zone.valide = zone.valide & !PTEGAUX(zone.sommets[1], zone.sommets[2]); // sommet
																				// 1
																				// !=
																				// sommet
																				// 2

		if (!zone.valide)
			return false; // non-valide

		// deux traitements differents selon les cas ...
		if (isT) {
			zone.valide = !PTEGAUX(zone.source, zone.sommets[1]) && !PTEGAUX(zone.source, zone.sommets[2]); // souce
																											// !=
																											// sommet
																											// 1
																											// et
																											// souce
																											// !=
																											// sommet
																											// 2
		} else {
			// attention aux zones réfléchies qui restent valides !!!
			zone.valide = !(PTEGAUX(zone.source, zone.sommets[1]) && PTEGAUX(zone.sommets[2], zone.sommets[3]));
		}
		zone.valide = zone.valide & !Zone_Croisee(zone); // 4 sommets si croisée
															// => non valide

		if (zone.valide) {
			// on vérifie enfin que l'ouverture angulaire est correcte !

			Vector3d V = new Vector3d();
			Vector3d U = new Vector3d();

			Vector3d vectSommet1 = new Vector3d(zone.sommets[1]);
			Vector3d vectSommet2 = new Vector3d(zone.sommets[2]);
			Vector3d vectSource = new Vector3d(zone.source);

			V = VECTORSUB(vectSommet1, vectSource);
			U = VECTORSUB(vectSommet2, vectSource);

			V.normalize();
			U.normalize();

			double dot = V.dot(U);

			if (dot < 1D - 10E-20)
				zone.valide = true;
		}
		return zone.valide;

	}

	public static boolean Zone_Croisee(Zone2d zone) {

		if (Type_De_Zone2D(zone) == 4) {
			double[] t;
			for (int i = 0; i < 2 && zone.valide; i++) { // i vaut soit zero
															// soit un
				t = intersection(zone.sommets[i], zone.sommets[i + 1], zone.sommets[i + 2], zone.sommets[(i + 3) % 4]);
				if ((t[0] < 0.0) && (t[0] > -1.0)) {

					System.out.println(" zone croisée !");
					return true;
				}
			}
		}
		return false;

	}

	/*****************************************************************************/
	/* definition de la nature des intersection entre deux segments */
	/*                                                                           */
	/* entree : pa, pb representant le segment [a,b] */
	/* pr, ps representant le segment [r,s] */
	/* sortie : t1(t2) representant le coeffescient d'intersection entre le */
	/* segment[a,b]([r,s]) et la droite (r,s)((a,b)). */
	/* si -1<=t1<=0 alors la droite (r,s) coupe [a,b] */
	/* si -1<=t2<=0 alors la droite (a,b) coupe [r,s] */
	/*****************************************************************************/
	public static double[] intersection(Point3d pa, Point3d pb, Point3d pr, Point3d ps) {
		double det, a, b, c, d, e, f, t1, t2;

		a = pa.x - pb.x;
		b = pa.y - pb.y;
		c = pr.x - ps.x;
		d = pr.y - ps.y;
		e = pr.x - pa.x;
		f = pr.y - pa.y;

		det = (c * b - d * a);

		if (Math.abs(det) < 10e-7)
			t1 = t2 = +10.; /* droite parallele */
		else if (PTEGAUX(pa, pr)) {
			t1 = 0.;
			t2 = 0.;
		} else if (PTEGAUX(pa, ps)) {
			t1 = 0.;
			t2 = -1.;
		} else if (PTEGAUX(pb, pr)) {
			t1 = -1.;
			t2 = 0.;
		} else if (PTEGAUX(pb, ps)) {
			t1 = -1.;
			t2 = -1.;
		} else {
			t1 = (c * f - d * e) / det;
			t2 = (f * a - b * e) / det;
			// évitons la confusion avec le cas parallèle ...
			if (t1 == 10. && t2 == 10.)
				t1 = t2 = 9.9999999;
		}
		double t[] = new double[2];
		t[0] = t1;
		t[1] = t2;
		return t;
	}

	public static boolean PTEGAUX(Point3d a, Point3d b) {
		return ((Math.abs(a.x - b.x) < 10e-7 && Math.abs(a.y - b.y) < 10e-7) ? true : false);
		// return ((Math.abs(a.x-b.x)==0D && Math.abs(a.y-b.y)==0D) ? true :
		// false);
	}

	public static Vector3d VECTORSUB(Vector3d a, Vector3d b) {
		Vector3d temp = new Vector3d();
		temp.set(a.x - b.x, a.y - b.y, a.z - b.z);
		return temp;
	}

	public static Point3d PointSUB(Point3d a, Point3d b) {
		Point3d temp = new Point3d();
		temp.set(a.x - b.x, a.y - b.y, a.z - b.z);
		return temp;
	}

	/**********************************************************************************/
	/*                                                                                */
	/*
	 * cette fonction effectue un "clipping" (ou coupe) de l'arete AB, de telle
	 */
	/*
	 * sorte que la partie resultante appartienne a la zone2d zone. Si l'arete
	 * n'est
	 */
	/*
	 * pas du tout dans la zone, la fonction retourne FALSE. Sinon elle retourne
	 * TRUE.
	 */
	/*                                                                                */
	/* entree : Point A, B : arete AB. */
	/* Zone2D zone : la zone de clipping */
	/* sortie : boolean : arete AB dans la zone. */
	/* Point A, B : modifies pour la partie de l'arete dans zone. */
	/*                                                                                */
	/**********************************************************************************/

	public static boolean coupeArete(Point3d A, Point3d B, Zone2d zone) {

		// en 2D
		A.z = (0);
		B.z = (0);

		if ((!zone.valide) || PTEGAUX(A, B)) {
			if (PRINT)
				System.out.println(" false A=B ");
			return false;
		}

		boolean isT = true;
		int nbsommets = Type_De_Zone2D(zone);
		Point3d E = new Point3d();
		Point3d F = new Point3d();
		Point3d G = new Point3d();

		Point3d pG = new Point3d();
		Point3d pA = new Point3d();
		Point3d pB = new Point3d();

		Vector3d U = new Vector3d();
		Vector3d V = new Vector3d();
		Vector3d W = new Vector3d();

		double t[];
		double dot; /* dot product */

		for (int i = 0; isT && (i < nbsommets); i++) {
			E.set(zone.sommets[i]);
			E.z = (0);
			F.set(zone.sommets[(i + 1) % nbsommets]);
			E.z = (0);
			if (PTEGAUX(E, F))
				continue;

			t = intersection(A, B, E, F);

			if ((t[0] == 10.0) && (t[1] == 10.0)) {
				/* l'arete est parallele au cote ... est-elle confondue ? */
				if (PTEGAUX(E, A)) {
					if (PRINT)
						System.out.println(" parallele confondue ");
					return false; // confondue
				}

				U.set(B);
				U.sub(A);// U = B-A
				U.normalize();

				V.set(E);
				V.sub(A);// V = E-A
				V.normalize();
				dot = U.dot(V);
				if (PRINT)
					System.out.println(" parallele non-confondue ");
				if (Math.abs(dot) >= 1.0) {

					return false;
				}
			} // end parallele
			G.set(zone.sommets[(i + 2) % nbsommets]);
			G.z = (0);
			if (PTEGAUX(F, G))
				G.set(zone.sommets[(i + 3) % nbsommets]);
			G.z = (0);

			/* calcul du vecteur unitaire de la droite EF... */
			U.set(F);
			U.sub(E);// U = F-E
			U.normalize();

			/* calcul du projete de G sur la droite EF ... */
			V.set(G);
			V.sub(E);// V = G-E
			dot = U.dot(V);

			Point3d u = new Point3d(U.x, U.y, U.z); // Vector3d => Point3d
			pG = VECTOR2DADDSCALE(dot, u, E);

			/* calcul du projete de A sur la droite EF ... */
			V.set(A);
			V.sub(E);// V = A-E
			dot = U.dot(V);
			pA = VECTOR2DADDSCALE(dot, u, E);

			/* calcul du projete de B sur la droite EF ... */
			V.set(B);
			V.sub(E);// V = B-E
			dot = U.dot(V);
			pB = VECTOR2DADDSCALE(dot, u, E);

			/* calcul des produit scalaire pAA.pGG et pBB.pGG */
			V.set(G);
			V.sub(pG); // V = G-pG
			W.set(A);
			W.sub(pA); // W = A-pA
			double dotA = V.dot(W); // produit scalaire
			W.set(B);
			W.sub(pB); // W = B-pB
			double dotB = V.dot(W); // produit scalaire

			/* traitement proprement dit ! */
			if ((dotA <= 0.0) && (dotB <= 0.0)) {
				// l'arete est en dehors de la zone
				isT = false;
				if (PRINT)
					System.out.println("l'arete est en dehors de la zone");
			}

			else if ((dotA < 0.0) || (dotB < 0.0)) {
				// l'arete coupe un bord de la zone
				/* A ou B doit etre egal a l'intersection de AB et FE */
				if (PRINT)
					System.out.println("l'arete coupe un bord de la zone");
				t = intersection(A, B, E, F);

				if (t[0] != 10.) {
					Point3d C;
					if (dotB < 0.0) {
						C = calc_point_2d(t[0], A, B);
						B.x = (C.x);
						B.y = (C.y);
						if (PRINT)
							System.out.println("If " + dotB);
					} else {
						C = calc_point_2d(t[0], A, B);
						A.x = (C.x);
						A.y = (C.y);
						if (PRINT)
							System.out.println("else  " + dotB);
					}
				}
			}
		}

		isT = isT & !PTEGAUX(A, B);

		return isT;

	}

	public static Point3d VECTOR2DADDSCALE(double a, Point3d A, Point3d B) {
		Point3d C = new Point3d();
		C.x = a * A.x + B.x;
		C.y = a * A.y + B.y;
		return C;
	}

	/*****************************************************************************/
	/*                                                                           */
	/* calcul des coordonnees d'un point se trouvant sur un segment */
	/* entree : un double t permettant de calculer les coordonnees */
	/* deux points representant les extremites du segment */
	/* sortie : un point representant le point recherche */
	/*                                                                           */
	/*****************************************************************************/

	public static Point3d calc_point_2d(double t, Point3d A, Point3d B) {
		Point3d C = new Point3d();
		C.x = (A.x - B.x) * t + A.x;
		C.y = (A.y - B.y) * t + A.y;
		C.z = 0;
		return C;
	}

	/*******************************************************************************
	 * Methode qui repond a la question suivante : le point C est a gauche ou a
	 * droite de la droite (AB) ? Si la valeur retournee est negative, alors on
	 * tourne dans le sens inverse des aiguilles d'une montre (SIAM) (a gauche).
	 * Si c'est zero, les points sont alignés. Sinon, on tourne dans le sens des
	 * aiguilles d'une montre (SAM) (a droite).
	 *******************************************************************************/

	public static double SIAM(Point3d A, Point3d B, Point3d C) {
		// position = sign( (By - Ay) * (X - Ax)- (Bx - Ax) * (Y - Ay) )
		return ((B.y - A.y) * (C.x - A.x) - (C.y - A.y) * (B.x - A.x));
	}

	/*******************************************************************************
	 * Methode qui permet de comparer deux zone2D
	 * 
	 * @param zoneA
	 *            : La première zone2D à comparer
	 * @param zoneB
	 *            : La deuxieme zone2D à comparer
	 * @return : TRUE si les deux zones sont identiques FALSE sinon
	 ******************************************************************************/
	public static boolean egalZone2D(Zone2d zoneA, Zone2d zoneB) {

		boolean isIdent = true;

		if (((!zoneA.valide) || (!zoneB.valide)) || (zoneA.support != zoneB.support) || (zoneA.type != zoneB.type)
				|| (!PTEGAUX((zoneA.source), (zoneB.source))))
			isIdent = false;

		for (int i = 0; isIdent && i < 4; i++)
			// isIdent = Math.abs (zoneA.sommets[i].x-zoneB.sommets[i].x) >
			// 10e-5 && Math.abs(zoneA.sommets[i].y-zoneB.sommets[i].y) > 10e-5;
			isIdent = Math.abs(zoneA.sommets[i].x - zoneB.sommets[i].x) < 10e-5
					&& Math.abs(zoneA.sommets[i].y - zoneB.sommets[i].y) < 10e-5;

		return isIdent;
	}
	/*****************************************************************************/
	/*                                                                           */
	/*
	 * Ajout d'une zone en queue d'une liste, apres verification de sa coherence
	 */
	/*                                                                           */
	/* entree : la zone a ajouter */
	/* sortie : TRUE si la zone est bonne */
	/*                                                                           */
	/*****************************************************************************/
	/*
	 * private boolean Zone2DAjouter(Zone2d zone) {
	 * 
	 * boolean b; double []t;
	 * 
	 * b = ZoneValide (zone); if (b) { if
	 * (!COMP_EPS(zone.source,zone.sommets[0])) { t=intersection (zone.source,
	 * zone.sommets[0], zone.sommets[1], zone.sommets[2]); if (t[0] < -EPS)
	 * EchangePoint (zone.sommets[1], zone.sommets[2]); }
	 * 
	 * ListeZone2d.add(zone);
	 * 
	 * }
	 * 
	 * return b; }
	 */

	/*****************************************************************************/
	/*                                                                           */
	/*
	 * Ajout d'une zone en queue d'une liste , apres verification de sa
	 * coherence
	 */
	/*                                                                           */
	/* entree : la zone a ajouter */
	/* sortie : TRUE si la zone est bonne */
	/*                                                                           */
	/*****************************************************************************/
	public static boolean Zone2DAjouter(Zone2d zone, ArrayList<Zone2d> liste) {

		boolean b;
		double[] t;

		b = ZoneValide(zone);
		if (b) {
			if (!COMP_EPS(zone.source, zone.sommets[0])) {
				t = intersection(zone.source, zone.sommets[0], zone.sommets[1], zone.sommets[2]);
				if (t[1] < -EPS)
					EchangePoint(zone.sommets[1], zone.sommets[2]);
			}

			liste.add(zone);

		}

		return b;
	}

	public static void EchangePoint(Point3d a, Point3d b) {

		Point3d temp = new Point3d();
		temp.set(a.x, a.y, a.z);
		a.set(b.x, b.y, b.z);
		b.set(temp.x, temp.y, temp.z);
	}

	public static boolean COMP(Point3d a, Point3d b) {

		return (Math.abs(a.x - b.x) <= 10e-4 && Math.abs(a.y - b.y) <= 10e-4);
	}

	public static boolean COMP_EPS(Point3d a, Point3d b) {

		return (Math.abs(a.x - b.x) <= EPS && Math.abs(a.y - b.y) <= EPS);
	}

	public static void TypeInteraction(int t) {
//		switch (t) {
//		case 0:
//			System.out.print(" VISIBLE ");
//			break;
//
//		case 1:
//			System.out.print(" REFLEXION ");
//			break;
//		case 2:
//			System.out.print(" DIFFRACTION ");
//			break;
//		case 3:
//			System.out.print(" TRANSMISSION ");
//			break;
//		case 4:
//			System.out.print(" INVISIBLE ");
//			break;
//
//		}
	}

}
