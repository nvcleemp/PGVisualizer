/* GraphInfo.java
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

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class GraphInfo extends JPanel implements ListSelectionListener{
    
    private GraphListModel model;
    private JLabel sizeLabel;
    private JLabel numberLabel;

    public GraphInfo(GraphListModel model) {
        this.model = model;
        if(model.getSelectedGraph()==null)
            model.getSelectionModel().setSelectionInterval(0, 0);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Number of vertices per domain: "), gbc);
        gbc.gridx = 1;
        sizeLabel = new JLabel("" +  model.getSelectedGraph().getVertices().size());
        add(sizeLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Catalogue number: "), gbc);
        gbc.gridx = 1;
        numberLabel = new JLabel("" +  model.getSelectedCatalogueNumber());
        add(numberLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton symbol = new JButton("Original Delaney symbol");
        symbol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog((Frame)null, "Delaney symbol", true);
                dialog.setSize(360, 360);
                dialog.add(new JScrollPane(new JTextArea(GraphInfo.this.model.getSelectedGraphGUIData().getSymbol())));
                dialog.setVisible(true);
            }
        });
        add(symbol, gbc);
        model.addListSelectionListener(this);
        setName("Info");
    }

    public void valueChanged(ListSelectionEvent e) {
        sizeLabel.setText("" +  model.getSelectedGraph().getVertices().size());
        numberLabel.setText("" +  model.getSelectedCatalogueNumber());
    }

}
