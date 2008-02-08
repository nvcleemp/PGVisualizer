/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;


import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.GraphListener;
import azul.io.FileFormatException;
import azul.io.IOManager;
import azul.toroidalembedder.SpringEmbedder;
import azul.toroidalembedder.SpringEmbedderEqualEdges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private int xView = 2;
    private int yView = 2;
    private Graph graph;
    private TorusView torusView = new TorusView(-2, -2, 2, 2);
    private EmbedderComboBoxModel model = new EmbedderComboBoxModel();
    private JSplitPane split;
    private ActionListener embedAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(model.getSelectedEmbedder()!=null)
                model.getSelectedEmbedder().embed();
        }
    };
    private Timer timer = new Timer(100, embedAction);
    private ChangeListener changeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton)(e.getSource());
            ButtonModel model = button.getModel();
            if (model != null) {
                if (model.isPressed()) {
                    if (!timer.isRunning()) {
                        timer.start();
                    }
                } else if (timer.isRunning()) {
                    timer.stop();
                }
            }
        }
    };

    public EmbedderWindow(Graph graph) {
        super("Toroidal embedder: EmbedderWindow");
        this.graph = graph;
        torusView.setGraph(graph, true);
        graph.addGraphListener(this);
        model.addEmbedder("Spring embedder", new SpringEmbedder(graph));
        model.setSelectedItem("Spring embedder");
        model.addEmbedder("Spring embedder equal edges", new SpringEmbedderEqualEdges(graph));
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(torusView);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // view controls
        JPanel viewPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        viewPanel.add(new JLabel("Horizontal", JLabel.LEFT), gbc);
        gbc.gridy = 1;
        viewPanel.add(new JLabel("Vertical", JLabel.LEFT), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        viewPanel.add(new JButton(new XAction(1)), gbc);
        gbc.gridx = 2;
        viewPanel.add(new JButton(new XAction(-1)), gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        viewPanel.add(new JButton(new YAction(1)), gbc);
        gbc.gridx = 2;
        viewPanel.add(new JButton(new YAction(-1)), gbc);
        viewPanel.setBorder(BorderFactory.createTitledBorder("View"));
        
        // embedder controls
        JPanel embedderPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        JComboBox comboBox = new JComboBox(model);
        comboBox.setPrototypeDisplayValue("Spring embedder equal edges");
        embedderPanel.add(comboBox, gbc);
        gbc.gridx = 1;
        JButton runEmbedder = new JButton("Run embedder");
        runEmbedder.addChangeListener(changeListener);
        timer.setInitialDelay(0);
        embedderPanel.add(runEmbedder, gbc);
        embedderPanel.setBorder(BorderFactory.createTitledBorder("Embedder"));
        
        // export controls
        JPanel exportPanel = new JPanel(new GridLayout(0, 2));
        JButton saveImage = new JButton("Save image");
        saveImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                torusView.exportImage();
            }
        });
        exportPanel.add(saveImage);
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export"));
        
        JPanel controls = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        controls.add(viewPanel, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 1;
        controls.add(embedderPanel, gbc);
        gbc.gridy = 1;
        controls.add(exportPanel, gbc);
        

        split.setBottomComponent(controls);
        setContentPane(split);
        pack();
    }
    
    public static void main(String[] args) {
        try {
            //new EmbedderWindow(IOManager.readTorGraph("3|0 0;0 0;0 0|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1;2 1 1 0")).setVisible(true);
            new EmbedderWindow(IOManager.readTorGraph("10|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 8 0 0;8 1 0 0;1 2 0 0;2 3 0 0;3 9 0 0;9 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0;8 9 0 0")).setVisible(true);
            new EmbedderWindow(IOManager.readTorGraph("8|0 0;0 0;0 0;0 0;0 0;0 0;0 0;0 0|0 1 0 0;1 2 0 0;2 3 0 0;3 4 0 0;4 5 0 0;5 6 0 0;6 7 0 0;7 0 0 0;0 5 0 -1;1 4 0 -1;2 7 1 0;3 6 1 0")).setVisible(true);
            TorusViewTest.main(args);
        } catch (FileFormatException ex) {
            Logger.getLogger(EmbedderWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void graphChanged() {
        split.repaint();
    }
    
    private class XAction extends AbstractAction {
        
        private int increment;

        public XAction(int increment) {
            super(increment>0 ? "+" : "-");
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            if(xView + increment > 0){
                xView+=increment;
                torusView.setView(-xView, -yView, xView, yView);
            }
        }
        
    }

    private class YAction extends AbstractAction {
        
        private int increment;

        public YAction(int increment) {
            super(increment>0 ? "+" : "-");
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            if(yView + increment > 0){
                yView+=increment;
                torusView.setView(-xView, -yView, xView, yView);
            }
        }
        
    }   
}
