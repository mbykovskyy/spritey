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
package spritey.rcp.views.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import spritey.core.Sheet;

/**
 * Specialised cell editor for width or height property.
 */
public class SizeCellEditor extends CellEditor {

    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    static final int DECIMAL_DIGITS = 0;
    static final int INCREMENT = 1;
    static final int PAGE_INCREMENT = 10;

    static final int DEFAULT_WIDTH = Sheet.DEFAULT_SIZE.width;
    static final int MAX_WIDTH = Sheet.MAX_WIDTH;
    static final int MIN_WIDTH = Sheet.MIN_WIDTH;

    static final int DEFAULT_HEIGHT = Sheet.DEFAULT_SIZE.height;
    static final int MAX_HEIGHT = Sheet.MAX_HEIGHT;
    static final int MIN_HEIGHT = Sheet.MIN_HEIGHT;

    private Spinner spinner;
    private KeyListener spinnerKeyListener;
    private FocusListener spinnerFocusListener;

    private int attribute;

    /**
     * Constructor.
     * 
     * @param attribute
     *        1 for width or 0 (zero) for height.
     * @throws IllegalArgumentException
     *         when attribute is not 1 or 0 (zero).
     */
    public SizeCellEditor(Composite parent, int attribute) {
        validateArgument(attribute);
        this.attribute = attribute;

        create(parent);
    }

    private void validateArgument(int attribute) {
        if ((attribute != WIDTH) && (attribute != HEIGHT)) {
            throw new IllegalArgumentException(
                    "Attribute must be either 1 for HEIGHT or 0 for WIDTH.");
        }
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
        spinner = new Spinner(parent, SWT.NONE);
        spinner.addKeyListener(getSpinnerKeyListener());
        spinner.addFocusListener(getSpinnerFocusListener());

        if (attribute == WIDTH) {
            spinner.setValues(DEFAULT_WIDTH, MIN_WIDTH, MAX_WIDTH,
                    DECIMAL_DIGITS, INCREMENT, PAGE_INCREMENT);
        } else if (attribute == HEIGHT) {
            spinner.setValues(DEFAULT_HEIGHT, MIN_HEIGHT, MAX_HEIGHT,
                    DECIMAL_DIGITS, INCREMENT, PAGE_INCREMENT);
        }

        return spinner;
    }

    private KeyListener getSpinnerKeyListener() {
        if (null == spinnerKeyListener) {
            spinnerKeyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    keyReleaseOccured(e);
                }
            };
        }

        return spinnerKeyListener;
    }

    private FocusListener getSpinnerFocusListener() {
        if (null == spinnerFocusListener) {
            spinnerFocusListener = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    // focusLost will apply editor value before deactivating it.
                    SizeCellEditor.this.focusLost();
                }
            };
        }

        return spinnerFocusListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
     */
    @Override
    protected Object doGetValue() {
        return spinner.getSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
     */
    @Override
    protected void doSetFocus() {
        spinner.setFocus();
        spinner.addFocusListener(getSpinnerFocusListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
     */
    @Override
    protected void doSetValue(Object value) {
        spinner.setSelection((Integer) value);
    }

    @Override
    public void deactivate() {
        if ((spinner != null) && !spinner.isDisposed()) {
            spinner.removeFocusListener(getSpinnerFocusListener());
        }

        super.deactivate();
    }

}
