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
package spritey.core.packer;

/**
 * Signals that sprite sheet is too small to fit all sprites.
 */
public class SizeTooSmallException extends Exception {

    /**
     * Auto-generated UID.
     */
    private static final long serialVersionUID = 8898174933215228502L;

    /**
     * Creates a new instance of SizeTooSmallException.
     */
    public SizeTooSmallException() {
        super();
    }

    /**
     * Creates a new instance of SizeTooSmallException with the specified
     * message.
     * 
     * @param message
     *        the message describing the reason for this exception.
     */
    public SizeTooSmallException(String message) {
        super(message);
    }

}
