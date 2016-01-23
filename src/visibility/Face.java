/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.media.j3d.GeometryArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
//import org.openide.util.Lookup;
//import org.openide.util.lookup.Lookups;
//import org.openide.windows.IOProvider;
//import org.openide.windows.OutputWriter;
//import org.xlim.sic.ig.kernel.BoundingBox;
//import org.xlim.sic.ig.kernel.Edge;
//import org.xlim.sic.ig.kernel.physics.Field;
//import org.xlim.sic.ig.spatial.ArrayListHit;
//import org.xlim.sic.ig.spatial.Hit;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.GeometryInfo;

/**
 * <p>
 * A Face contains all the information useful for reflexion, refraction and
 * diffraction. It is the basic geometrical object for radio propagation
 * simulation! A face is always planar, defined by a given set of points for its
 * geometry, may have a width and some information about the interface it
 * delimitates, get some information about the radio electric properties of its
 * interface, and some useful method like intersection, for instance.
 * </p>
 * 
 * <p>
 * It implements Primitive, even if all Primitive may be reduced to a set of
 * Faces. A Face is one Face, or may be many if it necessitates to be
 * convexified.
 * </p>
 * 
 * @author pcombeau
 */
public class Face implements Serializable {
	private static final long serialVersionUID = 1L;

	final static double DISTANCE_MIN = 2e-5D;

	/** Definition d'une face : une liste de sommets dans un plan */
	public Vector3d sommets[];

	BoundingBox bbox;

	/* local coordinates system */
	private Vector3d u = new Vector3d();
	private Vector3d v = new Vector3d();

	/**
	 * Points associated to the convex hull of this face. They are computed on
	 * demand only.
	 */
	private Vector3d hull[] = null;

	/** Le plan support d'une face */
	private double a;
	private double b;
	private double c;
	private double d;

	// private Lookup brdf = Lookup.EMPTY;

	// @Override
	// public Brdf<? extends Field> getBrdf(Class<? extends Brdf <? extends
	// Field> > cl)
	// {
	// //return (Brdf<? extends Field>)brdf.lookup(cl);
	// }

	private static enum NormalT {
		X, Y, Z;
		static final long serialVersionUID = 1L;
	};

	//private NormalT normalXYZ;

	/** Les droites de Plucker correspondant au bord de la face */

	/** la normale */
	Vector3d normale;
	/** couleur d'affichage */
	Color couleur;

	/**
	 * The face color to use when drawing it in iHM
	 */
	public Color3f color;

	/** triangles obtenus par triangulation de la face */
	// Triangle triangles[];
	private boolean isEdge[][]; // is triangle edges real edges (of face)
	private int triId[][];

	Triangle triangles[];

	/** Computes the support plane */
	final private void calcul_plan() {
		if (sommets.length > 2) {
			Vector3d AB = new Vector3d(sommets[0]);
			AB.sub(sommets[1]);
			Vector3d AC = new Vector3d(sommets[0]);
			AC.sub(sommets[2]);
			AB.cross(AB, AC);
			a = AB.x;
			b = AB.y;
			c = AB.z;
			d = -(a * sommets[0].x + b * sommets[0].y + c * sommets[0].z);
			normale = new Vector3d(AB);
			normale.normalize();

			//final double normaleX = Math.abs(normale.x);
			//final double normaleY = Math.abs(normale.y);
			//final double normaleZ = Math.abs(normale.z);

			//normalXYZ = normaleX >= normaleY && normaleX >= normaleZ ? NormalT.X : normaleY >= normaleZ ? NormalT.Y : NormalT.Z;
		} else {
			throw new Error("face avec moins de 3 sommets");
		}
	}

	/**
	 * Creates a Face, using a set of vertices
	 * 
	 * @param brdf
	 * @param v
	 *            vertices of the face
	 */
	public Face(final Brdf<? extends Field> brdf, final Vector3d v[]) {
		// this.brdf = Lookups.fixed(brdf);
		setSommets(v);
		couleur = Color.RED;
		color = null;
		// color = new Color3f();
		getHull();
		// In EM mode do that
		// Permittivity.set (1.D, -1.D);
		// Permittivity2.set (1.D, 0.D);
		// rough = 0.D;
	}

	/**
	 * Creates a new Face by copy
	 * 
	 * @param f
	 *            the copied face
	 */
	public Face(final Face f) {
		// this.brdf = f.brdf;
		setSommets(f.sommets);
		couleur = f.couleur;
		color = null;
		// color = f.color;
		// Permittivity.set (f.Permittivity);
		// Permittivity2.set (f.Permittivity2);
		// rough = f.rough;
	}

	/**
	 * Custom redefinition of the hash value
	 * 
	 * @return Hash value
	 */
	@Override
	public int hashCode() {
		return (sommets.length % 255) | (((int) (((double) (1 << 24)) * (a + b + c))) << 8);
	}

	/**
	 * Test equality between two given faces
	 * 
	 * @param obj
	 *            a second face to test with
	 * @return <code>true</code> iff the two faces are equals in term of their
	 *         contents
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Face other = (Face) obj;
		if (this.sommets != other.sommets && (this.sommets == null || !Arrays.equals(this.sommets, other.sommets))) {
			return false;
		}
		if (this.a != other.a) {
			return false;
		}
		if (this.b != other.b) {
			return false;
		}
		if (this.c != other.c) {
			return false;
		}
		if (this.d != other.d) {
			return false;
		}
		if (this.couleur != other.couleur && (this.couleur == null || !this.couleur.equals(other.couleur))) {
			return false;
		}
		// if (!brdf.equals(other.brdf)) {
		// return false;
		// }
		return true;
	}

	/**
	 * Modify a face by setting a new set of vertices
	 * 
	 * @param v
	 *            new set of vertices
	 */
	private void setSommets(final Vector3d newSommets[]) {
		bbox = new BoundingBox(newSommets[0]);
		sommets = new Vector3d[newSommets.length];
		Point3d[] pts = new Point3d[newSommets.length];
		for (int i = 0; i < newSommets.length; i++) {
			sommets[i] = new Vector3d(newSommets[i]);
			bbox.include(sommets[i]);
			pts[i] = new Point3d(newSommets[i]);
		}
		calcul_plan();

		GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		gi.setCoordinates(pts);
		gi.setStripCounts(new int[] { pts.length });
		GeometryArray ga = gi.getGeometryArray();
		double coords[] = new double[3];
		int nbTriangle = 0;
		do {
			try {
				ga.getCoordinates(nbTriangle, coords);
				nbTriangle++;
			} catch (Exception e) {
				break;
			}
		} while (true);
		nbTriangle /= 3;

		triangles = new Triangle[nbTriangle];
		isEdge = new boolean[nbTriangle][];
		triId = new int[nbTriangle][];

		for (int i = 0; i < nbTriangle; i++) {
			isEdge[i] = new boolean[3];
			triId[i] = new int[3];

			final int indice = 3 * i;

			ga.getCoordinates(indice, coords);
			final int idA = coord2vect(coords[0], coords[1], coords[2]);
			final Vector3d A = sommets[idA];
			triId[i][0] = idA;

			ga.getCoordinates(indice + 1, coords);
			final int idB = coord2vect(coords[0], coords[1], coords[2]);
			final Vector3d B = sommets[idB];
			isEdge[i][0] = isEdge(idA, idB);
			triId[i][1] = idB;

			ga.getCoordinates(indice + 2, coords);
			final int idC = coord2vect(coords[0], coords[1], coords[2]);
			final Vector3d C = sommets[idC];
			isEdge[i][1] = isEdge(idB, idC);
			isEdge[i][2] = isEdge(idC, idA);
			triId[i][2] = idC;

			triangles[i] = new Triangle(this, A, B, C);
		}

		u.sub(sommets[1], sommets[0]);
		u.normalize();
		v.cross(normale, u);
		v.normalize();

	}

	private final boolean isEdge(final int i1, final int i2) {
		final int diff = Math.abs(i1 - i2);
		return (diff == 1) || (diff == sommets.length - 1);
	}

	private final int coord2vect(final double x, final double y, final double z) {
		double minL = Double.MAX_VALUE;
		int res = -1;
		final Vector3d coord = new Vector3d(x, y, z);
		for (int i = 0; i < sommets.length; i++) {
			final Vector3d V = new Vector3d(coord);
			V.sub(sommets[i]);
			final double l = V.lengthSquared();
			if (l < minL) {
				res = i;
				minL = l;
			}
		}
		return res;
	}

	// @Override
	// public final Triangle[] getTriangle () {
	// return triangles;
	// }

	/**
	 * How many vertices in this face?
	 * 
	 * @return number of vertices
	 */
	public int nombreSommets() {
		return sommets.length;
	}

	/**
	 * Return the normal vector to this face instance
	 * 
	 * @return normal vector
	 */
	final public Vector3d getNormal() {
		Vector3d res = new Vector3d(normale);
		return res;
	}

	/**
	 * Intersection? Where? NB : thread safe !
	 * 
	 * @param ray
	 *            which may intersect the face
	 * @return <code>this</code> if intersection occurs, <code>null</code>
	 *         otherwise
	 */
	// final public Face intersection_new (final Ray ray)
	// {
	// for (int i=0; i<triangles.length; i++) {
	// final double t = triangles[i].intersection(ray);
	// if (t <ray.distance) {
	// final double Interx = t*ray.direction.x + ray.from.x;
	// final double Intery = t*ray.direction.y + ray.from.y;
	// final double Interz = t*ray.direction.z + ray.from.z;
	// ray.distance = t;
	// ray.to.set(Interx, Intery, Interz);
	// return this;
	// }
	// }
	// return null;
	// }

	/**
	 * Test intersection with a given ray, as a parameter value along the ray.
	 * If no intersection occurs, then return a negative value.
	 * 
	 * @param ray
	 *            which may intersect the face
	 * @return ray parameter intersection, or negative value if no intersection
	 *         occurs
	 */
	// final public double intersectiont(final Ray ray) {
	// for (int i=0; i<triangles.length; i++) {
	// final double t = triangles[i].intersection(ray);
	// if (t > 0.D && t < Double.MAX_VALUE) {
	// return t;
	// }
	// }
	// return -Double.MAX_VALUE;
	// }

	// static private ArrayListHit noHit = Hit.noHit;

	// @Override
	// public ArrayListHit getHit(Ray ray)
	// {
	// final double vd = normale.dot(ray.direction);
	// if (vd == 0D) {
	// return Hit.noHit;
	// }
	//
	// /* Intersection avec le plan (directions orthogonales) */
	// final double v0 = normale.dot(sommets[0])-normale.dot(ray.from);
	// final double distance = v0/vd;
	// if (distance < DISTANCE_MIN
	// || distance > ray.distance-DISTANCE_MIN) {
	// return Hit.noHit;
	// }
	//
	// final double Interx = distance*ray.direction.x + ray.from.x;
	// final double Intery = distance*ray.direction.y + ray.from.y;
	// final double Interz = distance*ray.direction.z + ray.from.z;
	// int nc = 0;
	//
	// /* est-ce que ce point est dans la face ? */
	// double Aix = sommets[0].x;
	// double Aiy = sommets[0].y;
	// double Aiz = sommets[0].z;
	// double Ajx = sommets[sommets.length-1].x - Interx;
	// double Ajy = sommets[sommets.length-1].y - Intery;
	// double Ajz = sommets[sommets.length-1].z - Interz;
	// int sh;
	//
	// switch (normalXYZ) {
	// case X : // U == Y, V == Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aiy=Ajy; Aiz=Ajz;
	// Ajy = sommets[i].y - Intery;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.0 ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aiy >= 0.D || Ajy >= 0.D) &&
	// (Aiy - Aiz*(Ajy-Aiy) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Y : // U = X V = Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix=Ajx; Aiz=Ajz;
	// Ajx = sommets[i].x - Interx;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiz*(Ajx-Aix) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Z : // U=X V=Y
	// sh = Ajy >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix = Ajx; Aiy = Ajy;
	// Ajx = sommets[i].x - Interx;
	// Ajy = sommets[i].y - Intery;
	// final int nsh = Ajy >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiy*(Ajx-Aix) / (Ajy-Aiy) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// }
	//
	// if ((nc & 1) == 0) {
	// return Hit.noHit;
	// }
	//
	// final ArrayListHit res = new ArrayListHit();
	// res.add(new Hit (Interx, Intery, Interz, distance, this)); /* BINGO */
	// return res;
	// }

	// @Override
	// final public Face intersection (Ray ray)
	// {
	// //if (ray.direction.length() != 1.D)
	// // IOProvider.getDefault().getStdOut().println("ray.direction =
	// "+ray.direction+" with length = "+ray.direction.length());
	// final double vd = normale.dot(ray.direction);
	// if (vd == 0D) {
	// return null;
	// }
	//
	// /* Intersection avec le plan (directions orthogonales) */
	// final double v0 = normale.dot(sommets[0])-normale.dot(ray.from);
	// final double distance = v0/vd;
	// if (distance < DISTANCE_MIN
	// || distance > ray.distance-DISTANCE_MIN) {
	// return null;
	// }
	//
	// final double Interx = distance*ray.direction.x + ray.from.x;
	// final double Intery = distance*ray.direction.y + ray.from.y;
	// final double Interz = distance*ray.direction.z + ray.from.z;
	// int nc = 0;
	//
	// /* est-ce que ce point est dans la face ? */
	// double Aix = sommets[0].x;
	// double Aiy = sommets[0].y;
	// double Aiz = sommets[0].z;
	// double Ajx = sommets[sommets.length-1].x - Interx;
	// double Ajy = sommets[sommets.length-1].y - Intery;
	// double Ajz = sommets[sommets.length-1].z - Interz;
	// int sh;
	//
	// switch (normalXYZ) {
	// case X : // U == Y, V == Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aiy=Ajy; Aiz=Ajz;
	// Ajy = sommets[i].y - Intery;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.0 ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aiy >= 0.D || Ajy >= 0.D) &&
	// (Aiy - Aiz*(Ajy-Aiy) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Y : // U = X V = Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix=Ajx; Aiz=Ajz;
	// Ajx = sommets[i].x - Interx;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiz*(Ajx-Aix) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Z : // U=X V=Y
	// sh = Ajy >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix = Ajx; Aiy = Ajy;
	// Ajx = sommets[i].x - Interx;
	// Ajy = sommets[i].y - Intery;
	// final int nsh = Ajy >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiy*(Ajx-Aix) / (Ajy-Aiy) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// }
	//
	// if ((nc & 1) == 0) {
	// return null;
	// }
	//
	// ray.distance = distance;
	// ray.to.set(Interx, Intery, Interz);
	// return this; /* BINGO */
	// }

	/**
	 * Is this object intersected by the given ray ? Yes means the intersection
	 * is not on the object boundary !
	 * 
	 * @param ray
	 *            to intersect with
	 * @return <code>true</code> iff a strict intersection occurs
	 */
	// final public boolean strictIntersection (final Ray ray)
	// {
	// if (intersection(ray) == null) {
	// return false;
	// }
	// return isStrictlyInside(ray.to);
	//
	//// final Plucker l1 = new Plucker (ray.getDepart(), ray.getFin());
	//// Vector3d start = sommets[sommets.length-1];
	//// final Plucker l2 = new Plucker ();
	////
	//// for (int i=0; i<sommets.length; i++)
	//// {
	//// final Vector3d end = sommets[i];
	//// l2.set(start, end);
	//// if ( l1.areCoplanar(l2) ) return false;
	//// start = end;
	//// }
	////
	// /* Le code suivant est supprimé, car trop restrictif (si on passe
	// * au voisinnage d'une arête on est jeté). Cela vaut lorsque
	// * on est hors de la face, et aussi lorsque un coté est très grand.
	// * */
	//// final Vector3d Ve = new Vector3d();
	//// final Vector3d Vhit = new Vector3d ();
	//// Vector3d from = sommets[sommets.length-1];
	//// for (int i=0; i<sommets.length; i++)
	//// {
	//// final Vector3d to = sommets[i];
	//// Ve.set (to);
	//// Ve.sub (from);
	//// Vhit.set (ray.getFin());
	//// Vhit.sub(to);
	//// final double ls = Ve.lengthSquared()*Vhit.lengthSquared();
	//// Ve.cross(Ve, Vhit);
	//// if (Ve.lengthSquared()<1e-5D*ls) return false;
	////
	//// from = to;
	//// }
	//
	//// return true;
	// }
	//

	/**
	 * Is a given edge inside a face? This implies that the edge is not an edge
	 * of the face, and all the edge point are inside the face ...
	 * 
	 * @param e
	 *            the edge to test
	 * @return <code>true</code> iff <code>e</code> is included
	 */
	final public boolean inside(Edge e) {
		// false if edge not parallel to the face plane
		if (Math.abs(e.u.dot(normale)) > 1e-4D) {
			return false;
		}
		// is e.A in the plane ?
		if (Math.abs(normale.x * (e.A.x - sommets[0].x) + normale.y * (e.A.y - sommets[0].y)
				+ normale.z * (e.A.z - sommets[0].z)) > 1E-3D) {
			return false;
		}
		if (Math.abs(normale.x * (e.B.x - sommets[0].x) + normale.y * (e.B.y - sommets[0].y)
				+ normale.z * (e.B.z - sommets[0].z)) > 1E-3D) {
			return false;
		}
		// test e is not on the boundary !
		for (int i = 0; i < sommets.length; i++) {
			final int j = (i + 1) % sommets.length;
			final Vector3d AB = new Vector3d(sommets[j]);
			AB.sub(sommets[i]);
			final Vector3d AP = new Vector3d(e.A);
			AP.sub(sommets[i]);
			AP.cross(AB, AP);
			if (AP.lengthSquared() > 1E-4D) {
				continue;
			}
			AP.sub(e.B, sommets[i]);
			AP.cross(AB, AP);
			if (AP.lengthSquared() > 1E-4D) {
				continue;
			}
			// Edge is aligned with the semgnet i-j !!
			return false;
		}
		// test inside
		boolean ai = false;
		boolean bi = false;
		final Vector3d AA = new Vector3d(e.A);
		AA.add(normale);
		final Vector3d BB = new Vector3d(e.B);
		BB.sub(normale);
		final Plucker plA = new Plucker(e.A, AA);
		final Plucker plB = new Plucker(e.B, BB);
		for (Triangle tri : triangles) {
			final Plucker pl1 = new Plucker(tri.getA(), tri.getB());
			final Plucker pl2 = new Plucker(tri.getB(), tri.getC());
			final Plucker pl3 = new Plucker(tri.getC(), tri.getA());
			final double min = -1E-6D;
			final double max = +1E-6D;
			if (!ai) {
				final double s1 = plA.volumeSize(pl1);
				final double s2 = plA.volumeSize(pl2);
				final double s3 = plA.volumeSize(pl3);
				ai = (s1 >= min && s2 >= min && s3 >= min) || (s1 <= max && s2 <= max && s3 <= max);
			}
			if (!bi) {
				final double s1 = plB.volumeSize(pl1);
				final double s2 = plB.volumeSize(pl2);
				final double s3 = plB.volumeSize(pl3);
				bi = (s1 >= min && s2 >= min && s3 >= min) || (s1 <= max && s2 <= max && s3 <= max);
			}
			if (ai && bi) {
				break;
			}
		}
		// final boolean aai = inside(e.A);
		// final boolean bbi = inside(e.B);
		// if (aai&&!ai || bbi&&!bi) {
		// System.err.println
		// ("=============================================================");
		// System.err.println ("Edge "+e.A+" to "+e.B);
		// printFace(System.err);
		// System.err.println ("A inside ? "+inside(e.A)+" <=> "+ai);
		// System.err.println ("B inside ? "+inside(e.B)+" <=> "+bi);
		// }
		return ai && bi;
	}
	//
	// /** Is a given point inside the face?
	// * @param P a given point
	// * @return <code>true</code> iff P is inside this face
	// **/
	// @Override
	// public final boolean isInside (final Vector3d P) {
	// // is P inside the plane ??
	// if (Math.abs(normale.x*(P.x-sommets[0].x) +
	// normale.y*(P.y-sommets[0].y) +
	// normale.z*(P.z-sommets[0].z) ) > 1E-5D)
	// {
	// return false;
	// }
	//
	// int nc = 0;
	//
	// /* est-ce que ce point est dans la face ? */
	// double Aix = sommets[0].x;
	// double Aiy = sommets[0].y;
	// double Aiz = sommets[0].z;
	// double Ajx = sommets[sommets.length-1].x - P.x;
	// double Ajy = sommets[sommets.length-1].y - P.y;
	// double Ajz = sommets[sommets.length-1].z - P.z;
	// boolean sh;
	//
	// switch (normalXYZ) {
	// case X : // U == Y, V == Z
	// sh = Ajz >= 0.D ;
	// for (int i = 0; i < sommets.length; i++) {
	// Aiy=Ajy; Aiz=Ajz;
	// Ajy = sommets[i].y - P.y;
	// Ajz = sommets[i].z - P.z;
	// final boolean nsh = Ajz >= 0.0 ;
	// if ( (sh != nsh) &&
	// (Aiy >= 0.D || Ajy >= 0.D) &&
	// (Aiy - Aiz*(Ajy-Aiy) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Y : // U = X V = Z
	// sh = Ajz >= 0.D ;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix=Ajx; Aiz=Ajz;
	// Ajx = sommets[i].x - P.x;
	// Ajz = sommets[i].z - P.z;
	// final boolean nsh = Ajz >= 0.D ;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiz*(Ajx-Aix) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Z : // U=X V=Y
	// sh = Ajy >= 0.D ;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix = Ajx; Aiy = Ajy;
	// Ajx = sommets[i].x - P.x;
	// Ajy = sommets[i].y - P.y;
	// final boolean nsh = Ajy >= 0.D ;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiy*(Ajx-Aix) / (Ajy-Aiy) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// }
	//
	// return ((nc & 1) != 0);
	// }

	// public boolean isStrictlyInside ( final Vector3d pt )
	// {
	// // is P inside the plane ??
	// if (Math.abs(normale.x*(pt.x-sommets[0].x) +
	// normale.y*(pt.y-sommets[0].y) +
	// normale.z*(pt.z-sommets[0].z) ) > 1E-5D)
	// {
	// return false;
	// }
	//
	// final Plucker pl = new Plucker (pt, normale);
	// final Plucker edge = new Plucker ();
	//
	// // scan all triangles, make a include wrt the boundary ...
	// for (int i=0; i<triangles.length; i++)
	// {
	// edge.set(sommets[triId[i][0]], sommets[triId[i][1]]);
	// final PluckerSide side1 = pl.side (edge);
	//
	// edge.set(sommets[triId[i][1]], sommets[triId[i][2]]);
	// final PluckerSide side2 = pl.side (edge);
	//
	// edge.set(sommets[triId[i][2]], sommets[triId[i][0]]);
	// final PluckerSide side3 = pl.side (edge);
	//
	// // is a triangle contains pt ??
	// if (side1 == PluckerSide.ON) {
	// if ( side2 == PluckerSide.ON ) {
	// return false;
	// } // on vertex 1 !
	// if (side3 == PluckerSide.ON ) {
	// return false;
	// } // on vertex 0 !
	// if ( side2 == side3 ) {
	// return !isEdge[i][0];
	// }
	// }
	// else if (side2 == PluckerSide.ON) {
	// if (side3 == PluckerSide.ON) {
	// return false;
	// } // on vertex 2
	// if (side1 == side3) {
	// return !isEdge[i][1];
	// }
	// }
	// else if (side3 == PluckerSide.ON) {
	// if (side2 == side1) {
	// return !isEdge[i][2];
	// }
	// }
	// else if (side1 == side2 && side2 == side3) {
	// return true;
	// }
	// }
	// return false;
	// }

	// static final public boolean debug = false;

	// @Override
	// final public boolean visible (Ray ray)
	// {
	// final double vd = normale.dot(ray.direction);
	// if ( debug )
	// {
	// IOProvider.getDefault().getStdOut().println();
	// IOProvider.getDefault().getStdOut().println("Face = "+toString());
	// IOProvider.getDefault().getStdOut().println("vd = "+vd);
	// }
	// /* Intersection avec le plan (directions orthogonales) */
	// if (vd == 0.D) { return true; }
	//
	// final double v0 = normale.dot(sommets[0])-normale.dot(ray.from);
	// final double distance = v0/vd;
	// if ( debug )
	// {
	// IOProvider.getDefault().getStdOut().println("v0 = "+v0);
	// IOProvider.getDefault().getStdOut().println("distance = "+distance+" vs
	// "+ray.distance);
	// }
	// if (distance < DISTANCE_MIN
	// || distance > ray.distance-DISTANCE_MIN) {
	// return true;
	// }
	//
	// if (sommets.length == 4)
	// {
	// final double prod1 = pluckerSide (ray, sommets[0], sommets[1]);
	// final double prod2 = pluckerSide (ray, sommets[1], sommets[2]);
	// if (prod1 * prod2 < 0D) {
	// return true;
	// }
	// final double prod3 = pluckerSide(ray, sommets[2], sommets[3]);
	// if (prod2 * prod3 < 0D) {
	// return true;
	// }
	// final double prod4 = pluckerSide (ray, sommets[3], sommets[0]);
	// if (prod3 * prod4 < 0D) {
	// return true;
	// }
	// return prod4*prod1 < 0D;
	// /*Plucker line = new Plucker (ray.direction);
	// final Vector3d s1 = new Vector3d(sommets[0]);
	// final Vector3d s2 = new Vector3d(sommets[1]);
	// s1.sub (ray.from);
	// s2.sub (ray.from);
	// final double prod1 = line.side (new Plucker(s1, s2));
	// s1.set(s2);
	// s2.sub(sommets[2], ray.from);
	// final double prod2 = line.side (new Plucker(s1, s2));
	// if (prod1 * prod2 < 0D) return true;
	// s1.set(s2);
	// s2.sub(sommets[3], ray.from);
	// final double prod3 = line.side (new Plucker(s1, s2));
	// if (prod2 * prod3 < 0D) return true;
	// s1.set(s2);
	// s2.sub(sommets[0], ray.from);
	// final double prod4 = line.side (new Plucker(s1, s2));
	// if (prod3 * prod4 < 0D) return true;
	// return prod4*prod1 < 0D;*/
	// }
	//
	// final double Interx = distance*ray.direction.x + ray.from.x;
	// final double Intery = distance*ray.direction.y + ray.from.y;
	// final double Interz = distance*ray.direction.z + ray.from.z;
	// int nc = 0;
	// if ( debug )
	// {
	// IOProvider.getDefault().getStdOut().println("pt = ( "+Interx+" ,
	// "+Intery+" , "+Interz+" )");
	// }
	// /* est-ce que ce point est dans la face ? */
	// double Aix = sommets[0].x;
	// double Aiy = sommets[0].y;
	// double Aiz = sommets[0].z;
	// double Ajx = sommets[sommets.length-1].x - Interx;
	// double Ajy = sommets[sommets.length-1].y - Intery;
	// double Ajz = sommets[sommets.length-1].z - Interz;
	// int sh;
	//
	// switch (normalXYZ) {
	// case X : // U == Y, V == Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aiy=Ajy; Aiz=Ajz;
	// Ajy = sommets[i].y - Intery;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.0 ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aiy >= 0.D || Ajy >= 0.D) &&
	// (Aiy - Aiz*(Ajy-Aiy) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Y : // U = X V = Z
	// sh = Ajz >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix=Ajx; Aiz=Ajz;
	// Ajx = sommets[i].x - Interx;
	// Ajz = sommets[i].z - Interz;
	// final int nsh = Ajz >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiz*(Ajx-Aix) / (Ajz-Aiz) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// case Z : // U=X V=Y
	// sh = Ajy >= 0.D ? +1 : -1;
	// for (int i = 0; i < sommets.length; i++) {
	// Aix = Ajx; Aiy = Ajy;
	// Ajx = sommets[i].x - Interx;
	// Ajy = sommets[i].y - Intery;
	// final int nsh = Ajy >= 0.D ? +1 : -1;
	// if ( (sh != nsh) &&
	// (Aix >= 0.D || Ajx >= 0.D) &&
	// (Aix - Aiy*(Ajx-Aix) / (Ajy-Aiy) >= 0.D) ) {
	// nc++;
	// }
	// sh = nsh;
	// }
	// break;
	// }
	//
	// if ((nc & 1) != 0) {
	// //ray.distance = distance;
	// //IOProvider.getDefault().getStdOut().println ("face.intersection at
	// distance="+distance+" vd="+vd);
	// //ray.to.set(Interx, Intery, Interz);
	// return false; /* BINGO */
	// }
	//
	// return true;
	// }

	// @Override
	public Face[] getFaces() {
		Face[] res = new Face[1];
		res[0] = this;
		return res;
	}

	// @Override
	// public final BoundingBox getBBox () {
	// return bbox;
	// }

	/**
	 * Print to <code>StdOut</code> the list of vertices of this face
	 */
	// public void printFace() {
	// for (int i=0; i<sommets.length; i++) {
	// IOProvider.getDefault().getStdOut().println (sommets[i]);
	// }
	// }

	@Override
	public String toString() {
		String res = new String("Face with " + sommets.length + " vertices\n");
		for (int i = 0; i < sommets.length; i++) {
			res += " v[ " + i + " ] = " + (sommets[i]) + "\n";
		}
		return res;
	}

	/**
	 * Print to <code>out</code> the list of vertices of this face
	 * 
	 * @param out
	 *            The PrintStream to use for printing this faces
	 */
	// public void printFace(OutputWriter out) {
	// for (int i=0; i<sommets.length; i++) {
	// out.println (sommets[i]);
	// }
	// }

	/**
	 * Return the normal to this face instance
	 * 
	 * @return the normale
	 */
	public final Vector3d getNormale() {
		return normale;
	}

	public final Vector3d[] getVertices() {
		return sommets;
	}

	public final Edge[] getEdges() {
		Edge[] res = new Edge[sommets.length];

		for (int i = 0; i < sommets.length; i++) {
			res[i] = new Edge(sommets[i], sommets[(i + 1) % sommets.length], this);
		}

		return res;
	}

	/**
	 * Set a new color for visualisation
	 * 
	 * @param col
	 *            the new color
	 */
	public void setCouleur(final Color col) {
		couleur = col;
	}

	/**
	 * Computes the specular vector w.r.t. an incident one
	 * 
	 * @param incident
	 *            the incident vector
	 * @return a specular vector
	 **/
	final public Vector3d reflection(Vector3d incident) {
		Vector3d reflected = new Vector3d(incident);// Modif Pierre 14/04/11
		reflected.scaleAdd(-2.D * reflected.dot(normale), normale, incident);
		return reflected;
	}

	/**
	 * Computes the specular vector w.r.t. an incident one
	 * 
	 * @param incident
	 *            the incident vector
	 * @param res
	 *            where to store the result
	 **/
	final public void reflection(final Vector3d incident, Vector3d res) {
		res.scaleAdd(-2.D * res.dot(normale), normale, incident);
	}

	/**
	 * Computes the symetrical point
	 * 
	 * @param point
	 *            a given point
	 * @return the symetric of <code>point</code>
	 **/
	final public Vector3d symetrique(final Vector3d point) {
		Vector3d sym = new Vector3d(point);
		sym.sub(sommets[0]);
		final double t = -2.D * sym.dot(normale);
		sym.scaleAdd(t, normale, point);
		return sym;
	}

	/**
	 * Computes the symetrical point
	 * 
	 * @param point
	 *            a given point
	 * @param sym
	 *            where to store the result
	 **/
	final public void symetrique(final Vector3d point, final Vector3d sym) {
		sym.set(point);
		sym.sub(sommets[0]);
		final double t = -2.D * sym.dot(normale);
		sym.scaleAdd(t, normale, point);
	}

	/**
	 * Computes the symetrical Vector
	 * 
	 * @param vec
	 *            vector for which we computes the symetrical
	 * @param sym
	 *            the symetrical vector of <code>vec</code>
	 **/
	final public void symetriqueVector(final Vector3d vec, final Vector3d sym) {
		final double t = -2.D * vec.dot(normale);
		sym.scaleAdd(t, normale, vec);
	}

	/**
	 * Returns (after computing it iff necessary) the convex hull of this face
	 * instance. Notice that the algorithm used is QHull.
	 * 
	 * @return The vertices representing the convex hull of this face
	 */
	public Vector3d[] getHull() {
		if (hull != null) { // already computed
			return hull;
		}

		EdgePlus face1, face2;
		ArrayList<EdgePlus> faces = new ArrayList<EdgePlus>();

		Vector3d pts[] = new Vector3d[sommets.length];
		for (int i = 0; i < sommets.length; ++i) {
			pts[i] = sommets[i];
		}

		// puts in pts[0] value with min x
		// puts in pts[1] value with max x
		final double normaleX = Math.abs(normale.x);
		final double normaleY = Math.abs(normale.y);
		final double normaleZ = Math.abs(normale.z);

		NormalT NormalXYZ = normaleX <= normaleY && normaleX <= normaleZ ? NormalT.X
				: normaleY <= normaleZ ? NormalT.Y : NormalT.Z;
		switch (NormalXYZ) {
		case X:
			for (int i = 0; i < pts.length; i++) {
				if (pts[i].x < pts[0].x) {
					Vector3d tmp = pts[0];
					pts[0] = pts[i];
					pts[i] = tmp;
				}
				if (pts[i].x > pts[1].x) {
					Vector3d tmp = pts[1];
					pts[1] = pts[i];
					pts[i] = tmp;
				}
			}
			break;
		case Y:
			for (int i = 0; i < pts.length; i++) {
				if (pts[i].y < pts[0].y) {
					Vector3d tmp = pts[0];
					pts[0] = pts[i];
					pts[i] = tmp;
				}
				if (pts[i].y > pts[1].y) {
					Vector3d tmp = pts[1];
					pts[1] = pts[i];
					pts[i] = tmp;
				}
			}
			break;
		case Z:
			for (int i = 0; i < pts.length; i++) {
				if (pts[i].z < pts[0].z) {
					Vector3d tmp = pts[0];
					pts[0] = pts[i];
					pts[i] = tmp;
				}
				if (pts[i].z > pts[1].z) {
					Vector3d tmp = pts[1];
					pts[1] = pts[i];
					pts[i] = tmp;
				}
			}
			break;
		}

		// System.err.println ("\nQHULL ...");
		// printFace(System.err);
		// System.err.println ("pts[0]="+pts[0]+"\npts[1]="+pts[1]);
		faces.add(face1 = new EdgePlus(pts[0], pts[1]));
		faces.add(face2 = new EdgePlus(pts[1], pts[0]));

		// associate remaining points with one of these two faces */
		for (int i = 2; i < pts.length; ++i) {
			if (!face1.add(pts[i])) {
				face2.add(pts[i]);
			}
		}

		/* Each time around the main loop we process one face */
		int i = 0;
		while (i < faces.size()) {
			EdgePlus selected = faces.get(i);
			Vector3d newp = selected.extreme();
			// System.err.println ("Select edge "+i);
			// System.err.println (" get "+selected.pts.size()+" candidats");
			if (newp == null) {
				++i;
				// System.err.println (" SKIP");
				continue;
			}
			faces.set(i, face1 = new EdgePlus(selected.a, newp));
			faces.add(i + 1, face2 = new EdgePlus(newp, selected.b));
			// System.err.println ("new size = "+faces.size());
			for (int k = 0; k < selected.pts.size(); k++) {
				Vector3d p = selected.pts.get(k);
				if (p != newp) {
					if (!face1.add(p)) {
						face2.add(p);
					}
				}
			}
		}

		hull = new Vector3d[faces.size()];
		for (i = 0; i < faces.size(); i++) {
			hull[i] = faces.get(i).a;
		}

		// if (hull.length < 3) {
		// IOProvider.getDefault().getStdOut().println ("Increidible! Convex
		// Hull contains less than 3 points!");
		// printFace ();
		// for (i=0; i<hull.length; i++) {
		// IOProvider.getDefault().getStdOut().println ("Hull["+i+"] =
		// "+hull[i]);
		// }
		// System.exit(-1);
		// }
		return hull;
	}

	private class EdgePlus {
		//static final long serialVersionUID = 1L;
		ArrayList<Vector3d> pts;
		final Vector3d a;
		final Vector3d b;
		final Vector3d enormal;
		final double d;

		public EdgePlus(Vector3d start, Vector3d end) {
			pts = new ArrayList<Vector3d>();
			a = start;
			b = end;
			enormal = new Vector3d(); // b.y - a.y, a.x - b.x, 0.D);
			Vector3d ab = new Vector3d(b);
			ab.sub(a);
			enormal.cross(ab, normale);
			d = enormal.dot(a);
			// System.err.println ("Make an EdgePlus : "+a+"->"+b+",
			// N="+enormal);
		}

		private boolean inside(Vector3d x) {
			return enormal.dot(x) > d;
		}

		public boolean add(Vector3d p) {
			if (inside(p)) {
				pts.add(p);
				return true;
			}
			return false;
		}

		public Vector3d extreme() {
			if (pts == null) {
				return null;
			}
			Vector3d res = null;
			double maxd = d;
			for (int i = 0; i < pts.size(); i++) {
				final Vector3d ith = pts.get(i);
				final double dd = enormal.dot(ith);
				if (dd > maxd) {
					res = ith;
					maxd = dd;
				}
			}
			return res;
		}
	}

	public void symetricalVector(Vector3d at, Vector3d in, Vector3d out) {
		symetriqueVector(in, out);
	}

	public Vector3d getNormalAt(Vector3d at) {
		return normale;
	}

	/**
	 * Returns the normal at a given point.
	 * 
	 * @param at
	 *            the point where the normal must be set
	 * @param normal
	 *            the result
	 */
	public void getNormalAt(Vector3d at, Vector3d normal) {
		normal.set(normale);
	}

	static enum PluckerSide {
		MINUS, ON, PLUS
	};

	private class Plucker {
		final double Pi[];

//		public Plucker() {
//			Pi = new double[6];
//		}

		public void set(final Vector3d a, final Vector3d b) {
			Pi[0] = b.x - a.x;
			Pi[1] = b.y - a.y;
			Pi[2] = b.z - a.z;
			Pi[3] = a.y * b.z - a.z * b.y;
			Pi[4] = a.z * b.x - a.x * b.z;
			Pi[5] = a.x * b.y - a.y * b.x;
		}

		public Plucker(final Vector3d a, final Vector3d b) {
			Pi = new double[6];
			set(a, b);
		}

		public final double volumeSize(final Plucker l) {
			return Pi[0] * l.Pi[3] + Pi[1] * l.Pi[4] + Pi[2] * l.Pi[5] + Pi[3] * l.Pi[0] + Pi[4] * l.Pi[1]
					+ Pi[5] * l.Pi[2];
		}

//		public final PluckerSide side(final Plucker l) {
//			final double v = Pi[0] * l.Pi[3] + Pi[1] * l.Pi[4] + Pi[2] * l.Pi[5] + Pi[3] * l.Pi[0] + Pi[4] * l.Pi[1]
//					+ Pi[5] * l.Pi[2];
//
//			return v < 0.D ? PluckerSide.MINUS : v > 0.D ? PluckerSide.PLUS : PluckerSide.ON;
//		}
//
//		public final boolean areCoplanar(final Plucker l) {
//			final double dot1 = (Pi[0] * l.Pi[3] + Pi[1] * l.Pi[4] + Pi[2] * l.Pi[5]);
//			final double dot2 = -(Pi[3] * l.Pi[0] + Pi[4] * l.Pi[1] + Pi[5] * l.Pi[2]);
//
//			return dot1 == dot2;
//		}
	}

	// final private double pluckerSide (final Ray ray, final Vector3d a, final
	// Vector3d b)
	// {
	// final double xa = a.x - ray.from.x;
	// final double ya = a.y - ray.from.y;
	// final double za = a.z - ray.from.z;
	// final double xb = b.x - ray.from.x;
	// final double yb = b.y - ray.from.y;
	// final double zb = b.z - ray.from.z;
	//
	// final double x = ya*zb - za*yb;
	// final double y = za*xb - xa*zb;
	// final double z = xa*yb - ya*xb;
	//
	// return ray.direction.x*x + ray.direction.y*y + ray.direction.z*z;
	// }

	//////////////////////////////////////////////////////////////////////////
	// parametric curve implantation
	public Vector3d uv2Vector3d(double u, double v) {
		Vector3d P = new Vector3d(0D, 0D, 0D);
		uv2Vector3d(u, v, P);
		return P;
	}

	public void uv2Vector3d(double u, double v, Vector3d res) {
		res.x = sommets[0].x + u * this.u.x + v * this.v.x;
		res.y = sommets[0].y + u * this.u.y + v * this.v.y;
		res.z = sommets[0].z + u * this.u.z + v * this.v.z;
	}

	public double getUCoordinate(Vector3d P) {
		final Vector3d V = new Vector3d(P);
		V.sub(sommets[0]);
		return V.dot(u);
	}

	public double getVCoordinate(Vector3d P) {
		final Vector3d V = new Vector3d(P);
		V.sub(sommets[0]);
		return V.dot(v);
	}

	public Vector2d getUVCoordinate(Vector3d P) {
		final Vector3d V = new Vector3d(P);
		V.sub(sommets[0]);
		return new Vector2d(V.dot(u), V.dot(v));
	}

	public Vector3d uDerivative(Vector3d P) {
		return new Vector3d(u);
	}

	public void uDerivative(Vector3d P, Vector3d der) {
		der.set(u);
	}

	public Vector3d vDerivative(Vector3d P) {
		return new Vector3d(v);
	}

	public void vDerivative(Vector3d P, Vector3d der) {
		der.set(v);
	}

	public Vector3d uvDerivative(Vector3d P) {
		return new Vector3d();
	}

	public void uvDerivative(Vector3d P, Vector3d der) {
		der.set(0D, 0D, 0D);
	}

	public Vector3d vuDerivative(Vector3d P) {
		return new Vector3d();
	}

	public void vuDerivative(Vector3d P, Vector3d der) {
		der.set(0D, 0D, 0D);
	}

	public Vector3d uuDerivative(Vector3d P) {
		return new Vector3d();
	}

	public void uuDerivative(Vector3d P, Vector3d der) {
		der.set(0D, 0D, 0D);
	}

	public Vector3d vvDerivative(Vector3d P) {
		return new Vector3d();
	}

	public void vvDerivative(Vector3d P, Vector3d der) {
		der.set(0D, 0D, 0D);
	}
}
