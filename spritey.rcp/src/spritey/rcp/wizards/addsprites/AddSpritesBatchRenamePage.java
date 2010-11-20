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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.filter.ModelNameFilter;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.Root;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.views.navigator.SpriteTreeContentProvider;

/**
 * A customised BatchRenamePage to rename models selected in the AddSpritesPage.
 */
public class AddSpritesBatchRenamePage extends BatchRenamePage {

    private Model sheet;
    private Model[] sheetChildren;

    /**
     * Creates a new instance of AddSpritesBatchRenamePage.
     */
    public AddSpritesBatchRenamePage() {
        super(null);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            root = ((AddSpritesPage) getPreviousPage()).getRootModel();
            tree.setInput(root);
            tree.expandAll();
        }
    }

    @Override
    protected void createPreviewTree(Composite parent) {
        super.createPreviewTree(parent);

        tree.setContentProvider(new SpriteTreeContentProvider() {
            @Override
            public Object[] getChildren(Object parentElement) {
                Object[] children = super.getChildren(parentElement);
                List<Object> checked = new ArrayList<Object>(children.length);

                for (Object child : children) {
                    if (isChecked((Model) child)) {
                        checked.add(child);
                    }
                }
                return checked.toArray();
            }
        });
    }

    /**
     * Returns an instance of a Sheet.
     * 
     * @return an instance of a Sheet.
     */
    protected Model getSheet() {
        if (null == sheet) {
            sheet = SpriteyPlugin.getDefault().getRootModel().getChildren()[0];
        }
        return sheet;
    }

    /**
     * Returns sprite sheet's immediate children.
     * 
     * @return sheet's children.
     */
    protected Model[] getSheetChildren() {
        if (null == sheetChildren) {
            sheetChildren = getSheet().getChildren();
        }
        return sheetChildren;
    }

    /**
     * Specified whether the specified model is checked by testing the CHECKED
     * property value.
     * 
     * @param model
     *        the model to test.
     * @return <code>true</code> if the model is checked, otherwise
     *         <code>false</code>.
     */
    private boolean isChecked(Model model) {
        int property = model instanceof Group ? GroupConstants.CHECKED
                : SpriteConstants.CHECKED;
        Object value = model.getProperty(property);
        return (null != value) && (Boolean) value;
    }

    @Override
    protected boolean validate(Model model) {
        if ((model instanceof Root) || isChecked(model)) {
            if (model.getParent() instanceof Root) {
                // Check if the NEW_NAME is unique within sprite sheet.
                boolean isGroup = model instanceof Group;
                int property = isGroup ? GroupConstants.NEW_NAME
                        : SpriteConstants.NEW_NAME;
                String name = (String) model.getProperty(property);

                if (new ModelNameFilter(name).filter(getSheetChildren()).length > 0) {
                    String message = isGroup ? Messages.BATCH_RENAME_SHEET_CONTAINS_GROUP
                            : Messages.BATCH_RENAME_SHEET_CONTAINS_SPRITE;
                    message = NLS.bind(message, name);

                    setErrorMessage(message);
                    setPageComplete(false);
                    return false;
                }
            }
            return super.validate(model);
        }
        return true;
    }

}
