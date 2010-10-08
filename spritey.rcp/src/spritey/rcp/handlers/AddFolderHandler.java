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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.filter.InvisibleSpriteFilter;
import spritey.core.node.Node;
import spritey.core.node.NodeFactory;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.utils.ImageFactory;

/**
 * Handler for handling the addition of a group of sprites.
 */
public class AddFolderHandler extends AbstractHandler implements IHandler {

    private ImageFactory imageFactory;

    private List<IStatus> errorMessages;

    public AddFolderHandler() {
        imageFactory = new ImageFactory();
        errorMessages = new ArrayList<IStatus>();
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
        // This handler remains disabled until root node is created, however,
        // better be safe than sorry.
        if (null == SpriteyPlugin.getDefault().getRootNode()) {
            return null;
        }

        Shell shell = HandlerUtil.getActiveShell(event);
        DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
        final String folder = dialog.open();

        if (null != folder) {
            loadFiles(new File(folder), new ProgressMonitorDialog(shell));

            if (!errorMessages.isEmpty()) {
                displayErrorMessages(shell);
            }
        }

        return null;
    }

    /**
     * Displays accumulated error messages that occurred while importing
     * sprites.
     * 
     * @param shell
     *        the parent shell.
     */
    private void displayErrorMessages(Shell shell) {
        IStatus status = new MultiStatus("unknown", 0,
                errorMessages.toArray(new IStatus[errorMessages.size()]),
                Messages.SPRITES_IMPORT_FAILED, null);
        ErrorDialog.openError(shell, Messages.ADD_SPRITES, null, status);
        errorMessages.clear();
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootNode().isLeaf();
    }

    /**
     * Imports sprites from the specified directory. A group will be created for
     * each sub-directory.
     * 
     * @param root
     *        the parent directory to load sprites from.
     * @param dialog
     *        the progress dialog to display while sprites will be loading.
     */
    private void loadFiles(final File root, final ProgressMonitorDialog dialog) {
        IRunnableWithProgress progress = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException, InterruptedException {
                monitor.beginTask(Messages.ADDING_SPRITES,
                        IProgressMonitor.UNKNOWN);
                final Node branchRoot = createNode(root, monitor);

                if (monitor.isCanceled()) {
                    // TODO Created sprites need to be destroyed to free Image
                    // handles.

                    errorMessages.clear();
                } else if (null != branchRoot) {
                    monitor.beginTask(Messages.PACKING_SPRITES,
                            IProgressMonitor.UNKNOWN);

                    final SpriteyPlugin plugin = SpriteyPlugin.getDefault();
                    Node sheetNode = plugin.getRootNode().getChildren()[0];

                    if (sheetNode.addChild(branchRoot)) {
                        plugin.getPacker().pack(sheetNode, false);
                        checkForInvisibleSprites(branchRoot);

                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                                plugin.getViewUpdater().refreshViews();
                            }
                        });
                    } else {
                        String message = NLS.bind(Messages.GROUP_NAME_EXISTS,
                                root.getName());
                        errorMessages.add(new Status(IStatus.WARNING,
                                "unknown", message));
                    }
                }

                monitor.done();
            }
        };

        try {
            dialog.run(true, true, progress);
        } catch (InvocationTargetException e) {
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
        }
    }

    /**
     * Checks for sprites that do not fit into sprite sheet and adds a warning
     * messages for each match.
     * 
     * @param branchRoot
     *        the root of the branch where new sprites were added.
     */
    private void checkForInvisibleSprites(Node branchRoot) {
        Sprite[] sprites = new InvisibleSpriteFilter().filterModels(branchRoot);

        for (Sprite sprite : sprites) {
            String message = NLS.bind(Messages.SPRITE_DOES_NOT_FIT,
                    sprite.getProperty(Sprite.NAME));
            errorMessages.add(new Status(IStatus.WARNING, "unknown", message));
        }
    }

    /**
     * Creates a node with attached model for the specified file. If file is a
     * directory a group node is created, otherwise sprite node is created.
     * 
     * @param file
     *        the file to create a node for.
     * @param monitor
     *        the progress dialog to display while creating a node.
     * @return an instance of a node with attached model.
     */
    private Node createNode(File file, IProgressMonitor monitor) {
        monitor.subTask(file.getAbsolutePath());

        if (file.isDirectory()) {
            return createGroupNode(file, monitor);
        }
        return createSpriteNode(file);
    }

    /**
     * Creates a node and attaches a sprite model.
     * 
     * @param file
     *        the file to create a sprite node for.
     * @return an instance of a node with a sprite model attached.
     */
    private Node createSpriteNode(File file) {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        NodeFactory nodeFactory = plugin.getNodeFactory();
        Node node = null;

        Model data = createSprite(file, modelFactory);

        if (null != data) {
            node = nodeFactory.createNode(file.getName());
            node.setModel(data);
        }

        return node;
    }

    /**
     * Creates a node and attaches a group model.
     * 
     * @param file
     *        the file to create a group node for.
     * @param monitor
     *        the progress monitor to give the user feedback about the progress.
     * @return an instance of a node with a group model attached.
     */
    private Node createGroupNode(File file, IProgressMonitor monitor) {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        NodeFactory nodeFactory = plugin.getNodeFactory();
        Node node = null;

        Model data = createGroup(file.getName(), modelFactory);

        if (null != data) {
            // Traverse sub-directories
            for (File child : file.listFiles()) {
                if (monitor.isCanceled()) {
                    break;
                } else if (isAccessible(child)) {
                    Node childNode = createNode(child, monitor);

                    if (null != childNode) {
                        // Create parent node only if there's at least one child
                        if (null == node) {
                            node = nodeFactory.createNode(file.getName());
                            node.setModel(data);
                        }

                        node.addChild(childNode);
                        // It is very unlikely that a child node will not be
                        // added to a parent node here as operating systems do
                        // not allow duplicate file names within one directory.
                    }
                } else {
                    String message = NLS.bind(Messages.DIRECTORY_INACCESSIBLE,
                            child.getAbsolutePath());
                    errorMessages.add(new Status(IStatus.WARNING, "unknown",
                            message));
                }
            }
        }

        return node;
    }

    /**
     * Returns <code>true</code> if the specified file is accessible, otherwise
     * <code>false</code>.
     * 
     * @param file
     *        the file to test for accessibility.
     * @return <code>true</code> if the specified file is accessible.
     */
    private boolean isAccessible(File file) {
        // AccessController.checkPermission() does not work. This, on the other
        // hand, is short and works perfectly.
        return file.isFile() || (file.listFiles() != null);
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
     * Creates and populates sprite model.
     * 
     * @param file
     *        file to load image data from.
     * @param modelFactory
     *        factory to use to create sprite model.
     * 
     * @return instance of sprite model.
     */
    private Model createSprite(File file, ModelFactory modelFactory) {
        ImageData imageData = loadImage(file);

        if (null == imageData) {
            String message = NLS.bind(Messages.UNABLE_TO_LOAD_IMAGE,
                    file.getAbsolutePath());
            errorMessages.add(new Status(IStatus.WARNING, "unknown", message));
            return null;
        }

        Model data = modelFactory.createSprite();

        try {
            data.setProperty(Sprite.IMAGE, imageFactory.createImage(imageData));
            data.setProperty(Sprite.NAME, file.getName());
            data.setProperty(Sprite.BOUNDS, new Rectangle(
                    Sprite.DEFAULT_BOUNDS.x, Sprite.DEFAULT_BOUNDS.y,
                    imageData.width, imageData.height));
        } catch (InvalidPropertyValueException e) {
            handleSpriteException(e);
            return null;
        }

        return data;
    }

    /**
     * Creates and populates group model.
     * 
     * @param name
     *        name to give this group.
     * @param modelFactory
     *        factory to use to create group model.
     * 
     * @return instance of sprite group.
     */
    private Model createGroup(String name, ModelFactory modelFactory) {
        Model data = modelFactory.createGroup();

        try {
            data.setProperty(Group.NAME, name);
        } catch (InvalidPropertyValueException e) {
            handleGroupException(e);
            return null;
        }

        return data;
    }

    /**
     * Adds an error message about fired exception to a list of other messages.
     * This method only deals with sprite specific exceptions.
     * 
     * @param e
     *        the exception to add an error message for.
     */
    private void handleSpriteException(InvalidPropertyValueException e) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case UniqueNameValidator.NAME_NOT_UNIQUE:
            message = NLS.bind(Messages.SPRITE_NAME_EXISTS, e.getValue());
            break;
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.SPRITE_NAME_INVALID,
                    Sprite.MIN_NAME_LENGTH, Sprite.MAX_NAME_LENGTH);
            break;
        case NotNullValidator.NULL:
        case TypeValidator.NOT_TYPE:
        default:
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
            break;
        }
        errorMessages.add(new Status(IStatus.WARNING, "unknown", message));
    }

    /**
     * Adds an error message about fired exception to a list of other messages.
     * This method only deals with group specific exceptions.
     * 
     * @param e
     *        the exception to add an error message for.
     */
    private void handleGroupException(InvalidPropertyValueException e) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case UniqueNameValidator.NAME_NOT_UNIQUE:
            message = NLS.bind(Messages.GROUP_NAME_EXISTS, e.getValue());
            break;
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.GROUP_NAME_INVALID,
                    Group.MIN_NAME_LENGTH, Group.MAX_NAME_LENGTH);
            break;
        case NotNullValidator.NULL:
        case TypeValidator.NOT_TYPE:
        default:
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
            break;
        }
        errorMessages.add(new Status(IStatus.WARNING, "unknown", message));
    }

}
