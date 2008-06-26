/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azul.toroidalembedder.gui.action;

import azul.toroidalembedder.gui.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

/**
 *
 * @author nvcleemp
 */
public class SaveGraphListAction extends AbstractAction {

    private GraphListModel graphListModel;

    public SaveGraphListAction(GraphListModel graphListModel) {
        super("Save graph list");
        this.graphListModel = graphListModel;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                new SaveDialog(null, chooser.getSelectedFile(), graphListModel).save();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
