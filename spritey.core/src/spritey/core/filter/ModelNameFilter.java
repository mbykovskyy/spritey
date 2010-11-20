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
package spritey.core.filter;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;

/**
 * Filter for extracting models that match the specified name.
 */
public class ModelNameFilter extends AbstractFilter {

    private final String name;

    /**
     * Creates a new instance of ModelNameFilter.
     * 
     * @param name
     *        the name to match.
     */
    public ModelNameFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean select(Model model) {
        String name = "";
        if (model instanceof Sprite) {
            name = (String) model.getProperty(Sprite.NAME);
        } else if (model instanceof Group) {
            name = (String) model.getProperty(Group.NAME);
        } else if (model instanceof Sheet) {
            name = Sheet.DEFAULT_NAME;
        }
        return name.equals(this.name);
    }
}
