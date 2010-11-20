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

/**
 * Abstract implementation of Filter. Each method simply iterates through the
 * specified set and calls the <code>select</code> method on each element.
 * <p>
 * Subclasses must implement the <code>select</code> method.
 * </p>
 */
public abstract class AbstractFilter implements Filter {

    @Override
    public Model[] filter(Model root) {
        List<Model> out = new ArrayList<Model>();
        if (select(root)) {
            out.add(root);
        }

        for (Model child : root.getChildren()) {
            out.addAll(Arrays.asList(filter(child)));
        }

        return out.toArray(new Model[out.size()]);
    }

    @Override
    public Model[] filter(Model[] models) {
        List<Model> out = new ArrayList<Model>(models.length);
        for (Model e : models) {
            if (select(e)) {
                out.add(e);
            }
        }

        return out.toArray(new Model[out.size()]);
    }

    @Override
    public abstract boolean select(Model model);

}
