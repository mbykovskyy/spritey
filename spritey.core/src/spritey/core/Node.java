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

import spritey.core.event.NodeChangeListener;

/**
 * The primary datatype for the entire sprite sheet model. It represent a single
 * node in a sprite tree. While this interface exposes methods for dealing with
 * children, not all objects can have children. For example, a sprite does not
 * have any children, therefore, it may choose to throw an exception when a
 * client tries to add a child or may simply ignore the call.
 */
public interface Node<T> {

    /**
     * Sets the node's parent. <code>parent</code> can be null but cannot be the
     * node itself. When parent is null the node becomes a root.
     * 
     * @param parent
     *        the node to be set as a parent of this node.
     * @throws IllegalArgumentException
     *         when <code>parent</code> is the node itself. i.e.
     *         <code>parent == this</code>.
     */
    public void setParent(T parent);

    /**
     * Returns the parent of this node.
     * 
     * @return the parent of this node.
     */
    public T getParent();

    /**
     * Adds an array of children to this node. Nodes that are already children
     * of this node are skipped.
     * 
     * @param children
     *        an array of children to add.
     * @return an array of nodes that have been skipped.
     * @throws IllegalArgumentException
     *         when <code>children</code> is null.
     */
    public T[] addChildren(T... children);

    /**
     * Returns node's children.
     * 
     * @return the node's children.
     */
    public T[] getChildren();

    /**
     * Specifies whether node is a parent of the specified child. The comparison
     * is done against objects. This is similar to Object.equals(Object).
     * 
     * @param child
     *        the child to check.
     * @return <code>true</code> if this node is a parent of the specified
     *         child.
     * @throws IllegalArgumentException
     *         when <code>child</code> is null.
     */
    public boolean contains(T child);

    /**
     * Specifies whether this node is a root. i.e. has not parent.
     * 
     * @return <code>true</code> if this node is a root, otherwise
     *         <code>false</code>.
     */
    public boolean isRoot();

    /**
     * Specifies whether this node is a branch. i.e. has at least one child.
     * 
     * @return <code>true</code> if this node is a branch, otherwise
     *         <code>false</code>.
     */
    public boolean isBranch();

    /**
     * Specifies whether this node is a leaf. i.e. has no children.
     * 
     * @return <code>true</code> if this node is a leaf, otherwise
     *         <code>false</code>.
     */
    public boolean isLeaf();

    /**
     * Removes all children.
     */
    public void removeAll();

    /**
     * Removes the specified child from this node. The child will also loose
     * this node as a parent. If the specified node is not a child of this node,
     * no action is taken.
     * 
     * @param child
     *        the child to be removed.
     * @return <code>true</code> if child has successfully been removed.
     * @throws IllegalArgumentException
     *         when <code>child</code> is null.
     */
    public boolean removeChild(T child);

    /**
     * Adds node listener. No action is taken when the specified listener has
     * already been added.
     * 
     * @param listener
     *        the listener to add.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void addNodeChangeListener(NodeChangeListener<T> listener);

    /**
     * Removes node listener. No action is taken when the specified listener has
     * never been added to the list.
     * 
     * @param listener
     *        the listener to remove.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void removeNodeChangeListener(NodeChangeListener<T> listener);

}
