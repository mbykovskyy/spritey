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

import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.node.Node;

/**
 * A sprite sheet organizer for packing sprites according to the selected
 * strategy.
 */
public class Packer {

    private Strategy strategy;

    /**
     * Constructor.
     * 
     * @param defaultStrategy
     *        the default strategy.
     * @throws IllegalArgumentException
     *         when the <code>defaultStrategy</code> is null.
     */
    public Packer(Strategy defaultStrategy) throws IllegalArgumentException {
        validateArgument(defaultStrategy);
        strategy = defaultStrategy;
    }

    private void validateArgument(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException("Argument is null.");
        }
    }

    /**
     * Changes packer strategy to the specified one.
     * 
     * @param strategy
     *        the strategy to use for organizing sprites.
     * @throws IllegalArgumentException
     *         when the <code>strategy</code> is null.
     */
    public void setStrategy(Strategy strategy) throws IllegalArgumentException {
        validateArgument(strategy);
        this.strategy = strategy;
    }

    /**
     * Returns active strategy.
     * 
     * @return active strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Arranges the specified tree according to the active strategy.
     * 
     * @param sheetNode
     *        the root of a tree. It has to be a sheet node.
     * @param flushCache
     *        specifies whether cached values should be flushed.
     * @throws IllegalArgumentException
     *         when the root node does not have sheet model attached to it.
     * @throws IllegalArgumentException
     *         when <code>sheetNode</code> is null.
     * 
     */
    public void pack(Node sheetNode, boolean flushCache)
            throws IllegalArgumentException {
        validateArgument(sheetNode);

        Node[] leaves = sheetNode.getLeaves();
        Sprite[] sprites = new Sprite[leaves.length];

        // TODO Move this into Node#getLeavesModels()
        for (int i = 0; i < leaves.length; ++i) {
            sprites[i] = (Sprite) leaves[i].getModel();
        }

        strategy.pack((Sheet) sheetNode.getModel(), sprites, flushCache);
    }

}
