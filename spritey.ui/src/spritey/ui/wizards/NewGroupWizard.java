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
package spritey.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import spritey.core.Node;
import spritey.ui.Messages;
import spritey.ui.pages.NewGroupPage;

/**
 * A wizard for creating a new group.
 */
public class NewGroupWizard extends Wizard {

    private NewGroupPage mainPage;

    /**
     * Creates a new group wizard.
     * 
     * @param parent
     *        the node to which a new group should be added.
     */
    public NewGroupWizard(Node parent) {
        mainPage = new NewGroupPage(parent);

        setWindowTitle(Messages.NEW_GROUP_WIZARD_TITLE);
    }

    @Override
    public void addPages() {
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        return mainPage.isPageComplete();
    }

    /**
     * Returns an instance of a new group.
     * 
     * @return a new group.
     */
    public Node getGroup() {
        return mainPage.getGroup();
    }

}
