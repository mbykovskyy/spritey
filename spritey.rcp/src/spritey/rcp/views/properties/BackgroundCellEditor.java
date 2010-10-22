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
package spritey.rcp.views.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Specialised cell editor for background property.
 */
public class BackgroundCellEditor extends CellEditor {

    private static final String CHOOSE_COLOR = "Choose Color...";
    private static final String TRANSPARENT = "Transparent";

    private CCombo combo;
    private FocusListener comboFocusListener;
    private SelectionListener comboSelectionListener;
    private KeyListener comboKeyListener;

    private RGB background;

    /**
     * Constructor
     * 
     * @param parent
     *        the parent of this cell editor.
     */
    public BackgroundCellEditor(Composite parent) {
        super(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    protected Control createControl(Composite parent) {
        combo = new CCombo(parent, SWT.READ_ONLY);
        combo.setBackground(parent.getBackground());
        combo.add(CHOOSE_COLOR);
        combo.add(TRANSPARENT);
        combo.addSelectionListener(getComboSelectionListener(parent.getShell()));
        combo.addKeyListener(getComboKeyListener());

        return combo;
    }

    private FocusListener getComboFocusListener() {
        if (null == comboFocusListener) {
            comboFocusListener = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    BackgroundCellEditor.this.focusLost();
                }
            };
        }

        return comboFocusListener;
    }

    private SelectionListener getComboSelectionListener(final Shell shell) {
        if (null == comboSelectionListener) {
            final ColorDialog dialog = new ColorDialog(shell);

            comboSelectionListener = new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    CCombo source = (CCombo) e.getSource();

                    switch (source.getSelectionIndex()) {
                    case 0:
                        // Remove the combo's focus listener since it's
                        // guaranteed
                        // to lose focus when the dialog opens.
                        combo.removeFocusListener(getComboFocusListener());

                        RGB color = dialog.open();

                        // Re-add the listener once the dialog closes.
                        combo.addFocusListener(getComboFocusListener());

                        if (null != color) {
                            // Dialog was not cancelled.
                            markDirty();
                            doSetValue(color);
                            fireApplyEditorValue();
                        } else {
                            // When dialog is cancelled set the value back to
                            // original but do not make editor dirty or fire
                            // apply
                            // value event.
                            doSetValue(background);
                        }
                        break;
                    case 1:
                        markDirty();
                        doSetValue(null);
                        fireApplyEditorValue();
                        break;
                    }
                }
            };
        }

        return comboSelectionListener;
    }

    private KeyListener getComboKeyListener() {
        if (null == comboKeyListener) {
            comboKeyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    keyReleaseOccured(e);
                }
            };
        }

        return comboKeyListener;
    }

    private void updateContent(Object value) {
        if (null == combo) {
            return;
        } else if (null != value) {
            combo.setText(value.toString());
        } else {
            combo.setText(TRANSPARENT);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
     */
    @Override
    protected Object doGetValue() {
        return background;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
     */
    @Override
    protected void doSetFocus() {
        combo.setFocus();
        combo.addFocusListener(getComboFocusListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
     */
    @Override
    protected void doSetValue(Object value) {
        // We need this until bug #320200
        // (https://bugs.eclipse.org/bugs/show_bug.cgi?id=320200) is fixed.
        if ((null == value) || value.equals(TRANSPARENT)) {
            background = null;
        } else {
            background = (RGB) value;
        }
        updateContent(background);
    }

    @Override
    public void deactivate() {
        if ((combo != null) && !combo.isDisposed()) {
            combo.removeFocusListener(getComboFocusListener());
        }

        super.deactivate();
    }

}
