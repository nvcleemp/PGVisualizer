/* GraphShiftOperations.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
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

import be.ugent.caagt.pg.graph.Graph;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class GraphShiftOperations extends JPanel {
    private Graph graph;
    
    GraphShiftOperations(boolean withBorder) {
        setLayout(new GridLayout(3, 3));
        add(new JButton(new ShiftAction("NW", -1, -1)));
        add(new JButton(new ShiftAction("N", 0, -1)));
        add(new JButton(new ShiftAction("NE", 1, -1)));
        add(new JButton(new ShiftAction("W", -1, 0)));
        add(new JLabel(""));
        add(new JButton(new ShiftAction("E", 1, 0)));
        add(new JButton(new ShiftAction("SW", -1, 1)));
        add(new JButton(new ShiftAction("S", 0, 1)));
        add(new JButton(new ShiftAction("SE", 1, 1)));
        if(withBorder)
            setBorder(BorderFactory.createTitledBorder("Graph shift operations"));
    }

    public GraphShiftOperations(Graph graph) {
        this(graph, true);
    }
    
    public GraphShiftOperations(Graph graph, boolean withBorder) {
        this(withBorder);
        this.graph = graph;
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }
    
    private class ShiftAction extends AbstractAction{
        
        int x = 0;
        int y = 0;
        
        public ShiftAction(String name, int x, int y){
            super(name);
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            graph.translate(0.1*x, 0.1*y);
        }
    }
    
}
