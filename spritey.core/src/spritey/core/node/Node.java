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
package spritey.core.node;

import spritey.core.Model;
import spritey.core.node.event.NodeListener;

/**
 * The primary datatype for the entire sprite sheet model. It represent a single
 * node in a sprite tree. While this interface exposes methods for dealing with
 * children, not all objects can have children. For example, a sprite does not
 * have any children, therefore, it may choose to throw an exception when a
 * client tries to add a child or may simply ignore the call.
 */
public interface Node {

    public static String DEFAULT_NAME = "New Node";

    /**
     * Sets the name of the node.
     * 
     * @param name
     *        the name to be set.
     * @throws IllegalArgumentException
     *         when <code>name</code> is null or empty.
     */
    public void setName(String name);

    /**
     * Returns the name of the node.
     * 
     * @return the node's name.
     */
    public String getName();

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
    public void setParent(Node parent);

    /**
     * Returns the parent of this node.
     * 
     * @return the parent of this node.
     */
    public Node getParent();

    /**
     * Adds the specified child to this node. If the specified node is already a
     * child no action is taken.
     * 
     * @param child
     *        the child node to add.
     * @return <code>true</code> if child was added successfully.
     * @throws IllegalArgumentException
     *         when <code>child</code> is null or the node itself. i.e.
     *         <code>child == null || child == this</code>.
     */
    public boolean addChild(Node child);

    /**
     * Returns a child with the specified name.
     * 
     * @param childName
     *        the name of the child to find.
     * @return if a child with the specified name exists then the instance of
     *         that child is returned, otherwise null.
     * @throws IllegalArgumentException
     *         when <code>childName</code> is null.
     */
    public Node getChild(String childName);

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
    public Node[] addChildren(Node[] children);

    /**
     * Returns node's children.
     * 
     * @return the node's children.
     */
    public Node[] getChildren();

    /**
     * Returns all leaf nodes all the way down starting from this node.
     * 
     * @return a list of leaf nodes.
     */
    public Node[] getLeaves();

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
    public boolean contains(Node child);

    /**
     * Specifies whether node is a parent of node with the specified name.
     * 
     * @param childName
     *        the name to be checked.
     * @return <code>true</code> if a node with the specified name is a child of
     *         this node.
     * @throws IllegalArgumentException
     *         when <code>childName</code> is null.
     */
    public boolean contains(String childName);

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
    public boolean removeChild(Node child);

    /**
     * Removes a child with the specified name. The child will also loose its
     * parent. No action is taken when no node with the specified name is found.
     * 
     * @param childName
     *        the name of a child to remove.
     * @return <code>true</code> if child has successfully been removed.
     * @throws IllegalArgumentException
     *         when <code>childName</code> is null.
     */
    public boolean removeChild(String childName);

    /**
     * Attaches the specified data to this node.
     * 
     * @param model
     *        the data to be attached.
     * @throws IllegalArgumentException
     *         when <code>model</code> is null.
     */
    public void setModel(Model model);

    /**
     * Returns the data attached to this node.
     * 
     * @return the attached data.
     */
    public Model getModel();

    /**
     * Adds node listener. No action is taken when the specified listener has
     * already been added.
     * 
     * @param listener
     *        the listener to add.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void addNodeListener(NodeListener listener);

    /**
     * Removes node listener. No action is taken when the specified listener has
     * never been added to the list.
     * 
     * @param listener
     *        the listener to remove.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void removeNodeListener(NodeListener listener);

}
