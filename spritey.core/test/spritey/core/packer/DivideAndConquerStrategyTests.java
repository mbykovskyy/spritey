/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.adapter.AdapterFactory;

/**
 * Tests the implementation of Packer.
 */
public class DivideAndConquerStrategyTests {

    Sheet sheetMock;
    AdapterFactory factoryMock;

    DivideAndConquerStrategy strategy;

    @Before
    public void initialize() {
        final Dimension SHEET_SIZE = new Dimension(10, 10);

        sheetMock = mock(Sheet.class);
        doReturn(SHEET_SIZE).when(sheetMock).getProperty(Sheet.SIZE);

        factoryMock = mock(AdapterFactory.class);
        doReturn(SHEET_SIZE).when(factoryMock).getAdapter(SHEET_SIZE,
                Dimension.class);

        strategy = new DivideAndConquerStrategy(sheetMock, factoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWhenSheetIsNull() {
        new DivideAndConquerStrategy(null, factoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWhenAdapterFactoryIsNull() {
        new DivideAndConquerStrategy(sheetMock, null);
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
    public void sortAndFilter() {
        List<Rectangle> list = new ArrayList<Rectangle>();
        list.add(new Rectangle(0, 0, 2, 4));
        list.add(new Rectangle(0, 0, 2, 2));
        list.add(new Rectangle(1, 0, 5, 5));
        list.add(new Rectangle(6, 5, 10, 5));
        list.add(new Rectangle(5, 5, 5, 5));
        list.add(new Rectangle(6, 5, 5, 5));

        strategy.sortAndFilter(list);

        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(new Rectangle(0, 0, 2, 4), list.get(0));
        assertEquals(new Rectangle(1, 0, 5, 5), list.get(1));
        assertEquals(new Rectangle(5, 5, 5, 5), list.get(2));
        assertEquals(new Rectangle(6, 5, 10, 5), list.get(3));
    }

    @Test
    public void packOneSprite() {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);

        Sprite spriteMock = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(spriteMock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE_BOUNDS).when(factoryMock).getAdapter(SPRITE_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { spriteMock };
        strategy.pack(sprites, false);

        assertNotNull(sprites);
        assertEquals(1, sprites.length);
        assertEquals(spriteMock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(2, freeZones.length);
        assertEquals(new Rectangle(0, 4, 10, 6), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 10), freeZones[1]);
    }

    @Test
    public void packOneSpriteAfterFlush() {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);

        Sprite spriteMock = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(spriteMock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE_BOUNDS).when(factoryMock).getAdapter(SPRITE_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { spriteMock };
        strategy.pack(sprites, true);

        assertNotNull(sprites);
        assertTrue(1 == sprites.length);
        assertEquals(spriteMock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(2, freeZones.length);
        assertEquals(new Rectangle(0, 4, 10, 6), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 10), freeZones[1]);
    }

    @Test
    public void packTwoSprites() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(-1, -1, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 5, 5);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, false);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);
        assertEquals(new Point(0, 4), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(3, freeZones.length);
        assertEquals(new Rectangle(0, 9, 10, 1), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 4), freeZones[1]);
        assertEquals(new Rectangle(5, 0, 5, 10), freeZones[2]);
    }

    @Test
    public void packTwoSpritesAfterFlush() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(-1, -1, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 5, 5);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, true);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);
        assertEquals(new Point(0, 4), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(3, freeZones.length);
        assertEquals(new Rectangle(0, 9, 10, 1), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 4), freeZones[1]);
        assertEquals(new Rectangle(5, 0, 5, 10), freeZones[2]);
    }

    @Test
    public void packTwoSpritesOneFitsOneDoesNot() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(-1, -1, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 15, 15);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, false);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);
        assertEquals(new Point(-1, -1), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(2, freeZones.length);
        assertEquals(new Rectangle(0, 4, 10, 6), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 10), freeZones[1]);
    }

    @Test
    public void packTwoSpritesOneFitsOneDoesNotAfterFlush() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(-1, -1, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 15, 15);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, true);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);
        assertEquals(new Point(-1, -1), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(2, freeZones.length);
        assertEquals(new Rectangle(0, 4, 10, 6), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 10), freeZones[1]);
    }

    @Test
    public void packTwoSpritesOneLocatedOneIsNot() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(0, 0, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 5, 5);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, false);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);

        // TODO This will fail because free zones have not been generated. So,
        // sprite2 will be positioned at [0, 0]. Is this behaviour obvious?
        // Should we recalculate free zones every time we pack sprites? This
        // might make it quite slow. We'll leave this test failing as a reminder
        // until we decide on how to handle this. But for now to recalculate
        // free zones a client should flush the cache.
        assertEquals(new Point(0, 4), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(3, freeZones.length);
        assertEquals(new Rectangle(0, 9, 10, 1), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 4), freeZones[1]);
        assertEquals(new Rectangle(5, 0, 5, 10), freeZones[2]);
    }

    @Test
    public void packTwoSpritesOneLocatedOneIsNotAfterFlush() {
        final Rectangle SPRITE1_BOUNDS = new Rectangle(0, 0, 3, 4);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(-1, -1, 5, 5);

        Sprite sprite1Mock = mock(Sprite.class);
        doReturn(SPRITE1_BOUNDS).when(sprite1Mock).getProperty(Sprite.BOUNDS);

        Sprite sprite2Mock = mock(Sprite.class);
        doReturn(SPRITE2_BOUNDS).when(sprite2Mock).getProperty(Sprite.BOUNDS);

        doReturn(SPRITE1_BOUNDS).when(factoryMock).getAdapter(SPRITE1_BOUNDS,
                Rectangle.class);
        doReturn(SPRITE2_BOUNDS).when(factoryMock).getAdapter(SPRITE2_BOUNDS,
                Rectangle.class);

        Sprite[] sprites = new Sprite[] { sprite1Mock, sprite2Mock };
        strategy.pack(sprites, true);

        assertNotNull(sprites);
        assertTrue(2 == sprites.length);
        assertEquals(sprite1Mock, sprites[0]);
        assertEquals(new Point(0, 0), SPRITE1_BOUNDS.getLocation());
        assertEquals(sprite2Mock, sprites[1]);
        assertEquals(new Point(0, 4), SPRITE2_BOUNDS.getLocation());

        Rectangle[] freeZones = strategy.getFreeZones();
        assertNotNull(freeZones);
        assertEquals(3, freeZones.length);
        assertEquals(new Rectangle(0, 9, 10, 1), freeZones[0]);
        assertEquals(new Rectangle(3, 0, 7, 4), freeZones[1]);
        assertEquals(new Rectangle(5, 0, 5, 10), freeZones[2]);
    }

}
