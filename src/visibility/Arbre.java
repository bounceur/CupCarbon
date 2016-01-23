/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import geo_objects.GeoZone;
import geo_objects.GeoZoneList;

/**
 *
 * @author Alwajeeh
 */
public class Arbre {

	public Arbre Pere;
	public Arbre Fils;
	public Arbre Frere;
	public Object Element;

	private final double EPS = 1E-7;
	private static boolean PRECALCULE = true; // déjà precalulé oui ou non ?

	GeoZoneList geoZoneList = null;

	public Arbre(Object element) {
		Pere = null;
		Fils = null;
		Frere = null;

		if (element instanceof Zone2d) {
			Zone2d zone = new Zone2d();
			zone.setZone2d((Zone2d) element);
			Element = zone;
		}

	}

	/* constructeur vide */
	public Arbre() {
	}

	public GeoZoneList getGeoZoneList() {
		return geoZoneList;
	}

	public void setElementArbre(Object element) {

		if (element instanceof Zone2d) {

			Zone2d zone = new Zone2d();
			zone.setZone2d((Zone2d) element);
			Element = zone;
		}

	}

	/*****************************************************************************/
	/*                                                                           */
	/* Ajout d'un fils dans un arbre */
	/*                                                                           */
	/*****************************************************************************/
	public void ArbreAjouter(Object element) {

		Arbre nouveau = new Arbre(element);
		nouveau.Fils = null;
		nouveau.Frere = this.Fils;
		nouveau.Pere = this;
		this.Fils = nouveau;

	}

	/*****************************************************************************/
	/*                                                                           */
	/* get fils ... */
	/*                                                                           */
	/*****************************************************************************/
	public Arbre getFils() {

		return this.Fils;
	}

	/*****************************************************************************/
	/*                                                                           */
	/* get frere ... */
	/*                                                                           */
	/*****************************************************************************/
	public Arbre getFrere() {

		return this.Frere;
	}

	/*****************************************************************************/
	/* Fonction qui renvoie le pere du noeud de l'arbre */
	/* @return : Le pere */
	/*****************************************************************************/
	public Arbre getPere() {
		return this.Pere;
	}

	/*****************************************************************************/
	/*
	 * Fonction qui renvoie L'élément associée au noeud de l'arbre /* @return :
	 * Le contenu dans le noeud de l'arbre
	 *****************************************************************************/
	public Object getArbreElement() {

		if (this.Element instanceof Zone2d)
			return (Zone2d) this.Element;
		else if (this.Element instanceof Point3d)
			return (Point3d) this.Element;
		else
			return null;
	}

	public void Calculer_Arbre(NbrInteractions NbI) {

		int h, hauteur_max;

		/*
		 * Initialisation : construction des vecteurs d'arbre pères et fils
		 */
		ArrayList<Arbre> ListePeres = new ArrayList<Arbre>();
		ArrayList<Arbre> ListeFils = new ArrayList<Arbre>();
		ArrayList<Arbre> ListeAll = new ArrayList<Arbre>();

		// ArrayList<Integer> Niveau = new ArrayList<Integer> ();

		Arbre temp;// = new Arbre();
		temp = this;

		for (temp = temp.getFils(); temp != null; temp = temp.getFrere())
			ListePeres.add(temp);

		hauteur_max = NbI.nbR + NbI.nbD + NbI.nbT + 1;
		// MaxMarqueNbr = 1+ hauteur_max;

		for (h = 1; h < hauteur_max; h++) {

			for (int i = 0; i < ListePeres
					.size(); i++) { /*
									 * Traitement des arbres pères : idem
									 * précédent
									 */

				/* Calcul ... */
				temp = ListePeres.get(i);
				temp.CalculZone2Daux(h, NbI);

				/* Mise du résultat dans le vecteur des fils */
				// for (temp = ListePeres.get(i).getFils();temp!= null; temp=
				// temp.getFrere())
				for (temp = temp.getFils(); temp != null; temp = temp.getFrere())
					ListeFils.add(temp);
			}

			ListeAll.addAll(ListePeres); // check if you need it
			// Niveau.add(ListePeres.size());
			// System.out.print(" h " + h);
			// System.out.println(" size " +ListePeres.size());

			ListePeres.removeAll(ListePeres);
			ListePeres.addAll(ListeFils);

			if (h != (hauteur_max - 1)) // check if you need it
				ListeFils.removeAll(ListeFils);
		}

		// if (PRECALCULE){
		if (true) {
			// Affichage
			Zone2d test = new Zone2d();
			// for (int i = 0; i < ListeAll.size(); i++) {
			for (Arbre e : ListeAll) {
				// System.out.print("zone" + i + "=[");
				test = (Zone2d) e.Element; // (Zone2d) ListeAll.get(i).Element;

				GeoZone geoZone = new GeoZone(Zone2d.Type_De_Zone2D(test), 0);

				for (int j = 0; j < Zone2d.Type_De_Zone2D(test); j++) {
					// System.out.print(test.sommets[j]);
					geoZone.set(test.sommets[j].x, test.sommets[j].y, 0, j);
					// if (j != Zone2d.Type_De_Zone2D(test) - 1) {
					// System.out.print(" ; ");
					// }
				}
				GeoZoneList.add(geoZone);
				// System.out.print("]; ");
				// System.out.print(" type" + i + "=[");
				Zone2d.TypeInteraction(test.type);
				// System.out.print("]; ");
				// System.out.println();
			}
		}

		//
		// int count=0;
		// for (temp = ListeFils.get(0);temp!= null; temp= temp.getPere()){
		// System.out.print("zone"+ count+"=[");
		// test=(Zone2d)temp.Element;
		// for(int j=0; j<Zone2d.Type_De_Zone2D(test); j++){
		// System.out.print(test.sommets[j]);
		// if (j!=Zone2d.Type_De_Zone2D(test)-1)
		// System.out.print(" ; ");
		//
		// }
		// System.out.print("]; ");
		// System.out.print("type"+ count+"=[");
		// Zone2d.TypeInteraction(test.type);
		// System.out.print("];");
		// System.out.print(" support"+ count+"=[");
		// if (test.support!=null)
		// System.out.print(test.support.getA()+ " ; " + test.support.getB());
		// else
		// System.out.print(" NULL ");
		// System.out.print("]; ");
		//
		// System.out.println();
		// count++;
		// }

	}

	/******************************************************************************/
	/*                                                                            */
	/* fonction auxiliaire */
	/*                                                                            */
	/******************************************************************************/
	public void CalculZone2Daux(int h, NbrInteractions NbI) {

		Arbre pere;
		Zone2d zTmp;

		ArrayList<Zone2d> ZonesReflechies = new ArrayList<Zone2d>();
		ArrayList<Zone2d> ZonesTransmises = new ArrayList<Zone2d>();
		ArrayList<Zone2d> ZonesDiffractees = new ArrayList<Zone2d>();

		if (this.Element instanceof Zone2d)
			if ((h > 0) && (h < (NbI.nbR + NbI.nbD + NbI.nbT + 1))) {

				/* zone du père */
				// Zone2d zPere = new Zone2d();
				//
				// if (this.getPere().getArbreElement() instanceof Zone2d)
				// zPere.setZone2d((Zone2d) this.getPere().getArbreElement());

				/* zone de l'arbre */
				Zone2d zArbre = new Zone2d();
				zArbre.setZone2d((Zone2d) this.getArbreElement());

				/* Calcul des Antecedents et de la marque courante */
				int nD = 0;
				int nR = 0;
				int nT = 0;
				// Marques.nbr = 0;
				for (pere = this; pere != null; pere = pere.getPere()) {

					if (pere.getArbreElement() instanceof Zone2d) {
						zTmp = (Zone2d) pere.getArbreElement();

						switch (zTmp.type) {
						case Zone2d.REFLEXION:
							nR++;
							break;
						case Zone2d.DIFFRACTION:
							nD++;
							break;
						case Zone2d.TRANSMISSION:
							nT++;
							break;
						/* autre cas : VISIBLE & INVISIBLE Sans intérêt ici ! */
						}
					} else
						nD++;

					// Marques check if you need it

				}

				/* Traitement Classique */
				boolean DiffA = false, DiffB = false;

				Point3d A = new Point3d();
				Point3d B = new Point3d();

				if (zArbre.support != null) {
					if (nR < NbI.nbR) {
						/* Traitement de la reflexion par le support */

						reflexion2D(zArbre, ZonesReflechies);
						Zone2d.analyseScene(Zone2d.grille, ZonesReflechies);
					}

					if (nT < NbI.nbT) {
						/* Traitement de la reflexion par le support */

						transmission2D(zArbre, ZonesTransmises);
						Zone2d.analyseScene(Zone2d.grille, ZonesTransmises);
					}

					if (nD < NbI.nbD) {
						/*
						 * Traitement de la diffraction par les sommets du
						 * support
						 */
						A.set(zArbre.support.getA());
						B.set(zArbre.support.getB());

						if (Zone2d.PTEGAUX(B, zArbre.sommets[1]) || Zone2d.PTEGAUX(B, zArbre.sommets[2]))
							if (ReadScene2D.PointsDiffractants.contains(B)) // test
																			// si
																			// le
																			// point
																			// B
																			// est
																			// diffractant
								if (PRECALCULE)
									DiffB = true; // Marque le Point B comme
													// étant diffractant
								else
									// diffraction2D (B, zArbre,
									// ZonesDiffractees);
									diffraction2D(B, ZonesDiffractees);

						if (Zone2d.PTEGAUX(A, zArbre.sommets[1]) || Zone2d.PTEGAUX(A, zArbre.sommets[2]))
							if (ReadScene2D.PointsDiffractants.contains(A)) // test
																			// si
																			// le
																			// point
																			// A
																			// est
																			// diffractant
								if (PRECALCULE)
									DiffA = true; // Marque le Point A comme
													// étant diffractant
								else
									// diffraction2D (A, zArbre,
									// ZonesDiffractees);
									diffraction2D(A, ZonesDiffractees);

						// Zone2d.analyseScene(Zone2d.grille, ZonesDiffractees);
					}

					/* Ajout Zones Transmises */
					for (int i = 0; i < ZonesTransmises.size(); i++) //
						if (Zone2d.ZoneValide(ZonesTransmises.get(i)))
							this.ArbreAjouter(ZonesTransmises.get(i));

					/* Ajout Zones Diffractées */
					if (PRECALCULE) {
						// si les zones diffractées sont precalulées, on marque
						// seulement
						if (DiffB)
							this.ArbreAjouter(B);

						if (DiffA)
							this.ArbreAjouter(A);
					} else {
						// si les zones diffractées sont pas encore calulées
						for (int i = 0; i < ZonesDiffractees.size(); i++)
							if (Zone2d.ZoneValide(ZonesDiffractees.get(i)))
								this.ArbreAjouter(ZonesDiffractees.get(i));
					}

					/* Ajout Zones Reflechies */
					for (int i = 0; i < ZonesReflechies.size(); i++)
						if (Zone2d.ZoneValide(ZonesReflechies.get(i)))
							this.ArbreAjouter(ZonesReflechies.get(i));
					// !! Bien mettre les réflexions en dernier !!

					/* Affichage */
					// Affichage_Zones(zArbre,ZonesReflechies,ZonesTransmises,ZonesDiffractees);
					// if (DiffA) System.out.println("PointDiffractantA =["+ A
					// +"];");
					// if (DiffB) System.out.println("PointDiffractantB =["+ B
					// +"];");

				}
			}

	}

	/*****************************************************************************/
	/*                                                                           */
	/* methode qui calcule la zone eblouie par une reflexion sur une arete */
	/*                                                                           */
	/* entree : la zone sur laquelle on calcule la zone de reflexion */
	/* sortie : une liste des zones de reflexions */
	/*                                                                           */
	/*****************************************************************************/

	void reflexion2D(Zone2d zone, ArrayList<Zone2d> ListeZone) {

		// ArrayList <Zone2d> ListeZone = new ArrayList<Zone2d> ();

		double t[] = { 0D, 0D };
		double v[] = { 0D, 0D };

		/* On ne travaille pas avec des zones invalides ... */
		if ((!zone.valide) || (zone.support == null)) {
			System.out.println("reflexion : out car la zone est non valide ");

			return;
		}

		/* ================================================================ */
		/* Modif : réutilisation des calculs précédents. */

		// check if you still need it

		/* ================================================================ */

		/* Copie de l'arête de réflexion comme étant le fond de la zone ... */

		Point3d A = new Point3d();
		Point3d B = new Point3d();
		A.set(zone.sommets[1]);
		A.z = (0);
		B.set(zone.sommets[2]);
		B.z = (0);

		if (Zone2d.COMP_EPS(A, B)) {
			System.out.println("reflexion : out car A == B ");
			return;
		}

		/* calcul de la source de la nouvelle zone de reflexion */
		Vector3d U = new Vector3d();
		Vector3d V = new Vector3d();
		Point3d source = new Point3d();

		U.set(B);
		U.sub(A);// U = B-A
		U.normalize();

		V.set(zone.source);
		V.sub(A);// V = Source -A
		double dot = U.dot(V);

		Point3d u = new Point3d(U.x, U.y, U.z); // Vector3d => Point3d
		Point3d p = new Point3d();
		p = Zone2d.VECTOR2DADDSCALE(dot, u, A);

		V.set(p);
		V.sub(zone.source);// V = p - source
		source.set(V.x + p.x, V.y + p.y, 0D); // new source = V + p

		/* calcul des zones ou il y a réflexion */
		/* Etape 1 : recherche du premier contact avec un bord ... */
		int inter = -1, sens = +1;

		for (int i = 0; i < 4; i++) {
			t = Zone2d.intersection(source, A, Zone2d.fenetre[i], Zone2d.fenetre[(i + 1) % 4]);
			v = Zone2d.intersection(source, B, Zone2d.fenetre[i], Zone2d.fenetre[(i + 1) % 4]);

			if ((t[0] <= -1.0) && (t[1] <= 0.0) && (t[1] >= -1.0) && (v[0] <= -1.0) && (v[1] <= 0.0)
					&& (v[1] >= -1.0)) {
				/* A et B intersecte le meme cote ! */
				inter = i;
				i = 4;
				sens = 0; /* pas besoin de determiner le sens ! */
				if (t[1] < v[1]) {
					Zone2d.EchangePoint(A, B);
					t[0] = v[0];
					t[1] = v[1];

				}
				if (Zone2d.PRINT)
					System.out.println(" A + B meme cote ! ");

			} else if ((t[0] <= -1.0) && (t[1] <= 0.0) && (t[1] > -1.0)) {
				/* seul A intersecte le cote ... */
				inter = i;
				i = 4;
				if (Zone2d.PRINT)
					System.out.println(" A bon cote tout seul ");
			} else if ((v[0] <= -1.0) && (v[1] <= 0.0) && (v[1] > -1.0)) {
				/* Seul B intersecte le cote */
				if (Zone2d.PRINT)
					System.out.println(" B tout seul bon cote ");
				Zone2d.EchangePoint(A, B);
				inter = i;
				i = 4;
				t[0] = v[0];
				t[1] = v[1];
			}
		}

		assert (inter != -1);
		Point3d temp = new Point3d();
		temp = Zone2d.calc_point_2d(t[1], Zone2d.fenetre[inter], Zone2d.fenetre[(inter + 1) % 4]);
		p.set(temp);

		/* il faut determiner le sens de rotation ... */
		if (sens == 0)
			sens = +1;
		else {
			t = Zone2d.intersection(source, Zone2d.fenetre[(inter + 1) % 4], A, B);
			if ((t[0] < 0) && (t[1] <= 0) && (t[1] >= -1))
				sens = +1; // intersection avec AB ...
			else
				sens = -1;
		}
		/* Etape 2 : segmentation de la zone (obligatoire : une zone etant */
		/* definie par 4 points ... */

		inter = (sens < 0) ? ((inter + 1) % 4) : inter;
		int i = (sens < 0) ? ((inter + 3) % 4) : ((inter + 1) % 4);

		if (sens > 0)
			for (;;) {
				t = Zone2d.intersection(source, Zone2d.fenetre[i], A, B);

				// on sort dès que l'on n'intersecte plus un bord de scène
				if (!((t[1] >= -1.0) && (t[1] < 0.0)))
					break;

				/* il faut une zone supplementaire ... */
				temp = Zone2d.calc_point_2d(t[1], A, B);

				if (!Zone2d.COMP_EPS(p, Zone2d.fenetre[i])) {
					Zone2d nouvelle = new Zone2d(A, p, Zone2d.fenetre[i], temp, source, Zone2d.REFLEXION, null);

					assert (!Zone2d.Zone_Croisee(nouvelle));
					Zone2d.Zone2DAjouter(nouvelle, ListeZone);
				}
				A.set(temp); /* !! */
				p.set(Zone2d.fenetre[i]); /* !! */

				inter = (sens < 0) ? ((inter + 3) % 4) : ((inter + 1) % 4);
				i = (sens < 0) ? ((inter + 3) % 4) : ((inter + 1) % 4);
			}

		/* Etape 3 : creation de la zone (finale ?) */
		t = Zone2d.intersection(source, B, Zone2d.fenetre[inter], Zone2d.fenetre[i]);
		temp = Zone2d.calc_point_2d(t[0], source, B);

		if (!Zone2d.COMP_EPS(p, temp) && !Zone2d.PTEGAUX(A, p) && !Zone2d.PTEGAUX(temp, B)) {
			boolean b;
			Zone2d nouvelle = new Zone2d(A, p, temp, B, source, Zone2d.REFLEXION, null);
			b = Zone2d.Zone_Croisee(nouvelle);
			assert (!b); //
			b = Zone2d.Zone2DAjouter(nouvelle, ListeZone);
		}

		if (Zone2d.PRINT)
			System.out.println(" source  " + source);

		return;
	}

	/*****************************************************************************/
	/*                                                                           */
	/* calcul de la zone transmise par une pôv arête sans défense. */
	/*                                                                           */
	/* entree : la zone sur laquelle on calcule la zone de transmission */
	/* sortie : une a plusieurs zones de transmission */
	/*                                                                           */
	/*****************************************************************************/
	void transmission2D(Zone2d zone, ArrayList<Zone2d> ListeZone) {
		// static enum { UN, DEUX } level = UN;

		Zone2d nouvelle = new Zone2d();

		int i, inter;

		Point3d A = new Point3d();
		Point3d B = new Point3d();

		/* On ne travaille pas avec des zones invalides ... */
		if ((!zone.valide) || (zone.support == null)) {
			if (zone.support == null)
				System.out.println("transmission : out car zone->support == NULL ");
			else
				System.out.println("transmission : out car zone non valide");
			return;
		}

		/*
		 * Algorithme : la solution retenue est simple : on prolonge la zone
		 * initiale au delà de l'arête de réflexion, et donc on recherche quelle
		 * est l'arête du fond qui permet de finir la zone de transmission que
		 * l'on construit. En effet, c'est astucieux, il n'y a qu'une seule
		 * partie de la fenêtre qui sera le support de fin ; cela est dû au
		 * découpage précédent ayant ammener la construction de la zone
		 * précédent la transmission.
		 *
		 * Donc algo is : - création d'une zone de transmission par copie de
		 * zone précédente, - modification des deux points 0 et 3, qui sont les
		 * anciens points 1 et 2, - recherche de l'arête de la fenêtre qui
		 * intersecte le prolongement de la zone, - mise à jour des points 1 et
		 * 2 !
		 *
		 */

		/* Initialisation des parties simples de la zone */
		nouvelle.valide = true; /* Par défaut ! */
		nouvelle.source
				.set(zone.source); /* nouvelle source = ancienne source */
		nouvelle.type = Zone2d.TRANSMISSION; /*
												 * Création d'une zone de
												 * transmission !
												 */
		nouvelle.support = null; /* Fin non connue */

		nouvelle.sommets[0] = new Point3d();
		nouvelle.sommets[1] = new Point3d();
		nouvelle.sommets[2] = new Point3d();
		nouvelle.sommets[3] = new Point3d();

		/* Copie de l'arête de réflexion comme étant le fond de la zone ... */
		A.set(zone.sommets[1]);
		A.setZ(0);
		B.set(zone.sommets[2]);
		B.setZ(0);

		/* mise à jour points 0 et 3 */
		nouvelle.sommets[0].set(A);
		nouvelle.sommets[3].set(B);

		/* calcul des zones ou il y a réflexion */
		/* Etape 1 : recherche du premier contact avec un bord ... */
		double t[] = { 0D, 0D };
		double v[] = { 0D, 0D };
		inter = -1;
		for (i = 0; i < 4; i++) {
			t = Zone2d.intersection(nouvelle.source, A, Zone2d.fenetre[i], Zone2d.fenetre[(i + 1) % 4]);
			v = Zone2d.intersection(nouvelle.source, B, Zone2d.fenetre[i], Zone2d.fenetre[(i + 1) % 4]);

			if ((t[0] <= -1.0) && (t[1] <= EPS) && (t[1] + EPS >= -1.0) && (v[0] <= -1.0) && (v[1] <= EPS)
					&& (v[1] + EPS >= -1.0)) {
				/* A et B intersecte le meme cote ! */
				inter = i;
				i = 4;
			}
		}

		t[1] = t[1] < -1. ? -1 : t[1] > 0. ? 0. : t[1];
		v[1] = v[1] < -1. ? -1 : v[1] > 0. ? 0. : v[1];

		if (inter == -1)
			return;
		assert (inter != -1);
		nouvelle.sommets[1] = Zone2d.calc_point_2d(t[1], Zone2d.fenetre[inter], Zone2d.fenetre[(inter + 1) % 4]);
		nouvelle.sommets[2] = Zone2d.calc_point_2d(v[1], Zone2d.fenetre[inter], Zone2d.fenetre[(inter + 1) % 4]);

		/* Evitons d'avoir une zone croisée */
		if (Zone2d.Zone_Croisee(nouvelle)) {
			Point3d temp = new Point3d();
			temp.set(nouvelle.sommets[1]);
			nouvelle.sommets[1].set(nouvelle.sommets[2]);
			nouvelle.sommets[2].set(temp);
		}

		/* Ajout de la nouvelle zone dans la liste résultante */
		Zone2d.Zone2DAjouter(nouvelle, ListeZone);
		Zone2d.analyseScene(Zone2d.grille, ListeZone);

	}

	// private void Affichage_Zones(Zone2d ZonePere, ArrayList<Zone2d>
	// ZonesReflechies, ArrayList<Zone2d> ZonesTransmises,
	// ArrayList<Zone2d> ZonesDiffractees) {
	//
	// System.out.print("zone=[");
	// for (int j = 0; j < Zone2d.Type_De_Zone2D(ZonePere); j++) {
	// System.out.print(ZonePere.sommets[j]);
	// if (j != Zone2d.Type_De_Zone2D(ZonePere) - 1)
	// System.out.print(" ; ");
	// }
	// System.out.print("];");
	// System.out.print("type=[");
	// Zone2d.TypeInteraction(ZonePere.type);
	// System.out.print("];");
	// System.out.println();
	//
	// // affichage des zones reflechies
	// for (int i = 0; i < ZonesReflechies.size(); i++) {
	// System.out.print("zone" + i + "=[");
	// for (int j = 0; j < Zone2d.Type_De_Zone2D(ZonesReflechies.get(i)); j++) {
	// System.out.print(ZonesReflechies.get(i).sommets[j]);
	// if (j != Zone2d.Type_De_Zone2D(ZonesReflechies.get(i)) - 1)
	// System.out.print(" ; ");
	// }
	// System.out.print("];");
	// System.out.print("type" + i + "=[");
	// Zone2d.TypeInteraction(ZonesReflechies.get(i).type);
	// System.out.print("];");
	// System.out.println();
	// }
	//
	// // affichage des zones transmises
	// for (int i = 0; i < ZonesTransmises.size(); i++) {
	// System.out.print("zonet" + i + "=[");
	// for (int j = 0; j < Zone2d.Type_De_Zone2D(ZonesTransmises.get(i)); j++) {
	// System.out.print(ZonesTransmises.get(i).sommets[j]);
	// if (j != Zone2d.Type_De_Zone2D(ZonesTransmises.get(i)) - 1)
	// System.out.print(" ; ");
	// }
	// System.out.print("];");
	// System.out.print("type" + i + "=[");
	// Zone2d.TypeInteraction(ZonesTransmises.get(i).type);
	// System.out.print("];");
	// System.out.println();
	// }
	//
	// // affichage des zones diffractées
	// for (int i = 0; i < ZonesDiffractees.size(); i++) {
	// System.out.print("zone" + i + "=[");
	// for (int j = 0; j < Zone2d.Type_De_Zone2D(ZonesDiffractees.get(i)); j++)
	// {
	// System.out.print(ZonesDiffractees.get(i).sommets[j]);
	// if (j != Zone2d.Type_De_Zone2D(ZonesDiffractees.get(i)) - 1)
	// System.out.print(" ; ");
	// }
	// System.out.print("];");
	// System.out.print("type" + i + "=[");
	// Zone2d.TypeInteraction(ZonesDiffractees.get(i).type);
	// System.out.print("];");
	// System.out.println();
	// }
	// }

	/*****************************************************************************/
	/*                                                                           */
	/* calcul des zones de diffraction */
	/*                                                                           */
	/* entree : le point source de diffraction */
	/* la zone dont est issu le point */
	/* sortie : une ou plusieurs zones de diffraction */
	/*                                                                           */
	/*****************************************************************************/

	// void diffraction2D (Point3d source, Zone2d zone, ArrayList<Zone2d>
	// ListeZone) {
	//
	// Point3d P, A, B, temp;
	// int i, j, inter=-1;
	// int count=0;
	//
	// double Xmax= Zone2d.fenetre[0].x;
	// double Ymax= Zone2d.fenetre[0].y;
	// double Xmin= Zone2d.fenetre[2].x;
	// double Ymin= Zone2d.fenetre[2].y;
	//
	// // attention : ne pas diffracter depuis le bord de la scène !!
	// if (source.x == Xmin|| source.x==Xmax || source.y == Ymin
	// || source.y == Ymax || !zone.valide) {
	// return ;
	// }
	//
	// if (Zone2d.COMP_EPS(zone.sommets[0],source) ||
	// Zone2d.COMP_EPS(zone.sommets[3],source)) {
	// return ;
	// }
	//
	// /* Debut factorisation */
	// Factor res = new Factor ();
	// res = factorisation (zone, source);
	// System.out.println("Point diffractant : " + source);
	// System.out.println("res A: " + res.pA+" res B: " + res.pB);
	// if (res.fin == 1) {
	// return;
	// }
	// /* Debut factorisation */
	//
	// /* Optimization to be Added */
	// /*------------------------------*/
	// A = new Point3d();A.set(res.pA);
	// B = new Point3d();A.set(res.pB);
	//
	// /* calcul des zones ou il y a diffraction */
	// /* Etape 1 : recherche du premier contact avec un bord ... */
	// /* cherche ou B tape ... */
	// for (j=0; j<4; j++) {
	// double U[] =Zone2d.intersection(source, B, Zone2d.fenetre[j],
	// Zone2d.fenetre[(j+1)%4]);
	// if ((U[0] <=0.0) && (U[1] <= 0.0) && (U[1]-EPS >= -1.0)) {
	// break;
	// }
	// }
	// if (j == 4) {
	// if (Math.abs(source.x-450234.09375)<EPS) // !!!!!
	// return ;
	// }
	//
	// double [] U={0D,0D};
	// /* cherche ou A tape ... */
	// for (i=0; i<4; i++) {
	// U=Zone2d.intersection(source, A, Zone2d.fenetre[i],
	// Zone2d.fenetre[(i+1)%4]);
	// if ((U[0] <=0.0) && (U[1] <= 0.0) && (U[1]-EPS >= -1.0)) {
	// inter = i;
	// break;
	// }
	// }
	//
	// if (inter == -1) {
	// if (Math.abs(source.x-450234.09375)<EPS) // !!!!!
	// return ;
	// }
	// P = new Point3d();
	// P = Zone2d.calc_point_2d(U[1], Zone2d.fenetre[inter],
	// Zone2d.fenetre[(inter+1)%4]);
	//
	// if (res.sens > 0) do {
	// if (!Zone2d.COMP_EPS(P,Zone2d.fenetre[i])) {
	// Zone2d nouvelle = new
	// Zone2d(source,P,Zone2d.fenetre[i],source,source,Zone2d.DIFFRACTION,null);
	//
	// assert(!Zone2d.Zone_Croisee(nouvelle));
	// Zone2d.Zone2DAjouter(nouvelle,ListeZone);
	// count++;
	//
	// }
	// P.set(Zone2d.fenetre[i]);
	// i = ((i+3)%4);
	// } while (i!=j);
	//
	// else do {
	// i = ((i+1)%4);
	// if (!Zone2d.COMP_EPS(P,Zone2d.fenetre[i])) {
	// Zone2d nouvelle = new
	// Zone2d(source,P,Zone2d.fenetre[i],source,source,Zone2d.DIFFRACTION,null);
	// assert(!Zone2d.Zone_Croisee(nouvelle));
	// Zone2d.Zone2DAjouter(nouvelle,ListeZone);
	// count++;
	// }
	// P.set(Zone2d.fenetre[i]);
	// } while (i!=j);
	//
	// /* Etape 3 : creation de la zone (finale ?) */
	//
	// U=Zone2d.intersection(source, B, Zone2d.fenetre[j],
	// Zone2d.fenetre[(j+1)%4]);
	// temp=new Point3d();
	// temp = Zone2d.calc_point_2d (U[0],source,B);
	//
	// if (!Zone2d.COMP_EPS(P,temp)) {
	// Zone2d nouvelle = new
	// Zone2d(source,P,temp,source,source,Zone2d.DIFFRACTION,null);
	// assert(!Zone2d.Zone_Croisee(nouvelle));
	// Zone2d.Zone2DAjouter(nouvelle,ListeZone);
	// count++;
	// }
	//
	// }

	private static void diffraction2D(Point3d source, ArrayList<Zone2d> ListeZone) {

		for (int i = 0; i < 4; i++) {
			Zone2d zone = new Zone2d(source, Zone2d.fenetre[i], Zone2d.fenetre[(i + 1) % 4], source, source,
					Zone2d.DIFFRACTION, null);
			Zone2d.Zone2DAjouter(zone, ListeZone);
		}
		Zone2d.analyseScene(Zone2d.grille, ListeZone);

		int j = ReadScene2D.PGindice(source);
		if (ReadScene2D.PolyGrid[j] != null)
			SupprimerZones_Interieur(source, ReadScene2D.PolyGrid[j], ListeZone);

	}

	public void ArbreDiffractionPreCalcul() {

		int n = ReadScene2D.ListePointArbre.size();
		Point3d PointDiffractant;

		NbrInteractions NbI = new NbrInteractions(0, 1, 0);
		for (int i = 0; i < n; i++) { // Pour tous les Points diffractants

			/* Precaluler les arbres de diffraction */
			/* 0 Réflexions; 0 Transmision ; une diffraction */
			PRECALCULE = false;
			NbI.SetNbrInteractions(0, 1, 0);

			// System.out.println("case "+i);
			// System.out.println("Point"+i+"=["+ReadScene2D.PointsDiffractants.get(i)+"];");
			PointDiffractant = new Point3d(ReadScene2D.ListePointArbre.get(i).P);

			/* Calculer les zones diffractées pour chaque point diffractant */
			ArrayList<Zone2d> ListeZone = new ArrayList<Zone2d>();
			diffraction2D(PointDiffractant, ListeZone);

			/* arbre des zones diffractants */
			Arbre ArbreDeDiffraction = new Arbre(geoZoneList);
			ArbreDeDiffraction.setElementArbre(PointDiffractant);

			for (int j = 0; j < ListeZone.size(); j++)
				if (Zone2d.ZoneValide(ListeZone.get(j)))
					ArbreDeDiffraction.ArbreAjouter(ListeZone.get(j));

			/* Considérons seulement la diffraction */

			ArbreDeDiffraction.Calculer_Arbre(NbI);

			/* Ajout */
			ReadScene2D.ListePointArbre.get(i).A = new Arbre(geoZoneList);
			ReadScene2D.ListePointArbre.get(i).A = ArbreDeDiffraction;

			/* Considérons 10 réflexions pour chaque arbre */
			// NbI.SetNbrInteractions(10,0,0); /* 10 réflexions */
			// PRECALCULE = true;
			// //System.out.println("PRECALCULE = true ");
			// ReadScene2D.ListePointArbre.get(i).A.Calculer_Arbre(NbI);

		}
		//
		// PRECALCULE = true;
		//
		// NbI.SetNbrInteractions(10,0,0);
		// /* Considérons 10 réflexions pour chaque arbre */
		// System.out.println("PRECALCULE = true ");
		// for(int i=0; i<ReadScene2D.ListePointArbre.size(); i++){
		// System.out.println("Point"+i+"=["+ReadScene2D.ListePointArbre.get(i).P
		// + "];");
		// for(Arbre temp=ReadScene2D.ListePointArbre.get(i).A.getFils();
		// temp!=null; temp=temp.getFrere()){
		// temp.Calculer_Arbre(NbI);
		// }
		// }

	}

	/*****************************************************************************/
	/*                                                                           */
	/* Supprimer les zones de diffraction qui se trouve dans un batiment */
	/*                                                                           */
	/* entree : le point diffractant */
	/* la liste de toutes les zones dont elles sont issues */
	/* sortie (void): la liste de zones de diffraction modifiée */
	/*                                                                           */
	/*****************************************************************************/

	private static void SupprimerZones_Interieur(Point3d P, ArrayList<Face> ListeFace, ArrayList<Zone2d> ListeZone) {

		/* le batiment à traiter */
		Face f = ListeFace.get(0); // valeur initiale = le premier batiment

		/* nombre de sommets */
		int n = 0;

		/* prenre la valueur d'un sommet du polygone */
		Point3d S;
		/* batiment trouvé Oui ou non */
		boolean found = false;

		outerloop: for (int i = 0; i < ListeFace.size(); i++) {
			f = ListeFace.get(i);
			n = f.sommets.length;

			for (int j = 0; j < n; j++) {
				// on cherche le sommet du polygone correspondant au point
				// diffractant à l'entrée
				S = new Point3d(f.sommets[j]);
				if (Zone2d.PTEGAUX(P, S)) {
					// on a trouvé le polygone == f où le point diffractant se
					// trouve
					found = true;
					break outerloop;
				}
			}
		}

		int listSize = ListeZone.size();
		assert (found);

		boolean methode1 = true; /* Methode optimisée */

		for (int i = 0; i < listSize; i++) {

			if (methode1) {
				Point3d mid1 = new Point3d(); /* Midpoint */
				Point3d mid2 = new Point3d(); /* Midpoint */
				Point3d test = new Point3d(); /* test point */
				mid1.set((ListeZone.get(i).sommets[0].x + ListeZone.get(i).sommets[1].x) / 2D,
						(ListeZone.get(i).sommets[0].y + ListeZone.get(i).sommets[1].y) / 2D, 0D);
				mid2.set((ListeZone.get(i).sommets[0].x + ListeZone.get(i).sommets[2].x) / 2D,
						(ListeZone.get(i).sommets[0].y + ListeZone.get(i).sommets[2].y) / 2D, 0D);
				test.set((mid1.x + mid2.x) / 2D, (mid1.y + mid2.y) / 2D, 0D);

				/* veifier si le point est dans le batiment si oui supprimer */
				if (InsidePoly(test, f)) {
					ListeZone.remove(i); /* suppression */
					i--; /* un pas en arrière */
					listSize = ListeZone.size(); /* mise à jour de la taille */
				}
			}

			else {

				/* Quatre tests pour chaque sommet */
				boolean A = false;
				boolean B = false;
				boolean C = false;
				boolean D = false;

				for (int j = 0; j < n; j++) {
					S = new Point3d(f.sommets[j]);
					if (!A)
						A = Zone2d.PTEGAUX(ListeZone.get(i).sommets[0], S);
					if (!B)
						B = Zone2d.PTEGAUX(ListeZone.get(i).sommets[1], S);
					if (!C)
						C = Zone2d.PTEGAUX(ListeZone.get(i).sommets[2], S);
					if (!D)
						D = Zone2d.PTEGAUX(ListeZone.get(i).sommets[3], S);

					if (A && B && C && D) { /*
											 * les sommets de cette zone sont
											 * aussi les sommets du batiment !!
											 */

						Point3d mid1 = new Point3d(); /* Midpoint 1 */
						Point3d mid2 = new Point3d(); /* Midpoint 2 */
						Point3d test = new Point3d(); /* test point */
						mid1.set((ListeZone.get(i).sommets[0].x + ListeZone.get(i).sommets[1].x) / 2D,
								(ListeZone.get(i).sommets[0].y + ListeZone.get(i).sommets[1].y) / 2D, 0D);
						mid2.set((ListeZone.get(i).sommets[0].x + ListeZone.get(i).sommets[2].x) / 2D,
								(ListeZone.get(i).sommets[0].y + ListeZone.get(i).sommets[2].y) / 2D, 0D);
						test.set((mid1.x + mid2.x) / 2D, (mid1.y + mid2.y) / 2D, 0D);
						/*
						 * veifier si le point est dans le batiment si oui
						 * supprimer
						 */
						if (InsidePoly(test, f)) {
							ListeZone.remove(i); /* suppression */
							i--; /* un pas en arrière */
							listSize = ListeZone
									.size(); /* mise à jour de la taille */
							break;
						}

					}

				}
			}
		}
	}

	/**
	 * regard:
	 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 * 
	 * @param le
	 *            point à tester, le polygone (batiment)
	 * @return renvoie true si le point à tester est is dans un polygone, sinon
	 *         false
	 *
	 */
	public static boolean InsidePoly(Point3d test, Face f) {
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = f.sommets.length - 1; i < f.sommets.length; j = i++) {
			if ((f.sommets[i].y > test.y) != (f.sommets[j].y > test.y) && (test.x < (f.sommets[j].x - f.sommets[i].x)
					* (test.y - f.sommets[i].y) / (f.sommets[j].y - f.sommets[i].y) + f.sommets[i].x)) {
				result = !result;
			}
		}

		return result;
	}

	static boolean diffraction_eloignee_en_cours = false;
	// Factor factorisation (Zone2d zone, Point3d source) {
	//
	// Factor res= new Factor() ;
	// int i;
	//
	// Zone2d_BPoint zBP= new Zone2d_BPoint() ;
	// zBP.z.setZone2d(zone);
	// zBP.p.set(source);
	//
	// /* Parcourt de la liste des polygones */
	//
	// i = ReadScene2D.PGindice(source);
	//
	// if (ReadScene2D.PolyGrid [i] != null) {
	// for (int j =0;j<ReadScene2D.PolyGrid [i].size() ;j++)
	// PointDiffractant(ReadScene2D.PolyGrid[i].get(j),zBP);
	//
	// }
	//
	// if (!(zBP.b && zBP.e)) {
	// //System.out.println(" sortir Tchao !! ");
	// res.fin = 1;
	// return res;
	// }
	//
	// /* Pas la peine de diffracter si c'est plat ... */
	// if (zBP.Rear != null && zBP.Front != null) {
	// res.fin = 1;
	// return res;
	// }
	//
	// /* on risque de diffracter DANS un bâtiment. Pour éviter cela, on regarde
	// * simplement si le pt de diffraction == sommet e ou f ! */
	//
	// if (zBP.Rear!=null){
	//
	// Point3d ED, ER;
	//
	// /* diffraction eloignée en cours ? */
	// if (!diffraction_eloignee_en_cours) {
	// res.fin = 1;
	// return res;
	// }
	//
	// /* Est-ce que l'on rentre ? */
	// if (entre_batiment (zone, source, zBP)) {
	// res.fin = 1;
	// return res;
	// }
	//
	// res.pA.set(zone.source);
	// ED = new Point3d();
	// ED.set(Zone2d.PointSUB(source, res.pA));
	//
	// res.pB = null;
	//
	// ER = new Point3d();
	// ER.set(Zone2d.PointSUB(zone.sommets[1], source));
	//
	// int v_sgn = ReadScene2D.sign (ED,ER);
	//
	// switch (v_sgn) {
	// case -1: // Sommet 1 à gauche
	// if (zBP.Rleft != null )
	// res.pB.set(zBP.Rleft);
	// else
	// res.pB.set(zBP.Fleft);
	//
	// break;
	// case 1: // Sommet 1 à droite
	//
	// if (zBP.Rright != null )
	// res.pB.set(zBP.Rright);
	// else
	// res.pB.set(zBP.Fright);
	// break;
	// default: ;
	// // rien à faire
	// }
	//
	// if (res.pB == null) { // Oups : sommets aligné !! On recommence avec
	// sommet 2
	//
	// ER.set(Zone2d.PointSUB(zone.sommets[2],source));
	// v_sgn = ReadScene2D.sign (ED,ER);
	//
	// switch (v_sgn) {
	// case -1: // Sommet 2 à gauche
	// if (zBP.Rleft != null )
	// res.pB.set(zBP.Rleft);
	// else
	// res.pB.set(zBP.Fleft);
	// break;
	// case 1: // Sommet 2 à droite
	// if (zBP.Rright != null )
	// res.pB.set(zBP.Rright);
	// else
	// res.pB.set(zBP.Fright);
	// break;
	// }
	// if (res.pB == null) {
	// if (zBP.Fleft != null)
	// res.pB.set(zBP.Fleft);
	// else {
	// res.pB.set(res.pA);
	// res.pA.set(zBP.Fright);
	//
	// if (res.pA == null) {
	// res.fin = 1;
	// return res;
	// }
	// }
	// }
	// }
	// }
	//
	// else {
	// boolean deuxCote = false;
	//
	// if (diffraction_eloignee_en_cours) {
	// res.fin = 1;
	// return res;
	// }
	//
	// /* recherche du BON sommet limitant la diffraction */
	// if (zBP.Rleft != null || zBP.Fleft != null || zBP.Front != null) { //
	// deux côtés ... non pas toujours
	//
	// if (zBP.Rleft != null )
	// res.pA.set(zBP.Rleft);
	// else if (zBP.Fleft != null )
	// res.pA.set(zBP.Fleft);
	// else
	// res.pA.set(zBP.Front);
	//
	// if (zBP.Rright != null )
	// res.pB.set(zBP.Rright);
	// else if (zBP.Fright != null )
	// res.pB.set(zBP.Fright);
	// else if (zBP.Front != null )
	// res.pB.set(zBP.Front);
	// else
	// res.pB.set(zBP.Fleft);
	//
	// res.dA = zBP.Rleft!=null ? zBP.dotRL : zBP.Fleft!=null ? zBP.dotFL :
	// zBP.dotF;
	// res.dB = zBP.Rright!=null ? zBP.dotRR : zBP.Fright!= null ? zBP.dotFR :
	// zBP.Front!=null ? zBP.dotF : zBP.dotFL;
	//
	// deuxCote = res.pB == zBP.Rright || res.pB == zBP.Fright;
	// }
	//
	// else {
	// if (zBP.Rright == null) {
	// res.fin = 1;
	// return res;
	// }
	//
	// res.pA.set(zBP.Fright);
	// res.pB.set(zBP.Rright);
	// res.dA = zBP.dotFR;
	// res.dB = zBP.dotRR;
	// }
	//
	// // Ordonnons en A et en B ...
	// if (res.dA > res.dB) { // proche de 1 => loin ... car vecteur DP dans
	// WhichDihedra :-)
	// // inversion de A et B !!
	// double d = res.dA;
	// Point3d temp = new Point3d();
	// temp.set(res.pA);
	//
	// res.pA.set(res.pB);
	// res.pB.set(temp);
	// res.dA = res.dB;
	// res.dB = d;
	// res.sens = -1; // tournera dans sens inverse car inversion de A et B
	// }
	//
	// if (deuxCote && (res.dA<-res.dB)) {
	// res.fin = 1;
	// return res;
	// }
	// }
	//
	// if (res.pA == null)
	// System.out.println("diffraction eloignee : " +
	// diffraction_eloignee_en_cours);
	//
	// assert (res.pA != null);
	// assert (res.pB != null);
	//
	// return res;
	//
	//
	// }

	// private void PointDiffractant(Face f , Zone2d_BPoint zBP) {
	//
	// assert (zBP.z.valide);
	// int prec; /* le sommet precedent */
	// int suiv; /* le sommet suivant */
	//
	// int n = f.sommets.length;
	//
	// for (int j=0; zBP.b && j<n; j++){
	// // on cherche le sommet du polygone correspondant au point diffractant
	// supposé
	// Point3d S = new Point3d(f.sommets[j]);
	// if (Zone2d.PTEGAUX(zBP.p,S)) {
	//// if (marques_egales (&Marques, p->Marques+j)) {
	//// // Sommets déjà traité !!!
	//// zBP.e = false;
	//// zBP.b = false;
	//// return ;
	//// }
	//// marques_assign (p->Marques+j, &Marques);
	//
	//
	// /*le point est sommet du polygone*/
	// zBP.e = true;
	//
	// prec = (j+n-1)%n; /* le sommet precedent */
	// suiv = (j+1)%n; /* le sommet suivant */
	//
	// //on regarde le sens des 2 vecteurs obtenus...
	// // mise à jour de la structure Zone2D_BPoint ...
	// Point3d P= new Point3d(f.sommets[suiv]);
	// zBP.b = ReadScene2D.WhichDihedra (zBP.p, zBP.z.source, P, zBP);
	// P.set(f.sommets[prec]);
	// zBP.b = ReadScene2D.WhichDihedra (zBP.p, zBP.z.source, P, zBP);
	//
	// }
	//
	//
	// }
	// }

	// private boolean entre_batiment(Zone2d zone, Point3d source, Zone2d_BPoint
	// zBP) {
	//
	// assert (zBP.Rear != null); // Sinon on risque pas d'y entrer ...
	// Point3d zS , P, dirS, dirP, V;
	//
	// zS = new Point3d(zone.source);
	//
	// dirS = new Point3d();
	// dirS.set(Zone2d.PointSUB(source, zS));
	//
	// if (Zone2d.COMP_EPS(source,zone.sommets[1]))
	// P = new Point3d(zone.sommets[2]);
	// else if (Zone2d.COMP_EPS(source,zone.sommets[2]))
	// P = new Point3d(zone.sommets[1]);
	// else
	// return false; // cas diffraction éloignée !!
	//
	// dirP = new Point3d();
	// dirP.set(Zone2d.PointSUB(P, zS));
	//
	// int posP = ReadScene2D.sign(dirS,dirP); // -1 à droite, +1 à gauche
	//
	// /* On entre dans le bâtiment si des arêtes de bâtiments se trouve des 2
	// côtés ... */
	//
	// /* Traitement pour chaque arête de bâtiment trouvée */
	// if (zBP.Fleft != null) {
	// V = new Point3d();
	// V.set(Zone2d.PointSUB(source, zBP.Fleft));
	// if (posP == ReadScene2D.sign (dirS, V))
	// return true;
	// }
	//
	// if (zBP.Fright != null) {
	// V = new Point3d();
	// V.set(Zone2d.PointSUB(source, zBP.Fright));
	// if (posP == ReadScene2D.sign (dirS, V))
	// return true;
	// }
	//
	// return false;
	//
	// }

}

// New class
class ZonesNiveau {
	ArrayList<Arbre> Zones = new ArrayList<Arbre>();
	int[] Niveau;
}
