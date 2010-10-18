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
package spritey.rcp;

import org.eclipse.osgi.util.NLS;

/**
 * Spritey RCP message catalog.
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "spritey.rcp.messages";

    // Titles
    public static String NEW_SPRITE_SHEET;
    public static String NEW_GROUP;
    public static String ADD_SPRITE;
    public static String ADD_SPRITES;
    public static String DELETE_SPRITES;
    public static String CHANGE_PROPERTY;

    // Tasks
    public static String ADDING_SPRITES;
    public static String PACKING_SPRITES;
    public static String DELETING_SPRITES;
    public static String UPDATING_VIEWS;

    // General messages
    public static String INTERNAL_ERROR;
    public static String DIRECTORY_INACCESSIBLE;

    // IO messages
    public static String UNABLE_TO_LOAD_IMAGE;

    // Sheet messages
    public static String SHEET_WIDTH_INVALID;
    public static String SHEET_HEIGHT_INVALID;
    public static String SHEET_BACKGROUND_INVALID;
    public static String SHEET_DESCRIPTION_INVALID;
    public static String DELETING_SHEET_DISALLOWED;

    // Sprite messages
    public static String SPRITE_NAME_INVALID;
    public static String SPRITE_NAME_EXISTS;
    public static String SPRITE_DOES_NOT_FIT;
    public static String SPRITES_IMPORT_FAILED;
    public static String DELETE_SPRITES_PROBLEMS;

    // Group messages
    public static String GROUP_NAME_INVALID;
    public static String GROUP_NAME_EXISTS;

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

}
