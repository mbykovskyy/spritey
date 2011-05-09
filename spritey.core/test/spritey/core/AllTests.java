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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import spritey.core.packer.FirstFitStrategyTests;
import spritey.core.packer.PackerTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ NodeTests.class, SheetTests.class, GroupTests.class,
        SpriteTests.class, FirstFitStrategyTests.class, PackerTests.class })
public class AllTests {
}
