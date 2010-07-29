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
package spritey.core.packer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import spritey.core.Sprite;
import spritey.core.node.Node;

/**
 * Tests the implementation of Packer.
 */
public class PackerTests {

    Strategy strategyMock;
    Packer packer;

    @Before
    public void initialize() {
        strategyMock = mock(Strategy.class);
        doThrow(new IllegalArgumentException()).when(strategyMock).pack(null,
                false);

        packer = new Packer(strategyMock);
    }

    @Test
    public void construct() {
        assertEquals(strategyMock, packer.getStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWhenStrategyIsNull() {
        new Packer(null);
    }

    @Test
    public void setStrategy() {
        Strategy newStragetyMock = mock(Strategy.class);

        packer.setStrategy(newStragetyMock);

        assertEquals(newStragetyMock, packer.getStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullStrategy() {
        packer.setStrategy(null);
    }

    @Test
    public void pack() throws Exception {
        Node nodeMock = mock(Node.class);
        doReturn(new Node[0]).when(nodeMock).getLeaves();

        packer.pack(nodeMock);

        verify(strategyMock).pack(new Sprite[0], false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void packWhenNodeIsNull() throws Exception {
        packer.pack(null);
    }
}
