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
import spritey.core.Node;
import spritey.ui.Messages;

/**
 * A main wizard page for creating a new group.
 */
public class NewGroupPage extends WizardPage {

    public static final String NAME = "NEW_GROUP";

    private Group group;
    private Node parent;
    private Text nameText;

    /**
     * Creates a new group main wizard page.
     * 
     * @param parent
     *        the node to which a new group should be added.
     */
    public NewGroupPage(Node parent) {
        super(NAME);
        setTitle(Messages.NEW_GROUP_PAGE_TITLE);
        setDescription(Messages.NEW_GROUP_PAGE_DESCRIPTION);

        group = new Group();
        this.parent = parent;
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
        Label commentLabel = new Label(parent, SWT.NONE);
        commentLabel.setText(Messages.NEW_GROUP_NAME);
        commentLabel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_BEGINNING));

        nameText = new Text(parent, SWT.BORDER);
        nameText.setText(group.getName());
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
        String name = nameText.getText();

        try {
            group.setName(name);

            if (parent.contains(name)) {
                setErrorMessage(NLS.bind(spritey.core.Messages.NAME_NOT_UNIQUE,
                        name, parent.getName()));
                setPageComplete(false);
            } else {
                setErrorMessage(null);
                setPageComplete(true);
            }
        } catch (IllegalArgumentException e) {
            setErrorMessage(e.getMessage());
            setPageComplete(false);
        }
    }

    /**
     * Returns an instance of a new group.
     * 
     * @return a new group.
     */
    public Node getGroup() {
        return group;
    }

}
