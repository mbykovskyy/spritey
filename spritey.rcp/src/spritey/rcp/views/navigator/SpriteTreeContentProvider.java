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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

import spritey.core.node.Node;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.views.ViewUpdateListener;

/**
 * Content provider for updating sprite tree view.
 */
public class SpriteTreeContentProvider implements ITreeContentProvider,
        ViewUpdateListener {

    private StructuredViewer viewer;

    /**
     * Default constructor
     */
    public SpriteTreeContentProvider() {
        SpriteyPlugin.getDefault().getViewUpdater().addListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
     * Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Node) {
            return ((Node) parentElement).getChildren();
        }
        return new Object[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
     * )
     */
    @Override
    public Object getParent(Object element) {
        if (element instanceof Node) {
            return ((Node) element).getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
     * Object)
     */
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof Node) {
            return ((Node) element).isBranch();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
     * .lang.Object)
     */
    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = (StructuredViewer) viewer;
    }

    @Override
    public void dispose() {
        SpriteyPlugin.getDefault().getViewUpdater().removeListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.rcp.views.ViewUpdateListener#refreshView()
     */
    @Override
    public void refreshView() {
        viewer.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.rcp.views.ViewUpdateListener#updateView()
     */
    @Override
    public void updateView() {
        // TODO change this to update().
        viewer.refresh();
    }

}
