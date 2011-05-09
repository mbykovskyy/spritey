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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
        assertEquals(Sheet.DEFAULT_MAXIMUM_WIDTH, sheet.getMaxWidth());
        assertEquals(Sheet.DEFAULT_MAXIMUM_HEIGHT, sheet.getMaxHeight());
        assertEquals(Sheet.DEFAULT_WIDTH, sheet.getWidth());
        assertEquals(Sheet.DEFAULT_HEIGHT, sheet.getHeight());
        assertEquals(Sheet.DEFAULT_DESCRIPTION, sheet.getDescription());
        assertEquals(Sheet.DEFAULT_MAINTAIN_POWER_OF_TWO,
                sheet.maintainPowerOfTwo());
        assertEquals(Sheet.DEFAULT_MAINTAIN_ASPECT_RATIO,
                sheet.maintainAspectRatio());
    }

    @Test
    public void constructor() {
        sheet = new Sheet(new Color(231, 1, 2), 24, 56, false, false, "comment");

        assertEquals(new Color(231, 1, 2), sheet.getBackground());
        assertEquals(24, sheet.getMaxWidth());
        assertEquals(56, sheet.getMaxHeight());
        assertEquals(Sheet.DEFAULT_WIDTH, sheet.getWidth());
        assertEquals(Sheet.DEFAULT_HEIGHT, sheet.getHeight());
        assertEquals("comment", sheet.getDescription());
        assertEquals(false, sheet.maintainPowerOfTwo());
        assertEquals(false, sheet.maintainAspectRatio());
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
    public void setAndGetMaxWidth() {
        sheet.setMaxWidth(Sheet.MAX_MAXIMUM_WIDTH - 1);
        assertEquals(Sheet.MAX_MAXIMUM_WIDTH - 1, sheet.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthTooBig() {
        sheet.setMaxWidth(Sheet.MAX_MAXIMUM_WIDTH + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthTooSmall() {
        sheet.setMaxWidth(Sheet.MIN_MAXIMUM_WIDTH - 1);
    }

    @Test
    public void setMaxWidthToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);

        sheet.setMaxWidth(1);
        assertEquals(1, sheet.getMaxWidth());

        sheet.setMaxWidth(2);
        assertEquals(2, sheet.getMaxWidth());

        sheet.setMaxWidth(4);
        assertEquals(4, sheet.getMaxWidth());

        sheet.setMaxWidth(8);
        assertEquals(8, sheet.getMaxWidth());

        sheet.setMaxWidth(16);
        assertEquals(16, sheet.getMaxWidth());

        sheet.setMaxWidth(32);
        assertEquals(32, sheet.getMaxWidth());

        sheet.setMaxWidth(64);
        assertEquals(64, sheet.getMaxWidth());

        sheet.setMaxWidth(128);
        assertEquals(128, sheet.getMaxWidth());

        sheet.setMaxWidth(256);
        assertEquals(256, sheet.getMaxWidth());

        sheet.setMaxWidth(512);
        assertEquals(512, sheet.getMaxWidth());

        sheet.setMaxWidth(1024);
        assertEquals(1024, sheet.getMaxWidth());

        sheet.setMaxWidth(2048);
        assertEquals(2048, sheet.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);
        sheet.setMaxWidth(22);
    }

    @Test
    public void setAndGetMaxHeight() {
        sheet.setMaxHeight(Sheet.MAX_MAXIMUM_HEIGHT - 1);
        assertEquals(Sheet.MAX_MAXIMUM_HEIGHT - 1, sheet.getMaxHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightTooBig() {
        sheet.setMaxHeight(Sheet.MAX_MAXIMUM_HEIGHT + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightTooSmall() {
        sheet.setMaxHeight(Sheet.MIN_MAXIMUM_HEIGHT - 1);
    }

    @Test
    public void setMaxHeightToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);

        sheet.setMaxHeight(1);
        assertEquals(1, sheet.getMaxHeight());

        sheet.setMaxHeight(2);
        assertEquals(2, sheet.getMaxHeight());

        sheet.setMaxHeight(4);
        assertEquals(4, sheet.getMaxHeight());

        sheet.setMaxHeight(8);
        assertEquals(8, sheet.getMaxHeight());

        sheet.setMaxHeight(16);
        assertEquals(16, sheet.getMaxHeight());

        sheet.setMaxHeight(32);
        assertEquals(32, sheet.getMaxHeight());

        sheet.setMaxHeight(64);
        assertEquals(64, sheet.getMaxHeight());

        sheet.setMaxHeight(128);
        assertEquals(128, sheet.getMaxHeight());

        sheet.setMaxHeight(256);
        assertEquals(256, sheet.getMaxHeight());

        sheet.setMaxHeight(512);
        assertEquals(512, sheet.getMaxHeight());

        sheet.setMaxHeight(1024);
        assertEquals(1024, sheet.getMaxHeight());

        sheet.setMaxHeight(2048);
        assertEquals(2048, sheet.getMaxHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);
        sheet.setMaxHeight(241);
    }

    public void setAndGetWidth() {
        sheet.setWidth(Sheet.MIN_WIDTH + 1);
        assertEquals(Sheet.MIN_WIDTH + 1, sheet.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWidthTooBig() {
        when(sheet.getMaxWidth()).thenReturn(35);
        sheet.setWidth(36);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWidthTooSmall() {
        sheet.setWidth(Sheet.MIN_WIDTH - 1);
    }

    @Test
    public void setWidthToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);

        sheet.setWidth(1);
        assertEquals(1, sheet.getWidth());

        sheet.setWidth(2);
        assertEquals(2, sheet.getWidth());

        sheet.setWidth(4);
        assertEquals(4, sheet.getWidth());

        sheet.setWidth(8);
        assertEquals(8, sheet.getWidth());

        sheet.setWidth(16);
        assertEquals(16, sheet.getWidth());

        sheet.setWidth(32);
        assertEquals(32, sheet.getWidth());

        sheet.setWidth(64);
        assertEquals(64, sheet.getWidth());

        sheet.setWidth(128);
        assertEquals(128, sheet.getWidth());

        sheet.setWidth(256);
        assertEquals(256, sheet.getWidth());

        sheet.setWidth(512);
        assertEquals(512, sheet.getWidth());

        sheet.setWidth(1024);
        assertEquals(1024, sheet.getWidth());

        sheet.setWidth(2048);
        assertEquals(2048, sheet.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWidthToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);
        sheet.setWidth(56);
    }

    public void setAndGetHeight() {
        sheet.setHeight(Sheet.MIN_HEIGHT + 1);
        assertEquals(Sheet.MIN_HEIGHT + 1, sheet.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeightTooBig() {
        when(sheet.getMaxHeight()).thenReturn(53);
        sheet.setHeight(83);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeightTooSmall() {
        sheet.setHeight(Sheet.MIN_HEIGHT - 1);
    }

    @Test
    public void setHeightToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);

        sheet.setHeight(1);
        assertEquals(1, sheet.getHeight());

        sheet.setHeight(2);
        assertEquals(2, sheet.getHeight());

        sheet.setHeight(4);
        assertEquals(4, sheet.getHeight());

        sheet.setHeight(8);
        assertEquals(8, sheet.getHeight());

        sheet.setHeight(16);
        assertEquals(16, sheet.getHeight());

        sheet.setHeight(32);
        assertEquals(32, sheet.getHeight());

        sheet.setHeight(64);
        assertEquals(64, sheet.getHeight());

        sheet.setHeight(128);
        assertEquals(128, sheet.getHeight());

        sheet.setHeight(256);
        assertEquals(256, sheet.getHeight());

        sheet.setHeight(512);
        assertEquals(512, sheet.getHeight());

        sheet.setHeight(1024);
        assertEquals(1024, sheet.getHeight());

        sheet.setHeight(2048);
        assertEquals(2048, sheet.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeightToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(sheet.maintainPowerOfTwo()).thenReturn(true);
        sheet.setHeight(721);
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

    @Test
    public void setMaintainPowerOfTwo() {
        sheet.setMaintainPowerOfTwo(true);
        assertTrue(sheet.maintainPowerOfTwo());

        sheet.setMaintainPowerOfTwo(false);
        assertFalse(sheet.maintainPowerOfTwo());
    }

    @Test
    public void setMaintainAspectRatio() {
        sheet.setMaintainAspectRatio(true);
        assertTrue(sheet.maintainAspectRatio());

        sheet.setMaintainAspectRatio(false);
        assertFalse(sheet.maintainAspectRatio());
    }

    @Test
    public void isPowerOfTwo() {
        assertTrue(Sheet.isPowerOfTwo(1));
        assertTrue(Sheet.isPowerOfTwo(2));
        assertFalse(Sheet.isPowerOfTwo(3));
        assertTrue(Sheet.isPowerOfTwo(4));
        assertFalse(Sheet.isPowerOfTwo(5));
        assertFalse(Sheet.isPowerOfTwo(6));
        assertFalse(Sheet.isPowerOfTwo(7));
        assertTrue(Sheet.isPowerOfTwo(8));
        assertFalse(Sheet.isPowerOfTwo(9));
        assertFalse(Sheet.isPowerOfTwo(10));
        assertTrue(Sheet.isPowerOfTwo(16));
        assertFalse(Sheet.isPowerOfTwo(19));
        assertFalse(Sheet.isPowerOfTwo(24));
        assertTrue(Sheet.isPowerOfTwo(32));
        assertFalse(Sheet.isPowerOfTwo(36));
        assertFalse(Sheet.isPowerOfTwo(48));
        assertTrue(Sheet.isPowerOfTwo(64));
        assertFalse(Sheet.isPowerOfTwo(72));
        assertTrue(Sheet.isPowerOfTwo(128));
        assertTrue(Sheet.isPowerOfTwo(256));
        assertTrue(Sheet.isPowerOfTwo(512));
        assertTrue(Sheet.isPowerOfTwo(1024));
        assertTrue(Sheet.isPowerOfTwo(2048));
    }

}
