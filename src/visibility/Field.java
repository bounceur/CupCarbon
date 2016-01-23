/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visibility;

/**
 * A field is the physical quantity that can be propagated along paths and rays.
 * All fields must be converted to a single real values, as an equivalent
 * representation in decibel.
 * Moreover a field must be print, so an implementation must propose a
 * correct toString() method  overrideing.
 *
 * @author pcombeau
 */
public interface Field {
    /**
     * Convert a Field to decibels
     * @return decibel representation of field's instance
     */
    public double toDb ();

    @Override
    public String toString();
}
