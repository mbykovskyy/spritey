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
package spritey.rcp.validators;

import spritey.core.validator.TypeValidator;

/**
 * Validates that the value is of type specified. This validator allows
 * <code>null</code> value.
 */
public class BackgroundValidator extends TypeValidator {

    /**
     * Constructor
     * <p>
     * This validator yields <code>null</code> value.
     * 
     * @param type
     *        the expected type.
     */
    public BackgroundValidator(Class<?> type) {
        super(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.validator.Validator#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object value) {
        if (null == value) {
            return true;
        }

        return super.isValid(value);
    }

}
