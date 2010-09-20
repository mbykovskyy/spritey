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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;
import spritey.rcp.utils.ImageFactory;

/**
 * SheetEditPart is a child of ContentsEditPart. It corresponds to model's sheet
 * node.
 */
public class SheetEditPart extends AbstractGraphicalEditPart implements
        NodeListener, ModelListener {

    private ImageFigure sheet;
    private ImageFactory imageFactory;

    public SheetEditPart() {
        imageFactory = new ImageFactory();
    }

    /**
     * Populates the sheet with model values.
     * 
     * @param sheet
     *        the figure to populate.
     * @param model
     *        the model to get values from.
     */
    private void populateSheet(ImageFigure sheet, Model model) {
        Dimension size = (Dimension) model.getProperty(Sheet.SIZE);
        boolean isOpaque = (Boolean) model.getProperty(Sheet.OPAQUE);
        RGB background = (RGB) model.getProperty(Sheet.BACKGROUND);

        Image image = null;
        if (isOpaque && (null != background)) {
            image = imageFactory.createColorImage(background, size.width,
                    size.height, false);
        } else {
            image = imageFactory.createCheckerImage(size.width, size.height,
                    false);
        }

        sheet.setOpaque(isOpaque);
        sheet.setSize(size);
        sheet.setImage(image);

        // TODO Draw two-color dashed outline/border similar to Gimp.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        sheet = new ImageFigure();
        populateSheet(sheet, ((Node) getModel()).getModel());
        return sheet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    @Override
    protected List<?> getModelChildren() {
        return Arrays.asList(((Node) getModel()).getLeaves());
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

        if (node.getModel() != null) {
            node.getModel().addModelListener(this);
        }
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

        if (model.getModel() != null) {
            model.getModel().removeModelListener(this);
        }

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
     * @see spritey.core.node.event.NodeListener#properitesChanged(spritey.core.
     * Model, spritey.core.Model)
     */
    @Override
    public void modelChanged(Model oldValue, Model newValue) {
        refreshVisuals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.event.PropertyListener#propertyChanged(spritey.core.event
     * .PropertyEvent)
     */
    @Override
    public void propertyChanged(ModelEvent event) {
        if (event.getProperty() != Sheet.DESCRIPTION) {
            refreshVisuals();
        }
    }

    @Override
    protected void refreshVisuals() {
        populateSheet(sheet, ((Node) getModel()).getModel());
    }

}
