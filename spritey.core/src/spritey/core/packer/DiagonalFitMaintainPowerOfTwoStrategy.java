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
package spritey.core.packer;

import static spritey.core.packer.Constraints.isPowerOfTwo;
import static spritey.core.packer.Constraints.nextPowerOfTwo;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spritey.core.Messages;

/**
 * This packer tries to put each sprite as close to the top-left corner as
 * possible by sorting free zones by the distance from the origin in ascending
 * order. When sprite doesn't fit into any zone the closest zone is expanded
 * just enough to fit the sprite and size is adjusted to the next power of two.
 */
public class DiagonalFitMaintainPowerOfTwoStrategy extends WidestFirstStrategy {

    /**
     * Adjusts the value to expand the width by to make the end result be the
     * power of two.
     * 
     * @param width
     *        the value to adjust.
     * @return the value to expand the width by to make the end result be the
     *         power of two.
     */
    protected int adjustWidth(int width) {
        int nextWidth = currentSize.width + width;

        return isPowerOfTwo(nextWidth) ? width : nextPowerOfTwo(nextWidth)
                - currentSize.width;
    }

    /**
     * Adjusts the value to expand the height by to make the end result be the
     * power of two.
     * 
     * @param height
     *        the value to adjust.
     * @return the value to expand the height by to make the end result be the
     *         power of two.
     */
    protected int adjustHeight(int height) {
        int nextHeight = currentSize.height + height;

        return isPowerOfTwo(nextHeight) ? height : nextPowerOfTwo(nextHeight)
                - currentSize.height;
    }

    /**
     * Sorts the specified list of rectangles by the closest to the top-left
     * corner.
     * 
     * @param zones
     *        the list of zones to sort.
     */
    @Override
    protected void sortZones(List<Rectangle> zones) {
        Collections.sort(zones, new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle r1, Rectangle r2) {
                return (r1.x + r1.y) - (r2.x + r2.y);
            }
        });
    }

    /**
     * Tries to expand zone closest to the top-left corner. When no zones can be
     * expanded either width or height is expanded. Size is then adjusted to
     * power of two.
     * 
     * @param rect
     *        the rectangle that has to fit after the expansion.
     * @throws SizeTooSmallException
     *         when sheet size is too small to fit all sprites.
     */
    @Override
    protected Dimension expandBy(Rectangle rect) throws SizeTooSmallException {
        Dimension maxSize = constraints.getMaxSize();

        for (Rectangle zone : freeZones) {
            boolean canExpandWidth = (zone.x + zone.width == currentSize.width);
            boolean canExpandHeight = (zone.y + zone.height == currentSize.height);

            if ((zone.height >= rect.height) && canExpandWidth) {
                // Zone height is big enough for rectangle and zone width can be
                // expanded, but will sheet size be still within maximum size
                // constraints?
                int expandWidthBy = adjustWidth(rect.width - zone.width);

                if (currentSize.width + expandWidthBy <= maxSize.width) {
                    return new Dimension(expandWidthBy, 0);
                }
            } else if ((zone.width >= rect.width) && canExpandHeight) {
                int expandHeightBy = adjustHeight(rect.height - zone.height);

                if (currentSize.height + expandHeightBy <= maxSize.height) {
                    return new Dimension(0, expandHeightBy);
                }
            } else if (canExpandWidth && canExpandHeight) {
                // Zone width and height are not big enough for rectangle but
                // both dimensions can be expanded.
                int expandWidthBy = adjustWidth(rect.width - zone.width);
                int expandHeightBy = adjustHeight(rect.height - zone.height);

                if ((currentSize.width + expandWidthBy <= maxSize.width)
                        && (currentSize.height + expandHeightBy <= maxSize.height)) {
                    return new Dimension(expandWidthBy, expandHeightBy);
                }
            }
        }
        // If we reach this point it means one of the following, either there
        // are no zones, none of zones can be expanded or expanding a zone will
        // break the maximum size constraint. Try to expand either width or
        // height, what ever is the smallest, one last time. It could be the
        // case that we almost reached the maximum size of one dimension and the
        // other dimension does not have any expandable zones.
        boolean canExpandWidth = (currentSize.width + adjustWidth(rect.width) <= maxSize.width);
        boolean canExpandHeight = (currentSize.height
                + adjustHeight(rect.height) <= maxSize.height);
        boolean shouldExpandHeight = currentSize.width > currentSize.height;

        if ((canExpandHeight && shouldExpandHeight)
                || (canExpandHeight && !canExpandWidth)) {
            if (rect.width > currentSize.width) {
                int expandWidthBy = adjustWidth(rect.width - currentSize.width);

                if (currentSize.width + expandWidthBy <= maxSize.width) {
                    return new Dimension(expandWidthBy,
                            adjustHeight(rect.height));
                }
                // Rectangle width is bigger than maximum width.
                throw new SizeTooSmallException(
                        Messages.PACKER_SHEET_SIZE_TOO_SMALL);
            }
            return new Dimension(0, adjustHeight(rect.height));
        } else if (canExpandWidth) {
            if (rect.height > currentSize.height) {
                int expandHeightBy = adjustHeight(rect.height
                        - currentSize.height);

                if (currentSize.height + expandHeightBy <= maxSize.height) {
                    return new Dimension(adjustWidth(rect.width),
                            expandHeightBy);
                }
                // Rectangle height is bigger than maximum height. We have to
                // terminate packing as not all sprites can fit into sprite
                // sheet.
                throw new SizeTooSmallException(
                        Messages.PACKER_SHEET_SIZE_TOO_SMALL);
            }
            return new Dimension(adjustWidth(rect.width), 0);
        }
        throw new SizeTooSmallException(Messages.PACKER_SHEET_SIZE_TOO_SMALL);
    }

}
