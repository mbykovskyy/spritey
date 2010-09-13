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
package spritey.core;

import java.awt.Color;
import java.awt.Dimension;

/**
 * A data object representing a canvas that can be attached to a node.
 */
public interface Sheet extends Model {

    // Properties (each id has to be unique across the whole application)
    public static int BACKGROUND = 200;
    public static int OPAQUE = 201;
    public static int SIZE = 202;
    public static int DESCRIPTION = 203;
    public static int NODE = 204;

    // Defaults
    public static Color DEFAULT_BACKGROUND = new Color(255, 0, 255);
    public static boolean DEFAULT_OPAQUE = true;
    public static Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static String DEFAULT_DESCRIPTION = "Created with Spritey.";

    // Limits
    public static int MIN_DESCRIPTION_LENGTH = 0;
    public static int MAX_DESCRIPTION_LENGTH = 1024;
    public static int MAX_WIDTH = 1024;
    public static int MAX_HEIGHT = 1024;
    public static int MIN_WIDTH = 0;
    public static int MIN_HEIGHT = 0;

}
