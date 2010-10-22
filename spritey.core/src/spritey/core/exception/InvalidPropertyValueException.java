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
package spritey.core.exception;

/**
 * Thrown to indicate that a value is invalid and cannot be assigned to a
 * property. This exception provides information that describes why a value
 * could not be assigned.
 */
public class InvalidPropertyValueException extends Exception {

    /**
     * Auto-generated UID.
     */
    private static final long serialVersionUID = -8444951277278656057L;

    private int property;
    private Object value;

    private int errorCode;

    /**
     * Constructs a new exception for the specified invalid property value.
     * 
     * @param property
     *        the property the invalid value can not be assigned.
     * @param value
     *        the value that cannot be assigned.
     * @param errorCode
     *        an error code what explains why the value can not be assigned to
     *        the specified property.
     * @param message
     *        a descriptive message that explains why the value can not be
     *        assigned to the specified property.
     */
    public InvalidPropertyValueException(int property, Object value,
            int errorCode, String message) {
        super(message);
        this.property = property;
        this.value = value;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new exception for the specified invalid property value.
     * 
     * @param property
     *        the property the invalid value cannot be assigned.
     * @param value
     *        the value that cannot be assigned.
     * @param errorCode
     *        an error code what explains why the value can not be assigned to
     *        the specified property.
     * @param message
     *        a descriptive message that explains why the value can not be
     *        assigned to the specified property.
     * @param cause
     *        the cause of this throwable or <code>null</code> if the cause is
     *        nonexistent or unknown.
     */
    public InvalidPropertyValueException(int property, Object value,
            int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.property = property;
        this.value = value;
        this.errorCode = errorCode;
    }

    /**
     * Returns the property that the value cannot be assigned to.
     * 
     * @return the property the value cannot be assigned to.
     */
    public int getProperty() {
        return property;
    }

    /**
     * Returns an error code describing why the value could not be assigned to a
     * property.
     * 
     * @return an error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the value that could not be assigned.
     * 
     * @return the invalid value.
     */
    public Object getValue() {
        return value;
    }
}
