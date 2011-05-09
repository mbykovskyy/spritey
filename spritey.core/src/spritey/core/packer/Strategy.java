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
package spritey.core.packer;

import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * A strategy for packing sprites.
 */
public interface Strategy {

    /**
     * Arranges the specified list of sprites according to the rules implemented
     * by clients. Clients have to implement this method and set the active
     * strategy to their strategy if they want sprites to be organized the way
     * the want.
     * 
     * @param sheet
     *        a sprite sheet defining boundaries within which sprites should be
     *        packed.
     * @param sprites
     *        a list of sprites to pack into sprite sheet.
     * @param flushCache
     *        specifies whether cached values should be flushed.
     * @throws IllegalArgumentException
     *         when <code>sprites</code> list is null.
     * 
     */
    public void pack(Sheet sheet, Sprite[] sprites, boolean flushCache)
            throws IllegalArgumentException;

}
