/* ExportBitmapAction.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.visualizer.gui.TorusView;
import be.ugent.caagt.pg.preferences.PGPreferences;
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
                    ImageIO.write(torusView.getBitmap(), "PNG", file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
