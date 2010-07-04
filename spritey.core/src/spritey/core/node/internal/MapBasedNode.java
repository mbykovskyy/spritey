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
package spritey.core.node.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import spritey.core.Model;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;

/**
 * A map based implementation of a Node.
 */
public class MapBasedNode implements Node {

    private String name;
    private Node parent;
    private Map<String, Node> children;
    private Model model;
    private List<NodeListener> listeners;

    /**
     * Constructor
     * 
     * @param name
     *        the node's name.
     */
    public MapBasedNode(String name) {
        this.name = name;
        parent = null;
        children = new LinkedHashMap<String, Node>();
        model = null;
        listeners = new ArrayList<NodeListener>();
    }

    /**
     * Validates the specified argument.
     * 
     * @param object
     *        the object to validate.
     * @param message
     *        the description to set for an exception if the <code>object</code>
     *        is invalid.
     * @throws IllegalArgumentException
     *         when the specified <code>object</code> is null.
     */
    private void validateArgument(Object object, String message) {
        if (null == object) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Sends name changed message to all registered listeners.
     * 
     * @param oldName
     *        the old name.
     * @param newName
     *        the new name.
     */
    private void fireNameChanged(String oldName, String newName) {
        for (NodeListener listener : listeners) {
            listener.nameChanged(oldName, newName);
        }
    }

    /**
     * Sends parent change message to all registered listeners.
     * 
     * @param oldParent
     *        the old parent.
     * @param newParent
     *        the new parent.
     */
    private void fireParentChanged(Node oldParent, Node newParent) {
        for (NodeListener listener : listeners) {
            listener.parentChanged(oldParent, newParent);
        }
    }

    /**
     * Sends child added message to all registered listeners.
     * 
     * @param child
     *        the child that has been added.
     */
    private void fireChildAdded(Node child) {
        for (NodeListener listener : listeners) {
            listener.childAdded(child);
        }
    }

    /**
     * Sends child removed message to all listeners.
     * 
     * @param child
     *        the child that has been removed.
     */
    private void fireChildRemoved(Node child) {
        for (NodeListener listener : listeners) {
            listener.childRemoved(child);
        }
    }

    /**
     * Sends model changed message to all listeners.
     * 
     * @param oldValue
     *        the old model.
     * @param newValue
     *        the new model.
     */
    private void fireModelChanged(Model oldValue, Model newValue) {
        for (NodeListener listener : listeners) {
            listener.modelChanged(oldValue, newValue);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#setName(java.lang.String)
     */
    @Override
    public void setName(String name) throws IllegalArgumentException {
        validateArgument(name, "Name is null.");

        String oldName = this.name;
        this.name = name;

        fireNameChanged(oldName, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#setParent(spritey.core.node.Node)
     */
    @Override
    public void setParent(Node parent) throws IllegalArgumentException {
        Node oldParent = this.parent;
        this.parent = parent;

        fireParentChanged(oldParent, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getParent()
     */
    @Override
    public Node getParent() {
        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#addChild(spritey.core.node.Node)
     */
    @Override
    public boolean addChild(Node child) throws IllegalArgumentException {
        validateArgument(child, "Child is null.");

        // Adding children the second time and nodes with similar names are not
        // allowed.
        if (!contains(child) && !contains(child.getName())) {
            children.put(child.getName(), child);
            child.setParent(this);

            fireChildAdded(child);
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getChild(java.lang.String)
     */
    @Override
    public Node getChild(String childName) throws IllegalArgumentException {
        return children.get(childName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#addChildren(spritey.core.node.Node[])
     */
    @Override
    public Node[] addChildren(Node[] children) throws IllegalArgumentException {
        validateArgument(children, "children is null.");

        List<Node> skipped = new ArrayList<Node>();

        for (Node child : children) {
            try {
                if (!addChild(child)) {
                    skipped.add(child);
                }
            } catch (IllegalArgumentException e) {
                // There must have been null in the array. Ignore it and keep
                // going.
            }
        }

        return skipped.toArray(new Node[skipped.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getChildren()
     */
    @Override
    public Node[] getChildren() {
        return children.values().toArray(new Node[children.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#contains(spritey.core.node.Node)
     */
    @Override
    public boolean contains(Node child) throws IllegalArgumentException {
        validateArgument(child, "Child is null.");
        return children.containsValue(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#contains(java.lang.String)
     */
    @Override
    public boolean contains(String childName) throws IllegalArgumentException {
        validateArgument(childName, "Child name is null.");
        return children.containsKey(childName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#isRoot()
     */
    @Override
    public boolean isRoot() {
        return null == parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#isBranch()
     */
    @Override
    public boolean isBranch() {
        return children.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#removeAll()
     */
    @Override
    public void removeAll() {
        // TODO Removing all children at once is more efficient but we don't
        // have childrenRemoved message to sent to listeners plus each child has
        // to set its parent to null, resulting in addition of a loop anyway.
        // children.clear() has to be introduced if we find that the current
        // approach is too inefficient.

        // Need to get a copy of children otherwise a
        // ConcurrentModificationException is thrown when reading and removing
        // children at the same time.
        for (Node child : getChildren()) {
            removeChild(child);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#removeChild(spritey.core.node.Node)
     */
    @Override
    public boolean removeChild(Node child) throws IllegalArgumentException {
        validateArgument(child, "Child is null.");
        return removeChild(child.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#removeChild(java.lang.String)
     */
    @Override
    public boolean removeChild(String childName)
            throws IllegalArgumentException {
        validateArgument(childName, "Child name is null.");

        Node child = children.remove(childName);

        if (null == child) {
            return false;
        }

        fireChildRemoved(child);
        child.setParent(null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#setModel(spritey.core.Model)
     */
    @Override
    public void setModel(Model model) throws IllegalArgumentException {
        validateArgument(model, "Model is null.");

        Model oldValue = this.model;
        this.model = model;

        fireModelChanged(oldValue, model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getModel()
     */
    @Override
    public Model getModel() {
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.Node#addNodeListener(spritey.core.node.event.NodeListener
     * )
     */
    @Override
    public void addNodeListener(NodeListener listener)
            throws IllegalArgumentException {
        validateArgument(listener, "Listener is null.");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#removeNodeListener(spritey.core.node.event.
     * NodeListener)
     */
    @Override
    public void removeNodeListener(NodeListener listener)
            throws IllegalArgumentException {
        validateArgument(listener, "Listener is null.");
        listeners.remove(listener);
    }

}
