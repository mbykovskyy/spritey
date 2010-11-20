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
package spritey.rcp.editors.editparts;

import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import spritey.core.Model;
import spritey.rcp.editors.tools.SpriteDragTracker;

/**
 * ContentsEditPart is a contents node of the viewer that contains sprite
 * sheet's root node. Note that this is not a sprite sheet node itself.
 */
public class ContentsEditPart extends AbstractGraphicalEditPart {

    @Override
    protected IFigure createFigure() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        IFigure contents = new Figure();
        contents.setLayoutManager(layout);

        return contents;
    }

    @Override
    protected void createEditPolicies() {
        // Do nothing.
    }

    @Override
    protected List<?> getModelChildren() {
        return Arrays.asList(((Model) getModel()).getChildren());
    }

    @Override
    public DragTracker getDragTracker(Request request) {
        return new SpriteDragTracker((SheetEditPart) getChildren().get(0));
    }

}
