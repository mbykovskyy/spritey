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
import spritey.rcp.SpriteyPlugin;

/**
 * Label provider for sprite tree view.
 */
public class SpriteTreeLabelProvider extends BaseLabelProvider implements
        ILabelProvider {

    private static final String INVISIBLE = "invisible";

    @Override
    public Image getImage(Object element) {
        ImageRegistry reg = SpriteyPlugin.getDefault().getImageRegistry();

        if (element instanceof Sprite) {
            Sprite sprite = (Sprite) element;
            Rectangle bounds = (Rectangle) sprite.getProperty(Sprite.BOUNDS);

            if ((bounds.x < 0) || (bounds.y < 0)) {
                return reg.get(SpriteyPlugin.INVISIBLE_IMG_ID);
            }
            return reg.get(SpriteyPlugin.SPRITE_IMG_ID);
        } else if (element instanceof Sheet) {
            return reg.get(SpriteyPlugin.SHEET_IMG_ID);
        } else if (element instanceof Group) {
            return reg.get(SpriteyPlugin.GROUP_IMG_ID);
        }
        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof Sheet) {
            // Sheet does not have a name, therefore, use default name.
            return Sheet.DEFAULT_NAME;
        } else if (element instanceof Group) {
            return ((Model) element).getProperty(Group.NAME).toString();
        } else if (element instanceof Sprite) {
            Rectangle bounds = (Rectangle) ((Model) element)
                    .getProperty(Sprite.BOUNDS);

            String name = ((Model) element).getProperty(Sprite.NAME).toString();
            if ((bounds.x < 0) || (bounds.y < 0)) {
                name = "[" + INVISIBLE + "] " + name;
            }

            return name;
        }
        return null;
    }

}
