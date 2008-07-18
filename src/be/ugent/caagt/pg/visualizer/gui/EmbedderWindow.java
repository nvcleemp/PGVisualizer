/* EmbedderWindow.java
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


import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.GraphListener;
import be.ugent.caagt.pg.io.FileFormatException;
import be.ugent.caagt.pg.io.IOManager;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.gui.action.ExportBitmapAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


/**
 *
 * @author nvcleemp
 */
public class EmbedderWindow extends JFrame implements GraphListener {
    private Graph graph;
    private TorusView torusView = new TorusView(-2, -2, 2, 2);
    private JSplitPane split;

    public EmbedderWindow(Graph graph) {
        super("Toroidal embedder: EmbedderWindow");
        this.graph = graph;
        torusView.setGraph(graph);
        if(graph instanceof DefaultGraph)
            ((DefaultGraph)graph).addGraphListener(this);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(torusView);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // view controls
        JPanel viewPanel = new ViewController(torusView);
        
        // graph controls
        JPanel graphPanel = new GraphOperations(graph);
        
        // embedder controls
        JPanel embedderPanel = new EmbedderControl(graph);
        
        // export controls
        JPanel exportPanel = new JPanel(new GridLayout(0, 2));
        exportPanel.add(new JButton(new ExportBitmapAction(torusView)));
        JButton saveGraph = new JButton("Save graph");
        saveGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(IOManager.writePG(EmbedderWindow.this.graph));
            }
        });
        exportPanel.add(saveGraph);
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export"));
        
        JPanel controls = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        controls.add(viewPanel, gbc);
        gbc.gridx = 1;
        controls.add(graphPanel, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 2;
        controls.add(embedderPanel, gbc);
        gbc.gridy = 1;
        controls.add(exportPanel, gbc);
        

        split.setBottomComponent(controls);
        setContentPane(split);
        pack();
    }
    
    public static void main(String[] args) {
        try {
            //new EmbedderWindow(IOManager.readPG("3|0 0;0 0;0 0|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1;2 1 1 0")).setVisible(true);
            new EmbedderWindow(IOManager.readPG("10|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
            new EmbedderWindow(IOManager.readPG("8|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 1 0 0;1 2 0 0;2 3 0 0;3 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0")).setVisible(true);
            TorusViewTest.main(args);
        } catch (FileFormatException ex) {
            Logger.getLogger(EmbedderWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void graphChanged() {
        split.repaint();
    }
    
    public void fundamentalDomainChanged(FundamentalDomain oldDomain) {
        split.repaint();
    }
    
    public void fundamentalDomainShapeChanged() {
        split.repaint();
    }
}
