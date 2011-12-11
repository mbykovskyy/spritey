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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import spritey.core.Node;
import spritey.ui.InternalError;
import spritey.ui.operations.LoadSpritesOperation;
import spritey.ui.pages.WizardPageEx;

/**
 * Action for loading sprites from a file system.
 */
public abstract class LoadSpritesAction extends SelectionListenerAction {

    private WizardPageEx page;

    /**
     * Creates a new instance of AddFolderAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     * @param page
     *        the wizard page that this action affects.
     */
    public LoadSpritesAction(Viewer viewer, WizardPageEx page) {
        super(viewer);
        this.page = page;
    }

    /**
     * Returns the wizard page that this action affects.
     * 
     * @return the wizard page.
     */
    protected WizardPageEx getPage() {
        return page;
    }

    /**
     * Loads sprites from the file system.
     * 
     * @param paths
     *        a list of paths to sprites.
     * @return a list of loaded sprites.
     */
    protected Node[] loadSprites(String... paths) {
        if ((null != paths) && (0 < paths.length)) {
            try {
                LoadSpritesOperation operation = new LoadSpritesOperation(paths);
                getPage().getContainer().run(true, true, operation);

                return operation.getRoot().getChildren();
            } catch (InterruptedException e) {
                // Do nothing.
            } catch (InvocationTargetException e) {
                throw new InternalError(
                        "Error occurred during load sprites operation.", e);
            }
        }
        return new Node[0];
    }

    @Override
    public void selectionChanged(IStructuredSelection selection) {
        setEnabled(1 == selection.toArray().length);
    }

}
