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
package spritey.rcp.wizards.addsprites;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.filter.InvisibleSpriteFilter;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;

/**
 * A wizard for importing sprites into sprite sheet.
 */
public class AddSpritesWizard extends Wizard {

    AddSpritesPage mainPage;
    AddSpritesBatchRenamePage renamePage;

    private List<IStatus> errorMessages;

    /**
     * Creates a new instance of AddSpritesWizard.
     */
    public AddSpritesWizard() {
        errorMessages = new ArrayList<IStatus>();
        renamePage = new AddSpritesBatchRenamePage();
        mainPage = new AddSpritesPage(renamePage);
    }

    @Override
    public void addPages() {
        setWindowTitle(Messages.ADD_SPRITES);
        addPage(mainPage);
        addPage(renamePage);
        setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish() {
        IRunnableWithProgress op = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException, InterruptedException {
                monitor.beginTask("", 2);

                final Model root = mainPage.finalizeRootModel();
                final SpriteyPlugin plugin = SpriteyPlugin.getDefault();
                Sheet sheet = (Sheet) plugin.getRootModel().getChildren()[0];
                sheet.addChildren(root.getChildren());
                monitor.worked(1);

                plugin.getPacker().pack(sheet, false);
                monitor.worked(1);

                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        plugin.getViewUpdater().refreshViews();

                        checkForInvisibleSprites(root);
                        if (!errorMessages.isEmpty()) {
                            displayErrorMessages(getShell());
                        }
                    }
                });
                monitor.done();
            }
        };

        try {
            getContainer().run(true, false, op);
        } catch (InvocationTargetException e) {
            // TODO Log this exception.
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Log this exception as we don't expect any interruptions.
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks for sprites that do not fit into sprite sheet and adds a warning
     * messages for each match.
     * 
     * @param branchRoot
     *        the root of the branch where new sprites were added.
     */
    private void checkForInvisibleSprites(Model branchRoot) {
        Sprite[] sprites = new InvisibleSpriteFilter().filter(branchRoot);

        for (Sprite sprite : sprites) {
            String message = NLS.bind(Messages.SPRITE_DOES_NOT_FIT,
                    sprite.getProperty(Sprite.NAME));
            errorMessages.add(new Status(IStatus.WARNING, "unknown", message));
        }
    }

    /**
     * Displays accumulated error messages that occurred while importing
     * sprites.
     * 
     * @param shell
     *        the parent shell.
     */
    private void displayErrorMessages(Shell shell) {
        IStatus status = new MultiStatus("unknown", 0,
                errorMessages.toArray(new IStatus[errorMessages.size()]),
                Messages.ADD_SPRITES_DO_NOT_FIT, null);
        ErrorDialog.openError(shell, Messages.ADD_SPRITES, null, status);
        errorMessages.clear();
    }

}
