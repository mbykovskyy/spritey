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

    protected static final String DEFAULT_NAME = Messages.SHEET_DEFAULT_NAME;
    protected static final Color DEFAULT_BACKGROUND = new Color(255, 0, 255);
    protected static final int DEFAULT_WIDTH = 0;
    protected static final int DEFAULT_HEIGHT = 0;
    protected static final String DEFAULT_DESCRIPTION = Messages.SHEET_DEFAULT_DESCRIPTION;

    public static final Color TRANSPARENT_BACKGROUND = new Color(0, 0, 0, 0);

    private Color background;
    private int width;
    private int height;
    private String description;

    /**
     * Creates a new instance of <code>Sheet</code> with default settings.
     */
    public Sheet() {
        this(DEFAULT_BACKGROUND, DEFAULT_DESCRIPTION);
    }

    /**
     * Creates a new instance of <code>Sheet</code>.
     * 
     * @param background
     *        the background color.
     * @param description
     *        the short description or comment.
     */
    public Sheet(final Color background, final String description) {
        super(DEFAULT_NAME);
        setBackground(background);
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
     *         when given width is negative.
     */
    public void setWidth(final int width) {
        if (getWidth() != width) {
            if (width < 0) {
                throw new IllegalArgumentException(Messages.SHEET_WIDTH_INVALID);
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
            if (height < 0) {
                throw new IllegalArgumentException(
                        Messages.SHEET_HEIGHT_INVALID);
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

}
