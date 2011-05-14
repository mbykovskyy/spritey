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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Shell;

/**
 * Action for acting on the specified viewer.
 */
public abstract class ViewerAction extends Action implements KeyListener {

    private Viewer viewer;

    /**
     * Creates an new instance of ViewerAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public ViewerAction(Viewer viewer) {
        this.viewer = viewer;
        this.viewer.getControl().addKeyListener(this);
    }

    /**
     * Returns the viewer on which action is performed.
     * 
     * @return the viewer.
     */
    protected Viewer getViewer() {
        return viewer;
    }

    /**
     * Returns the shell this action is contained in.
     * 
     * @return the shell this action is contained in
     */
    protected Shell getShell() {
        return getViewer().getControl().getShell();
    }

    /**
     * Refreshes the viewer and sets specified selection.
     * 
     * @param selection
     *        the selection to set.
     */
    protected void refreshAndSelect(Object... selection) {
        getViewer().refresh();
        getViewer().setSelection(new StructuredSelection(selection), true);
    }

    /**
     * Generates accelerator from the specified key event.
     * 
     * @param e
     *        the key event to generate accelerator.
     * @return the accelerator.
     */
    private int getAccelerator(KeyEvent e) {
        return e.stateMask | Character.toUpperCase(e.keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isEnabled() && (0 != getAccelerator())
                && (getAccelerator() == getAccelerator(e))) {
            run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing.
    }

}
