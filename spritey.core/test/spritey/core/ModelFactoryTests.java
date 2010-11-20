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
package spritey.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import spritey.core.validator.CharacterValidator;
import spritey.core.validator.FirstCharacterAlphaValidator;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.NullOrTypeValidator;
import spritey.core.validator.SizeValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of ModelFactory.
 */
public class ModelFactoryTests {

    ModelFactory factory;

    @Before
    public void initialize() {
        factory = new ModelFactory();
    }

    @Test
    public void createSheet() {
        Model model = factory.createSheet();

        Validator[] validators = model.getValidators(Sheet.BACKGROUND);
        assertEquals(1, validators.length);
        assertTrue(validators[0] instanceof NullOrTypeValidator);

        validators = model.getValidators(Sheet.OPAQUE);
        assertEquals(2, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);

        validators = model.getValidators(Sheet.DESCRIPTION);
        assertEquals(3, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);
        assertTrue(validators[2] instanceof StringLengthValidator);

        validators = model.getValidators(Sheet.SIZE);
        assertEquals(3, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);
        assertTrue(validators[2] instanceof SizeValidator);
    }

    @Test
    public void createSprite() {
        Model model = factory.createSprite();

        Validator[] validators = model.getValidators(Sprite.NAME);
        assertEquals(6, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);
        assertTrue(validators[2] instanceof StringLengthValidator);
        assertTrue(validators[3] instanceof UniqueNameValidator);
        assertTrue(validators[4] instanceof CharacterValidator);
        assertTrue(validators[5] instanceof FirstCharacterAlphaValidator);

        validators = model.getValidators(Sprite.BOUNDS);
        assertEquals(2, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);

        validators = model.getValidators(Sprite.IMAGE);
        assertEquals(2, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);
    }

    @Test
    public void createGroup() {
        Model model = factory.createGroup();

        Validator[] validators = model.getValidators(Group.NAME);
        assertEquals(6, validators.length);
        assertTrue(validators[0] instanceof NotNullValidator);
        assertTrue(validators[1] instanceof TypeValidator);
        assertTrue(validators[2] instanceof StringLengthValidator);
        assertTrue(validators[3] instanceof UniqueNameValidator);
        assertTrue(validators[4] instanceof CharacterValidator);
        assertTrue(validators[5] instanceof FirstCharacterAlphaValidator);
    }

}
