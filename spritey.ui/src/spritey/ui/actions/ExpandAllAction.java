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

import static spritey.ui.Application.EXPAND_ALL_IMG_ID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

import spritey.ui.Application;
import spritey.ui.Messages;

/**
 * Action for expanding group tree.
 */
public class ExpandAllAction extends ViewerAction {

    public ExpandAllAction(TreeViewer viewer) {
        super(viewer);
        Image image = Application.getImageRegistry().get(EXPAND_ALL_IMG_ID);
        setImageDescriptor(ImageDescriptor.createFromImage(image));
        setToolTipText(Messages.ADD_SPRITES_EXPAND_ALL);
        setText(Messages.ADD_SPRITES_EXPAND_ALL);
    }

    @Override
    public void run() {
        ((TreeViewer) getViewer()).expandAll();
    }

}
