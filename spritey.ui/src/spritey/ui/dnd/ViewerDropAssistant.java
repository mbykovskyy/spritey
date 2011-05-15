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
package spritey.ui.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import spritey.core.Node;
import spritey.core.Sprite;

/**
 * Drop support for a viewer.
 */
public class ViewerDropAssistant extends ViewerDropAdapter {

    /**
     * Creates a new instance of DropAssistant.
     * 
     * @param viewer
     *        the viewer.
     */
    public ViewerDropAssistant(Viewer viewer) {
        super(viewer);
        setFeedbackEnabled(false);
    }

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
     * Returns current selection in the viewer.
     * 
     * @return the current selection.
     */
    private IStructuredSelection getSelection() {
        return (IStructuredSelection) LocalSelectionTransfer.getTransfer()
                .getSelection();
    }

    @Override
    public boolean performDrop(Object data) {
        for (Object o : getSelection().toArray()) {
            Node node = (Node) o;

            node.getParent().removeChild(node);
            ((Node) getCurrentTarget()).addChildren(node);
        }
        getViewer().refresh();
        return true;
    }

    @Override
    public boolean validateDrop(Object target, int operation,
            TransferData transferType) {

        // Validate target.
        if ((null == target) || (target instanceof Sprite)
                || (DND.DROP_MOVE != operation)) {
            return false;
        }

        // Validate selection.
        for (Object o : getSelection().toArray()) {
            Node node = (Node) o;

            // Drop is not allowed when,
            // 1. target is selected,
            // 2. target is already a parent of one of the selected nodes,
            // 3. target already contains a child with a name of one of the
            // selected nodes,
            // 4. target is a child of one of the selected nodes.
            if ((target == node) || (node.getParent() == target)
                    || ((Node) target).contains(node.getName())
                    || isChild(target, node)) {
                return false;
            }
        }
        return true;
    }

}
