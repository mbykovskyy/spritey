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
package spritey.core.packer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.adapter.AdapterFactory;

/**
 * This strategy tries to put each sprite into first smallest area it finds by
 * dividing empty area into zones. Zones are sorted in ascending order, then
 * sprite is tested against each zone. Redundant zones i.e. zones completely
 * covered by other zones, are removed.
 */
public class DivideAndConquerStrategy implements Strategy {

    private AdapterFactory adapterFactory;
    private Sheet sheet;
    private List<Rectangle> freeZones;

    /**
     * Constructor.
     * 
     * @param sheet
     *        a sprite sheet defining boundaries within which sprites should be
     *        packed.
     * @param adapterFactory
     *        an adapter factory to convert model properties types into standard
     *        java types.
     */
    public DivideAndConquerStrategy(Sheet sheet, AdapterFactory adapterFactory) {
        validateNotNull(sheet, "Sheet is null.");
        validateNotNull(adapterFactory, "Adapter factory is null.");

        this.sheet = sheet;
        this.adapterFactory = adapterFactory;
        freeZones = new ArrayList<Rectangle>();

        flushCache();
    }

    private void validateNotNull(Object o, String msg) {
        if (null == o) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Clears cached values.
     */
    protected void flushCache() {
        Dimension size = (Dimension) adapterFactory.getAdapter(
                sheet.getProperty(Sheet.SIZE), Dimension.class);

        freeZones.clear();
        freeZones.add(new Rectangle(new Point(0, 0), size));
    }

    /**
     * Recalculates each zone.
     * 
     * @param rect
     *        the rectangle representing occupied area.
     */
    protected void recalculateZones(Rectangle rect) {
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
     *        the rectangle to subract.
     * @return the difference.
     */
    protected List<Rectangle> subtract(Rectangle minuend, Rectangle subtrahend) {
        validateNotNull(minuend, "Minuend rectangle is null.");
        validateNotNull(subtrahend, "Subtrahend rectangle is null.");

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
     * Sorts a list of rectangles by their area and removes bounds covered by
     * bigger rectangles.
     * 
     * @param list
     *        a list to sort and filter.
     */
    protected void sortAndFilter(List<Rectangle> list) {
        Collections.sort(list, new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle r1, Rectangle r2) {
                // Do not return 0 (zero) because we don't want to loose
                // rectangles with the same area but different bounds.
                return ((r1.width * r1.height) > (r2.width * r2.height)) ? 1
                        : -1;
            }
        });

        Rectangle[] copy = list.toArray(new Rectangle[list.size()]);

        for (int i = 0; i < copy.length; ++i) {
            for (int j = i + 1; j < copy.length; ++j) {
                // The list is sorted, therefore, the next element in the list
                // will always be either bigger or the same size. If bigger
                // rectangle covers smaller one then remove the small rectangle.
                if (copy[j].contains(copy[i])) {
                    list.remove(copy[i]);
                    break;
                }
            }
        }
    }

    /**
     * Calculates the location of the specified sprite. When the specified
     * sprite does not fit any empty areas, the sprite is left unchanged.
     * 
     * @param sprite
     *        the sprite to position.
     */
    protected void computeLocation(Sprite sprite) {
        Rectangle bounds = (Rectangle) adapterFactory.getAdapter(
                sprite.getProperty(Sprite.BOUNDS), Rectangle.class);
        Rectangle boundsCopy = (Rectangle) bounds.clone();

        for (Rectangle freeZone : freeZones) {
            // Temporarily set location so we can check if sprite fits a zone.
            boundsCopy.setLocation(freeZone.getLocation());

            if (freeZone.contains(boundsCopy)) {
                bounds.setLocation(freeZone.getLocation());
                break;
            }
        }

        // Recalculate zones outside for-loop to avoid concurrent access
        // exception.
        if ((bounds.x != -1) && (bounds.y != -1)) {
            recalculateZones(bounds);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.packer.Strategy#pack(spritey.core.Sprite[], boolean)
     */
    @Override
    public void pack(Sprite[] sprites, boolean flushCache) {
        if (flushCache) {
            flushCache();
        }

        List<Sprite> unlocatedSprites = new ArrayList<Sprite>();

        for (Sprite sprite : sprites) {
            Rectangle bounds = (Rectangle) adapterFactory.getAdapter(
                    sprite.getProperty(Sprite.BOUNDS), Rectangle.class);

            if ((bounds.x == -1) && (bounds.y == -1)) {
                // Unlocated sprites should be positioned last, after all
                // located sprites have been positioned and free zones have been
                // calculated.
                unlocatedSprites.add(sprite);
            } else if (flushCache) {
                recalculateZones(bounds);
            }
        }

        for (Sprite sprite : unlocatedSprites) {
            computeLocation(sprite);
        }
    }

    /**
     * Returns a list of free zones.
     * 
     * @return a list of free zones.
     */
    protected Rectangle[] getFreeZones() {
        return freeZones.toArray(new Rectangle[freeZones.size()]);
    }

}
