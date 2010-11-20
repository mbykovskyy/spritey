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
package spritey.rcp.editors.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import spritey.rcp.editors.draw2d.HazardBorder;
import spritey.rcp.editors.editparts.SheetEditPart;
import spritey.rcp.editors.editparts.SpriteEditPart;

/**
 * A drag tracker for selecting and dragging sprites in a graphical editor. When
 * no sprites are enclosed by the selection, the sprite sheet will be selected.
 * If the Shift key is pressed at the beginning of a drag, the enclosed sprites
 * will be appended to the current selection. If the MOD1 key is pressed at the
 * beginning of the drag, the enclosed sprites will have their selection state
 * inverted.
 */
public class SpriteDragTracker extends AbstractTool implements DragTracker {

    protected static final int MODE_MARQUEE_TOGGLE = 0;
    protected static final int MODE_MARQUEE_APPEND = 1;
    protected static final int MODE_MARQUEE_DEFAULT = 2;

    protected static final int MODE_SELECT_TOGGLE = 3;
    protected static final int MODE_SELECT_APPEND = 4;
    protected static final int MODE_SELECT_DEFAULT = 5;

    protected static final RGB COLOR1 = new RGB(0, 0, 0);
    protected static final RGB COLOR2 = new RGB(255, 255, 255);

    protected int mode;

    protected IFigure selectionFigure;
    protected SheetEditPart sheetEditPart;

    public SpriteDragTracker(SheetEditPart sheetEditPart) {
        this.sheetEditPart = sheetEditPart;
    }

    @Override
    protected String getCommandName() {
        return REQ_SELECTION;
    }

    private Rectangle getSelectionRectangle() {
        return new Rectangle(getStartLocation(), getLocation());
    }

    private IFigure getSelectionFigure() {
        if (selectionFigure == null) {
            selectionFigure = new Figure();
            selectionFigure.setBorder(new HazardBorder(COLOR1, COLOR2));
            addFeedback(selectionFigure);
        }
        return selectionFigure;
    }

    private void showSelectionFigure() {
        Rectangle rect = getSelectionRectangle().getCopy();
        getSelectionFigure().translateToRelative(rect);
        getSelectionFigure().setBounds(rect);
    }

    private void hideSelectionFigure() {
        if (selectionFigure != null) {
            removeFeedback(selectionFigure);
            selectionFigure = null;
        }
    }

    private boolean isSelectable(GraphicalEditPart part) {
        IFigure figure = part.getFigure();
        return ((part instanceof SpriteEditPart) && part.isSelectable()
                && figure.isVisible() && figure.isShowing());
    }

    private void calculateSelection(List<Object> selections,
            List<Object> deselections) {
        Rectangle selection = getSelectionRectangle();
        ScalableRootEditPart root = (ScalableRootEditPart) getCurrentViewer()
                .getRootEditPart();

        calculateSelection(root, selection, selections, deselections);
    }

    private void calculateSelection(GraphicalEditPart part,
            Rectangle selection, List<Object> selections,
            List<Object> deselections) {
        IFigure figure = part.getFigure();
        Rectangle sprite = figure.getBounds().getCopy();

        figure.translateToAbsolute(sprite);

        if (isSelectable(part) && selection.intersects(sprite)) {
            if ((part.getSelected() == EditPart.SELECTED_NONE)
                    || (mode != MODE_MARQUEE_TOGGLE)) {
                selections.add(part);
            } else {
                deselections.add(part);
            }
        }

        for (Object child : part.getChildren()) {
            calculateSelection((GraphicalEditPart) child, selection,
                    selections, deselections);
        }
    }

    protected void handleSingleSelect() {
        EditPartViewer viewer = getCurrentViewer();
        EditPart part = viewer.findObjectAt(getLocation());

        if (!(part instanceof SpriteEditPart)) {
            viewer.select(sheetEditPart);
        } else if (mode == MODE_SELECT_DEFAULT) {
            viewer.select(part);
        } else if (mode == MODE_SELECT_APPEND) {
            viewer.appendSelection(part);
        } else if (mode == MODE_SELECT_TOGGLE) {
            if (part.getSelected() == EditPart.SELECTED_NONE) {
                viewer.appendSelection(part);
            } else {
                viewer.deselect(part);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void handleMarqueeSelect() {
        hideSelectionFigure();

        List<Object> selections = new ArrayList<Object>();
        List<Object> deselections = new ArrayList<Object>();
        calculateSelection(selections, deselections);

        EditPartViewer viewer = getCurrentViewer();
        if (mode != MODE_MARQUEE_DEFAULT) {
            selections.addAll(viewer.getSelectedEditParts());
            selections.removeAll(deselections);
        }

        if (selections.isEmpty()) {
            // Sheet should be selected when no sprites are selected.
            viewer.select(sheetEditPart);
        } else {
            viewer.setSelection(new StructuredSelection(selections.toArray()));
        }
    }

    @Override
    protected boolean handleButtonDown(int button) {
        if (1 != button) {
            setState(STATE_INVALID);
        } else if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)) {
            if (getCurrentInput().isModKeyDown(SWT.MOD1)) {
                mode = MODE_SELECT_TOGGLE;
            } else if (getCurrentInput().isShiftKeyDown()) {
                mode = MODE_SELECT_APPEND;
            } else {
                mode = MODE_SELECT_DEFAULT;
            }
        }
        return true;
    }

    @Override
    protected boolean handleDragInProgress() {
        if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
            // Change mode to a marquee selection.
            switch (mode) {
            case MODE_SELECT_TOGGLE:
                mode = MODE_MARQUEE_TOGGLE;
                break;
            case MODE_SELECT_APPEND:
                mode = MODE_MARQUEE_APPEND;
                break;
            case MODE_SELECT_DEFAULT:
                mode = MODE_MARQUEE_DEFAULT;
                break;

            }
            showSelectionFigure();
        }
        return true;
    }

    @Override
    protected boolean handleButtonUp(int button) {
        if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
            if (sheetEditPart.getSelected() != EditPart.SELECTED_NONE) {
                getCurrentViewer().deselect(sheetEditPart);
            }

            // For performance reasons single selection is handled differently
            // to marquee selection.
            if ((MODE_SELECT_APPEND == mode) || (MODE_SELECT_TOGGLE == mode)
                    || (MODE_SELECT_DEFAULT == mode)) {
                handleSingleSelect();
            } else if ((MODE_MARQUEE_APPEND == mode)
                    || (MODE_MARQUEE_TOGGLE == mode)
                    || (MODE_MARQUEE_DEFAULT == mode)) {
                handleMarqueeSelect();
            }
        }
        handleFinished();
        return true;
    }

}
