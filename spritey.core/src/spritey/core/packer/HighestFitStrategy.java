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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spritey.core.Messages;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.filter.SpriteFilter;

/**
 * This packer tries to put each sprite as high as possible by dividing empty
 * area into zones. Zones are sorted according to their Y position in ascending
 * order, then sprite is tested against each zone. When sprite doesn't fist into
 * any zone the highest zone is expanded just enough to fit the sprite.
 * Redundant zones i.e. zones completely covered by other zones, are removed.
 */
public class HighestFitStrategy implements Strategy {

    protected Constraints constraints;
    protected Dimension currentSize;
    protected List<Rectangle> freeZones;

    /**
     * Creates a new instance of HighestFitPacker.
     */
    public HighestFitStrategy() {
        currentSize = new Dimension();
        freeZones = new ArrayList<Rectangle>();
    }

    /**
     * Validates that the specified object is not null.
     * 
     * @param o
     *        the object to validate.
     * @param msg
     *        the exception message.
     */
    private void validateNotNull(Object o, String msg) {
        if (null == o) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Sorts sprites by their width in descending order.
     * 
     * @param sprites
     *        a list of sprites to sort.
     */
    private void sort(List<Sprite> sprites) {
        Collections.sort(sprites, new Comparator<Sprite>() {
            @Override
            public int compare(Sprite s1, Sprite s2) {
                return s2.getSize().width - s1.getSize().width;
            }
        });
    }

    /**
     * Calculates the location where specified rectangle fits. When rectangle
     * doesn't fit <code>null</code> is returned;
     * 
     * @param rect
     *        the rectangle to position.
     * @return the location where rectangle fits, if <code>null</code> is
     *         returned the rectangle does not fit.
     */
    private Point computeLocation(Rectangle rect) {
        for (Rectangle zone : freeZones) {
            rect.setLocation(zone.getLocation());

            if (zone.contains(rect)) {
                return zone.getLocation();
            }
        }
        return null;
    }

    /**
     * Recalculates each zone. It will modify <code>freeZones</code> list, so be
     * careful when calling this while reading from <code>freeZones</code>.
     * 
     * @param rect
     *        the rectangle representing occupied area.
     */
    private void recalculateZones(Rectangle rect) {
        List<Rectangle> newZones = new ArrayList<Rectangle>();

        for (Rectangle zone : freeZones) {
            newZones.addAll(subtract(zone, rect));
        }

        sortAndFilter(newZones);
        freeZones = newZones;
    }

    /**
     * Subtracts one rectangle from another. The returned rectangles may look
     * like r1, r2, r3, r4 below,
     * 
     * <pre>
     * +---------+------------+------------+
     * |         |            |            |
     * |         |            |            |
     * |         |            |            |
     * |         +------------+            |
     * |         |            |            |
     * |   r1    | subtrahend |     r2     |
     * |         |            |            |
     * |         +------------+            |
     * |         |            |            |
     * |         |            |            |
     * |         |            |            |
     * +---------+------------+------------+
     * 
     * +-----------------------------------+
     * |                                   |
     * |               r3                  |
     * |                                   |
     * +---------+------------+------------+
     * |         |            |            |
     * |         | subtrahend |            |
     * |         |            |            |
     * +---------+------------+------------+
     * |                                   |
     * |               r4                  |
     * |                                   |
     * +-----------------------------------+
     * </pre>
     * 
     * When two rectangles are not intersecting the <code>minuend</code>
     * rectangle is returned.
     * 
     * @param minuend
     *        the rectangle to subtract from.
     * @param subtrahend
     *        the rectangle to subtract.
     * @return the difference.
     */
    protected List<Rectangle> subtract(Rectangle minuend, Rectangle subtrahend) {
        validateNotNull(minuend, Messages.NULL);
        validateNotNull(subtrahend, Messages.NULL);

        List<Rectangle> differences = new ArrayList<Rectangle>();

        if (!subtrahend.intersects(minuend)) {
            // The subtrahend rectangle does not intersect minuend rectangle
            // therefore the difference will be minuend rectangle. This is
            // similar to 5 - 0 = 5.
            differences.add(minuend);
        } else if (subtrahend.contains(minuend)) {
            // If subtrahend rectangle entirely covers the minuend rectangle
            // then there's no need for subtraction as there'll be no difference
            // left. This is similar to 4 - 6 = -2, but instead of the negative
            // rectangle we return an empty list of differences to indicate that
            // minuend has been completely "eaten" by subtrahend rectangle.
        } else {
            // The subtrahend intersects the minuend, therefore, we need to get
            // the intersection and subtract that intersection from minuend.
            // Note that if subtrahend is completely contained within minuend
            // rectangle then intersection == minuend.
            Rectangle intersection = minuend.intersection(subtrahend);

            // @formatter:off
            // +---------+------------+------------+
            // |         |            |            |
            // |         |            |            |
            // |         |            |            |
            // |         +------------+            |
            // |         |            |            |
            // |   r1    | subtrahend |     r2     |
            // |         |            |            |
            // |         +------------+            |
            // |         |            |            |
            // |         |            |            |
            // |         |            |            |
            // +---------+------------+------------+
            // @formatter:on
            int width = intersection.x - minuend.x;
            if (width > 0) {
                differences.add(new Rectangle(minuend.x, minuend.y, width,
                        minuend.height));
            }

            width = minuend.x + minuend.width - intersection.x
                    - intersection.width;
            if (width > 0) {
                differences
                        .add(new Rectangle(intersection.x + intersection.width,
                                minuend.y, width, minuend.height));
            }

            // @formatter:off
            // +-----------------------------------+
            // |                                   |
            // |               r3                  |
            // |                                   |
            // +---------+------------+------------+
            // |         |            |            |
            // |         | subtrahend |            |
            // |         |            |            |
            // +---------+------------+------------+
            // |                                   |
            // |               r4                  |
            // |                                   |
            // +-----------------------------------+
            // @formatter:on
            int height = intersection.y - minuend.y;
            if (height > 0) {
                differences.add(new Rectangle(minuend.x, minuend.y,
                        minuend.width, height));
            }

            height = minuend.y + minuend.height - intersection.y
                    - intersection.height;
            if (height > 0) {
                differences.add(new Rectangle(minuend.x, intersection.y
                        + intersection.height, minuend.width, height));
            }
        }

        return differences;
    }

    /**
     * Sorts the specified list by rectangle are in ascending order and removes
     * rectangles covered by bigger rectangles.
     * 
     * @param zones
     *        the list of rectangles to remove overlapping rectangles in.
     */
    private void removeOverlappingZones(List<Rectangle> zones) {
        // Sort rectangles by their area in ascending order to make removing
        // overlapping rectangles easier.
        Collections.sort(zones, new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle r1, Rectangle r2) {
                return r1.width * r1.height - r2.width * r2.height;
            }
        });

        Rectangle[] copy = zones.toArray(new Rectangle[zones.size()]);

        for (int i = 0; i < copy.length; ++i) {
            for (int j = i + 1; j < copy.length; ++j) {
                // The list is sorted, therefore, the next element in the list
                // will always be either bigger or the same size. If bigger
                // rectangle covers smaller one then remove the small rectangle.
                if (copy[j].contains(copy[i])) {
                    zones.remove(copy[i]);
                    break;
                }
            }
        }
    }

    /**
     * Sorts the specified list of rectangles by the highest and left most
     * position.
     * 
     * @param zones
     *        the list to sort.
     */
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
     * Sorts rectangles by the highest and left most, and removes bounds covered
     * by bigger rectangles.
     * 
     * @param list
     *        a list to sort and filter.
     */
    private void sortAndFilter(List<Rectangle> list) {
        removeOverlappingZones(list);
        sortZones(list);
    }

    /**
     * Calculates the width and height by which sheet has to be expanded to fit
     * the specified rectangle.
     * 
     * @param rect
     *        the rectangle that has to fit after the expansion.
     * @throws SizeTooSmallException
     *         when sheet size is too small to fit all sprites.
     */
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

    /**
     * Expands zones located along the sheet's east and south boarder.
     * 
     * @param expandBy
     *        the dimension which specifies how much width and height needs to
     *        be expanded.
     */
    private void expandZones(Dimension expandBy) {
        for (Rectangle zone : freeZones) {
            if (zone.x + zone.width + expandBy.width == currentSize.width) {
                zone.width += expandBy.width;
            }
            if (zone.y + zone.height + expandBy.height == currentSize.height) {
                zone.height += expandBy.height;
            }
        }

        if (expandBy.width > 0) {
            freeZones.add(new Rectangle(currentSize.width - expandBy.width, 0,
                    expandBy.width, currentSize.height));
        }
        if (expandBy.height > 0) {
            freeZones.add(new Rectangle(0,
                    currentSize.height - expandBy.height, currentSize.width,
                    expandBy.height));
        }
        // For the sake of optimisation there's no need to sort and filter zones
        // because they will be sorted and filtered after the next sprite is
        // positioned.
    }

    /**
     * Clears cached values.
     */
    private void flushCache() {
        freeZones.clear();
        currentSize.setSize(0, 0);
    }

    @Override
    public void pack(Sheet sheet, Constraints constraints)
            throws IllegalArgumentException, SizeTooSmallException {
        validateNotNull(sheet, Messages.NULL);
        validateNotNull(constraints, Messages.NULL);

        Sprite[] sprites = new SpriteFilter().filter(sheet);
        if (0 == sprites.length) {
            return;
        }

        List<Sprite> spriteList = Arrays.asList(sprites);
        sort(spriteList);

        this.constraints = constraints;
        flushCache();

        for (Sprite sprite : spriteList) {
            Point location = computeLocation(sprite.getBounds());

            if (null != location) {
                sprite.setLocation(location);
                recalculateZones(sprite.getBounds());
            } else {
                // Expand sprite sheet and try again.
                Dimension expandBy = expandBy(sprite.getBounds());

                currentSize.setSize(currentSize.width + expandBy.width,
                        currentSize.height + expandBy.height);

                expandZones(expandBy);

                location = computeLocation(sprite.getBounds());
                if (null == location) {
                    throw new SizeTooSmallException(
                            Messages.PACKER_SHEET_SIZE_TOO_SMALL);
                }

                sprite.setLocation(location);
                recalculateZones(sprite.getBounds());
            }
        }

        sheet.setWidth(currentSize.width);
        sheet.setHeight(currentSize.height);
    }

}
