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
package spritey.rcp.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * A helper converter for converting types.
 */
public class Converter {

    /**
     * Returns <code>true</code> if specified image data has alpha information.
     * 
     * @param swt
     *        the image data to test.
     * @return <code>true</code> if image has alpha information.
     */
    private static boolean hasAlpha(ImageData swt) {
        return (((null != swt.alphaData) && (swt.alphaData.length > 0)) || (-1 != swt.alpha));
    }

    /**
     * Returns alpha value at the specified location.
     * 
     * @param x
     *        pixel's x location.
     * @param y
     *        pixel's y location.
     * @param swt
     *        the image data to get alpha value from.
     * @return alpha value.
     */
    private static int getAlpha(int x, int y, ImageData swt) {
        if (-1 != swt.alpha) {
            return swt.alpha;
        }
        return swt.alphaData[y * swt.width + x];
    }

    /**
     * Converts SWT ImageData to AWT BufferedImage.
     * 
     * @param swt
     *        the image data.
     * @return AWT buffered image.
     */
    public static BufferedImage toBufferedImage(ImageData swt) {
        BufferedImage awt = null;
        PaletteData palette = swt.palette;

        if (palette.isDirect) {
            ColorModel model = null;

            if (hasAlpha(swt)) {
                model = new DirectColorModel(swt.depth + 8, palette.redMask,
                        palette.greenMask, palette.blueMask, 0xFF << 24);
            } else {
                model = new DirectColorModel(swt.depth, palette.redMask,
                        palette.greenMask, palette.blueMask);
            }

            WritableRaster raster = model.createCompatibleWritableRaster(
                    swt.width, swt.height);
            awt = new BufferedImage(model, raster, false, null);

            int[] pixelArray = new int[4];
            for (int y = 0; y < swt.height; y++) {
                for (int x = 0; x < swt.width; x++) {
                    int pixel = swt.getPixel(x, y);
                    RGB rgb = palette.getRGB(pixel);
                    pixelArray[0] = rgb.red;
                    pixelArray[1] = rgb.green;
                    pixelArray[2] = rgb.blue;
                    if (hasAlpha(swt)) {
                        pixelArray[3] = getAlpha(x, y, swt);
                    }
                    raster.setPixels(x, y, 1, 1, pixelArray);
                }
            }
        } else {
            RGB[] rgbs = palette.getRGBs();
            byte[] reds = new byte[rgbs.length];
            byte[] greens = new byte[rgbs.length];
            byte[] blues = new byte[rgbs.length];
            for (int i = 0; i < rgbs.length; i++) {
                RGB rgb = rgbs[i];
                reds[i] = (byte) rgb.red;
                greens[i] = (byte) rgb.green;
                blues[i] = (byte) rgb.blue;
            }

            ColorModel model = null;
            if (swt.transparentPixel != -1) {
                model = new IndexColorModel(swt.depth, rgbs.length, reds,
                        greens, blues, swt.transparentPixel);
            } else {
                model = new IndexColorModel(swt.depth, rgbs.length, reds,
                        greens, blues);
            }
            awt = new BufferedImage(
                    model,
                    model.createCompatibleWritableRaster(swt.width, swt.height),
                    false, null);
            WritableRaster raster = awt.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < swt.height; y++) {
                for (int x = 0; x < swt.width; x++) {
                    int pixel = swt.getPixel(x, y);
                    pixelArray[0] = pixel;
                    raster.setPixel(x, y, pixelArray);
                }
            }
        }
        return awt;
    }

    /**
     * Converts AWT BufferedImage to SWT ImageData.
     * 
     * @param awt
     *        the AWT buffered image to convert.
     * @return SWT image data.
     */
    public static ImageData toImageData(final BufferedImage awt) {
        ImageData data = null;

        if (awt.getColorModel() instanceof DirectColorModel) {
            DirectColorModel model = (DirectColorModel) awt.getColorModel();
            PaletteData palette = new PaletteData(model.getRedMask(),
                    model.getGreenMask(), model.getBlueMask());
            data = new ImageData(awt.getWidth(), awt.getHeight(),
                    model.getPixelSize(), palette);
            WritableRaster raster = awt.getRaster();
            int[] pixels = new int[3];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixels);
                    int pixel = palette.getPixel(new RGB(pixels[0], pixels[1],
                            pixels[2]));
                    data.setPixel(x, y, pixel);
                }
            }
        } else if (awt.getColorModel() instanceof IndexColorModel) {
            IndexColorModel model = (IndexColorModel) awt.getColorModel();
            int size = model.getMapSize();

            byte[] reds = new byte[size];
            model.getReds(reds);

            byte[] greens = new byte[size];
            model.getGreens(greens);

            byte[] blues = new byte[size];
            model.getBlues(blues);

            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
                        blues[i] & 0xFF);
            }

            PaletteData palette = new PaletteData(rgbs);
            data = new ImageData(awt.getWidth(), awt.getHeight(),
                    model.getPixelSize(), palette);
            data.transparentPixel = model.getTransparentPixel();

            WritableRaster raster = awt.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
        }
        return data;
    }

}
