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
package spritey.rcp.core;

/**
 * Various sheet properties related constants.
 */
public interface SpriteConstants {

    // Defaults
    public static String DEFAULT_NAME = "Sprite";
    public static int DEFAULT_X = -1;
    public static int DEFAULT_Y = -1;

    // Limits
    public static int MIN_NAME_LENGTH = 1;
    public static int MAX_NAME_LENGTH = 256;
    public static int MAX_WIDTH = 1024;
    public static int MAX_HEIGHT = 1024;
    public static int MIN_WIDTH = 0;
    public static int MIN_HEIGHT = 0;

}
