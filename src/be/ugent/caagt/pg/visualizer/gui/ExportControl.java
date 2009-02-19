/* ExportControl.java
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

import be.ugent.caagt.pg.io.IOManager;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.gui.action.CommitGraphAction;
import be.ugent.caagt.pg.visualizer.gui.action.ExportBitmapAction;
import be.ugent.caagt.pg.visualizer.gui.action.ExportExcelAction;
import be.ugent.caagt.pg.visualizer.gui.action.ExportSVGAction;
import be.ugent.caagt.pg.visualizer.gui.action.RevertGraphAction;
import be.ugent.caagt.pg.visualizer.gui.action.SaveGraphListAction;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ExportControl extends JPanel{
    
    private ExportControl(){
        //
    }

    public static JPanel getPanel(TorusView torusView, final Graph graph) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton(new ExportBitmapAction(torusView)));
        panel.add(new JButton(new AbstractAction("Save graph") {
            public void actionPerformed(ActionEvent e) {
                System.out.println(IOManager.writePG(graph));
            }
        }));
        panel.setBorder(BorderFactory.createTitledBorder("Export"));
        return panel;
    }
    
    public static JPanel getPanel(final TorusView torusView, final GraphListModel graphModel) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton(new ExportBitmapAction(torusView)));
        panel.add(new JButton(new CommitGraphAction(graphModel)));
        panel.add(new JButton(new RevertGraphAction(graphModel)));
        panel.add(new JButton(new SaveGraphListAction(graphModel)));
        panel.setName("Export");
        return panel;
    }
    
    public static JMenu getMenu(final TorusView torusView, final GraphListModel graphModel) {
        JMenu panel = new JMenu("Export");
        panel.add(new JMenuItem(new ExportBitmapAction(torusView)));
        panel.add(new JMenuItem(new ExportSVGAction(graphModel)));
        panel.add(new JMenuItem(new ExportExcelAction(graphModel)));
        panel.add(new JMenuItem(new CommitGraphAction(graphModel)));
        panel.add(new JMenuItem(new RevertGraphAction(graphModel)));
        panel.add(new JMenuItem(new SaveGraphListAction(graphModel)));
        return panel;
    }
}
