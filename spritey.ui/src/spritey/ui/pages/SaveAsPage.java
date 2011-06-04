/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2011 Maksym Bykovskyy.
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
package spritey.ui.pages;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import spritey.ui.Messages;

/**
 * A wizard page for saving sprite sheet.
 */
public class SaveAsPage extends WizardPage {

    static final String NAME = "SAVE_AS";

    static final String[] IMAGE_FORMATS = { "BMP", "GIF", "PNG" };
    static final String[] METADATA_FORMATS = { "XML" };

    private Text nameText;
    private Text locationText;
    private Button overwriteButton;
    private Combo imageCombo;
    private Combo metadataCombo;
    private DirectoryDialog dialog;

    /**
     * Creates a new instance of SaveAsPage.
     */
    public SaveAsPage() {
        super(NAME);
        setTitle(Messages.SAVE_AS_PAGE_TITLE);
        setDescription(Messages.SAVE_AS_ENTER_NAME);
        setPageComplete(false);
    }

    /**
     * Validates page fields.
     */
    protected void validate() {
        String name = nameText.getText();
        String location = locationText.getText();

        if (name.isEmpty()) {
            setErrorMessage(null);
            setDescription(Messages.SAVE_AS_ENTER_NAME);
            setPageComplete(false);
        } else if (!isNameValid(name)) {
            if ((null == getErrorMessage())
                    || !getErrorMessage().equals(Messages.SAVE_AS_NAME_INVALID)) {
                setErrorMessage(Messages.SAVE_AS_NAME_INVALID);
                setPageComplete(false);
            }
        } else if (location.isEmpty()) {
            setErrorMessage(null);
            setDescription(Messages.SAVE_AS_ENTER_LOCATION);
            setPageComplete(false);
        } else if (!new File(location).isDirectory()) {
            if ((null == getErrorMessage())
                    || !getErrorMessage().equals(
                            Messages.SAVE_AS_LOCATION_INVALID)) {
                setErrorMessage(Messages.SAVE_AS_LOCATION_INVALID);
                setPageComplete(false);
            }
        } else {
            setErrorMessage(null);
            setDescription(Messages.SAVE_AS_FINISH);
            setPageComplete(true);
        }
    }

    /**
     * Returns <code>true</code> if the specified name does not contain any
     * illegal characters by creating a temporary file in the default temporary
     * directory.
     * 
     * @param name
     *        the name to verify.
     * @return <code>true</code> if name is valid, otherwise <code>false</code>.
     */
    private boolean isNameValid(String name) {
        try {
            File tmp = File.createTempFile("tmp" + name, null);

            if (tmp != null) {
                // Created successfully, filename is valid.
                tmp.delete();
                return true;
            }
        } catch (IOException e) {
            // Do nothing.
        }
        return false;
    }

    /**
     * Handles browse button action.
     */
    protected void handleBrowseButtonPressed() {
        if (null == dialog) {
            dialog = new DirectoryDialog(getShell(), SWT.SAVE | SWT.SHEET);
        }

        String file = dialog.open();
        if (file != null) {
            locationText.setText(file);
        }
    }

    /**
     * Creates name controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createNameControls(Composite parent) {
        Label nameLabel = new Label(parent, SWT.NONE);
        nameLabel.setText(Messages.SAVE_AS_PAGE_NAME);

        nameText = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        nameText.setLayoutData(data);
        nameText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
    }

    /**
     * Creates location controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createLocationControls(Composite parent) {
        Label locationLabel = new Label(parent, SWT.NONE);
        locationLabel.setText(Messages.SAVE_AS_PAGE_LOCATION);

        locationText = new Text(parent, SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        locationText.setLayoutData(data);
        locationText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });

        Button browseButton = new Button(parent, SWT.NONE);
        browseButton.setText(Messages.SAVE_AS_BROWSE);
        browseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowseButtonPressed();
            }
        });
        setButtonLayoutData(browseButton);
    }

    /**
     * Creates image controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createImageControls(Composite parent) {
        Label imageLabel = new Label(parent, SWT.NONE);
        imageLabel.setText(Messages.SAVE_AS_IMAGE);

        imageCombo = new Combo(parent, SWT.READ_ONLY);
        imageCombo.setItems(IMAGE_FORMATS);
        imageCombo.select(0);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        imageCombo.setLayoutData(data);
    }

    /**
     * Creates metadata controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createMetadataControls(Composite parent) {
        Label metadataLabel = new Label(parent, SWT.NONE);
        metadataLabel.setText(Messages.SAVE_AS_METADATA);

        metadataCombo = new Combo(parent, SWT.READ_ONLY);
        metadataCombo.setItems(METADATA_FORMATS);
        metadataCombo.select(0);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        metadataCombo.setLayoutData(data);
    }

    /**
     * Creates override confirmation controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createOverrideControls(Composite parent) {
        overwriteButton = new Button(parent, SWT.CHECK | SWT.LEFT);
        overwriteButton.setText(Messages.SAVE_AS_OVERWRITE_EXISTING_FILES);
        GridData data = new GridData();
        data.horizontalSpan = 3;
        data.verticalIndent = 10;
        overwriteButton.setLayoutData(data);
    }

    @Override
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));

        createNameControls(container);
        createLocationControls(container);
        createImageControls(container);
        createMetadataControls(container);
        createOverrideControls(container);
        setControl(container);
    }

    /**
     * Specifies whether existing files should be overwritten without a warning.
     * 
     * @return <code>true</code> if overwrite is checked.
     */
    public boolean isOverwrite() {
        return overwriteButton.getSelection();
    }

    /**
     * Returns a file to write sprite sheet image to.
     * 
     * @return an instance of a file to write to.
     */
    public File getImageFile() {
        return new File(locationText.getText(), nameText.getText() + "."
                + imageCombo.getText().toLowerCase());
    }

    /**
     * Returns a file to write sprite sheet configuration to.
     * 
     * @return an instance of a file to write to.
     */
    public File getMetadataFile() {
        return new File(locationText.getText(), nameText.getText() + "."
                + metadataCombo.getText().toLowerCase());
    }

}
