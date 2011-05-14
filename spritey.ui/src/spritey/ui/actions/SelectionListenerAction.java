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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * Action that listens to selection change events in the specified viewer.
 */
public abstract class SelectionListenerAction extends ViewerAction implements
        ISelectionChangedListener {

    private IStructuredSelection selection;

    /**
     * Creates an new instance of SelectionListenerAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public SelectionListenerAction(Viewer viewer) {
        super(viewer);
        viewer.addSelectionChangedListener(this);
        selection = StructuredSelection.EMPTY;
    }

    /**
     * Returns current selection in the viewer.
     * 
     * @return current structured selection.
     */
    protected IStructuredSelection getSelection() {
        return selection;
    }

    /**
     * Clients should implement this method to provide their logic for handling
     * selection changes.
     * 
     * @param selection
     *        the new selection.
     */
    protected abstract void selectionChanged(IStructuredSelection selection);

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        if (selection instanceof IStructuredSelection) {
            this.selection = (IStructuredSelection) selection;
            selectionChanged(this.selection);
        } else {
            throw new RuntimeException("Expected structured selection.");
        }
    }

}
