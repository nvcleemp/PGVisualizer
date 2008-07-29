/* Tiled3DStructureDialog.java
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

import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.DefaultVertex;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.action.Show3DAction;
import be.ugent.caagt.pg.visualizer.molecule.JRealityFrame;
import be.ugent.caagt.pg.visualizer.molecule.JmolFrame;
import be.ugent.caagt.pg.visualizer.molecule.Molecule;
import be.ugent.caagt.pg.visualizer.molecule.Tiled3DStructure;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 * @author nvcleemp
 */
public class Tiled3DStructureDialog extends JDialog{
    
    private TorusView view = new TorusView(0, 0, 0, 0);
    private DefaultGraph result = null;
    private Graph input;
    private List<Face> inputFaces;
    private List<Face> resultFaces;
    private JComboBox comboBox = new JComboBox(Molecule.Embedding.values());
    private JmolFrame frame = new JmolFrame();
    private JRealityFrame frame3D = new JRealityFrame();
    private Map<Face, Color> inputColors;
    private Map<Face, Color> resultColors;

    public Tiled3DStructureDialog() {
        setLayout(new BorderLayout());
        view.setPreferredSize(new Dimension(300, 400));
        view.setPaintFaces(false);
        add(view, BorderLayout.SOUTH);
        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        controls.add(new JLabel("X :"), gbc);
        gbc.weightx = 0;
        gbc.gridx++;
        final JFormattedTextField x = new JFormattedTextField(Integer.valueOf(6));
        x.setColumns(3);
        controls.add(x, gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        controls.add(new JLabel("Shift X :"), gbc);
        gbc.weightx = 0;
        gbc.gridx++;
        final JFormattedTextField shiftX = new JFormattedTextField(Integer.valueOf(0));
        shiftX.setColumns(3);
        controls.add(shiftX, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 1;
        controls.add(new JLabel("Y :"), gbc);
        gbc.gridx++;
        gbc.weightx = 0;
        final JFormattedTextField y = new JFormattedTextField(Integer.valueOf(6));
        y.setColumns(3);
        controls.add(y, gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        controls.add(new JLabel("Shift Y :"), gbc);
        gbc.weightx = 0;
        gbc.gridx++;
        final JFormattedTextField shiftY = new JFormattedTextField(Integer.valueOf(0));
        shiftY.setColumns(3);
        controls.add(shiftY, gbc);
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        final JCheckBox overflowCheckBox = new JCheckBox("with overflow", false);
        controls.add(overflowCheckBox, gbc);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        JButton viewButton = new JButton("Graph Preview");
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());
                view.setGraph(result);
            }
        });
        controls.add(viewButton, gbc);
        gbc.gridx = 2;
        JButton shiftButton = new JButton("Optimal shift");
        shiftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double hOffset = input.getFundamentalDomain().getHorizontalShift()*(Integer)y.getValue();
                double amount = hOffset/input.getFundamentalDomain().getHorizontalSide();
                shiftX.setValue((int)Math.round(amount));
                shiftY.setValue(Integer.valueOf(0));
            }
        });
        controls.add(shiftButton, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        comboBox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((Molecule.Embedding)value).getDescription());
                return this;
            }
            
        });
        controls.add(comboBox, gbc);
        gbc.gridy++;
        JButton show3DButton = new JButton("Show tiled structure");
        show3DButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected(), true);

                Tiled3DStructure struc = new Tiled3DStructure(result, resultFaces, resultColors, (Molecule.Embedding) comboBox.getSelectedItem(), false);
                if (struc == null) {
                    return;
                }
                frame3D.display(struc);
            }
        });
        controls.add(show3DButton, gbc);
        gbc.gridy++;
        JButton showMoleculeButton = new JButton("Show molecule");
        showMoleculeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());

                Molecule mol = new Molecule(result, resultFaces, resultColors, (Molecule.Embedding) comboBox.getSelectedItem());
                if (mol == null) {
                    return;
                }
                frame.display(mol);
            }
        });
        controls.add(showMoleculeButton, gbc);
        gbc.gridy++;
        JButton saveButton = new JButton("Save CML file");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());
                try {
                    Molecule mol = new Molecule(result, (Molecule.Embedding) comboBox.getSelectedItem());
                    if (mol == null) {
                        return;
                    }
                    JFileChooser chooser;
                    String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
                    if(dir==null)
                        chooser = new JFileChooser();
                    else
                        chooser = new JFileChooser(new File(dir));
                    if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                        File file = chooser.getSelectedFile();
                        PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, file.getParent());
                        if(!file.exists() || JOptionPane.showConfirmDialog(null, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            Writer out = new FileWriter(file);
                            out.write(mol.writeCML());
                            out.close();
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Show3DAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        controls.add(saveButton, gbc);
        add(controls, BorderLayout.NORTH);
        
        //Focus managment
        FocusListener selectOnFocus = new SelectOnFocus();
        x.addFocusListener(selectOnFocus);
        y.addFocusListener(selectOnFocus);
        shiftX.addFocusListener(selectOnFocus);
        shiftY.addFocusListener(selectOnFocus);
        Action xRequest = new RequestFocusAction(x);
        Action yRequest = new RequestFocusAction(y);
        Action shiftXRequest = new RequestFocusAction(shiftX);
        Action shiftYRequest = new RequestFocusAction(shiftY);
        x.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        x.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        x.getActionMap().put("down", yRequest);
        x.getActionMap().put("right", shiftXRequest);
        y.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        y.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        y.getActionMap().put("up", xRequest);
        y.getActionMap().put("right", shiftYRequest);
        shiftX.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        shiftX.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        shiftX.getActionMap().put("down", shiftYRequest);
        shiftX.getActionMap().put("left", xRequest);
        shiftY.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        shiftY.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        shiftY.getActionMap().put("up", shiftXRequest);
        shiftY.getActionMap().put("left", yRequest);
        pack();
    }

    public Graph concatGraph(Graph input, List<Face> coloredFaces){
        this.input = input;
        inputFaces = new ArrayList<Face>(coloredFaces);
        view.setGraph(input);
        setVisible(true);
        return result;
    }
    
    private void concat(int x, int y, int shiftX, int shiftY, boolean overflow){
        concat(x, y, shiftX, shiftY, overflow, false);
    }
    
    private void concat(int x, int y, int shiftX, int shiftY, boolean overflow, boolean allFaces){
        if(input==null)
            return;
        FundamentalDomain inputDomain = input.getFundamentalDomain();
        result = new DefaultGraph(new FundamentalDomain(inputDomain.getAngle(), inputDomain.getHorizontalSide()*x, inputDomain.getVerticalSide()*y));
        resultFaces = new ArrayList<Face>();
        resultColors = new HashMap<Face, Color>();
        DefaultVertex[] vertices = new DefaultVertex[x*y*input.getVertices().size()];
        int verticalOffset = -(y - 1);
        for (int yi = 0; yi < y; yi++) {
            int horizontalOffset = -(x - 1);
            for (int xi = 0; xi < x; xi++) {
                for (int i = 0; i < input.getVertices().size(); i++) {
                    int index = (yi * x + xi)*input.getVertices().size() + i;
                    double coordX = (input.getVertex(i).getRawX() + horizontalOffset)/x;
                    double coordY = (input.getVertex(i).getRawY() + verticalOffset)/y;
                    vertices[index] = new DefaultVertex(coordX, coordY);
                    result.addVertex(vertices[index]);
                }
                horizontalOffset +=2;
            }
            verticalOffset+=2;
        }
        for (Vertex vertex : input.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                if(vertex.getIndex() <= edge.getEnd().getIndex()){
                    for (int yi = 0; yi < y; yi++) {
                        int newTargetY = (edge.getTargetY()+yi)/y;
                        int internalY = (edge.getTargetY()+yi)%y;
                        if((edge.getTargetY()+yi)<0){
                            newTargetY = ((edge.getTargetY()+yi)- y + 1)/y;
                            internalY = (internalY + y)%y;
                        }
                        for (int xi = 0; xi < x; xi++) {
                            int newTargetX = (edge.getTargetX()+xi)/x;
                            int internalX = ((edge.getTargetX()+xi)%x + x)%x;
                            if((edge.getTargetX()+xi)<0){
                                newTargetX = ((edge.getTargetX()+xi)- x + 1)/x;
                                internalX = (internalX + x)%x;
                            }
                            //apply shift
                            internalX += (newTargetY * shiftX);
                            internalY += (newTargetX * shiftY);
                            if(overflow){
                                internalX = (internalX%x + x)%x;
                                internalY = (internalY%y + y)%y;
                            }
                            if(!(internalX<0 || internalX >= x || internalY < 0 || internalY >= y))
                                result.addEdge((yi*x + xi)*input.getVertices().size() + vertex.getIndex(), (internalY*x + internalX)*input.getVertices().size() + edge.getEnd().getIndex(), newTargetX, newTargetY);
                        }
                    }
                }
            }
        }
        for (Face face : inputFaces) {
            if(allFaces || inputColors.containsKey(face)){
                for (int yi = 0; yi < y; yi++) {
                    for (int xi = 0; xi < x; xi++) {
                        Face newFace = new Face();
                        int xFace = 0;
                        int yFace = 0;
                        for (Edge edge : face.getEdges()) {
                            int newTargetX = (xFace+xi)/x;
                            int internalX = ((xi+xFace)%x + x)%x;
                            int newTargetY = (yFace+yi)/y;
                            int internalY = ((yi+yFace)%y + y)%y;
                            internalX += (newTargetY * shiftX);
                            internalY += (newTargetX * shiftY);
                            if(overflow){
                                internalX = (internalX%x + x)%x;
                                internalY = (internalY%y + y)%y;
                            }
                            if(!(internalX<0 || internalX >= x || internalY < 0 || internalY >= y))
                                newFace.add(vertices[(internalY*x + internalX)*input.getVertices().size() + edge.getStart().getIndex()]);
                            xFace += edge.getTargetX();
                            yFace += edge.getTargetY();
                        }
                        resultFaces.add(newFace);
                        Color c = inputColors.get(face);
                        if(c!=null)
                            resultColors.put(newFace, c);
                        else
                            resultColors.put(newFace, FaceColorMapping.getColorFor(newFace.getSize()));
                    }
                }
            }
        }
    }
    
    public void showDialog(Graph input, List<Face> coloredFaces, Map<Face, Color> colors){
        this.input = input;
        inputFaces = new ArrayList<Face>(coloredFaces);
        this.inputColors = new HashMap<Face, Color>(colors);
        view.setGraph(input);
        setVisible(true);
    }
    
    private static class SelectOnFocus implements FocusListener {
        
        public void focusGained(FocusEvent e) {
            if(e.getComponent()  instanceof JTextComponent){
                final JTextComponent textComp = (JTextComponent)e.getComponent();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        textComp.selectAll();
                    }
                });
            }
        }

        public void focusLost(FocusEvent e) {
            //
        }
        
    }
    
    private static class RequestFocusAction extends AbstractAction {
        
        private JComponent comp;

        public RequestFocusAction(JComponent comp) {
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.requestFocusInWindow();
        }
        
    }
    
}
