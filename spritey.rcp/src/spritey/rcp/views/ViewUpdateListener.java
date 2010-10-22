/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
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
package spritey.rcp.views;

/**
 * Views implementing this interface will be notified when they need need to
 * refresh themselves.
 */
public interface ViewUpdateListener {

    /**
     * Called when a view needs to be refreshed. This method is called when a
     * view and all it's children need to be updated.
     */
    public void refreshView();

    /**
     * Called when a view needs to be updated. This method is called when just a
     * single view needs to be updated.
     */
    public void updateView();

}
