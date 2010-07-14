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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.RGB;

/**
 * Various sheet properties related constants.
 */
public interface SheetConstants {

    // Defaults
    public static String DEFAULT_NAME = "Sheet";
    public static RGB DEFAULT_BACKGROUND = new RGB(255, 0, 255);
    public static boolean DEFAULT_OPAQUE = true;
    public static Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static String DEFAULT_DESCRIPTION = "Created with Spritey.";

    // Limits
    public static int DESCRIPTION_TEXT_LIMIT = 1024;
    public static int MAX_WIDTH = 1024;
    public static int MAX_HEIGHT = 1024;
    public static int MIN_WIDTH = 0;
    public static int MIN_HEIGHT = 0;

}
