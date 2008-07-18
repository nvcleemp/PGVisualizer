/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.embedder.gui;

import be.ugent.caagt.pg.embedder.FastDomainAngleEmbedder;
import be.ugent.caagt.pg.embedder.FastDomainEdgeEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedder2Zero;
import be.ugent.caagt.pg.embedder.energy.AngleEnergyCalculator;
import be.ugent.caagt.pg.embedder.energy.MeanEdgeLengthEnergyCalculator;
import be.ugent.caagt.pg.visualizer.gui.DefaultGraphListModel;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import be.ugent.caagt.pg.visualizer.gui.SaveDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author nvcleemp
 */
public class EmbedderRunner extends JPanel {

    private JProgressBar mainBar = new JProgressBar(){

        @Override
        public String getString() {
            return getValue() + " of " + getMaximum();
        }
        
    };
    private JProgressBar localBar = new JProgressBar(0, 660);
    private GraphListModel graphListModel;
    private boolean stopRequested;
    
    public EmbedderRunner(final File f, final Frame frame){
        graphListModel = new DefaultGraphListModel(f);
        graphListModel.getSelectionModel().setSelectionInterval(0, 0);
        stopRequested = false;
        mainBar.setMaximum(graphListModel.getSize());
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.bottom = 5;
        gbc.insets.top = 5;
        gbc.insets.left = 5;
        gbc.insets.right = 5;
        JButton startButton = new JButton("start");
        startButton.addActionListener(new RunActionListener());
        add(startButton, gbc);
        gbc.gridx = 1;
        JButton stopButton = new JButton("stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopRequested = true;
            }
        });
        add(stopButton, gbc);
        gbc.gridx = 2;
        final JButton saveButton = new JButton("save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(f.getParentFile());
                if(chooser.showSaveDialog(saveButton) == JFileChooser.APPROVE_OPTION){
                    try {
                        new SaveDialog(frame, chooser.getSelectedFile(), graphListModel).save();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        add(saveButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainBar.setStringPainted(true);
        add(mainBar, gbc);
        gbc.gridy = 2;
        localBar.setStringPainted(true);
        add(localBar, gbc);
    }
    
    private class RunActionListener implements ActionListener {

        private SpringEmbedder spring;
        private SpringEmbedder2Zero spring2;
        private FastDomainAngleEmbedder angle;
        private FastDomainEdgeEmbedder edge;
        
        public RunActionListener(){
            spring = new SpringEmbedder(graphListModel);
            spring2 = new SpringEmbedder2Zero(graphListModel);
            angle = new FastDomainAngleEmbedder(graphListModel, 0.1, 1, new MeanEdgeLengthEnergyCalculator());
            edge = new FastDomainEdgeEmbedder(graphListModel, 0.1, 1, new AngleEnergyCalculator());
        }

        public void actionPerformed(ActionEvent e) {
            stopRequested = false;
            Runnable run = new Runnable() {

                public void run() {
                    graphListModel.getSelectionModel().setSelectionInterval(0, 0);
                    int i = 0;
                    mainBar.setValue(i);
                    while(!stopRequested && i < graphListModel.getSize()){
                        graphListModel.getSelectionModel().setSelectionInterval(i, i);
                        int k = 0;
                        localBar.setValue(k);
                        for(int j = 0; j<20; j++){
                            spring.embed();
                            localBar.setValue(++k);
                        }
                        for(int j = 0; j<30; j++){
                            edge.embed();
                            localBar.setValue(++k);
                        }
                        spring.initialize();
                        for(int j = 0; j<20; j++){
                            spring.embed();
                            localBar.setValue(++k);
                            edge.embed();
                            localBar.setValue(++k);
                            angle.embed();
                            localBar.setValue(++k);
                        }
                        for(int j = 0; j<500; j++){
                            spring.embed();
                            localBar.setValue(++k);
                        }
                        for(int j = 0; j<50; j++){
                            spring2.embed();
                            localBar.setValue(++k);
                        }
                        mainBar.setValue(++i);
                    }
                }
            };
            Thread thread = new Thread(run);
            thread.start();
        }
        
    }
}
