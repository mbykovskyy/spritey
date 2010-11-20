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
package spritey.core.event;

import spritey.core.Node;

/**
 * A convenience class for implementing node listeners. Methods in this class
 * are empty.
 */
public abstract class AbstractNodeChangeListener<T extends Node<T>> implements
        NodeChangeListener<T> {

    @Override
    public void parentChanged(T oldParent, T newParent) {
        // Do nothing.
    }

    @Override
    public void childAdded(T child) {
        // Do nothing.
    }

    @Override
    public void childRemoved(T child) {
        // Do nothing.
    }

}
