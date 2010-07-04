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
package spritey.core.event;

/**
 * Interface for listening for property changes on an object.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 */
public interface ModelListener {

    /**
     * Indicates that a property has changed.
     * 
     * @param event
     *        the property change event object describing which property changed
     *        and how.
     */
    public void propertyChanged(ModelEvent event);

}
