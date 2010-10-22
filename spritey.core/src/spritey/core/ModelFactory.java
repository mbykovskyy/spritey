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

import java.awt.Color;
import java.awt.Dimension;

import spritey.core.validator.NotNullValidator;
import spritey.core.validator.NullOrTypeValidator;
import spritey.core.validator.SizeValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.core.validator.Validator;

/**
 * A factory for creating models.
 */
public class ModelFactory {

    /**
     * Creates a new sheet.
     * 
     * @return an instance of a new sheet.
     */
    public Sheet createSheet() {
        Validator notNullValidator = new NotNullValidator();

        Sheet sheet = new Sheet();
        sheet.addValidator(Sheet.BACKGROUND, new NullOrTypeValidator(
                Color.class));

        sheet.addValidator(Sheet.OPAQUE, notNullValidator);
        sheet.addValidator(Sheet.OPAQUE, new TypeValidator(Boolean.class));

        sheet.addValidator(Sheet.DESCRIPTION, notNullValidator);
        sheet.addValidator(Sheet.DESCRIPTION, new TypeValidator(String.class));
        sheet.addValidator(Sheet.DESCRIPTION, new StringLengthValidator(
                Sheet.MIN_DESCRIPTION_LENGTH, Sheet.MAX_DESCRIPTION_LENGTH));

        sheet.addValidator(Sheet.SIZE, notNullValidator);
        sheet.addValidator(Sheet.SIZE, new TypeValidator(Dimension.class));

        Dimension min = new Dimension(Sheet.MIN_WIDTH, Sheet.MIN_HEIGHT);
        Dimension max = new Dimension(Sheet.MAX_WIDTH, Sheet.MAX_HEIGHT);
        sheet.addValidator(Sheet.SIZE, new SizeValidator(min, max));

        return sheet;
    }

    /**
     * Creates a new sprite.
     * 
     * @return an instance of a new sprite.
     */
    public Sprite createSprite() {
        Validator notNullValidator = new NotNullValidator();

        Sprite sprite = new Sprite();

        sprite.addValidator(Sprite.NAME, notNullValidator);
        sprite.addValidator(Sprite.NAME, new TypeValidator(String.class));
        sprite.addValidator(Sprite.NAME, new StringLengthValidator(
                Sprite.MIN_NAME_LENGTH, Sprite.MAX_NAME_LENGTH));
        sprite.addValidator(Sprite.NAME, new UniqueNameValidator(sprite));

        sprite.addValidator(Sprite.BOUNDS, notNullValidator);

        sprite.addValidator(Sprite.IMAGE, notNullValidator);

        return sprite;
    }

    /**
     * Creates a new group.
     * 
     * @return an instance of a new group.
     */
    public Group createGroup() {
        Group group = new Group();
        group.addValidator(Group.NAME, new NotNullValidator());
        group.addValidator(Group.NAME, new TypeValidator(String.class));
        group.addValidator(Group.NAME, new StringLengthValidator(
                Group.MIN_NAME_LENGTH, Group.MAX_NAME_LENGTH));
        group.addValidator(Group.NAME, new UniqueNameValidator(group));

        return group;
    }

}
