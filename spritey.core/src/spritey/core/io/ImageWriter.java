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
package spritey.core.io;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import spritey.core.Messages;
import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.filter.VisibleSpriteFilter;

/**
 * Writer for saving sprite sheet image to disk.
 */
public class ImageWriter implements Writer {

    /**
     * Returns the file extension.
     * 
     * @param file
     *        the file to get extension for.
     * @return file extension.
     */
    protected String getFileExt(File file) {
        String[] parts = file.getName().split("\\.");
        return parts[parts.length - 1];
    }

    /**
     * Returns <code>true</code> if the specified file is GIF.
     * 
     * @param file
     *        the file to check.
     * @return <code>true</code> if the file is GIF, otherwise
     *         <code>false</code>.
     */
    protected boolean isGif(File file) {
        return getFileExt(file).equalsIgnoreCase("gif");
    }

    /**
     * Returns <code>true</code> if the specified file is PNG.
     * 
     * @param file
     *        the file to check.
     * @return <code>true</code> if the file is PNG, otherwise
     *         <code>false</code>.
     */
    protected boolean isPng(File file) {
        return getFileExt(file).equalsIgnoreCase("png");
    }

    /**
     * Returns <code>true</code> if the specified file supports transparency.
     * 
     * @param file
     *        the file to check.
     * @return <code>true</code> if file supports transparency, otherwise
     *         <code>false</code>.
     */
    protected boolean supportsTransparency(File file) {
        return (isGif(file) || isPng(file));
    }

    /**
     * Creates an index color model for gif images that supports transparency.
     * This method does exactly what BufferedImage does when TYPE_BYTE_INDEXED
     * type is specified, except the first color is transparent and the gray
     * ramp is between 18 and 246.
     * 
     * @return an instance of index color model.
     */
    protected IndexColorModel createIndexColorModelWithTransparency() {
        int[] cmap = new int[256];

        // First color is transparent.
        cmap[0] = 0;

        // Create a 6x6x6 color cube.
        int i = 1;
        for (int r = 0; r < 256; r += 51) {
            for (int g = 0; g < 256; g += 51) {
                for (int b = 0; b < 256; b += 51) {
                    cmap[i++] = (r << 16) | (g << 8) | b;
                }
            }
        }

        // Populate the rest of the cmap with gray values.
        int grayIncr = 256 / (256 - i);

        // Gray ramp will be between 18 and 246.
        int gray = grayIncr * 3;
        for (; i < 256; i++) {
            cmap[i] = (gray << 16) | (gray << 8) | gray;
            gray += grayIncr;
        }

        return new IndexColorModel(8, 256, cmap, 0, true, 0,
                DataBuffer.TYPE_BYTE);
    }

    @Override
    public void write(Sheet sheet, File file) throws FileNotFoundException,
            IOException {
        int width = sheet.getWidth();
        int height = sheet.getHeight();
        boolean isOpaque = sheet.getBackground().getAlpha() == 255;

        BufferedImage image = null;
        if (isGif(file)) {
            image = new BufferedImage(width, height,
                    BufferedImage.TYPE_BYTE_INDEXED,
                    createIndexColorModelWithTransparency());
        } else if (!isOpaque && isPng(file)) {
            image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        Graphics gfx = image.getGraphics();

        if (isOpaque) {
            gfx.setColor(sheet.getBackground());
            gfx.fillRect(0, 0, width, height);
        }

        for (Node child : new VisibleSpriteFilter().filter(sheet)) {
            draw(child, gfx);
        }

        gfx.dispose();

        if (!ImageIO.write(image, getFileExt(file), file)) {
            throw new IOException(Messages.IMAGE_WRITER_NO_WRITER_FOUND);
        }
    }

    /**
     * Draws the specified node onto the specified graphics context.
     * 
     * @param model
     *        the node to draw.
     * @param gfx
     *        graphics context to draw on.
     */
    protected void draw(Node node, Graphics gfx) {
        if (node instanceof Sprite) {
            Sprite sprite = (Sprite) node;
            Point location = sprite.getLocation();
            gfx.drawImage(sprite.getImage(), location.x, location.y, null);
        }
    }

}
