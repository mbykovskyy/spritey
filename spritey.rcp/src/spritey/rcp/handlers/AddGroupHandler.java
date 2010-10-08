package spritey.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.rcp.SpriteyPlugin;
import spritey.rcp.dialogs.StaticWizardDialog;
import spritey.rcp.wizards.NewGroupWizard;

/**
 * Handles the creation of a new group.
 */
public class AddGroupHandler extends AbstractHandler implements IHandler {

    private static final int WIDTH = 390;
    private static final int HEIGHT = 240;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);

        Window wizard = new StaticWizardDialog(shell, WIDTH, HEIGHT,
                new NewGroupWizard());

        if (wizard.open() != Window.CANCEL) {
            SpriteyPlugin.getDefault().getViewUpdater().refreshViews();
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return !SpriteyPlugin.getDefault().getRootNode().isLeaf();
    }

}
