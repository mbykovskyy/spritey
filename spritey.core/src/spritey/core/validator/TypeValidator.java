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
 * Validates that the value is of the specified type.
 */
public class TypeValidator extends AbstractValidator {

    // Error codes (each code has to be unique across the whole application)
    public static final int NOT_TYPE = 20;

    // Messages
    private static final String M_NOT_TYPE = "Object is not of type ";

    private Class<?> type;

    /**
     * Constructor
     * 
     * @param type
     *        the expected type.
     */
    public TypeValidator(Class<?> type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.validator.Validator#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object value) {
        if (type.isInstance(value)) {
            return true;
        }

        setErrorCode(NOT_TYPE);
        setMessage(M_NOT_TYPE + " " + type);
        return false;
    }

}
