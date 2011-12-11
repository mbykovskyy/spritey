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

import spritey.core.Group;
import spritey.core.Node;
import spritey.core.Sprite;
import spritey.ui.InternalError;
import spritey.ui.Messages;
import spritey.ui.pages.RenamePage;

/**
 * A wizard for renaming nodes in a sprite tree.
 */
public class RenameWizard extends Wizard {

    private RenamePage mainPage;
    private Node node;

    /**
     * Creates a new rename wizard.
     * 
     * @param node
     *        the node to rename.
     */
    public RenameWizard(Node node) {
        if (node instanceof Sprite) {
            setWindowTitle(Messages.RENAME_SPRITE_WIZARD_TITLE);
        } else if (node instanceof Group) {
            setWindowTitle(Messages.RENAME_GROUP_WIZARD_TITLE);
        } else {
            throw new InternalError("Unexpected node " + node.getClass() + ".");
        }

        mainPage = new RenamePage(node);
        this.node = node;
    }

    @Override
    public void addPages() {
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        String newName = mainPage.getNewName();

        try {
            node.setName(newName);
        } catch (IllegalArgumentException e) {
            // We shouldn't have end up here.
            throw new InternalError(newName + " was expected to be valid.");
        }
        return true;
    }

}
