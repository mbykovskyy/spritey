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

import org.eclipse.draw2d.geometry.Dimension;
import org.junit.Before;
import org.junit.Test;

import spritey.core.validator.Validator;

/**
 * Tests the implementation of SizeValidator.
 */
public class SizeValidatorTests {

    static final Dimension MIN = new Dimension(1, 1);
    static final Dimension MAX = new Dimension(64, 64);

    Validator validator;

    @Before
    public void initialize() {
        validator = new SizeValidator(MIN, MAX);
    }

    @Test
    public void isValidWidthAndHeightShorterThanMax() {
        assertTrue(validator.isValid(new Dimension(MAX.width - 1,
                MAX.height - 1)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWidthAndHeightLongerThanMin() {
        assertTrue(validator.isValid(new Dimension(MIN.width + 1,
                MIN.height + 1)));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWidthAndHeightEqualToMax() {
        assertTrue(validator.isValid(MAX));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWidthAndHeightEqualToMin() {
        assertTrue(validator.isValid(MIN));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWidthLongerThanMax() {
        assertFalse(validator.isValid(new Dimension(MAX.width + 1,
                MAX.height - 1)));
        assertEquals(SizeValidator.WIDTH_TOO_LONG, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidHeightLongerThanMax() {
        assertFalse(validator.isValid(new Dimension(MAX.width - 1,
                MAX.height + 1)));
        assertEquals(SizeValidator.HEIGHT_TOO_LONG, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWidthShorterThanMin() {
        assertFalse(validator.isValid(new Dimension(MIN.width - 1,
                MIN.height + 1)));
        assertEquals(SizeValidator.WIDTH_TOO_SHORT, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidHeightShorterThanMin() {
        assertFalse(validator.isValid(new Dimension(MIN.width + 1,
                MIN.height - 1)));
        assertEquals(SizeValidator.HEIGHT_TOO_SHORT, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidNull() {
        assertFalse(validator.isValid(null));
        assertEquals(SizeValidator.INVALID_SIZE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidObject() {
        assertFalse(validator.isValid(new Object()));
        assertEquals(SizeValidator.INVALID_SIZE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

}
