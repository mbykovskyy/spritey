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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import spritey.rcp.Messages;
import spritey.rcp.operations.ExportSheetOperation;

/**
 * A wizard for creating a new group.
 */
public class SaveAsWizard extends Wizard implements IOverwriteQuery {

    private SaveAsMainPage mainPage;
    private boolean isOverwrite;

    @Override
    public void addPages() {
        mainPage = new SaveAsMainPage();

        setWindowTitle(Messages.SAVE_AS);
        addPage(mainPage);
        setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish() {
        isOverwrite = mainPage.isOverwrite();

        try {
            ExportSheetOperation op = new ExportSheetOperation(
                    mainPage.getImageFile(), mainPage.getMetadataFile(), this);
            getContainer().run(true, true, op);

            IStatus status = op.getStatus();
            if (!status.isOK()) {
                ErrorDialog.openError(getContainer().getShell(),
                        Messages.SAVE_AS, null, status);
                return false;
            }
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
                    Messages.SAVE_AS, Messages.OPERATION_INVOCATION_ERROR,
                    SWT.SHEET);
            // TODO Log this exception.
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String queryOverwrite(String pathString) {
        if (isOverwrite) {
            return IOverwriteQuery.ALL;
        }

        String[] buttons = new String[] { IDialogConstants.YES_LABEL,
                IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.NO_LABEL,
                IDialogConstants.NO_TO_ALL_LABEL, IDialogConstants.CANCEL_LABEL };

        File file = new File(pathString);
        String message = NLS.bind(Messages.OVERWRITE_FILE, file.getName(),
                file.getParent());
        final MessageDialog dialog = new MessageDialog(getShell(),
                Messages.SAVE_AS, null, message, MessageDialog.QUESTION,
                buttons, 0) {
            @Override
            protected int getShellStyle() {
                return super.getShellStyle() | SWT.SHEET;
            }
        };

        // This method is called from a non-UI thread, therefore, opening dialog
        // has to be wrapped into runnable and executed in the UI thread.
        getContainer().getShell().getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                dialog.open();
            }
        });

        switch (dialog.getReturnCode()) {
        case 0:
            return IOverwriteQuery.YES;
        case 1:
            return IOverwriteQuery.ALL;
        case 2:
            return IOverwriteQuery.NO;
        case 3:
            return IOverwriteQuery.NO_ALL;
        }
        return IOverwriteQuery.CANCEL;
    }

}
