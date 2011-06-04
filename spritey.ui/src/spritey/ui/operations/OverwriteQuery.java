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
package spritey.ui.operations;

/**
 * Clients should implement this interface to answer with one overwrite code
 * when asked whether to overwrite a certain file.
 */
public interface OverwriteQuery {

    /**
     * Code indicating that file should be overwritten.
     */
    int YES = 0;

    /**
     * Code indicating that current file should be overwritten and subsequent
     * files should be overwritten without prompting.
     */
    int ALL = 1;

    /**
     * Code indicating that current file should not be overwritten.
     */
    int NO = 2;

    /**
     * Code indicating that current file should not be overwritten and
     * subsequent files should not be overwritten without prompting.
     */
    int NO_ALL = 3;

    /**
     * Code indicating that operation should be canceled.
     */
    int CANCEL = 4;

    /**
     * Returns one of the overwrite codes declared in this interface indicating
     * whether the file at the specified location should be overwritten.
     * 
     * @param path
     *        the path to file to be overwritten.
     * @return one of the overwrite code constants declared in this interface.
     */
    int queryOverwrite(String path);

}
