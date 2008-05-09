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
public class FilterAction extends AbstractAction{
    
    public FilterAction(GraphModel graphListModel) {
        super("Filter list");
        this.graphListModel = graphListModel;
        dialog = new SearchDialog(graphListModel);
        dialog.setDefaultCloseOperation(SearchDialog.HIDE_ON_CLOSE);
    }
    
    private GraphModel graphListModel;
    private SearchDialog dialog;

    public void actionPerformed(ActionEvent e) {
            dialog.setVisible(true);
    }
}
