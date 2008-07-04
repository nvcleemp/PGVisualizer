/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
