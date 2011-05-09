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
package spritey.core.packer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * Tests the implementation of Packer.
 */
public class PackerTests {

    Packer packer;

    @Mock
    Strategy strategyMock;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        doThrow(new IllegalArgumentException()).when(strategyMock).pack(null,
                null, false);

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
    public void packWithoutCacheFlush() throws Exception {
        Sheet sheetMock = mock(Sheet.class);
        doReturn(new Node[0]).when(sheetMock).getChildren();

        packer.pack(sheetMock, false);

        verify(strategyMock).pack(sheetMock, new Sprite[0], false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void packWhenNodeIsNull() throws Exception {
        packer.pack(null, false);
    }
}
