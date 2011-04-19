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
package spritey.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * An image factory for creating custom images.
 */
public class ImageFactory {

    public static final int DEFAULT_SQUARE_SIZE = 8;

    // Light grey
    public static final RGB DEFAULT_SQUARE_COLOR1 = new RGB(153, 153, 153);
    // Dark grey
    public static final RGB DEFAULT_SQUARE_COLOR2 = new RGB(102, 102, 102);
    // Black
    public static final RGB DEFAULT_BORDER_COLOR = new RGB(0, 0, 0);

    /**
     * Creates a checker image with default square size and colors. If
     * <code>outlined</code> is <code>true</code> then a border is drawn of
     * default color.
     * 
     * @param width
     *        the image width.
     * @param height
     *        the image height.
     * @param outlined
     *        if <code>true</code> then a default border is drawn.
     * @return an instance of checker image.
     */
    public static Image createCheckerImage(int width, int height,
            boolean outlined) {
        return createCheckerImage(width, height, DEFAULT_SQUARE_SIZE, outlined);
    }

    /**
     * Creates a checker image with default colors. If <code>outlined</code> is
     * <code>true</code> then a border is drawn of default color.
     * 
     * @param width
     *        the image width.
     * @param height
     *        the image height.
     * @param square
     *        the square size.
     * @param outlined
     *        if <code>true</code> then a default border is drawn.
     * @return an instance of checker image.
     */
    public static Image createCheckerImage(int width, int height, int square,
            boolean outlined) {
        RGB border = outlined ? DEFAULT_BORDER_COLOR : null;

        return createCheckerImage(DEFAULT_SQUARE_COLOR1, DEFAULT_SQUARE_COLOR2,
                square, width, height, border);
    }

    /**
     * Creates a checker image with border of 1 pixel. When
     * <code>borderColor</code> is null no border is created.
     * 
     * @param color1
     *        the square color one.
     * @param color2
     *        the square color two.
     * @param squareSize
     *        the square size.
     * @param width
     *        the width of an image to create.
     * @param height
     *        the height of an image to create
     * @param borderColor
     *        the border color.
     * @return an instance of a checker image.
     */
    public static Image createCheckerImage(RGB color1, RGB color2,
            int squareSize, int width, int height, RGB borderColor) {
        final Device device = Display.getDefault();
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

        if (null != borderColor) {
            gc.setBackground(new Color(device, borderColor));
            gc.drawRectangle(0, 0, width - 1, height - 1);
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
     * @param borderColor
     *        the outline color.
     * @return an instance of a color image.
     */
    public static Image createColorImage(RGB color, int width, int height,
            boolean outline) {
        RGB border = outline ? DEFAULT_BORDER_COLOR : null;

        return createColorImage(color, width, height, border);
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
     * @param borderColor
     *        the outline color.
     * @return an instance of a color image.
     */
    public static Image createColorImage(RGB color, int width, int height,
            RGB borderColor) {
        final Device device = Display.getDefault();
        Image colorImage = new Image(device, width, height);

        GC gc = new GC(colorImage);

        // When color is null an empty image is returned with what ever
        // background is set by default.
        if (null != color) {
            gc.setBackground(new Color(device, color));
            gc.fillRectangle(0, 0, width, height);
        }

        if (null != borderColor) {
            gc.setBackground(new Color(device, borderColor));
            gc.drawRectangle(0, 0, width - 1, height - 1);
        }

        gc.dispose();
        return colorImage;
    }

    /**
     * Create an image from the specified image data.
     * 
     * @param data
     *        the image data.
     * 
     * @return an instance of an image.
     */
    public static Image createImage(ImageData data) {
        return new Image(Display.getDefault(), data);
    }

    private static ImageData generateMask(ImageData data) {
        PaletteData palette = new PaletteData(new RGB[] {
                new RGB(255, 255, 255), new RGB(0, 0, 0) });

        ImageData mask = new ImageData(data.width, data.height, 1, palette);

        if (SWT.TRANSPARENCY_PIXEL == data.getTransparencyType()) {
            for (int y = 0; y < data.height; ++y) {
                for (int x = 0; x < data.width; ++x) {
                    if (data.transparentPixel == data.getPixel(x, y)) {
                        mask.setPixel(x, y, 1);
                    }
                }
            }
        } else if (SWT.TRANSPARENCY_MASK == data.getTransparencyType()) {
            mask = data.getTransparencyMask();
        }

        // TODO SWT.TRANSPARENCY_ALPHA

        return mask;
    }

    private static ImageData append(Image i1, Image i2) {
        ImageData d1 = i1.getImageData();
        ImageData d2 = i2.getImageData();

        int width = d1.width + d2.width;
        int height = d1.height > d2.height ? d1.height : d2.height;

        Image image = new Image(Display.getDefault(), width, height);

        GC gc = new GC(image);
        gc.setBackground(new Color(Display.getDefault(), new RGB(0, 0, 0)));
        gc.fillRectangle(0, 0, width, height);

        if (d1.height > d2.height) {
            gc.drawImage(i1, 0, 0);
            gc.drawImage(i2, d1.width, (d1.height - d2.height) / 2);
        } else {
            gc.drawImage(i1, 0, (d2.height - d1.height) / 2);
            gc.drawImage(i2, d1.width, 0);
        }

        ImageData data = image.getImageData();

        gc.dispose();
        image.dispose();
        return data;
    }

    /**
     * Combines two images and vertically centres them.
     * 
     * @param dst
     *        the image to append to.
     * @param src
     *        the image to append.
     * @return an instance of a combined image.
     */
    public static Image appendImage(Image dst, Image src) {
        Image dstMask = createImage(generateMask(dst.getImageData()));
        Image srcMask = createImage(generateMask(src.getImageData()));

        Image image = new Image(Display.getDefault(), append(dst, src), append(
                dstMask, srcMask));

        dstMask.dispose();
        srcMask.dispose();
        return image;
    }
}
