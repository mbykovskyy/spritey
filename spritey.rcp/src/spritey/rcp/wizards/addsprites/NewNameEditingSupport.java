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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.SpriteConstants;

/**
 * Editing support for NEW_NAME property.
 */
public class NewNameEditingSupport extends EditingSupport {

    /**
     * Creates a new instance of NewNameEditingSupport.
     * 
     * @param viewer
     *        the viewer to which this editing support is applied to.
     */
    public NewNameEditingSupport(TreeViewer viewer) {
        super(viewer);
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return new TextCellEditor(((TreeViewer) getViewer()).getTree());
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        Object value = null;

        if (element instanceof Sprite) {
            value = ((Model) element).getProperty(SpriteConstants.NEW_NAME);
        } else if (element instanceof Group) {
            value = ((Model) element).getProperty(GroupConstants.NEW_NAME);
        }
        return value;
    }

    @Override
    protected void setValue(Object element, Object value) {
        try {
            if (element instanceof Sprite) {
                ((Model) element).setProperty(SpriteConstants.NEW_NAME, value);
            } else if (element instanceof Group) {
                ((Model) element).setProperty(GroupConstants.NEW_NAME, value);
            }
        } catch (InvalidPropertyValueException e) {
            // Do nothing.
        }
        getViewer().update(element, null);
    }

}
