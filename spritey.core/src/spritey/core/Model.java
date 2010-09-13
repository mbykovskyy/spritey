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

import spritey.core.event.ModelListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.validator.Validator;

/**
 * The Model class represents a set of object properties.
 */
public interface Model {

    /**
     * Sets the specified property.
     * 
     * @param property
     *        the property to set.
     * @param value
     *        the value to set the property to.
     * @throws InvalidPropertyValueException
     *         when the specified <code>value</code> does not meet validation
     *         requirements.
     */
    public void setProperty(int property, Object value)
            throws InvalidPropertyValueException;

    /**
     * Returns the value the specified property is set to.
     * 
     * @param property
     *        the property to get value for.
     * @return the value.
     */
    public Object getProperty(int property);

    /**
     * Adds model listener. No action is taken when the specified listener has
     * already been added. Calling this method will not generate a model change
     * event.
     * 
     * @param listener
     *        the listener to add.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void addModelListener(ModelListener listener);

    /**
     * Removes model listener. No action is taken when the specified listener
     * has never been added to a list. Calling this method will not generate a
     * model change event.
     * 
     * @param listener
     *        the listener to remove.
     * @throws IllegalArgumentException
     *         when the specified <code>listener</code> is null.
     */
    public void removeModelListener(ModelListener listener);

    /**
     * Adds the specified validator for the specified property. This validator
     * will be called every time the property changes. No action is taken when
     * the specified validator has already been added. Calling this method will
     * not generate a model change event.
     * 
     * @param validator
     *        the validator to add.
     * @param property
     *        the property to set validator for.
     * @throws IllegalArgumentException
     *         when the specified <code>validator</code> is null.
     */
    public void addValidator(int property, Validator validator);

    /**
     * Removes the specified validator from the specified property. No action is
     * taken when the specified validator has never been added. Calling this
     * method will not generate a model change event.
     * 
     * @param validator
     *        the validator to remove.
     * @param property
     *        the property to remove validator from.
     * @throws IllegalArgumentException
     *         when the specified <code>validator</code> is null.
     */
    public void removeValidator(int property, Validator validator);

}
