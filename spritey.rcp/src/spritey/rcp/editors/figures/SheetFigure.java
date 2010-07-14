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
package spritey.rcp.editors.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import spritey.rcp.utils.ImageFactory;

/**
 * The sheet figure with checker background when background is transparent.
 */
public class SheetFigure extends RectangleFigure {

    private ImageFactory imageFactory;

    /**
     * Default constructor
     */
    public SheetFigure() {
        imageFactory = new ImageFactory();
    }

    @Override
    public void fillShape(Graphics graphics) {
        if (!isOpaque() || (getBackgroundColor() == null)) {
            // TODO Optimise this. Creating checker image every time figure is
            // redrawn is wasteful. If size and background hasn't changed then
            // we can reuse an image.
            Image checker = imageFactory.createCheckerImage(getSize().width,
                    getSize().height);
            graphics.drawImage(checker, new Rectangle(checker.getBounds()),
                    getBounds());
        } else {
            super.fillShape(graphics);
        }
    }

}
