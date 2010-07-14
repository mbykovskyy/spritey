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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import spritey.core.validator.TypeValidator;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of BackgroundValidator.
 */
public class BackgroundValidatorTests {

    @Test
    public void isValidWhenExpectedRgbActualRgb() {
        Validator validator = new BackgroundValidator(RGB.class);

        assertTrue(validator.isValid(new RGB(0, 0, 0)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedRgbActualObject() {
        Validator validator = new BackgroundValidator(RGB.class);

        assertFalse(validator.isValid(new Object()));
        assertEquals(TypeValidator.NOT_TYPE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenExpectedRgbActualNull() {
        Validator validator = new BackgroundValidator(RGB.class);

        assertTrue(validator.isValid(null));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

}
