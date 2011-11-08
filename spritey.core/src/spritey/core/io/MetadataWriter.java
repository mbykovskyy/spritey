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
import java.io.FileNotFoundException;
import java.io.IOException;

import spritey.core.Messages;
import spritey.core.Sheet;

/**
 * Writer for saving sprite sheet image to disk.
 */
public class MetadataWriter extends AbstractWriter {

    /**
     * Returns <code>true</code> if the specified file is XML.
     * 
     * @param file
     *        the file to check.
     * @return <code>true</code> if the file is XML, otherwise
     *         <code>false</code>.
     */
    protected boolean isXml(File file) {
        return getFileExt(file).equalsIgnoreCase("xml");
    }

    @Override
    public void write(Sheet sheet, File file) throws IllegalArgumentException,
            FileNotFoundException, IOException {
        validateNotNull(sheet, Messages.NULL);
        validateNotNull(file, Messages.NULL);

        if (isXml(file)) {
            new XmlWriter().write(sheet, file);
        } else {
            throw new RuntimeException("Unsupported file extension. "
                    + getFileExt(file) + " is currently not supported.");
        }
    }

}
