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
package spritey.rcp.views.navigator;

import java.awt.Rectangle;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.node.Node;
import spritey.rcp.SpriteyPlugin;

/**
 * Label provider for sprite tree view.
 */
public class SpriteTreeLabelProvider extends BaseLabelProvider implements
        ILabelProvider {

    private static final String INVISIBLE = "invisible";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        Model model = ((Node) element).getModel();
        ImageRegistry reg = SpriteyPlugin.getDefault().getImageRegistry();

        if (model instanceof Sprite) {
            Sprite sprite = (Sprite) model;
            Rectangle bounds = (Rectangle) sprite.getProperty(Sprite.BOUNDS);

            if ((bounds.x < 0) || (bounds.y < 0)) {
                return reg.get(SpriteyPlugin.INVISIBLE_IMG_ID);
            }
            return reg.get(SpriteyPlugin.SPRITE_IMG_ID);
        } else if (model instanceof Sheet) {
            return reg.get(SpriteyPlugin.SHEET_IMG_ID);
        } else if (model instanceof Group) {
            return reg.get(SpriteyPlugin.GROUP_IMG_ID);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if ((null != element) && (element instanceof Node)) {
            Model data = ((Node) element).getModel();

            if (null != data) {
                if (data instanceof Sheet) {
                    // Sheet does not have a name, therefore, use node's name.
                    return ((Node) element).getName();
                } else if (data instanceof Group) {
                    return data.getProperty(Group.NAME).toString();
                } else if (data instanceof Sprite) {
                    Rectangle bounds = (Rectangle) data
                            .getProperty(Sprite.BOUNDS);

                    String name = data.getProperty(Sprite.NAME).toString();
                    if ((bounds.x < 0) || (bounds.y < 0)) {
                        name = "[" + INVISIBLE + "] " + name;
                    }

                    return name;
                }
            }
        }
        return null;
    }

}
