/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul;

import azul.delaney.FundamentalPatch;
import azul.delaney.DelaneySymbol;
import azul.delaney.Utility;
import azul.io.FileFormatException;
import azul.io.IOManager;
import azul.toroidalembedder.gui.EmbedderWindow;
import azul.toroidalembedder.gui.TorusViewTest;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                    new EmbedderWindow(IOManager.readTorGraph("2|2 2|0 0;0 0|0 1 0 0;0 0 1 0;0 1 0 1;0 1 -1 0;0 1 -1 1;1 1 1 0")).setVisible(true);
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
                    new EmbedderWindow(IOManager.readTorGraph("8|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 1 0 0;1 2 0 0;2 3 0 0;3 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0")).setVisible(true);
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
                    //new EmbedderWindow(IOManager.readTorGraph("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
                    new EmbedderWindow(IOManager.readTorGraph("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
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
                    new EmbedderWindow(IOManager.readTorGraph("10|2 2|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 4 0 1;2 6 -1 0;3 5 -1 0;1 7 -1 0;8 9 0 0")).setVisible(true);
                } catch (FileFormatException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(button);
        button = new JButton("Embed ds");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    if(chooser.getSelectedFile().exists())
                        for(DelaneySymbol symbol: IOManager.readDS(chooser.getSelectedFile()))
                            try {
                                new EmbedderWindow(Utility.delaneyToTorGraph(symbol)).setVisible(true);
                            } catch (Exception exc){
                                symbol.printSymbol();
                                exc.printStackTrace();
                            } catch (Error er){
                                symbol.printSymbol();
                                er.printStackTrace();
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
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
