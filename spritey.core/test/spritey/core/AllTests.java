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
package spritey.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import spritey.core.internal.SimpleGroupTests;
import spritey.core.internal.SimpleSheetTests;
import spritey.core.internal.SimpleSpriteTests;
import spritey.core.node.internal.MapBasedNodeTests;
import spritey.core.packer.FirstFitStrategyTests;
import spritey.core.packer.PackerTests;
import spritey.core.validator.NotNullValidatorTests;
import spritey.core.validator.StringLengthValidatorTests;
import spritey.core.validator.TypeValidatorTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SimpleSpriteTests.class, SimpleGroupTests.class,
        SimpleSheetTests.class, MapBasedNodeTests.class,
        NotNullValidatorTests.class, TypeValidatorTests.class,
        StringLengthValidatorTests.class, PackerTests.class,
        FirstFitStrategyTests.class })
public class AllTests {
}
