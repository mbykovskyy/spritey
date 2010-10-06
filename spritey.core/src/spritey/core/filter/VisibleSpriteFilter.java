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
package spritey.core.filter;

import java.awt.Rectangle;

import spritey.core.Sprite;
import spritey.core.node.Node;

/**
 * Filter for extracting visible sprites.
 */
public class VisibleSpriteFilter extends SpriteFilter {

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.AbstractFilter#select(spritey.core.node.Node)
     */
    @Override
    public boolean select(Node node) {
        if (super.select(node)) {
            Rectangle bounds = (Rectangle) node.getModel().getProperty(
                    Sprite.BOUNDS);

            return (bounds.x >= 0) && (bounds.y >= 0);
        }
        return false;
    }
}
