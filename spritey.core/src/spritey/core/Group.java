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
package spritey.core;

/**
 * This model represents a container that does not have any graphical
 * representation but can contain a collection of sprites and groups.
 */
public class Group extends Node {

    public static final String DEFAULT_NAME = Messages.GROUP_DEFAULT_NAME;

    /**
     * Creates a new instance of a Group with DEFAULT_NAME.
     */
    public Group() {
        this(DEFAULT_NAME);
    }

    /**
     * Creates a new instance of a Group with a give name.
     * 
     * @param name
     *        the name to give to a new group.
     */
    public Group(final String name) {
        super(name);
    }

}
