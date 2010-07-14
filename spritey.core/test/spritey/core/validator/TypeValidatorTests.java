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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the implementation of MapBasedNode.
 */
public class TypeValidatorTests {

    @Test
    public void isValidWhenExpectedStringActualString() {
        Validator validator = new TypeValidator(String.class);

        assertTrue(validator.isValid(new String()));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedStringActualObject() {
        Validator validator = new TypeValidator(String.class);

        assertFalse(validator.isValid(new Object()));
        assertEquals(TypeValidator.NOT_TYPE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedStringActualNull() {
        Validator validator = new TypeValidator(String.class);

        assertFalse(validator.isValid(null));
        assertEquals(TypeValidator.NOT_TYPE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

}
