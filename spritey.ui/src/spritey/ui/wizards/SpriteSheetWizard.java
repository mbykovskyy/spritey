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

import spritey.ui.Messages;

/**
 * A wizard for creating a sprite sheet.
 */
public class SpriteSheetWizard extends Wizard {

    NewSheetPage newSheetPage;

    /**
     * Creates a new instance of SpriteSheetWizard.
     */
    public SpriteSheetWizard() {
        newSheetPage = new NewSheetPage();
    }

    @Override
    public void addPages() {
        setWindowTitle(Messages.SPRITE_SHEET_WIZARD_TITLE);
        setNeedsProgressMonitor(true);

        addPage(newSheetPage);
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
