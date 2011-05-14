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
package spritey.ui.actions;

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Action for selecting all items in a tree.
 */
public class SelectAllTreeAction extends SelectAllAction {

    /**
     * Creates a new instance of SelectAllTreeAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public SelectAllTreeAction(TreeViewer viewer) {
        super(viewer);
    }

    private void collect(TreeItem[] items, List<Object> list) {
        for (int i = 0; i < items.length; ++i) {
            TreeItem item = items[i];
            list.add(item.getData());

            if (item.getExpanded()) {
                collect(item.getItems(), list);
            }
        }
    }

    @Override
    protected void collectItems(Control control, List<Object> list) {
        collect(((Tree) control).getItems(), list);
    }

}
