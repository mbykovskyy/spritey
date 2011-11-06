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

import spritey.core.Messages;

/**
 * A set of constraints for sprite sheet packer.
 */
public class Constraints {

    public static final int MIN_MAXIMUM_WIDTH = 1;
    public static final int MAX_MAXIMUM_WIDTH = 8192;
    public static final int MIN_MAXIMUM_HEIGHT = 1;
    public static final int MAX_MAXIMUM_HEIGHT = 8192;

    protected static final int DEFAULT_MAXIMUM_WIDTH = MAX_MAXIMUM_WIDTH;
    protected static final int DEFAULT_MAXIMUM_HEIGHT = MAX_MAXIMUM_HEIGHT;
    protected static final boolean DEFAULT_MAINTAIN_POWER_OF_TWO = false;
    protected static final boolean DEFAULT_MAINTAIN_ASPECT_RATIO = false;

    private int maxWidth;
    private int maxHeight;
    private boolean maintainPowerOfTwo;
    private boolean maintainAspectRatio;

    /**
     * Creates a new instance of Constraints with default values.
     */
    public Constraints() {
        this(DEFAULT_MAXIMUM_WIDTH, DEFAULT_MAXIMUM_HEIGHT,
                DEFAULT_MAINTAIN_POWER_OF_TWO, DEFAULT_MAINTAIN_ASPECT_RATIO);
    }

    /**
     * Creates a new instance of Constrains.
     * 
     * @param maxWidth
     *        the maximum width.
     * @param maxHeight
     *        the maximum height.
     * @param maintainPowerOfTwo
     *        specifies whether size should be power of two.
     * @param maintainAspectRatio
     *        specifies whether size should maintain ratio.
     */
    public Constraints(int maxWidth, int maxHeight, boolean maintainPowerOfTwo,
            boolean maintainAspectRatio) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        setMaintainPowerOfTwo(maintainPowerOfTwo);
        setMaintainAspectRatio(maintainAspectRatio);
    }

    /**
     * Returns the maximum size of sprite sheet.
     * 
     * @return the maximum size of the sprite sheet.
     */
    public Dimension getMaxSize() {
        return new Dimension(getMaxWidth(), getMaxHeight());
    }

    /**
     * Returns the maximum width of sprite sheet specified by the user.
     * 
     * @return the maximum width of the sprite sheet.
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Sets the maximum width of the sprite sheet.
     * 
     * @param maxWidth
     *        the maximum width to set.
     * @throws IllegalArgumentException
     *         when the given <code>maxWidth</code> is not within the
     *         MIN_MAXIMUM_WIDTH and MAX_MAXIMUM_WIDTH range; or when sheet is
     *         set to maintain power of two but <code>maxWidth</code> is not
     *         power of two.
     */
    public void setMaxWidth(final int maxWidth) {
        if (getMaxWidth() != maxWidth) {
            if ((maxWidth < MIN_MAXIMUM_WIDTH)
                    || (maxWidth > MAX_MAXIMUM_WIDTH)) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.CONSTRAINTS_MAX_WIDTH_INVALID_RANGE,
                        MIN_MAXIMUM_WIDTH, MAX_MAXIMUM_WIDTH));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(maxWidth)) {
                throw new IllegalArgumentException(
                        Messages.CONSTRAINTS_MAX_WIDTH_INVALID_POWER_OF_TWO);
            }
            this.maxWidth = maxWidth;
        }
    }

    /**
     * Returns the maximum height of sprite sheet specified by the user.
     * 
     * @return the maximum height of the sprite sheet.
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the maximum height of the sprite sheet.
     * 
     * @param maxHeight
     *        the maximum height to set.
     * @throws IllegalArgumentException
     *         when the given <code>maxHeight</code> is not within the
     *         MIN_MAXIMUM_HEIGHT and MAX_MAXIMUM_HEIGHT range; or when sheet is
     *         set to maintain power of two but <code>maxHeight</code> is not
     *         power of two.
     */
    public void setMaxHeight(final int maxHeight) {
        if (getMaxHeight() != maxHeight) {
            if ((maxHeight < MIN_MAXIMUM_HEIGHT)
                    || (maxHeight > MAX_MAXIMUM_HEIGHT)) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.CONSTRAINTS_MAX_HEIGHT_INVALID_RANGE,
                        MIN_MAXIMUM_HEIGHT, MAX_MAXIMUM_HEIGHT));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(maxHeight)) {
                throw new IllegalArgumentException(
                        Messages.CONSTRAINTS_MAX_HEIGHT_INVALID_POWER_OF_TWO);
            }
            this.maxHeight = maxHeight;
        }
    }

    /**
     * Returns <code>true</code> if sprite sheet dimensions have to be power of
     * two.
     * 
     * @return <code>true</code> if sprite sheet has to be power of two,
     *         otherwise <code>false</code>.
     */
    public boolean maintainPowerOfTwo() {
        return maintainPowerOfTwo;
    }

    /**
     * Sets whether sprite sheet size has to be power of two.
     * 
     * @param isPowerOfTwo
     *        specifies whether to keep sprite sheet size to be power of two.
     */
    public void setMaintainPowerOfTwo(final boolean isPowerOfTwo) {
        if (maintainPowerOfTwo() != isPowerOfTwo) {
            if (isPowerOfTwo) {
                if (!isPowerOfTwo(getMaxWidth())) {
                    throw new IllegalArgumentException(
                            Messages.CONSTRAINTS_MAX_WIDTH_INVALID_POWER_OF_TWO);
                } else if (!isPowerOfTwo(getMaxHeight())) {
                    throw new IllegalArgumentException(
                            Messages.CONSTRAINTS_MAX_HEIGHT_INVALID_POWER_OF_TWO);
                }
            }
            maintainPowerOfTwo = isPowerOfTwo;
        }
    }

    /**
     * Returns <code>true</code> if sprite sheet has to maintain aspect ratio
     * when resizing.
     * 
     * @return <code>true</code> is aspect ratio is maintained when sprite sheet
     *         is resized, otherwise <code>false</code>.
     */
    public boolean maintainAspectRatio() {
        return maintainAspectRatio;
    }

    /**
     * Sets whether sprite sheet should maintain aspect ratio when resizing.
     * 
     * @param maintainRatio
     *        specifies whether to maintain aspect ratio.
     */
    public void setMaintainAspectRatio(final boolean maintainRatio) {
        if (maintainAspectRatio() != maintainRatio) {
            maintainAspectRatio = maintainRatio;
        }
    }

    /**
     * Returns the aspect ratio <code>maxWidth:maxHeight</code>.
     * 
     * @return the aspect ratio.
     */
    public float getAspectRatio() {
        return ((float) maxWidth) / maxHeight;
    }

    /**
     * Returns <code>true</code> if specified value is power of two.
     * 
     * @param value
     *        the value to check.
     * @return <code>true</code> if value is power of two, otherwise
     *         <code>false</code>.
     */
    public static boolean isPowerOfTwo(final int value) {
        return (value & -value) == value;
    }

    /**
     * Returns the next power of two of the specified value.
     * 
     * @param value
     *        the value to round up to the next power of two.
     * @return the next power of two.
     */
    public static int nextPowerOfTwo(final int value) {
        return 1 << 32 - Integer.numberOfLeadingZeros(value);
    }

}
