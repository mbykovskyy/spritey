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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import spritey.core.Group;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.node.Node;

/**
 * Tests the implementation of SpriteTreeContentProvider.
 */
public class SpriteTreeLabelProviderTests {

    SpriteTreeLabelProvider labelProvider;
    Node nodeMock;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        labelProvider = new SpriteTreeLabelProvider();
        nodeMock = mock(Node.class);
    }

    @Test
    public void getImageOfNode() {
        assertNull(labelProvider.getImage(nodeMock));
    }

    @Test
    public void getImageOfNull() {
        assertNull(labelProvider.getImage(null));
    }

    @Test
    public void getImageOfObject() {
        assertNull(labelProvider.getImage(new Object()));
    }

    @Test
    public void getTextOfSpriteNode() {
        final String NAME = "SpriteName";

        Sprite spriteMock = mock(Sprite.class);
        doReturn(NAME).when(spriteMock).getProperty(Sprite.NAME);
        doReturn(spriteMock).when(nodeMock).getModel();

        assertEquals(NAME, labelProvider.getText(nodeMock));
        verify(nodeMock).getModel();
        verify(spriteMock).getProperty(Sprite.NAME);
    }

    @Test
    public void getTextOfGroupNode() {
        final String NAME = "GroupName";

        Group groupMock = mock(Group.class);
        doReturn(NAME).when(groupMock).getProperty(Group.NAME);
        doReturn(groupMock).when(nodeMock).getModel();

        assertEquals(NAME, labelProvider.getText(nodeMock));
        verify(nodeMock).getModel();
        verify(groupMock).getProperty(Group.NAME);
    }

    @Test
    public void getTextOfNull() {
        assertNull(labelProvider.getText(null));
    }

    @Test
    public void getTextOfObject() {
        assertNull(labelProvider.getText(new Object()));
    }

    @Test
    public void getTextOfSheet() {
        Sheet sheetMock = mock(Sheet.class);
        assertNull(labelProvider.getText(sheetMock));
    }

    @Test
    public void getTextOfNodeWithNoModel() {
        doReturn(null).when(nodeMock).getModel();

        assertNull(labelProvider.getText(nodeMock));
        verify(nodeMock).getModel();
    }

}
