/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder;

import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.GraphListener;
import azul.io.FileFormatException;
import azul.io.IOManager;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JButton;
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
    private int xView = 2;
    private int yView = 2;
    private Graph graph;
    private TorusView torusView = new TorusView(-2, -2, 2, 2);
    private Embedder embedder;
    private Embedder embedder1;
    private Embedder embedder2;
    private JSplitPane split;
    private ActionListener embedAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            embedder.embed();
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
                        if(button.getText().endsWith("2"))
                            embedder = embedder2;
                        else
                            embedder = embedder1;
                        embedAction.actionPerformed(new ActionEvent(button, 0, null));
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
        embedder1 = new SpringEmbedder(graph);
        embedder2 = new SpringEmbedderEqualEdges(graph);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(torusView);
        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.add(new JButton(new XAction(1)));
        panel.add(new JButton(new YAction(1)));
        panel.add(new JButton(new XAction(-1)));
        panel.add(new JButton(new YAction(-1)));
        JButton button = new JButton("Run embedder");
        button.addChangeListener(changeListener);
        timer.setInitialDelay(0);
        panel.add(button);
        button = new JButton("Run embedder 2");
        button.addChangeListener(changeListener);
        //panel.add(button);
        button = new JButton("Save image");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                torusView.exportImage();
            }
        });
        panel.add(button);
        split.setBottomComponent(panel);
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
            super("horizontal view " + (increment>0 ? "+" : "-"));
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
            super("vertical view " + (increment>0 ? "+" : "-"));
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            if(yView + increment > 0){
                yView+=increment;
                torusView.setView(-xView, -yView, xView, yView);
            }
        }
        
    }

    private class EmbedAction implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            embedder.embed();
        }
        
    }
    
}
