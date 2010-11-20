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
package spritey.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.rcp.SpriteyPlugin;
import spritey.rcp.wizards.addsprites.AddSpritesWizard;

/**
 * Handling the addition of a new sprite to a sprite sheet.
 */
public class AddSpritesHandler extends AbstractHandler implements IHandler {

    private Shell shell;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Ideally this handler will not be invoked as the command bound to
        // this handler will be disabled until root node is created.
        if (null == SpriteyPlugin.getDefault().getRootModel()) {
            return null;
        }

        shell = HandlerUtil.getActiveShell(event);
        WizardDialog dialog = new WizardDialog(shell, new AddSpritesWizard());
        dialog.setPageSize(750, 495);
        dialog.open();
        return null;
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootModel().isLeaf();
    }

}
