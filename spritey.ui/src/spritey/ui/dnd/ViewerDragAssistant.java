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
package spritey.ui.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Drag support for a viewer.
 */
public class ViewerDragAssistant extends DragSourceAdapter {

    private Viewer viewer;

    /**
     * Creates a new instance of ViewerDragAssistant.
     * 
     * @param viewer
     *        the viewer.
     */
    public ViewerDragAssistant(Viewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Returns the viewer.
     * 
     * @return the viewer.
     */
    protected Viewer getViewer() {
        return viewer;
    }

    @Override
    public void dragStart(DragSourceEvent event) {
        LocalSelectionTransfer.getTransfer().setSelection(
                getViewer().getSelection());
    }

    @Override
    public void dragFinished(DragSourceEvent event) {
        getViewer().refresh();
    }

}
