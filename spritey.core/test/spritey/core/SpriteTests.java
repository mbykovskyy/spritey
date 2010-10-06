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
package spritey.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of SimpleSprite.
 */
public class SpriteTests {
    Sprite sprite;
    ModelListener listenerMock;
    Validator validatorMock;
    Node nodeMock;

    @Captor
    ArgumentCaptor<ModelEvent> eventCaptor;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        listenerMock = mock(ModelListener.class);
        validatorMock = mock(Validator.class);
        nodeMock = mock(Node.class);

        sprite = new Sprite();
        sprite.addValidator(Sprite.NAME, validatorMock);
        sprite.addValidator(Sprite.NODE, validatorMock);
        sprite.addValidator(Sprite.BOUNDS, validatorMock);
        sprite.addValidator(Sprite.IMAGE, validatorMock);
        sprite.addModelListener(listenerMock);
    }

    @Test
    public void setBoundsToRectangle() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.BOUNDS;
        final Object VALUE = new Rectangle(0, 0, 14, 15);

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setBoundsToNull() {
        final int PROPERTY = Sprite.BOUNDS;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Bounds is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            sprite.setProperty(PROPERTY, VALUE);
            fail("Expected spritey.core.exception.InvalidPropertyValueException.");
        } catch (InvalidPropertyValueException e) {
            assertEquals(ERROR_CODE, e.getErrorCode());
            assertEquals(ERROR_MESSAGE, e.getMessage());
            assertEquals(PROPERTY, e.getProperty());
            assertEquals(VALUE, e.getValue());
        }
    }

    @Test
    public void setNameToString() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.NAME;
        final Object VALUE = "NewSprite";

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNameToNull() {
        final int PROPERTY = Sprite.NAME;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Name is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            sprite.setProperty(PROPERTY, VALUE);
            fail("Expected spritey.core.exception.InvalidPropertyValueException.");
        } catch (InvalidPropertyValueException e) {
            assertEquals(ERROR_CODE, e.getErrorCode());
            assertEquals(ERROR_MESSAGE, e.getMessage());
            assertEquals(PROPERTY, e.getProperty());
            assertEquals(VALUE, e.getValue());
        }
    }

    @Test
    public void setNodeToNode() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.NODE;
        final Object VALUE = nodeMock;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNodeToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.NODE;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setImageToBufferedImage() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.IMAGE;
        final Object VALUE = new BufferedImage(32, 32,
                BufferedImage.TYPE_INT_RGB);

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setImageToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sprite.IMAGE;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sprite.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sprite.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sprite, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullPropertyListener() {
        sprite.addModelListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullValidator() {
        sprite.addValidator(Sprite.NODE, null);
    }

    @Test
    public void removePropertyListener() throws InvalidPropertyValueException {
        sprite.removeModelListener(listenerMock);

        doReturn(true).when(validatorMock).isValid("Name");

        sprite.setProperty(Sprite.NAME, "Name");

        verify(listenerMock, never()).propertyChanged(any(ModelEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullPropertyListener() {
        sprite.removeModelListener(null);
    }

    @Test
    public void removeValidator() throws InvalidPropertyValueException {
        sprite.removeValidator(Sprite.NAME, validatorMock);

        sprite.setProperty(Sprite.NAME, "Name");

        verify(validatorMock, never()).isValid("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullValidator() {
        sprite.removeValidator(Sprite.NAME, null);
    }
}
