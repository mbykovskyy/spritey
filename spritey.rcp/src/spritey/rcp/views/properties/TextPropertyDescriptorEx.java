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
package spritey.rcp.views.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * This class is just a work around to bug #327285
 * (https://bugs.eclipse.org/bugs/show_bug.cgi?id=327285). SpritePropertySource
 * will explicitly set editor value when value is invalid.
 */
public class TextPropertyDescriptorEx extends TextPropertyDescriptor {

    private CellEditor editor;

    /**
     * Creates an property descriptor with the given id and display name.
     * 
     * @param id
     *        the id of the property
     * @param displayName
     *        the name to display for the property
     */
    public TextPropertyDescriptorEx(Object id, String displayName) {
        super(id, displayName);
    }

    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        editor = new TextCellEditor(parent);
        if (getValidator() != null) {
            editor.setValidator(getValidator());
        }
        return editor;
    }

    /**
     * Returns cell editor instance.
     * 
     * @return the cell editor.
     */
    public CellEditor getCellEditor() {
        return editor;
    }

}
