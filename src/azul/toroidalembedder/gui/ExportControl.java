/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.io.IOManager;
import azul.toroidalembedder.graph.Graph;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ExportControl extends JPanel{

    public ExportControl(final TorusView torusView, final Graph graph) {
        setLayout(new GridLayout(0, 2));
        JButton saveImage = new JButton("Save image");
        saveImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                torusView.exportImage();
            }
        });
        add(saveImage);
        JButton saveGraph = new JButton("Save graph");
        saveGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(IOManager.writePG(graph));
            }
        });
        add(saveGraph);
        setBorder(BorderFactory.createTitledBorder("Export"));
    }
    
    public ExportControl(final TorusView torusView, final GraphModel graphModel) {
        setLayout(new GridLayout(0, 1));
        JButton saveImage = new JButton("Save image");
        saveImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                torusView.exportImage();
            }
        });
        add(saveImage);
        JButton commitGraph = new JButton("Commit graph");
        commitGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphModel.commitSelectedGraph();
            }
        });
        add(commitGraph);
        JButton revertGraph = new JButton("Revert graph");
        revertGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphModel.revertSelectedGraph();
            }
        });
        add(revertGraph);
        final JButton saveGraphList = new JButton("Save graph list");
        saveGraphList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if(chooser.showSaveDialog(saveGraphList) == JFileChooser.APPROVE_OPTION){
                    try {
                        new SaveDialog(null, chooser.getSelectedFile(), graphModel).save();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(EmbedderRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        add(saveGraphList);
        setBorder(BorderFactory.createTitledBorder("Export"));
    }
    
    

}
