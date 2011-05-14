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

import static spritey.ui.Application.DELETE_IMG_ID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import spritey.core.Node;
import spritey.core.filter.Filter;
import spritey.ui.Application;
import spritey.ui.Messages;
import spritey.ui.pages.WizardPageEx;

/**
 * Action for deleting selected items.
 */
public abstract class DeleteAction extends SelectionListenerAction {

    private WizardPageEx page;
    private Filter filter;

    /**
     * Creates an new instance of DeleteAction.
     * 
     * @param viewer
     *        the viewer on which action will be performed.
     * @param page
     *        the wizard page that this action affects.
     */
    public DeleteAction(Viewer viewer, WizardPageEx page) {
        super(viewer);
        Image image = Application.getImageRegistry().get(DELETE_IMG_ID);
        setImageDescriptor(ImageDescriptor.createFromImage(image));
        setToolTipText(Messages.ADD_SPRITES_DELETE);
        setText(Messages.ADD_SPRITES_DELETE);
        setAccelerator(SWT.DEL);
        setEnabled(false);

        this.page = page;
    }

    /**
     * Determines if the action can delete the select item(s) or not.
     * 
     * @return <code>true</code> if the selected item(s) can be deleted or not.
     */
    protected abstract boolean shouldDelete();

    /**
     * Sets filter to filter out nodes when calculating next selection target.
     * 
     * @param filter
     *        the filter to use for filtering out nodes.
     */
    protected void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Returns index of the specified node in the list of its siblings.
     * 
     * @param node
     *        the node to get index for.
     * @return the index of the node in the list of its siblings.
     */
    private int indexOf(Node node) {
        Node parent = node.getParent();

        if (null != parent) {
            Node[] children = parent.getChildren();
            if (null != filter) {
                children = filter.filter(children);
            }

            for (int i = 0; i < children.length; ++i) {
                if (children[i] == node) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Finds which node will be selected next.
     * 
     * @param parent
     *        the parent of the first node that has been removed.
     * @param index
     *        the index of the first removed node in the child list.
     * @return the node that should be selected next.
     */
    private Node getSelectionTarget(Node parent, int index) {
        Node target = null;
        if (null != parent) {
            Node[] children = parent.getChildren();
            if (null != filter) {
                children = filter.filter(children);
            }

            if (0 == children.length) {
                target = parent;
            } else if (index >= children.length) {
                target = children[children.length - 1];
            } else {
                target = children[index];
            }
        }
        return target;
    }

    @Override
    public void run() {
        if (!shouldDelete()) {
            return;
        }

        Object[] list = getSelection().toArray();

        if (0 < list.length) {
            // Store index and the parent of the first node to delete before the
            // deletion because after the node is removed from its parent this
            // information is lost.
            int index = indexOf((Node) list[0]);
            Node parent = ((Node) list[0]).getParent();

            for (Object o : list) {
                ((Node) o).getParent().removeChild((Node) o);
            }

            refreshAndSelect(getSelectionTarget(parent, index));
            page.validatePage();
        }
    }

}
