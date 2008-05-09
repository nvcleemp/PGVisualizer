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
public class CommitGraphAction extends AbstractAction{
    
    private GraphModel graphModel;

    public CommitGraphAction(GraphModel graphModel) {
        super("Commit graph");
        this.graphModel = graphModel;
    }

    public void actionPerformed(ActionEvent e) {
        graphModel.commitSelectedGraph();
    }

}
