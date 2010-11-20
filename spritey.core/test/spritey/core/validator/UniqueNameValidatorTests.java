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
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * Tests the implementation of UniqueNameValidator.
 */
public class UniqueNameValidatorTests {

    Validator validator;

    @Mock
    Group groupMock;

    @Mock
    Sprite spriteMock;

    @Mock
    Sprite siblingMock;

    @Mock
    Sheet sheetMock;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValidWhenSpriteHasNoSiblings() {
        Validator validator = new UniqueNameValidator(spriteMock);

        doReturn(new Model[0]).when(sheetMock).getChildren();
        doReturn(sheetMock).when(spriteMock).getParent();

        assertTrue(validator.isValid("unique-name"));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenSpriteHasSiblingWithDifferentName() {
        Validator validator = new UniqueNameValidator(spriteMock);

        doReturn("different-name").when(siblingMock).getProperty(Sprite.NAME);
        doReturn(new Model[] { siblingMock }).when(sheetMock).getChildren();
        doReturn(sheetMock).when(spriteMock).getParent();

        assertTrue(validator.isValid("unique-name"));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenSpriteHasSiblingWithSameName() {
        Validator validator = new UniqueNameValidator(spriteMock);

        doReturn("unique-name").when(siblingMock).getProperty(Sprite.NAME);
        doReturn(new Model[] { siblingMock }).when(sheetMock).getChildren();
        doReturn(sheetMock).when(spriteMock).getParent();

        assertFalse(validator.isValid("unique-name"));
        assertEquals(UniqueNameValidator.NAME_NOT_UNIQUE,
                validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenGroupHasNoSiblings() {
        Validator validator = new UniqueNameValidator(groupMock);

        doReturn(new Model[0]).when(sheetMock).getChildren();
        doReturn(sheetMock).when(groupMock).getParent();

        assertTrue(validator.isValid("unique-name"));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenGroupHasSiblingWithDifferentName() {
        Validator validator = new UniqueNameValidator(groupMock);

        doReturn("different-name").when(siblingMock).getProperty(Sprite.NAME);
        doReturn(new Model[] { siblingMock }).when(sheetMock).getChildren();
        doReturn(sheetMock).when(groupMock).getParent();

        assertTrue(validator.isValid("unique-name"));
        assertEquals(Validator.NONE, validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertTrue(validator.getMessage().isEmpty());
    }

    @Test
    public void isValidWhenGroupHasSiblingWithSameName() {
        Validator validator = new UniqueNameValidator(groupMock);

        doReturn("unique-name").when(siblingMock).getProperty(Sprite.NAME);
        doReturn(new Model[] { siblingMock }).when(sheetMock).getChildren();
        doReturn(sheetMock).when(groupMock).getParent();

        assertFalse(validator.isValid("unique-name"));
        assertEquals(UniqueNameValidator.NAME_NOT_UNIQUE,
                validator.getErrorCode());
        assertNotNull(validator.getMessage());
        assertFalse(validator.getMessage().isEmpty());
    }

}
