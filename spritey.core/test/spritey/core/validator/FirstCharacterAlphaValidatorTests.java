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
 * Tests the implementation of FirstCharacterAlphaValidator.
 */
public class FirstCharacterAlphaValidatorTests {

    static final String ALPHA_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String NON_ALPHA_CHARACTERS = "!£$%^&*()_+-=0123456789[]{}'#@~,./<>?|";

    Validator validator;

    @Before
    public void initialize() {
        validator = new FirstCharacterAlphaValidator();
    }

    private String generateString(char firstCh) {
        return firstCh + "_1321-23.@";
    }

    @Test
    public void isValidWhenFirstCharacterIsAlpha() {
        for (char ch : ALPHA_CHARACTERS.toCharArray()) {
            String str = generateString(ch);
            String message = str + " is invalid.";
            assertTrue(message, validator.isValid(str));
            assertEquals(message, Validator.NONE, validator.getErrorCode());
            assertNotNull(message, validator.getMessage());
            assertTrue(message, validator.getMessage().isEmpty());
        }
    }

    @Test
    public void isValidWhenFirstCharacterIsNotAlpha() {
        for (char ch : NON_ALPHA_CHARACTERS.toCharArray()) {
            String str = generateString(ch);
            String message = str + " is valid.";
            assertFalse(message, validator.isValid(str));
            assertEquals(message, FirstCharacterAlphaValidator.NOT_ALPHA,
                    validator.getErrorCode());
            assertNotNull(message, validator.getMessage());
            assertFalse(message, validator.getMessage().isEmpty());
        }
    }

}
