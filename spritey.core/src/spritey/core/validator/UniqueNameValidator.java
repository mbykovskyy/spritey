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

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.node.Node;
import spritey.core.validator.AbstractValidator;

/**
 * Validates that the name is unique within model's parent. A parent can be a
 * group or a sprite sheet.
 */
public class UniqueNameValidator extends AbstractValidator {

    // Error codes (each code has to be unique across the whole application)
    public static final int INVALID_NAME = 40;
    public static final int NAME_NOT_UNIQUE = 41;

    // Messages
    private static final String M_INVALID_NAME = "Name is not of type String.";
    private static final String M_NAME_NOT_UNIQUE = "Name is not unique.";

    private Model model;
    private int nodeProperty;
    private int nameProperty;

    /**
     * Constructor
     * 
     * @param model
     *        the sprite model to validate the name.
     */
    public UniqueNameValidator(Sprite model) {
        this.model = model;
        nodeProperty = Sprite.NODE;
        nameProperty = Sprite.NAME;
    }

    /**
     * Constructor
     * 
     * @param model
     *        the group model to validate the name.
     */
    public UniqueNameValidator(Group model) {
        this.model = model;
        nodeProperty = Group.NODE;
        nameProperty = Group.NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.validator.Validator#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object value) {
        if (value instanceof String) {
            Node node = (Node) model.getProperty(nodeProperty);

            if (null != node) {
                Node parentNode = node.getParent();

                if (null != parentNode) {
                    for (Node child : parentNode.getChildren()) {
                        String childName = (String) child.getModel()
                                .getProperty(nameProperty);

                        if (childName.equals(value)) {
                            setErrorCode(NAME_NOT_UNIQUE);
                            setMessage(M_NAME_NOT_UNIQUE);
                            return false;
                        }
                    }
                }
            }
            // Went through all the children and we're still here. The new name
            // must be unique.
            return true;
        }

        setErrorCode(INVALID_NAME);
        setMessage(M_INVALID_NAME);
        return false;
    }

}
