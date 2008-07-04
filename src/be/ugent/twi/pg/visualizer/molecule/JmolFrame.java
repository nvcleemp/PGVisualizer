/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.molecule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class JmolFrame extends JFrame{
    
    private JmolPanel panel = new JmolPanel();
    private JCheckBox showColoring;

    public JmolFrame() {
        super("3D model");
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        panel.getViewer().evalString("zap");
        //TODO: is there a way in Jmol to disable prints to System.out ?
        //panel.getViewer().evalString("set systemOutEnabled false");
        JPanel controls = new JPanel();
        showColoring = new JCheckBox(new AbstractAction("Show colouring") {
            public void actionPerformed(ActionEvent e) {
                showColoring(showColoring.isSelected());
            }
        });
        controls.add(showColoring);
        add(controls, BorderLayout.EAST);
        pack();
    }

    public void setMolecule(Molecule molecule) {
        panel.setMolecule(molecule);
    }
    
    public void display(){
        panel.getViewer().openDOM(null);
        panel.getViewer().evalString("delay;");
        if(!isVisible())
            setVisible(true);
        panel.getViewer().evalString("color BONDS gray");
        showColoring(showColoring.isSelected());
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
                panel.getViewer().evalString(buf.toString());
                Color c = panel.getMolecule().getColorOfFace(i);
                panel.getViewer().evalString(MessageFormat.format("color BONDS [{0},{1},{2}]", c.getRed(), c.getGreen(), c.getBlue()));
            }
        } else {
            panel.getViewer().evalString("select *");
            panel.getViewer().evalString("color BONDS gray");
        }
    }
}
