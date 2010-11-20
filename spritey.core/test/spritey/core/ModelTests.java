/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.event.NodeChangeListener;
import spritey.core.event.PropertyChangeEvent;
import spritey.core.event.PropertyChangeListener;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.validator.Validator;

/**
 * Tests the implementation of Node.
 */
public class ModelTests {

    static class TestModel extends Model {
        static final int PROPERTY = 0;
    }

    Model model;

    @Mock
    PropertyChangeListener propertyChangeListenerMock;

    @Mock
    NodeChangeListener<Model> nodeChangeListenerMock;

    @Mock
    Validator validatorMock;

    @Captor
    ArgumentCaptor<PropertyChangeEvent> eventCaptor;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        model = new TestModel();
        model.addValidator(TestModel.PROPERTY, validatorMock);
        model.addPropertyChangeListener(propertyChangeListenerMock);
        model.addNodeChangeListener(nodeChangeListenerMock);
    }

    @Test
    public void setProperty() throws InvalidPropertyValueException {
        final int PROPERTY = TestModel.PROPERTY;
        final Object VALUE = "value";

        doReturn(true).when(validatorMock).isValid(VALUE);

        model.setProperty(PROPERTY, VALUE);

        assertEquals(VALUE, model.getProperty(PROPERTY));
        verify(validatorMock).isValid(VALUE);
        verify(propertyChangeListenerMock).propertyChanged(
                eventCaptor.capture());
        assertEquals(model, eventCaptor.getValue().getSource());
        assertEquals(PROPERTY, eventCaptor.getValue().getProperty());
        assertEquals(null, eventCaptor.getValue().getOldValue());
        assertEquals(VALUE, eventCaptor.getValue().getNewValue());
    }

    @Test
    public void setPropertyToNull() {
        final int PROPERTY = TestModel.PROPERTY;
        final Object VALUE = null;
        final int ERROR_CODE = 1;
        final String ERROR_MESSAGE = "Value is null.";

        doReturn(false).when(validatorMock).isValid(VALUE);
        doReturn(ERROR_CODE).when(validatorMock).getErrorCode();
        doReturn(ERROR_MESSAGE).when(validatorMock).getMessage();

        try {
            model.setProperty(PROPERTY, VALUE);
            fail("Expected spritey.core.exception.InvalidPropertyValueException.");
        } catch (InvalidPropertyValueException e) {
            assertEquals(ERROR_CODE, e.getErrorCode());
            assertEquals(ERROR_MESSAGE, e.getMessage());
            assertEquals(PROPERTY, e.getProperty());
            assertEquals(VALUE, e.getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullPropertyChangeListener() {
        model.addPropertyChangeListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullValidator() {
        model.addValidator(TestModel.PROPERTY, null);
    }

    @Test
    public void removePropertyChangeListener()
            throws InvalidPropertyValueException {
        model.removePropertyChangeListener(propertyChangeListenerMock);

        final String VALUE = "value";
        doReturn(true).when(validatorMock).isValid(VALUE);
        model.setProperty(TestModel.PROPERTY, VALUE);

        verify(propertyChangeListenerMock, never()).propertyChanged(
                any(PropertyChangeEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullPropertyChangeListener() {
        model.removePropertyChangeListener(null);
    }

    @Test
    public void removeValidator() throws InvalidPropertyValueException {
        model.removeValidator(TestModel.PROPERTY, validatorMock);
        assertEquals(0, model.getValidators(TestModel.PROPERTY).length);

        final String VALUE = "value";
        model.setProperty(TestModel.PROPERTY, VALUE);
        verify(validatorMock, never()).isValid(VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullValidator() {
        model.removeValidator(TestModel.PROPERTY, null);
    }

    @Test
    public void addAndGetChildren() {
        Model mock1 = mock(Model.class);
        Model mock2 = mock(Model.class);

        Model[] expectedChildren = new Model[] { mock1, mock2 };
        assertEquals(0, model.addChildren(expectedChildren).length);
        verify(mock1).setParent(model);
        verify(mock2).setParent(model);
        verify(nodeChangeListenerMock).childAdded(mock1);
        verify(nodeChangeListenerMock).childAdded(mock2);

        Model[] actualChildren = model.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(expectedChildren, actualChildren);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addChildrenWhenNull() {
        model.addChildren((Model[]) null);
    }

    @Test
    public void addChildrenWhenElementIsNull() {
        Model mock1 = mock(Model.class);
        Model mock2 = mock(Model.class);

        // It should safely skip nulls and continue. Skipped nodes[] should
        // remain empty since it should only return valid nodes.
        assertEquals(0,
                model.addChildren(new Model[] { mock1, null, mock2 }).length);
        verify(mock1).setParent(model);
        verify(mock2).setParent(model);
        verify(nodeChangeListenerMock).childAdded(mock1);
        verify(nodeChangeListenerMock).childAdded(mock2);
    }

    @Test
    public void addChildrenWhenAlreadyAdded() {
        Model mock1 = mock(Model.class);
        model.addChild(mock1);
        verify(mock1).setParent(model);

        Model mock2 = mock(Model.class);

        Model[] skipped = model.addChildren(new Model[] { mock1, mock2 });
        assertEquals(1, skipped.length);
        assertEquals(mock1, skipped[0]);
        verify(mock2).setParent(model);
        verify(nodeChangeListenerMock).childAdded(mock2);
    }

    @Test
    public void contains() {
        Model mock1 = mock(Model.class);
        model.addChild(mock1);

        Model mock2 = mock(Model.class);
        model.addChild(mock2);

        Model mock3 = mock(Model.class);

        assertTrue(model.contains(mock1));
        assertTrue(model.contains(mock2));
        assertFalse(model.contains(mock3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsWhenNull() {
        model.contains(null);
    }

    @Test
    public void removeAll() {
        Model mock1 = mock(Model.class);
        model.addChild(mock1);

        Model mock2 = mock(Model.class);
        model.addChild(mock2);

        model.removeAll();

        assertFalse(model.contains(mock1));
        assertFalse(model.contains(mock2));
        verify(mock1).setParent(null);
        verify(mock2).setParent(null);
        verify(nodeChangeListenerMock).childRemoved(mock1);
        verify(nodeChangeListenerMock).childRemoved(mock2);
    }

    @Test
    public void removeChild() {
        Model mock1 = mock(Model.class);
        model.addChild(mock1);

        Model mock2 = mock(Model.class);
        model.addChild(mock2);

        Model mock3 = mock(Model.class);

        assertTrue(model.removeChild(mock1));
        assertTrue(model.removeChild(mock2));
        assertFalse(model.removeChild(mock3));
        assertFalse(model.contains(mock1));
        assertFalse(model.contains(mock2));
        assertFalse(model.contains(mock3));
        verify(mock1).setParent(null);
        verify(mock2).setParent(null);
        verify(mock3, times(0)).setParent(null);
        verify(nodeChangeListenerMock).childRemoved(mock1);
        verify(nodeChangeListenerMock).childRemoved(mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeChildWhenNull() {
        model.removeChild((Model) null);
    }

    @Test
    public void isRoot() {
        assertTrue(model.isRoot());
    }

    @Test
    public void isLeaf() {
        // It's a leaf because it doesn't have any children yet.
        assertTrue(model.isLeaf());

        // Now it's a branch.
        model.addChild(mock(Model.class));

        assertFalse(model.isLeaf());
    }

    @Test
    public void isBranch() {
        // No children means not a branch.
        assertFalse(model.isBranch());

        // Now it's a branch.
        model.addChild(mock(Model.class));

        assertTrue(model.isBranch());
    }

    @Test
    public void setAndGetParent() {
        Model mock = mock(Model.class);

        model.setParent(mock);

        assertEquals(mock, model.getParent());
        verify(nodeChangeListenerMock).parentChanged(null, mock);
    }

    @Test
    public void setParentToNull() {
        model.setParent(null);
        assertEquals(null, model.getParent());
    }

}
