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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;

import spritey.ui.Messages;
import spritey.ui.operations.OverwriteQuery;
import spritey.ui.operations.SaveSheetOperation;
import spritey.ui.pages.AddSpritesPage;
import spritey.ui.pages.NewSheetPage;
import spritey.ui.pages.SaveAsPage;

/**
 * A wizard for creating a sprite sheet.
 */
public class SpriteSheetWizard extends Wizard {

    private NewSheetPage newSheetPage;
    private AddSpritesPage addSpritesPage;
    private SaveAsPage saveAsPage;

    private boolean isOverwrite;

    /**
     * Creates a new instance of SpriteSheetWizard.
     */
    public SpriteSheetWizard() {
        newSheetPage = new NewSheetPage();
        addSpritesPage = new AddSpritesPage(newSheetPage);
        saveAsPage = new SaveAsPage();
    }

    @Override
    public void addPages() {
        setWindowTitle(Messages.SPRITE_SHEET_WIZARD_TITLE);
        setNeedsProgressMonitor(true);

        addPage(newSheetPage);
        addPage(addSpritesPage);
        addPage(saveAsPage);
    }

    @Override
    public boolean performFinish() {
        isOverwrite = saveAsPage.isOverwrite();

        OverwriteQuery callback = new OverwriteQuery() {
            @Override
            public int queryOverwrite(String path) {
                if (isOverwrite) {
                    return OverwriteQuery.ALL;
                }

                String[] buttons = new String[] { IDialogConstants.YES_LABEL,
                        IDialogConstants.YES_TO_ALL_LABEL,
                        IDialogConstants.NO_LABEL,
                        IDialogConstants.NO_TO_ALL_LABEL,
                        IDialogConstants.CANCEL_LABEL };

                File file = new File(path);
                String message = NLS.bind(Messages.SAVE_AS_OVERWRITE_FILE,
                        file.getName(), file.getParent());
                final MessageDialog dialog = new MessageDialog(getShell(),
                        Messages.SPRITE_SHEET_WIZARD_TITLE, null, message,
                        MessageDialog.QUESTION, buttons, 0) {
                    @Override
                    protected int getShellStyle() {
                        return super.getShellStyle() | SWT.SHEET;
                    }
                };

                // This method is called from a non-UI thread, therefore,
                // opening dialog has to be wrapped into runnable and executed
                // in the UI thread.
                getShell().getDisplay().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        dialog.open();
                    }
                });
                return dialog.getReturnCode();
            }
        };

        try {
            SaveSheetOperation op = new SaveSheetOperation(
                    newSheetPage.getConstraints(), newSheetPage.getSheet(),
                    saveAsPage.getImageFile(), saveAsPage.getMetadataFile(),
                    callback);
            getContainer().run(true, true, op);

            IStatus status = op.getStatus();
            if (!status.isOK()) {
                // To make dialogs consistent throughout the application,
                // display a custom error dialog with details button only when
                // there are multiple problems. Otherwise display an OS native
                // dialog.
                if (status.isMultiStatus()) {
                    ErrorDialog.openError(getContainer().getShell(),
                            Messages.SPRITE_SHEET_WIZARD_TITLE, null, status);
                } else {
                    MessageDialog.open(MessageDialog.ERROR, getContainer()
                            .getShell(), Messages.SPRITE_SHEET_WIZARD_TITLE,
                            status.getMessage(), SWT.SHEET);
                }
                return false;
            }
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
                    Messages.SPRITE_SHEET_WIZARD_TITLE,
                    Messages.SAVE_AS_OPERATION_INVOCATION_ERROR, SWT.SHEET);

            // TODO log it.
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
