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

import spritey.core.Model;
import spritey.core.node.Node;

/**
 * A filter is used to extract a subset of elements.
 */
public interface Filter {
    /**
     * Extracts a list of nodes from the specified tree.
     * 
     * @param rootNode
     *        the root of the tree.
     * @return a list of extracted nodes.
     */
    public Node[] filterNodes(Node rootNode);

    /**
     * Extracts a list of nodes from the specified list.
     * 
     * @param nodes
     *        a list of nodes to filter.
     * @return a list of extracted nodes.
     */
    public Node[] filterNodes(Node[] nodes);

    /**
     * Returns a list of models extracted from the specified tree.
     * 
     * @param rootNode
     *        the root of the tree.
     * @return a list of extracted models.
     */
    public Model[] filterModels(Node rootNode);

    /**
     * Returns a list of models extracted from the specified list.
     * 
     * @param nodes
     *        a list of nodes to filter.
     * @return a list of extracted models.
     */
    public Model[] filterModels(Node[] nodes);

    /**
     * Returns whether the given element makes it through this filter.
     * 
     * @param node
     *        the node to test.
     * @return <code>true</code> if element is included in the filtered set, and
     *         <code>false</code> if excluded.
     */
    public boolean select(Node node);
}
