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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.rcp.views.properties.BackgroundPropertyDescriptor;
import spritey.rcp.views.properties.HeightPropertyDescriptor;
import spritey.rcp.views.properties.WidthPropertyDescriptor;

/**
 * Property source for supplying sheet properties to the Properties view.
 */
public class SheetPropertySource implements IPropertySource {

    private static final int WIDTH_ID = 0;
    private static final int HEIGHT_ID = 1;
    private static final int BACKGROUND_ID = 2;
    private static final int DESCRIPTION_ID = 3;

    private static final String WIDTH_TEXT = "Width";
    private static final String HEIGHT_TEXT = "Height";
    private static final String BACKGROUND_TEXT = "Background";
    private static final String DESCRIPTION_TEXT = "Description";

    private static final String EDIT_ICON_PATH = "data/icons/textfield_rename.png";

    private final Model model;

    private IPropertyDescriptor[] propertyDescriptors;

    public SheetPropertySource(Model model) {
        this.model = model;
        initialize();
    }

    private void initialize() {
        PropertyLabelProvider editableLabelProvider = new PropertyLabelProvider(
                EDIT_ICON_PATH);

        PropertyDescriptor bg = new BackgroundPropertyDescriptor(BACKGROUND_ID,
                BACKGROUND_TEXT);
        bg.setLabelProvider(new BackgroundPropertyLabelProvider());

        TextPropertyDescriptor description = new TextPropertyDescriptor(
                DESCRIPTION_ID, DESCRIPTION_TEXT);
        description.setLabelProvider(editableLabelProvider);

        PropertyDescriptor width = new WidthPropertyDescriptor(WIDTH_ID,
                WIDTH_TEXT);
        width.setLabelProvider(editableLabelProvider);

        PropertyDescriptor height = new HeightPropertyDescriptor(HEIGHT_ID,
                HEIGHT_TEXT);
        height.setLabelProvider(editableLabelProvider);

        propertyDescriptors = new IPropertyDescriptor[] { description, width,
                height, bg };
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
            value = ((Dimension) model.getProperty(Sheet.SIZE)).width;
            break;
        case HEIGHT_ID:
            value = ((Dimension) model.getProperty(Sheet.SIZE)).height;
            break;
        case BACKGROUND_ID:
            value = model.getProperty(Sheet.BACKGROUND);
            break;
        case DESCRIPTION_ID:
            value = model.getProperty(Sheet.DESCRIPTION);
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
        switch ((Integer) id) {
        case WIDTH_ID:
            int width = (Integer) value;
            int height = ((Dimension) model.getProperty(Sheet.SIZE)).height;
            model.setProperty(Sheet.SIZE, new Dimension(width, height));
            break;
        case HEIGHT_ID:
            width = ((Dimension) model.getProperty(Sheet.SIZE)).width;
            height = (Integer) value;
            model.setProperty(Sheet.SIZE, new Dimension(width, height));
            break;
        case BACKGROUND_ID:
            model.setProperty(Sheet.OPAQUE, null != value);
            model.setProperty(Sheet.BACKGROUND, value);
            break;
        case DESCRIPTION_ID:
            model.setProperty(Sheet.DESCRIPTION, value);
            break;
        default:
            break;
        }
    }

}