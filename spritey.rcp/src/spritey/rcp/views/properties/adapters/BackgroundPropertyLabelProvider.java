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
package spritey.rcp.views.properties.adapters;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import spritey.rcp.utils.ImageFactory;

/**
 * Provides label for background property.
 */
public class BackgroundPropertyLabelProvider extends LabelProvider {

    private static final String TRANSPARENT = "Transparent";

    private static final int SIZE = 15;
    private static final int SQUARE = 5;

    private final ImageFactory imageFactory;

    private Image cachedImage;
    private Object cachedElement;

    public BackgroundPropertyLabelProvider() {
        imageFactory = new ImageFactory();
    }

    @Override
    public void dispose() {
        super.dispose();

        if ((null != cachedImage) && !cachedImage.isDisposed()) {
            cachedImage.dispose();
        }
    }

    @Override
    public Image getImage(Object element) {
        if ((null == cachedImage) || (cachedElement != element)) {
            cachedElement = element;

            if ((null != cachedImage) && !cachedImage.isDisposed()) {
                cachedImage.dispose();
            }

            // element.equals(TRANSPARENT) is needed until bug #320200
            // (https://bugs.eclipse.org/bugs/show_bug.cgi?id=320200) is fixed.
            if ((null == element) || element.equals(TRANSPARENT)) {
                cachedImage = imageFactory.createCheckerImage(SIZE, SIZE,
                        SQUARE, true);
            } else {
                cachedImage = imageFactory.createColorImage((RGB) element,
                        SIZE, SIZE, true);
            }
        }
        return cachedImage;
    }

    @Override
    public String getText(Object element) {
        if (null == element) {
            return TRANSPARENT;
        }

        return element.toString();
    }

}
