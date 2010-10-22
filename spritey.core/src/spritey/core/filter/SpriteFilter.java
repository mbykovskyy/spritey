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
package spritey.core.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spritey.core.Sprite;
import spritey.core.node.Node;

/**
 * Filter for extracting sprites.
 */
public class SpriteFilter extends AbstractFilter {

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterModels(spritey.core.node.Node)
     */
    @Override
    public Sprite[] filterModels(Node rootNode) {
        List<Sprite> out = new ArrayList<Sprite>();
        if (select(rootNode)) {
            out.add((Sprite) rootNode.getModel());
        }

        for (Node child : rootNode.getChildren()) {
            out.addAll(Arrays.asList(filterModels(child)));
        }

        return out.toArray(new Sprite[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterModels(spritey.core.node.Node[])
     */
    @Override
    public Sprite[] filterModels(Node[] nodes) {
        List<Sprite> out = new ArrayList<Sprite>(nodes.length);
        for (Node e : nodes) {
            if (select(e)) {
                out.add((Sprite) e.getModel());
            }
        }

        return out.toArray(new Sprite[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.AbstractFilter#select(spritey.core.node.Node)
     */
    @Override
    public boolean select(Node node) {
        return (node.getModel() instanceof Sprite);
    }

}
