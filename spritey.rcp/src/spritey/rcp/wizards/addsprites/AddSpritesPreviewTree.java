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
package spritey.rcp.wizards.addsprites;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sprite;
import spritey.core.exception.InvalidPropertyValueException;
import spritey.rcp.core.GroupConstants;
import spritey.rcp.core.SpriteConstants;

/**
 * A preview tree of loaded files where a user can select which sprites to
 * import.
 */
public class AddSpritesPreviewTree extends EventManager implements
        SelectionListener {

    private CheckboxTreeViewer tree;

    /**
     * Creates a new instance of AddSpritesPreviewTree.
     * 
     * @param parent
     *        the parent composite.
     */
    public AddSpritesPreviewTree(Composite parent) {
        TreeColumnLayout layout = new TreeColumnLayout();
        parent.setLayout(layout);

        tree = new CheckboxTreeViewer(parent, SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL);
        tree.getTree().addSelectionListener(this);
    }

    /**
     * Sets the content provider.
     * 
     * @param provider
     *        the content provider to set.
     */
    public void setContentProvider(IContentProvider provider) {
        tree.setContentProvider(provider);
    }

    /**
     * Sets the label provider.
     * 
     * @param provider
     *        the label provider to set.
     */
    public void setLabelProvider(IBaseLabelProvider provider) {
        tree.setLabelProvider(provider);
    }

    /**
     * Sets the tree input.
     * 
     * @param root
     *        the root of the tree to display in the tree viewer.
     */
    public void setInput(Model root) {
        tree.setInput(root);
    }

    /**
     * Returns the root node of the preview tree.
     * 
     * @return the root node.
     */
    public Model getInput() {
        return (Model) tree.getInput();
    }

    @Override
    public void widgetSelected(final SelectionEvent e) {
        if (e.detail == SWT.CHECK) {
            TreeItem item = (TreeItem) e.item;
            select(item, item.getChecked());
            updateParentSelection(item.getParentItem());

            fireCheckStateChnaged(new CheckStateChangedEvent(tree,
                    item.getData(), item.getChecked()));
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }

    /**
     * Adds the specified listener that will be notified when check state
     * changes.
     * 
     * @param listener
     *        the listener to add.
     */
    public void addCheckStateListener(ICheckStateListener listener) {
        addListenerObject(listener);
    }

    /**
     * Removes the specified listener.
     * 
     * @param listener
     *        the listener to remove.
     */
    public void removeCheckStateListener(ICheckStateListener listener) {
        removeListenerObject(listener);
    }

    /**
     * Notify all check state listeners that the check state of an element has
     * changed.
     */
    protected void fireCheckStateChnaged(final CheckStateChangedEvent event) {
        for (Object listener : getListeners()) {
            final ICheckStateListener l = (ICheckStateListener) listener;
            SafeRunner.run(new SafeRunnable() {
                public void run() {
                    l.checkStateChanged(event);
                }
            });
        }
    }

    /**
     * Updates parent's check and grayed state. This method recursively updates
     * all ancestors starting with the specified parent.
     * 
     * @param parent
     *        the parent to update.
     */
    protected void updateParentSelection(TreeItem parent) {
        if (null != parent) {
            boolean doGray = false;
            boolean doCheck = true;
            boolean doUncheck = true;

            TreeItem[] children = parent.getItems();
            for (TreeItem child : children) {
                if (child.getChecked() && doUncheck) {
                    doUncheck = false;
                } else if (!child.getChecked() && doCheck) {
                    doCheck = false;
                }

                if (child.getGrayed() || (!doUncheck && !doCheck)) {
                    if (doCheck) {
                        doCheck = false;
                    }
                    if (doUncheck) {
                        doUncheck = false;
                    }
                    doGray = true;
                    break;
                }
            }

            Model model = (Model) parent.getData();
            if (doGray) {
                parent.setGrayed(true);
                parent.setChecked(true);
                setModelSelected(model, true);
            } else if (doCheck) {
                parent.setGrayed(false);
                parent.setChecked(true);
                setModelSelected(model, true);
            } else if (doUncheck) {
                parent.setGrayed(false);
                parent.setChecked(false);
                setModelSelected(model, false);
            }

            updateParentSelection(parent.getParentItem());
        }
    }

    /**
     * Selects/Deselects all valid items.
     * 
     * @param select
     *        if <code>true</code> all valid items are selected, otherwise
     *        deselected.
     */
    public void selectAll(final boolean select) {
        BusyIndicator.showWhile(tree.getControl().getDisplay(), new Runnable() {
            public void run() {
                tree.getTree().setRedraw(false);
                tree.expandAll();

                TreeItem[] children = tree.getTree().getItems();
                for (TreeItem child : children) {
                    select(child, select);
                }
                tree.getTree().setRedraw(true);
            }
        });
    }

    /**
     * Recursively checks or unchecks only valid items i.e. items that have no
     * errors.
     * 
     * @param item
     *        item to check/uncheck.
     */
    protected void select(TreeItem item, boolean selected) {
        Model data = (Model) item.getData();
        if (null != data) {
            if (hasError(data) && selected) {
                item.setChecked(false);
                setModelSelected(data, false);
                setGrayed(item.getParentItem());
            } else {
                item.setGrayed(false);
                item.setChecked(selected);
                setModelSelected(data, selected);

                TreeItem[] children = item.getItems();
                for (TreeItem child : children) {
                    select(child, selected);
                }
            }
        }
    }

    /**
     * Sets the specified model's selection state.
     * 
     * @param model
     *        the model to update.
     * @param selected
     *        the value to set state to.
     */
    protected void setModelSelected(Model model, boolean selected) {
        boolean isGroup = model instanceof Group;
        int property = isGroup ? GroupConstants.CHECKED
                : SpriteConstants.CHECKED;
        Object isSelected = model.getProperty(property);

        if ((null == isSelected) || ((Boolean) isSelected != selected)) {
            try {
                model.setProperty(property, selected);
            } catch (InvalidPropertyValueException e) {
                // Do nothing.
            }
        }
    }

    /**
     * Indicates whether the specified model has an error to display.
     * 
     * @param model
     *        the model to test.
     * @return <code>true</code> if the model has an error, otherwise
     *         <code>false</code>.
     */
    protected boolean hasError(Model model) {
        boolean spriteHasError = ((model instanceof Sprite) && (model
                .getProperty(SpriteConstants.DISPLAY_ERROR) != null));
        boolean groupHasError = ((model instanceof Group) && (model
                .getProperty(GroupConstants.DISPLAY_ERROR) != null));
        return spriteHasError || groupHasError;
    }

    /**
     * Recursively grays parent items starting with the specified item.
     * 
     * @param item
     *        the item to gray.
     */
    protected void setGrayed(TreeItem item) {
        if ((null != item) && !item.getGrayed()) {
            item.setGrayed(true);
            setGrayed(item.getParentItem());
        }
    }

}
