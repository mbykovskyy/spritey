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
import spritey.core.Sheet;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.core.node.Node;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.UniqueNameValidator;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;

/**
 * A main wizard page for creating a new sprite sheet.
 */
public class NewGroupMainPage extends WizardPage {

    static final String NAME = "Main";
    static final String TITLE = "Group";
    static final String DESCRIPTION = "Create a new group.";

    static final String NAME_TEXT = "Name:";

    static final int NAME_TEXT_LIMIT = Group.MAX_NAME_LENGTH;

    static final String DEFAULT_COMMENT = Group.DEFAULT_NAME;

    private Text nameText;
    private ModifyListener modifyListener;

    /**
     * Creates a new group main wizard page.
     */
    public NewGroupMainPage() {
        super(NAME);
        initialize();
    }

    private void initialize() {
        setTitle(TITLE);
        setDescription(DESCRIPTION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
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
        commentLabel.setText(NAME_TEXT);
        commentLabel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_BEGINNING));

        nameText = new Text(parent, SWT.BORDER);
        nameText.setText(DEFAULT_COMMENT);
        nameText.selectAll();
        nameText.setTextLimit(NAME_TEXT_LIMIT);
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

        Node sheet = plugin.getRootNode().getChild(Sheet.DEFAULT_NAME);
        if (sheet.contains(value)) {
            setErrorMessage(NLS.bind(Messages.GROUP_NAME_EXISTS, value));
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
            // Log it since we don't expect this exception.
            e.printStackTrace();
            break;
        }
        setErrorMessage(message);
        setPageComplete(false);
    }

}
