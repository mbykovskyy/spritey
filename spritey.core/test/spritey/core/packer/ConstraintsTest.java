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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests the implementation of Constrains.
 */
public class ConstraintsTest {

    @Spy
    Constraints constraints = new Constraints();

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void defaultConstructor() {
        assertEquals(Constraints.DEFAULT_MAXIMUM_WIDTH,
                constraints.getMaxWidth());
        assertEquals(Constraints.DEFAULT_MAXIMUM_HEIGHT,
                constraints.getMaxHeight());
        assertEquals(Constraints.DEFAULT_MAINTAIN_POWER_OF_TWO,
                constraints.maintainPowerOfTwo());
        assertEquals(Constraints.DEFAULT_MAINTAIN_ASPECT_RATIO,
                constraints.maintainAspectRatio());
    }

    @Test
    public void constructor() {
        constraints = new Constraints(24, 56, false, false);

        assertEquals(24, constraints.getMaxWidth());
        assertEquals(56, constraints.getMaxHeight());
        assertEquals(false, constraints.maintainPowerOfTwo());
        assertEquals(false, constraints.maintainAspectRatio());
    }

    @Test
    public void setAndGetMaxWidth() {
        constraints.setMaxWidth(Constraints.MAX_MAXIMUM_WIDTH - 1);
        assertEquals(Constraints.MAX_MAXIMUM_WIDTH - 1,
                constraints.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthTooBig() {
        constraints.setMaxWidth(Constraints.MAX_MAXIMUM_WIDTH + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthTooSmall() {
        constraints.setMaxWidth(Constraints.MIN_MAXIMUM_WIDTH - 1);
    }

    @Test
    public void setMaxWidthToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(constraints.maintainPowerOfTwo()).thenReturn(true);

        constraints.setMaxWidth(1);
        assertEquals(1, constraints.getMaxWidth());

        constraints.setMaxWidth(2);
        assertEquals(2, constraints.getMaxWidth());

        constraints.setMaxWidth(4);
        assertEquals(4, constraints.getMaxWidth());

        constraints.setMaxWidth(8);
        assertEquals(8, constraints.getMaxWidth());

        constraints.setMaxWidth(16);
        assertEquals(16, constraints.getMaxWidth());

        constraints.setMaxWidth(32);
        assertEquals(32, constraints.getMaxWidth());

        constraints.setMaxWidth(64);
        assertEquals(64, constraints.getMaxWidth());

        constraints.setMaxWidth(128);
        assertEquals(128, constraints.getMaxWidth());

        constraints.setMaxWidth(256);
        assertEquals(256, constraints.getMaxWidth());

        constraints.setMaxWidth(512);
        assertEquals(512, constraints.getMaxWidth());

        constraints.setMaxWidth(1024);
        assertEquals(1024, constraints.getMaxWidth());

        constraints.setMaxWidth(2048);
        assertEquals(2048, constraints.getMaxWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxWidthToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(constraints.maintainPowerOfTwo()).thenReturn(true);
        constraints.setMaxWidth(22);
    }

    @Test
    public void setAndGetMaxHeight() {
        constraints.setMaxHeight(Constraints.MAX_MAXIMUM_HEIGHT - 1);
        assertEquals(Constraints.MAX_MAXIMUM_HEIGHT - 1,
                constraints.getMaxHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightTooBig() {
        constraints.setMaxHeight(Constraints.MAX_MAXIMUM_HEIGHT + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightTooSmall() {
        constraints.setMaxHeight(Constraints.MIN_MAXIMUM_HEIGHT - 1);
    }

    @Test
    public void setMaxHeightToPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(constraints.maintainPowerOfTwo()).thenReturn(true);

        constraints.setMaxHeight(1);
        assertEquals(1, constraints.getMaxHeight());

        constraints.setMaxHeight(2);
        assertEquals(2, constraints.getMaxHeight());

        constraints.setMaxHeight(4);
        assertEquals(4, constraints.getMaxHeight());

        constraints.setMaxHeight(8);
        assertEquals(8, constraints.getMaxHeight());

        constraints.setMaxHeight(16);
        assertEquals(16, constraints.getMaxHeight());

        constraints.setMaxHeight(32);
        assertEquals(32, constraints.getMaxHeight());

        constraints.setMaxHeight(64);
        assertEquals(64, constraints.getMaxHeight());

        constraints.setMaxHeight(128);
        assertEquals(128, constraints.getMaxHeight());

        constraints.setMaxHeight(256);
        assertEquals(256, constraints.getMaxHeight());

        constraints.setMaxHeight(512);
        assertEquals(512, constraints.getMaxHeight());

        constraints.setMaxHeight(1024);
        assertEquals(1024, constraints.getMaxHeight());

        constraints.setMaxHeight(2048);
        assertEquals(2048, constraints.getMaxHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxHeightToNonPowerOfTwoWhenMaintainPowerOfTwoEnabled() {
        when(constraints.maintainPowerOfTwo()).thenReturn(true);
        constraints.setMaxHeight(241);
    }

    @Test
    public void setMaintainPowerOfTwo() {
        constraints.setMaintainPowerOfTwo(true);
        assertTrue(constraints.maintainPowerOfTwo());

        constraints.setMaintainPowerOfTwo(false);
        assertFalse(constraints.maintainPowerOfTwo());
    }

    @Test
    public void setMaintainAspectRatio() {
        constraints.setMaintainAspectRatio(true);
        assertTrue(constraints.maintainAspectRatio());

        constraints.setMaintainAspectRatio(false);
        assertFalse(constraints.maintainAspectRatio());
    }

    @Test
    public void isPowerOfTwo() {
        assertTrue(Constraints.isPowerOfTwo(1));
        assertTrue(Constraints.isPowerOfTwo(2));
        assertFalse(Constraints.isPowerOfTwo(3));
        assertTrue(Constraints.isPowerOfTwo(4));
        assertFalse(Constraints.isPowerOfTwo(5));
        assertFalse(Constraints.isPowerOfTwo(6));
        assertFalse(Constraints.isPowerOfTwo(7));
        assertTrue(Constraints.isPowerOfTwo(8));
        assertFalse(Constraints.isPowerOfTwo(9));
        assertFalse(Constraints.isPowerOfTwo(10));
        assertTrue(Constraints.isPowerOfTwo(16));
        assertFalse(Constraints.isPowerOfTwo(19));
        assertFalse(Constraints.isPowerOfTwo(24));
        assertTrue(Constraints.isPowerOfTwo(32));
        assertFalse(Constraints.isPowerOfTwo(36));
        assertFalse(Constraints.isPowerOfTwo(48));
        assertTrue(Constraints.isPowerOfTwo(64));
        assertFalse(Constraints.isPowerOfTwo(72));
        assertTrue(Constraints.isPowerOfTwo(128));
        assertTrue(Constraints.isPowerOfTwo(256));
        assertTrue(Constraints.isPowerOfTwo(512));
        assertTrue(Constraints.isPowerOfTwo(1024));
        assertTrue(Constraints.isPowerOfTwo(2048));
    }

    @Test
    public void nextPowerOfTwo() {
        assertEquals(2, Constraints.nextPowerOfTwo(1));
        assertEquals(4, Constraints.nextPowerOfTwo(2));
        assertEquals(4, Constraints.nextPowerOfTwo(3));
        assertEquals(8, Constraints.nextPowerOfTwo(4));
        assertEquals(8, Constraints.nextPowerOfTwo(5));
        assertEquals(8, Constraints.nextPowerOfTwo(6));
        assertEquals(8, Constraints.nextPowerOfTwo(7));
        assertEquals(16, Constraints.nextPowerOfTwo(8));
        assertEquals(16, Constraints.nextPowerOfTwo(9));
        assertEquals(16, Constraints.nextPowerOfTwo(10));
        assertEquals(32, Constraints.nextPowerOfTwo(16));
        assertEquals(32, Constraints.nextPowerOfTwo(19));
        assertEquals(32, Constraints.nextPowerOfTwo(24));
        assertEquals(64, Constraints.nextPowerOfTwo(32));
        assertEquals(64, Constraints.nextPowerOfTwo(36));
        assertEquals(64, Constraints.nextPowerOfTwo(48));
        assertEquals(128, Constraints.nextPowerOfTwo(64));
        assertEquals(128, Constraints.nextPowerOfTwo(72));
        assertEquals(256, Constraints.nextPowerOfTwo(128));
        assertEquals(512, Constraints.nextPowerOfTwo(256));
        assertEquals(1024, Constraints.nextPowerOfTwo(512));
        assertEquals(2048, Constraints.nextPowerOfTwo(1024));
        assertEquals(4096, Constraints.nextPowerOfTwo(2048));
    }

}
