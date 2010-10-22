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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.node.Node;
import spritey.rcp.editors.editparts.SheetEditPart;
import spritey.rcp.editors.editparts.SpriteEditPart;

/**
 * Factory for creating property source adapters. This factory can adapt
 * SheetEditPart.
 */
public class PropertySourceAdapterFactory implements IAdapterFactory {

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IPropertySource.class) {
            if (adaptableObject instanceof SheetEditPart) {
                SheetEditPart part = (SheetEditPart) adaptableObject;
                Node node = (Node) part.getModel();
                return new SheetPropertySource(node.getModel());
            } else if (adaptableObject instanceof SpriteEditPart) {
                SpriteEditPart part = (SpriteEditPart) adaptableObject;
                Node node = (Node) part.getModel();
                return new SpritePropertySource(node.getModel());
            } else if (adaptableObject instanceof Node) {
                Model model = ((Node) adaptableObject).getModel();

                if (model instanceof Sheet) {
                    return new SheetPropertySource(model);
                } else if (model instanceof Sprite) {
                    return new SpritePropertySource(model);
                } else if (model instanceof Group) {
                    return new GroupPropertySource(model);
                }
            }
        }

        return null;
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

}
