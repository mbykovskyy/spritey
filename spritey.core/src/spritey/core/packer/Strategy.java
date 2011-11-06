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

/**
 * A strategy for packing sprites.
 */
public interface Strategy {

    /**
     * Arranges the specified list of sprites according to the rules implemented
     * by clients.
     * 
     * @param sheet
     *        a sprite sheet to pack.
     * @param constraints
     *        the set of constraints that have to be obeyed when packing sprite
     *        sheet.
     * @throws IllegalArgumentException
     *         when either <code>sheet</code> or <code>constraints</code> is
     *         null.
     * @throws SizeTooSmallException
     *         when sheet size is too small to fit all sprites.
     * 
     */
    public void pack(Sheet sheet, Constraints constraints)
            throws IllegalArgumentException, SizeTooSmallException;

}
