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

import spritey.core.Sheet;

/**
 * Writer for exporting sprite sheet to a disk.
 */
public interface Writer {

    /**
     * Writes the specified tree to a disk.
     * 
     * @param sheet
     *        the sheet node.
     * @param file
     *        the file to write to.
     * @throws FileNotFoundException
     *         if the file exists but is a directory rather than a regular file,
     *         does not exist but cannot be created, or cannot be opened for any
     *         other reason.
     * @throws IOException
     *         when problem occurres during writing.
     */
    public void write(Sheet sheet, File file) throws FileNotFoundException,
            IOException;

}
