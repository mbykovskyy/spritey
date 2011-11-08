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
package spritey.core.io;

import java.io.File;

/**
 * AbstractWriter defining common behaviour for exporting sprite sheet to a
 * disk.
 */
public abstract class AbstractWriter implements Writer {

    /**
     * Validates that the specified object is not null.
     * 
     * @param o
     *        the object to validate.
     * @param msg
     *        the exception message.
     */
    protected void validateNotNull(Object o, String msg) {
        if (null == o) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Returns the file extension.
     * 
     * @param file
     *        the file to get extension for.
     * @return file extension.
     */
    protected String getFileExt(File file) {
        String[] parts = file.getName().split("\\.");
        return parts.length < 2 ? "" : parts[parts.length - 1];
    }

}
