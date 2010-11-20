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

import org.eclipse.jface.wizard.Wizard;

import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.rcp.SpriteyPlugin;

/**
 * A wizard for creating a new sprite sheet.
 */
public class NewSpriteSheetWizard extends Wizard {

    static final String TITLE = "New Sprite Sheet";

    NewSpriteSheetMainPage mainPage;

    @Override
    public void addPages() {
        mainPage = new NewSpriteSheetMainPage();

        setWindowTitle(TITLE);
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();

        Model sheet = modelFactory.createSheet();
        mainPage.populateSheet(sheet);

        return plugin.getRootModel().addChildren(sheet).length == 0;
    }

}
