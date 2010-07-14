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
package spritey.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.validator.Validator;

/**
 * The default implementation of Model.
 */
public abstract class AbstractModel implements Model {

    private final Map<Integer, Object> properties;
    private final List<ModelListener> listeners;
    private final Map<Integer, List<Validator>> validators;

    /**
     * Default constructor
     */
    protected AbstractModel() {
        properties = new HashMap<Integer, Object>();
        listeners = new ArrayList<ModelListener>();
        validators = new HashMap<Integer, List<Validator>>();
    }

    /**
     * Validates the specified argument.
     * 
     * @param object
     *        the object to validate.
     * @throws IllegalArgumentException
     *         when the specified <code>object</code> is null.
     */
    private void validateArgument(Object object) {
        if (null == object) {
            throw new IllegalArgumentException("Argument is null.");
        }
    }

    /**
     * Sends the specified event to the registered model listeners.
     * 
     * @param event
     *        the event to send.
     */
    private void firePropertyChanged(ModelEvent event) {
        for (ModelListener listener : listeners) {
            listener.propertyChanged(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#setProperty(int, java.lang.Object)
     */
    @Override
    public void setProperty(int property, Object value)
            throws IllegalArgumentException {
        validate(property, value);

        // TODO No need to verify that property exists? This will allow new
        // properties to be set. Investigate.
        Object oldValue = properties.get(property);
        properties.put(property, value);

        firePropertyChanged(new ModelEvent(this, property, oldValue, value));
    }

    /**
     * Verifies that the specified value meets the validation requirements set
     * for the property.
     * 
     * @param property
     *        the property to verify the value for.
     * @param value
     *        the value to validate.
     * @throws IllegalArgumentException
     *         when the specified <code>value</code> does not meet validation
     *         requirements.
     */
    void validate(int property, Object value) throws IllegalArgumentException {
        List<Validator> list = validators.get(property);

        if (null != list) {
            for (Validator validator : list) {
                if (!validator.isValid(value)) {
                    throw new IllegalArgumentException(validator.getMessage());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#getProperty(int)
     */
    @Override
    public Object getProperty(int property) throws IllegalArgumentException {
        // TODO No need to verify that property exists? If property is not
        // in the map this will return null.
        return properties.get(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#addPropertyListener(spritey.core.event.
     * PropertyListener)
     */
    @Override
    public void addModelListener(ModelListener listener)
            throws IllegalArgumentException {
        validateArgument(listener);

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#removePropertyListener(spritey.core.event.
     * PropertyListener)
     */
    @Override
    public void removeModelListener(ModelListener listener)
            throws IllegalArgumentException {
        validateArgument(listener);
        listeners.remove(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#addValidator(int, spritey.core.validator.
     * Validator)
     */
    @Override
    public void addValidator(int property, Validator validator) {
        validateArgument(validator);
        List<Validator> list = validators.get(property);

        if (null == list) {
            // No validators for this property has been added yet.
            list = new ArrayList<Validator>();
            validators.put(property, list);
        }

        if (!list.contains(validator)) {
            list.add(validator);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.Model#removeValidator(int, spritey.core.validator.
     * Validator)
     */
    @Override
    public void removeValidator(int property, Validator validator) {
        validateArgument(validator);
        List<Validator> list = validators.get(property);

        if (null != list) {
            list.remove(validator);
        }
    }

}
