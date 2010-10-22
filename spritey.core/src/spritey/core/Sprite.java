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
 * A data object representing an image that can be attached to a node.
 */
public class Sprite extends AbstractModel {

    /**
     * Sprite name property id. Has to be <code>java.lang.String</code> type.
     */
    public final static int NAME = 300;

    /**
     * Node property id. Has to be <code>spritey.core.node.Node</code> type.
     */
    public final static int NODE = 301;

    /**
     * Sprite bounds property id. Has to be of <code>java.awt.Rectangle</code>
     * type. Sprite is not positioned when <code>x</code> and <code>y</code> are
     * negative.
     */
    public final static int BOUNDS = 302;

    /**
     * Sprite image property id. Has to be of <code>TODO</code> type.
     */
    public final static int IMAGE = 303;

    // Defaults
    public final static String DEFAULT_NAME = "New Sprite";
    public final static Rectangle DEFAULT_BOUNDS = new Rectangle(-1, -1, 0, 0);

    // Limits
    public final static int MIN_NAME_LENGTH = 1;
    public final static int MAX_NAME_LENGTH = 1024;

}
