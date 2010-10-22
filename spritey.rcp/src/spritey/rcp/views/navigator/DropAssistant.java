/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
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
package spritey.rcp.views.navigator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.node.Node;
import spritey.rcp.SpriteyPlugin;

public class DropAssistant extends CommonDropAdapterAssistant {

    public static final String ID = "spritey.rcp.views.navigator.dropAssistant";

    /**
     * Returns <code>true</code> if <code>target</code> is a direct or indirect
     * child of the <code>node</code>.
     * 
     * @param target
     *        the target to test.
     * @param node
     *        the node to test against.
     * @return <code>true</code> when target is a child of the node.
     */
    private boolean isChild(Object target, Node node) {
        if (node.contains((Node) target)) {
            return true;
        }

        for (Node child : node.getChildren()) {
            if (isChild(target, child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the name of the specified source node is unique within the
     * specified target.
     * 
     * @param target
     *        the drop target.
     * @param src
     *        the source node to test.
     * @return <code>true</code> if the name of the source node is unique within
     *         the target.
     */
    private boolean isNameUnique(Node target, Node src) {
        Model srcModel = src.getModel();

        if (srcModel instanceof Sprite) {
            return !target.contains((String) srcModel.getProperty(Sprite.NAME));
        } else if (srcModel instanceof Group) {
            return !target.contains((String) srcModel.getProperty(Group.NAME));
        }
        return false;
    }

    /**
     * Tests whether the specified target is valid. Note, this call is expensive
     * as it traverses through the current selection and its children,
     * therefore, is should always be called last.
     * 
     * @param target
     *        the target to validate.
     * @return <code>true</code> if target is valid, otherwise
     *         <code>false</code>.
     */
    private boolean isValid(Object target) {
        if (!(target instanceof Node)
                || (((Node) target).getModel() instanceof Sprite)) {
            return false;
        }

        IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer
                .getTransfer().getSelection();

        for (Object s : selection.toArray()) {
            Node node = (Node) s;

            if ((target == s) || (node.getParent() == target)
                    || !isNameUnique((Node) target, node)
                    || isChild(target, node)) {
                // Drop is not allowed when,
                // 1. target is selected,
                // 2. target is already a parent of one of the selected nodes,
                // 3. target already contains a child with a name of one of the
                // selected nodes,
                // 4. target is a child of one of the selected nodes.
                return false;
            }
        }
        return true;
    }

    @Override
    public IStatus validateDrop(Object target, int operation,
            TransferData transferType) {
        if (!isSupportedType(transferType) || (DND.DROP_MOVE != operation)
                || !isValid(target)) {
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    @Override
    public IStatus handleDrop(CommonDropAdapter dropAdapter,
            DropTargetEvent dropTargetEvent, Object target) {
        IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer
                .getTransfer().getSelection();

        for (Object s : selection.toArray()) {
            Node src = (Node) s;

            src.getParent().removeChild(src);
            ((Node) target).addChild(src);
        }

        SpriteyPlugin.getDefault().getViewUpdater().refreshViews();

        dropTargetEvent.detail = DND.DROP_NONE;
        return Status.OK_STATUS;
    }
}
