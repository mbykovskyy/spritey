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
package spritey.core.validator;

/**
 * An abstract implementation of Validator that provides implementation common
 * to all validators.
 */
public abstract class AbstractValidator implements Validator {

    private String message;
    private int errorCode;

    /**
     * Constructor
     */
    protected AbstractValidator() {
        message = "";
        errorCode = NONE;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the specified error message.
     * 
     * @param message
     *        the message to set.
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the specified error code.
     * 
     * @param errorCode
     *        the code to set.
     */
    protected void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
