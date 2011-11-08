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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Sheet;

/**
 * Tests the implementation of MetadataWriter.
 */
public class MetadataWriterTests {

    MetadataWriter writer;

    @Mock
    Sheet sheet;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        writer = new MetadataWriter();
    }

    @Test
    public void isXml() {
        assertTrue(writer.isXml(new File("c:\\a.xml")));
        assertFalse(writer.isXml(new File("c:\\b.css")));
        assertFalse(writer.isXml(new File("c:\\a.bmp")));
        assertFalse(writer.isXml(new File("c:\\xml")));
        assertFalse(writer.isXml(new File("c:\\")));
    }

    @Test(expected = RuntimeException.class)
    public void writeUnsupportedFileExtension() throws IOException {
        writer.write(sheet, new File("c:\\a.css"));
    }

}
