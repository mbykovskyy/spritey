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
package spritey.core.adapter;

/**
 * An adapter factory converts the behaviour of a class into the interface
 * understandable by spritey core system.
 * <p>
 * Since spritey models are liberal towards types of objects stored under each
 * property, it needs this adapter factory to convert the model properties into
 * standard java types.
 * </p>
 */
public interface AdapterFactory {

    /**
     * Returns an adapter which is of type specified by <code>adapterType</code>
     * . It returns <code>null</code> if the specified adapter type or the
     * adaptable object is not handled by this factory.
     * 
     * @param adaptableObject
     *        the object to adapt to the specified adapter type.
     * @param adapterType
     *        the adapter type to convert the adaptable object.
     * @return an instance of an adapter or <code>null</code> when adapter was
     *         not found.
     */
    public Object getAdapter(Object adaptableObject, Class<?> adapterType);

}
