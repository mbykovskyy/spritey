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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the implementation of NullOrTypeValidator.
 */
public class NullOrTypeValidatorTests {

    Validator validator;

    @Before
    public void initialize() {
        validator = new NullOrTypeValidator(String.class);
    }

    @Test
    public void isValidWhenExpectedStringActualString() {
        assertTrue(validator.isValid(new String()));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedStringActualObject() {
        assertFalse(validator.isValid(new Object()));
        assertEquals(TypeValidator.NOT_TYPE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedStringActualNull() {
        assertTrue(validator.isValid(new String()));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

}
