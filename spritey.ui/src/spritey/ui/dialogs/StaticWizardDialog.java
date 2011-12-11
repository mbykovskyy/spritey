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
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * A static wizard dialog that can not be resized.
 */
public class StaticWizardDialog extends WizardDialog {

    /**
     * Creates a new wizard dialog.
     * 
     * @param parent
     *        the parent shell.
     * @param wizard
     *        the wizard to display in this dialog.
     */
    public StaticWizardDialog(Shell parent, IWizard wizard) {
        this(parent, 0, 0, wizard);
    }

    /**
     * Creates a new wizard dialog.
     * 
     * @param parent
     *        the parent shell.
     * @param minPageWidth
     *        the minimum page width.
     * @param minPageHeight
     *        the minimum page height.
     * @param wizard
     *        the wizard to display in this dialog.
     */
    public StaticWizardDialog(Shell parent, int minPageWidth,
            int minPageHeight, IWizard wizard) {
        super(parent, wizard);
        setShellStyle(getShellStyle() & ~SWT.MAX & ~SWT.RESIZE);
        setMinimumPageSize(minPageWidth, minPageHeight);
    }

    @Override
    protected Point getInitialSize() {
        return getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        Button finish = getButton(IDialogConstants.FINISH_ID);
        finish.setText(IDialogConstants.OK_LABEL);
        setButtonLayoutData(finish);
    }

}
