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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import spritey.core.Group;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
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
    public void setNameToString() {
        final int PROPERTY = Group.NAME;
        final Object VALUE = "NewGroup";

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
    public void setNodeToNode() {
        final int PROPERTY = Group.NODE;
        final Object VALUE = nodeMock;

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
    public void setNodeToNull() {
        final int PROPERTY = Group.NODE;
        final Object VALUE = null;

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
    public void removePropertyListener() {
        group.removeModelListener(listenerMock);

        group.setProperty(Group.NAME, "Name");

        verify(listenerMock, times(0)).propertyChanged(any(ModelEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullPropertyListener() {
        group.removeModelListener(null);
    }

    @Test
    public void removeValidator() {
        group.removeValidator(Group.NAME, validatorMock);

        group.setProperty(Group.NAME, "Name");

        verify(validatorMock, times(0)).isValid("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullValidator() {
        group.removeValidator(Group.NAME, null);
    }

}
