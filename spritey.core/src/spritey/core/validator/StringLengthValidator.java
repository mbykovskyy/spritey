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
 * Validates that the value is of type string.
 */
public class StringLengthValidator extends AbstractValidator {

    // Error codes
    public static final int INVALID = 0;
    public static final int TOO_SHORT = 1;
    public static final int TOO_LONG = 2;

    // Messages
    private static final String M_INVALID = "Object is not of type String or string is not valid.";
    private static final String M_TOO_SHORT = "String is too short.";
    private static final String M_TOO_LONG = "String is too long.";

    private int min;
    private int max;

    /**
     * Constructor
     * 
     * @param min
     *        the minimum length of a string.
     * @param max
     *        the maximum length of a string.
     */
    public StringLengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.validator.Validator#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object value) {
        int code = getErrorCode();
        String message = getMessage();
        boolean isValid = false;

        if (value instanceof String) {
            String str = (String) value;

            if (str.length() > max) {
                code = TOO_LONG;
                message = M_TOO_LONG;
            } else if (str.length() < min) {
                code = TOO_SHORT;
                message = M_TOO_SHORT;
            } else {
                isValid = true;
            }
        } else {
            code = INVALID;
            message = M_INVALID;
        }

        setErrorCode(code);
        setMessage(message);
        return isValid;
    }

}
