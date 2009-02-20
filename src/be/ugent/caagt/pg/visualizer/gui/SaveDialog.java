/* SaveDialog.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
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

package be.ugent.caagt.pg.visualizer.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author nvcleemp
 */
public class SaveDialog extends JDialog{
    
    private File file;
    private GraphListModel model;
    private JProgressBar bar;
    private JLabel label;

    public SaveDialog(Frame owner, File file, GraphListModel model) {
        super(owner, "Save", false);
        this.file = file;
        this.model = model;
        label = new JLabel();
        bar = new JProgressBar(0, model.getSize());
        bar.setStringPainted(true);
        label.setPreferredSize(new Dimension(300, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label, gbc);
        gbc.gridy = 1;
        add(bar, gbc);
        pack();
    }
    
    public void save() throws FileNotFoundException, IOException{
        new Thread(new Runnable() {
            public void run() {
                FileWriter out = null;
                try{
                    out = new FileWriter(file);
                    setVisible(true);
                    label.setText("Converting graphs");
                    for (int i = 0; i < model.getSize(); i++) {
                        model.commitGraph(i);
                        bar.setValue(i);
                    }
                    bar.setValue(0);
                    label.setText("Writing data to file");
                    for (int i = 0; i < model.getSize(); i++) {
                        out.write(model.getString(i));
                        out.write("\n");
                        bar.setValue(i);
                    }
                } catch (IOException ex){

                } finally {
                    setVisible(false);
                    try {
                        if(out!=null)
                            out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SaveDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
}
