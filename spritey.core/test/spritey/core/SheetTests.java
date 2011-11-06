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
package spritey.core;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests the implementation of Sheet.
 */
public class SheetTests {

    @Spy
    Sheet sheet = new Sheet();

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }

    private String generateString(int length) {
        String text = "";

        for (int i = 0; i < length; ++i) {
            text += "a";
        }
        return text;
    }

    @Test
    public void defaultConstructor() {
        assertEquals(Sheet.DEFAULT_BACKGROUND, sheet.getBackground());
        assertEquals(Sheet.DEFAULT_WIDTH, sheet.getWidth());
        assertEquals(Sheet.DEFAULT_HEIGHT, sheet.getHeight());
        assertEquals(Sheet.DEFAULT_DESCRIPTION, sheet.getDescription());
    }

    @Test
    public void constructor() {
        sheet = new Sheet(new Color(231, 1, 2), "comment");

        assertEquals(new Color(231, 1, 2), sheet.getBackground());
        assertEquals(Sheet.DEFAULT_WIDTH, sheet.getWidth());
        assertEquals(Sheet.DEFAULT_HEIGHT, sheet.getHeight());
        assertEquals("comment", sheet.getDescription());
    }

    @Test
    public void setAndGetBackground() {
        sheet.setBackground(new Color(0, 212, 221));
        assertEquals(new Color(0, 212, 221), sheet.getBackground());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBackgroundToNull() {
        sheet.setBackground(null);
    }

    @Test
    public void setAndGetWidth() {
        sheet.setWidth(10);
        assertEquals(10, sheet.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeWidth() {
        sheet.setWidth(-1);
    }

    public void setAndGetHeight() {
        sheet.setHeight(15);
        assertEquals(15, sheet.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeHeight() {
        sheet.setHeight(-1);
    }

    @Test
    public void setAndGetDescription() {
        String description = generateString(Sheet.MAX_DESCRIPTION_LENGTH - 1);
        sheet.setDescription(description);
        assertEquals(description, sheet.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDescriptionToNull() {
        sheet.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDescriptionTooLong() {
        sheet.setDescription(generateString(Sheet.MAX_DESCRIPTION_LENGTH + 1));
    }

    @Test
    public void setEmptyDescription() {
        sheet.setDescription("");
        assertEquals("", sheet.getDescription());
    }

}
