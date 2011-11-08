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

import spritey.core.Messages;

/**
 * This packer tries to put each sprite as close to the top-left corner as
 * possible by sorting free zones by the distance from the origin in ascending
 * order. When sprite doesn't fit into any zone the closest zone is expanded
 * just enough to fit the sprite and size is adjusted to the next power of two
 * and to maintain aspect ratio.
 */
public class DiagonalFitMaintainAspectRatioAndPowerOfTwoStrategy extends
        DiagonalFitMaintainPowerOfTwoStrategy {

    @Override
    protected Dimension expandBy(Rectangle rect) throws SizeTooSmallException {
        Dimension maxSize = constraints.getMaxSize();
        float ratio = constraints.getAspectRatio();

        for (Rectangle zone : freeZones) {
            boolean canExpandWidth = (zone.x + zone.width == currentSize.width);
            boolean canExpandHeight = (zone.y + zone.height == currentSize.height);

            if ((zone.height >= rect.height) && canExpandWidth) {
                // Zone height is big enough for rectangle and zone width can be
                // expanded.
                int expandWidthBy = adjustWidth(rect.width - zone.width);

                if (currentSize.width + expandWidthBy <= maxSize.width) {
                    // Adjust height to maintain aspect ratio.
                    int expandHeightBy = (int) (expandWidthBy / ratio);

                    if ((currentSize.height + expandHeightBy <= maxSize.height)) {
                        return new Dimension(expandWidthBy, expandHeightBy);
                    }
                }
            } else if ((zone.width >= rect.width) && canExpandHeight) {
                int expandHeightBy = adjustHeight(rect.height - zone.height);

                if (currentSize.height + expandHeightBy <= maxSize.height) {
                    int expandWidthBy = (int) (expandHeightBy * ratio);

                    if ((currentSize.width + expandWidthBy <= maxSize.width)) {
                        return new Dimension(expandWidthBy, expandHeightBy);
                    }
                }
            } else if (canExpandWidth && canExpandHeight) {
                // Zone width and height are not big enough to fit rectangle but
                // both dimensions can be expanded.
                int expandWidthBy = adjustWidth(rect.width - zone.width);
                int expandHeightBy = adjustHeight(rect.height - zone.height);

                // For optimisation sake check if expanding width and height
                // without maintaining aspect ratio doesn't break maximum size
                // constraints.
                if ((currentSize.width + expandWidthBy <= maxSize.width)
                        && (currentSize.height + expandHeightBy <= maxSize.height)) {

                    if ((expandWidthBy / ratio) >= expandHeightBy) {
                        // Height has to be adjusted to maintain aspect ratio.
                        expandHeightBy = (int) (expandWidthBy / ratio);

                        if (currentSize.height + expandHeightBy <= maxSize.height) {
                            return new Dimension(expandWidthBy, expandHeightBy);
                        }
                    } else {
                        expandWidthBy = (int) (expandHeightBy * ratio);

                        if ((currentSize.width + expandWidthBy <= maxSize.width)) {
                            return new Dimension(expandWidthBy, expandHeightBy);
                        }
                    }
                }
            }
        }
        // If we reach this point it means that either there are no zones, none
        // of zones can be expanded or expanding a zone will break the maximum
        // size constraint. Try to expand either width or height, it could be
        // the case that there are no expandable zones.
        int expandWidthBy = adjustWidth(rect.width);
        int expandHeightBy = adjustHeight(rect.height);

        if ((currentSize.width + expandWidthBy <= maxSize.width)
                && ((expandWidthBy / ratio) >= expandHeightBy)) {
            // Adjust height to maintain aspect ratio.
            expandHeightBy = (int) (expandWidthBy / ratio);

            if (currentSize.height + expandHeightBy <= maxSize.height) {
                return new Dimension(expandWidthBy, expandHeightBy);
            }
            // Rectangle width fits but adjusted height breaks the maximum size
            // constraint.
            throw new SizeTooSmallException(
                    Messages.PACKER_SHEET_SIZE_TOO_SMALL);
        } else if ((currentSize.height + expandHeightBy <= maxSize.height)
                && ((expandHeightBy * ratio) >= expandWidthBy)) {
            expandWidthBy = (int) (expandHeightBy * ratio);

            if ((currentSize.width + expandWidthBy <= maxSize.width)) {
                return new Dimension(expandWidthBy, expandHeightBy);
            }
            throw new SizeTooSmallException(
                    Messages.PACKER_SHEET_SIZE_TOO_SMALL);
        }

        throw new SizeTooSmallException(Messages.PACKER_SHEET_SIZE_TOO_SMALL);
    }

}
