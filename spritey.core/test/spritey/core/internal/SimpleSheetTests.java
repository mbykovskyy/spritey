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
package spritey.core.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.awt.Color;
import java.awt.Dimension;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import spritey.core.Sheet;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of SimpleSheet.
 */
public class SimpleSheetTests {
    Sheet sheet;
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

        sheet = new SimpleSheet();
        sheet.addValidator(Sheet.BACKGROUND, validatorMock);
        sheet.addValidator(Sheet.SIZE, validatorMock);
        sheet.addValidator(Sheet.OPAQUE, validatorMock);
        sheet.addValidator(Sheet.DESCRIPTION, validatorMock);
        sheet.addValidator(Sheet.NODE, validatorMock);
        sheet.addModelListener(listenerMock);
    }

    @Test
    public void setSizeToDimension() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.SIZE;
        final Object VALUE = new Dimension(14, 15);

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setSizeToNull() {
        final int PROPERTY = Sheet.SIZE;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Size is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            sheet.setProperty(PROPERTY, VALUE);
            fail("Expected spritey.core.exception.InvalidPropertyValueException.");
        } catch (InvalidPropertyValueException e) {
            assertEquals(ERROR_CODE, e.getErrorCode());
            assertEquals(ERROR_MESSAGE, e.getMessage());
            assertEquals(PROPERTY, e.getProperty());
            assertEquals(VALUE, e.getValue());
        }
    }

    @Test
    public void setBackgroundToColor() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.BACKGROUND;
        final Object VALUE = new Color(255, 0, 255);

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setBackgoundToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.BACKGROUND;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setOpaqueToBoolean() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.OPAQUE;
        final Object VALUE = true;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setOpaqueToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.OPAQUE;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Opaque is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            sheet.setProperty(PROPERTY, VALUE);
            fail("Expected spritey.core.exception.InvalidPropertyValueException.");
        } catch (InvalidPropertyValueException e) {
            assertEquals(ERROR_CODE, e.getErrorCode());
            assertEquals(ERROR_MESSAGE, e.getMessage());
            assertEquals(PROPERTY, e.getProperty());
            assertEquals(VALUE, e.getValue());
        }
    }

    @Test
    public void setDescriptionToString() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.DESCRIPTION;
        final Object VALUE = "Created with JUnit.";

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setDescriptionToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.DESCRIPTION;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNodeToNode() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.NODE;
        final Object VALUE = nodeMock;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNodeToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Sheet.NODE;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        sheet.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, sheet.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(sheet, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullPropertyListener() {
        sheet.addModelListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullValidator() {
        sheet.addValidator(Sheet.NODE, null);
    }

    @Test
    public void removePropertyListener() throws InvalidPropertyValueException {
        sheet.removeModelListener(listenerMock);

        doReturn(true).when(validatorMock).isValid(nodeMock);

        sheet.setProperty(Sheet.NODE, nodeMock);

        verify(listenerMock, never()).propertyChanged(any(ModelEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullPropertyListener() {
        sheet.removeModelListener(null);
    }

    @Test
    public void removeValidator() throws InvalidPropertyValueException {
        sheet.removeValidator(Sheet.NODE, validatorMock);

        sheet.setProperty(Sheet.NODE, nodeMock);

        verify(validatorMock, never()).isValid(nodeMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullValidator() {
        sheet.removeValidator(Sheet.NODE, null);
    }
}
