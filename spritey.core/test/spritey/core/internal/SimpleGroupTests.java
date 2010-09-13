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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import spritey.core.Group;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of SimpleGroup.
 */
public class SimpleGroupTests {
    Group group;
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

        group = new SimpleGroup();
        group.addValidator(Group.NAME, validatorMock);
        group.addValidator(Group.NODE, validatorMock);
        group.addModelListener(listenerMock);
    }

    @Test
    public void setNameToString() throws InvalidPropertyValueException {
        final int PROPERTY = Group.NAME;
        final Object VALUE = "NewGroup";

        doReturn(true).when(validatorMock).isValid(VALUE);

        group.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, group.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(group, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNameToNull() {
        final int PROPERTY = Group.NAME;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Name is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            group.setProperty(PROPERTY, VALUE);
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
        final int PROPERTY = Group.NODE;
        final Object VALUE = nodeMock;

        doReturn(true).when(validatorMock).isValid(VALUE);

        group.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, group.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(group, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setNodeToNull() throws InvalidPropertyValueException {
        final int PROPERTY = Group.NODE;
        final Object VALUE = null;

        doReturn(true).when(validatorMock).isValid(VALUE);

        group.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, group.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(listenerMock).propertyChanged(eventCaptor.capture());
        assertEquals(group, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullPropertyListener() {
        group.addModelListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullValidator() {
        group.addValidator(Group.NODE, null);
    }

    @Test
    public void removePropertyListener() throws InvalidPropertyValueException {
        group.removeModelListener(listenerMock);

        doReturn(true).when(validatorMock).isValid("Name");

        group.setProperty(Group.NAME, "Name");

        verify(listenerMock, never()).propertyChanged(any(ModelEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullPropertyListener() {
        group.removeModelListener(null);
    }

    @Test
    public void removeValidator() throws InvalidPropertyValueException {
        group.removeValidator(Group.NAME, validatorMock);

        group.setProperty(Group.NAME, "Name");

        verify(validatorMock, never()).isValid("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullValidator() {
        group.removeValidator(Group.NAME, null);
    }

}
