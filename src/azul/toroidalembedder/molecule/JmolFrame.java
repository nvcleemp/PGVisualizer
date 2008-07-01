/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.molecule;

import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class JmolFrame extends JFrame{
    
    private JmolPanel panel = new JmolPanel();

    public JmolFrame() {
        super("3D model");
        add(panel);
        panel.getViewer().evalString("zap");
        //TODO: is there a way in Jmol to disable prints to System.out ?
        //panel.getViewer().evalString("set systemOutEnabled false");
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
    }

    public void display(Molecule molecule){
        setMolecule(molecule);
        display();
    }
}
