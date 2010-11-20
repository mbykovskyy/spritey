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
package spritey.rcp.wizards.addsprites;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.validator.AbstractValidator;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.Validator;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.SpriteConstants;

/**
 * Validator for validating NEW_NAME property value.
 */
public class NewNameValidator extends AbstractValidator {

    // Error codes (each code has to be unique across the whole application)
    public static final int NAME_NOT_UNIQUE = 70;

    // Messages
    private static final String M_NAME_NOT_UNIQUE = "Name is not unique.";

    private Validator[] validators;
    private Model model;

    /**
     * Creates a new instance of NewNameValidator.
     * 
     * @param model
     *        the model to validate NEW_NAME.
     */
    public NewNameValidator(Model model) {
        this.model = model;
        int min = model instanceof Sprite ? Sprite.MIN_NAME_LENGTH
                : Group.MIN_NAME_LENGTH;
        int max = model instanceof Group ? Sprite.MAX_NAME_LENGTH
                : Group.MAX_NAME_LENGTH;

        validators = new Validator[] { new NotNullValidator(),
                new TypeValidator(String.class),
                new StringLengthValidator(min, max) };
    }

    @Override
    public boolean isValid(Object value) {
        for (Validator validator : validators) {
            if (!validator.isValid(value)) {
                setErrorCode(validator.getErrorCode());
                setMessage(validator.getMessage());
                return false;
            }
        }

        Model parent = model.getParent();
        if (null != parent) {
            for (Model child : parent.getChildren()) {
                if (child == model) {
                    continue;
                }

                int property = (child instanceof Sprite) ? SpriteConstants.NEW_NAME
                        : GroupConstants.NEW_NAME;
                String childName = (String) child.getProperty(property);

                if ((null != childName) && childName.equals(value)) {
                    setErrorCode(NAME_NOT_UNIQUE);
                    setMessage(M_NAME_NOT_UNIQUE);
                    return false;
                }
            }
        }
        return true;
    }

}
