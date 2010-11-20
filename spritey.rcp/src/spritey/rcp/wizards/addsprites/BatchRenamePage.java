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

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.Validator;
import spritey.rcp.Messages;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.Root;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.views.navigator.SpriteTreeContentProvider;

/**
 * A wizard page for renaming models.
 */
public class BatchRenamePage extends WizardPage {

    public static final String NAME = "BATCH_RENAME";

    static final int ORIGINAL_NAME_COLUMN_WEIGHT = 50;
    static final int NEW_NAME_COLUMN_WEIGHT = 50;

    protected TreeViewer tree;

    private Button expandAllButton;
    private Button collapseAllButton;

    protected Model root;
    protected NewNameValidator newNameValidator;

    /**
     * Creates a new instance of BatchRenamePage.
     * 
     * @param root
     *        the root of the tree to rename nodes.
     */
    public BatchRenamePage(Model root) {
        super(NAME);
        this.root = root;

        setTitle(Messages.BATCH_RENAME_TITLE);
        setDescription(Messages.BATCH_RENAME_DESCRIPTION);
    }

    @Override
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        setControl(container);

        createPreviewTree(container);
        createViewControls(container);
    }

    /**
     * Creates the preview tree table.
     * 
     * @param parent
     *        the parent composite.
     */
    protected void createPreviewTree(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        TreeColumnLayout layout = new TreeColumnLayout();
        container.setLayout(layout);

        tree = new TreeViewer(container, SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL | SWT.FULL_SELECTION);
        tree.getTree().setLinesVisible(true);
        tree.getTree().setHeaderVisible(true);

        TreeViewerColumn column = new TreeViewerColumn(tree, SWT.NONE);
        column.getColumn().setText(Messages.BATCH_RENAME_ORIGINAL);

        layout.setColumnData(column.getColumn(), new ColumnWeightData(
                ORIGINAL_NAME_COLUMN_WEIGHT));

        column = new TreeViewerColumn(tree, SWT.NONE);
        column.getColumn().setText(Messages.BATCH_RENAME_NEW);
        column.setEditingSupport(new NewNameEditingSupport(tree) {
            @Override
            protected void setValue(Object element, Object value) {
                super.setValue(element, value);
                validateTree(root);
            }
        });

        layout.setColumnData(column.getColumn(), new ColumnWeightData(
                NEW_NAME_COLUMN_WEIGHT));

        tree.setContentProvider(new SpriteTreeContentProvider());
        tree.setLabelProvider(new RenamePreviewTreeLabelProvider());
        tree.setInput(root);
        tree.expandAll();
    }

    /**
     * Creates the expand and collapse all buttons.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createViewControls(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 0;
        container.setLayout(layout);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        container.setLayoutData(data);

        expandAllButton = new Button(container, SWT.NONE);
        expandAllButton.setText(Messages.BATCH_RENAME_EXPAND_ALL);
        expandAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tree.expandAll();
            }
        });
        setButtonLayoutData(expandAllButton);

        collapseAllButton = new Button(container, SWT.NONE);
        collapseAllButton.setText(Messages.BATCH_RENAME_COLLAPSE_ALL);
        collapseAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tree.collapseAll();
            }
        });
        setButtonLayoutData(collapseAllButton);
    }

    /**
     * Validates the specified tree by traversing the tree and sets the error
     * message accordingly.
     * 
     * @param root
     *        the root of the tree.
     * @return <code>true</code> if all tree nodes are valid, otherwise
     *         <code>false</code>.
     */
    protected boolean validateTree(Model root) {
        if (validate(root)) {
            setErrorMessage(null);
            setPageComplete(true);
        }
        return isPageComplete();
    }

    /**
     * Recursively validates models' NEW_NAME property starting from the
     * specified model.
     * 
     * @param model
     *        the model to validate.
     * @return <code>true</code> if the NEW_NAME is valid, otherwise
     *         <code>false</code>.
     */
    protected boolean validate(Model model) {
        if (null == model) {
            // TODO Log unexpected condition.
            setErrorMessage(Messages.INTERNAL_ERROR);
            setPageComplete(false);
            return false;
        } else if ((model instanceof Group) || (model instanceof Sprite)) {
            boolean isGroup = model instanceof Group;
            int newNameId = isGroup ? GroupConstants.NEW_NAME
                    : SpriteConstants.NEW_NAME;
            String newName = (String) model.getProperty(newNameId);

            Validator validator = new NewNameValidator(model);
            if (!validator.isValid(newName)) {
                updateErrorMessage(validator.getErrorCode(), newName, model);
                return false;
            }
        }

        for (Model child : model.getChildren()) {
            if (!validate(child)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the error message and disables the finish button.
     * 
     * @param errorCode
     *        the validation error code.
     * @param value
     *        the invalid value.
     * @param model
     *        the model being validated.
     */
    protected void updateErrorMessage(int errorCode, Object value, Model model) {
        String message = Messages.INTERNAL_ERROR;
        boolean isGroup = model instanceof Group;

        switch (errorCode) {
        case StringLengthValidator.TOO_LONG:
        case StringLengthValidator.TOO_SHORT:
            if (isGroup) {
                message = NLS.bind(Messages.GROUP_NAME_INVALID_LENGTH,
                        Group.MIN_NAME_LENGTH, Group.MAX_NAME_LENGTH);
            } else {
                message = NLS.bind(Messages.SPRITE_NAME_INVALID_LENGTH,
                        Sprite.MIN_NAME_LENGTH, Sprite.MAX_NAME_LENGTH);
            }
            break;
        case NewNameValidator.NAME_NOT_UNIQUE:
            Model parent = model.getParent();

            if ((parent instanceof Root) || (parent instanceof Sheet)) {
                message = isGroup ? Messages.BATCH_RENAME_SHEET_CONTAINS_GROUP
                        : Messages.BATCH_RENAME_SHEET_CONTAINS_SPRITE;
                message = NLS.bind(message, value);
            } else {
                message = isGroup ? Messages.GROUP_NAME_EXISTS
                        : Messages.SPRITE_NAME_EXISTS;
                message = NLS.bind(message, value,
                        parent.getProperty(Group.NAME));
            }
            break;
        default:
            // TODO Log unexpected condition.
            break;
        }
        setErrorMessage(message);
        setPageComplete(false);
    }

}
