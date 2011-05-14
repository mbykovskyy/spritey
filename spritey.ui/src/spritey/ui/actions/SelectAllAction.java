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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import spritey.ui.Messages;

/**
 * Action for selecting all items in a viewer.
 */
public abstract class SelectAllAction extends ViewerAction {

    /**
     * Creates a new instance of SelectAllAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public SelectAllAction(Viewer viewer) {
        super(viewer);
        setToolTipText(Messages.ADD_SPRITES_SELECT_ALL);
        setText(Messages.ADD_SPRITES_SELECT_ALL);
        setAccelerator(SWT.MOD1 | 'A');
    }

    /**
     * Collects a list of items visible in the specified control.
     * 
     * @param control
     *        the control to collect items from.
     * @param list
     *        a list of visible items.
     */
    protected abstract void collectItems(Control control, List<Object> list);

    @Override
    public void run() {
        List<Object> list = new ArrayList<Object>();
        collectItems(getViewer().getControl(), list);
        getViewer().setSelection(new StructuredSelection(list));
    }
}
