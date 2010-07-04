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
public interface Group extends Model {

    // Properties (each id has to be unique across the whole application)
    public static int NAME = 100;
    public static int NODE = 101;

    // Defaults
    public static String DEFAULT_NAME = "New Group";

    // Limits
    public static int NAME_TEXT_LIMIT = 1024;

}
