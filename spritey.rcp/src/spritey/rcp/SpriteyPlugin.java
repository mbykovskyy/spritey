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
package spritey.rcp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import spritey.core.ModelFactory;
import spritey.core.node.MapBasedNodeFactory;
import spritey.core.node.Node;
import spritey.core.node.NodeFactory;
import spritey.core.packer.FirstFitStrategy;
import spritey.core.packer.Packer;
import spritey.rcp.views.SelectionSynchronizer;
import spritey.rcp.views.ViewUpdateManager;

/**
 * The activator class controls the plug-in life cycle. It provides a series of
 * convenience methods such as access to the workspace.
 */
public class SpriteyPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "spritey.rcp";

    public static final String LOCK_IMG_ID = "lock";
    public static final String INVISIBLE_IMG_ID = "invisible";
    public static final String SPRITE_IMG_ID = "sprite";
    public static final String SHEET_IMG_ID = "sheet";
    public static final String GROUP_IMG_ID = "group";
    public static final String EDIT_IMG_ID = "edit";

    public static final String LOCK_IMG_PATH = "data/icons/lock.png";
    public static final String INVISIBLE_IMG_PATH = "data/icons/image_invisible.png";
    public static final String SPRITE_IMG_PATH = "data/icons/image_1.png";
    public static final String SHEET_IMG_PATH = "data/icons/application_view_icons.png";
    public static final String GROUP_IMG_PATH = "data/icons/images.png";
    public static final String EDIT_IMG_PATH = "data/icons/textfield_rename.png";

    // The shared instance
    private static SpriteyPlugin plugin;

    private Node rootNode;
    private ModelFactory modelFactory;
    private NodeFactory nodeFactory;
    private Packer packer;
    private ViewUpdateManager viewUpdater;
    private SelectionSynchronizer selectionSynchronizer;

    /**
     * The constructor
     */
    public SpriteyPlugin() {
        rootNode = null;
        modelFactory = new ModelFactory();
        nodeFactory = new MapBasedNodeFactory();
        packer = new Packer(new FirstFitStrategy());
        viewUpdater = new ViewUpdateManager();
        selectionSynchronizer = new SelectionSynchronizer();
        selectionSynchronizer.activate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static SpriteyPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *        the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * Returns sprite sheet's root node. Root node has no data attached. When
     * sprite sheet is created, root node becomes a parent of a newly created
     * sheet.
     * <p>
     * This is similar to GEF tree structure. Root-node contains Contents-node.
     * A client has to define/implement Contents-node as opposed to Root-node.
     * 
     * @return an instance of root node.
     */
    public Node getRootNode() {
        if (null == rootNode) {
            rootNode = getNodeFactory().createNode("Root");
        }

        return rootNode;
    }

    /**
     * Returns a default model factory.
     * 
     * @return an instance of model factory.
     */
    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    /**
     * Returns a default node factory.
     * 
     * @return an instance of a node factory.
     */
    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    /**
     * Returns a sprite sheet packer.
     * 
     * @return an instance of packer.
     */
    public Packer getPacker() {
        return packer;
    }

    /**
     * Returns a view updater.
     * 
     * @return view updater.
     */
    public ViewUpdateManager getViewUpdater() {
        return viewUpdater;
    }

    /**
     * Returns a selection synchronizer.
     * 
     * @return selection synchronizer.
     */
    public SelectionSynchronizer getSelectionSynchronizer() {
        return selectionSynchronizer;
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);

        reg.put(LOCK_IMG_ID, getImageDescriptor(LOCK_IMG_PATH));
        reg.put(INVISIBLE_IMG_ID, getImageDescriptor(INVISIBLE_IMG_PATH));
        reg.put(SPRITE_IMG_ID, getImageDescriptor(SPRITE_IMG_PATH));
        reg.put(SHEET_IMG_ID, getImageDescriptor(SHEET_IMG_PATH));
        reg.put(GROUP_IMG_ID, getImageDescriptor(GROUP_IMG_PATH));
        reg.put(EDIT_IMG_ID, getImageDescriptor(EDIT_IMG_PATH));
    }

}
