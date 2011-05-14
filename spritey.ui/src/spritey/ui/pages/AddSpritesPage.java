/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2011 Maksym Bykovskyy.
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
package spritey.ui.pages;

import java.awt.Dimension;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import spritey.core.Node;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.filter.SpriteFilter;
import spritey.ui.Application;
import spritey.ui.Messages;
import spritey.ui.actions.AddFolderAction;
import spritey.ui.actions.AddSpritesAction;
import spritey.ui.actions.CollapseAllAction;
import spritey.ui.actions.CreateGroupAction;
import spritey.ui.actions.DeleteGroupsAction;
import spritey.ui.actions.DeleteSpritesAction;
import spritey.ui.actions.ExpandAllAction;
import spritey.ui.actions.SelectAllTableAction;
import spritey.ui.actions.SelectAllTreeAction;
import spritey.ui.providers.GroupTreeContentProvider;
import spritey.ui.providers.GroupTreeLabelProvider;
import spritey.ui.providers.SpriteTableContentProvider;

/**
 * The page for importing sprites from the file system into sprite sheet.
 */
public class AddSpritesPage extends WizardPageEx {

    static final String NAME = "ADD_SPRITES";

    private TreeViewer groups;
    private TableViewer sprites;
    private Sheet sheet;

    public AddSpritesPage(NewSheetPage newSheetPage) {
        super(NAME);
        setTitle(Messages.ADD_SPRITES_PAGE_TITLE);
        setDescription(Messages.ADD_SPRITES_PAGE_DESCRIPTION);
        setPageComplete(false);

        sheet = newSheetPage.getSheet();
    }

    @Override
    public void validatePage() {
        setPageComplete(0 < new SpriteFilter().filter(sheet).length);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        groups.getControl().setFocus();
    }

    @Override
    public void createControl(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.SMOOTH);
        createLeftControls(sash);
        createRightControls(sash);

        sash.setWeights(new int[] { 1, 2 });
        setControl(sash);
    }

    /**
     * Creates controls on the left-hand side of the sash form.
     * 
     * @param parent
     *        the parent.
     */
    private void createLeftControls(Composite parent) {
        ViewForm form = new ViewForm(parent, SWT.BORDER | SWT.FLAT);

        groups = new TreeViewer(form, SWT.MULTI);
        groups.setContentProvider(new GroupTreeContentProvider());
        groups.setLabelProvider(new GroupTreeLabelProvider());
        groups.setInput(getInitalInput());
        groups.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object[] list = ((IStructuredSelection) event.getSelection())
                        .toArray();

                if (null != sprites) {
                    if (1 == list.length) {
                        sprites.setInput(getSelectedNode());
                    } else {
                        sprites.setInput(null);
                    }
                }
            }
        });

        IAction addSprites = new AddSpritesAction(groups, this);
        IAction addFolder = new AddFolderAction(groups, this);
        IAction createGroup = new CreateGroupAction(groups);
        IAction deleteGroups = new DeleteGroupsAction(groups, this);

        ToolBarManager toolbarManager = new ToolBarManager(SWT.FLAT);
        toolbarManager.add(addSprites);
        toolbarManager.add(addFolder);
        toolbarManager.add(createGroup);
        toolbarManager.add(deleteGroups);
        toolbarManager.add(new Separator());
        toolbarManager.add(new ExpandAllAction(groups));
        toolbarManager.add(new CollapseAllAction(groups));

        MenuManager menuManager = new MenuManager();
        menuManager.add(addSprites);
        menuManager.add(addFolder);
        menuManager.add(createGroup);
        menuManager.add(new Separator());
        menuManager.add(deleteGroups);
        menuManager.add(new SelectAllTreeAction(groups));

        Tree tree = groups.getTree();
        tree.setMenu(menuManager.createContextMenu(tree));

        form.setTopLeft(toolbarManager.createControl(form));
        form.setContent(tree);

        // Set selection after toolbar and context menu have been created to set
        // their initial states.
        groups.setSelection(new StructuredSelection(sheet));
    }

    /**
     * Creates controls on the right-hand side of the sash form.
     * 
     * @param parent
     *        the parent.
     */
    private void createRightControls(Composite parent) {
        ViewForm form = new ViewForm(parent, SWT.BORDER | SWT.FLAT);

        sprites = new TableViewer(form, SWT.MULTI | SWT.FULL_SELECTION
                | SWT.V_SCROLL | SWT.H_SCROLL);
        sprites.getTable().setHeaderVisible(true);

        TableViewerColumn column = new TableViewerColumn(sprites, SWT.NONE);
        column.getColumn().setText(Messages.ADD_SPRITES_NAME_COLUMN);
        column.getColumn().setWidth(150);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public Image getImage(Object element) {
                if (element instanceof Node) {
                    return Application.getImageRegistry().get(
                            Application.SPRITE_IMG_ID);
                }
                return super.getImage(element);
            }

            @Override
            public String getText(Object element) {
                if (element instanceof Node) {
                    return ((Node) element).getName();
                }
                return super.getText(element);
            }
        });

        column = new TableViewerColumn(sprites, SWT.NONE);
        column.getColumn().setText(Messages.ADD_SPRITES_SIZE_COLUMN);
        column.getColumn().setWidth(100);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Sprite) {
                    Dimension size = ((Sprite) element).getSize();
                    return size.width + "x" + size.height;
                }
                return super.getText(element);
            }
        });

        sprites.setContentProvider(new SpriteTableContentProvider());
        sprites.setInput(getSelectedNode());

        MenuManager menuManager = new MenuManager();
        menuManager.add(new DeleteSpritesAction(sprites, this));
        menuManager.add(new SelectAllTableAction(sprites));

        Table table = sprites.getTable();
        table.setMenu(menuManager.createContextMenu(table));

        form.setContent(table);
    }

    /**
     * Returns initial input for group tree.
     * 
     * @return
     */
    private Object getInitalInput() {
        Node root = new Node("root");
        root.addChildren(sheet);
        return root;
    }

    /**
     * Returns the node selected in the group tree.
     * 
     * @return the selected node.
     * @throws RuntimeException
     *         when multiple nodes are selected.
     */
    private Node getSelectedNode() {
        Object[] list = ((IStructuredSelection) groups.getSelection())
                .toArray();

        if (1 != list.length) {
            throw new RuntimeException(
                    "Expected group tree to have a single selection.");
        }
        return (Node) list[0];
    }

}
