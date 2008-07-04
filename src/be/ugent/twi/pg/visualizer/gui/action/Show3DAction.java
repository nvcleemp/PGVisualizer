/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui.action;

import be.ugent.twi.pg.visualizer.gui.MoleculeDialog;
import be.ugent.twi.pg.visualizer.gui.GraphListModelListener;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
import be.ugent.twi.pg.visualizer.gui.DefaultFaceHightlighter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class Show3DAction extends AbstractAction implements GraphListModelListener{

    public Show3DAction(GraphListModel graphListModel) {
        super("Show 3D model");
        this.graphListModel = graphListModel;
        graphListModel.addGraphModelListener(this);
        
    }
    
    private GraphListModel graphListModel;
    private MoleculeDialog dialog = null;

    public void actionPerformed(ActionEvent e) {
            if(dialog==null) {
                dialog = new MoleculeDialog();
                dialog.showDialog(graphListModel.getSelectedGraph(),
                        ((DefaultFaceHightlighter)graphListModel.getSelectedGraphGUIData().getFaceHighlighter()).getFaces(),
                        ((DefaultFaceHightlighter)graphListModel.getSelectedGraphGUIData().getFaceHighlighter()).getMap());
            } else {
                dialog.setVisible(true);
            }
    }

    public void selectedGraphChanged() {
        dialog = null;
    }

    public void intervalAdded(ListDataEvent e) {
        //
    }

    public void intervalRemoved(ListDataEvent e) {
        //
    }

    public void contentsChanged(ListDataEvent e) {
        //
    }

}
