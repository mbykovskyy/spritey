/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
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

import java.awt.Dimension;

/**
 * Validates that the value satisfies min and max dimension requirements.
 */
public class SizeValidator extends AbstractValidator {

    // Error codes (each code has to be unique across the whole application)
    public static final int INVALID_SIZE = 30;
    public static final int WIDTH_TOO_SHORT = 31;
    public static final int WIDTH_TOO_LONG = 32;
    public static final int HEIGHT_TOO_SHORT = 33;
    public static final int HEIGHT_TOO_LONG = 34;

    // Messages
    private static final String M_INVALID_SIZE = "Object is not of type Dimension or size is not valid.";
    private static final String M_WIDTH_TOO_SHORT = "Width is too short.";
    private static final String M_WIDTH_TOO_LONG = "Width is too long.";
    private static final String M_HEIGHT_TOO_SHORT = "Height is too short.";
    private static final String M_HEIGHT_TOO_LONG = "Height is too long.";

    private Dimension min;
    private Dimension max;

    /**
     * Constructor
     * 
     * @param min
     *        the minimum size.
     * @param max
     *        the maximum size.
     */
    public SizeValidator(Dimension min, Dimension max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid(Object value) {
        int code = getErrorCode();
        String message = getMessage();
        boolean isValid = false;

        if (value instanceof Dimension) {
            Dimension size = (Dimension) value;

            if (size.width > max.width) {
                code = WIDTH_TOO_LONG;
                message = M_WIDTH_TOO_LONG;
            } else if (size.width < min.width) {
                code = WIDTH_TOO_SHORT;
                message = M_WIDTH_TOO_SHORT;
            } else if (size.height > max.height) {
                code = HEIGHT_TOO_LONG;
                message = M_HEIGHT_TOO_LONG;
            } else if (size.height < min.height) {
                code = HEIGHT_TOO_SHORT;
                message = M_HEIGHT_TOO_SHORT;
            } else {
                isValid = true;
            }
        } else {
            code = INVALID_SIZE;
            message = M_INVALID_SIZE;
        }

        setErrorCode(code);
        setMessage(message);
        return isValid;
    }

}
