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
package spritey.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Spritey UI message catalog.
 */
public class Messages {

    private static final String BUNDLE_NAME = "spritey.ui.messages";

    public static String UNEXPECTED_ERROR_OCCURRED;

    public static String SPRITE_SHEET_WIZARD_TITLE;
    public static String SPRITE_SHEET_WIZARD_BUILD;
    public static String SPRITE_SHEET_WIZARD_CLOSE;
    public static String SPRITE_SHEET_WIZARD_BUILT_SUCCESSFULLY;

    public static String NEW_SHEET_PAGE_TITLE;
    public static String NEW_SHEET_PAGE_DESCRIPTION;
    public static String NEW_SHEET_PAGE_SIZE_CONSTRAINTS;
    public static String NEW_SHEET_PAGE_MAX_WIDTH;
    public static String NEW_SHEET_PAGE_MAX_HEIGHT;
    public static String NEW_SHEET_PAGE_POWER_OF_TWO;
    public static String NEW_SHEET_PAGE_ASPECT_RATIO;
    public static String NEW_SHEET_PAGE_BACKGROUND;
    public static String NEW_SHEET_PAGE_COMMENT;
    public static String NEW_SHEET_PAGE_TRANSPARENT;
    public static String NEW_SHEET_PAGE_CHOOSE_COLOR;

    public static String ADD_SPRITES_PAGE_TITLE;
    public static String ADD_SPRITES_PAGE_DESCRIPTION;
    public static String ADD_SPRITES_ADD_SPRITES;
    public static String ADD_SPRITES_ADD_FOLDER;
    public static String ADD_SPRITES_CREATE_GROUP;
    public static String ADD_SPRITES_DELETE;
    public static String ADD_SPRITES_EXPAND_ALL;
    public static String ADD_SPRITES_COLLAPSE_ALL;
    public static String ADD_SPRITES_SELECT_ALL;
    public static String ADD_SPRITES_RENAME;
    public static String ADD_SPRITES_NAME_COLUMN;
    public static String ADD_SPRITES_SIZE_COLUMN;
    public static String ADD_SPRITES_DELETE_GROUP_TITLE;
    public static String ADD_SPRITES_DELETE_GROUPS_TITLE;
    public static String ADD_SPRITES_DELETE_SPRITE_TITLE;
    public static String ADD_SPRITES_DELETE_SPRITES_TITLE;
    public static String ADD_SPRITES_DELETE_SELECTED_GROUP_QUESTION;
    public static String ADD_SPRITES_DELETE_SELECTED_GROUPS_QUESTION;
    public static String ADD_SPRITES_DELETE_SELECTED_SPRITE_QUESTION;
    public static String ADD_SPRITES_DELETE_SELECTED_SPRITES_QUESTION;

    public static String NEW_GROUP_WIZARD_TITLE;

    public static String NEW_GROUP_PAGE_TITLE;
    public static String NEW_GROUP_PAGE_DESCRIPTION;
    public static String NEW_GROUP_NAME;

    public static String RENAME_SPRITE_WIZARD_TITLE;
    public static String RENAME_GROUP_WIZARD_TITLE;

    public static String RENAME_SPRITE_PAGE_TITLE;
    public static String RENAME_GROUP_PAGE_TITLE;
    public static String RENAME_PAGE_DESCRIPTION;
    public static String RENAME_PAGE_RENAME_GROUP;
    public static String RENAME_PAGE_RENAME_SPRITE;
    public static String RENAME_PAGE_NEW_NAME;

    public static String SAVE_AS_PAGE_TITLE;
    public static String SAVE_AS_PAGE_NAME;
    public static String SAVE_AS_PAGE_LOCATION;
    public static String SAVE_AS_BROWSE;
    public static String SAVE_AS_IMAGE;
    public static String SAVE_AS_METADATA;
    public static String SAVE_AS_OVERWRITE_EXISTING_FILES;
    public static String SAVE_AS_ENTER_NAME;
    public static String SAVE_AS_ENTER_LOCATION;
    public static String SAVE_AS_FINISH;
    public static String SAVE_AS_NAME_INVALID;
    public static String SAVE_AS_LOCATION_INVALID;
    public static String SAVE_AS_PACKING;
    public static String SAVE_AS_SAVING;
    public static String SAVE_AS_WRITING_FAILED;
    public static String SAVE_AS_OPEN_FILE_FAILED;
    public static String SAVE_AS_OVERWRITE_FILE;
    public static String SAVE_AS_PROBLEMS_SAVING;
    public static String SAVE_AS_ACCESS_DENIED;

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

}
