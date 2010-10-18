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
package spritey.rcp.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * A service for synchronizing selection across multiple views.
 */
public class SelectionSynchronizer implements ISelectionChangedListener {

    private List<ISelectionProvider> providers;

    private boolean isActive;

    /**
     * Creates an instance of a new selection synchronizer.
     */
    public SelectionSynchronizer() {
        providers = new ArrayList<ISelectionProvider>();
        isActive = false;
    }

    /**
     * Makes the synchronizer active.
     */
    public void activate() {
        if (!isActive) {
            for (ISelectionProvider p : providers) {
                p.addSelectionChangedListener(this);
            }
            isActive = true;
        }
    }

    /**
     * Makes the synchronizer disabled.
     */
    public void deactivate() {
        if (isActive) {
            for (ISelectionProvider p : providers) {
                p.removeSelectionChangedListener(this);
            }
            isActive = false;
        }
    }

    /**
     * Adds the specified selection provider to a list of other providers.
     * 
     * @param provider
     *        selection provider that needs to sync its selection with other
     *        viewers.
     */
    public void addSelectionProvider(ISelectionProvider provider) {
        if (isActive) {
            provider.addSelectionChangedListener(this);
        }
        providers.add(provider);
    }

    /**
     * Converts the specified selected object from one viewer to an object
     * understandable by the specified provider.
     * 
     * @param provider
     *        the viewer being mapped to.
     * @param selectee
     *        the object to map.
     * @return <code>null</code> or a corresponding object.
     */
    protected Object convert(ISelectionProvider provider, Object selectee) {
        Object converted = null;

        if (provider instanceof EditPartViewer) {
            // Mapping selectee to an edit part.
            EditPartViewer viewer = (EditPartViewer) provider;

            if (selectee instanceof EditPart) {
                EditPart part = (EditPart) selectee;
                converted = viewer.getEditPartRegistry().get(part.getModel());
            } else {
                converted = viewer.getEditPartRegistry().get(selectee);
            }
        } else if (provider instanceof Viewer) {
            // Mapping selectee to some arbitrary object, usually a model.
            if (selectee instanceof EditPart) {
                converted = ((EditPart) selectee).getModel();
            } else {
                converted = selectee;
            }
        }
        return converted;
    }

    /**
     * Sets the specified selection in the specified target.
     * 
     * @param target
     *        the viewer to apply selection to.
     * @param selection
     *        the selection to apply.
     */
    protected void setSelection(ISelectionProvider target,
            IStructuredSelection selection) {
        Object[] selectees = selection.toArray();
        List<Object> newSelectees = new ArrayList<Object>(selectees.length);

        for (Object s : selectees) {
            Object converted = convert(target, s);
            if (null != converted) {
                newSelectees.add(converted);
            }
        }

        ISelection newSelection = new StructuredSelection(newSelectees);

        if (target instanceof Viewer) {
            ((Viewer) target).setSelection(newSelection, false);
        } else if (target instanceof EditPartViewer) {
            EditPartViewer viewer = (EditPartViewer) target;
            viewer.setSelection(newSelection);

            if (newSelectees.size() > 0) {
                viewer.reveal((EditPart) newSelectees.get(0));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
     * org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if (isActive) {
            deactivate();
            ISelectionProvider source = event.getSelectionProvider();
            for (ISelectionProvider p : providers) {
                if (p != source) {
                    setSelection(p, (IStructuredSelection) event.getSelection());
                }
            }
            activate();
        }
    }

}
