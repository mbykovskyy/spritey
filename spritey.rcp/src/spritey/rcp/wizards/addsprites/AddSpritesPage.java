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

import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.rcp.Messages;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.Root;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.dialogs.ImageFileDialog;
import spritey.rcp.operations.LoadSpritesOperation;
import spritey.rcp.utils.Converter;
import spritey.rcp.utils.ImageFactory;
import spritey.rcp.views.navigator.SpriteTreeContentProvider;

/**
 * The page for importing sprites from the file system into sprite sheet.
 */
public class AddSpritesPage extends WizardPage {

    public static final String NAME = "ADD_SPRITES";

    private Text directoryText;
    private Button browseDirectoryButton;
    private DirectoryDialog browseDialog;

    private Text filesText;
    private Button browseFilesButton;
    private ImageFileDialog fileDialog;

    private AddSpritesPreviewTree tree;

    private Button selectAllButton;
    private Button deselectAllButton;

    private ImageFactory imageFactory;
    private AddSpritesBatchRenamePage nextPage;

    /**
     * Creates a new instance of AddSpritesPage.
     * 
     * @param nextPage
     *        the next page.
     */
    public AddSpritesPage(AddSpritesBatchRenamePage nextPage) {
        super(NAME);
        this.nextPage = nextPage;
        imageFactory = new ImageFactory();

        setTitle(Messages.ADD_SPRITES_TITLE);
        setDescription(Messages.ADD_SPRITES_SELECT_DIRECTORY_ERROR);
    }

    /**
     * Returns the root node of the imported model tree.
     * 
     * @return the root model.
     */
    public Model getRootModel() {
        return tree.getInput();
    }

    /**
     * <p>
     * Puts the generated sprite tree into its final state by removing all
     * unselected nodes and storing all arbitrary/temporary information under
     * persistent properties.
     * </p>
     * <p>
     * <b>IMPORTANT:</b> This method should only be called when the generated
     * branch is about to be added to a sprite sheet.
     * </p>
     * 
     * @return an instance of a root model.
     */
    public Model finalizeRootModel() {
        finalizeModel(getRootModel());
        return getRootModel();
    }

    /**
     * Copies all arbitrary/temporary property values to persistent properties.
     * 
     * @param model
     *        the model to finalize.
     */
    private void finalizeProperties(Model model) {
        if ((model instanceof Sprite) || (model instanceof Group)) {
            boolean isSprite = model instanceof Sprite;
            int originalNameId = isSprite ? Sprite.NAME : Group.NAME;
            int newNameId = isSprite ? SpriteConstants.NEW_NAME
                    : GroupConstants.NEW_NAME;
            String originalName = (String) model.getProperty(originalNameId);
            String newName = (String) model.getProperty(newNameId);

            try {
                if ((null != newName) && !originalName.equals(newName)) {
                    model.setProperty(originalNameId, newName);
                }

                if (isSprite) {
                    ImageData data = (ImageData) model
                            .getProperty(SpriteConstants.IMAGE_DATA);

                    model.setProperty(Sprite.BOUNDS, new Rectangle(
                            Sprite.DEFAULT_BOUNDS.x, Sprite.DEFAULT_BOUNDS.y,
                            data.width, data.height));
                    model.setProperty(Sprite.IMAGE,
                            Converter.toBufferedImage(data));
                    model.setProperty(SpriteConstants.SWT_IMAGE,
                            imageFactory.createImage(data));
                }
            } catch (InvalidPropertyValueException e) {
                // Do nothing.
            }
        }
    }

    /**
     * Recursively finalizes models starting from the specified model. The model
     * will be removed from the tree if it is not checked. Properties of
     * "survived" models will be finalized.
     * 
     * @param model
     *        the model to finalize.
     */
    private void finalizeModel(Model model) {
        if (!(model instanceof Root) && !isChecked(model)) {
            model.getParent().removeChild(model);
        } else {
            finalizeProperties(model);

            for (Model child : model.getChildren()) {
                finalizeModel(child);
            }
        }
    }

    @Override
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));
        setControl(container);

        createBrowseDirectoryControls(container);
        createBrowseFilesControls(container);
        createPreviewTree(container);
        createSelectionControls(container);
        validate();
    }

    /**
     * Creates controls for specifying directory.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createBrowseDirectoryControls(Composite parent) {
        Button radio = new Button(parent, SWT.RADIO);
        radio.setText(Messages.ADD_SPRITES_FROM_DIRECTORY);
        radio.setSelection(true);
        radio.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setBrowseDirectoryControlsEnabled(true);
                setBrowseFilesControlsEnabled(false);
                validate();
            }
        });

        directoryText = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        directoryText.setLayoutData(data);
        directoryText.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    // Validate directory field before trying to load its files.
                    if (validate()) {
                        loadFiles(new File(directoryText.getText().trim()));
                    } else {
                        tree.setInput(null);
                        setSelectionControlsEnabled(false);
                    }
                    validate();
                }
            }
        });

        browseDirectoryButton = new Button(parent, SWT.NONE);
        browseDirectoryButton.setText(Messages.ADD_SPRITES_BROWSE);
        browseDirectoryButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowseDirectoryButton();
                validate();
            }
        });
        setButtonLayoutData(browseDirectoryButton);
    }

    /**
     * Creates controls for specifying a list of files.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createBrowseFilesControls(Composite parent) {
        Button radio = new Button(parent, SWT.RADIO);
        radio.setText(Messages.ADD_SPRITES_SELECT_FILES);
        radio.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setBrowseFilesControlsEnabled(true);
                setBrowseDirectoryControlsEnabled(false);
                validate();
            }
        });

        filesText = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        filesText.setLayoutData(data);
        filesText.setEnabled(false);
        filesText.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    String text = filesText.getText().trim();
                    if (text.isEmpty()) {
                        tree.setInput(null);
                        setSelectionControlsEnabled(false);
                    } else {
                        String[] paths = text.split("\\"
                                + File.pathSeparatorChar);
                        File[] files = new File[paths.length];
                        for (int i = 0; i < paths.length; ++i) {
                            files[i] = new File(paths[i]);
                        }
                        loadFiles(files);
                    }
                    validate();
                }
            }
        });

        browseFilesButton = new Button(parent, SWT.NONE);
        browseFilesButton.setText(Messages.ADD_SPRITES_BROWSE);
        browseFilesButton.setEnabled(false);
        browseFilesButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowseFilesButton();
                validate();
            }
        });
        setButtonLayoutData(browseFilesButton);
    }

    /**
     * Creates a checkbox preview tree.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createPreviewTree(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL);
        data.horizontalSpan = 3;
        container.setLayoutData(data);

        tree = new AddSpritesPreviewTree(container);

        Device device = parent.getShell().getDisplay();

        Color gray = device.getSystemColor(SWT.COLOR_GRAY);
        tree.setLabelProvider(new AddSpritesPreviewTreeLabelProvider(gray));
        tree.setContentProvider(new SpriteTreeContentProvider());
        tree.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                validate();
            }
        });
    }

    /**
     * Creates select all and deselect all buttons.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createSelectionControls(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 0;
        container.setLayout(layout);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.horizontalSpan = 3;
        container.setLayoutData(data);

        selectAllButton = new Button(container, SWT.NONE);
        selectAllButton.setText(Messages.ADD_SPRITES_SELECT_ALL);
        selectAllButton.setEnabled(false);
        selectAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tree.selectAll(true);
                validate();
            }
        });
        setButtonLayoutData(selectAllButton);

        deselectAllButton = new Button(container, SWT.NONE);
        deselectAllButton.setText(Messages.ADD_SPRITES_DESELECT_ALL);
        deselectAllButton.setEnabled(false);
        deselectAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tree.selectAll(false);
                validate();
            }
        });
        setButtonLayoutData(deselectAllButton);
    }

    /**
     * Enables or disables controls for specifying directory.
     * 
     * @param enabled
     *        if <code>true</code> controls will be enabled, otherwise controls
     *        will be disabled.
     */
    private void setBrowseDirectoryControlsEnabled(boolean enabled) {
        directoryText.setEnabled(enabled);
        browseDirectoryButton.setEnabled(enabled);
    }

    /**
     * Enables or disables controls for specifying a list of files.
     * 
     * @param enabled
     *        if <code>true</code> controls will be enabled, otherwise controls
     *        will be disabled.
     */
    private void setBrowseFilesControlsEnabled(boolean enabled) {
        filesText.setEnabled(enabled);
        browseFilesButton.setEnabled(enabled);
    }

    /**
     * Enables or disables selection controls.
     * 
     * @param enabled
     *        if <code>true</code> controls will be enabled, otherwise controls
     *        will be disabled.
     */
    private void setSelectionControlsEnabled(boolean enabled) {
        selectAllButton.setEnabled(enabled);
        deselectAllButton.setEnabled(enabled);
    }

    /**
     * Loads the specified files and populates the preview tree.
     * 
     * @param files
     *        a list of files to load.
     */
    protected void loadFiles(File... files) {
        try {
            LoadSpritesOperation addOp = new LoadSpritesOperation(files);
            getContainer().run(true, true, addOp);
            final Model root = addOp.getRootModel();

            if (root.getChildren().length > 0) {
                tree.setInput(root);
                tree.selectAll(true);
                setSelectionControlsEnabled(true);
            }
        } catch (InterruptedException e) {
            // Do nothing.
        } catch (InvocationTargetException e) {
            MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
                    Messages.ADD_SPRITES, Messages.OPERATION_INVOCATION_ERROR,
                    SWT.SHEET);
            // TODO Log this exception.
            e.printStackTrace();
        }
    }

    /**
     * Handles browse directory button action.
     */
    protected void handleBrowseDirectoryButton() {
        if (null == browseDialog) {
            browseDialog = new DirectoryDialog(getShell(), SWT.OPEN | SWT.SHEET);
        }

        String file = browseDialog.open();
        if (file != null) {
            directoryText.setText(file);
            loadFiles(new File(file));
        }
    }

    /**
     * Handles browse files button action.
     */
    protected void handleBrowseFilesButton() {
        if (null == fileDialog) {
            fileDialog = new ImageFileDialog(getShell(), SWT.OPEN | SWT.SHEET
                    | SWT.MULTI);
        }

        String file = fileDialog.open();
        if (null != file) {
            StringBuilder builder = new StringBuilder();
            String[] filepaths = fileDialog.getFilePaths();
            File[] files = new File[filepaths.length];

            for (int i = 0; i < filepaths.length; ++i) {
                builder.append(filepaths[i]).append(File.pathSeparatorChar);
                files[i] = new File(filepaths[i]);
            }
            // Remove last path separator.
            builder.replace(builder.length() - 1, builder.length(), "");
            filesText.setText(builder.toString());
            loadFiles(files);
        }
    }

    /**
     * Validates page.
     */
    protected boolean validate() {
        if (directoryText.isEnabled()) {
            String directory = directoryText.getText().trim();
            if (directory.isEmpty()) {
                setErrorMessage(null);
                setMessage(Messages.ADD_SPRITES_SELECT_DIRECTORY_ERROR);
                setPageComplete(false);
                return false;
            } else if (!new File(directory).isDirectory()) {
                setErrorMessage(Messages.ADD_SPRITES_DIRECTORY_INVALID_ERROR);
                setPageComplete(false);
                return false;
            }
        } else if (filesText.isEnabled()) {
            String files = filesText.getText().trim();
            if (files.isEmpty()) {
                setErrorMessage(null);
                setMessage(Messages.ADD_SPRITES_SELECT_FILES_ERROR);
                setPageComplete(false);
                return false;
            }
        }

        if (null != getRootModel()) {
            if (!isAtleastOneChecked()) {
                setErrorMessage(Messages.ADD_SPRITES_NO_SPRITES_SELECTED_ERROR);
                setPageComplete(false);
                return false;
            } else if (validateNextPage()) {
                setErrorMessage(null);
                setMessage(Messages.ADD_SPRITES_ADD_SPRITES_TO_SHEET);
                setPageComplete(true);
            }
        }
        return true;
    }

    /**
     * Specifies whether the model is checked.
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

    /**
     * Specifies whether at least one model is checked.
     * 
     * @return <code>true</code> if at least one model is checked, otherwise
     *         <code>false</code>.
     */
    protected boolean isAtleastOneChecked() {
        // Only immediate children need to be checked as every node along the
        // path to the checked sprite gets checked.
        for (Model child : getRootModel().getChildren()) {
            if (isChecked(child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the next page is valid.
     * 
     * @return <code>true</code> if the next page is valid, otherwise
     *         <code>false</code>.
     */
    protected boolean validateNextPage() {
        if (!nextPage.validateTree(getRootModel())) {
            setErrorMessage(null);
            setMessage(nextPage.getErrorMessage() + " "
                    + Messages.ADD_SPRITES_FIX_CONFLICT, WARNING);
            setPageComplete(true);
            return false;
        }
        return true;
    }

}
