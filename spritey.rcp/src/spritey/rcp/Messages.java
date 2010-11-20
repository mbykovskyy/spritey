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
    public static String SAVE_AS;

    // Tasks
    public static String ADDING_SPRITES;
    public static String PACKING_SPRITES;
    public static String DELETING_SPRITES;
    public static String UPDATING_VIEWS;

    // General messages
    public static String OPERATION_INVOCATION_ERROR;
    public static String INTERNAL_ERROR;
    public static String DIRECTORY_INACCESSIBLE;
    public static String OVERWRITE_FILE;

    // IO messages
    public static String UNABLE_TO_LOAD_IMAGE;
    public static String NO_IMAGES_FOUND;
    public static String ACCESS_DENIED;
    public static String DOES_NOT_EXIST;

    // New group messages
    public static String NEW_GROUP_CREATE_GROUP;
    public static String NEW_GROUP_NAME;

    // Add sprites messages
    public static String ADD_SPRITES_TITLE;
    public static String ADD_SPRITES_ADD_SPRITES_TO_SHEET;
    public static String ADD_SPRITES_FROM_DIRECTORY;
    public static String ADD_SPRITES_SELECT_FILES;
    public static String ADD_SPRITES_BROWSE;
    public static String ADD_SPRITES_SELECT_ALL;
    public static String ADD_SPRITES_DESELECT_ALL;
    public static String ADD_SPRITES_SELECT_DIRECTORY_ERROR;
    public static String ADD_SPRITES_SELECT_FILES_ERROR;
    public static String ADD_SPRITES_DIRECTORY_INVALID_ERROR;
    public static String ADD_SPRITES_NO_SPRITES_SELECTED_ERROR;
    public static String ADD_SPRITES_FIX_CONFLICT;
    public static String ADD_SPRITES_DO_NOT_FIT;

    // Batch rename messages
    public static String BATCH_RENAME_TITLE;
    public static String BATCH_RENAME_DESCRIPTION;

    public static String BATCH_RENAME_ORIGINAL;
    public static String BATCH_RENAME_NEW;

    public static String BATCH_RENAME_EXPAND_ALL;
    public static String BATCH_RENAME_COLLAPSE_ALL;

    public static String BATCH_RENAME_SHEET_CONTAINS_SPRITE;
    public static String BATCH_RENAME_SHEET_CONTAINS_GROUP;

    // Sheet messages
    public static String SHEET_WIDTH_INVALID;
    public static String SHEET_HEIGHT_INVALID;
    public static String SHEET_BACKGROUND_INVALID;
    public static String SHEET_DESCRIPTION_INVALID;
    public static String DELETING_SHEET_DISALLOWED;

    // Sprite messages
    public static String SPRITE_NAME_INVALID_LENGTH;
    public static String SPRITE_NAME_EXISTS;
    public static String SPRITE_NAME_ILLEGAL_CHARACTER;
    public static String SPRITE_NAME_FIRST_CHARACTER_NOT_ALPHA;
    public static String SPRITE_DOES_NOT_FIT;
    public static String SPRITE_DELETE_PROBLEMS;

    // Group messages
    public static String GROUP_NAME_INVALID_LENGTH;
    public static String GROUP_NAME_EXISTS;
    public static String GROUP_NAME_ILLEGAL_CHARACTER;
    public static String GROUP_NAME_FIRST_CHARACTER_NOT_ALPHA;

    // Save as messages
    public static String WRITING_FAILED;
    public static String OPEN_FILE_FAILED;
    public static String PROBLEMS_SAVING;

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

}
