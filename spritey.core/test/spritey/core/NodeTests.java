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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests the implementation of Node.
 */
public class NodeTests {

    @Spy
    Node node = new Node("TEST_NODE");

    @Mock
    Node parent;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }

    private String generateString(int length) {
        String text = "";

        for (int i = 0; i < length; ++i) {
            text += "a";
        }
        return text;
    }

    @Test
    public void constructor() {
        assertEquals("TEST_NODE", node.getName());
    }

    @Test
    public void setAndGetName() {
        node.setName("NEW_NAME");
        assertEquals("NEW_NAME", node.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameToNull() {
        node.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameTooShort() {
        node.setName(generateString(Node.MIN_NAME_LENGTH - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameTooLong() {
        node.setName(generateString(Node.MAX_NAME_LENGTH + 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameToSiblingName() {
        when(parent.contains("NEW_NAME")).thenReturn(true);
        when(node.getParent()).thenReturn(parent);

        node.setName("NEW_NAME");
    }

    @Test
    public void setAndGetParent() {
        Node mock = mock(Node.class);
        node.setParent(mock);
        assertEquals(mock, node.getParent());
    }

    @Test
    public void setParentToNull() {
        node.setParent(null);
        assertEquals(null, node.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setParentToItself() {
        node.setParent(node);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setChildAsParent() {
        when(node.contains(parent)).thenReturn(true);
        node.setParent(parent);
    }

    @Test
    public void addAndGetChildren() {
        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        when(node.contains("CHILD1")).thenReturn(false);
        when(node.contains("CHILD2")).thenReturn(false);

        Node[] expectedChildren = new Node[] { child1, child2 };
        assertEquals(0, node.addChildren(expectedChildren).length);

        verify(child1).setParent(node);
        verify(child2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(expectedChildren, actualChildren);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addChildrenWhenNull() {
        node.addChildren((Node[]) null);
    }

    @Test
    public void addChildrenWhenElementIsNull() {
        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        when(node.contains("CHILD1")).thenReturn(false);
        when(node.contains("CHILD2")).thenReturn(false);

        Node[] expectedChildren = new Node[] { child1, null, child2 };
        assertEquals(0, node.addChildren(expectedChildren).length);

        verify(child1).setParent(node);
        verify(child2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(new Node[] { child1, child2 }, actualChildren);
    }

    @Test
    public void addChildrenWhenElementIsParent() {
        when(node.getParent()).thenReturn(parent);

        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        when(node.contains("CHILD1")).thenReturn(false);
        when(node.contains("CHILD2")).thenReturn(false);

        Node[] expectedChildren = new Node[] { child1, parent, child2 };
        Node[] skipped = node.addChildren(expectedChildren);
        assertEquals(1, skipped.length);
        assertArrayEquals(new Node[] { parent }, skipped);

        verify(child1).setParent(node);
        verify(child2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(new Node[] { child1, child2 }, actualChildren);
    }

    @Test
    public void addChildrenWhenElementIsNodeItself() {
        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        when(node.contains("CHILD1")).thenReturn(false);
        when(node.contains("CHILD2")).thenReturn(false);

        Node[] expectedChildren = new Node[] { child1, node, child2 };
        Node[] skipped = node.addChildren(expectedChildren);
        assertEquals(1, skipped.length);
        assertArrayEquals(new Node[] { node }, skipped);

        verify(child1).setParent(node);
        verify(child2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(2, actualChildren.length);
        assertArrayEquals(new Node[] { child1, child2 }, actualChildren);
    }

    @Test
    public void addChildrenWhenAlreadyAdded() {
        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        when(node.contains("CHILD1")).thenReturn(true);
        when(node.contains("CHILD2")).thenReturn(false);

        Node[] expectedChildren = new Node[] { child1, child2 };
        Node[] skipped = node.addChildren(expectedChildren);
        assertEquals(1, skipped.length);
        assertArrayEquals(new Node[] { child1 }, skipped);

        verify(child1, never()).setParent(node);
        verify(child2).setParent(node);

        Node[] actualChildren = node.getChildren();
        assertEquals(1, actualChildren.length);
        assertArrayEquals(new Node[] { child2 }, actualChildren);
    }

    @Test
    public void containsNode() {
        Node child1 = mock(Node.class);

        List<Node> children = new ArrayList<Node>();
        children.add(child1);

        when(node.getChildrenList()).thenReturn(children);

        assertTrue(node.contains(child1));
        assertFalse(node.contains((Node) null));
        assertFalse(node.contains(mock(Node.class)));
    }

    @Test
    public void containsNodeWithName() {
        Node child1 = mock(Node.class);
        when(child1.getName()).thenReturn("CHILD1");

        Node child2 = mock(Node.class);
        when(child2.getName()).thenReturn("CHILD2");

        List<Node> children = new ArrayList<Node>();
        children.add(child1);

        when(node.getChildrenList()).thenReturn(children);

        assertTrue(node.contains(child1));
        assertFalse(node.contains((Node) null));
        assertFalse(node.contains(child2));
    }

    @Test
    public void isRoot() {
        assertTrue(node.isRoot());

        when(node.getParent()).thenReturn(parent);
        assertFalse(node.isRoot());
    }

    @Test
    public void isLeaf() {
        assertTrue(node.isLeaf());

        List<Node> children = new ArrayList<Node>();
        children.add(mock(Node.class));
        when(node.getChildrenList()).thenReturn(children);

        assertFalse(node.isLeaf());
    }

    @Test
    public void isBranch() {
        assertFalse(node.isBranch());

        List<Node> children = new ArrayList<Node>();
        children.add(mock(Node.class));
        when(node.getChildrenList()).thenReturn(children);

        assertTrue(node.isBranch());
    }

    @Test
    public void removeAll() {
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        when(node.getChildren()).thenReturn(new Node[] { child1, child2 });

        List<Node> children = new ArrayList<Node>();
        children.add(child1);
        children.add(child2);
        when(node.getChildrenList()).thenReturn(children);

        node.removeAll();

        assertEquals(0, children.size());
        verify(child1).setParent(null);
        verify(child2).setParent(null);
    }

    @Test
    public void removeChild() {
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        Node child3 = mock(Node.class);

        List<Node> children = new ArrayList<Node>();
        children.add(child1);
        children.add(child2);
        when(node.getChildrenList()).thenReturn(children);

        assertTrue(node.removeChild(child1));
        assertTrue(node.removeChild(child2));
        assertFalse(node.removeChild(child3));
        assertFalse(node.contains(child1));
        assertFalse(node.contains(child2));
        assertFalse(node.contains(child3));
        verify(child1).setParent(null);
        verify(child2).setParent(null);
        verify(child3, never()).setParent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeChildWhenNull() {
        node.removeChild((Node) null);
    }

}
