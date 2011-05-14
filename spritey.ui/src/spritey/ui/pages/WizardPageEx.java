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

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;

/**
 * An action friendly wizard page that provides methods to allow actions gain
 * access to page container for long-running operations and page validation.
 */
public abstract class WizardPageEx extends WizardPage {

    /**
     * Creates a new instance of SpriteyWizardPage.
     * 
     * @param pageName
     *        the name of the page.s
     */
    protected WizardPageEx(String pageName) {
        super(pageName);
    }

    @Override
    public IWizardContainer getContainer() {
        return super.getContainer();
    }

    /**
     * Clients should implement this method to provide page validation logic.
     */
    public abstract void validatePage();

}
