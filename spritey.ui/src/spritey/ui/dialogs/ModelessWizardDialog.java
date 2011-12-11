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
package spritey.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import spritey.ui.Messages;

/**
 * A modeless wizard dialog to show to the end user.
 */
public class ModelessWizardDialog extends WizardDialog {

    /**
     * An exception handler for handling escaped exceptions.
     */
    private static class ExceptionHandler implements IExceptionHandler {

        private Shell parent;

        /**
         * Creates a new instance of ExeptionHandler.
         * 
         * @param parent
         *        the parent shell.
         */
        public ExceptionHandler(Shell parent) {
            this.parent = parent;
        }

        @Override
        public void handleException(Throwable t) {
            if (t instanceof ThreadDeath) {
                // ThreadDeath is a normal occurrence when the thread dies.
                throw (ThreadDeath) t;
            }

            // Log it.
            t.printStackTrace();

            MessageDialog.open(MessageDialog.ERROR, parent,
                    Messages.SPRITE_SHEET_WIZARD_TITLE,
                    Messages.UNEXPECTED_ERROR_OCCURRED, SWT.SHEET);
        }
    }

    public ModelessWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
        setShellStyle(SWT.SHELL_TRIM);
        setExceptionHandler(new ExceptionHandler(getShell()));
    }

    @Override
    protected Shell getParentShell() {
        // Returning null makes this dialog modeless.
        return null;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        Button finish = getButton(IDialogConstants.FINISH_ID);
        finish.setText(Messages.SPRITE_SHEET_WIZARD_BUILD);
        setButtonLayoutData(finish);

        Button cancel = getButton(IDialogConstants.CANCEL_ID);
        cancel.setText(Messages.SPRITE_SHEET_WIZARD_CLOSE);
        setButtonLayoutData(cancel);
    }

}
