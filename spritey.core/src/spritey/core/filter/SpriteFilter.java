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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spritey.core.Model;
import spritey.core.Sprite;

/**
 * Filter for extracting sprites.
 */
public class SpriteFilter extends AbstractFilter {

    @Override
    public Sprite[] filter(Model root) {
        List<Sprite> out = new ArrayList<Sprite>();
        if (select(root)) {
            out.add((Sprite) root);
        }

        for (Model child : root.getChildren()) {
            out.addAll(Arrays.asList(filter(child)));
        }
        return out.toArray(new Sprite[out.size()]);
    }

    @Override
    public Sprite[] filter(Model[] models) {
        List<Sprite> out = new ArrayList<Sprite>(models.length);
        for (Model e : models) {
            if (select(e)) {
                out.add((Sprite) e);
            }
        }
        return out.toArray(new Sprite[out.size()]);
    }

    @Override
    public boolean select(Model model) {
        return (model instanceof Sprite);
    }

}
