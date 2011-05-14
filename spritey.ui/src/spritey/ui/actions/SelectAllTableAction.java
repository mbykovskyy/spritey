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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Action for selecting all items in a table.
 */
public class SelectAllTableAction extends SelectAllAction {

    /**
     * Creates a new instance of SelectAllTableAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public SelectAllTableAction(TableViewer viewer) {
        super(viewer);
    }

    @Override
    protected void collectItems(Control control, List<Object> list) {
        TableItem[] items = ((Table) control).getItems();
        for (int i = 0; i < items.length; ++i) {
            list.add(items[i].getData());
        }
    }

}
