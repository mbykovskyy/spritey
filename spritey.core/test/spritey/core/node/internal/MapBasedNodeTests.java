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
package spritey.core.node.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Model;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;

/**
 * Tests the implementation of MapBasedNode.
 */
public class MapBasedNodeTests {

    final String NAME = "Root";

    Node node;

    @Mock
    NodeListener listenerMock;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        node = new MapBasedNode(NAME);
        node.addNodeListener(listenerMock);
    }

    @Test
    public void addAndGetChild() {
        Node mock = mock(Node.class);
        doReturn("child-node").when(mock).getName();

        assertTrue(node.addChild(mock));
        assertEquals(mock, node.getChild(mock.getName()));
        verify(mock).setParent(node);
        verify(listenerMock).childAdded(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addChildWhenNull() {
        node.addChild(null);
    }

    @Test
    public void addAndGetChildren() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();

        Node[] expectedChildren = new Node[] { mock1, mock2 };
        assertEquals(0, node.addChildren(expectedChildren).length);
        verify(mock1).setParent(node);
        verify(mock2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(expectedChildren, actualChildren);
        verify(listenerMock).childAdded(mock1);
        verify(listenerMock).childAdded(mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addChildrenWhenNull() {
        node.addChildren(null);
    }

    @Test
    public void addChildrenWhenElementIsNull() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();

        // It should safely skip nulls and continue. Skipped nodes[] should
        // remain empty since it should only return valid nodes.
        assertEquals(0,
                node.addChildren(new Node[] { mock1, null, mock2 }).length);
        verify(mock1).setParent(node);
        verify(mock2).setParent(node);
        verify(listenerMock).childAdded(mock1);
        verify(listenerMock).childAdded(mock2);
    }

    @Test
    public void addChildrenWhenAlreadyAdded() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();
        node.addChild(mock1);
        verify(mock1).setParent(node);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();

        Node[] skipped = node.addChildren(new Node[] { mock1, mock2 });
        assertEquals(1, skipped.length);
        assertEquals(mock1, skipped[0]);
        verify(mock2).setParent(node);
        verify(listenerMock).childAdded(mock2);
    }

    @Test
    public void addChildrenWhenNodeWithSimilarNameAlreadyAdded() {
        Node mock1 = mock(Node.class);
        doReturn("similar-name").when(mock1).getName();
        node.addChild(mock1);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();

        Node mock3 = mock(Node.class);
        doReturn("similar-name").when(mock3).getName();

        Node[] skipped = node.addChildren(new Node[] { mock2, mock3 });
        assertEquals(1, skipped.length);
        assertEquals(mock3, skipped[0]);
        verify(mock3, times(0)).setParent(node);
        verify(mock2).setParent(node);
        verify(listenerMock).childAdded(mock2);
    }

    @Test
    public void contains() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();
        node.addChild(mock1);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();
        node.addChild(mock2);

        Node mock3 = mock(Node.class);
        doReturn("child-node3").when(mock3).getName();

        assertTrue(node.contains(mock1));
        assertTrue(node.contains(mock2));
        assertFalse(node.contains(mock3));
        assertTrue(node.contains("child-node1"));
        assertTrue(node.contains("child-node2"));
        assertFalse(node.contains("child-node3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsWhenNull() {
        node.contains((Node) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsWhenNameIsNull() {
        node.contains((String) null);
    }

    @Test
    public void removeAll() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();
        node.addChild(mock1);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();
        node.addChild(mock2);

        node.removeAll();

        assertFalse(node.contains(mock1));
        assertFalse(node.contains(mock2));
        verify(mock1).setParent(null);
        verify(mock2).setParent(null);
        verify(listenerMock).childRemoved(mock1);
        verify(listenerMock).childRemoved(mock2);
    }

    @Test
    public void removeChild() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();
        node.addChild(mock1);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();
        node.addChild(mock2);

        Node mock3 = mock(Node.class);
        doReturn("child-node3").when(mock3).getName();

        assertTrue(node.removeChild(mock1));
        assertTrue(node.removeChild(mock2));
        assertFalse(node.removeChild(mock3));
        assertFalse(node.contains(mock1));
        assertFalse(node.contains(mock2));
        assertFalse(node.contains(mock3));
        verify(mock1).setParent(null);
        verify(mock2).setParent(null);
        verify(mock3, times(0)).setParent(null);
        verify(listenerMock).childRemoved(mock1);
        verify(listenerMock).childRemoved(mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeChildWhenNull() {
        node.removeChild((Node) null);
    }

    @Test
    public void removeChildByName() {
        Node mock1 = mock(Node.class);
        doReturn("child-node1").when(mock1).getName();
        node.addChild(mock1);

        Node mock2 = mock(Node.class);
        doReturn("child-node2").when(mock2).getName();
        node.addChild(mock2);

        Node mock3 = mock(Node.class);
        doReturn("child-node3").when(mock3).getName();

        assertTrue(node.removeChild("child-node1"));
        assertTrue(node.removeChild("child-node2"));
        assertFalse(node.removeChild("child-node3"));
        assertFalse(node.contains(mock1));
        assertFalse(node.contains(mock2));
        assertFalse(node.contains(mock3));
        verify(mock1).setParent(null);
        verify(mock2).setParent(null);
        verify(mock3, times(0)).setParent(null);
        verify(listenerMock).childRemoved(mock1);
        verify(listenerMock).childRemoved(mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeChildByNameWhenNull() {
        node.removeChild((String) null);
    }

    @Test
    public void setAndGetName() {
        node.setName("new-name");

        assertEquals("new-name", node.getName());
        verify(listenerMock).nameChanged(NAME, "new-name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameToNull() {
        node.setName(null);
    }

    @Test
    public void isRoot() {
        assertTrue(node.isRoot());
    }

    @Test
    public void isLeaf() {
        // It's a leaf because it doesn't have any children yet.
        assertTrue(node.isLeaf());

        Node mock = mock(Node.class);
        doReturn("node-name").when(mock).getName();

        // Now it's a branch.
        node.addChild(mock);
        assertFalse(node.isLeaf());
    }

    @Test
    public void isBranch() {
        // No children means not a branch.
        assertFalse(node.isBranch());

        Node mock = mock(Node.class);
        doReturn("node-name").when(mock).getName();

        // Now it's a branch.
        node.addChild(mock);
        assertTrue(node.isBranch());
    }

    @Test
    public void setAndGetParent() {
        Node mock = mock(Node.class);

        node.setParent(mock);

        assertEquals(mock, node.getParent());
        verify(listenerMock).parentChanged(null, mock);
    }

    @Test
    public void setParentToNull() {
        node.setParent(null);

        assertEquals(null, node.getParent());
    }

    @Test
    public void setAndGetModel() {
        Model mock = mock(Model.class);

        node.setModel(mock);

        assertEquals(mock, node.getModel());
        verify(listenerMock).modelChanged(null, mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setModelToNull() {
        node.setModel(null);
    }
}
