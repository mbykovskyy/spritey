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

import static spritey.ui.Application.CREATE_GROUP_IMG_ID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import spritey.core.Node;
import spritey.ui.Application;
import spritey.ui.Messages;
import spritey.ui.dialogs.StaticWizardDialog;
import spritey.ui.wizards.NewGroupWizard;

/**
 * Action for creating a new group and adding it to a sheet.
 */
public class CreateGroupAction extends SelectionListenerAction {

    /**
     * Creates an new instance of CreateGroupAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     */
    public CreateGroupAction(Viewer viewer) {
        super(viewer);
        Image image = Application.getImageRegistry().get(CREATE_GROUP_IMG_ID);
        setImageDescriptor(ImageDescriptor.createFromImage(image));
        setToolTipText(Messages.ADD_SPRITES_CREATE_GROUP);
        setText(Messages.ADD_SPRITES_CREATE_GROUP);
        setAccelerator(SWT.MOD1 | 'G');
        setEnabled(false);
    }

    @Override
    public void run() {
        Node parent = (Node) getSelection().getFirstElement();
        NewGroupWizard wizard = new NewGroupWizard(parent);
        Window dialog = new StaticWizardDialog(getShell(), 390, 240, wizard);

        if (dialog.open() != Window.CANCEL) {
            Node group = wizard.getGroup();
            parent.addChildren(group);
            refreshAndSelect(group);
        }
    }

    @Override
    protected void selectionChanged(IStructuredSelection selection) {
        setEnabled(1 == selection.toArray().length);
    }

}
