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
package spritey.core.event;

import java.util.EventObject;

/**
 * An event object describing a change to a named property.
 */
public class ModelEvent extends EventObject {

    private final int property;
    private final Object oldValue;
    private final Object newValue;

    /**
     * Generated serial version UID for this class.
     */
    private static final long serialVersionUID = -8239095380915431880L;

    /**
     * Constructor
     * 
     * @param source
     *        the object that sent this event.
     * @param property
     *        the property that was changed.
     * @param oldValue
     *        the previous value.
     * @param newValue
     *        the value property has been set to.
     */
    public ModelEvent(Object source, int property, Object oldValue,
            Object newValue) {
        super(source);

        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Returns the name of the property that changed.
     * 
     * @return the property that changed.
     */
    public int getProperty() {
        return property;
    }

    /**
     * Returns the old value of the property.
     * 
     * @return the old value, or <code>null</code> if not known or not relevant.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Returns the new value of the property.
     * 
     * @return the new value, or <code>null</code> if not known or not relevant.
     */
    public Object getNewValue() {
        return newValue;
    }

}
