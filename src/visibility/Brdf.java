/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

import javax.vecmath.Vector3d;

/**
 * Bi-Directional Reflectance Functions: allows to describe the interaction
 * of a wave with a given surface, usable both for reflection and diffraction.
 * @param <F> the Wave Field nature this Brdf can be used with
 * @author pcombeau
 */
public interface Brdf <F extends Field> {
    /**
     * Exception raised when a BRDF is used with a bad wavelength
     */
    public class ExceptionWavelengthOutOfScope extends Exception
    {
        public static final long serialVersionUID = 1L;

        public ExceptionWavelengthOutOfScope(double wl) {
            super("Wavelength "+wl+" is out of scope this brdf");
        }
    }

    /**
     * Exception raised when the functions <code>reflect</code> or
     * <code>transmit</code> are called with bad parameters.
     */
    public class ExceptionArrayLengths extends Exception
    {
        public static final long serialVersionUID = 1L;
        public ExceptionArrayLengths (int s1, int s2) {
            super("wavelengths and result arrays have different sizes: "+s1+" and "+s2+"");
        }
    }
    
    /**
     * Computes physical reflexion of a given ray.
     * @param in
     * @param reflected
     * @param normal
     * @param tangent
     * @param wavelengths
     * @param result
     * @throws org.xlim.sic.ig.kernel.physics.Brdf.ExceptionWavelengthOutOfScope
     * @throws org.xlim.sic.ig.kernel.physics.Brdf.ExceptionArrayLengths
     */
    public void reflect( Vector3d in, Vector3d reflected,
                         Vector3d normal, Vector3d tangent,
                         double wavelengths[], F[] result )
            throws ExceptionWavelengthOutOfScope , ExceptionArrayLengths;


    /**
     * Computes physical refraction of a given ray.
     * @param in
     * @param refracted
     * @param normal
     * @param tangent
     * @param wavelengths
     * @param result
     * @throws org.xlim.sic.ig.kernel.physics.Brdf.ExceptionWavelengthOutOfScope
     * @throws org.xlim.sic.ig.kernel.physics.Brdf.ExceptionArrayLengths
     */
    public void transmit( Vector3d in, Vector3d refracted,
                         Vector3d normal, Vector3d tangent,
                         double wavelengths[], F[] result )
            throws ExceptionWavelengthOutOfScope , ExceptionArrayLengths;
}
