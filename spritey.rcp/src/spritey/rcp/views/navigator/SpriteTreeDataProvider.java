/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.event.ModelEvent;
import spritey.core.event.ModelListener;
import spritey.core.node.Node;
import spritey.core.node.event.NodeListener;
import spritey.rcp.SpriteyPlugin;

/**
 * Data provider for updating sprite tree view.
 */
public class SpriteTreeDataProvider extends BaseLabelProvider implements
        ITreeContentProvider, ILabelProvider, ModelListener, NodeListener {

    private Object input;
    private Viewer viewer;

    private final Image ERROR_IMG;
    private final Image SPRITE_IMG;
    private final Image SHEET_IMG;
    private final Image GROUP_IMG;

    private static final String ERROR_ICON_PATH = "data/icons/image_invisible.png";
    private static final String SPRITE_ICON_PATH = "data/icons/image_1.png";
    private static final String SHEET_ICON_PATH = "data/icons/application_view_icons.png";
    private static final String GROUP_ICON_PATH = "data/icons/images.png";

    private static final String INVISIBLE = "invisible";

    public SpriteTreeDataProvider() {
        // TODO This makes SpriteTreeDataProvider test to fail because RCP is
        // not initialized when running JUnit tests.
        ERROR_IMG = SpriteyPlugin.getImageDescriptor(ERROR_ICON_PATH)
                .createImage();
        SPRITE_IMG = SpriteyPlugin.getImageDescriptor(SPRITE_ICON_PATH)
                .createImage();
        SHEET_IMG = SpriteyPlugin.getImageDescriptor(SHEET_ICON_PATH)
                .createImage();
        GROUP_IMG = SpriteyPlugin.getImageDescriptor(GROUP_ICON_PATH)
                .createImage();
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
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
        // Do nothing.
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
        this.viewer = viewer;
        input = newInput;

        if (input instanceof Node) {
            ((Node) input).addNodeListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        Model model = ((Node) element).getModel();

        if (model instanceof Sprite) {
            Sprite sprite = (Sprite) model;
            Rectangle bounds = (Rectangle) sprite.getProperty(Sprite.BOUNDS);

            if ((bounds.x < 0) || (bounds.y < 0)) {
                return ERROR_IMG;
            }
            return SPRITE_IMG;
        } else if (model instanceof Sheet) {
            return SHEET_IMG;
        } else if (model instanceof Group) {
            return GROUP_IMG;
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if ((null != element) && (element instanceof Node)) {
            Model data = ((Node) element).getModel();

            if (null != data) {
                if (data instanceof Sheet) {
                    // Sheet does not have a name, therefore, use node's name.
                    return ((Node) element).getName();
                } else if (data instanceof Group) {
                    return data.getProperty(Group.NAME).toString();
                } else if (data instanceof Sprite) {
                    Rectangle bounds = (Rectangle) data
                            .getProperty(Sprite.BOUNDS);

                    String name = data.getProperty(Sprite.NAME).toString();
                    if ((bounds.x < 0) || (bounds.y < 0)) {
                        name = "[" + INVISIBLE + "] " + name;
                    }

                    return name;
                }
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.event.PropertyListener#propertyChanged(spritey.core.event
     * .PropertyEvent)
     */
    @Override
    public void propertyChanged(ModelEvent event) {
        viewer.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.event.NodeListener#nameChanged(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void nameChanged(String oldName, String newName) {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#parentChanged(spritey.core.node.
     * Node, spritey.core.node.Node)
     */
    @Override
    public void parentChanged(Node oldParent, Node newParent) {
        viewer.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#childAdded(spritey.core.node.Node)
     */
    @Override
    public void childAdded(Node child) {
        listen(child);
        viewer.refresh();
        viewer.setSelection(new StructuredSelection(child), true);
    }

    /**
     * Registers itself with a node and node's children to be notified about
     * event.
     * 
     * @param node
     *        the node to start listening to.
     */
    private void listen(Node node) {
        node.addNodeListener(this);

        if (null != node.getModel()) {
            node.getModel().addModelListener(this);
        }

        // If the node is a branch the provider has to add itself to all of
        // branch's children and children's children. Otherwise, only the top
        // node will notify about changes.
        for (Node child : node.getChildren()) {
            listen(child);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * spritey.core.node.event.NodeListener#childRemoved(spritey.core.node.Node)
     */
    @Override
    public void childRemoved(Node child) {
        ignore(child);
        viewer.refresh();
    }

    /**
     * Unregisters itself from a node and node's children to stop recieving
     * events.
     * 
     * @param node
     *        the node to stop listening to.
     */
    private void ignore(Node node) {
        node.removeNodeListener(this);

        if (null != node.getModel()) {
            node.getModel().removeModelListener(this);
        }

        // If the node is a branch the provider has to ignore branch's
        // children as well. Otherwise, this provider may be notified about
        // events from children that are no longer part of the tree.
        for (Node child : node.getChildren()) {
            ignore(child);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see spritey.core.node.event.NodeListener#properitesChanged(spritey.core.
     * Model, spritey.core.Model)
     */
    @Override
    public void modelChanged(Model oldValue, Model newValue) {
        viewer.refresh();
    }

}
