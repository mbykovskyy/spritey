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

import java.awt.Rectangle;

/**
 * Sprite is an image that can be drawn on sprite sheet's canvas.
 */
public class Sprite extends Model {

    /**
     * Sprite name property id. Has to be <code>java.lang.String</code> type.
     */
    public static final int NAME = 300;

    /**
     * Sprite bounds property id. Has to be of <code>java.awt.Rectangle</code>
     * type. Sprite is not positioned when <code>x</code> and <code>y</code> are
     * negative.
     */
    public static final int BOUNDS = 301;

    /**
     * Sprite image property id. Has to be of <code>java.awt.Image</code> type.
     */
    public static final int IMAGE = 302;

    // Defaults
    public static final String DEFAULT_NAME = "New Sprite";
    public static final Rectangle DEFAULT_BOUNDS = new Rectangle(-1, -1, 0, 0);

    // Limits
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 1024;
    public static final String LEGAL_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

}
