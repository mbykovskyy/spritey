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
package spritey.core;

import java.awt.Color;

/**
 * Sheet is a top level container which contains sprites.
 */
public class Sheet extends Node {

    public static final int MIN_DESCRIPTION_LENGTH = 0;
    public static final int MAX_DESCRIPTION_LENGTH = 1024;
    public static final int MIN_MAXIMUM_WIDTH = 1;
    public static final int MAX_MAXIMUM_WIDTH = 8192;
    public static final int MIN_MAXIMUM_HEIGHT = 1;
    public static final int MAX_MAXIMUM_HEIGHT = 8192;
    public static final int MIN_WIDTH = MIN_MAXIMUM_WIDTH;
    public static final int MIN_HEIGHT = MIN_MAXIMUM_HEIGHT;

    protected static final String DEFAULT_NAME = Messages.SHEET_DEFAULT_NAME;
    protected static final Color DEFAULT_BACKGROUND = new Color(255, 0, 255);
    protected static final int DEFAULT_MAXIMUM_WIDTH = MAX_MAXIMUM_WIDTH;
    protected static final int DEFAULT_MAXIMUM_HEIGHT = MAX_MAXIMUM_HEIGHT;
    protected static final int DEFAULT_WIDTH = MIN_WIDTH;
    protected static final int DEFAULT_HEIGHT = MIN_HEIGHT;
    protected static final String DEFAULT_DESCRIPTION = Messages.SHEET_DEFAULT_DESCRIPTION;
    protected static final boolean DEFAULT_MAINTAIN_POWER_OF_TWO = false;
    protected static final boolean DEFAULT_MAINTAIN_ASPECT_RATIO = false;

    public static final Color TRANSPARENT_BACKGROUND = new Color(0, 0, 0, 0);

    private Color background;
    private int maxWidth;
    private int maxHeight;
    private int width;
    private int height;
    private String description;
    private boolean maintainPowerOfTwo;
    private boolean maintainAspectRatio;

    /**
     * Creates a new instance of <code>Sheet</code> with default settings.
     */
    public Sheet() {
        this(DEFAULT_BACKGROUND, DEFAULT_MAXIMUM_WIDTH, DEFAULT_MAXIMUM_HEIGHT,
                DEFAULT_MAINTAIN_POWER_OF_TWO, DEFAULT_MAINTAIN_ASPECT_RATIO,
                DEFAULT_DESCRIPTION);
    }

    /**
     * Creates a new instance of <code>Sheet</code>.
     * 
     * @param background
     *        the background color.
     * @param maxWidth
     *        the maximum width.
     * @param maxHeight
     *        the maximum height.
     * @param maintainPowerOfTwo
     *        specifies whether size should be power of two.
     * @param maintainAspectRatio
     *        specifies whether size should maintain ratio.
     * @param description
     *        the short description or comment.
     */
    public Sheet(final Color background, final int maxWidth,
            final int maxHeight, final boolean maintainPowerOfTwo,
            final boolean maintainAspectRatio, final String description) {
        super(DEFAULT_NAME);
        setBackground(background);
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
        setMaintainPowerOfTwo(maintainPowerOfTwo);
        setMaintainAspectRatio(maintainAspectRatio);
        setDescription(description);
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
    }

    /**
     * Returns the background color.
     * 
     * @return the background color.
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Sets the background color.
     * 
     * @param background
     *        the color to set.
     * @throws IllegalArgumentException
     *         when the specified <code>background</code> is <code>null</code>.
     */
    public void setBackground(final Color background) {
        if ((null == getBackground()) || !getBackground().equals(background)) {
            if (null == background) {
                throw new IllegalArgumentException(Messages.NULL);
            }
            this.background = background;
        }
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
                        Messages.SHEET_MAX_WIDTH_INVALID_RANGE,
                        MIN_MAXIMUM_WIDTH, MAX_MAXIMUM_WIDTH));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(maxWidth)) {
                throw new IllegalArgumentException(
                        Messages.SHEET_MAX_WIDTH_INVALID_POWER_OF_TWO);
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
                        Messages.SHEET_MAX_HEIGHT_INVALID_RANGE,
                        MIN_MAXIMUM_HEIGHT, MAX_MAXIMUM_HEIGHT));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(maxHeight)) {
                throw new IllegalArgumentException(
                        Messages.SHEET_MAX_HEIGHT_INVALID_POWER_OF_TWO);
            }
            this.maxHeight = maxHeight;
        }
    }

    /**
     * Returns the current width of the sprite sheet.
     * 
     * @return the current width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the current width of the sprite sheet.
     * 
     * @param width
     *        the current width.
     * @throws IllegalArgumentException
     *         when given width is either less than minimum width allowed or
     *         greater than maximum width specified by the user via
     *         <code>setMaxWidth()</code>; or when sheet is set to maintain
     *         power of two but <code>width</code> is not power of two.
     */
    public void setWidth(final int width) {
        if (getWidth() != width) {
            if ((width < MIN_WIDTH) || (width > getMaxWidth())) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.SHEET_WIDTH_INVALID_RANGE, MIN_WIDTH,
                        getMaxWidth()));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(width)) {
                throw new IllegalArgumentException(
                        Messages.SHEET_WIDTH_INVALID_POWER_OF_TWO);
            }
            this.width = width;
        }
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the current height of the sprite sheet.
     * 
     * @param height
     *        the current height.
     * @throws IllegalArgumentException
     *         when given width is either less than minimum height allowed or
     *         greater than maximum height specified by the user via
     *         <code>setMaxHeight()</code>; or when sheet is set to maintain
     *         power of two but <code>height</code> is not power of two.
     */
    public void setHeight(final int height) {
        if (getHeight() != height) {
            if ((height < MIN_HEIGHT) || (height > getMaxHeight())) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.SHEET_HEIGHT_INVALID_RANGE, MIN_HEIGHT,
                        getMaxHeight()));
            } else if (maintainPowerOfTwo() && !isPowerOfTwo(height)) {
                throw new IllegalArgumentException(
                        Messages.SHEET_HEIGHT_INVALID_POWER_OF_TWO);
            }
            this.height = height;
        }
    }

    /**
     * Returns sprite sheet description.
     * 
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the sprite sheet description. When <code>null</code> is specified
     * the description is set to empty.
     * 
     * @param description
     *        the description to set.
     */
    public void setDescription(final String description) {
        if ((null == getDescription()) || !getDescription().equals(description)) {
            if (null == description) {
                throw new IllegalArgumentException(Messages.NULL);
            } else if ((description.length() < MIN_DESCRIPTION_LENGTH)
                    || (description.length() > MAX_DESCRIPTION_LENGTH)) {
                throw new IllegalArgumentException(Messages.format(
                        Messages.SHEET_DESCRIPTION_LENGTH_INVALID,
                        MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH));
            } else {
                this.description = description;
            }
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
                            Messages.SHEET_MAX_WIDTH_INVALID_POWER_OF_TWO);
                } else if (!isPowerOfTwo(getMaxHeight())) {
                    throw new IllegalArgumentException(
                            Messages.SHEET_MAX_HEIGHT_INVALID_POWER_OF_TWO);
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
     * Returns <code>true</code> if specified value is power of two.
     * 
     * @param value
     *        the value to check.
     * @return <code>true</code> if value is power of two, otherwise
     *         <code>false</code>.
     */
    protected static boolean isPowerOfTwo(final int value) {
        return (value & -value) == value;
    }

}
