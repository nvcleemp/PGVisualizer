/* JmolFrame.java
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
package be.ugent.caagt.pg.visualizer.structures;

import be.ugent.caagt.pg.visualizer.structures.jmol.JmolData;
import be.ugent.caagt.pg.visualizer.structures.jmol.JmolMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;

/**
 *
 * @author nvcleemp
 */
public class JmolFrame extends JFrame {

    private JmolPanel panel = new JmolPanel();
    private JCheckBoxMenuItem pgColoringBox;
    private JmolData data;

    public JmolFrame() {
        super("3D model");
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        panel.getViewer().evalString("zap");
        data = new JmolData(panel.getViewer());
        setJMenuBar(JmolMenu.getJMenuBar(panel, data, this));
        JMenu pgColorings = new JMenu("PG Colorings");
        pgColoringBox = new JCheckBoxMenuItem(new AbstractAction("Show PG Coloring") {

            public void actionPerformed(ActionEvent e) {
                showColoring(pgColoringBox.isSelected());
            }
        });
        pgColorings.add(pgColoringBox);
        getJMenuBar().add(pgColorings);
        pack();
    }

    public void setMolecule(Molecule molecule) {
        panel.setMolecule(molecule);
    }

    public void display() {
        data.getViewer().openDOM(null);
        data.getViewer().evalString("delay;");
        if(!isVisible())
            setVisible(true);
        data.getViewer().evalString("select *");
        if (data.areBondsTranslucent())
            data.getViewer().evalString("color BONDS TRANSLUCENT gray");
        else
            data.getViewer().evalString("color BONDS gray");
        if (data.areAtomsTranslucent())
            data.getViewer().evalString("color ATOMS TRANSLUCENT gray");
        else
            data.getViewer().evalString("color ATOMS gray");
        showColoring(pgColoringBox.isSelected());
    }

    public void display(Molecule molecule){
        setMolecule(molecule);
        display();
    }

    private void showColoring(boolean value){
        if(value){
            for (int i = 0; i < panel.getMolecule().getFaceSize(); i++) {
                StringBuffer buf = new StringBuffer();
                String glue = "select ";
                for (int j : panel.getMolecule().getFaceAt(i)) {
                    buf.append(glue);
                    buf.append(MessageFormat.format("ATOMNO={0}", j+1));
                    if(glue.equals("select "))
                        glue = " or ";
                }
                data.getViewer().evalString(buf.toString());
                Color c = panel.getMolecule().getColorOfFace(i);
                if (data.areBondsTranslucent())
                    data.getViewer().evalString(MessageFormat.format("color BONDS TRANSLUCENT [{0},{1},{2}]", c.getRed(), c.getGreen(), c.getBlue()));
                else
                    data.getViewer().evalString(MessageFormat.format("color BONDS [{0},{1},{2}]", c.getRed(), c.getGreen(), c.getBlue()));
                if (data.areAtomsTranslucent())
                    panel.getViewer().evalString(MessageFormat.format("color ATOMS TRANSLUCENT [{0},{1},{2}]", c.getRed(), c.getGreen(), c.getBlue()));
                else
                    panel.getViewer().evalString(MessageFormat.format("color ATOMS [{0},{1},{2}]", c.getRed(), c.getGreen(), c.getBlue()));
                //in the end we select all the atoms to make. Otherwise any menu option chosen by the user
                //will only be performed on the last face.
                panel.getViewer().evalString("select *");
            }
        } else {
            data.getViewer().evalString("select *");
            if (data.areBondsTranslucent())
                data.getViewer().evalString("color BONDS TRANSLUCENT gray");
            else
                data.getViewer().evalString("color BONDS gray");
            if (data.areAtomsTranslucent())
                data.getViewer().evalString("color ATOMS TRANSLUCENT gray");
            else
                data.getViewer().evalString("color ATOMS gray");
        }
    }
}
