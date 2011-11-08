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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * Tests the implementation of
 * DiagonalFitMaintainAspectRatioAndPowerOfTwoStrategy.
 */
public class DiagonalFitMaintainAspectRatioAndPowerOfTwoStrategyTests {

    DiagonalFitMaintainAspectRatioAndPowerOfTwoStrategy strategy;

    @Mock
    Sheet sheet;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        strategy = new DiagonalFitMaintainAspectRatioAndPowerOfTwoStrategy();
    }

    @Test
    public void packOneSprite() throws SizeTooSmallException {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 6, 6);
        final Point NEW_LOCATION = new Point(0, 0);

        Sprite sprite = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(sprite).getBounds();
        doReturn(new Node[0]).when(sprite).getChildren();

        Sprite[] sprites = new Sprite[] { sprite };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(16, 32, true, true));

        verify(sprite).setLocation(NEW_LOCATION);
        verify(sheet).setWidth(8);
        verify(sheet).setHeight(16);
    }

    @Test(expected = SizeTooSmallException.class)
    public void packOneSpriteDoesNotFit() throws SizeTooSmallException {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);

        Sprite sprite = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(sprite).getBounds();
        doReturn(new Node[0]).when(sprite).getChildren();

        Sprite[] sprites = new Sprite[] { sprite };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(2, 8, true, true));
    }

    @Test
    public void packTwoSprites() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(60, 60);
        final Dimension SPRITE2_SIZE = new Dimension(70, 10);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Point NEW_LOCATION1 = new Point(0, 10);
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

        strategy.pack(sheet, new Constraints(128, 128, true, true));

        verify(sprite1).setLocation(NEW_LOCATION1);
        verify(sprite2).setLocation(NEW_LOCATION2);
        verify(sheet).setWidth(128);
        verify(sheet).setHeight(128);
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

        strategy.pack(sheet, new Constraints(4, 8, true, true));
    }

    @Test
    public void packTwoSpritesWithMaxWidth() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(8, 4);
        final Dimension SPRITE2_SIZE = new Dimension(8, 4);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Point NEW_LOCATION1 = new Point(0, 0);
        final Point NEW_LOCATION2 = new Point(0, 4);

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

        strategy.pack(sheet, new Constraints(8, 8, true, true));

        verify(sprite1).setLocation(NEW_LOCATION1);
        verify(sprite2).setLocation(NEW_LOCATION2);
        verify(sheet).setWidth(8);
        verify(sheet).setHeight(8);
    }

}
