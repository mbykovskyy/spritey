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
package spritey.rcp.core;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import spritey.core.adapter.AdapterFactory;

/**
 * An adapter factory for converting client types into standard java types.
 */
public class AwtTypeAdapterFactory implements AdapterFactory {

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.adapter.AdapterFactory#getAdapter(java.lang.Object,
     * java.lang.Class)
     */
    @Override
    public Object getAdapter(Object adaptableObject, Class<?> adapterType) {
        if ((adapterType == java.awt.Rectangle.class)
                && (adaptableObject instanceof Rectangle)) {
            Rectangle r = (Rectangle) adaptableObject;
            return new java.awt.Rectangle(r.x, r.y, r.width, r.height);
        } else if ((adapterType == java.awt.Dimension.class)
                && (adaptableObject instanceof Dimension)) {
            Dimension d = (Dimension) adaptableObject;
            return new java.awt.Dimension(d.width, d.height);
        }

        return null;
    }

}
