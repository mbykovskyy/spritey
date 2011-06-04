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
package spritey.ui.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import spritey.core.Node;
import spritey.core.filter.Filter;

/**
 * Content provider for Node tree.
 */
public class NodeTreeContentProvider implements ITreeContentProvider {

    private Filter filter;

    /**
     * Creates a new instance of NodeTreeContentProvider with no filter.
     */
    public NodeTreeContentProvider() {
        // Do nothing.
    }

    /**
     * Creates a new instance of NodeTreeContentProvider with given filter.
     * 
     * @param filter
     *        the filter to apply to children.
     */
    public NodeTreeContentProvider(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void dispose() {
        // Do nothing.
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // Do nothing.
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parent) {
        if (parent instanceof Node) {
            Node[] children = ((Node) parent).getChildren();
            if (null == filter) {
                return children;
            }
            return filter.filter(children);
        }
        return new Object[0];
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof Node) {
            return ((Node) element).getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return 0 < getChildren(element).length;
    }

}
