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
package spritey.rcp.handlers;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.SpriteConstants;
import spritey.rcp.views.SelectionSynchronizer;
import spritey.rcp.views.navigator.SpriteTree;

/**
 * Handles the deletion of sprites.
 */
public class DeleteSpritesHandler extends AbstractHandler implements IHandler {

    private List<IStatus> errorMessages;

    public DeleteSpritesHandler() {
        errorMessages = new ArrayList<IStatus>();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IViewPart view = window.getActivePage().findView(SpriteTree.ID);
        Shell shell = HandlerUtil.getActiveShell(event);
        ISelection selection = view.getSite().getSelectionProvider()
                .getSelection();

        if ((selection != null) && (selection instanceof IStructuredSelection)) {
            SelectionSynchronizer sync = SpriteyPlugin.getDefault()
                    .getSelectionSynchronizer();

            sync.deactivate();
            delete((IStructuredSelection) selection, new ProgressMonitorDialog(
                    shell));
            sync.activate();

            if (!errorMessages.isEmpty()) {
                displayErrorMessages(shell);
            }
        }

        return null;
    }

    /**
     * Displays accumulated error messages that occurred while importing
     * sprites.
     * 
     * @param shell
     *        the parent shell.
     */
    private void displayErrorMessages(Shell shell) {
        IStatus status = null;

        if (errorMessages.size() > 1) {
            status = new MultiStatus("unknown", 0,
                    errorMessages.toArray(new IStatus[errorMessages.size()]),
                    Messages.SPRITE_DELETE_PROBLEMS, null);
        } else {
            status = errorMessages.get(0);
        }

        ErrorDialog.openError(shell, Messages.DELETE_SPRITES, null, status);
        errorMessages.clear();
    }

    /**
     * Deletes the specified selection.
     * 
     * @param selection
     *        the selection to delete.
     * @param dialog
     *        the monitor dialog to show the progress.
     */
    private void delete(final IStructuredSelection selection,
            final ProgressMonitorDialog dialog) {
        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            /**
             * Destroys allocated resources.
             * 
             * @param model
             *        the model to free.
             */
            private void destroy(final Model model) {
                // TODO Investigate the best place to dispose an Image.
                // Disposing an image here will sometimes cause an exception in
                // a middle of this operation, when OS sends a PAINT message to
                // Spritey shell causing GEF editor to update. GEF update
                // manager does not check if image figure has been disposed,
                // therefore, resulting in an exception. We can dispose an image
                // inside a SpriteEditPart when removeNotify() is received.
                // removeNotify() is called in two places, one when
                // removeChild() is invoked on EditPart and two, when refresh()
                // method is called. However, moving this to removeNotify() will
                // only dispose visible images. Invisible images i.e. with
                // negative x and y will not be disposed as they don't have an
                // EditPart. For now remove only invisible sprites here and
                // remove visible sprites in SpriteEditPart.
                if (model instanceof Sprite) {
                    Image image = (Image) model
                            .getProperty(SpriteConstants.SWT_IMAGE);
                    Rectangle bounds = (Rectangle) model
                            .getProperty(Sprite.BOUNDS);

                    if ((null != image) && !image.isDisposed()
                            && (bounds.x < 0) && (bounds.y < 0)) {
                        image.dispose();
                    }
                }
            }

            /**
             * Deletes the specified model and all its children.
             * 
             * @param model
             *        the model to delete.
             * @param monitor
             *        the monitor to monitor progress.
             */
            private void delete(final Model model,
                    final IProgressMonitor monitor) {
                // Detach itself from the parent before destroying the model and
                // children.
                model.getParent().removeChild(model);
                destroy(model);

                // If node is a group it needs to free its children.
                for (Model child : model.getChildren()) {
                    // TODO Should we disallow cancelling. Cancelling this
                    // operation will not bring back deleted nodes. This may
                    // mislead the user to think that if the operation is
                    // cancelled then all sprites will remain untouched.
                    if (!monitor.isCanceled()) {
                        delete(child, monitor);
                    }
                }
            }

            @Override
            public void run(final IProgressMonitor monitor)
                    throws InvocationTargetException, InterruptedException {
                monitor.beginTask(Messages.DELETE_SPRITES,
                        IProgressMonitor.UNKNOWN);

                // Structured selection has the same structure as a tree i.e.
                // if the height node is selected last it will still be first
                // in the selection list. Therefore, loop in reverse to delete
                // selected nodes before non-selected children of the selected
                // group.
                Object[] list = selection.toArray();
                for (int i = list.length - 1; i >= 0; --i) {
                    if (list[i] instanceof Sheet) {
                        // TODO Do we need to display this message? It
                        // should be obvious to the user that sheet is not
                        // allowed to be deleted. A better solution would
                        // be to disable delete icon altogether.
                        String message = NLS.bind(
                                Messages.DELETING_SHEET_DISALLOWED,
                                Sheet.DEFAULT_NAME);
                        errorMessages.add(new Status(IStatus.INFO, "unknown",
                                message));
                    } else {
                        // TODO Should we disallow cancelling. Cancelling
                        // this operation will not bring back deleted nodes.
                        // This may mislead the user to think that if the
                        // operation is cancelled then all sprites will
                        // remain untouched. A solution could be to display
                        // a warning message to warn the user that some
                        // sprites were still deleted and provide a list of
                        // deleted nodes.
                        if (!monitor.isCanceled()) {
                            delete((Model) list[i], monitor);
                        }
                    }
                }

                monitor.beginTask(Messages.PACKING_SPRITES,
                        IProgressMonitor.UNKNOWN);

                final SpriteyPlugin plugin = SpriteyPlugin.getDefault();
                Sheet sheet = (Sheet) plugin.getRootModel().getChildren()[0];
                plugin.getPacker().pack(sheet, true);

                monitor.beginTask(Messages.UPDATING_VIEWS,
                        IProgressMonitor.UNKNOWN);

                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        plugin.getViewUpdater().refreshViews();
                    }
                });

                monitor.done();
            }
        };

        try {
            dialog.run(true, true, runnable);
        } catch (InvocationTargetException e) {
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Log it since we don't expect this exception.
            e.printStackTrace();
        }
    }

}
