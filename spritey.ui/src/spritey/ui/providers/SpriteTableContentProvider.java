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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import spritey.core.Node;
import spritey.core.filter.Filter;
import spritey.core.filter.SpriteFilter;

/**
 * Content provider for sprite table.
 */
public class SpriteTableContentProvider implements IStructuredContentProvider {

    private Filter filter;

    public SpriteTableContentProvider() {
        filter = new SpriteFilter();
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
        if (inputElement instanceof Node) {
            return filter.filter(((Node) inputElement).getChildren());
        }
        return new Object[0];
    }

}
