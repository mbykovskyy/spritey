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

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A custom file dialog for opening and saving images.
 */
public class ImageFileDialog {

    private static final String BMP_EXTENSIONS = "*.bmp;*.dib";
    private static final String JPEG_EXTENSIONS = "*.jpg;*.jpeg;*jpe;*.jfif";
    private static final String GIF_EXTENSIONS = "*.gif";
    private static final String TIFF_EXTENSIONS = "*.tif;*.tiff";
    private static final String PNG_EXTENSIONS = "*.png";
    private static final String ICO_EXTENSIONS = "*.ico";
    private static final String ALL_FILES_EXTENSIONS = "*.*";

    private static final String ALL_IMAGES_EXTENSIONS = BMP_EXTENSIONS + ";"
            + JPEG_EXTENSIONS + ";" + GIF_EXTENSIONS + ";" + TIFF_EXTENSIONS
            + ";" + PNG_EXTENSIONS + ";" + ICO_EXTENSIONS;

    private static final String[] SAVE_FILTER_NAMES = { "BMP (*.bmp;*.dib)",
            "JPEG (*.jpg;*.jpeg;*jpe;*.jfif)", "GIF (*.gif)",
            "TIFF (*.tif;*.tiff)", "PNG (*.png)" };

    private static final String[] OPEN_FILTER_NAMES = { "BMP (*.bmp;*.dib)",
            "JPEG (*.jpg;*.jpeg;*jpe;*.jfif)", "GIF (*.gif)",
            "TIFF (*.tif;*.tiff)", "PNG (*.png)", "ICO (*.ico)", "All images",
            "All files" };

    private static final String[] SAVE_FILTER_EXTENSIONS = { BMP_EXTENSIONS,
            JPEG_EXTENSIONS, GIF_EXTENSIONS, TIFF_EXTENSIONS, PNG_EXTENSIONS };

    private static final String[] OPEN_FILTER_EXTENSIONS = { BMP_EXTENSIONS,
            JPEG_EXTENSIONS, GIF_EXTENSIONS, TIFF_EXTENSIONS, PNG_EXTENSIONS,
            ICO_EXTENSIONS, ALL_IMAGES_EXTENSIONS, ALL_FILES_EXTENSIONS };

    private static final int SAVE_DEFAULT_FILTER_EXENSION = 0;
    private static final int OPEN_DEFAULT_FILTER_EXENSION = 6;

    private int style;
    private FileDialog dialog;

    /**
     * Creates an instance of a dialog.
     * 
     * @param parent
     *        the parent shell.
     * @param style
     *        the dialog style.
     */
    public ImageFileDialog(Shell parent, int style) {
        this.style = style;
        dialog = new FileDialog(parent, style);
    }

    /**
     * Opens the dialog.
     * 
     * @return selected file.
     */
    public String open() {
        if ((style & SWT.OPEN) == SWT.OPEN) {
            dialog.setFilterExtensions(OPEN_FILTER_EXTENSIONS);
            dialog.setFilterNames(OPEN_FILTER_NAMES);
            dialog.setFilterIndex(OPEN_DEFAULT_FILTER_EXENSION);
        } else if ((style & SWT.SAVE) == SWT.SAVE) {
            dialog.setFilterExtensions(SAVE_FILTER_EXTENSIONS);
            dialog.setFilterNames(SAVE_FILTER_NAMES);
            dialog.setFilterIndex(SAVE_DEFAULT_FILTER_EXENSION);
        }
        return dialog.open();
    }

    /**
     * Returns an array of selected file paths.
     * 
     * @return an array of selected file paths.
     */
    public String[] getFilePaths() {
        String[] filenames = dialog.getFileNames();
        String parent = dialog.getFilterPath();
        String[] filepaths = new String[filenames.length];

        for (int i = 0; i < filenames.length; ++i) {
            filepaths[i] = parent + File.separatorChar + filenames[i];
        }
        return filepaths;
    }

}
