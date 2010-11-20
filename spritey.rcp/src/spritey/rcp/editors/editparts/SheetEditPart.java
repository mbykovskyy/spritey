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

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.filter.VisibleSpriteFilter;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.editors.draw2d.HazardBorder;
import spritey.rcp.editors.tools.SpriteDragTracker;
import spritey.rcp.utils.ImageFactory;
import spritey.rcp.views.ViewUpdateListener;

/**
 * SheetEditPart is a child of ContentsEditPart. It corresponds to model's sheet
 * node.
 */
public class SheetEditPart extends AbstractGraphicalEditPart implements
        ViewUpdateListener {

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
        freeImage(sheet.getImage());

        Dimension size = (Dimension) model.getProperty(Sheet.SIZE);
        boolean isOpaque = (Boolean) model.getProperty(Sheet.OPAQUE);
        Color bg = (Color) model.getProperty(Sheet.BACKGROUND);
        RGB background = (null != bg) ? new RGB(bg.getRed(), bg.getGreen(),
                bg.getBlue()) : null;

        Image image = null;
        if (isOpaque && (null != background)) {
            image = imageFactory.createColorImage(background, size.width,
                    size.height, false);
        } else {
            image = imageFactory.createCheckerImage(size.width, size.height,
                    false);
        }

        sheet.setOpaque(isOpaque);
        sheet.setSize(size.width, size.height);
        sheet.setImage(image);
        sheet.setBorder(new HazardBorder());
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
        freeImage(sheet.getImage());
    }

    @Override
    protected IFigure createFigure() {
        sheet = new ImageFigure();
        sheet.setLayoutManager(new XYLayout());
        populateSheet(sheet, (Model) getModel());
        return sheet;
    }

    @Override
    protected void createEditPolicies() {
        // Do nothing.
    }

    @Override
    protected List<?> getModelChildren() {
        Model[] sprites = new VisibleSpriteFilter().filter((Model) getModel());
        return Arrays.asList(sprites);
    }

    @Override
    public void activate() {
        super.activate();
        SpriteyPlugin.getDefault().getViewUpdater().addListener(this);
    }

    @Override
    public void deactivate() {
        SpriteyPlugin.getDefault().getViewUpdater().removeListener(this);
        super.deactivate();
    }

    @Override
    protected void refreshVisuals() {
        populateSheet(sheet, (Model) getModel());
    }

    @Override
    public void updateView() {
        // TODO Only refresh when changed property is not DESCRIPTION.
        refreshVisuals();
    }

    @Override
    public void refreshView() {
        refresh();
    }

    @Override
    public DragTracker getDragTracker(Request request) {
        return new SpriteDragTracker(this);
    }

}
