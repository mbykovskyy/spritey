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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.node.event.AbstractNodeListener;
import spritey.core.node.event.NodeListener;

/**
 * A map based implementation of a Node.
 */
public class MapBasedNode implements Node, ModelListener {

    /**
     * Updates a key (used to reference a child) in a <code>children</code> map.
     */
    private class ChildMapUpdater extends AbstractNodeListener {
        @Override
        public void nameChanged(String oldName, String newName) {
            children.put(newName, children.remove(oldName));
        }
    }

    private String name;
    private Node parent;
    private Map<String, Node> children;
    private Model model;
    private List<NodeListener> listeners;
    private NodeListener childMapUpdater;

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
        childMapUpdater = new ChildMapUpdater();
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
    public void setName(String name) {
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
    // TODO Should this be private or protected? Users should only change node's
    // parent via addChild().
    @Override
    public void setParent(Node parent) {
        if (this == parent) {
            throw new IllegalArgumentException(
                    "Cannot assign itself as a parent");
        }

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
    public boolean addChild(Node child) {
        validateArgument(child, "Child is null.");

        // Adding children the second time and nodes with similar names are not
        // allowed.
        if (!contains(child) && !contains(child.getName())) {
            children.put(child.getName(), child);
            child.setParent(this);

            // When child's name changes parent has to update its child map.
            child.addNodeListener(childMapUpdater);

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
    public Node getChild(String childName) {
        validateArgument(childName, "Child name is null.");
        return children.get(childName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#addChildren(spritey.core.node.Node[])
     */
    @Override
    public Node[] addChildren(Node[] children) {
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
    public boolean contains(Node child) {
        validateArgument(child, "Child is null.");
        return children.containsValue(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#contains(java.lang.String)
     */
    @Override
    public boolean contains(String childName) {
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
    public boolean removeChild(Node child) {
        validateArgument(child, "Child is null.");
        return removeChild(child.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#removeChild(java.lang.String)
     */
    @Override
    public boolean removeChild(String childName) {
        validateArgument(childName, "Child name is null.");

        Node child = children.remove(childName);

        if (null == child) {
            return false;
        }

        child.removeNodeListener(childMapUpdater);
        child.setParent(null);

        fireChildRemoved(child);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#setModel(spritey.core.Model)
     */
    @Override
    public void setModel(Model model) {
        validateArgument(model, "Model is null.");

        Model oldValue = this.model;
        this.model = model;
        this.model.addModelListener(this);

        // TODO Should the user be responsible for changing model node property?
        try {
            if (model instanceof Sheet) {
                this.model.setProperty(Sheet.NODE, this);
            } else if (model instanceof Sprite) {
                this.model.setProperty(Sprite.NODE, this);
            } else if (model instanceof Group) {
                this.model.setProperty(Group.NODE, this);
            } else {
                throw new IllegalArgumentException("Unsupported model type "
                        + model);
            }

            if (null != oldValue) {
                if (oldValue instanceof Sheet) {
                    oldValue.setProperty(Sheet.NODE, null);
                } else if (oldValue instanceof Sprite) {
                    oldValue.setProperty(Sprite.NODE, null);
                } else if (oldValue instanceof Group) {
                    oldValue.setProperty(Group.NODE, null);
                } else {
                    throw new IllegalArgumentException(
                            "Unsupported model type " + model);
                }
            }
        } catch (InvalidPropertyValueException e) {
            // Setting NODE property inside this method is an implementation
            // detail that we want to hide. Declaring this method as throws
            // InvalidPropertyValueException will reveal such detail and will
            // force other implementations to throw this exception. A good
            // practice is to wrap checked exceptions into unchecked exceptions.
            // If InvalidPropertyValueException is thrown then it means this
            // node implementation has missed some detail.
            throw new IllegalArgumentException(e.getMessage(), e);
        }

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
    public void addNodeListener(NodeListener listener) {
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
    public void removeNodeListener(NodeListener listener) {
        validateArgument(listener, "Listener is null.");
        listeners.remove(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.Node#getLeaves()
     */
    @Override
    public Node[] getLeaves() {
        List<Node> leaves = new ArrayList<Node>();

        for (Node child : getChildren()) {
            if (child.isBranch()) {
                List<Node> childLeaves = Arrays.asList(child.getLeaves());
                leaves.addAll(childLeaves);
            } else {
                leaves.add(child);
            }
        }

        return leaves.toArray(new Node[leaves.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.event.ModelListener#propertyChanged(spritey.core.event.
     * ModelEvent)
     */
    @Override
    public void propertyChanged(ModelEvent event) {
        Object source = event.getSource();
        int property = event.getProperty();

        // A node needs to updated its name when model name property changes,
        // since node's name is used to keep model's names unique within a
        // group.
        if ((source instanceof Sprite) && (Sprite.NAME == property)) {
            setName((String) ((Sprite) source).getProperty(property));
        } else if ((source instanceof Group) && (Group.NAME == property)) {
            setName((String) ((Group) source).getProperty(property));
        }
    }

}
