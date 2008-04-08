/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class PGVisualizer extends JPanel{
    
    private GraphModel model;
    private TorusView view = new TorusView();
    private JPanel controls;
    private JPanel embedderBatch;

    public PGVisualizer(File file) {
        model = new GraphModel(file);
        setLayout(new BorderLayout());
        add(new ListSelectionNavigator(model.getSelectionModel(), model), BorderLayout.NORTH);
        view = new TorusView(model);
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TorusView.ViewVertex vv = view.getVertexAt(e.getX(), e.getY());
                if(e.isShiftDown()){
                    if(vv!=null){
                        view.getGraphSelectionModel().toggleVertex(vv.vertex);
                    }
                } else {
                    view.getGraphSelectionModel().clearSelection();
                    if(vv!=null){
                        view.getGraphSelectionModel().addVertex(vv.vertex);
                    }
                }
            }            
        });
        add(view, BorderLayout.CENTER);
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(new ViewController(view));
        controls.add(new GraphOperations(model));
        controls.add(new EmbedderControl(model));
        controls.add(new ExportControl(view, model));
        add(controls, BorderLayout.EAST);
        embedderBatch = new EmbedderBatch(model);
        //embedderBatch.setVisible(false);
        add(embedderBatch, BorderLayout.WEST);       
    }
    
    public void toggleManualAuto(){
        boolean visible = embedderBatch.isVisible();
        embedderBatch.setVisible(!visible);
        controls.setVisible(visible);
    }
}
