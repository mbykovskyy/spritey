/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2011 Maksym Bykovskyy.
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Spritey core message catalog.
 */
public class Messages {

    private static final String BUNDLE_NAME = "spritey.core.messages";

    public static String NULL;
    public static String NAME_LENGTH_INVALID;
    public static String NAME_NOT_UNIQUE;
    public static String CANNOT_ASSIGN_ITSELF_AS_PARENT;
    public static String CANNOT_ASSIGN_CHILD_AS_PARENT;

    public static String GROUP_DEFAULT_NAME;

    public static String SHEET_DEFAULT_NAME;
    public static String SHEET_DEFAULT_DESCRIPTION;
    public static String SHEET_MAX_WIDTH_INVALID_RANGE;
    public static String SHEET_MAX_HEIGHT_INVALID_RANGE;
    public static String SHEET_MAX_WIDTH_INVALID_POWER_OF_TWO;
    public static String SHEET_MAX_HEIGHT_INVALID_POWER_OF_TWO;
    public static String SHEET_WIDTH_INVALID_RANGE;
    public static String SHEET_HEIGHT_INVALID_RANGE;
    public static String SHEET_WIDTH_INVALID_POWER_OF_TWO;
    public static String SHEET_HEIGHT_INVALID_POWER_OF_TWO;
    public static String SHEET_DESCRIPTION_LENGTH_INVALID;

    public static String SPRITE_DEFAULT_NAME;
    public static String SPRITE_IMAGE_NOT_FINISHED_LOADING;

    public static String IMAGE_WRITER_NO_WRITER_FOUND;

    /**
     * Creates a formatted string by putting the given <code>args</code> into
     * the specified <code>pattern</code>.
     * 
     * @param pattern
     *        the string pattern defining the format.
     * @param args
     *        the arguments to format.
     * @return a formatted string.
     */
    public static String format(String pattern, Object... args) {
        return MessageFormat.format(pattern, args);
    }

    static {
        initializeMessages();
    }

    private static void initializeMessages() {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

        for (Field field : Messages.class.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())) {
                String resource = field.getName();
                String string = null;

                try {
                    string = bundle.getString(resource);
                } catch (MissingResourceException e) {
                    string = resource + " resource string is missing.";
                }

                try {
                    field.set(null, string);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
