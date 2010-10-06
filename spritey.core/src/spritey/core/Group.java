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

/**
 * A data object that can be attached to a node. This model represents a
 * container that does not have any graphical representation but can contain a
 * collection of sprites or other groups.
 */
public class Group extends AbstractModel {

    /**
     * Group name property id. Has to be <code>java.lang.String</code> type.
     */
    public final static int NAME = 100;

    /**
     * Node property id. Has to be <code>spritey.core.node.Node</code> type.
     */
    public final static int NODE = 101;

    // Defaults
    public final static String DEFAULT_NAME = "New Group";

    // Limits
    public final static int MIN_NAME_LENGTH = 0;
    public final static int MAX_NAME_LENGTH = 1024;

}
