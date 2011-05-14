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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import spritey.core.Node;
import spritey.core.Sprite;
import spritey.core.filter.AbstractFilter;
import spritey.ui.Messages;
import spritey.ui.pages.WizardPageEx;

/**
 * Action for deleting selected sprites.
 */
public class DeleteSpritesAction extends DeleteAction {

    /**
     * Creates an new instance of DeleteSpritesAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     * @param page
     *        the wizard page that this action affects.
     */
    public DeleteSpritesAction(Viewer viewer, WizardPageEx page) {
        super(viewer, page);
        setFilter(new AbstractFilter() {
            @Override
            public boolean select(Node node) {
                return node instanceof Sprite;
            }
        });
    }

    @Override
    protected boolean shouldDelete() {
        IStructuredSelection selection = getSelection();

        if (!(selection.getFirstElement() instanceof Sprite)) {
            throw new RuntimeException("Expected selection of sprites.");
        }

        String title = selection.size() > 1 ? Messages.ADD_SPRITES_DELETE_SPRITES_TITLE
                : Messages.ADD_SPRITES_DELETE_SPRITE_TITLE;
        String message = selection.size() > 1 ? Messages.ADD_SPRITES_DELETE_SELECTED_SPRITES_QUESTION
                : Messages.ADD_SPRITES_DELETE_SELECTED_SPRITE_QUESTION;

        return MessageDialog.openQuestion(getShell(), title, message);
    }

    @Override
    protected void selectionChanged(IStructuredSelection selection) {
        setEnabled(0 < selection.toArray().length);
    }

}
