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
package spritey.rcp.dialogs;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * A static wizard dialog that can not be resized.
 */
public class StaticWizardDialog extends WizardDialog {

    private int width;
    private int height;

    /**
     * Creates a new wizard dialog.
     * 
     * @param parentShell
     *        the parent shell.
     * @param width
     *        the dialog width.
     * @param height
     *        the dialog height.
     * @param newWizard
     *        the wizard to display in this dialog.
     */
    public StaticWizardDialog(Shell parentShell, int width, int height,
            IWizard newWizard) {
        super(parentShell, newWizard);
        this.width = width;
        this.height = height;

        setShellStyle(getShellStyle() & ~SWT.MAX & ~SWT.RESIZE);
    }

    /**
     * Configures the size and the location of the shell.
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setSize(width, height);
        center(newShell, getParentShell());
    }

    /**
     * Centers window relative to its parent window.
     * 
     * @param dialog
     *        the window to center.
     * @param parent
     *        the parent window.
     */
    private void center(Shell dialog, Shell parent) {
        Rectangle parentSize = parent.getBounds();
        Rectangle dialogSize = dialog.getBounds();

        int x = (parentSize.width - dialogSize.width) / 2 + parentSize.x;
        int y = (parentSize.height - dialogSize.height) / 2 + parentSize.y;

        dialog.setLocation(new Point(x, y));
    }

}
