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
package spritey.rcp.wizards;

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

/**
 * A main wizard page for saving sprite sheet.
 */
public class SaveAsMainPage extends WizardPage {

    static final String SAVE_AS_PAGE_NAME = "Main";
    static final String SAVE_AS_PAGE_TITLE = "Save As";
    static final String SAVE_AS_ENTER_NAME = "Please enter a file name.";
    static final String SAVE_AS_ENTER_LOCATION = "Please enter a destination directory.";
    static final String SAVE_AS_FINISH = "Save sprite sheet to disk.";

    static final String SAVE_AS_NAME_INVALID = "Name contains illegal characters.";
    static final String SAVE_AS_LOCATION_INVALID = "Location does not exist.";

    static final String NAME_LABEL = "Name:";
    static final String LOCATION_LABEL = "Location:";
    static final String IMAGE_LABEL = "Image:";
    static final String METADATA_LABEL = "Metadata:";
    static final String BROWSE_LABEL = "Browse";
    static final String OVERWRITE_LABEL = "Overwrite existing files without warning";

    static final String[] IMAGE_FORMATS = { "BMP", "GIF", "PNG" };
    static final String[] METADATA_FORMATS = { "XML" };

    private Label nameLabel;
    private Text nameText;

    private Label locationLabel;
    private Text locationText;
    private Button browseButton;

    private Label imageLabel;
    private Combo imageCombo;

    private Label metadataLabel;
    private Combo metadataCombo;

    private Button overwriteButton;

    private DirectoryDialog dialog;

    /**
     * Creates a save as main wizard page.
     */
    public SaveAsMainPage() {
        super(SAVE_AS_PAGE_NAME);
        initialize();
    }

    private void initialize() {
        setTitle(SAVE_AS_PAGE_TITLE);
        setDescription(SAVE_AS_ENTER_NAME);
        setPageComplete(false);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));

        nameLabel = new Label(container, SWT.NONE);
        nameLabel.setText(NAME_LABEL);

        nameText = new Text(container, SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        nameText.setLayoutData(data);
        nameText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });

        locationLabel = new Label(container, SWT.NONE);
        locationLabel.setText(LOCATION_LABEL);

        locationText = new Text(container, SWT.BORDER);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        locationText.setLayoutData(data);
        locationText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });

        browseButton = new Button(container, SWT.NONE);
        browseButton.setText(BROWSE_LABEL);
        browseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowseButtonPressed();
            }
        });

        imageLabel = new Label(container, SWT.NONE);
        imageLabel.setText(IMAGE_LABEL);

        imageCombo = new Combo(container, SWT.READ_ONLY);
        imageCombo.setItems(IMAGE_FORMATS);
        imageCombo.select(0);
        data = new GridData();
        data.horizontalSpan = 2;
        imageCombo.setLayoutData(data);

        metadataLabel = new Label(container, SWT.NONE);
        metadataLabel.setText(METADATA_LABEL);

        metadataCombo = new Combo(container, SWT.READ_ONLY);
        metadataCombo.setItems(METADATA_FORMATS);
        metadataCombo.select(0);
        data = new GridData();
        data.horizontalSpan = 2;
        metadataCombo.setLayoutData(data);

        overwriteButton = new Button(container, SWT.CHECK | SWT.LEFT);
        overwriteButton.setText(OVERWRITE_LABEL);
        data = new GridData(GridData.GRAB_VERTICAL
                | GridData.VERTICAL_ALIGN_END);
        data.horizontalSpan = 3;
        overwriteButton.setLayoutData(data);

        setControl(container);
    }

    /**
     * Validates page fields.
     */
    protected void validate() {
        String name = nameText.getText();
        String location = locationText.getText();

        if (name.isEmpty()) {
            setErrorMessage(null);
            setDescription(SAVE_AS_ENTER_NAME);
            setPageComplete(false);
        } else if (!isNameValid(name)) {
            if ((null == getErrorMessage())
                    || !getErrorMessage().equals(SAVE_AS_NAME_INVALID)) {
                setErrorMessage(SAVE_AS_NAME_INVALID);
                setPageComplete(false);
            }
        } else if (location.isEmpty()) {
            setErrorMessage(null);
            setDescription(SAVE_AS_ENTER_LOCATION);
            setPageComplete(false);
        } else if (!new File(location).isDirectory()) {
            if ((null == getErrorMessage())
                    || !getErrorMessage().equals(SAVE_AS_LOCATION_INVALID)) {
                setErrorMessage(SAVE_AS_LOCATION_INVALID);
                setPageComplete(false);
            }
        } else {
            setErrorMessage(null);
            setDescription(SAVE_AS_FINISH);
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
     * Specifies whether operation should overwrite existing files without a
     * warning.
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
