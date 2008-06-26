/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.action;

import azul.toroidalembedder.gui.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class RevertGraphAction extends AbstractAction{
    
    private GraphListModel graphModel;

    public RevertGraphAction(GraphListModel graphModel) {
        super("Revert graph");
        this.graphModel = graphModel;
    }

    public void actionPerformed(ActionEvent e) {
        graphModel.revertSelectedGraph();
    }

}
