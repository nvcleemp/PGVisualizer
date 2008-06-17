/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.action;

import azul.toroidalembedder.gui.GraphFaceSelectionModel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class ClearFaceSelectionAction extends AbstractAction{
    
    private GraphFaceSelectionModel selectionModel;

    public ClearFaceSelectionAction(GraphFaceSelectionModel selectionModel) {
        super("Clear selection");
        this.selectionModel = selectionModel;
    }

    public void actionPerformed(ActionEvent e) {
        selectionModel.clearSelection();
    }

}
