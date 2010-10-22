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
package spritey.rcp.handlers;

import java.awt.Rectangle;
import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.utils.ImageFactory;

/**
 * Handling the addition of a new sprite to a sprite sheet.
 */
public class AddSpriteHandler extends AbstractHandler implements IHandler {

    private static final String BMP_EXTENSIONS = "*.bmp;*.dib";
    private static final String JPEG_EXTENSIONS = "*.jpg;*.jpeg;*jpe;*.jfif";
    private static final String GIF_EXTENSIONS = "*.gif";
    private static final String TIFF_EXTENSIONS = "*.tif;*.tiff";
    private static final String PNG_EXTENSIONS = "*.png";
    private static final String ICO_EXTENSIONS = "*.ico";

    private static final String ALL_IMAGES_EXTENSIONS = BMP_EXTENSIONS + ";"
            + JPEG_EXTENSIONS + ";" + GIF_EXTENSIONS + ";" + TIFF_EXTENSIONS
            + ";" + PNG_EXTENSIONS + ";" + ICO_EXTENSIONS;

    private static final String ALL_FILES_EXTENSIONS = "*.*";

    private static final String[] FILTER_NAMES = { "BMP (*.bmp;*.dib)",
            "JPEG (*.jpg;*.jpeg;*jpe;*.jfif)", "GIF (*.gif)",
            "TIFF (*.tif;*.tiff)", "PNG (*.png)", "ICO (*.ico)", "All images",
            "All files" };

    private static final String[] FILTER_EXTENSIONS = { BMP_EXTENSIONS,
            JPEG_EXTENSIONS, GIF_EXTENSIONS, TIFF_EXTENSIONS, PNG_EXTENSIONS,
            ICO_EXTENSIONS, ALL_IMAGES_EXTENSIONS, ALL_FILES_EXTENSIONS };
    private static final int DEFAULT_FILTER_EXENSION_INDEX = 6;

    private ImageFactory imageFactory;
    private Shell shell;

    public AddSpriteHandler() {
        imageFactory = new ImageFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Ideally this handler will not be invoked as the command bound to
        // this handler will be disabled until root node is created.
        if (null == SpriteyPlugin.getDefault().getRootNode()) {
            return null;
        }

        shell = HandlerUtil.getActiveShell(event);

        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(FILTER_EXTENSIONS);
        dialog.setFilterNames(FILTER_NAMES);
        dialog.setFilterIndex(DEFAULT_FILTER_EXENSION_INDEX);

        String file = dialog.open();
        if (null != file) {
            loadFile(new File(file));
        }

        return null;
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootNode().isLeaf();
    }

    private ImageData loadImage(File file) {
        ImageData image = null;

        try {
            image = new ImageLoader().load(file.getAbsolutePath())[0];
        } catch (SWTException e) {
            // Do nothing.
        }
        return image;
    }

    private void loadFile(File file) {
        ImageData imageData = loadImage(file);

        if (null == imageData) {
            String message = NLS.bind(Messages.UNABLE_TO_LOAD_IMAGE,
                    file.getAbsolutePath());
            MessageDialog.openError(shell, Messages.ADD_SPRITE, message);
            return;
        }

        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        Model sprite = plugin.getModelFactory().createSprite();

        try {
            sprite.setProperty(Sprite.NAME, file.getName());
            sprite.setProperty(Sprite.IMAGE,
                    imageFactory.createImage(imageData));
            sprite.setProperty(Sprite.BOUNDS, new Rectangle(
                    Sprite.DEFAULT_BOUNDS.x, Sprite.DEFAULT_BOUNDS.y,
                    imageData.width, imageData.height));
        } catch (InvalidPropertyValueException e) {
            handleException(e);
            return;
        }

        Node node = plugin.getNodeFactory().createNode(file.getName());
        node.setModel(sprite);

        Node sheetNode = plugin.getRootNode().getChildren()[0];
        if (sheetNode.addChild(node)) {
            plugin.getPacker().pack(sheetNode, false);
            plugin.getViewUpdater().refreshViews();

            if (((Rectangle) sprite.getProperty(Sprite.BOUNDS)).x < 0) {
                MessageDialog.openWarning(shell, Messages.ADD_SPRITE,
                        NLS.bind(Messages.SPRITE_DOES_NOT_FIT, file.getName()));
            }
        } else {
            MessageDialog.openError(shell, Messages.ADD_SPRITE,
                    NLS.bind(Messages.MODEL_NAME_EXISTS, file.getName()));
        }
    }

    private void handleException(InvalidPropertyValueException e) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case UniqueNameValidator.NAME_NOT_UNIQUE:
            message = NLS.bind(Messages.MODEL_NAME_EXISTS, e.getValue());
            break;
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.SPRITE_NAME_INVALID,
                    Sprite.MIN_NAME_LENGTH, Sprite.MAX_NAME_LENGTH);
            break;
        case NotNullValidator.NULL:
        case TypeValidator.NOT_TYPE:
        default:
            // Log it since we don't expect this exception.
            e.printStackTrace();
            break;
        }

        MessageDialog.openError(shell, Messages.NEW_SPRITE_SHEET, message);
    }

}
