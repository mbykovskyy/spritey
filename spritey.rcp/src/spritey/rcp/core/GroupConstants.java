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
 * Custom group properties used to store temporary and arbitrary information.
 */
public interface GroupConstants {

    /**
     * Original name property id. Has to be <code>java.lang.String</code> type.
     */
    int ORIGINAL_NAME = 1100;

    /**
     * New name property id. Has to be <code>java.lang.String</code> type.
     */
    int NEW_NAME = 1101;

    /**
     * Error property id. Has to be <code>java.lang.String</code> type.
     */
    int DISPLAY_ERROR = 1102;

    /**
     * Checked property id. Has to be <code>boolean</code> type.
     */
    int CHECKED = 1103;

}
