package spritey.rcp.views.navigator;

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

}
