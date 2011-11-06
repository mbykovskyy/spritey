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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Sprite is an image that can be drawn on a canvas.
 */
public class Sprite extends Node {

    public static final String DEFAULT_NAME = Messages.SPRITE_DEFAULT_NAME;
    public static final Point DEFAULT_LOCATION = new Point(-1, -1);
    public static final Dimension DEFAULT_SIZE = new Dimension(0, 0);

    private Point location;
    private Image image;

    /**
     * Creates a new instance of Sprite with DEFAULT_NAME.
     * 
     * @param image
     *        the image this sprite will represent.
     */
    public Sprite(final Image image) {
        this(DEFAULT_NAME, image);
    }

    /**
     * Creates a new instance of Sprite with the give name.
     * 
     * @param name
     *        the name to give to the sprite.
     * @param image
     *        the image this sprite will represent.
     */
    public Sprite(final String name, final Image image) {
        super(name);
        setImage(image);
        setLocation(DEFAULT_LOCATION);
    }

    /**
     * Returns coordinates where sprite is located.
     * 
     * @return sprite location.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets sprite location.
     * 
     * @param location
     *        the location where sprite is position within a sprite sheet.
     */
    public void setLocation(final Point location) {
        if ((null == getLocation()) || !getLocation().equals(location)) {
            if (null == location) {
                throw new IllegalArgumentException(Messages.NULL);
            }
            this.location = location;
        }
    }

    /**
     * Returns the image this sprite represents.
     * 
     * @return the sprite image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets sprite image.
     * 
     * @param image
     *        the image to assign to this sprite.
     * @throws IllegalArgumentException
     *         when <code>image</code> or image size is <code>null</code>.
     */
    protected void setImage(final Image image) {
        if ((null == getImage()) || (getImage() != image)) {
            if (null == image) {
                throw new IllegalArgumentException(Messages.NULL);
            }
            this.image = image;
        }
    }

    /**
     * Returns the sprite size.
     * 
     * @return the size.
     */
    public Dimension getSize() {
        Image image = getImage();
        if (null == image) {
            return DEFAULT_SIZE;
        }

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        if ((-1 == width) || (-1 == height)) {
            throw new RuntimeException(
                    Messages.SPRITE_IMAGE_NOT_FINISHED_LOADING);
        }
        return new Dimension(width, height);
    }

    /**
     * Returns the area of the sprite.
     * 
     * @return the sprite area.
     */
    public int getArea() {
        Dimension size = getSize();
        return size.width * size.height;
    }

    /**
     * Returns whether sprite is visible. i.e. fits the sprite sheet.
     * 
     * @return <code>true</code> if sprite fits the sprite sheet, otherwise
     *         <code>false</code>.
     */
    public boolean isVisible() {
        return (getLocation().x > -1) && (getLocation().y > -1);
    }

    /**
     * Returns sprite's bounds.
     * 
     * @return sprite's bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(getLocation(), getSize());
    }

}
