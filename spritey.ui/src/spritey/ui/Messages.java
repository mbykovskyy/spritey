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

    public static String SPRITE_SHEET_WIZARD_TITLE;

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

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

}
