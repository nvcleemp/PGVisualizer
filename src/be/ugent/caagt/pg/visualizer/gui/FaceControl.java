/* FaceControl.java
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

import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.visualizer.gui.toggler.FillFacesToggler;
import be.ugent.caagt.pg.visualizer.gui.toggler.Toggler;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class FaceControl extends JPanel implements ListSelectionListener{
    
    private static final Color AZULENE_BLUE = new Color(0, 100, 200);
    
    private TorusView torusView;
    private GraphListModel graphListModel;

    public FaceControl(TorusView torusView, GraphListModel graphListModel) {
        this.torusView = torusView;
        this.graphListModel = graphListModel;
        graphListModel.addListSelectionListener(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        Toggler fillFaces = new FillFacesToggler(torusView);
        final JCheckBox drawFaces = fillFaces.getJCheckBox(); //new JCheckBox("Fill faces", torusView.isPaintFaces());
        //drawFaces.addItemListener(new ItemListener() {
        //    public void itemStateChanged(ItemEvent e) {
        //        FaceControl.this.torusView.setPaintFaces(drawFaces.isSelected());
        //    }
        //});
        add(drawFaces, gbc);
        final JSlider transparency = new JSlider(0, 255, torusView.getTransparency());
        transparency.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FaceControl.this.torusView.setTransparency(transparency.getValue());
            }
        });
        gbc.gridx++;
        gbc.weightx = 1;
        add(transparency, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(new JButton(new ColorAction()), gbc);
        gbc.gridy++;
        add(new JButton(new RemoveColorAction()), gbc);
        setName("Face");
    }



    public void valueChanged(ListSelectionEvent e) {
        //TODO: still use?
    }
    
    private class ColorAction extends AbstractAction {
        
        public ColorAction() {
            super("Color selected faces");
        }

        public void actionPerformed(ActionEvent e) {
            if(torusView.getFaceHighlight()==null)
                torusView.setFaceHighlight(new DefaultFaceHightlighter());

            Color selected = JColorChooser.showDialog(torusView, "Select color", AZULENE_BLUE);
            if(selected != null){
                FaceHighlighter theHighlighter = torusView.getFaceHighlight();
                for (Face f : torusView.getFaceSelectionModel().getSelectedFaces()) {
                    theHighlighter.setColor(f, selected);
                }
                torusView.repaint();
            }
        }
    }

    private class RemoveColorAction extends AbstractAction {
        
        public RemoveColorAction() {
            super("Remove color of selected faces");
        }

        public void actionPerformed(ActionEvent e) {
            if(torusView.getFaceHighlight()==null)
                return;

            FaceHighlighter theHighlighter = torusView.getFaceHighlight();
            for (Face f : torusView.getFaceSelectionModel().getSelectedFaces()) {
                theHighlighter.setColor(f, null);
            }
            torusView.repaint();
        }
    }

}
