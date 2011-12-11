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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import spritey.core.Group;
import spritey.core.Node;
import spritey.core.Sprite;
import spritey.ui.InternalError;
import spritey.ui.Messages;

/**
 * A main wizard page for renaming nodes.
 */
public class RenamePage extends WizardPage {

    public static final String NAME = "RENAME";

    private Node node;
    private Text nameText;

    private String originalName;

    /**
     * Creates a new rename page.
     * 
     * @param node
     *        the node to rename.
     */
    public RenamePage(Node node) {
        super(NAME);

        if (node instanceof Sprite) {
            setTitle(Messages.RENAME_SPRITE_PAGE_TITLE);
        } else if (node instanceof Group) {
            setTitle(Messages.RENAME_GROUP_PAGE_TITLE);
        } else {
            throw new InternalError("Unexpected node " + node.getClass() + ".");
        }

        setDescription(Messages.RENAME_PAGE_DESCRIPTION);
        this.node = node;
        originalName = node.getName();
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));
        createNameControls(container);
        setControl(container);
        validatePage();
    }

    /**
     * Creates name controls.
     * 
     * @param parent
     *        the parent composite.
     */
    private void createNameControls(Composite parent) {
        Label nameLabel = new Label(parent, SWT.NONE);
        nameLabel.setText(Messages.RENAME_PAGE_NEW_NAME);
        nameLabel
                .setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        nameText = new Text(parent, SWT.BORDER);
        nameText.setText(node.getName());
        nameText.selectAll();
        nameText.setTextLimit(Node.MAX_NAME_LENGTH);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validatePage();

            }
        });
    }

    /**
     * Validates the text value and sets an error message accordingly.
     */
    private void validatePage() {
        try {
            String newName = nameText.getText();

            if (!newName.equals(originalName)) {
                // Temporarily change node name to verify that the new name is
                // valid and then change it back to original.
                node.setName(newName);
                node.setName(originalName);

                if (node instanceof Sprite) {
                    setDescription(Messages.RENAME_PAGE_RENAME_SPRITE);
                } else {
                    setDescription(Messages.RENAME_PAGE_RENAME_GROUP);
                }
                setPageComplete(true);
            } else {
                setDescription(Messages.RENAME_PAGE_DESCRIPTION);
                setPageComplete(false);
            }
            setErrorMessage(null);
        } catch (IllegalArgumentException e) {
            setErrorMessage(e.getMessage());
            setPageComplete(false);
        }
    }

    /**
     * Returns the new name.
     * 
     * @return the new name.
     */
    public String getNewName() {
        return nameText.getText();
    }

}
