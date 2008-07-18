/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg;

import be.ugent.caagt.pg.delaney.FundamentalPatch;
import be.ugent.caagt.pg.delaney.DelaneySymbol;
import be.ugent.caagt.pg.io.FileFormatException;
import be.ugent.caagt.pg.io.IOManager;
import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.caagt.pg.embedder.gui.EmbedderRunner;
import be.ugent.caagt.pg.visualizer.gui.EmbedderWindow;
import be.ugent.caagt.pg.visualizer.gui.PGVisualizer;
import be.ugent.caagt.pg.visualizer.gui.TorusViewTest;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class Test {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(0, 1, 10, 10));
        JButton button = new JButton("Embedder test: movable vertices");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TorusViewTest.main(new String[0]);
            }
        });
        frame.add(button);
        button = new JButton("Embedder test: triangles");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new EmbedderWindow(IOManager.readPG("2|2 2|0 0;0 0|0 1 0 0;0 0 1 0;0 1 0 1;0 1 -1 0;0 1 -1 1;1 1 1 0")).setVisible(true);
                } catch (FileFormatException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(button);
        button = new JButton("Embedder test: octagon tiling");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new EmbedderWindow(IOManager.readPG("8|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 1 0 0;1 2 0 0;2 3 0 0;3 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0")).setVisible(true);
                } catch (FileFormatException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(button);
        button = new JButton("Embedder test: azulene");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    //new EmbedderWindow(IOManager.readPG("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
                    new EmbedderWindow(IOManager.readPG("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
                } catch (FileFormatException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(button);
        button = new JButton("Embedder test: azulene 2");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new EmbedderWindow(IOManager.readPG("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 4 0 1;2 6 -1 0;3 5 -1 0;1 7 -1 0;8 9 0 0")).setVisible(true);
                } catch (FileFormatException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(button);
        button = new JButton("Create patch");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if(chooser.getSelectedFile().exists()){
                        int count = 0;
                        int succes = 0;
                        for(DelaneySymbol symbol: IOManager.readDS(chooser.getSelectedFile()))
                            try {
                                count++;
                                FundamentalPatch tree = FundamentalPatch.createBFSTree(symbol);
                                tree.print();
                                tree.closeOrbits();
                                tree.print();
                                succes++;
                            } catch (Exception exc){
                                
                            } catch (Error er){

                            }
                    }
            }
        });
        frame.add(button);
        button = new JButton("PGVisualizer");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser;
                String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
                if(dir==null)
                    chooser = new JFileChooser();
                else
                    chooser = new JFileChooser(new File(dir));
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if(chooser.getSelectedFile().exists()){
                        PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, chooser.getSelectedFile().getParent());
                        JFrame frame = new JFrame("PGVisualizer");
                        PGVisualizer visualizer = new PGVisualizer(chooser.getSelectedFile());
                        frame.add(visualizer);
                        frame.setJMenuBar(visualizer.getMenuBar(frame));
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setVisible(true);
                    }
                        
            }
        });
        frame.add(button);
        button = new JButton("PGEmbedder");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser;
                String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
                if(dir==null)
                    chooser = new JFileChooser();
                else
                    chooser = new JFileChooser(new File(dir));
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if(chooser.getSelectedFile().exists()){
                        PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, chooser.getSelectedFile().getParent());
                        JFrame frame = new JFrame("PGVisualizer");
                        EmbedderRunner embedder = new EmbedderRunner(chooser.getSelectedFile(), frame);
                        frame.add(embedder);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                        
            }
        });
        frame.add(button);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
