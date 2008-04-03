/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;


import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.GraphListener;
import azul.io.FileFormatException;
import azul.io.IOManager;
import azul.toroidalembedder.embedder.FastDomainAngleEmbedder;
import azul.toroidalembedder.embedder.FastDomainEdgeEmbedder;
import azul.toroidalembedder.energy.MeanEdgeLengthEnergyCalculator;
import azul.toroidalembedder.embedder.RandomEmbedder;
import azul.toroidalembedder.embedder.SpringEmbedder;
import azul.toroidalembedder.embedder.SpringEmbedderEqualEdges;

import azul.toroidalembedder.energy.AngleEnergyCalculator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author nvcleemp
 */
public class EmbedderWindow extends JFrame implements GraphListener {
    private Graph graph;
    private TorusView torusView = new TorusView(-2, -2, 2, 2);
    private JSplitPane split;

    public EmbedderWindow(Graph graph) {
        super("Toroidal embedder: EmbedderWindow");
        this.graph = graph;
        torusView.setGraph(graph);
        graph.addGraphListener(this);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(torusView);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // view controls
        JPanel viewPanel = new ViewController(torusView);
        
        // graph controls
        JPanel graphPanel = new GraphOperations(graph);
        
        // embedder controls
        JPanel embedderPanel = new EmbedderControl(graph);
        
        // export controls
        JPanel exportPanel = new JPanel(new GridLayout(0, 2));
        JButton saveImage = new JButton("Save image");
        saveImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                torusView.exportImage();
            }
        });
        exportPanel.add(saveImage);
        JButton saveGraph = new JButton("Save graph");
        saveGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(IOManager.writePG(EmbedderWindow.this.graph));
            }
        });
        exportPanel.add(saveGraph);
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export"));
        
        JPanel controls = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        controls.add(viewPanel, gbc);
        gbc.gridx = 1;
        controls.add(graphPanel, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 2;
        controls.add(embedderPanel, gbc);
        gbc.gridy = 1;
        controls.add(exportPanel, gbc);
        

        split.setBottomComponent(controls);
        setContentPane(split);
        pack();
    }
    
    public static void main(String[] args) {
        try {
            //new EmbedderWindow(IOManager.readPG("3|0 0;0 0;0 0|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1;2 1 1 0")).setVisible(true);
            new EmbedderWindow(IOManager.readPG("10|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
            new EmbedderWindow(IOManager.readPG("8|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 1 0 0;1 2 0 0;2 3 0 0;3 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0")).setVisible(true);
            TorusViewTest.main(args);
        } catch (FileFormatException ex) {
            Logger.getLogger(EmbedderWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void graphChanged() {
        split.repaint();
    }
    
    public void fundamentalDomainChanged(FundamentalDomain oldDomain) {
        split.repaint();
    }
    
    public void fundamentalDomainShapeChanged() {
        split.repaint();
    }
}
