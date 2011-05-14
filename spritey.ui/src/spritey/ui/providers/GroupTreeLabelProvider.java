/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2011 Maksym Bykovskyy.
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
package spritey.ui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import spritey.core.Group;
import spritey.core.Node;
import spritey.core.Sheet;
import spritey.ui.Application;

/**
 * Label provider for group tree.
 */
public class GroupTreeLabelProvider extends BaseLabelProvider implements
        ILabelProvider {

    @Override
    public Image getImage(Object element) {
        ImageRegistry reg = Application.getImageRegistry();

        if (element instanceof Sheet) {
            return reg.get(Application.SHEET_IMG_ID);
        } else if (element instanceof Group) {
            return reg.get(Application.GROUP_IMG_ID);
        }
        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof Node) {
            return ((Node) element).getName();
        }
        return null;
    }

}
