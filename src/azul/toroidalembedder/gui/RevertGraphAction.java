/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class RevertGraphAction extends AbstractAction{
    
    private GraphModel graphModel;

    public RevertGraphAction(GraphModel graphModel) {
        super("Revert graph");
        this.graphModel = graphModel;
    }

    public void actionPerformed(ActionEvent e) {
        graphModel.revertSelectedGraph();
    }

}
