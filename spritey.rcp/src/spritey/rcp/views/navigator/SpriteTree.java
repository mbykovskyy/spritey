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
package spritey.rcp.views.navigator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.handlers.CollapseAllHandler;
import org.eclipse.ui.handlers.ExpandAllHandler;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.navigator.CommonNavigator;

import spritey.rcp.SpriteyPlugin;

/**
 * A hierarchical view of sprite sheet.
 */
public class SpriteTree extends CommonNavigator {

    public static final String ID = "spritey.rcp.views.spriteTree";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#getInitialInput()
     */
    @Override
    protected Object getInitialInput() {
        return SpriteyPlugin.getDefault().getRootNode();
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        SpriteyPlugin.getDefault().getSelectionSynchronizer()
                .addSelectionProvider(getCommonViewer());

        IHandlerService handlerService = (IHandlerService) getSite()
                .getService(IHandlerService.class);
        handlerService.activateHandler(CollapseAllHandler.COMMAND_ID,
                new CollapseAllHandler(getCommonViewer()));
        handlerService.activateHandler(ExpandAllHandler.COMMAND_ID,
                new ExpandAllHandler(getCommonViewer()));
    }

    @Override
    protected ActionGroup createCommonActionGroup() {
        return new ActionGroup() {
            // Do nothing.
        };
    }

}
