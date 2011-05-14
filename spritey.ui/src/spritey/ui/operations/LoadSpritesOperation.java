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
package spritey.ui.operations;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import spritey.core.Group;
import spritey.core.Node;
import spritey.core.Sprite;

/**
 * Operation for loading sprites.
 */
public class LoadSpritesOperation implements IRunnableWithProgress {

    private String[] paths;
    private Node root;

    public LoadSpritesOperation(String... paths) {
        this.paths = paths;
        root = new Node("root");
    }

    /**
     * Return root node.
     * 
     * @return root node.
     */
    public Node getRoot() {
        return root;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        monitor.beginTask("", IProgressMonitor.UNKNOWN);

        for (String path : paths) {
            if (null != path) {
                processFile(root, new File(path), monitor);
                if (monitor.isCanceled()) {
                    break;
                }
            }
        }

        monitor.done();
    }

    private void processFile(Node parent, File file, IProgressMonitor monitor) {
        monitor.subTask(file.getAbsolutePath());

        if (monitor.isCanceled()) {
            return;
        } else if (file.canRead()) {
            if (file.isDirectory() && (null != file.listFiles())) {
                Node node = new Group(file.getName());

                for (File child : file.listFiles()) {
                    processFile(node, child, monitor);
                    if (monitor.isCanceled()) {
                        return;
                    }
                }
                // Only add group when it contains at least one child.
                if (node.isBranch()) {
                    parent.addChildren(node);
                }
            } else {
                try {
                    Image image = ImageIO.read(file);

                    if (null != image) {
                        Node node = new Sprite(file.getName(), image);
                        parent.addChildren(node);
                    }
                } catch (IOException e) {
                    // Do nothing.
                } catch (IllegalArgumentException e) {
                    // Do nothing.
                }
            }
        }
    }

}
