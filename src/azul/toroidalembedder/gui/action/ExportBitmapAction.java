/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.action;

import azul.toroidalembedder.gui.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author nvcleemp
 */
public class ExportBitmapAction extends AbstractAction{

    private TorusView torusView;

    public ExportBitmapAction(TorusView torusView) {
        super("Save bitmap");
        this.torusView = torusView;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser();
            if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if(!file.exists() || JOptionPane.showConfirmDialog(null, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    ImageIO.write(torusView.getBitmap(), "PNG", file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}