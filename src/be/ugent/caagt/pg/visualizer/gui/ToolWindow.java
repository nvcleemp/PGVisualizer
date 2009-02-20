/* ToolWindow.java
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

package be.ugent.caagt.pg.visualizer.gui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ToolWindow extends JFrame {

    public ToolWindow(JPanel toolPanel, JFrame parentWindow) {
        this(toolPanel, parentWindow, false);
    }
    
    public ToolWindow(JPanel toolPanel, JFrame parentWindow, boolean maximize) {
        super(toolPanel.getName());
        parentWindow.addWindowListener(new ParentWindowListener());
        add(toolPanel);
        setAlwaysOnTop(true);
        pack();
        if(maximize)
            setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private class ParentWindowListener extends WindowAdapter{

        @Override
        public void windowClosed(WindowEvent e) {
            ToolWindow.this.setVisible(false);
            ToolWindow.this.dispose();
        }
        
    }

}
