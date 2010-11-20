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
package spritey.rcp.operations;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.validator.StringLengthValidator;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.Root;
import spritey.rcp.core.SpriteConstants;

/**
 * Operation for loading sprites.
 */
public class LoadSpritesOperation implements IRunnableWithProgress {

    private File[] files;
    private Model root;

    public LoadSpritesOperation(File... files) {
        this.files = files;
        root = new Root();
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        monitor.beginTask("", IProgressMonitor.UNKNOWN);

        for (File file : files) {
            processFile(root, file, monitor);
            if (monitor.isCanceled()) {
                break;
            }
        }

        monitor.done();
    }

    /**
     * Returns the root of the generated tree.
     * 
     * @return the root model.
     */
    public Model getRootModel() {
        return root;
    }

    private void processFile(Model parent, File file, IProgressMonitor monitor) {
        monitor.subTask(file.getAbsolutePath());

        Model model = null;
        Object errorMessage = null;

        if (monitor.isCanceled()) {
            return;
        } else if (file.isDirectory()) {
            model = createGroup(file);

            // Traverse sub-directories
            File[] children = file.listFiles();
            if (null != children) {
                for (File child : file.listFiles()) {
                    processFile(model, child, monitor);
                    if (monitor.isCanceled()) {
                        return;
                    }
                }
            }

            errorMessage = model.getProperty(GroupConstants.DISPLAY_ERROR);
        } else {
            model = createSprite(file);
            errorMessage = model.getProperty(SpriteConstants.DISPLAY_ERROR);
        }

        parent.addChildren(model);

        if ((null != parent.getProperty(GroupConstants.DISPLAY_ERROR))
                && (null == errorMessage)) {
            try {
                // Groups, by default, have error messages, in case no
                // images are found. However, when at least one valid image
                // is found the message can be set back to null.
                parent.setProperty(GroupConstants.DISPLAY_ERROR, null);
            } catch (InvalidPropertyValueException e) {
                // Do nothing.
            }
        }
    }

    /**
     * Tries to load the specified file as an image.
     * 
     * @param file
     *        the file to load.
     * @return image data if the specified file is an image, otherwise
     *         <code>null</code>.
     */
    private ImageData loadImage(File file) {
        ImageData image = null;

        try {
            image = new ImageLoader().load(file.getAbsolutePath())[0];
        } catch (SWTException e) {
            // Do nothing.
        }
        return image;
    }

    /**
     * Creates sprite model. Only name and arbitrary properties are set.
     * 
     * @param file
     *        the file to create a sprite from.
     * @return an instance of a sprite model.
     */
    private Model createSprite(File file) {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        Model data = modelFactory.createSprite();

        try {
            data.setProperty(Sprite.NAME, file.getName());
            data.setProperty(SpriteConstants.NEW_NAME, file.getName());

            if (!file.exists()) {
                data.setProperty(SpriteConstants.DISPLAY_ERROR,
                        Messages.DOES_NOT_EXIST);
            } else if (file.canRead()) {
                ImageData imageData = loadImage(file);
                if (null == imageData) {
                    data.setProperty(SpriteConstants.DISPLAY_ERROR,
                            Messages.UNABLE_TO_LOAD_IMAGE);
                } else {
                    data.setProperty(SpriteConstants.IMAGE_DATA, imageData);
                }
            } else {
                data.setProperty(SpriteConstants.DISPLAY_ERROR,
                        Messages.ACCESS_DENIED);
            }
        } catch (InvalidPropertyValueException e) {
            handleSpriteException(e, data);
        }

        return data;
    }

    private void handleSpriteException(InvalidPropertyValueException e,
            Model model) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case StringLengthValidator.TOO_LONG:
            message = null;
            String name = (String) e.getValue();
            String newName = name.substring(0, Sprite.MAX_NAME_LENGTH);
            try {
                model.setProperty(Sprite.NAME, newName);
                model.setProperty(SpriteConstants.NEW_NAME, newName);
            } catch (InvalidPropertyValueException ex) {
                handleSpriteException(ex, model);
            }
            break;
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.SPRITE_NAME_INVALID_LENGTH,
                    Sprite.MIN_NAME_LENGTH, Sprite.MAX_NAME_LENGTH);
            break;
        }

        Object error = model.getProperty(SpriteConstants.DISPLAY_ERROR);
        if ((null == error) && (null != message)) {
            try {
                model.setProperty(SpriteConstants.DISPLAY_ERROR, message);
            } catch (InvalidPropertyValueException ex) {
                // Do nothing.
            }
        }
    }

    /**
     * Creates group model. Only name and arbitrary properties are set.
     * 
     * @param file
     *        the file to create a group from.
     * @return an instance of a group model.
     */
    private Model createGroup(File file) {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        Model data = modelFactory.createGroup();

        try {
            data.setProperty(Group.NAME, file.getName());
            data.setProperty(GroupConstants.NEW_NAME, file.getName());

            if (!file.exists()) {
                data.setProperty(GroupConstants.DISPLAY_ERROR,
                        Messages.DOES_NOT_EXIST);
            } else if (!file.canRead() || (null == file.listFiles())) {
                data.setProperty(GroupConstants.DISPLAY_ERROR,
                        Messages.ACCESS_DENIED);
            } else {
                // Create a group with error to display. When a valid child
                // sprite is found the message is set to null.
                data.setProperty(GroupConstants.DISPLAY_ERROR,
                        Messages.NO_IMAGES_FOUND);
            }
        } catch (InvalidPropertyValueException e) {
            handleGroupException(e, data);
        }

        return data;
    }

    private void handleGroupException(InvalidPropertyValueException e,
            Model model) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case StringLengthValidator.TOO_LONG:
            message = null;
            String name = (String) e.getValue();
            String newName = name.substring(0, Group.MAX_NAME_LENGTH);
            try {
                model.setProperty(Group.NAME, newName);
                model.setProperty(GroupConstants.NEW_NAME, newName);
            } catch (InvalidPropertyValueException ex) {
                handleGroupException(ex, model);
            }
            break;
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.SPRITE_NAME_INVALID_LENGTH,
                    Group.MIN_NAME_LENGTH, Group.MAX_NAME_LENGTH);
            break;
        }

        Object error = model.getProperty(GroupConstants.DISPLAY_ERROR);
        if ((null == error) && (null != message)) {
            try {
                model.setProperty(GroupConstants.DISPLAY_ERROR, message);
            } catch (InvalidPropertyValueException ex) {
                // Do nothing.
            }
        }
    }

}
