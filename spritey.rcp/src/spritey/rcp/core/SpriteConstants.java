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
package spritey.rcp.core;

/**
 * Custom sprite properties used to store temporary and arbitrary information.
 */
public interface SpriteConstants {

    /**
     * Sprite SWT image property id. Has to be of
     * <code>org.eclipse.swt.graphics.Image</code> type.
     */
    int SWT_IMAGE = 1200;

    /**
     * Original name property id. Has to be <code>java.lang.String</code> type.
     */
    int ORIGINAL_NAME = 1201;

    /**
     * New name property id. Has to be <code>java.lang.String</code> type.
     */
    int NEW_NAME = 1202;

    /**
     * Image data property id. Has to be
     * <code>org.eclipse.swt.graphics.ImageData</code> type.
     */
    int IMAGE_DATA = 1203;

    /**
     * Error property id. Has to be <code>java.lang.String</code> type.
     */
    int DISPLAY_ERROR = 1204;

    /**
     * Checked property id. Has to be <code>boolean</code> type.
     */
    int CHECKED = 1205;

}
