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

import static spritey.ui.Application.ADD_IMG_ID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import spritey.core.Node;
import spritey.ui.Application;
import spritey.ui.Messages;
import spritey.ui.dialogs.ImageFileDialog;
import spritey.ui.pages.WizardPageEx;

/**
 * Action for loading sprites from a file system and adding them to a sheet.
 */
public class AddSpritesAction extends LoadSpritesAction {

    /**
     * Creates a new instance of AddFolderAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     * @param page
     *        the wizard page that this action affects.
     */
    public AddSpritesAction(Viewer viewer, WizardPageEx page) {
        super(viewer, page);
        Image image = Application.getImageRegistry().get(ADD_IMG_ID);
        setImageDescriptor(ImageDescriptor.createFromImage(image));
        setToolTipText(Messages.ADD_SPRITES_ADD_SPRITES);
        setText(Messages.ADD_SPRITES_ADD_SPRITES);
        setAccelerator(SWT.MOD1 | 'S');
        setEnabled(false);
    }

    @Override
    public void run() {
        ImageFileDialog dialog = new ImageFileDialog(getShell(), SWT.OPEN
                | SWT.MULTI);

        if (null != dialog.open()) {
            Node[] sprites = loadSprites(dialog.getFilePaths());
            Node parent = (Node) getSelection().getFirstElement();

            parent.addChildren(sprites);
            refreshAndSelect(parent);
            getPage().validatePage();
        }
    }

}
