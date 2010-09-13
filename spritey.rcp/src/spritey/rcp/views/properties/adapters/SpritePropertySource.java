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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;

/**
 * Property source for supplying sprite properties to the Properties view.
 */
public class SpritePropertySource implements IPropertySource {

    private static final int WIDTH_ID = 0;
    private static final int HEIGHT_ID = 1;
    private static final int X_ID = 2;
    private static final int Y_ID = 3;
    private static final int NAME_ID = 4;

    private static final String WIDTH_TEXT = "Width";
    private static final String HEIGHT_TEXT = "Height";
    private static final String X_TEXT = "X";
    private static final String Y_TEXT = "Y";
    private static final String NAME_TEXT = "Name";

    private static final String LOCK_ICON_PATH = "data/icons/lock.png";

    private final Model model;

    private IPropertyDescriptor[] propertyDescriptors;

    public SpritePropertySource(Model model) {
        this.model = model;
        initialize();
    }

    private void initialize() {
        ILabelProvider lockLabelProvider = new PropertyLabelProvider(
                LOCK_ICON_PATH);

        PropertyDescriptor width = new PropertyDescriptor(WIDTH_ID, WIDTH_TEXT);
        width.setLabelProvider(lockLabelProvider);
        width.setAlwaysIncompatible(true);

        PropertyDescriptor height = new PropertyDescriptor(HEIGHT_ID,
                HEIGHT_TEXT);
        height.setLabelProvider(lockLabelProvider);
        height.setAlwaysIncompatible(true);

        PropertyDescriptor x = new PropertyDescriptor(X_ID, X_TEXT);
        x.setLabelProvider(lockLabelProvider);
        x.setAlwaysIncompatible(true);

        PropertyDescriptor y = new PropertyDescriptor(Y_ID, Y_TEXT);
        y.setLabelProvider(lockLabelProvider);
        y.setAlwaysIncompatible(true);

        PropertyDescriptor name = new PropertyDescriptor(NAME_ID, NAME_TEXT);
        name.setLabelProvider(lockLabelProvider);
        name.setAlwaysIncompatible(true);

        propertyDescriptors = new IPropertyDescriptor[] { name, width, height,
                x, y };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
     * .lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        Object value = null;

        switch ((Integer) id) {
        case WIDTH_ID:
            value = ((Rectangle) model.getProperty(Sprite.BOUNDS)).width;
            break;
        case HEIGHT_ID:
            value = ((Rectangle) model.getProperty(Sprite.BOUNDS)).height;
            break;
        case X_ID:
            value = ((Rectangle) model.getProperty(Sprite.BOUNDS)).x;
            break;
        case Y_ID:
            value = ((Rectangle) model.getProperty(Sprite.BOUNDS)).y;
            break;
        case NAME_ID:
            value = model.getProperty(Sprite.NAME);
            break;
        default:
            break;
        }

        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
     * .Object)
     */
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
     * .lang.Object)
     */
    @Override
    public void resetPropertyValue(Object id) {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void setPropertyValue(Object id, Object value) {
        try {
            switch ((Integer) id) {
            case NAME_ID:
                model.setProperty(Sprite.NAME, value);
                break;
            default:
                break;
            }
        } catch (InvalidPropertyValueException e) {
            // TODO Do NOT display any messages here since it will cause
            // another focus changed event which will in turn cause this
            // method to be called a second time. Find a better place to
            // display a message.
        }
    }

}
