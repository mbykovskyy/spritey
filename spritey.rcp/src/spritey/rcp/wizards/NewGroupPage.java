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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.filter.ModelNameFilter;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.GroupConstants;

/**
 * A main wizard page for creating a new sprite sheet.
 */
public class NewGroupPage extends WizardPage {

    public static final String NAME = "NEW_GROUP";

    private Text nameText;
    private ModifyListener modifyListener;

    /**
     * Creates a new group main wizard page.
     */
    public NewGroupPage() {
        super(NAME);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.NEW_GROUP);
        setDescription(Messages.NEW_GROUP_CREATE_GROUP);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));
        createCommentControls(container);
        setControl(container);
        validateText();
    }

    /**
     * Creates comment control.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createCommentControls(Composite parent) {
        Label commentLabel = new Label(parent, SWT.NONE);
        commentLabel.setText(Messages.NEW_GROUP_NAME);
        commentLabel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_BEGINNING));

        nameText = new Text(parent, SWT.BORDER);
        nameText.setText(Group.DEFAULT_NAME);
        nameText.selectAll();
        nameText.setTextLimit(Group.MAX_NAME_LENGTH);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.addModifyListener(getModifyListener());
    }

    /**
     * Returns the modify listener.
     * 
     * @return an instance of modify listener.
     */
    private ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    validateText();
                }
            };
        }
        return modifyListener;
    }

    /**
     * Validates the text value and sets an error message accordingly.
     */
    private void validateText() {
        String value = nameText.getText();
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();

        try {
            // Create a temporary group model to validate text value.
            ModelFactory factory = plugin.getModelFactory();
            factory.createGroup().setProperty(Group.NAME, value);
        } catch (InvalidPropertyValueException ex) {
            handleException(ex);
            return;
        }

        Model sheet = plugin.getRootModel().getChildren()[0];
        if (new ModelNameFilter(value).filter(sheet.getChildren()).length > 0) {
            setErrorMessage(NLS.bind(
                    Messages.BATCH_RENAME_SHEET_CONTAINS_GROUP, value));
            setPageComplete(false);
        } else {
            setErrorMessage(null);
            setPageComplete(true);
        }
    }

    /**
     * Populates the specified group model with values from this wizard page.
     * 
     * @param group
     *        the model to populate.
     * @return <code>true</code> when populating model was successful.
     */
    public void populateGroup(Model group) {
        try {
            group.setProperty(Group.NAME, nameText.getText());
            group.setProperty(GroupConstants.NEW_NAME, nameText.getText());
        } catch (InvalidPropertyValueException e) {
            // Do nothing. This exception should never happen as we only allow
            // this method to be called when the text value is valid.
        }
    }

    /**
     * Sets an error message according to the specified exception.
     * 
     * @param e
     *        the exception that occurred.
     */
    private void handleException(InvalidPropertyValueException e) {
        String message = Messages.INTERNAL_ERROR;

        switch (e.getErrorCode()) {
        case UniqueNameValidator.NAME_NOT_UNIQUE:
            message = NLS.bind(Messages.BATCH_RENAME_SHEET_CONTAINS_GROUP,
                    e.getValue());
            break;
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            message = NLS.bind(Messages.GROUP_NAME_INVALID_LENGTH,
                    Group.MIN_NAME_LENGTH, Group.MAX_NAME_LENGTH);
            break;
        case NotNullValidator.NULL:
        case TypeValidator.NOT_TYPE:
        default:
            // Log it since we don't expect this exception.
            e.printStackTrace();
            break;
        }
        setErrorMessage(message);
        setPageComplete(false);
    }

}
