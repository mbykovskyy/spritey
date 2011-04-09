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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spritey.core.event.NodeChangeListener;
import spritey.core.event.PropertyChangeEvent;
import spritey.core.event.PropertyChangeListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.validator.Validator;

/**
 * The default implementation of Model.
 */
public abstract class Model implements Properties, Node<Model> {

    private final Map<Integer, Object> properties;
    private final List<PropertyChangeListener> propertyListeners;
    private final Map<Integer, List<Validator>> validators;

    private Model parent;
    private final List<Model> children;
    private List<NodeChangeListener<Model>> nodeListeners;

    /**
     * Creates a new instance of Model.
     */
    protected Model() {
        properties = new HashMap<Integer, Object>();
        propertyListeners = new ArrayList<PropertyChangeListener>();
        validators = new HashMap<Integer, List<Validator>>();
        children = new ArrayList<Model>();
        nodeListeners = new ArrayList<NodeChangeListener<Model>>();
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
     * Sends the specified event to the registered property listeners.
     * 
     * @param event
     *        the event to send.
     */
    protected void firePropertyChanged(PropertyChangeEvent event) {
        for (PropertyChangeListener listener : propertyListeners) {
            listener.propertyChanged(event);
        }
    }

    @Override
    public void setProperty(int property, Object value)
            throws InvalidPropertyValueException {
        validate(property, value);

        // TODO No need to verify that property exists? This will allow new
        // properties to be set. Investigate.
        Object oldValue = properties.get(property);
        properties.put(property, value);

        firePropertyChanged(new PropertyChangeEvent(this, property, oldValue,
                value));
    }

    /**
     * Verifies that the specified value meets the validation requirements set
     * for the property.
     * 
     * @param property
     *        the property to verify the value for.
     * @param value
     *        the value to validate.
     * @throws InvalidPropertyValueException
     *         when the specified <code>value</code> does not meet validation
     *         requirements.
     */
    protected void validate(int property, Object value)
            throws InvalidPropertyValueException {
        List<Validator> list = validators.get(property);

        if (null != list) {
            for (Validator validator : list) {
                if (!validator.isValid(value)) {
                    throw new InvalidPropertyValueException(property, value,
                            validator.getErrorCode(), validator.getMessage());
                }
            }
        }
    }

    @Override
    public Object getProperty(int property) {
        // TODO No need to verify that property exists? If property is not
        // in the map this will return null.
        return properties.get(property);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        validateArgument(listener, "listener is null.");

        if (!propertyListeners.contains(listener)) {
            propertyListeners.add(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        validateArgument(listener, "listener is null.");
        propertyListeners.remove(listener);
    }

    @Override
    public void addValidator(int property, Validator validator) {
        validateArgument(validator, "validator is null.");
        List<Validator> list = validators.get(property);

        if (null == list) {
            // No validators for this property have been added yet.
            list = new ArrayList<Validator>();
            validators.put(property, list);
        }

        if (!list.contains(validator)) {
            list.add(validator);
        }
    }

    @Override
    public void removeValidator(int property, Validator validator) {
        validateArgument(validator, "validator is null.");
        List<Validator> list = validators.get(property);

        if (null != list) {
            list.remove(validator);
        }
    }

    @Override
    public Validator[] getValidators(int property) {
        List<Validator> list = validators.get(property);

        if (null != list) {
            return list.toArray(new Validator[list.size()]);
        }
        return new Validator[0];
    }

    /**
     * Sends parent change message to all registered listeners.
     * 
     * @param oldParent
     *        the old parent.
     * @param newParent
     *        the new parent.
     */
    protected void fireParentChanged(Model oldParent, Model newParent) {
        for (NodeChangeListener<Model> listener : nodeListeners) {
            listener.parentChanged(oldParent, newParent);
        }
    }

    @Override
    public void setParent(Model parent) {
        if (this == parent) {
            throw new IllegalArgumentException(
                    "Cannot assign itself as a parent");
        }

        Model oldParent = this.parent;
        this.parent = parent;

        fireParentChanged(oldParent, parent);
    }

    @Override
    public Model getParent() {
        return parent;
    }

    /**
     * Sends child added message to all registered listeners.
     * 
     * @param child
     *        the child that has been added.
     */
    protected void fireChildAdded(Model child) {
        for (NodeChangeListener<Model> listener : nodeListeners) {
            listener.childAdded(child);
        }
    }

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
    protected boolean addChild(Model child) {
        validateArgument(child, "child is null.");

        if (!contains(child)) {
            children.add(child);
            child.setParent(this);
            fireChildAdded(child);
            return true;
        }
        return false;
    }

    @Override
    public Model[] addChildren(Model... children) {
        validateArgument(children, "children is null.");

        List<Model> skipped = new ArrayList<Model>();

        for (Model child : children) {
            try {
                if (!addChild(child)) {
                    skipped.add(child);
                }
            } catch (IllegalArgumentException e) {
                // There must have been null in the array. Ignore it and keep
                // going.
            }
        }
        return skipped.toArray(new Model[skipped.size()]);
    }

    @Override
    public Model[] getChildren() {
        return children.toArray(new Model[children.size()]);
    }

    @Override
    public boolean contains(Model child) {
        validateArgument(child, "child is null.");
        return children.contains(child);
    }

    @Override
    public boolean isRoot() {
        return null == parent;
    }

    @Override
    public boolean isBranch() {
        return !isLeaf();
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void removeAll() {
        // Need to get a copy of children otherwise a
        // ConcurrentModificationException is thrown when reading and removing
        // children at the same time.
        for (Model child : getChildren()) {
            removeChild(child);
        }
    }

    /**
     * Sends child removed message to all listeners.
     * 
     * @param child
     *        the child that has been removed.
     */
    protected void fireChildRemoved(Model child) {
        for (NodeChangeListener<Model> listener : nodeListeners) {
            listener.childRemoved(child);
        }
    }

    @Override
    public boolean removeChild(Model child) {
        validateArgument(child, "child name is null.");

        if (!children.remove(child)) {
            return false;
        }

        child.setParent(null);
        fireChildRemoved(child);
        return true;
    }

    @Override
    public void addNodeChangeListener(NodeChangeListener<Model> listener) {
        validateArgument(listener, "listener is null.");

        if (!nodeListeners.contains(listener)) {
            nodeListeners.add(listener);
        }
    }

    @Override
    public void removeNodeChangeListener(NodeChangeListener<Model> listener) {
        validateArgument(listener, "listener is null.");
        nodeListeners.remove(listener);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getName() + '[');

        for (Map.Entry<Integer, Object> property : properties.entrySet()) {
            builder.append(property.getKey()).append('=')
                    .append(property.getValue()).append(',');
        }
        builder.replace(builder.lastIndexOf(","), builder.length(), "]");

        return builder.toString();
    }
}
