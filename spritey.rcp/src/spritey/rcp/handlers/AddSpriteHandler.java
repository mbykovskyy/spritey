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

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.node.NodeFactory;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.Validator;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.Messages;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.utils.ImageFactory;
import spritey.rcp.validators.UniqueNameValidator;

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
            createSprite(file);
        }

        return null;
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootNode().isLeaf();
    }

    private ImageData loadImage(String path) {
        ImageLoader loader = new ImageLoader();
        return loader.load(path)[0];
    }

    private void createSprite(String path) {
        String spriteName = new File(path).getName();
        ImageData imageData = loadImage(path);

        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        NodeFactory nodeFactory = plugin.getNodeFactory();

        try {
            Model sprite = createSprite(modelFactory);
            sprite.setProperty(Sprite.NAME, spriteName);
            sprite.setProperty(Sprite.IMAGE,
                    imageFactory.createImage(imageData));
            sprite.setProperty(Sprite.BOUNDS, new Rectangle(
                    SpriteConstants.DEFAULT_X, SpriteConstants.DEFAULT_Y,
                    imageData.width, imageData.height));

            Node node = nodeFactory.createNode(spriteName);
            node.setModel(sprite);

            Node sheetNode = plugin.getRootNode().getChildren()[0];
            if (sheetNode.addChild(node)) {
                SpriteyPlugin.getDefault().getPacker().pack(sheetNode, false);

                if (((Rectangle) sprite.getProperty(Sprite.BOUNDS)).x < 0) {
                    MessageDialog.openWarning(shell, Messages.ADD_SPRITE,
                            NLS.bind(Messages.SPRITE_DOES_NOT_FIT, spriteName));
                }
            } else {
                MessageDialog.openError(shell, Messages.ADD_SPRITE,
                        NLS.bind(Messages.SPRITE_NAME_EXISTS, spriteName));
            }
        } catch (InvalidPropertyValueException e) {
            handleException(e);
        }
    }

    /**
     * Creates and populates sprite model.
     * 
     * @param modelFactory
     *        factory to use to create sprite model.
     * 
     * @return instance of sprite model.
     */
    private Model createSprite(ModelFactory modelFactory) {
        Validator notNullValidator = new NotNullValidator();

        Model sprite = modelFactory.createSprite();
        sprite.addValidator(Sprite.NAME, notNullValidator);
        sprite.addValidator(Sprite.NAME, new TypeValidator(String.class));
        sprite.addValidator(Sprite.NAME, new StringLengthValidator(
                SpriteConstants.MIN_NAME_LENGTH,
                SpriteConstants.MAX_NAME_LENGTH));
        sprite.addValidator(Sprite.NAME, new UniqueNameValidator(
                (Sprite) sprite));

        sprite.addValidator(Sprite.BOUNDS, notNullValidator);
        // TODO Add bounds validator.

        sprite.addValidator(Sprite.IMAGE, notNullValidator);
        // TODO Add image validator.

        return sprite;
    }

    private void handleException(InvalidPropertyValueException e) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case UniqueNameValidator.NAME_NOT_UNIQUE:
            message = NLS.bind(Messages.SPRITE_NAME_EXISTS, e.getValue());
            break;
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.SPRITE_NAME_INVALID,
                    SpriteConstants.MIN_NAME_LENGTH,
                    SpriteConstants.MAX_NAME_LENGTH);
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
