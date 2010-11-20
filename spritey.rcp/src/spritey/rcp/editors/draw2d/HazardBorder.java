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
package spritey.rcp.editors.draw2d;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * A 1 unit wide border resembling hazard stripe.
 */
public class HazardBorder extends AbstractBorder {

    private static final int WIDTH = 1;
    private static final int DASH_LENGTH = 4;
    private static final RGB COLOR1 = new RGB(0, 0, 0);
    private static final RGB COLOR2 = new RGB(255, 255, 0);

    private Color color1;
    private Color color2;

    private LineAttributes attributes1;
    private LineAttributes attributes2;

    /**
     * Creates an new instance of HazrdBorder.
     */
    public HazardBorder() {
        this(COLOR1, COLOR2);
    }

    /**
     * Creates an new instance of HazrdBorder.
     * 
     * @param c1
     *        the stripe color one.
     * @param c2
     *        the stripe color two.
     */
    public HazardBorder(RGB c1, RGB c2) {
        Device device = Display.getCurrent();
        color1 = new Color(device, c1);
        color2 = new Color(device, c2);

        float[] dash = new float[] { DASH_LENGTH };
        attributes1 = new LineAttributes(WIDTH, SWT.CAP_FLAT, SWT.JOIN_MITER,
                SWT.LINE_CUSTOM, dash, 0, 10);
        attributes2 = new LineAttributes(WIDTH, SWT.CAP_FLAT, SWT.JOIN_MITER,
                SWT.LINE_CUSTOM, dash, DASH_LENGTH, 10);
    }

    @Override
    public Insets getInsets(IFigure figure) {
        return new Insets(WIDTH);
    }

    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        tempRect.setBounds(getPaintRectangle(figure, insets));
        --tempRect.width;
        --tempRect.height;

        graphics.setForegroundColor(color1);
        graphics.setLineAttributes(attributes1);

        // TODO Draw a polyline instead of a rectangle until fix for a bug
        // #327693 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=327693) is
        // available.
        int[] points = new int[10];
        points[0] = tempRect.x;
        points[1] = tempRect.y;
        points[2] = tempRect.x + tempRect.width;
        points[3] = tempRect.y;
        points[4] = tempRect.x + tempRect.width;
        points[5] = tempRect.y + tempRect.height;
        points[6] = tempRect.x;
        points[7] = tempRect.y + tempRect.height;
        points[8] = tempRect.x;
        points[9] = tempRect.y;
        graphics.drawPolyline(points);

        graphics.setForegroundColor(color2);
        graphics.setLineAttributes(attributes2);

        // TODO Draw a polyline instead of a rectangle until fix for a bug
        // #327693 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=327693) is
        // available.
        points = new int[10];
        points[0] = tempRect.x + DASH_LENGTH;
        points[1] = tempRect.y;
        points[2] = tempRect.x + tempRect.width;
        points[3] = tempRect.y;
        points[4] = tempRect.x + tempRect.width;
        points[5] = tempRect.y + tempRect.height;
        points[6] = tempRect.x;
        points[7] = tempRect.y + tempRect.height;
        points[8] = tempRect.x;
        points[9] = tempRect.y - DASH_LENGTH;
        graphics.drawPolyline(points);
    }

}
