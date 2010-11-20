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
package spritey.rcp.wizards.addsprites;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.SpriteConstants;

/**
 * Label provider for batch rename preview tree.
 */
public class RenamePreviewTreeLabelProvider extends BaseLabelProvider implements
        ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        Image img = null;
        ImageRegistry reg = SpriteyPlugin.getDefault().getImageRegistry();

        switch (columnIndex) {
        case 0:
            if (element instanceof Sprite) {
                img = reg.get(SpriteyPlugin.SPRITE_IMG_ID);
            } else if (element instanceof Group) {
                img = reg.get(SpriteyPlugin.GROUP_IMG_ID);
            }
            break;
        case 1:
            img = reg.get(SpriteyPlugin.EDIT_IMG_ID);
            break;
        }
        return img;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        String text = "";
        Model model = (Model) element;

        switch (columnIndex) {
        case 0:
            if (model instanceof Sprite) {
                text = (String) model.getProperty(Sprite.NAME);
            } else if (model instanceof Group) {
                text = (String) model.getProperty(Group.NAME);
            }
            break;
        case 1:
            if (model instanceof Sprite) {
                text = (String) model.getProperty(SpriteConstants.NEW_NAME);
            } else if (model instanceof Group) {
                text = (String) model.getProperty(GroupConstants.NEW_NAME);
            }
            break;
        }
        return text;
    }
}
