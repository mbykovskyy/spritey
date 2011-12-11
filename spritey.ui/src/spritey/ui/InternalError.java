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
package spritey.ui;

/**
 * This error is thrown whenever an error occurs internally in Spritey. It
 * should never happen unless there's a programming error. This error is caught
 * by a top level exception handler which logs it and displays its user-friendly
 * localised message to the user.
 */
public class InternalError extends Error {

    /**
     * Auto-generated UID.
     */
    private static final long serialVersionUID = 7962971171508321772L;

    /**
     * Creates a new instance of InternalError with the specified message.
     * 
     * @param message
     *        the message describing the reason for this error.
     */
    public InternalError(String message) {
        super(message);
    }

    /**
     * Creates a new instance of InternalError with message and the cause.
     * 
     * @param message
     *        the message describing the reason for this error.
     * @param cause
     *        the cause of this error.
     */
    public InternalError(String message, Throwable cause) {
        super(message, cause);
    }

}
