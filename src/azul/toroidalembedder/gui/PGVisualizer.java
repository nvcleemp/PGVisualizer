/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.embedder.Embedder;
import azul.toroidalembedder.embedder.SpringEmbedder;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class PGVisualizer extends JPanel{
    
    private GraphModel model;
    private TorusView view = new TorusView();
    private Embedder embedder;
    private ActionListener embedAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(embedder!=null)
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
                        if(embedder!=null)
                            embedder.initialize();
                        timer.start();
                    }
                } else if (timer.isRunning()) {
                    timer.stop();
                }
            }
        }
    };
    public PGVisualizer(File file) {
        model = new GraphModel(file);
        final ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                view.setGraph(model.getGraph(selectionModel.getMinSelectionIndex()));
                embedder = new SpringEmbedder(model.getGraph(selectionModel.getMinSelectionIndex()));
            }
        });
        setLayout(new BorderLayout());
        add(new ListSelectionNavigator(selectionModel, model), BorderLayout.NORTH);
        add(view, BorderLayout.CENTER);
        JPanel controls = new JPanel();
        controls.add(new ViewController(view));
        final GraphOperations operations = new GraphOperations(model.getGraph(selectionModel.getMinSelectionIndex()));
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                operations.setGraph(model.getGraph(selectionModel.getMinSelectionIndex()));
            }
        });
        controls.add(operations);
        JButton run = new JButton("Run");
        run.addChangeListener(changeListener);
        controls.add(run);
        JButton output = new JButton("Output");
        output.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(model.exportUpdatedGraphs());
            }
        });
        controls.add(output);
        add(controls, BorderLayout.SOUTH);
    }
    
}