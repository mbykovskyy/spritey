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
 * Tests the implementation of StringLengthValidator.
 */
public class StringLengthValidatorTests {

    static final int MIN = 1;
    static final int MAX = 256;

    Validator validator;

    @Before
    public void initialize() {
        validator = new StringLengthValidator(MIN, MAX);
    }

    private String generateString(int length) {
        String text = "";

        for (int i = 0; i < length; ++i) {
            text += "a";
        }

        return text;
    }

    @Test
    public void isValidShorterThanMax() {
        assertTrue(validator.isValid(generateString(MAX - 1)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidLongerThanMin() {
        assertTrue(validator.isValid(generateString(MIN + 1)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidEqualToMax() {
        assertTrue(validator.isValid(generateString(MAX)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidEqualToMin() {
        assertTrue(validator.isValid(generateString(MIN)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidShorterThanMin() {
        assertFalse(validator.isValid(generateString(MIN - 1)));
        assertEquals(StringLengthValidator.TOO_SHORT, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidLongerThanMax() {
        assertFalse(validator.isValid(generateString(MAX + 1)));
        assertEquals(StringLengthValidator.TOO_LONG, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

}
