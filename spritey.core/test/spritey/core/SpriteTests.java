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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests the implementation of Sprite.
 */
public class SpriteTests {

    @Mock
    Image image;

    Sprite sprite;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        sprite = spy(new Sprite(image));
    }

    @Test
    public void constructorWithImage() {
        assertEquals(Sprite.DEFAULT_NAME, sprite.getName());
        assertEquals(image, sprite.getImage());
    }

    @Test
    public void constructorWithNameAndImage() {
        Sprite sprite = new Sprite("SPRITE1", image);

        assertEquals("SPRITE1", sprite.getName());
        assertEquals(image, sprite.getImage());
    }

    @Test
    public void setAndGetLocation() {
        sprite.setLocation(new Point(5, 5));
        assertEquals(new Point(5, 5), sprite.getLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLocationToNull() {
        sprite.setLocation(null);
    }

    @Test
    public void setAndGetImage() {
        Image image = mock(Image.class);
        sprite.setImage(image);
        assertEquals(image, sprite.getImage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setImageToNull() {
        sprite.setImage(null);
    }

    @Test
    public void getSize() {
        when(image.getWidth(null)).thenReturn(5);
        when(image.getHeight(null)).thenReturn(6);

        assertEquals(new Dimension(5, 6), sprite.getSize());
    }

    @Test
    public void getSizeWhenImageIsNull() {
        when(sprite.getImage()).thenReturn(null);

        assertEquals(Sprite.DEFAULT_SIZE, sprite.getSize());
    }

    @Test(expected = RuntimeException.class)
    public void getSizeWhenImageHasNotFinishedLoading() {
        when(image.getWidth(null)).thenReturn(-1);
        when(image.getHeight(null)).thenReturn(-1);
        sprite.getSize();
    }

    @Test
    public void isVisible() {
        when(sprite.getLocation()).thenReturn(new Point(0, 0));
        assertTrue(sprite.isVisible());

        when(sprite.getLocation()).thenReturn(new Point(3, 2));
        assertTrue(sprite.isVisible());

        when(sprite.getLocation()).thenReturn(new Point(-1, 2));
        assertFalse(sprite.isVisible());

        when(sprite.getLocation()).thenReturn(new Point(3, -1));
        assertFalse(sprite.isVisible());

        when(sprite.getLocation()).thenReturn(new Point(-4, -1));
        assertFalse(sprite.isVisible());
    }

    @Test
    public void getBounds() {
        when(sprite.getLocation()).thenReturn(new Point(0, 0));
        doReturn(new Dimension(5, 6)).when(sprite).getSize();
        assertEquals(new Rectangle(0, 0, 5, 6), sprite.getBounds());

        when(sprite.getLocation()).thenReturn(new Point(-1, -1));
        when(sprite.getSize()).thenReturn(new Dimension(0, 0));
        assertEquals(new Rectangle(-1, -1, 0, 0), sprite.getBounds());
    }

}
