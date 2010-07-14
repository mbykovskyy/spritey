/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
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
package spritey.rcp.editors.editparts;

import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import spritey.core.Model;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;

/**
 * ContentsEditPart is a contents node of the viewer that contains sprite
 * sheet's root node. Note that this is not a sprite sheet node itself.
 */
public class ContentsEditPart extends AbstractGraphicalEditPart implements
        NodeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        IFigure contents = new Figure();
        contents.setLayoutManager(layout);

        return contents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    @Override
    protected List<?> getModelChildren() {
        return Arrays.asList(((Node) getModel()).getChildren());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    @Override
    public void activate() {
        super.activate();

        Node node = (Node) getModel();
        node.addNodeListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        Node model = (Node) getModel();
        model.removeNodeListener(this);

        super.deactivate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.event.NodeListener#nameChanged(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void nameChanged(String oldName, String newName) {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#parentChanged(spritey.core.node.
     * Node, spritey.core.node.Node)
     */
    @Override
    public void parentChanged(Node oldParent, Node newParent) {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#childAdded(spritey.core.node.Node)
     */
    @Override
    public void childAdded(Node child) {
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#childRemoved(spritey.core.node.Node)
     */
    @Override
    public void childRemoved(Node child) {
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.event.NodeListener#childrenRemoved()
     */
    @Override
    public void childrenRemoved() {
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.event.NodeListener#properitesChanged(spritey.core.
     * Model, spritey.core.Model)
     */
    @Override
    public void modelChanged(Model oldValue, Model newValue) {
        // Do nothing.
    }

}
