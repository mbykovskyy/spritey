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
