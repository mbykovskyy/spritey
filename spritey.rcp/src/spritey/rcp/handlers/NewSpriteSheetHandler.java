/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.core.Sheet;
import spritey.core.node.Node;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.SheetConstants;
import spritey.rcp.dialogs.NewSpriteSheetDialog;
import spritey.rcp.editors.SheetEditor;
import spritey.rcp.editors.SheetEditorInput;
import spritey.rcp.wizards.NewSpriteSheetWizard;

/**
 * Handles the creation of a new sprite sheet.
 */
public class NewSpriteSheetHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);

        Window wizard = new NewSpriteSheetDialog(shell,
                new NewSpriteSheetWizard());

        if (wizard.open() != Window.CANCEL) {
            Node sheet = SpriteyPlugin.getDefault().getRootNode()
                    .getChild(SheetConstants.DEFAULT_NAME);

            if (sheet != null) {
                IWorkbenchWindow window = HandlerUtil
                        .getActiveWorkbenchWindow(event);
                SheetEditorInput input = new SheetEditorInput(
                        (Sheet) sheet.getModel());

                try {
                    window.getActivePage().openEditor(input, SheetEditor.ID);
                } catch (PartInitException e) {
                    // TODO Log this exception as we do not expect it.
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
