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
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.SpriteConstants;

/**
 * Label provider for AddSpritesPreviewTree.
 */
public class AddSpritesPreviewTreeLabelProvider extends LabelProvider implements
        IColorProvider {

    private Color errorTextForeground;

    /**
     * Creates a new instance of AddSpritesPreviewTreeLableProvider.
     * 
     * @param errorTextForeground
     *        the foreground color of the error text.
     */
    public AddSpritesPreviewTreeLabelProvider(Color errorTextForeground) {
        this.errorTextForeground = errorTextForeground;
    }

    /**
     * Indicates whether the specified model has an error to display.
     * 
     * @param model
     *        the model to test.
     * @return <code>true</code> if the model has an error, otherwise
     *         <code>false</code>.
     */
    protected boolean hasError(Model model) {
        boolean spriteHasError = ((model instanceof Sprite) && (model
                .getProperty(SpriteConstants.DISPLAY_ERROR) != null));
        boolean groupHasError = ((model instanceof Group) && (model
                .getProperty(GroupConstants.DISPLAY_ERROR) != null));
        return spriteHasError || groupHasError;
    }

    @Override
    public Image getImage(Object element) {
        Image img = null;
        Model model = (Model) element;
        ISharedImages share = PlatformUI.getWorkbench().getSharedImages();
        ImageRegistry reg = SpriteyPlugin.getDefault().getImageRegistry();

        if (hasError(model)) {
            img = share.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
        } else if (model instanceof Sprite) {
            img = reg.get(SpriteyPlugin.SPRITE_IMG_ID);
        } else if (model instanceof Group) {
            img = reg.get(SpriteyPlugin.GROUP_IMG_ID);
        }
        return img;
    }

    @Override
    public String getText(Object element) {
        String text = "";
        String errorMessage = "";
        Model model = (Model) element;

        if (model instanceof Sprite) {
            text = (String) model.getProperty(Sprite.NAME);
            errorMessage = (String) model
                    .getProperty(SpriteConstants.DISPLAY_ERROR);
        } else if (model instanceof Group) {
            text = (String) model.getProperty(Group.NAME);
            errorMessage = (String) model
                    .getProperty(GroupConstants.DISPLAY_ERROR);
        }

        if (null != errorMessage) {
            text += " " + errorMessage;
        }
        return text;
    }

    @Override
    public Color getForeground(Object element) {
        if (hasError((Model) element)) {
            return errorTextForeground;
        }
        return null;
    }

    @Override
    public Color getBackground(Object element) {
        return null;
    }

}
