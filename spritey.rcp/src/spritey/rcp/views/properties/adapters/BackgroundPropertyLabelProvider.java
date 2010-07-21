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

    private static final int SIZE = 16;
    private static final int SQUARE = 5;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        ImageFactory factory = new ImageFactory();

        if (null == element) {
            return factory.createCheckerImage(SIZE, SIZE, SQUARE, true);
        }

        return factory.createColorImage((RGB) element, SIZE, SIZE, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if (null == element) {
            return TRANSPARENT;
        }

        return element.toString();
    }

}
