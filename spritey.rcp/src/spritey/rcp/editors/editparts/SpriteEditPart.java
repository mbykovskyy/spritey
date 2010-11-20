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
package spritey.rcp.editors.editparts;

import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import spritey.core.Model;
import spritey.core.Sprite;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.editors.tools.SpriteDragTracker;

/**
 * SpriteEditPart is a child of SheetEditPart. It corresponds to model's sprite
 * node.
 */
public class SpriteEditPart extends AbstractGraphicalEditPart {

    private static final RGB BORDER_COLOR = new RGB(0, 0, 0);
    private static final RGB SELECTION_COLOR = new RGB(255, 255, 255);

    private static final int BORDER_WIDTH = 1;
    private static final int SELECTION_WIDTH = 2;

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
            Image image = (Image) model.getProperty(SpriteConstants.SWT_IMAGE);
            sprite.setImage(image);
            sprite.setVisible(true);
        }

        // XYLayout positions figures using figure constraints.
        if (null != sprite.getParent()) {
            sprite.getParent().setConstraint(sprite,
                    sprite.getBounds().getCopy());
        }
    }

    @Override
    protected IFigure createFigure() {
        sprite = new ImageFigure();
        populateSprite(sprite, (Model) getModel());
        return sprite;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
                new SelectionEditPolicy() {
                    @Override
                    protected void showSelection() {
                        sprite.setBorder(new LineBorder(SELECTION_WIDTH));
                        sprite.setForegroundColor(new Color(Display
                                .getDefault(), SELECTION_COLOR));
                    }

                    @Override
                    protected void hideSelection() {
                        sprite.setBorder(new LineBorder(BORDER_WIDTH));
                        sprite.setForegroundColor(new Color(Display
                                .getDefault(), BORDER_COLOR));
                    }
                });
    }

    @Override
    protected List<?> getModelChildren() {
        return Arrays.asList(((Model) getModel()).getChildren());
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

    @Override
    public void activate() {
        super.activate();
        // TODO add to view update manager. At the moment sheet updates all
        // sprites.
    }

    @Override
    public void deactivate() {
        // TODO remove from view update manager. At the moment sheet updates all
        // sprites.
        super.deactivate();
    }

    @Override
    protected void refreshVisuals() {
        populateSprite(sprite, (Model) getModel());
    }

    @Override
    public DragTracker getDragTracker(Request request) {
        return new SpriteDragTracker((SheetEditPart) getParent());
    }

}
