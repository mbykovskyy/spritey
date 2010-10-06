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
package spritey.rcp.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends an update notification to the registered listeners. Operations like
 * adding or removing sprites should not update views in a middle of an
 * operation as it will slow it down. Therefore, such operations will tell views
 * to update via this updater. Views interested in updating themselves after
 * long operations should register with this updater.
 */
public class ViewUpdateManager {

    private List<ViewUpdateListener> listeners;

    public ViewUpdateManager() {
        listeners = new ArrayList<ViewUpdateListener>();
    }

    /**
     * Registers the specified listener. When a listener is already in the no
     * action is taken.
     * 
     * @param listener
     *        the listener to add to a list.
     */
    public void addListener(final ViewUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes the specified listener from the list.
     * 
     * @param listener
     *        the listener to remove from the list.
     */
    public void removeListener(final ViewUpdateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sends an update notification to registered listeners.
     */
    public void updateViews() {
        for (ViewUpdateListener l : listeners) {
            l.updateView();
        }
    }

    /**
     * Sends a refresh notification to registered listeners.
     */
    public void refreshViews() {
        for (ViewUpdateListener l : listeners) {
            l.refreshView();
        }
    }

}
