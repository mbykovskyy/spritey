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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spritey.core.Messages;

/**
 * This strategy tries to put each sprite as high as possible by sorting free
 * zones according to their Y position in ascending order. When sprite doesn't
 * fit into any zone the highest zone is expanded just enough to fit the sprite.
 */
public class HighestFitStrategy extends WidestFirstStrategy {

    /**
     * Sorts the specified list of rectangles by the highest and left most
     * position.
     * 
     * @param zones
     *        the list to sort.
     */
    @Override
    protected void sortZones(List<Rectangle> zones) {
        Collections.sort(zones, new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle r1, Rectangle r2) {
                int difference = r1.y - r2.y;

                if (0 == difference) {
                    difference = r1.x - r2.x;
                }
                return difference;
            }
        });
    }

    /**
     * Tries to expand the highest zone which can fit the specified rectangle.
     * When no zones can be expanded either width or height is expanded.
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
                int expandWidthBy = rect.width - zone.width;

                if (currentSize.width + expandWidthBy <= maxSize.width) {
                    return new Dimension(expandWidthBy, 0);
                }
            } else if ((zone.width >= rect.width) && canExpandHeight) {
                int expandHeightBy = rect.height - zone.height;

                if (currentSize.height + expandHeightBy <= maxSize.height) {
                    return new Dimension(0, expandHeightBy);
                }
            } else if (canExpandWidth && canExpandHeight) {
                // Zone width and height are not big enough for rectangle but
                // both dimensions can be expanded.
                int expandWidthBy = rect.width - zone.width;
                int expandHeightBy = rect.height - zone.height;

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
        boolean canExpandWidth = (currentSize.width + rect.width <= maxSize.width);
        boolean canExpandHeight = (currentSize.height + rect.height <= maxSize.height);
        boolean shouldExpandHeight = currentSize.width > currentSize.height;

        if ((canExpandHeight && shouldExpandHeight)
                || (canExpandHeight && !canExpandWidth)) {
            if (rect.width > currentSize.width) {
                if (rect.width <= maxSize.width) {
                    return new Dimension(rect.width - currentSize.width,
                            rect.height);
                }
                // Rectangle width is bigger than maximum width.
                throw new SizeTooSmallException(
                        Messages.PACKER_SHEET_SIZE_TOO_SMALL);
            }
            return new Dimension(0, rect.height);
        } else if (canExpandWidth) {
            if (rect.height > currentSize.height) {
                if (rect.height <= maxSize.height) {
                    return new Dimension(rect.width, rect.height
                            - currentSize.height);
                }
                // Rectangle height is bigger than maximum height. We have to
                // terminate packing as not all sprites can fit into sprite
                // sheet.
                throw new SizeTooSmallException(
                        Messages.PACKER_SHEET_SIZE_TOO_SMALL);
            }
            return new Dimension(rect.width, 0);
        }
        throw new SizeTooSmallException(Messages.PACKER_SHEET_SIZE_TOO_SMALL);
    }

}
