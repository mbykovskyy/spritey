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
package spritey.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Composite;

import spritey.rcp.SpriteyPlugin;
import spritey.rcp.editors.editparts.ContentsEditPart;
import spritey.rcp.editors.editparts.GraphicalEditPartFactory;

/**
 * A graphical editor for displaying and editing sprite sheet.
 */
public class SheetEditor extends GraphicalEditor {

    public static final String ID = "spritey.rcp.editors.sheetEditor";

    /**
     * Default constructor
     */
    public SheetEditor() {
        setEditDomain(new DefaultEditDomain(this));
    }

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setEditPartFactory(new GraphicalEditPartFactory());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
     */
    @Override
    protected void initializeGraphicalViewer() {
        EditPart part = new ContentsEditPart();
        part.setModel(SpriteyPlugin.getDefault().getRootNode());
        getGraphicalViewer().setContents(part);
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        SpriteyPlugin.getDefault().getSelectionSynchronizer()
                .addSelectionProvider(getGraphicalViewer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        // Do nothing.
    }

}
