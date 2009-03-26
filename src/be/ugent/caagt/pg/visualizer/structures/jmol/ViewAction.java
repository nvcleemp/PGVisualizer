/* ViewAction.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.visualizer.structures.jmol;

import java.text.MessageFormat;
import org.jmol.api.JmolViewer;

/**
 * Action that allows to change the view of a given JmolViewer.
 *
 * @author nvcleemp
 */
public class ViewAction extends JmolAction {

    public static final String FRONT =  "moveto 2.0 front;delay 1";
    public static final String LEFT =   "moveto 1.0 front;moveto 2.0 left;delay 1";
    public static final String RIGHT =  "moveto 1.0 front;moveto 2.0 right;delay 1";
    public static final String TOP =    "moveto 1.0 front;moveto 2.0 top;delay 1";
    public static final String BOTTOM = "moveto 1.0 front;moveto 2.0 bottom;delay 1";
    public static final String BACK =   "moveto 1.0 front;moveto 2.0 back;delay 1";


    private final String command;

    /**
     * Creates a <code>ViewAction</code> that changes the given <code>
     * JmolViewer</code> to the given view. It is adviced to pass in one
     * of the constants defined in this class as <tt>view</tt>.
     *
     * @param name The name for this action
     * @param viewer The viewer to which this action applies
     * @param view The view that is to be shown
     */
    public ViewAction(String name, JmolViewer viewer, String view) {
        super(name, viewer);
        command = MessageFormat.format("if not(showBoundBox);if not(showUnitcell);boundbox on;{0};boundbox off;endif;else;{0};endif;", view);
    }

    @Override
    public String getCommand() {
        return command;
    }

}
