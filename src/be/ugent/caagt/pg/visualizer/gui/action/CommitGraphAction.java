/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class CommitGraphAction extends AbstractAction{
    
    private GraphListModel graphModel;

    public CommitGraphAction(GraphListModel graphModel) {
        super("Commit graph");
        this.graphModel = graphModel;
    }

    public void actionPerformed(ActionEvent e) {
        graphModel.commitSelectedGraph();
    }

}
