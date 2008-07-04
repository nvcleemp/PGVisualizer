/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.visualizer.gui.GraphFaceSelectionModel;
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
