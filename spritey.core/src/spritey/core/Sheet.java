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

import java.awt.Color;
import java.awt.Dimension;

/**
 * A data object representing a canvas on to which sprites are be drawn.
 */
public class Sheet extends AbstractModel {

    /**
     * Background color property id of the Has to be of
     * <code>java.awt.Color</code> type.
     */
    public final static int BACKGROUND = 200;

    /**
     * Sheet opaque property id, specifies whether sheet is opaque. Has to be of
     * <code>boolean</code> type.
     */
    public final static int OPAQUE = 201;

    /**
     * Sheet size property id. Has to be of <code>java.awt.Dimension</code>
     * type.
     */
    public final static int SIZE = 202;

    /**
     * Description property id. Has to be of <code>java.lang.String</code> type.
     */
    public final static int DESCRIPTION = 203;

    /**
     * Node to which this model is attached. Has to be of
     * <code>spritey.core.node.Node</code> type.
     */
    public final static int NODE = 204;

    // Defaults
    public final static String DEFAULT_NAME = "Sheet";
    public final static Color DEFAULT_BACKGROUND = new Color(255, 0, 255);
    public final static boolean DEFAULT_OPAQUE = true;
    public final static Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public final static String DEFAULT_DESCRIPTION = "Created with Spritey.";

    // Limits
    public final static int MIN_DESCRIPTION_LENGTH = 0;
    public final static int MAX_DESCRIPTION_LENGTH = 1024;
    public final static int MAX_WIDTH = 1024;
    public final static int MAX_HEIGHT = 1024;
    public final static int MIN_WIDTH = 0;
    public final static int MIN_HEIGHT = 0;

}
