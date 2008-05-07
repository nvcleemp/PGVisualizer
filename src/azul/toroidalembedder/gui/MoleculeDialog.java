/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.DefaultGraph;
import azul.toroidalembedder.graph.DefaultVertex;
import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.molecule.Molecule;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nvcleemp
 */
public class MoleculeDialog extends JDialog{
    
    private TorusView view = new TorusView(0, 0, 0, 0);
    private DefaultGraph result = null;
    private Graph input;
    private boolean done = false;
    private JComboBox comboBox = new JComboBox(Molecule.Embedding.values());
    private JmolViewer viewer = null;


    public MoleculeDialog() {
        setLayout(new BorderLayout());
        view.setPreferredSize(new Dimension(300, 400));
        add(view, BorderLayout.SOUTH);
        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        controls.add(new JLabel("X :"), gbc);
        gbc.weightx = 0;
        gbc.gridx++;
        final JFormattedTextField x = new JFormattedTextField(Integer.valueOf(1));
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
        final JFormattedTextField y = new JFormattedTextField(Integer.valueOf(1));
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
        gbc.fill = GridBagConstraints.BOTH;
        JButton viewButton = new JButton("Graph Preview");
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());
                view.setGraph(result);
            }
        });
        gbc.gridy++;
        controls.add(viewButton, gbc);
        gbc.gridy++;
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
        JButton showButton = new JButton("Show");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());
                try {
                    Rectangle bounds = null;
                    if(viewer != null){
                        bounds = viewer.getBounds();
                        viewer.destroy();
                    }
                    Molecule mol = new Molecule(result, (Molecule.Embedding) comboBox.getSelectedItem());
                    if (mol == null) {
                        return;
                    }
                    File temp = File.createTempFile("PGCML", ".cml");
                    temp.deleteOnExit();
                    Writer out = new FileWriter(temp);
                    out.write(mol.writeCML());
                    out.close();
                    viewer = new JmolViewer();
                    viewer.setBounds(bounds);
                    viewer.setParameter("FORMAT", "CML");
                    viewer.setParameter("MODEL", temp.toString());
                    viewer.show();
                } catch (IOException ex) {
                    Logger.getLogger(Show3DAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        controls.add(showButton, gbc);
        gbc.gridy++;
        /*JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                concat((Integer)x.getValue(), (Integer)y.getValue(), (Integer)shiftX.getValue(), (Integer)shiftY.getValue(), overflowCheckBox.isSelected());
                done = true;
                setVisible(false);
            }
        });
        controls.add(doneButton, gbc);*/
        add(controls, BorderLayout.NORTH);
        pack();
    }

    public Graph concatGraph(Graph input){
        this.input = input;
        concat(1, 1, 0, 0, false);
        view.setGraph(result);
        setVisible(true);
        return result;
    }
    
    private void concat(int x, int y, int shiftX, int shiftY, boolean overflow){
        if(input==null)
            return;
        FundamentalDomain inputDomain = input.getFundamentalDomain();
        result = new DefaultGraph(new FundamentalDomain(inputDomain.getAngle(), inputDomain.getHorizontalSide()*x, inputDomain.getVerticalSide()*y));
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
    }
    
    public void showDialog(Graph input){
        this.input = input;
        concat(1, 1, 0, 0, false);
        view.setGraph(result);
        setVisible(true);
    }
    
}
