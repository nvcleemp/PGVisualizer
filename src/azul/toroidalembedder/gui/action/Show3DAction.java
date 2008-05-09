/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.action;

import azul.toroidalembedder.gui.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class Show3DAction extends AbstractAction implements GraphModelListener{

    public Show3DAction(GraphModel graphListModel) {
        super("Show 3D model");
        this.graphListModel = graphListModel;
        
    }
    
    private GraphModel graphListModel;
    private MoleculeDialog dialog = null;

    public void actionPerformed(ActionEvent e) {
            if(dialog==null) {
                dialog = new MoleculeDialog();
                dialog.showDialog(graphListModel.getSelectedGraph());
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
