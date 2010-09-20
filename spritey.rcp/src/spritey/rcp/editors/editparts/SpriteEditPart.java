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
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;

import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;

/**
 * SpriteEditPart is a child of SheetEditPart. It corresponds to model's sprite
 * node.
 */
public class SpriteEditPart extends AbstractGraphicalEditPart implements
        NodeListener, ModelListener {

    private ImageFigure sprite;

    public SpriteEditPart() {
    }

    /**
     * Populates the sprite with model values.
     * 
     * @param sprite
     *        the figure to populate.
     * @param model
     *        the model to get values from.
     */
    private void populateSprite(ImageFigure sprite, Model model) {
        Rectangle bounds = (Rectangle) model.getProperty(Sprite.BOUNDS);
        sprite.setBounds(bounds);

        if ((bounds.x < 0) || (bounds.y < 0)) {
            sprite.setVisible(false);
        } else {
            Image image = (Image) model.getProperty(Sprite.IMAGE);
            sprite.setImage(image);
            sprite.setVisible(true);
            sprite.setBorder(new LineBorder());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        sprite = new ImageFigure();
        populateSprite(sprite, ((Node) getModel()).getModel());
        return sprite;
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
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#childRemoved(spritey.core.node.Node)
     */
    @Override
    public void childRemoved(Node child) {
        // Do nothing.
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
        if (event.getProperty() == Sprite.BOUNDS) {
            refreshVisuals();
        }
    }

    @Override
    protected void refreshVisuals() {
        populateSprite(sprite, ((Node) getModel()).getModel());
    }

}
