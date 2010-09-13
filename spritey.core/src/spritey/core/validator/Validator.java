/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
 * 
 * Spritey is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Spritey is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Spritey. If not, see <http://www.gnu.org/licenses/>.
 */
package spritey.core.validator;

/**
 * Validator verifies that a value meets certain validation requirements.
 */
// TODO Look at how this can be made generic. At the moment validators are added
// to a list, therefore, making this Validator<T> and a list of validators
// List<Validator<?>> causes a lot of headache. For instance, it is not possible
// to call ((Validator<?>) validator).isValid(new Object()). Java 7 may
// introduce reified generics that may have a solution to this.
public interface Validator {

    // Error codes (each code has to be unique across the whole application)
    public static int NONE = -1;

    /**
     * Verifies that the specified value meets validation requirements. Use
     * <code>getMessage()</code> for explanation why the validation failed.
     * 
     * @param value
     *        the value to validate.
     * @return <code>true</code> if the value meets validation requirements,
     *         otherwise <code>false</code>.
     */
    public boolean isValid(Object value);

    /**
     * Returns a descriptive message that explains why <code>isValid()</code>
     * returned <code>false</code>.
     * 
     * @return if <code>isValid()</code> was never called or returned
     *         <code>true</code>, then this method returns an empty string.
     *         Otherwise, a descriptive message is returned.
     */
    public String getMessage();

    /**
     * Returns an error code what explains why <code>isValid()</code> returned
     * <code>false</code>.
     * 
     * @return if <code>isValid()</code> was never called or returned
     *         <code>true</code>, then this method returns <code>NONE</code>
     *         error code, which is equal to 0 (zero). Otherwise, an error code
     *         is returned.
     */
    public int getErrorCode();

}
