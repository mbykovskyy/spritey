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
 * Tests the implementation of HighestFitMaintainRatioStrategy.
 */
public class HighestFitMaintainRatioStrategyTests {

    HighestFitMaintainRatioStrategy strategy;

    @Mock
    Sheet sheet;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        strategy = new HighestFitMaintainRatioStrategy();
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

        strategy.pack(sheet, new Constraints(10, 50, false, true));

        verify(sprite).setLocation(NEW_LOCATION);
        verify(sheet).setWidth(6);
        verify(sheet).setHeight(30);
    }

    @Test(expected = SizeTooSmallException.class)
    public void packOneSpriteDoesNotFit() throws SizeTooSmallException {
        final Rectangle SPRITE_BOUNDS = new Rectangle(-1, -1, 3, 4);

        Sprite sprite = mock(Sprite.class);
        doReturn(SPRITE_BOUNDS).when(sprite).getBounds();
        doReturn(new Node[0]).when(sprite).getChildren();

        Sprite[] sprites = new Sprite[] { sprite };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(2, 6, false, true));
    }

    @Test
    public void packTwoSprites() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(6, 6);
        final Dimension SPRITE2_SIZE = new Dimension(5, 4);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Point NEW_LOCATION1 = new Point(0, 0);
        final Point NEW_LOCATION2 = new Point(6, 0);

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

        strategy.pack(sheet, new Constraints(20, 20, false, true));

        verify(sprite1).setLocation(NEW_LOCATION1);
        verify(sprite2).setLocation(NEW_LOCATION2);
        verify(sheet).setWidth(11);
        verify(sheet).setHeight(11);
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

        strategy.pack(sheet, new Constraints(6, 7, false, true));
    }

    @Test
    public void packThirdSpriteAfterExpanding() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(6, 6);
        final Dimension SPRITE2_SIZE = new Dimension(5, 4);
        final Dimension SPRITE3_SIZE = new Dimension(3, 3);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Rectangle SPRITE3_BOUNDS = new Rectangle(UNSET, SPRITE3_SIZE);
        final Point NEW_LOCATION1 = new Point(0, 0);
        final Point NEW_LOCATION2 = new Point(6, 0);
        final Point NEW_LOCATION3 = new Point(6, 4);

        Sprite sprite1 = mock(Sprite.class);
        doReturn(SPRITE1_SIZE).when(sprite1).getSize();
        doReturn(SPRITE1_BOUNDS).when(sprite1).getBounds();
        doReturn(new Node[0]).when(sprite1).getChildren();

        Sprite sprite2 = mock(Sprite.class);
        doReturn(SPRITE2_SIZE).when(sprite2).getSize();
        doReturn(SPRITE2_BOUNDS).when(sprite2).getBounds();
        doReturn(new Node[0]).when(sprite2).getChildren();

        Sprite sprite3 = mock(Sprite.class);
        doReturn(SPRITE3_SIZE).when(sprite3).getSize();
        doReturn(SPRITE3_BOUNDS).when(sprite3).getBounds();
        doReturn(new Node[0]).when(sprite3).getChildren();

        Sprite[] sprites = new Sprite[] { sprite1, sprite2, sprite3 };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(20, 20, false, true));

        verify(sprite1).setLocation(NEW_LOCATION1);
        verify(sprite2).setLocation(NEW_LOCATION2);
        verify(sprite3).setLocation(NEW_LOCATION3);
        verify(sheet).setWidth(11);
        verify(sheet).setHeight(11);
    }

    @Test(expected = SizeTooSmallException.class)
    public void packThreeSpritesThirdDoesNotFit() throws SizeTooSmallException {
        final Point UNSET = new Point(-1, -1);
        final Dimension SPRITE1_SIZE = new Dimension(6, 6);
        final Dimension SPRITE2_SIZE = new Dimension(5, 4);
        final Dimension SPRITE3_SIZE = new Dimension(3, 3);

        final Rectangle SPRITE1_BOUNDS = new Rectangle(UNSET, SPRITE1_SIZE);
        final Rectangle SPRITE2_BOUNDS = new Rectangle(UNSET, SPRITE2_SIZE);
        final Rectangle SPRITE3_BOUNDS = new Rectangle(UNSET, SPRITE3_SIZE);

        Sprite sprite1 = mock(Sprite.class);
        doReturn(SPRITE1_SIZE).when(sprite1).getSize();
        doReturn(SPRITE1_BOUNDS).when(sprite1).getBounds();
        doReturn(new Node[0]).when(sprite1).getChildren();

        Sprite sprite2 = mock(Sprite.class);
        doReturn(SPRITE2_SIZE).when(sprite2).getSize();
        doReturn(SPRITE2_BOUNDS).when(sprite2).getBounds();
        doReturn(new Node[0]).when(sprite2).getChildren();

        Sprite sprite3 = mock(Sprite.class);
        doReturn(SPRITE3_SIZE).when(sprite3).getSize();
        doReturn(SPRITE3_BOUNDS).when(sprite3).getBounds();
        doReturn(new Node[0]).when(sprite3).getChildren();

        Sprite[] sprites = new Sprite[] { sprite1, sprite2, sprite3 };
        doReturn(sprites).when(sheet).getChildren();

        strategy.pack(sheet, new Constraints(11, 6, false, true));
    }

}
