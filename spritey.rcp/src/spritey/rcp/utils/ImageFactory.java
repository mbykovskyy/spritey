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
package spritey.rcp.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * An image factory for creating custom images.
 */
public class ImageFactory {

    public static final int DEFAULT_SQUARE_SIZE = 8;

    // Light grey
    public static final RGB DEFAULT_COLOR1 = new RGB(153, 153, 153);
    // Dark grey
    public static final RGB DEFAULT_COLOR2 = new RGB(102, 102, 102);

    /**
     * Creates a checker image with default square size and colors.
     * 
     * @param width
     *        the image width.
     * @param height
     *        the image height.
     * @return an instance of checker image.
     */
    public Image createCheckerImage(int width, int height) {
        return createCheckerImage(DEFAULT_COLOR1, DEFAULT_COLOR2,
                DEFAULT_SQUARE_SIZE, width, height);
    }

    /**
     * Creates a checker image.
     * 
     * @return an instance of a checker image.
     */
    public Image createCheckerImage(RGB color1, RGB color2, int squareSize,
            int width, int height) {
        final Device device = Display.getCurrent();
        final Color LIGHT_GREY = new Color(device, color1);
        final Color DARK_GREY = new Color(device, color2);

        // Adding one is a bit wasteful when image width and height are
        // divisible by square size but it makes sure that image is completely
        // covered with checker when width and height divide with a remainder.
        final int ROW_COUNT = (height / squareSize) + 1;
        final int COLUMN_COUNT = (width / squareSize) + 1;

        Image checker = new Image(device, width, height);

        GC gc = new GC(checker);

        for (int r = 0; r < ROW_COUNT; ++r) {
            for (int c = 0; c < COLUMN_COUNT; ++c) {
                gc.setBackground((((r + c) % 2) == 0) ? DARK_GREY : LIGHT_GREY);
                gc.fillRectangle(c * squareSize, r * squareSize, squareSize,
                        squareSize);
            }
        }

        gc.dispose();

        return checker;
    }

    /**
     * Creates an image filled with the specified color.
     * 
     * @param color
     *        the color to fill image with.
     * @param width
     *        the width of an image.
     * @param height
     *        the height of an image.
     * @return an instance of a color image.
     */
    public Image createColorImage(RGB color, int width, int height) {
        final Device device = Display.getCurrent();
        Image colorImage = new Image(device, width, height);

        // When color is null an empty image is returned with what ever
        // background is set by default.
        if (null != color) {
            GC gc = new GC(colorImage);
            gc.setBackground(new Color(device, color));
            gc.fillRectangle(0, 0, width, height);
            gc.dispose();
        }

        return colorImage;
    }

}
