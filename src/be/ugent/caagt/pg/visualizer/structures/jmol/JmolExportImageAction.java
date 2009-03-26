/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.structures.jmol;

import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.caagt.pg.visualizer.gui.TorusView;
import be.ugent.caagt.pg.visualizer.structures.JmolPanel;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
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
public class JmolExportImageAction extends AbstractAction{

    private JmolPanel panel;

    public JmolExportImageAction(JmolPanel panel) {
        super("Save image");
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        BufferedImage im = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        panel.getViewer().renderScreenImage(g, panel.getSize(), g.getClipBounds());
        try {
            JFileChooser chooser;
            String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
            if(dir==null)
                chooser = new JFileChooser();
            else
                chooser = new JFileChooser(new File(dir));
            if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, file.getParent());
                if(!file.exists() || JOptionPane.showConfirmDialog(null, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    ImageIO.write(im, "PNG", file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}