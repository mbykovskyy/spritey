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
package spritey.core.node.event;

import spritey.core.Model;
import spritey.core.node.Node;

/**
 * The listener for receiving notifications about changes in the node. The class
 * that is interested in processing node events implements this interface.
 */
public interface NodeListener {

    /**
     * Sent when node's name is changed.
     * 
     * @param oldName
     *        the previous value.
     * @param newName
     *        the value the name has been set to.
     */
    public void nameChanged(String oldName, String newName);

    /**
     * Sent when node's parent is changed.
     * 
     * @param oldParent
     *        the previous parent.
     * @param newParent
     *        the node the node's parent has been set to.
     */
    public void parentChanged(Node oldParent, Node newParent);

    /**
     * Sent when a new child is added.
     * 
     * @param child
     *        the new child.
     */
    public void childAdded(Node child);

    // TODO We probably don't need this since when each child is added the
    // childAdded event will be sent. However, it might be more efficient to
    // notify listeners once when adding bulk. For example, it's more efficient
    // to update a tree in gui once.
    // public void childrenAdded(Node[] children);

    // TODO Same issue here.
    // public void childrenRemoved();

    /**
     * Sent when a child has been removed from the node.
     * 
     * @param child
     *        the child that has been removed.
     */
    public void childRemoved(Node child);

    /**
     * Sent when all children has been removed from the node.
     */
    public void childrenRemoved();

    /**
     * Sent when then data that this node represent is attached.
     * 
     * @param oldValue
     *        the old value.
     * @param newValue
     *        the new value.
     */
    public void modelChanged(Model oldValue, Model newValue);

}
