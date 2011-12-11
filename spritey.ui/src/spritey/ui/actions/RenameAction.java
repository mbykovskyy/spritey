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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;

import spritey.core.Node;
import spritey.core.Sheet;
import spritey.ui.Messages;
import spritey.ui.dialogs.StaticWizardDialog;
import spritey.ui.wizards.RenameWizard;

/**
 * Action for renaming nodes in a sprite tree.
 */
public class RenameAction extends SelectionListenerAction {

    /**
     * Creates an new instance of RenameAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public RenameAction(Viewer viewer) {
        super(viewer);
        setToolTipText(Messages.ADD_SPRITES_RENAME);
        setText(Messages.ADD_SPRITES_RENAME);
        setAccelerator(SWT.F2);
        setEnabled(false);
    }

    @Override
    public void run() {
        Node node = (Node) getSelection().getFirstElement();
        RenameWizard wizard = new RenameWizard(node);
        StaticWizardDialog dialog = new StaticWizardDialog(getShell(), 380, 40,
                wizard);

        if (dialog.open() != Window.CANCEL) {
            refreshAndSelect(node);
        }
    }

    @Override
    protected void selectionChanged(IStructuredSelection selection) {
        setEnabled((1 == selection.size())
                && !(selection.getFirstElement() instanceof Sheet));
    }
}
