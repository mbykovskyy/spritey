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

import static spritey.ui.Application.DROP_DOWN_IMG_ID;
import static spritey.ui.ImageFactory.appendImage;
import static spritey.ui.ImageFactory.createCheckerImage;
import static spritey.ui.ImageFactory.createColorImage;

import java.awt.Color;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import spritey.core.Sheet;
import spritey.ui.Application;
import spritey.ui.Messages;

/**
 * The page for creating a new sprite sheet.
 */
public class NewSheetPage extends WizardPage {

    static final String NAME = "NEW_SPRITE_SHEET";

    static final int DEFAULT_WIDTH = Sheet.DEFAULT_SIZE.width;
    static final int MAX_WIDTH = Sheet.MAX_WIDTH;
    static final int MIN_WIDTH = Sheet.MIN_WIDTH;

    static final int DEFAULT_HEIGHT = Sheet.DEFAULT_SIZE.height;
    static final int MAX_HEIGHT = Sheet.MAX_HEIGHT;
    static final int MIN_HEIGHT = Sheet.MIN_HEIGHT;

    static final RGB DEFAULT_BACKGROUND = new RGB(255, 0, 255);
    static final int IMAGE_WIDTH = 32;
    static final int IMAGE_HEIGHT = 16;

    static final String DEFAULT_COMMENT = Sheet.DEFAULT_DESCRIPTION;
    static final int MAX_DESCRIPTION_LENGTH = Sheet.MAX_DESCRIPTION_LENGTH;

    static final int DECIMAL_DIGITS = 0;
    static final int INCREMENT = 1;
    static final int PAGE_INCREMENT = 10;

    private Text widthText;
    private Text heightText;
    private Button powerOfTwoCheck;
    private Button aspectRatioCheck;
    private Text commentText;

    private boolean isOpaque;
    private Color background;

    private boolean powerOfTwoChecked;
    private boolean maintainRatioChecked;

    /**
     * Creates a new instance of NewSheetPage.
     */
    public NewSheetPage() {
        super(NAME);

        setTitle(Messages.NEW_SHEET_PAGE_TITLE);
        setDescription(Messages.NEW_SHEET_PAGE_DESCRIPTION);

        isOpaque = true;
        background = new Color(DEFAULT_BACKGROUND.red,
                DEFAULT_BACKGROUND.green, DEFAULT_BACKGROUND.blue);
    }

    @Override
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        createSizeConstraintsControls(container);
        createBackgroundControls(container);
        createCommentControls(container);

        setControl(container);
    }

    /**
     * Validates all fields and sets error message accordingly.
     */
    private void validateFields() {
        String text = widthText.getText();
        int width = text.isEmpty() ? 0 : Integer.valueOf(text);

        text = heightText.getText();
        int height = text.isEmpty() ? 0 : Integer.valueOf(text);

        if ((width < MIN_WIDTH) || (width > MAX_WIDTH)) {
            setErrorMessage(NLS.bind(
                    Messages.NEW_SHEET_PAGE_MAX_WIDTH_INVALID_RANGE, MIN_WIDTH,
                    MAX_WIDTH));
        } else if ((height < MIN_HEIGHT) || (height > MAX_HEIGHT)) {
            setErrorMessage(NLS.bind(
                    Messages.NEW_SHEET_PAGE_MAX_HEIGHT_INVALID_RANGE,
                    MIN_HEIGHT, MAX_HEIGHT));
        } else if (powerOfTwoChecked && ((width & -width) != width)) {
            setErrorMessage(Messages.NEW_SHEET_PAGE_MAX_WIDTH_INVALID_POWER_OF_TWO);
        } else if (powerOfTwoChecked && ((height & -height) != height)) {
            setErrorMessage(Messages.NEW_SHEET_PAGE_MAX_HEIGHT_INVALID_POWER_OF_TWO);
        } else {
            setErrorMessage(null);
        }
    }

    /**
     * Creates size controls.
     * 
     * @param parent
     *        the parent widget.
     */
    private void createSizeControls(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = layout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Label widthLabel = new Label(container, SWT.NONE);
        widthLabel.setText(Messages.NEW_SHEET_PAGE_MAX_WIDTH);

        VerifyListener digitValidator = new VerifyListener() {
            @Override
            public void verifyText(VerifyEvent e) {
                for (int i = 0; i < e.text.length(); ++i) {
                    if (!Character.isDigit(e.text.charAt(i))) {
                        e.doit = false;
                        return;
                    }
                }
            }
        };

        ModifyListener sizeValidator = new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        };

        widthText = new Text(container, SWT.BORDER);
        widthText.setTextLimit(4);
        widthText.setText(String.valueOf(MAX_WIDTH));
        widthText.addVerifyListener(digitValidator);
        widthText.addModifyListener(sizeValidator);

        Label heightLabel = new Label(container, SWT.NONE);
        heightLabel.setText(Messages.NEW_SHEET_PAGE_MAX_HEIGHT);

        heightText = new Text(container, SWT.BORDER);
        heightText.setTextLimit(4);
        heightText.setText(String.valueOf(MAX_HEIGHT));
        heightText.addVerifyListener(digitValidator);
        heightText.addModifyListener(sizeValidator);
    }

    /**
     * Creates constraints controls.
     * 
     * @param parent
     *        the parent widget.
     */
    private void createOptionsControls(Composite parent) {
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = layout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        powerOfTwoCheck = new Button(container, SWT.CHECK);
        powerOfTwoCheck.setText(Messages.NEW_SHEET_PAGE_POWER_OF_TWO);
        powerOfTwoCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                powerOfTwoChecked = powerOfTwoCheck.getSelection();
                validateFields();
            }
        });

        aspectRatioCheck = new Button(container, SWT.CHECK);
        aspectRatioCheck.setText(Messages.NEW_SHEET_PAGE_ASPECT_RATIO);
        aspectRatioCheck.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                maintainRatioChecked = aspectRatioCheck.getSelection();
            }
        });
    }

    /**
     * Creates a group of size constraints controls.
     * 
     * @param parent
     *        the parent widget.
     */
    private void createSizeConstraintsControls(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(2, true));
        group.setText(Messages.NEW_SHEET_PAGE_SIZE_CONSTRAINTS);
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        createSizeControls(group);
        createOptionsControls(group);
    }

    /**
     * Creates background controls.
     * 
     * @param parent
     *        the parent widget.
     */
    private void createBackgroundControls(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);

        Label backgroundLabel = new Label(container, SWT.NONE);
        backgroundLabel.setText(Messages.NEW_SHEET_PAGE_BACKGROUND);

        final Image dropdownImage = Application.getImageRegistry().get(
                DROP_DOWN_IMG_ID);
        Image colorImage = createColorImage(DEFAULT_BACKGROUND, IMAGE_WIDTH,
                IMAGE_HEIGHT, true);

        final Button button = new Button(container, SWT.TOGGLE);
        button.setImage(appendImage(colorImage, dropdownImage));
        colorImage.dispose();

        final Menu menu = new Menu(button);
        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuHidden(MenuEvent e) {
                button.setSelection(false);
            }
        });

        button.addSelectionListener(new SelectionAdapter() {
            /*
             * Shows drop-down menu;
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                Button source = (Button) e.getSource();

                if (source.getSelection()) {
                    Composite parent = source.getParent();

                    Rectangle rect = source.getBounds();
                    Point point = parent.toDisplay(new Point(rect.x, rect.y));

                    menu.setLocation(point.x, point.y + rect.height);
                    menu.setVisible(true);
                }
            }
        });

        MenuItem chooseColor = new MenuItem(menu, SWT.NONE);
        chooseColor.setText(Messages.NEW_SHEET_PAGE_CHOOSE_COLOR);
        chooseColor.addSelectionListener(new SelectionAdapter() {
            /*
             * Opens colour chooser dialog.
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                RGB rbg = new ColorDialog(button.getShell()).open();
                if (null != rbg) {
                    Color color = new Color(rbg.red, rbg.green, rbg.blue);

                    if (!color.equals(background)) {
                        background = color;
                        isOpaque = true;

                        Image colorImage = createColorImage(rbg, IMAGE_WIDTH,
                                IMAGE_HEIGHT, true);

                        button.setImage(appendImage(colorImage, dropdownImage));
                        colorImage.dispose();
                    }
                }
            }
        });

        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem transparent = new MenuItem(menu, SWT.NONE);
        transparent.setText(Messages.NEW_SHEET_PAGE_TRANSPARENT);
        transparent.addSelectionListener(new SelectionAdapter() {
            /*
             * Sets background colour to transparent.
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (isOpaque) {
                    Image checker = createCheckerImage(IMAGE_WIDTH,
                            IMAGE_HEIGHT, true);

                    button.setImage(appendImage(checker, dropdownImage));
                    checker.dispose();

                    background = null;
                    isOpaque = false;
                }
            }
        });
    }

    /**
     * Creates comment controls.
     * 
     * @param parent
     *        the parent widget.
     */
    private void createCommentControls(Composite parent) {
        Label commentLabel = new Label(parent, SWT.NONE);
        commentLabel.setText(Messages.NEW_SHEET_PAGE_COMMENT);
        commentLabel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_BEGINNING));

        commentText = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
                | SWT.WRAP);
        commentText.setText(DEFAULT_COMMENT);
        commentText.setTextLimit(MAX_DESCRIPTION_LENGTH);
        commentText.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

}
