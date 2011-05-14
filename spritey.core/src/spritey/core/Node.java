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

import java.util.ArrayList;
import java.util.List;

/**
 * A fundamental element out of which a tree is made. Each node has a unique
 * name across its siblings.
 */
public class Node {

    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 1024;

    private String name;
    private Node parent;
    private final List<Node> children;

    /**
     * Creates a new instance of Node.
     * 
     * @param name
     *        the name to give to the node.
     * @throws IllegalArgumentException
     *         when specified <code>name</code> is <code>null</code>.
     */
    public Node(final String name) {
        setName(name);
        children = new ArrayList<Node>();
    }

    /**
     * Sets node's name.
     * 
     * @param name
     *        the new node name.
     * @throws IllegalArgumentException
     *         when either <code>name</code> is <code>null</code> or the length
     *         of the specified <code>name</code> is less than MIN_NAME_LENGTH
     *         or greater than MAX_NAME_LENGTH.
     */
    public void setName(final String name) {
        if ((null == getName()) || !getName().equals(name)) {
            if (null == name) {
                throw new IllegalArgumentException(Messages.NULL);
            } else if ((name.length() < MIN_NAME_LENGTH)
                    || (name.length() > MAX_NAME_LENGTH)) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.NAME_LENGTH_INVALID, MIN_NAME_LENGTH,
                        MAX_NAME_LENGTH));
            } else if ((null != getParent()) && getParent().contains(name)) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.NAME_NOT_UNIQUE, name, getParent().getName()));
            }
            this.name = name;
        }
    }

    /**
     * Returns the name given to this node.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the node's parent. <code>parent</code> can be null but cannot be the
     * node itself. When parent is null the node becomes a root.
     * 
     * @param parent
     *        the node to be set as a parent of this node.
     * @throws IllegalArgumentException
     *         when <code>parent</code> is the node itself. i.e.
     *         <code>parent == this</code> or <code>parent</code> is a child of
     *         this node.
     */
    protected void setParent(final Node parent) {
        if (parent != getParent()) {
            if (this == parent) {
                throw new IllegalArgumentException(
                        Messages.CANNOT_ASSIGN_ITSELF_AS_PARENT);
            } else if (contains(parent)) {
                throw new IllegalArgumentException(
                        Messages.CANNOT_ASSIGN_CHILD_AS_PARENT);
            }
            this.parent = parent;
        }
    }

    /**
     * Returns the parent of this node.
     * 
     * @return the parent of this node.
     */
    public Node getParent() {
        return parent;
    }

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
    public Node[] addChildren(final Node... children) {
        if (null == children) {
            throw new IllegalArgumentException(Messages.NULL);
        }

        List<Node> skipped = new ArrayList<Node>();

        for (Node child : children) {
            if (null != child) {
                if (!((this == child) || (child == getParent()) || contains(child
                        .getName()))) {
                    getChildrenList().add(child);
                    child.setParent(this);
                } else {
                    skipped.add(child);
                }
            }
        }
        return skipped.toArray(new Node[skipped.size()]);
    }

    /**
     * Returns node's children.
     * 
     * @return the node's children.
     */
    public Node[] getChildren() {
        return getChildrenList().toArray(new Node[getChildrenList().size()]);
    }

    /**
     * Specifies whether node is a parent of the specified child. The comparison
     * is done against objects. This is similar to Object.equals(Object).
     * 
     * @param child
     *        the child to check.
     * @return <code>true</code> if this node is a parent of the specified
     *         child.
     */
    protected boolean contains(final Node child) {
        return getChildrenList().contains(child);
    }

    /**
     * Specifies whether this node contains a child with the given name.
     * 
     * @param name
     *        the name to test against.
     * @return <code>true</code> if one of the children has a given name,
     *         otherwise <code>false</code>.
     */
    public boolean contains(final String name) {
        for (Node child : getChildrenList()) {
            if (child.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Specifies whether this node is a root. i.e. has not parent.
     * 
     * @return <code>true</code> if this node is a root, otherwise
     *         <code>false</code>.
     */
    public boolean isRoot() {
        return null == getParent();
    }

    /**
     * Specifies whether this node is a branch. i.e. has at least one child.
     * 
     * @return <code>true</code> if this node is a branch, otherwise
     *         <code>false</code>.
     */
    public boolean isBranch() {
        return !isLeaf();
    }

    /**
     * Specifies whether this node is a leaf. i.e. has no children.
     * 
     * @return <code>true</code> if this node is a leaf, otherwise
     *         <code>false</code>.
     */
    public boolean isLeaf() {
        return getChildrenList().isEmpty();
    }

    /**
     * Removes all children.
     */
    public void removeAll() {
        // Need to get a copy of children otherwise a
        // ConcurrentModificationException is thrown when reading and removing
        // children at the same time.
        for (Node child : getChildren()) {
            removeChild(child);
        }
    }

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
    public boolean removeChild(final Node child) {
        if (null == child) {
            throw new IllegalArgumentException(Messages.NULL);
        }

        if (!getChildrenList().remove(child)) {
            return false;
        }

        child.setParent(null);
        return true;
    }

    /**
     * Returns a mutable list of node's children.
     * 
     * @return a mutable list.
     */
    protected List<Node> getChildrenList() {
        return children;
    }

}
