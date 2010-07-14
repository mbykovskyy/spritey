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
package spritey.rcp.views.navigator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.eclipse.jface.viewers.Viewer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.event.ModelEvent;
import spritey.core.node.Node;

/**
 * Tests the implementation of SpriteTreeDataProvider.
 */
public class SpriteTreeDataProviderTests {

    SpriteTreeDataProvider provider;
    Node nodeMock;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        provider = new SpriteTreeDataProvider();
        nodeMock = mock(Node.class);
    }

    @Test
    public void getChildrenOfNode() {
        provider.getChildren(nodeMock);
        verify(nodeMock).getChildren();
    }

    @Test
    public void getChildrenOfNull() {
        assertArrayEquals(new Object[0], provider.getChildren(null));
    }

    @Test
    public void getChildrenOfObject() {
        assertArrayEquals(new Object[0], provider.getChildren(new Object()));
    }

    @Test
    public void getParentOfNode() {
        provider.getParent(nodeMock);
        verify(nodeMock).getParent();
    }

    @Test
    public void getParentOfNull() {
        assertNull(provider.getParent(null));
    }

    @Test
    public void getParentOfObject() {
        assertNull(provider.getParent(new Object()));
    }

    @Test
    public void hasChildrenWhenNode() {
        provider.hasChildren(nodeMock);
        verify(nodeMock).isBranch();
    }

    @Test
    public void hasChildrenWhenNull() {
        assertFalse(provider.hasChildren(null));
    }

    @Test
    public void hasChildrenWhenObject() {
        assertFalse(provider.hasChildren(new Object()));
    }

    @Test
    public void getElementsOfNode() {
        provider.getElements(nodeMock);
        verify(nodeMock).getChildren();
    }

    @Test
    public void getElementsOfNull() {
        assertArrayEquals(new Object[0], provider.getElements(null));
    }

    @Test
    public void getElementsOfObject() {
        assertArrayEquals(new Object[0], provider.getElements(new Object()));
    }

    @Test
    public void inputChangedToNode() {
        Viewer viewerMock = mock(Viewer.class);
        Node oldInputMock = mock(Node.class);
        Node newInputMock = mock(Node.class);

        provider.inputChanged(viewerMock, oldInputMock, newInputMock);

        verify(newInputMock).addNodeListener(provider);
    }

    @Test
    public void inputChangedToViewerNull() {
        Viewer viewerMock = null;
        Node oldInputMock = mock(Node.class);
        Node newInputMock = mock(Node.class);

        provider.inputChanged(viewerMock, oldInputMock, newInputMock);

        verify(newInputMock).addNodeListener(provider);
    }

    @Test
    public void inputChangedToOldInputNull() {
        Viewer viewerMock = mock(Viewer.class);
        Node oldInputMock = null;
        Node newInputMock = mock(Node.class);

        provider.inputChanged(viewerMock, oldInputMock, newInputMock);

        verify(newInputMock).addNodeListener(provider);
    }

    @Test
    public void inputChangedToNewInputNull() {
        Viewer viewerMock = mock(Viewer.class);
        Node oldInputMock = mock(Node.class);
        Node newInputMock = null;

        provider.inputChanged(viewerMock, oldInputMock, newInputMock);
    }

    @Test
    public void inputChangedWhenNewInputObject() {
        Viewer viewerMock = mock(Viewer.class);
        Node oldInputMock = null;

        provider.inputChanged(viewerMock, oldInputMock, new Object());
    }

    @Test
    public void getImageOfNode() {
        assertNull(provider.getImage(nodeMock));
    }

    @Test
    public void getImageOfNull() {
        assertNull(provider.getImage(null));
    }

    @Test
    public void getImageOfObject() {
        assertNull(provider.getImage(new Object()));
    }

    @Test
    public void getTextOfSpriteNode() {
        final String NAME = "SpriteName";

        Sprite spriteMock = mock(Sprite.class);
        doReturn(NAME).when(spriteMock).getProperty(Sprite.NAME);
        doReturn(spriteMock).when(nodeMock).getModel();

        assertEquals(NAME, provider.getText(nodeMock));
        verify(nodeMock).getModel();
        verify(spriteMock).getProperty(Sprite.NAME);
    }

    @Test
    public void getTextOfGroupNode() {
        final String NAME = "GroupName";

        Group groupMock = mock(Group.class);
        doReturn(NAME).when(groupMock).getProperty(Group.NAME);
        doReturn(groupMock).when(nodeMock).getModel();

        assertEquals(NAME, provider.getText(nodeMock));
        verify(nodeMock).getModel();
        verify(groupMock).getProperty(Group.NAME);
    }

    @Test
    public void getTextOfNull() {
        assertNull(provider.getText(null));
    }

    @Test
    public void getTextOfObject() {
        assertNull(provider.getText(new Object()));
    }

    @Test
    public void getTextOfSheet() {
        Sheet sheetMock = mock(Sheet.class);
        assertNull(provider.getText(sheetMock));
    }

    @Test
    public void getTextOfNodeWithNoModel() {
        doReturn(null).when(nodeMock).getModel();

        assertNull(provider.getText(nodeMock));
        verify(nodeMock).getModel();
    }

    @Test
    public void propertyChanged() {
        ModelEvent eventMock = mock(ModelEvent.class);
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.propertyChanged(eventMock);

        verify(viewerMock).refresh();
    }

    @Test
    public void nameChanged() {
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.nameChanged("OldName", "NewName");

        verify(viewerMock, times(0)).refresh();
    }

    @Test
    public void parentChnaged() {
        Node oldParentMock = mock(Node.class);
        Node newParentMock = mock(Node.class);
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.parentChanged(oldParentMock, newParentMock);

        verify(viewerMock).refresh();
    }

    @Test
    public void childAdded() {
        // Provider should add register itself with all node's children
        Node childNodeMock = mock(Node.class);
        doReturn(new Node[0]).when(childNodeMock).getChildren();
        doReturn(new Node[] { childNodeMock }).when(nodeMock).getChildren();

        // Provider should register itself with a model
        Model modelMock = mock(Model.class);
        doReturn(modelMock).when(nodeMock).getModel();

        // A viewer has to be refreshed
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.childAdded(nodeMock);

        verify(nodeMock).addNodeListener(provider);
        verify(modelMock).addModelListener(provider);
        verify(childNodeMock).addNodeListener(provider);
        verify(viewerMock).refresh();
    }

    @Test
    public void childRemoved() {
        // Provider should add unregister itself from all node's children
        Node childNodeMock = mock(Node.class);
        doReturn(new Node[0]).when(childNodeMock).getChildren();
        doReturn(new Node[] { childNodeMock }).when(nodeMock).getChildren();

        // Provider should unregister itself from a model
        Model modelMock = mock(Model.class);
        doReturn(modelMock).when(nodeMock).getModel();

        // A viewer has to be refreshed
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.childRemoved(nodeMock);

        verify(nodeMock).removeNodeListener(provider);
        verify(modelMock).removeModelListener(provider);
        verify(childNodeMock).removeNodeListener(provider);
        verify(viewerMock).refresh();
    }

    @Test
    public void childrenRemoved() {
        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.childrenRemoved();

        verify(viewerMock).refresh();
    }

    @Test
    public void modelChanged() {
        Model modelMock = mock(Model.class);

        Viewer viewerMock = mock(Viewer.class);
        provider.inputChanged(viewerMock, null, null);

        provider.modelChanged(null, modelMock);

        verify(viewerMock).refresh();
    }
}
