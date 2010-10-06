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
import spritey.core.node.Node;

/**
 * SpriteEditPart is a child of SheetEditPart. It corresponds to model's sprite
 * node.
 */
public class SpriteEditPart extends AbstractGraphicalEditPart {

    private ImageFigure sprite;

    /**
     * Populates the sprite with model values.
     * 
     * @param sprite
     *        the figure to populate.
     * @param model
     *        the model to get values from.
     */
    private void populateSprite(ImageFigure sprite, Model model) {
        java.awt.Rectangle bounds = (java.awt.Rectangle) model
                .getProperty(Sprite.BOUNDS);
        sprite.setBounds(new Rectangle(bounds.x, bounds.y, bounds.width,
                bounds.height));

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

    /**
     * Free the specified image.
     * 
     * @param image
     *        the image to free.
     */
    private void freeImage(Image image) {
        if ((null != image) && !image.isDisposed()) {
            image.dispose();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        freeImage(sprite.getImage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    @Override
    public void activate() {
        super.activate();
        // TODO add to view update manager.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        // TODO remove from view update manager.
        super.deactivate();
    }

    // public void propertyChanged(ModelEvent event) {
    // if (event.getProperty() == Sprite.BOUNDS) {
    // refreshVisuals();
    // }
    // }

    @Override
    protected void refreshVisuals() {
        populateSprite(sprite, ((Node) getModel()).getModel());
    }

}
