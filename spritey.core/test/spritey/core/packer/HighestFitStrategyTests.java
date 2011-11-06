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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * Tests the implementation of HighestFitStrategy.
 */
public class HighestFitStrategyTests {

    HighestFitStrategy strategy;

    @Mock
    Sheet sheet;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        strategy = new HighestFitStrategy();
    }

    @Test
    public void subtractNonOverlappingRectangle() {
        final Rectangle BIG_RECT = new Rectangle(0, 0, 10, 10);

        List<Rectangle> zones = strategy.subtract(BIG_RECT, new Rectangle(11,
                10, 5, 5));

        assertNotNull(zones);
        assertEquals(BIG_RECT, zones.get(0));
    }

    @Test
    public void subtractIntersectingRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(5, 2, 10, 5));

        assertNotNull(zones);
        assertEquals(3, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 0, 5, 10)));
        assertTrue(zones.contains(new Rectangle(0, 0, 10, 2)));
        assertTrue(zones.contains(new Rectangle(0, 7, 10, 3)));
    }

    @Test
    public void subtractContainedRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(2, 3, 6, 5));

        assertNotNull(zones);
        assertEquals(4, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 0, 10, 3)));
        assertTrue(zones.contains(new Rectangle(0, 0, 2, 10)));
        assertTrue(zones.contains(new Rectangle(0, 8, 10, 2)));
        assertTrue(zones.contains(new Rectangle(8, 0, 2, 10)));
    }

    @Test
    public void subtractTopLeftOverlappingRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(0, 0, 5, 5));

        assertNotNull(zones);
        assertEquals(2, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 5, 10, 5)));
        assertTrue(zones.contains(new Rectangle(5, 0, 5, 10)));
    }

    @Test
    public void subtractTopRightOverlappingRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(5, 0, 5, 5));

        assertNotNull(zones);
        assertEquals(2, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 5, 10, 5)));
        assertTrue(zones.contains(new Rectangle(0, 0, 5, 10)));
    }

    @Test
    public void subtractBottomLeftOverlappingRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(0, 5, 5, 5));

        assertNotNull(zones);
        assertEquals(2, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 0, 10, 5)));
        assertTrue(zones.contains(new Rectangle(5, 0, 5, 10)));
    }

    @Test
    public void subtractBottomRightOverlappingRectangle() {
        List<Rectangle> zones = strategy.subtract(new Rectangle(0, 0, 10, 10),
                new Rectangle(5, 5, 5, 5));

        assertNotNull(zones);
        assertEquals(2, zones.size());
        assertTrue(zones.contains(new Rectangle(0, 0, 10, 5)));
        assertTrue(zones.contains(new Rectangle(0, 0, 5, 10)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void subtractWhenMinuendRectangleIsNull() {
        strategy.subtract(null, new Rectangle(5, 5, 5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void subtractWhenSubtrahendRectangleIsNull() {
        strategy.subtract(new Rectangle(5, 5, 5, 5), null);
    }

    @Test
    public void sortZones() {
        List<Rectangle> list = new ArrayList<Rectangle>();
        list.add(new Rectangle(0, 0, 2, 4));
        list.add(new Rectangle(0, 0, 2, 2));
        list.add(new Rectangle(1, 0, 5, 5));
        list.add(new Rectangle(6, 5, 10, 5));
        list.add(new Rectangle(5, 5, 5, 5));
        list.add(new Rectangle(6, 5, 5, 5));
        list.add(new Rectangle(25, 25, 5, 5));

        strategy.sortZones(list);

        assertNotNull(list);
        assertEquals(7, list.size());
        assertEquals(new Rectangle(0, 0, 2, 4), list.get(0));
        assertEquals(new Rectangle(0, 0, 2, 2), list.get(1));
        assertEquals(new Rectangle(1, 0, 5, 5), list.get(2));
        assertEquals(new Rectangle(5, 5, 5, 5), list.get(3));
        assertEquals(new Rectangle(6, 5, 10, 5), list.get(4));
        assertEquals(new Rectangle(6, 5, 5, 5), list.get(5));
        assertEquals(new Rectangle(25, 25, 5, 5), list.get(6));
    }

    @Test
    public void packOneSprite() throws SizeTooSmallException {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);
        final Point NEW_LOCATION = new Point(0, 0);

        Sprite sprite = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(sprite).getBounds();
        doReturn(new Node[0]).when(sprite).getChildren();

        Sprite[] sprites = new Sprite[] { sprite };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints());

        verify(sprite).setLocation(NEW_LOCATION);
        verify(sheet).setWidth(3);
        verify(sheet).setHeight(4);
    }

    @Test(expected = SizeTooSmallException.class)
    public void packOneSpriteDoesNotFit() throws SizeTooSmallException {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);

        Sprite sprite = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(sprite).getBounds();
        doReturn(new Node[0]).when(sprite).getChildren();

        Sprite[] sprites = new Sprite[] { sprite };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(2, 2, false, false));
    }

    @Test
    public void packTwoSprites() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(3, 4);
        final Dimension SPRITE2_SIZE = new Dimension(5, 5);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Point NEW_LOCATION1 = new Point(5, 0);
        final Point NEW_LOCATION2 = new Point(0, 0);

        Sprite sprite1 = mock(Sprite.class);
        doReturn(SPRITE1_SIZE).when(sprite1).getSize();
        doReturn(SPRITE1_BOUNDS).when(sprite1).getBounds();
        doReturn(new Node[0]).when(sprite1).getChildren();

        Sprite sprite2 = mock(Sprite.class);
        doReturn(SPRITE2_SIZE).when(sprite2).getSize();
        doReturn(SPRITE2_BOUNDS).when(sprite2).getBounds();
        doReturn(new Node[0]).when(sprite2).getChildren();

        Sprite[] sprites = new Sprite[] { sprite1, sprite2 };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints());

        verify(sprite1).setLocation(NEW_LOCATION1);
        verify(sprite2).setLocation(NEW_LOCATION2);
        verify(sheet).setWidth(8);
        verify(sheet).setHeight(5);
    }

    @Test(expected = SizeTooSmallException.class)
    public void packTwoSpritesOneFitsOneDoesNot() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(3, 4);
        final Dimension SPRITE2_SIZE = new Dimension(5, 5);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);

        Sprite sprite1 = mock(Sprite.class);
        doReturn(SPRITE1_SIZE).when(sprite1).getSize();
        doReturn(SPRITE1_BOUNDS).when(sprite1).getBounds();
        doReturn(new Node[0]).when(sprite1).getChildren();

        Sprite sprite2 = mock(Sprite.class);
        doReturn(SPRITE2_SIZE).when(sprite2).getSize();
        doReturn(SPRITE2_BOUNDS).when(sprite2).getBounds();
        doReturn(new Node[0]).when(sprite2).getChildren();

        Sprite[] sprites = new Sprite[] { sprite1, sprite2 };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(5, 5, false, false));
    }

}
