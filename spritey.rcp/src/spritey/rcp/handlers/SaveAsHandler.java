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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.rcp.SpriteyPlugin;
import spritey.rcp.dialogs.StaticWizardDialog;
import spritey.rcp.wizards.SaveAsWizard;

public class SaveAsHandler extends AbstractHandler {

    private static final int WIDTH = 420;
    private static final int HEIGHT = 360;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);
        Window wizard = new StaticWizardDialog(shell, WIDTH, HEIGHT,
                new SaveAsWizard());

        if (wizard.open() != Window.CANCEL) {
            SpriteyPlugin.getDefault().getViewUpdater().refreshViews();
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootModel().isLeaf();
    }

}
