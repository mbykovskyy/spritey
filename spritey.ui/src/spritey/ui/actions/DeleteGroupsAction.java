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

import spritey.core.Group;
import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.filter.AbstractFilter;
import spritey.ui.InternalError;
import spritey.ui.Messages;
import spritey.ui.pages.WizardPageEx;

/**
 * Action for deleting selected groups.
 */
public class DeleteGroupsAction extends DeleteAction {

    /**
     * Creates an new instance of DeleteGroupsAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     * @param page
     *        the wizard page that this action affects.
     */
    public DeleteGroupsAction(Viewer viewer, WizardPageEx page) {
        super(viewer, page);
        setFilter(new AbstractFilter() {
            @Override
            public boolean select(Node node) {
                return node instanceof Group;
            }
        });
    }

    @Override
    protected boolean shouldDelete() {
        IStructuredSelection selection = getSelection();

        if (!(selection.getFirstElement() instanceof Group)) {
            throw new InternalError("Expected selection of groups.");
        }

        String title = selection.size() > 1 ? Messages.ADD_SPRITES_DELETE_GROUPS_TITLE
                : Messages.ADD_SPRITES_DELETE_GROUP_TITLE;
        String message = selection.size() > 1 ? Messages.ADD_SPRITES_DELETE_SELECTED_GROUPS_QUESTION
                : Messages.ADD_SPRITES_DELETE_SELECTED_GROUP_QUESTION;

        return MessageDialog.openQuestion(getShell(), title, message);
    }

    @Override
    protected void selectionChanged(IStructuredSelection selection) {
        Object[] list = selection.toArray();

        boolean enabled = 0 < list.length;

        for (Object o : list) {
            if (o instanceof Sheet) {
                enabled = false;
                break;
            }
        }
        setEnabled(enabled);
    }

}
