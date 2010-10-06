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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spritey.core.Model;
import spritey.core.node.Node;

/**
 * Abstract implementation of Filter. Each method simply iterates through the
 * specified set and calls the <code>select</code> method on each element.
 * <p>
 * Subclasses must implement the <code>select</code> method.
 * </p>
 */
public abstract class AbstractFilter implements Filter {

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterNodes(spritey.core.node.Node)
     */
    @Override
    public Node[] filterNodes(Node rootNode) {
        List<Node> out = new ArrayList<Node>();
        if (select(rootNode)) {
            out.add(rootNode);
        }

        for (Node child : rootNode.getChildren()) {
            out.addAll(Arrays.asList(filterNodes(child)));
        }

        return out.toArray(new Node[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterNodes(spritey.core.node.Node[])
     */
    @Override
    public Node[] filterNodes(Node[] nodes) {
        List<Node> out = new ArrayList<Node>(nodes.length);
        for (Node e : nodes) {
            if (select(e)) {
                out.add(e);
            }
        }

        return out.toArray(new Node[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterModels(spritey.core.node.Node)
     */
    @Override
    public Model[] filterModels(Node rootNode) {
        List<Model> out = new ArrayList<Model>();
        if (select(rootNode)) {
            out.add(rootNode.getModel());
        }

        for (Node child : rootNode.getChildren()) {
            out.addAll(Arrays.asList(filterModels(child)));
        }

        return out.toArray(new Model[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#filterModels(spritey.core.node.Node[])
     */
    @Override
    public Model[] filterModels(Node[] nodes) {
        List<Model> out = new ArrayList<Model>(nodes.length);
        for (Node e : nodes) {
            if (select(e)) {
                out.add(e.getModel());
            }
        }

        return out.toArray(new Model[out.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.filter.Filter#select(spritey.core.node.Node)
     */
    @Override
    public abstract boolean select(Node node);

}
