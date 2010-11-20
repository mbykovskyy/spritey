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

import java.awt.Color;
import java.awt.Dimension;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.rcp.SpriteyPlugin;
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
    private static final String TRANSPARENT_TEXT = "Transparent";

    private final Model model;

    private IPropertyDescriptor[] propertyDescriptors;

    public SheetPropertySource(Model model) {
        this.model = model;
        initialize();
    }

    private void initialize() {
        ImageRegistry reg = SpriteyPlugin.getDefault().getImageRegistry();

        PropertyLabelProvider editableLabelProvider = new PropertyLabelProvider(
                reg.get(SpriteyPlugin.EDIT_IMG_ID));

        PropertyDescriptor bg = new BackgroundPropertyDescriptor(BACKGROUND_ID,
                BACKGROUND_TEXT);
        bg.setLabelProvider(new BackgroundPropertyLabelProvider());
        bg.setAlwaysIncompatible(true);

        TextPropertyDescriptor description = new TextPropertyDescriptor(
                DESCRIPTION_ID, DESCRIPTION_TEXT);
        description.setLabelProvider(editableLabelProvider);
        description.setAlwaysIncompatible(true);

        PropertyDescriptor width = new WidthPropertyDescriptor(WIDTH_ID,
                WIDTH_TEXT);
        width.setLabelProvider(editableLabelProvider);
        width.setAlwaysIncompatible(true);

        PropertyDescriptor height = new HeightPropertyDescriptor(HEIGHT_ID,
                HEIGHT_TEXT);
        height.setLabelProvider(editableLabelProvider);
        height.setAlwaysIncompatible(true);

        propertyDescriptors = new IPropertyDescriptor[] { description, width,
                height, bg };
    }

    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;

    }

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
            Color bg = (Color) model.getProperty(Sheet.BACKGROUND);

            if (null == bg) {
                // We need this until bug #320200
                // (https://bugs.eclipse.org/bugs/show_bug.cgi?id=320200) is
                // fixed.
                value = TRANSPARENT_TEXT;
            } else {
                value = new RGB(bg.getRed(), bg.getGreen(), bg.getBlue());
            }
            break;
        case DESCRIPTION_ID:
            value = model.getProperty(Sheet.DESCRIPTION);
            break;
        default:
            break;
        }

        return value;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // Do nothing.
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        try {
            switch ((Integer) id) {
            case WIDTH_ID:
                int width = (Integer) value;
                int height = ((Dimension) model.getProperty(Sheet.SIZE)).height;
                model.setProperty(Sheet.SIZE, new Dimension(width, height));

                Sheet sheet = (Sheet) SpriteyPlugin.getDefault().getRootModel()
                        .getChildren()[0];
                SpriteyPlugin.getDefault().getPacker().pack(sheet, true);
                break;
            case HEIGHT_ID:
                width = ((Dimension) model.getProperty(Sheet.SIZE)).width;
                height = (Integer) value;
                model.setProperty(Sheet.SIZE, new Dimension(width, height));

                sheet = (Sheet) SpriteyPlugin.getDefault().getRootModel()
                        .getChildren()[0];
                SpriteyPlugin.getDefault().getPacker().pack(sheet, true);
                break;
            case BACKGROUND_ID:
                if (null != value) {
                    RGB bg = (RGB) value;
                    model.setProperty(Sheet.BACKGROUND, new Color(bg.red,
                            bg.green, bg.blue));
                } else {
                    model.setProperty(Sheet.BACKGROUND, null);
                }
                model.setProperty(Sheet.OPAQUE, null != value);
                break;
            case DESCRIPTION_ID:
                model.setProperty(Sheet.DESCRIPTION, value);
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

        SpriteyPlugin.getDefault().getViewUpdater().refreshViews();
    }

}
