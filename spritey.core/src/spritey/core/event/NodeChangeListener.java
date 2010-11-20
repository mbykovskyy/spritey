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
package spritey.core.event;


/**
 * The listener for receiving notifications about changes in the node. The class
 * that is interested in processing node events implements this interface.
 */
public interface NodeChangeListener<T> {

    /**
     * Sent when node's parent is changed.
     * 
     * @param oldParent
     *        the previous parent.
     * @param newParent
     *        the node the node's parent has been set to.
     */
    public void parentChanged(T oldParent, T newParent);

    /**
     * Sent when a new child is added.
     * 
     * @param child
     *        the new child.
     */
    public void childAdded(T child);

    /**
     * Sent when a child has been removed from the node.
     * 
     * @param child
     *        the child that has been removed.
     */
    public void childRemoved(T child);

}
